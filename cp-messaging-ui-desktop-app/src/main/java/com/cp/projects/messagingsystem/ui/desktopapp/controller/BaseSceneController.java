package com.cp.projects.messagingsystem.ui.desktopapp.controller;

import com.cp.projects.messagingsystem.ui.desktopapp.config.props.MetaProperties;
import com.cp.projects.messagingsystem.ui.desktopapp.util.mouse.RepositionMouseEventHandler;
import com.cp.projects.messagingsystem.ui.desktopapp.util.mouse.ResizeMouseEventHandler;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class BaseSceneController {

    private final MetaProperties meta;

    @FXML
    public AnchorPane container;

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

    @Value("classpath:/img/icon.png")
    private Resource titleIconResource;

    public void initialize() throws IOException {
        titleText.setText(meta.getApplicationTitle());
        titleIcon.setImage(new Image(titleIconResource.getInputStream()));

        topBarContainer.addEventHandler(MouseEvent.ANY, new RepositionMouseEventHandler(container));
        container.addEventHandler(MouseEvent.ANY, new ResizeMouseEventHandler(container));
    }

    public void minimize(ActionEvent actionEvent) {
        getStage().setIconified(true);

    }

    public void maximize(ActionEvent actionEvent) {
        getStage().setMaximized(!getStage().isMaximized());
    }

    public void quit(ActionEvent actionEvent) {
        getStage().close();
    }

    public void buttonHovered(MouseEvent mouseEvent) {
        container.setCursor(Cursor.HAND);
    }

    public void buttonHoverEnded(MouseEvent mouseEvent) {
        container.setCursor(Cursor.DEFAULT);
    }

    // --

    private Stage getStage() {
        return (Stage) container.getScene().getWindow();
    }
}
