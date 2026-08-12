package com.srots.presentation.navigation.shortcut;

import com.srots.presentation.navigation.model.NavigationRouteId;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;

/**
 * Central registry of global navigation keyboard shortcuts.
 */
public final class NavigationShortcutRegistry {

    private final Map<KeyCombination, Runnable> shortcuts = new LinkedHashMap<>();

    public void register(KeyCombination combination, Runnable action) {
        Objects.requireNonNull(combination, "combination");
        Objects.requireNonNull(action, "action");
        shortcuts.put(combination, action);
    }

    public void register(KeyCombination combination, NavigationRouteId route, Consumer<NavigationRouteId> navigator) {
        Objects.requireNonNull(route, "route");
        Objects.requireNonNull(navigator, "navigator");
        register(combination, () -> navigator.accept(route));
    }

    public Optional<Runnable> find(KeyCombination combination) {
        if (combination == null) {
            return Optional.empty();
        }
        return Optional.ofNullable(shortcuts.get(combination));
    }

    public void install(Scene scene) {
        Objects.requireNonNull(scene, "scene");
        scene.addEventFilter(KeyEvent.KEY_PRESSED, this::onKeyPressed);
    }

    /**
     * Registers default shortcuts. Ctrl+1 Overview; Alt+Left/Right history; Ctrl+K optional palette.
     */
    public void registerDefaults(
            Consumer<NavigationRouteId> navigator,
            Runnable backAction,
            Runnable forwardAction,
            Runnable commandPaletteAction) {
        if (navigator != null) {
            register(
                    new KeyCodeCombination(KeyCode.DIGIT1, KeyCombination.SHORTCUT_DOWN),
                    NavigationRouteId.OVERVIEW,
                    navigator);
            register(
                    new KeyCodeCombination(KeyCode.DIGIT2, KeyCombination.SHORTCUT_DOWN),
                    NavigationRouteId.WORKSPACE,
                    navigator);
            register(
                    new KeyCodeCombination(KeyCode.DIGIT3, KeyCombination.SHORTCUT_DOWN),
                    NavigationRouteId.PROJECTS,
                    navigator);
        }
        if (backAction != null) {
            register(new KeyCodeCombination(KeyCode.LEFT, KeyCombination.ALT_DOWN), backAction);
        }
        if (forwardAction != null) {
            register(new KeyCodeCombination(KeyCode.RIGHT, KeyCombination.ALT_DOWN), forwardAction);
        }

        Runnable ctrlK = commandPaletteAction != null ? commandPaletteAction : () -> {
        };
        register(new KeyCodeCombination(KeyCode.K, KeyCombination.SHORTCUT_DOWN), ctrlK);
    }

    public void registerDefaults(Consumer<NavigationRouteId> navigator, Runnable commandPaletteAction) {
        registerDefaults(navigator, null, null, commandPaletteAction);
    }

    public void registerDefaults(Consumer<NavigationRouteId> navigator) {
        registerDefaults(navigator, null, null, null);
    }

    private void onKeyPressed(KeyEvent event) {
        for (Map.Entry<KeyCombination, Runnable> entry : shortcuts.entrySet()) {
            if (entry.getKey().match(event)) {
                entry.getValue().run();
                event.consume();
                return;
            }
        }
    }
}
