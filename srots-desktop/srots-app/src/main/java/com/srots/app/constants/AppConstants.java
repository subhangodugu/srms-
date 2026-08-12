package com.srots.app.constants;

/**
 * Launcher and shell constants. Feature modules must not redefine app identity or window defaults.
 */
public final class AppConstants {

    private AppConstants() {}

    public static final String APP_NAME = "SROTS";
    public static final String APP_FULL_NAME = "SROTS Enterprise Control Plane";
    public static final String APP_VERSION = "0.1.0-SNAPSHOT";

    public static final double MIN_WINDOW_WIDTH = 1024.0;
    public static final double MIN_WINDOW_HEIGHT = 700.0;
    public static final double DEFAULT_WINDOW_WIDTH = 1280.0;
    public static final double DEFAULT_WINDOW_HEIGHT = 820.0;

    public static final String FXML_MAIN_VIEW = "/fxml/app/MainView.fxml";
    public static final String CSS_BASE = "/css/base.css";
    public static final String CSS_THEME = "/css/theme.css";

    /** Default route after shell load (Prompt 08). */
    public static final String DEFAULT_ROUTE_NAME = "OVERVIEW";
}
