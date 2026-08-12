package com.srots.presentation.window;

import javafx.geometry.Rectangle2D;
import javafx.stage.Screen;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.prefs.Preferences;

/**
 * Persists and restores safe window preferences via {@link Preferences}.
 */
public final class SrotsWindowStateStore {

    private static final Logger log = LoggerFactory.getLogger(SrotsWindowStateStore.class);
    private static final String NODE = "com.srots.desktop.window";

    private final Preferences preferences;

    public SrotsWindowStateStore() {
        this(Preferences.userRoot().node(NODE));
    }

    public SrotsWindowStateStore(Preferences preferences) {
        this.preferences = preferences;
    }

    public SrotsWindowState load(SrotsWindowConfiguration configuration) {
        SrotsWindowState state = new SrotsWindowState();
        state.setWidth(preferences.getDouble("width", configuration.defaultWidth()));
        state.setHeight(preferences.getDouble("height", configuration.defaultHeight()));
        state.setX(preferences.getDouble("x", Double.NaN));
        state.setY(preferences.getDouble("y", Double.NaN));
        state.setMaximized(preferences.getBoolean("maximized", false));
        state.setSidebarCollapsed(preferences.getBoolean("sidebarCollapsed", false));
        return sanitize(state, configuration);
    }

    public void save(SrotsWindowState state) {
        if (state == null) {
            return;
        }
        try {
            preferences.putDouble("width", state.getWidth());
            preferences.putDouble("height", state.getHeight());
            if (state.hasPosition()) {
                preferences.putDouble("x", state.getX());
                preferences.putDouble("y", state.getY());
            }
            preferences.putBoolean("maximized", state.isMaximized());
            preferences.putBoolean("sidebarCollapsed", state.isSidebarCollapsed());
            preferences.flush();
        } catch (Exception ex) {
            log.warn("Unable to persist window state", ex);
        }
    }

    /**
     * Ensures size bounds and that position intersects a visible screen.
     */
    public static SrotsWindowState sanitize(SrotsWindowState state, SrotsWindowConfiguration configuration) {
        SrotsWindowState copy = state == null ? new SrotsWindowState() : state.copy();
        copy.setWidth(Math.max(configuration.minWidth(), copy.getWidth()));
        copy.setHeight(Math.max(configuration.minHeight(), copy.getHeight()));

        Rectangle2D visual = Screen.getPrimary().getVisualBounds();
        if (copy.getWidth() >= visual.getWidth() - 16 || copy.getHeight() >= visual.getHeight() - 16) {
            copy.setWidth(configuration.defaultWidth());
            copy.setHeight(configuration.defaultHeight());
        }

        if (!copy.hasPosition() || !intersectsAnyScreen(copy)) {
            copy.setX(visual.getMinX() + (visual.getWidth() - copy.getWidth()) / 2.0);
            copy.setY(visual.getMinY() + (visual.getHeight() - copy.getHeight()) / 2.0);
        }
        return copy;
    }

    private static boolean intersectsAnyScreen(SrotsWindowState state) {
        Rectangle2D windowBounds = new Rectangle2D(state.getX(), state.getY(), state.getWidth(), state.getHeight());
        for (Screen screen : Screen.getScreens()) {
            if (screen.getVisualBounds().intersects(windowBounds)) {
                return true;
            }
        }
        return false;
    }
}
