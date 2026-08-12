package com.srots.presentation.window;

/**
 * Preference key names for primary window persistence (no secrets).
 * Used by {@link SrotsWindowStateStore}.
 */
public final class SrotsWindowPreferences {

    public static final String NODE = "com.srots.desktop.window";

    public static final String WIDTH = "width";
    public static final String HEIGHT = "height";
    public static final String X = "x";
    public static final String Y = "y";
    public static final String MAXIMIZED = "maximized";
    public static final String SIDEBAR_COLLAPSED = "sidebarCollapsed";

    private SrotsWindowPreferences() {
    }
}
