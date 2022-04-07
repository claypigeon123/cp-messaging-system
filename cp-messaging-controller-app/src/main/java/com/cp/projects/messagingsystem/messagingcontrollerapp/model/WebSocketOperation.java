package com.cp.projects.messagingsystem.messagingcontrollerapp.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class WebSocketOperation {
    private WebSocketMessageType type;

    private Object payload;

    public WebSocketOperation(WebSocketMessageType type) {
        this.type = type;
        this.payload = null;
    }

    public WebSocketOperation(WebSocketMessageType type, Object payload) {
        this.type = type;
        this.payload = payload;
    }
}
