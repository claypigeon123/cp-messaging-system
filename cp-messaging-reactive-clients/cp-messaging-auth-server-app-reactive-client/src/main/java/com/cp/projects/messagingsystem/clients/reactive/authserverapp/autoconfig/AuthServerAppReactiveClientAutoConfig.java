package com.cp.projects.messagingsystem.clients.reactive.authserverapp.autoconfig;

import com.cp.projects.messagingsystem.clients.reactive.authserverapp.client.AuthServerAppReactiveClient;
import com.cp.projects.messagingsystem.clients.reactive.authserverapp.properties.AuthServerAppClientProperties;
import com.cp.projects.messagingsystem.components.ymlfactory.YamlPropertySourceFactory;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
@EnableConfigurationProperties({ AuthServerAppClientProperties.class })
@PropertySource(value = "classpath:auth-server-app-reactive-client.yml", factory = YamlPropertySourceFactory.class)
public class AuthServerAppReactiveClientAutoConfig {

    @Bean
    public AuthServerAppReactiveClient authServerAppReactiveClient(AuthServerAppClientProperties props, WebClient webClient) {
        return new AuthServerAppReactiveClient(props, webClient);
    }
}
