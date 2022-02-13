package com.cp.projects.messagingsystem.messagingserver.util;

import com.cp.projects.messagingsystem.messagingserver.model.Message;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.SynchronousSink;

@Component
@RequiredArgsConstructor
public class WebSocketMessageParser {
    private final ObjectMapper objectMapper;

    public void parseMessage(WebSocketMessage raw, SynchronousSink<Message> sink) {
        Message parsed;
        try {
            parsed = objectMapper.readValue(raw.getPayloadAsText(), Message.class);
        } catch (JsonProcessingException e) {
            sink.error(new RuntimeException("Error while deserializing websocket message", e));
            return;
        }
        sink.next(parsed);
        sink.complete();
    }

    public <T> WebSocketMessage createWebsocketMessage(WebSocketSession session, T payload) {
        WebSocketMessage wsMessage;
        try {
            wsMessage = session.textMessage(objectMapper.writeValueAsString(payload));
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error during websocket message serialization", e);
        }

        return wsMessage;
    }
}
