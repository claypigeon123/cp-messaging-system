package com.cp.projects.messagingsystem.messagingcontrollerapp.model;

public enum WebSocketMessageType {
    SEND_MESSAGE,
    RECEIVED_MESSAGE,
    CREATE_CONVERSATION,
    ADDED_TO_CONVERSATION,

    SUCCESS_SIGNAL,
    ERROR_SIGNAL
}
