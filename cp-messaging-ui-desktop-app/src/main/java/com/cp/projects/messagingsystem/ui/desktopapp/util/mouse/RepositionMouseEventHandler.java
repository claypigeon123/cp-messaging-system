package com.cp.projects.messagingsystem.ui.desktopapp.util.mouse;

import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.scene.Parent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class RepositionMouseEventHandler implements EventHandler<MouseEvent> {

    private final Parent container;

    private double xOffset, yOffset;

    public RepositionMouseEventHandler(Parent container) {
        this.container = container;
        this.xOffset = 0;
        this.yOffset = 0;
    }

    @Override
    public void handle(MouseEvent mouseEvent) {
        EventType<? extends MouseEvent> eventType = mouseEvent.getEventType();

        if (eventType.equals(MouseEvent.MOUSE_PRESSED)) {
            handleMousePressed(mouseEvent);
        } else if (eventType.equals(MouseEvent.MOUSE_DRAGGED)) {
            handleMouseDragged(mouseEvent);
        }
    }

    // --

    private Stage getStage() {
        return (Stage) container.getScene().getWindow();
    }

    private void handleMousePressed(MouseEvent mouseEvent) {
        xOffset = mouseEvent.getSceneX();
        yOffset = mouseEvent.getSceneY();
    }

    private void handleMouseDragged(MouseEvent mouseEvent) {
        getStage().setX(mouseEvent.getScreenX() - xOffset);
        getStage().setY(mouseEvent.getScreenY() - yOffset);
    }
}
