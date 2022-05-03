package com.cp.projects.messagingsystem.clients.reactive.controllerapp.client;

import com.cp.projects.messagingsystem.clients.reactive.controllerapp.properties.ControllerAppClientProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.client.WebSocketClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import java.net.URI;

@RequiredArgsConstructor
public class ControllerAppReactiveClient {
    private final ControllerAppClientProperties props;
    private final WebClient webClient;
    private final WebSocketClient webSocketClient;

    public Mono<Void> connect(String token, WebSocketHandler handler) {
        URI uri = UriComponentsBuilder.fromUriString(props.getWsMessagesUri()).build().toUri();
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, token);

        return webSocketClient.execute(uri, headers, handler);
    }

}
