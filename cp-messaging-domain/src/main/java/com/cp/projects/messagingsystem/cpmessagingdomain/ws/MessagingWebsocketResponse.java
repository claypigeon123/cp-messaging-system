package com.cp.projects.messagingsystem.cpmessagingdomain.ws;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
public class MessagingWebsocketResponse {
    private boolean success;
    private String reason;

    public static MessagingWebsocketResponse ofSuccess() {
        return new MessagingWebsocketResponse(true, null);
    }

    public static MessagingWebsocketResponse ofFailure(String reason) {
        return new MessagingWebsocketResponse(false, reason);
    }
}
