package com.cp.projects.messagingsystem.authserverapp.config.props;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ConfigurationProperties("com.cp.projects.messaging-system.auth-server-app-config")
public class AuthServerAppProperties {
    private int minimumUsernameLength;
    private int minimumPasswordLength;

    private String messageUsernameTooShort;
    private String messagePasswordTooShort;
    private String messagePasswordsDontMatch;
}
