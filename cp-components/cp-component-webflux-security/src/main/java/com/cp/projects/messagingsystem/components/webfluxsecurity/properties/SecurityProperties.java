package com.cp.projects.messagingsystem.components.webfluxsecurity.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@Data
@ConfigurationProperties("com.cp.projects.messaging-system.security-config")
public class SecurityProperties {
    private List<String> allowedOrigins;

    private List<String> allowedMethods;

    private List<String> exposedHeaders;

    private List<String> allowedHeaders;

    private boolean allowCredentials;

    private String jwtSecret;
}
