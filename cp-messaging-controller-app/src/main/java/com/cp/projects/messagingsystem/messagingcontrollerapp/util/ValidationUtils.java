package com.cp.projects.messagingsystem.messagingcontrollerapp.util;

import com.cp.projects.messagingsystem.messagingcontrollerapp.repository.ConversationRepository;
import com.cp.projects.messagingsystem.messagingcontrollerapp.repository.UserRepository;
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
    private final ConversationRepository conversationRepository;
    private final UserRepository userRepository;
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
        return userRepository.existsById(username)
            .switchIfEmpty(Mono.just(false));
    }

    public Mono<Boolean> isValidConversation(String targetConversation) {
        return conversationRepository.existsById(targetConversation)
            .switchIfEmpty(Mono.just(false));
    }
}
