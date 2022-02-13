package com.cp.projects.messagingsystem.messagingserver.api;

import com.cp.projects.messagingsystem.messagingserver.model.User;
import com.cp.projects.messagingsystem.messagingserver.model.request.AuthRequest;
import com.cp.projects.messagingsystem.messagingserver.model.request.RegisterRequest;
import com.cp.projects.messagingsystem.messagingserver.model.response.AuthResponse;
import com.cp.projects.messagingsystem.messagingserver.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {
    private static final Logger log = LoggerFactory.getLogger(AuthController.class);

    private final AuthService authService;

    @PostMapping("/get-token")
    public Mono<AuthResponse> auth(@RequestBody @Valid AuthRequest request) {
        log.info("Request to authenticate {}", request.getUsername());
        return authService.auth(request);
    }

    @PostMapping("/register")
    public Mono<Void> register(@RequestBody @Valid RegisterRequest request) {
        log.info("Request to register new user");
        return authService.register(request);
    }
}
