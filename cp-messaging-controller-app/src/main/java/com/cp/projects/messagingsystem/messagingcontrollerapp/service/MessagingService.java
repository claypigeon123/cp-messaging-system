package com.cp.projects.messagingsystem.messagingcontrollerapp.service;

import com.cp.projects.messagingsystem.clients.reactive.aggregatesapp.client.AggregatesAppReactiveClient;
import com.cp.projects.messagingsystem.cpmessagingdomain.document.User;
import com.cp.projects.messagingsystem.cpmessagingdomain.exception.CpMessagingSystemException;
import com.cp.projects.messagingsystem.cpmessagingdomain.document.Conversation;
import com.cp.projects.messagingsystem.cpmessagingdomain.document.Message;
import com.cp.projects.messagingsystem.messagingcontrollerapp.model.WebSocketMessageType;
import com.cp.projects.messagingsystem.messagingcontrollerapp.model.WebSocketOperation;
import com.cp.projects.messagingsystem.messagingcontrollerapp.util.ValidationUtils;
import com.cp.projects.messagingsystem.messagingcontrollerapp.util.WebSocketMessageParser;
import io.jsonwebtoken.Claims;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;

import java.time.Clock;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class MessagingService implements WebSocketHandler {
    private static final Logger log = LoggerFactory.getLogger(MessagingService.class);
    private static final String NO_VALID_AUTH = "No valid authorization provided";

    @Data
    @AllArgsConstructor
    private static class SessionData {
        private WebSocketSession session;
        private Sinks.Many<WebSocketMessage> outgoingSink;
    }

    private final Map<String, SessionData> userMap = new ConcurrentHashMap<>();

    private final Clock clock;
    private final AggregatesAppReactiveClient aggregatesClient;

    private final ValidationUtils validationUtils;
    private final WebSocketMessageParser wsParser;

    @Scheduled(fixedRate = 5, timeUnit = TimeUnit.MINUTES)
    public void stats() {
        log.info("[MESSAGING SERVICE MONITORING] Users connected: {}", userMap.size());
    }

    @Override
    public Mono<Void> handle(WebSocketSession session) {
        List<String> values = session.getHandshakeInfo().getHeaders().get("Authorization");
        if (values == null || values.size() != 1) {
            return refuseConnection(session);
        }

        String token = values.get(0);

        Optional<Claims> claims = validationUtils.isValidToken(token);
        if (claims.isEmpty()) {
            return refuseConnection(session);
        }

        String senderUsername = claims.get().getSubject();
        return aggregatesClient.findById(senderUsername, User.class)
            .switchIfEmpty(Mono.error(new CpMessagingSystemException("Sender does not exist", 404)))
            .flatMap(sender -> {
                // Outgoing message source
                Sinks.Many<WebSocketMessage> outgoingSink = Sinks.many().unicast().onBackpressureBuffer();
                Mono<Void> outgoing = session.send(outgoingSink.asFlux());

                // Incoming message source
                Mono<Void> incoming = session.receive()
                    .map(wsMessage -> wsParser.parseWebsocketMessage(wsMessage, WebSocketOperation.class))
                    .doOnNext(operation -> {
                        if (operation.getType() == WebSocketMessageType.CREATE_CONVERSATION) {
                            Conversation conversation = wsParser.parsePayload(operation, Conversation.class);
                            createConversation(conversation, outgoingSink, session);
                        } else if (operation.getType() == WebSocketMessageType.SEND_MESSAGE) {
                            Message message = wsParser.parsePayload(operation, Message.class);
                            broadcast(message, sender.getId(), outgoingSink, session);
                        }
                    })
                    .then();

                return Mono.zip(incoming, outgoing)
                    .doFirst(() -> userMap.put(sender.getId(), new SessionData(session, outgoingSink)))
                    .then()
                    .doFinally(signal -> userMap.remove(sender.getId()));
            })
            .onErrorResume(throwable -> refuseConnection(session, senderUsername));
    }

    // --

    private void createConversation(Conversation conversation, Sinks.Many<WebSocketMessage> localOutgoingSink, WebSocketSession localSession) {
        OffsetDateTime now = OffsetDateTime.now(clock);
        conversation.setId(UUID.randomUUID().toString());
        conversation.setCreatedDate(now);
        conversation.setUpdatedDate(now);

        aggregatesClient.create(conversation, Conversation.class)
            .switchIfEmpty(Mono.error(new CpMessagingSystemException("Could not create conversation")))
            .flatMap(savedConversation -> spread(savedConversation, new WebSocketOperation(WebSocketMessageType.ADDED_TO_CONVERSATION, conversation)))
            .onErrorResume(throwable -> {
                if (throwable instanceof CpMessagingSystemException cpmse) {
                    localOutgoingSink.tryEmitNext(wsParser.createWebsocketMessage(localSession, new WebSocketOperation(WebSocketMessageType.ERROR_SIGNAL, cpmse.getMessage())));
                }

                return Mono.empty();
            })
            .subscribe();
    }

    private void broadcast(Message msg, String senderUsername, Sinks.Many<WebSocketMessage> localOutgoingSink, WebSocketSession localSession) {
        OffsetDateTime now = OffsetDateTime.now(clock);
        msg.setId(UUID.randomUUID().toString());
        msg.setCreatedDate(now);
        msg.setUpdatedDate(now);
        msg.setSenderId(senderUsername);

        String targetConversation = msg.getConversationId();

        // Find the targeted conversation
        aggregatesClient.findById(targetConversation, Conversation.class)
            // Switch to exception if no such conversation exists
            .switchIfEmpty(Mono.error(new CpMessagingSystemException("Conversation does not exist", 404)))

            // Save the message, tuple the result up with the already found conversation
            .flatMap(conversation -> Mono.zip(Mono.just(conversation), aggregatesClient.create(msg, Message.class)))

            // If saving the message was a success, send success feedback to the client
            .doOnSuccess(tuple -> localOutgoingSink.tryEmitNext(wsParser.createWebsocketMessage(localSession, new WebSocketOperation(WebSocketMessageType.SUCCESS_SIGNAL))))

            // Broadcast the message to people in the conversation who are connected to the messaging service
            .flatMap(tuple -> spread(tuple.getT1(), new WebSocketOperation(WebSocketMessageType.RECEIVED_MESSAGE, tuple.getT2())))

            // If error along the way, let the client know
            .onErrorResume(throwable -> {
                if (throwable instanceof CpMessagingSystemException cpmse) {
                    localOutgoingSink.tryEmitNext(wsParser.createWebsocketMessage(localSession, new WebSocketOperation(WebSocketMessageType.ERROR_SIGNAL, cpmse.getMessage())));
                }

                return Mono.empty();
            })
            .subscribe();
    }

    private Mono<Void> spread(Conversation conversation, WebSocketOperation op) {
        return aggregatesClient.findAllByIds(conversation.getUserIds(), User.class)
            .flatMap(user -> userMap.containsKey(user.getId()) ? Mono.just(userMap.get(user.getId())) : Mono.empty())
            .doOnNext(targetSessionData -> {
                Sinks.Many<WebSocketMessage> targetSink = targetSessionData.getOutgoingSink();
                WebSocketMessage sentMessage = wsParser.createWebsocketMessage(targetSessionData.getSession(), op);
                targetSink.tryEmitNext(sentMessage);
            })
            .then();
    }

    private Mono<Void> refuseConnection(WebSocketSession session) {
        return refuseConnection(session, null);
    }

    private Mono<Void> refuseConnection(WebSocketSession session, String username) {
        if (username != null) {
            log.info("Refused connection of {}", username);
        }
        return session.send(Mono.just(wsParser.createWebsocketMessage(session, new WebSocketOperation(WebSocketMessageType.ERROR_SIGNAL, NO_VALID_AUTH))));
    }
}
