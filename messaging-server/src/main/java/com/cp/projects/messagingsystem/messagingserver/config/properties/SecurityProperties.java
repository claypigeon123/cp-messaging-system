package com.cp.projects.messagingsystem.messagingserver.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@Data
@ConfigurationProperties(prefix = "com.cp.projects.messaging-system.messaging-server.security-config")
public class SecurityProperties {
    private List<String> allowedOrigins;

    private List<String> allowedMethods;

    private List<String> exposedHeaders;

    private List<String> allowedHeaders;

    private boolean allowCredentials;

    private String jwtSecret;
}
