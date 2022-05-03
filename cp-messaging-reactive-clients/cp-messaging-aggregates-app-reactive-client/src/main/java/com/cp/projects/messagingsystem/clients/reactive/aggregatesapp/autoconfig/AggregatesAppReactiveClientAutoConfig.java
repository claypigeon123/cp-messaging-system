package com.cp.projects.messagingsystem.clients.reactive.aggregatesapp.autoconfig;

import com.cp.projects.messagingsystem.clients.reactive.aggregatesapp.client.AggregatesAppReactiveClient;
import com.cp.projects.messagingsystem.clients.reactive.aggregatesapp.properties.AggregatesAppClientProperties;
import com.cp.projects.messagingsystem.components.ymlfactory.YamlPropertySourceFactory;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
@EnableConfigurationProperties({ AggregatesAppClientProperties.class })
@PropertySource(value = "classpath:aggregates-app-reactive-client.yml", factory = YamlPropertySourceFactory.class)
public class AggregatesAppReactiveClientAutoConfig {

    @Bean
    public AggregatesAppReactiveClient aggregatesAppReactiveClient(AggregatesAppClientProperties props, WebClient webClient) {
        return new AggregatesAppReactiveClient(props, webClient);
    }
}
