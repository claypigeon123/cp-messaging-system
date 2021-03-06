package com.cp.projects.messagingsystem.authserverapp.api;

import com.cp.projects.messagingsystem.authserverapp.service.AuthService;
import com.cp.projects.messagingsystem.authserverapp.validation.annotation.ValidRegisterRequest;
import com.cp.projects.messagingsystem.cpmessagingdomain.request.AuthRequest;
import com.cp.projects.messagingsystem.cpmessagingdomain.request.RegisterRequest;
import com.cp.projects.messagingsystem.cpmessagingdomain.response.AuthResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
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
        log.debug("Request to authenticate {}", request.getUsername());
        return authService.auth(request);
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Void> register(@RequestBody @Valid @ValidRegisterRequest RegisterRequest request) {
        log.debug("Request to register new user");
        return authService.register(request);
    }
}
