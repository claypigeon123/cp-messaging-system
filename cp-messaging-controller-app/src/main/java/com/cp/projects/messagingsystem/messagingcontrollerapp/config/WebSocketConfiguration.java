package com.cp.projects.messagingsystem.messagingcontrollerapp.config;

import com.cp.projects.messagingsystem.messagingcontrollerapp.service.MessagingService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.HandlerMapping;
import org.springframework.web.reactive.handler.SimpleUrlHandlerMapping;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.server.support.WebSocketHandlerAdapter;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class WebSocketConfiguration implements WebSocketMessageBrokerConfigurer {
    private static final String MESSAGES_URI = "/messages";

    @Bean
    public HandlerMapping webSocketHandlerMapping(MessagingService messagingService) {
        Map<String, WebSocketHandler> processors = new HashMap<>();
        processors.put(MESSAGES_URI, messagingService);

        SimpleUrlHandlerMapping handlerMapping = new SimpleUrlHandlerMapping();
        handlerMapping.setOrder(1);
        handlerMapping.setUrlMap(processors);
        return handlerMapping;
    }

    @Bean
    public WebSocketHandlerAdapter webSocketHandlerAdapter() {
        return new WebSocketHandlerAdapter();
    }
}
