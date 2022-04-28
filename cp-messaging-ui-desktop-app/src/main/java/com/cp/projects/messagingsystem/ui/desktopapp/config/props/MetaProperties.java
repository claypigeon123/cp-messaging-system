package com.cp.projects.messagingsystem.ui.desktopapp.config.props;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "com.cp.projects.messaging-system.ui-desktop-app.meta")
public class MetaProperties {
    private String applicationTitle;
}
