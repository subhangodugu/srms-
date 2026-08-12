package com.srots.presentation.window;

/**
 * Immutable defaults for the primary SROTS desktop window.
 * Default 1280×820 and min 1024×700 match the previous SRMS desktop frame
 * so the shell opens at a usable size with OS minimize / maximize / close.
 */
public final class SrotsWindowConfiguration {

    public static final double DEFAULT_WIDTH = 1280;
    public static final double DEFAULT_HEIGHT = 820;
    public static final double MIN_WIDTH = 1024;
    public static final double MIN_HEIGHT = 700;
    public static final double SIDEBAR_EXPANDED_WIDTH = 260;
    public static final double SIDEBAR_COLLAPSED_WIDTH = 68;

    private final double defaultWidth;
    private final double defaultHeight;
    private final double minWidth;
    private final double minHeight;

    public SrotsWindowConfiguration() {
        this(DEFAULT_WIDTH, DEFAULT_HEIGHT, MIN_WIDTH, MIN_HEIGHT);
    }

    public SrotsWindowConfiguration(double defaultWidth, double defaultHeight, double minWidth, double minHeight) {
        this.defaultWidth = defaultWidth;
        this.defaultHeight = defaultHeight;
        this.minWidth = minWidth;
        this.minHeight = minHeight;
    }

    public double defaultWidth() {
        return defaultWidth;
    }

    public double defaultHeight() {
        return defaultHeight;
    }

    public double minWidth() {
        return minWidth;
    }

    public double minHeight() {
        return minHeight;
    }
}
