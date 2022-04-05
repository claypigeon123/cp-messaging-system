package com.cp.projects.messagingsystem.components.cpcomponentwebfluxreporting.autoconfig;

import com.cp.projects.messagingsystem.components.cpcomponentwebfluxreporting.filter.WebClientReportingFilter;
import com.cp.projects.messagingsystem.components.cpcomponentwebfluxreporting.properties.WebClientReportingFilterProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties({ WebClientReportingFilterProperties.class })
public class WebfluxReportingAutoconfig {

    @Bean
    public WebClientReportingFilter webClientReportingFilter(WebClientReportingFilterProperties webClientReportingFilterProperties) {
        return new WebClientReportingFilter(webClientReportingFilterProperties);
    }

}
