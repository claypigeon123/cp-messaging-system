package com.cp.projects.messagingsystem.clients.reactive.authserverapp.client;

import com.cp.projects.messagingsystem.clients.reactive.authserverapp.properties.AuthServerAppProperties;
import com.cp.projects.messagingsystem.cpmessagingdomain.request.AuthRequest;
import com.cp.projects.messagingsystem.cpmessagingdomain.request.RegisterRequest;
import com.cp.projects.messagingsystem.cpmessagingdomain.response.AuthResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import java.net.URI;

@RequiredArgsConstructor
public class AuthServerAppReactiveClient {
    private final AuthServerAppProperties props;
    private final WebClient webClient;

    public Mono<Void> register(RegisterRequest request) {
        URI uri = UriComponentsBuilder.fromUriString(props.getRegisterUri()).build().toUri();

        return webClient.post()
            .uri(uri)
            .body(BodyInserters.fromValue(request))
            .retrieve()
            .bodyToMono(Void.class);
    }

    public Mono<AuthResponse> getToken(AuthRequest request) {
        URI uri = UriComponentsBuilder.fromUriString(props.getGetTokenUri()).build().toUri();

        return webClient.post()
            .uri(uri)
            .body(BodyInserters.fromValue(request))
            .retrieve()
            .bodyToMono(AuthResponse.class);
    }
}
