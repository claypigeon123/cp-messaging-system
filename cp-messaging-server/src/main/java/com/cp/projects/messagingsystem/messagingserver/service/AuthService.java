package com.cp.projects.messagingsystem.messagingserver.service;

import com.cp.projects.messagingsystem.messagingserver.model.document.User;
import com.cp.projects.messagingsystem.messagingserver.model.request.AuthRequest;
import com.cp.projects.messagingsystem.messagingserver.model.request.RegisterRequest;
import com.cp.projects.messagingsystem.messagingserver.model.response.AuthResponse;
import com.cp.projects.messagingsystem.messagingserver.repository.UserRepository;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import javax.crypto.SecretKey;
import java.time.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final Clock clock;
    private final SecretKey secretKey;

    public Mono<AuthResponse> auth(AuthRequest request) {
        return userRepository.findByUsername(request.getUsername())
            .switchIfEmpty(Mono.error(new RuntimeException()))
            .flatMap(aggregate -> verifyPassword(aggregate, request.getPassword()))
            .switchIfEmpty(Mono.error(new RuntimeException()))
            .map(this::generateTokenFor);
    }

    public Mono<Void> register(RegisterRequest request) {
        if (!request.getPassword().equals(request.getConfirmPassword())) {
            throw new RuntimeException();
        }

        return userRepository.existsByUsername(request.getUsername())
            .flatMap(isPresent -> isPresent ? Mono.error(new RuntimeException()) : Mono.just(request))
            .flatMap(req -> userRepository.save(new User(
                UUID.randomUUID().toString(),
                OffsetDateTime.now(clock),
                OffsetDateTime.now(clock),
                req.getUsername(),
                passwordEncoder.encode(req.getPassword()),
                req.getDisplayName(),
                new ArrayList<>(),
                false
            ))).then();
    }

    // ---

    private Mono<User> verifyPassword(User aggregate, String raw) {
        return passwordEncoder.matches(raw, aggregate.getPassword()) ? Mono.just(aggregate) : Mono.empty();
    }

    private AuthResponse generateTokenFor(User aggregate) {
        long now = OffsetDateTime.now(clock).toInstant().toEpochMilli();
        long until = now + 604800000;
        String token = Jwts.builder()
            .setSubject(aggregate.getUsername())
            .setIssuedAt(new Date(now))
            .setExpiration(new Date(until))
            .signWith(secretKey)
            .compact();

        return new AuthResponse("Bearer " + token, OffsetDateTime.ofInstant(Instant.ofEpochMilli(until), ZoneId.of(ZoneOffset.UTC.getId())));
    }
}
