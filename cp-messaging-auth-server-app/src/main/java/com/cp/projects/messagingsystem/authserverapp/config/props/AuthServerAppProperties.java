package com.cp.projects.messagingsystem.authserverapp.config.props;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties("com.cp.projects.messaging-system.auth-server-app-config")
public class AuthServerAppProperties {
    private int minimumUsernameLength;
    private int minimumPasswordLength;
}
