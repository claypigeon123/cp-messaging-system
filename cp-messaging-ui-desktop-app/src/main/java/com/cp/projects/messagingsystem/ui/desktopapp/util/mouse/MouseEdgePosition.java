package com.cp.projects.messagingsystem.ui.desktopapp.util.mouse;

import javafx.scene.Cursor;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import static javafx.scene.Cursor.*;

@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public enum MouseEdgePosition {
    TOP_LEFT(NW_RESIZE),
    TOP_RIGHT(NE_RESIZE),
    TOP(N_RESIZE),

    BOTTOM_LEFT(SW_RESIZE),
    BOTTOM_RIGHT(SE_RESIZE),
    BOTTOM(S_RESIZE),

    RIGHT(E_RESIZE),
    LEFT(W_RESIZE),

    NOT_ON_EDGE(DEFAULT);

    @Getter
    private final Cursor cursor;
}
