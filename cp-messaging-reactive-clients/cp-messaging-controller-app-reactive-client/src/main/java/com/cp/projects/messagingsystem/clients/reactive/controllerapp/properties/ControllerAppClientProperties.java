package com.cp.projects.messagingsystem.clients.reactive.controllerapp.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties("com.cp.projects.messaging-system.reactive-clients.cp-messaging-controller-app")
public class ControllerAppClientProperties {
    private String baseUri;
    private String baseUriWs;

    private String wsMessagesUri;
}
