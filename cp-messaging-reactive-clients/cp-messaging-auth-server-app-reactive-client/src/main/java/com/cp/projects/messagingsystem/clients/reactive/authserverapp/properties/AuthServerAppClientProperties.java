package com.cp.projects.messagingsystem.clients.reactive.authserverapp.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties("com.cp.projects.messaging-system.reactive-clients.cp-messaging-auth-server-app")
public class AuthServerAppClientProperties {
    private String baseUri;

    private String healthCheckUri;
    private String registerUri;
    private String getTokenUri;
}
