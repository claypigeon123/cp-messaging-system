package com.cp.projects.messagingsystem.messagingserver.service;

import com.cp.projects.messagingsystem.messagingserver.util.JwtUtils;
import com.cp.projects.messagingsystem.messagingserver.util.WebSocketMessageParser;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.function.Supplier;

@Service
@RequiredArgsConstructor
public class MessagingService implements WebSocketHandler {
    private static final Logger log = LoggerFactory.getLogger(MessagingService.class);

    private final JwtUtils jwtUtils;
    private final WebSocketMessageParser wsParser;

    @Override
    public Mono<Void> handle(WebSocketSession session) {
        List<String> values = session.getHandshakeInfo().getHeaders().get("Authorization");
        if (values == null) {
            throw new RuntimeException("No token supplied");
        }
        String token = values.stream().findFirst().orElseThrow();

        Claims claims = jwtUtils.isValidToken(token).orElseThrow();

        log.info("{} has connected to the messaging service", claims.getSubject());

        return Mono.empty();

        /*Flux<WebSocketMessage> messages = messagesSink.asFlux()
            .map(msg -> wsParser.createWebsocketMessage(session, msg));

        session.receive()
            .handle(wsParser::parseMessage)
            .subscribeWith(new Subscriber<Message>() {
                @Override
                public void onSubscribe(Subscription s) {}
                @Override
                public void onNext(Message message) {
                    messagesSink.tryEmitNext(message);
                }
                @Override
                public void onError(Throwable t) {
                    messagesSink.tryEmitError(t);
                }
                @Override
                public void onComplete() {
                    messagesSink.tryEmitComplete();
                }
            });

        return*/
    }
}
