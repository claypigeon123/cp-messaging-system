package com.cp.projects.messagingsystem.messagingserver.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties("com.cp.projects.messaging-system.messaging-server.mongo-config")
public class MongoProperties {
    private String databaseName;
    private String connectionString;
}
