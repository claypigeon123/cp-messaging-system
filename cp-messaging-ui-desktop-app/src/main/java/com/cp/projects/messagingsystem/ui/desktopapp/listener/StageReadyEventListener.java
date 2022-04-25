package com.cp.projects.messagingsystem.ui.desktopapp.listener;

import com.cp.projects.messagingsystem.ui.desktopapp.event.StageReadyEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class StageReadyEventListener implements ApplicationListener<StageReadyEvent> {

    private final FXMLLoader loader;
    private final Resource startScene;
    private final String applicationTitle;

    public StageReadyEventListener(FXMLLoader loader,
                                   @Value("classpath:/scenes/start-scene.fxml") Resource startScene,
                                   @Value("${spring.application.main-title}") String applicationTitle
    ) {
        this.loader = loader;
        this.startScene = startScene;
        this.applicationTitle = applicationTitle;
    }

    @Override
    public void onApplicationEvent(StageReadyEvent event) {
        Parent parent;
        try {
            loader.setLocation(startScene.getURL());
            parent = loader.load();
        } catch (IOException e) {
            throw new RuntimeException();
        }

        Stage stage = event.getStage();
        stage.setScene(new Scene(parent));
        stage.setResizable(true);
        stage.setTitle(applicationTitle);
        stage.show();
    }

}
