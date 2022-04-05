package com.cp.projects.messagingsystem.components.webfluxmongostarter.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties("com.cp.projects.messaging-system.mongo-config")
public class MongoProperties {
    private String username;
    private String password;
    private String hostUri;
    private String port;
    private String authSource;
    private String databaseName;
    private String connectionString;
}
