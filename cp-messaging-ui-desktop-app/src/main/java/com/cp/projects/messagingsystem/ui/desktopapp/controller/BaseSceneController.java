package com.cp.projects.messagingsystem.ui.desktopapp.controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class BaseSceneController {

    @FXML
    public VBox container;

    @FXML
    public AnchorPane topBarContainer;

    @FXML
    public HBox titleContainer;

    @FXML
    public ImageView titleIcon;

    @FXML
    public Label titleText;

    @FXML
    public HBox controlsContainer;

    @Value("${spring.application.main-title}")
    private String applicationTitle;

    @Value("classpath:/img/icon.png")
    private Resource titleIconResource;

    private double xOffset = 0;
    private double yOffset = 0;

    public void initialize() throws IOException {
        titleText.setText(applicationTitle);

        titleIcon.setImage(new Image(titleIconResource.getInputStream()));

        topBarContainer.setOnMousePressed(mouseEvent -> {
            xOffset = mouseEvent.getSceneX();
            yOffset = mouseEvent.getSceneY();
        });

        topBarContainer.setOnMouseDragged(mouseEvent -> {
            getStage().setX(mouseEvent.getScreenX() - xOffset);
            getStage().setY(mouseEvent.getScreenY() - yOffset);
        });
    }

    public void minimize(ActionEvent actionEvent) {
        getStage().setIconified(true);

    }

    public void maximize(ActionEvent actionEvent) {
        getStage().setMaximized(!getStage().isMaximized());
    }

    public void quit(ActionEvent actionEvent) {
        getStage().hide(); // to give the illusion of closing fast
        Platform.exit(); // actually shut down in the background
    }

    private Stage getStage() {
        return (Stage) container.getScene().getWindow();
    }
}
