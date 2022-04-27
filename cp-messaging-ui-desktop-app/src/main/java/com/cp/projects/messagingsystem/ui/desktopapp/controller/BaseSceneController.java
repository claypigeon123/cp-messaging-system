package com.cp.projects.messagingsystem.ui.desktopapp.controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BaseSceneController {

    @FXML
    public VBox container;

    @FXML
    public HBox controlBar;

    private double xOffset = 0;
    private double yOffset = 0;

    public void initialize() {
        controlBar.setOnMousePressed(mouseEvent -> {
            xOffset = mouseEvent.getSceneX();
            yOffset = mouseEvent.getSceneY();
        });

        controlBar.setOnMouseDragged(mouseEvent -> {
            getStage().setX(mouseEvent.getScreenX() - xOffset);
            getStage().setY(mouseEvent.getScreenY() - yOffset);
        });
    }

    public void minimize(ActionEvent actionEvent) {
        getStage().setIconified(true);
    }

    public void collapse(ActionEvent actionEvent) {
        getStage().setFullScreen(!getStage().isFullScreen());
    }

    public void close(ActionEvent actionEvent) {
        getStage().hide(); // to give the illusion of closing fast
        Platform.exit(); // actually shut down in the background
    }

    private Stage getStage() {
        return (Stage) container.getScene().getWindow();
    }
}
