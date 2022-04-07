package com.cp.projects.messagingsystem.components.webfluxreporting.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Collection;

@Data
@ConfigurationProperties("com.cp.projects.messaging-system.reporting")
public class WebClientReportingFilterProperties {
    private Collection<String> excludedPaths;
}
