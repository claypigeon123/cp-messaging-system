package com.cp.projects.messagingsystem.ui.desktopapp;

import com.cp.projects.messagingsystem.ui.desktopapp.event.StageReadyEvent;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

public class CpMessagingJavafxDesktopApp extends Application {

    private ConfigurableApplicationContext context;

    @Override
    public void init() {
        context = new SpringApplicationBuilder(CpMessagingSpringDesktopApp.class)
            .web(WebApplicationType.NONE)
            .headless(false)
            .run();
    }

    @Override
    public void start(Stage primaryStage) {
        Platform.setImplicitExit(false);
        context.publishEvent(new StageReadyEvent(primaryStage));
    }

    @Override
    public void stop() {
        context.close();
        System.exit(0);
    }
}
