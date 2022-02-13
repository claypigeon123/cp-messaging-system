package com.cp.projects.messagingsystem.messagingserver.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.WebSocketSession;

@Component
@RequiredArgsConstructor
public class WebSocketMessageParser {
    private final ObjectMapper objectMapper;

    public <T> T parseMessage(WebSocketMessage raw, Class<T> clazz) {
        T parsed;
        try {
            parsed = objectMapper.readValue(raw.getPayloadAsText(), clazz);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error while deserializing websocket message", e);
        }

        return parsed;
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
