package com.cp.projects.messagingsystem.ui.desktopapp.config;

import com.cp.projects.messagingsystem.ui.desktopapp.config.props.MetaProperties;
import javafx.fxml.FXMLLoader;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties({ MetaProperties.class })
public class AutoConfiguration {

    @Bean
    public FXMLLoader fxmlLoader(ApplicationContext context) {
        FXMLLoader loader = new FXMLLoader();
        loader.setControllerFactory(context::getBean);
        return loader;
    }
}
