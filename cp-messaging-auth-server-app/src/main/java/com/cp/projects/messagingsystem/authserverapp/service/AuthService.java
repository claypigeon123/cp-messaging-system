package com.cp.projects.messagingsystem.authserverapp.service;

import com.cp.projects.messagingsystem.clients.reactive.aggregatesapp.client.AggregatesAppReactiveClient;
import com.cp.projects.messagingsystem.cpmessagingdomain.document.User;
import com.cp.projects.messagingsystem.cpmessagingdomain.exception.CpMessagingSystemException;
import com.cp.projects.messagingsystem.cpmessagingdomain.request.AuthRequest;
import com.cp.projects.messagingsystem.cpmessagingdomain.request.RegisterRequest;
import com.cp.projects.messagingsystem.cpmessagingdomain.response.AuthResponse;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import javax.crypto.SecretKey;
import java.time.*;
import java.util.ArrayList;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final AggregatesAppReactiveClient aggregatesClient;
    private final PasswordEncoder passwordEncoder;
    private final Clock clock;
    private final SecretKey secretKey;

    public Mono<AuthResponse> auth(AuthRequest request) {
        return aggregatesClient.findById(request.getUsername(), User.class)
            .switchIfEmpty(Mono.error(new CpMessagingSystemException(HttpStatus.UNAUTHORIZED.value())))
            .flatMap(aggregate -> verifyPassword(aggregate, request.getPassword()))
            .switchIfEmpty(Mono.error(new CpMessagingSystemException(HttpStatus.UNAUTHORIZED.value())))
            .map(this::generateTokenFor);
    }

    public Mono<Void> register(RegisterRequest request) {
        return Mono.just(new User(request.getUsername(), null, null, passwordEncoder.encode(request.getPassword()), request.getDisplayName(), new ArrayList<>(), false))
            .flatMap(user -> aggregatesClient.create(user, User.class))
            .then();
    }

    // ---

    private Mono<User> verifyPassword(User aggregate, String raw) {
        return passwordEncoder.matches(raw, aggregate.getPassword()) ? Mono.just(aggregate) : Mono.empty();
    }

    private AuthResponse generateTokenFor(User aggregate) {
        long now = OffsetDateTime.now(clock).toInstant().toEpochMilli();
        long until = now + 604800000;
        String token = Jwts.builder()
            .setSubject(aggregate.getId())
            .setIssuedAt(new Date(now))
            .setExpiration(new Date(until))
            .signWith(secretKey)
            .compact();

        return new AuthResponse("Bearer " + token, OffsetDateTime.ofInstant(Instant.ofEpochMilli(until), ZoneId.of(ZoneOffset.UTC.getId())));
    }
}
