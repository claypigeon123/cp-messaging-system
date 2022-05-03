package com.cp.projects.messagingsystem.authserverapp.config;

import com.cp.projects.messagingsystem.authserverapp.config.props.AuthServerAppProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties({ AuthServerAppProperties.class })
public class AuthServerAppConfiguration {
}
