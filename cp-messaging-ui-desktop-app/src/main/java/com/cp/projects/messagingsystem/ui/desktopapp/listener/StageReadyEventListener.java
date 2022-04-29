package com.cp.projects.messagingsystem.ui.desktopapp.listener;

import com.cp.projects.messagingsystem.ui.desktopapp.config.props.MetaProperties;
import com.cp.projects.messagingsystem.ui.desktopapp.event.StageReadyEvent;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.io.IOException;

@Component
@RequiredArgsConstructor
public class StageReadyEventListener implements ApplicationListener<StageReadyEvent> {

    private final FXMLLoader loader;
    private final MetaProperties meta;

    @Value("classpath:/scenes/base-scene.fxml")
    private Resource startScene;

    @Value("classpath:/img/icon.png")
    private Resource titleIconResource;

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

        Stage stage = initializeStage(event.getStage(), new Scene(root));

        stage.show();
    }

    // --

    private Stage initializeStage(Stage stage, Scene scene) throws IOException, AWTException {
        stage.setScene(scene);
        stage.setResizable(true);
        stage.setMinWidth(939.4);
        stage.setMinHeight(528);
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.getIcons().add(new Image(titleIconResource.getInputStream()));
        stage.setTitle(meta.getApplicationTitle());
        this.setUpTray(stage);

        return stage;
    }

    private void setUpTray(Stage stage) throws IOException, AWTException {
        if (!SystemTray.isSupported()) {
            return;
        }

        PopupMenu popup = new PopupMenu(meta.getApplicationTitle());
        popup.setFont(Font.getFont(Font.MONOSPACED));
        SystemTray tray = SystemTray.getSystemTray();

        java.awt.Image iconImage = Toolkit.getDefaultToolkit().getImage(titleIconResource.getURL());
        iconImage = iconImage.getScaledInstance(20, 20, java.awt.Image.SCALE_SMOOTH);
        TrayIcon icon = new TrayIcon(iconImage);
        icon.addActionListener(e -> Platform.runLater(stage::show));

        MenuItem openItem = new MenuItem("Open");
        openItem.addActionListener(e -> Platform.runLater(stage::show));

        MenuItem exitItem = new MenuItem("Exit");
        exitItem.addActionListener(e -> Platform.runLater(() -> {
            tray.remove(icon);
            stage.close();
            Platform.exit();
        }));


        popup.add(openItem);
        popup.addSeparator();
        popup.add(exitItem);

        icon.setPopupMenu(popup);
        tray.add(icon);
    }
}
