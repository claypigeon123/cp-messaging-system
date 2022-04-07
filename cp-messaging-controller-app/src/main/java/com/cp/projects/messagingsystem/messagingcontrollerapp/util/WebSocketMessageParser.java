package com.cp.projects.messagingsystem.messagingcontrollerapp.util;

import com.cp.projects.messagingsystem.cpmessagingdomain.exception.CpMessagingSystemException;
import com.cp.projects.messagingsystem.messagingcontrollerapp.model.WebSocketOperation;
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

    public <T> T parseWebsocketMessage(WebSocketMessage raw, Class<T> clazz) {
        T parsed;
        try {
            parsed = objectMapper.readValue(raw.getPayloadAsText(), clazz);
        } catch (JsonProcessingException e) {
            throw new CpMessagingSystemException("Error while deserializing websocket message", e);
        }

        return parsed;
    }

    public <T> T parsePayload(WebSocketOperation raw, Class<T> clazz) {
        T parsed;
        try {
            parsed = objectMapper.convertValue(raw.getPayload(), clazz);
        } catch (IllegalArgumentException e) {
            throw new CpMessagingSystemException("Error while deserializing message payload", e);
        }

        return parsed;
    }

    public <T> WebSocketMessage createWebsocketMessage(WebSocketSession session, T payload) {
        WebSocketMessage wsMessage;
        try {
            wsMessage = session.textMessage(objectMapper.writeValueAsString(payload));
        } catch (JsonProcessingException e) {
            throw new CpMessagingSystemException("Error during websocket message serialization", e);
        }

        return wsMessage;
    }
}
