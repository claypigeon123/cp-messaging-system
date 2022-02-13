package com.cp.projects.messagingsystem.messagingserver.util;

import com.cp.projects.messagingsystem.messagingserver.repository.UserRepository;
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
public class AuthUtils {
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

    public boolean isValidUser(String username) {
        return Boolean.TRUE.equals(userRepository.existsByUsername(username)
            .switchIfEmpty(Mono.just(false))
            .block());
    }

    public Optional<Claims> isValidToken(String value) {
        return isValidToken(value, true);
    }
}
