package com.cp.projects.messagingsystem.clients.reactive.controllerapp.autoconfig;

import com.cp.projects.messagingsystem.clients.reactive.controllerapp.client.ControllerAppReactiveClient;
import com.cp.projects.messagingsystem.clients.reactive.controllerapp.properties.ControllerAppClientProperties;
import com.cp.projects.messagingsystem.components.ymlfactory.YamlPropertySourceFactory;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.socket.client.WebSocketClient;

@Configuration
@EnableConfigurationProperties({ ControllerAppClientProperties.class })
@PropertySource(value = "classpath:controller-app-reactive-client.yml", factory = YamlPropertySourceFactory.class)
public class ControllerAppReactiveClientAutoConfig {

    @Bean
    public ControllerAppReactiveClient controllerAppReactiveClient(ControllerAppClientProperties props, WebClient webClient, WebSocketClient webSocketClient) {
        return new ControllerAppReactiveClient(props, webClient, webSocketClient);
    }
}
