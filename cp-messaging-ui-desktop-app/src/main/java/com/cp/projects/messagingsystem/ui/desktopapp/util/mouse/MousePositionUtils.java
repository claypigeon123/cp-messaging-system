package com.cp.projects.messagingsystem.ui.desktopapp.util.mouse;

import javafx.scene.Cursor;

public class MousePositionUtils {
    private static final double ZERO = 0.0;
    private static final double TOLERANCE = 5.0;

    public static Cursor determinePosition(double x, double y, double xMax, double yMax) {

        if (withinRange(x, ZERO, TOLERANCE) && withinRange(y, ZERO, TOLERANCE))
            return Cursor.NW_RESIZE;
        else if (withinRange(x, xMax - TOLERANCE, xMax) && withinRange(y, ZERO, TOLERANCE))
            return Cursor.NE_RESIZE;
        else if (withinRange(x, ZERO, xMax) && withinRange(y, ZERO, TOLERANCE))
            return Cursor.N_RESIZE;

        else if (withinRange(x, ZERO, TOLERANCE) && withinRange(y, yMax - TOLERANCE, yMax))
            return Cursor.SW_RESIZE;
        else if (withinRange(x, xMax - TOLERANCE, xMax) && withinRange(y, yMax - TOLERANCE, yMax))
            return Cursor.SE_RESIZE;
        else if (withinRange(x, ZERO, xMax) && withinRange(y, yMax - TOLERANCE, yMax))
            return Cursor.S_RESIZE;

        else if (withinRange(x, ZERO, TOLERANCE) && withinRange(y, ZERO, yMax))
            return Cursor.W_RESIZE;
        else if (withinRange(x, xMax - TOLERANCE, xMax) && withinRange(y, ZERO, yMax))
            return Cursor.E_RESIZE;

        else
            return Cursor.DEFAULT;
    }

    // --

    private static boolean withinRange(double value, double lowerBound, double upperBound) {
        return value >= lowerBound && value <= upperBound;
    }
}
