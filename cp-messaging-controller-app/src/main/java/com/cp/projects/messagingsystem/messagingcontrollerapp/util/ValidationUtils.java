package com.cp.projects.messagingsystem.messagingcontrollerapp.util;

import com.cp.projects.messagingsystem.clients.reactive.aggregatesapp.client.AggregatesAppReactiveClient;
import com.cp.projects.messagingsystem.cpmessagingdomain.document.Conversation;
import com.cp.projects.messagingsystem.cpmessagingdomain.document.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import javax.crypto.SecretKey;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ValidationUtils {
    private final AggregatesAppReactiveClient aggregatesClient;
    private final SecretKey secretKey;

    public Optional<Claims> isValidToken(String value, boolean withPrefix) {
        String token = withPrefix ? value.split(" ")[1] : value;

        try {
            return Optional.of(Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody());
        } catch (JwtException e) {
            return Optional.empty();
        }
    }

    public Optional<Claims> isValidToken(String value) {
        return isValidToken(value, true);
    }

    public Mono<Boolean> isValidUser(String username) {
        return aggregatesClient.findById(username, User.class)
            .map(user -> Boolean.TRUE)
            .onErrorResume(th -> Mono.just(Boolean.FALSE));
    }

    public Mono<Boolean> isValidConversation(String targetConversation) {
        return aggregatesClient.findById(targetConversation, Conversation.class)
            .map(conversation -> Boolean.TRUE)
            .onErrorResume(th -> Mono.just(Boolean.FALSE));
    }
}
