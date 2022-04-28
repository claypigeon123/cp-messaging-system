package com.cp.projects.messagingsystem.ui.desktopapp.util.mouse;

import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import static com.cp.projects.messagingsystem.ui.desktopapp.util.mouse.MouseEdgePosition.*;
import static com.cp.projects.messagingsystem.ui.desktopapp.util.mouse.MousePositionUtils.determinePosition;

public class ResizeMouseEventHandler implements EventHandler<MouseEvent> {

    private final Parent container;

    private double xDrag, yDrag;

    public ResizeMouseEventHandler(Parent container) {
        this.container = container;
        this.xDrag = 0;
        this.yDrag = 0;
    }

    @Override
    public void handle(MouseEvent mouseEvent) {
        EventType<? extends MouseEvent> eventType = mouseEvent.getEventType();

        if (eventType.equals(MouseEvent.MOUSE_MOVED)) {
            handleMouseMoved(mouseEvent);
        } else if (eventType.equals(MouseEvent.MOUSE_PRESSED)) {
            handleMousePressed(mouseEvent);
        } else if (eventType.equals(MouseEvent.MOUSE_DRAGGED)) {
            handleMouseDragged(mouseEvent);
        } else if (eventType.equals(MouseEvent.MOUSE_RELEASED)) {
            handleMouseReleased(mouseEvent);
        }
    }

    // --

    private Stage getStage() {
        return (Stage) container.getScene().getWindow();
    }

    private void handleMouseMoved(MouseEvent mouseEvent) {
        double x = mouseEvent.getX(), y = mouseEvent.getY();
        double xMax = getStage().getWidth(), yMax = getStage().getHeight();

        switch (determinePosition(x, y, xMax, yMax)) {
            case TOP_LEFT -> container.setCursor(TOP_LEFT.getCursor());
            case TOP_RIGHT -> container.setCursor(TOP_RIGHT.getCursor());
            case TOP -> container.setCursor(TOP.getCursor());
            case BOTTOM_LEFT -> container.setCursor(BOTTOM_LEFT.getCursor());
            case BOTTOM_RIGHT -> container.setCursor(BOTTOM_RIGHT.getCursor());
            case BOTTOM -> container.setCursor(BOTTOM.getCursor());
            case LEFT -> container.setCursor(LEFT.getCursor());
            case RIGHT -> container.setCursor(RIGHT.getCursor());
            default -> container.setCursor(NOT_ON_EDGE.getCursor());
        }
    }

    private void handleMousePressed(MouseEvent mouseEvent) {
        xDrag = getStage().getWidth() - mouseEvent.getSceneX();
        yDrag = getStage().getHeight() - mouseEvent.getSceneY();
    }

    private void handleMouseDragged(MouseEvent mouseEvent) {
        Cursor cursor = container.getCursor();
        if (cursor.equals(Cursor.DEFAULT) || cursor.equals(Cursor.HAND)) {
            return;
        }

        Stage stage = getStage();
        int border = 5;
        double mouseEventX = mouseEvent.getSceneX(), mouseEventY = mouseEvent.getSceneY();

        if (!Cursor.W_RESIZE.equals(cursor) && !Cursor.E_RESIZE.equals(cursor)) {
            double minHeight = stage.getMinHeight() > (border*2) ? stage.getMinHeight() : (border*2);
            if (Cursor.NW_RESIZE.equals(cursor) || Cursor.N_RESIZE.equals(cursor) || Cursor.NE_RESIZE.equals(cursor)) {
                if (stage.getHeight() > minHeight || mouseEventY < 0) {
                    stage.setHeight(stage.getY() - mouseEvent.getScreenY() + stage.getHeight());
                    stage.setY(mouseEvent.getScreenY());
                }
            } else {
                if (stage.getHeight() > minHeight || mouseEventY + yDrag - stage.getHeight() > 0) {
                    stage.setHeight(mouseEventY + yDrag);
                }
            }
        }

        if (!Cursor.N_RESIZE.equals(cursor) && !Cursor.S_RESIZE.equals(cursor)) {
            double minWidth = stage.getMinWidth() > (border*2) ? stage.getMinWidth() : (border*2);
            if (Cursor.NW_RESIZE.equals(cursor) || Cursor.W_RESIZE.equals(cursor) || Cursor.SW_RESIZE.equals(cursor)) {
                if (stage.getWidth() > minWidth || mouseEventX < 0) {
                    stage.setWidth(stage.getX() - mouseEvent.getScreenX() + stage.getWidth());
                    stage.setX(mouseEvent.getScreenX());
                }
            } else {
                if (stage.getWidth() > minWidth || mouseEventX + xDrag - stage.getWidth() > 0) {
                    stage.setWidth(mouseEventX + xDrag);
                }
            }
        }
    }

    private void handleMouseReleased(MouseEvent mouseEvent) {
        xDrag = 0;
        yDrag = 0;
    }
}
