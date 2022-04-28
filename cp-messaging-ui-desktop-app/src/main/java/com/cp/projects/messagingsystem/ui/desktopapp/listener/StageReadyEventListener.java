package com.cp.projects.messagingsystem.ui.desktopapp.listener;

import com.cp.projects.messagingsystem.ui.desktopapp.event.StageReadyEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class StageReadyEventListener implements ApplicationListener<StageReadyEvent> {

    private final ApplicationContext context;

    private final FXMLLoader loader;

    @Value("classpath:/scenes/base-scene.fxml")
    private Resource startScene;

    @Value("classpath:/img/icon.png")
    private Resource titleIconResource;

    @Value("${spring.application.main-title}")
    private String applicationTitle;

    @Override
    @SneakyThrows
    public void onApplicationEvent(StageReadyEvent event) {
        Parent root;
        try {
            loader.setLocation(startScene.getURL());
            root = loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Stage stage = event.getStage();
        stage.setScene(new Scene(root));
        stage.setResizable(true);
        stage.setMinWidth(939.4);
        stage.setMinHeight(528);
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.getIcons().add(new Image(titleIconResource.getInputStream()));
        stage.setTitle(applicationTitle);
        stage.show();
    }

}
