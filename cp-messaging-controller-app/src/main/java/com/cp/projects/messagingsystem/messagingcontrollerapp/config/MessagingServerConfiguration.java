package com.cp.projects.messagingsystem.messagingcontrollerapp.config;

import com.cp.projects.messagingsystem.messagingcontrollerapp.config.properties.SecurityProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableScheduling
@EnableConfigurationProperties({ SecurityProperties.class})
public class MessagingServerConfiguration {

}
