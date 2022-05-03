package com.cp.projects.messagingsystem.ui.desktopapp.websocket;

import com.cp.projects.messagingsystem.cpmessagingdomain.document.Message;
import com.cp.projects.messagingsystem.cpmessagingdomain.util.WebSocketMessageParser;
import com.cp.projects.messagingsystem.cpmessagingdomain.ws.WebSocketOperation;
import com.cp.projects.messagingsystem.ui.desktopapp.controller.BaseSceneController;
import javafx.application.Platform;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class MessagingWebSocketHandler implements WebSocketHandler {

    private final BaseSceneController baseSceneController;
    private final WebSocketMessageParser wsParser;

    @Override
    public Mono<Void> handle(WebSocketSession session) {
        Mono<Void> incoming = session.receive()
            .map(webSocketMessage -> wsParser.parseWebsocketMessage(webSocketMessage, WebSocketOperation.class))
            .doOnNext(System.out::println)
            .map(raw -> wsParser.parsePayload(raw, Message.class))
            .doOnNext(parsed -> Platform.runLater(() -> baseSceneController.addMessage(parsed)))
            .then();

        return incoming
            .doFirst(() -> System.out.println("Connected"))
            .then()
            .doFinally(signal -> System.out.println("Disconnected"));
    }

}
