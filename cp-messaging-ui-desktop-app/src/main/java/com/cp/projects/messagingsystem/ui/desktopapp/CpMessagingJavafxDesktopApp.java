package com.cp.projects.messagingsystem.ui.desktopapp;

import com.cp.projects.messagingsystem.ui.desktopapp.event.StageReadyEvent;
import javafx.application.Application;
import javafx.stage.Stage;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

public class CpMessagingJavafxDesktopApp extends Application {

    private ConfigurableApplicationContext context;

    @Override
    public void init() {
        context = new SpringApplicationBuilder(CpMessagingSpringDesktopApp.class).run();
    }

    @Override
    public void start(Stage primaryStage) {
        context.publishEvent(new StageReadyEvent(primaryStage));
    }

    @Override
    public void stop() {
        System.out.println("Called stop");
        context.close();
    }
}
