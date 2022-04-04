package com.cp.projects.messagingsystem.messagingserver.service;

import com.cp.projects.messagingsystem.cpmessagingdomain.ws.MessagingWebsocketResponse;
import com.cp.projects.messagingsystem.messagingserver.model.document.Message;
import com.cp.projects.messagingsystem.messagingserver.repository.MessageRepository;
import com.cp.projects.messagingsystem.messagingserver.util.AuthUtils;
import com.cp.projects.messagingsystem.messagingserver.util.WebSocketMessageParser;
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

import java.util.List;
import java.util.Map;
import java.util.Optional;
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

    private final MessageRepository messageRepository;
    private final AuthUtils authUtils;
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

        Optional<Claims> claims = authUtils.isValidToken(token);
        if (claims.isEmpty()) {
            return refuseConnection(session);
        }

        String senderUsername = claims.get().getSubject();
        return authUtils.isValidUser(senderUsername)
            .flatMap(isValid -> {
                if (!isValid) {
                    return refuseConnection(session, senderUsername);
                }

                // Outgoing message source
                Sinks.Many<WebSocketMessage> outgoingSink = Sinks.many().unicast().onBackpressureBuffer();
                Mono<Void> outgoing = session.send(outgoingSink.asFlux());

                // Incoming message source
                Mono<Void> incoming = session.receive()
                    .map(wsMessage -> wsParser.parseMessage(wsMessage, Message.class))
                    .doOnNext(message -> broadcast(message, senderUsername, outgoingSink, session))
                    .then();

                return Mono.zip(incoming, outgoing)
                    .doFirst(() -> {
                        userMap.put(senderUsername, new SessionData(session, outgoingSink));
                        log.info("{} has connected to the messaging service", senderUsername);
                    })
                    .then()
                    .doFinally(signal -> {
                        userMap.remove(senderUsername);
                        log.info("{} has disconnected from the messaging service", senderUsername);
                    });
            });
    }

    // --

    private void broadcast(Message msg, String senderUsername, Sinks.Many<WebSocketMessage> outgoingSink, WebSocketSession localSession) {
        msg.setSender(senderUsername);
        String targetUsername = msg.getReceiver();

        authUtils.isValidUser(targetUsername)
            .doOnSuccess(isValid -> {
                if (!isValid) {
                    outgoingSink.tryEmitNext(wsParser.createWebsocketMessage(localSession, MessagingWebsocketResponse.ofFailure("Recipient doesn't exist in the system")));
                    return;
                }

                if (userMap.containsKey(targetUsername)) {
                    SessionData targetSessionData = userMap.get(targetUsername);
                    Sinks.Many<WebSocketMessage> targetSink = targetSessionData.getOutgoingSink();
                    WebSocketMessage sentMessage = wsParser.createWebsocketMessage(targetSessionData.getSession(), msg);
                    targetSink.tryEmitNext(sentMessage);
                }
            })
            .then()
            .subscribe();
    }

    private Mono<Void> refuseConnection(WebSocketSession session) {
        return refuseConnection(session, null);
    }

    private Mono<Void> refuseConnection(WebSocketSession session, String username) {
        if (username != null) {
            log.info("Refused connection of {}", username);
        }
        return session.send(Mono.just(wsParser.createWebsocketMessage(session, MessagingWebsocketResponse.ofFailure(NO_VALID_AUTH))));
    }
}
