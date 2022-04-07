package com.cp.projects.messagingsystem.clients.reactive.aggregatesapp.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties("com.cp.projects.messaging-system.reactive-clients.cp-messaging-aggregates-app")
public class AggregatesAppProperties {
    private String baseUri;

    private String messagesPathVariable;
    private String conversationsPathVariable;
    private String usersPathVariable;

    private String healthCheckUri;
    private String aggregatesUri;
    private String aggregatesIdUri;
    private String findByIdUri;
    private String findAllByIdsUri;
}
