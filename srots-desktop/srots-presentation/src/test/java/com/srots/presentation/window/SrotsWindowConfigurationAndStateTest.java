package com.srots.presentation.window;

import com.srots.presentation.components.support.JavaFxTestSupport;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.UUID;
import java.util.prefs.Preferences;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SrotsWindowConfigurationAndStateTest {

    @BeforeAll
    static void bootFx() throws Exception {
        JavaFxTestSupport.ensureToolkit();
    }

    @Test
    void configuration_exposesDefaultAndMinimumSizes() {
        SrotsWindowConfiguration configuration = new SrotsWindowConfiguration();
        assertEquals(1280, configuration.defaultWidth(), 0.001);
        assertEquals(820, configuration.defaultHeight(), 0.001);
        assertEquals(1024, configuration.minWidth(), 0.001);
        assertEquals(700, configuration.minHeight(), 0.001);
    }

    @Test
    void sanitize_clampsBelowMinimumSize() {
        SrotsWindowConfiguration configuration = new SrotsWindowConfiguration();
        SrotsWindowState state = new SrotsWindowState();
        state.setWidth(800);
        state.setHeight(500);
        state.setX(100);
        state.setY(100);

        SrotsWindowState safe = SrotsWindowStateStore.sanitize(state, configuration);
        assertEquals(1024, safe.getWidth(), 0.001);
        assertEquals(700, safe.getHeight(), 0.001);
        assertTrue(safe.hasPosition());
    }

    @Test
    void sanitize_resetsNearlyFullscreenSizeToDefault() throws Exception {
        JavaFxTestSupport.runOnFxThread(() -> {
            SrotsWindowConfiguration configuration = new SrotsWindowConfiguration();
            javafx.geometry.Rectangle2D visual = javafx.stage.Screen.getPrimary().getVisualBounds();
            SrotsWindowState state = new SrotsWindowState();
            state.setWidth(visual.getWidth());
            state.setHeight(visual.getHeight());
            state.setX(visual.getMinX());
            state.setY(visual.getMinY());

            SrotsWindowState safe = SrotsWindowStateStore.sanitize(state, configuration);
            assertEquals(1280, safe.getWidth(), 0.001);
            assertEquals(820, safe.getHeight(), 0.001);
        });
    }

    @Test
    void sanitize_recentersInvalidOffScreenPosition() throws Exception {
        JavaFxTestSupport.runOnFxThread(() -> {
            SrotsWindowConfiguration configuration = new SrotsWindowConfiguration();
            SrotsWindowState state = new SrotsWindowState();
            state.setWidth(1280);
            state.setHeight(820);
            state.setX(-50_000);
            state.setY(-50_000);

            SrotsWindowState safe = SrotsWindowStateStore.sanitize(state, configuration);
            assertTrue(safe.getX() > -10_000);
            assertTrue(safe.getY() > -10_000);
        });
    }

    @Test
    void stateStore_roundTripsSafePreferences() {
        Preferences prefs = Preferences.userRoot().node("com.srots.test.window." + UUID.randomUUID());
        try {
            SrotsWindowStateStore store = new SrotsWindowStateStore(prefs);
            SrotsWindowConfiguration configuration = new SrotsWindowConfiguration();

            SrotsWindowState original = new SrotsWindowState();
            original.setWidth(1280);
            original.setHeight(820);
            original.setX(120);
            original.setY(80);
            original.setMaximized(true);
            original.setSidebarCollapsed(true);
            store.save(original);

            SrotsWindowState loaded = store.load(configuration);
            assertEquals(1280, loaded.getWidth(), 0.001);
            assertEquals(820, loaded.getHeight(), 0.001);
            assertTrue(loaded.isMaximized());
            assertTrue(loaded.isSidebarCollapsed());
        } finally {
            try {
                prefs.removeNode();
            } catch (Exception ignored) {
                // test cleanup best-effort
            }
        }
    }

    @Test
    void windowState_doesNotCarrySecrets() {
        SrotsWindowState state = new SrotsWindowState();
        assertFalse(state.toString().toLowerCase().contains("password"));
        assertFalse(state.toString().toLowerCase().contains("token"));
    }
}
