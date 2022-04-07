package com.cp.projects.messagingsystem.components.webfluxreporting.autoconfig;

import com.cp.projects.messagingsystem.components.webfluxreporting.filter.WebClientReportingFilter;
import com.cp.projects.messagingsystem.components.webfluxreporting.properties.WebClientReportingFilterProperties;
import com.cp.projects.messagingsystem.components.ymlfactory.YamlPropertySourceFactory;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@EnableConfigurationProperties({ WebClientReportingFilterProperties.class })
@PropertySource(value = "classpath:webflux-reporting-properties.yml", factory = YamlPropertySourceFactory.class)
public class WebfluxReportingAutoConfig {

    @Bean
    public WebClientReportingFilter webClientReportingFilter(WebClientReportingFilterProperties webClientReportingFilterProperties) {
        return new WebClientReportingFilter(webClientReportingFilterProperties);
    }

}
