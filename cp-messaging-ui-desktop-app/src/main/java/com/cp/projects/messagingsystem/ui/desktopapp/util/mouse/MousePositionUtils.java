package com.cp.projects.messagingsystem.ui.desktopapp.util.mouse;

public class MousePositionUtils {
    private static final double ZERO = 0.0;
    private static final double TOLERANCE = 5.0;

    public static MouseEdgePosition determinePosition(double x, double y, double xMax, double yMax) {

        if (withinRange(x, ZERO, TOLERANCE) && withinRange(y, ZERO, TOLERANCE))
            return MouseEdgePosition.TOP_LEFT;
        else if (withinRange(x, xMax - TOLERANCE, xMax) && withinRange(y, ZERO, TOLERANCE))
            return MouseEdgePosition.TOP_RIGHT;
        else if (withinRange(x, ZERO, xMax) && withinRange(y, ZERO, TOLERANCE))
            return MouseEdgePosition.TOP;

        else if (withinRange(x, ZERO, TOLERANCE) && withinRange(y, yMax - TOLERANCE, yMax))
            return MouseEdgePosition.BOTTOM_LEFT;
        else if (withinRange(x, xMax - TOLERANCE, xMax) && withinRange(y, yMax - TOLERANCE, yMax))
            return MouseEdgePosition.BOTTOM_RIGHT;
        else if (withinRange(x, ZERO, xMax) && withinRange(y, yMax - TOLERANCE, yMax))
            return MouseEdgePosition.BOTTOM;

        else if (withinRange(x, ZERO, TOLERANCE) && withinRange(y, ZERO, yMax))
            return MouseEdgePosition.LEFT;
        else if (withinRange(x, xMax - TOLERANCE, xMax) && withinRange(y, ZERO, yMax))
            return MouseEdgePosition.RIGHT;

        else
            return MouseEdgePosition.NOT_ON_EDGE;
    }

    // --

    private static boolean withinRange(double value, double lowerBound, double upperBound) {
        return value >= lowerBound && value <= upperBound;
    }
}
