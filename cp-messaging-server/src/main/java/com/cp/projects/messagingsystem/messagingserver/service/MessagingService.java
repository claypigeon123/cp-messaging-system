package com.cp.projects.messagingsystem.messagingserver.service;

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
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class MessagingService implements WebSocketHandler {
    @Data
    @AllArgsConstructor
    private static class SessionData {
        private WebSocketSession session;
        private Sinks.Many<WebSocketMessage> outgoingSink;
    }
    private static final Logger log = LoggerFactory.getLogger(MessagingService.class);
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
        if (values == null) {
            throw new RuntimeException("No token supplied");
        }
        String token = values.stream().findFirst().orElseThrow();

        Claims claims = authUtils.isValidToken(token).orElseThrow();
        String senderUsername = claims.getSubject();

        if (!authUtils.isValidUser(senderUsername)) {
            throw new RuntimeException("No such user");
        }

        // Outgoing message source
        Sinks.Many<WebSocketMessage> outgoingSink = Sinks.many().unicast().onBackpressureBuffer();
        Mono<Void> outgoing = session.send(outgoingSink.asFlux());

        // Incoming message source
        Mono<Void> incoming = session.receive()
            .map(wsMessage -> broadcast(wsMessage, senderUsername, outgoingSink, session))
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
    }

    private Sinks.EmitResult broadcast(WebSocketMessage wsMessage, String senderUsername, Sinks.Many<WebSocketMessage> outgoingSink, WebSocketSession localSession) {
        try {
            Message msg = wsParser.parseMessage(wsMessage, Message.class);
            msg.setSender(senderUsername);
            String targetUsername = msg.getReceiver();
            if (!authUtils.isValidUser(targetUsername)) {
                throw new RuntimeException("No such user");
            }
            if (userMap.containsKey(targetUsername)) {
                SessionData targetSessionData = userMap.get(targetUsername);
                Sinks.Many<WebSocketMessage> targetSink = targetSessionData.getOutgoingSink();
                WebSocketMessage sentMessage = wsParser.createWebsocketMessage(targetSessionData.getSession(), msg);
                return targetSink.tryEmitNext(sentMessage);
            }
        } catch (Exception e) {
            log.error("Error", e);
            outgoingSink.tryEmitNext(localSession.textMessage(e.getMessage()));
        }
        return Sinks.EmitResult.FAIL_TERMINATED;
    }
}
