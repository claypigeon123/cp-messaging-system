package com.cp.projects.messagingsystem.ui.desktopapp.controller;

import com.cp.projects.messagingsystem.ui.desktopapp.config.props.MetaProperties;
import com.cp.projects.messagingsystem.ui.desktopapp.util.mouse.RepositionMouseEventHandler;
import com.cp.projects.messagingsystem.ui.desktopapp.util.mouse.ResizeMouseEventHandler;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
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

    @FXML public AnchorPane container;

    @FXML public AnchorPane topBarContainer;

    @FXML public HBox titleContainer;

    @FXML public ImageView titleIcon;

    @FXML public Label titleText;

    @FXML public HBox controlsContainer;

    @FXML public Button profileBtn;
    @FXML public Button friendsBtn;
    @FXML public Button settingsBtn;

    @Value("classpath:/img/icon.png")
    private Resource titleIconResource;

    @Value("classpath:/img/user-solid.png")
    private Resource userSolidResource;

    @Value("classpath:/img/users-solid.png")
    private Resource usersSolidResource;

    @Value("classpath:/img/cog-solid.png")
    private Resource settingsSolidResource;

    public void initialize() throws IOException {
        titleText.setText(meta.getApplicationTitle());
        titleIcon.setImage(new Image(titleIconResource.getInputStream()));

        ImageView userSolidImg = new ImageView(new Image(userSolidResource.getInputStream()));
        userSolidImg.setFitWidth(20);
        userSolidImg.setFitHeight(20);
        profileBtn.setGraphic(userSolidImg);

        ImageView friendsSolidImg = new ImageView(new Image(usersSolidResource.getInputStream()));
        friendsSolidImg.setFitWidth(20);
        friendsSolidImg.setFitHeight(20);
        friendsBtn.setGraphic(friendsSolidImg);

        ImageView settingsSolidImg = new ImageView(new Image(settingsSolidResource.getInputStream()));
        settingsSolidImg.setFitWidth(20);
        settingsSolidImg.setFitHeight(20);
        settingsBtn.setGraphic(settingsSolidImg);

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
