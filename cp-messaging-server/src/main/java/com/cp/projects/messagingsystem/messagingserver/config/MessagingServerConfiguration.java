package com.cp.projects.messagingsystem.messagingserver.config;

import com.cp.projects.messagingsystem.messagingserver.config.properties.SecurityProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableScheduling
@EnableConfigurationProperties({ SecurityProperties.class})
public class MessagingServerConfiguration {

}
