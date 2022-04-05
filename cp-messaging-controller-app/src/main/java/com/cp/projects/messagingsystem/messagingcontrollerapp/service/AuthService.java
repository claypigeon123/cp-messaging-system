package com.cp.projects.messagingsystem.messagingcontrollerapp.service;

import com.cp.projects.messagingsystem.cpmessagingdomain.exception.CpMessagingSystemException;
import com.cp.projects.messagingsystem.messagingcontrollerapp.model.document.Message;
import com.cp.projects.messagingsystem.messagingcontrollerapp.model.document.User;
import com.cp.projects.messagingsystem.cpmessagingdomain.request.AuthRequest;
import com.cp.projects.messagingsystem.cpmessagingdomain.request.RegisterRequest;
import com.cp.projects.messagingsystem.cpmessagingdomain.response.AuthResponse;
import com.cp.projects.messagingsystem.messagingcontrollerapp.model.ws.WebSocketMessageType;
import com.cp.projects.messagingsystem.messagingcontrollerapp.repository.UserRepository;
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
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final Clock clock;
    private final SecretKey secretKey;

    public Mono<AuthResponse> auth(AuthRequest request) {
        return userRepository.findById(request.getUsername())
            .switchIfEmpty(Mono.error(new CpMessagingSystemException(HttpStatus.UNAUTHORIZED.value())))
            .flatMap(aggregate -> verifyPassword(aggregate, request.getPassword()))
            .switchIfEmpty(Mono.error(new CpMessagingSystemException(HttpStatus.UNAUTHORIZED.value())))
            .map(this::generateTokenFor);
    }

    public Mono<Void> register(RegisterRequest request) {
        if (!request.getPassword().equals(request.getConfirmPassword())) {
            throw new CpMessagingSystemException("Provided passwords didn't match", HttpStatus.BAD_REQUEST.value());
        }

        OffsetDateTime now = OffsetDateTime.now(clock);
        return userRepository.existsById(request.getUsername())
            .flatMap(isPresent -> isPresent ? Mono.error(new CpMessagingSystemException("Username already taken", HttpStatus.CONFLICT.value())) : Mono.just(request))
            .flatMap(req -> userRepository.save(new User(
                req.getUsername(),
                now,
                now,
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
            .setSubject(aggregate.getId())
            .setIssuedAt(new Date(now))
            .setExpiration(new Date(until))
            .signWith(secretKey)
            .compact();

        return new AuthResponse("Bearer " + token, OffsetDateTime.ofInstant(Instant.ofEpochMilli(until), ZoneId.of(ZoneOffset.UTC.getId())));
    }
}