package com.srots.presentation.navigation.host;

import com.srots.presentation.navigation.lifecycle.SrotsViewLifecycle;
import javafx.animation.FadeTransition;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

import java.util.Objects;

/**
 * Hosts the active feature view inside the application shell content area.
 * TopBar and StatusBar stay fixed; only this host scrolls / swaps feature content.
 */
public final class SrotsContentHost {

    private static final Duration FADE_DURATION = Duration.millis(150);

    private final StackPane host;
    private boolean transitionsEnabled;
    private Node currentView;

    public SrotsContentHost() {
        this(new StackPane());
    }

    public SrotsContentHost(StackPane host) {
        this.host = Objects.requireNonNull(host, "host");
        if (!this.host.getStyleClass().contains("srots-content")) {
            this.host.getStyleClass().add("srots-content");
        }
    }

    public void setView(Node view) {
        if (view == null) {
            clear();
            return;
        }
        deactivate(currentView);
        dispose(currentView);

        if (!transitionsEnabled || host.getChildren().isEmpty()) {
            host.getChildren().setAll(view);
            StackPane.setAlignment(view, Pos.TOP_LEFT);
        } else {
            view.setOpacity(0);
            host.getChildren().setAll(view);
            StackPane.setAlignment(view, Pos.TOP_LEFT);
            FadeTransition fade = new FadeTransition(FADE_DURATION, view);
            fade.setFromValue(0);
            fade.setToValue(1);
            fade.play();
        }

        currentView = view;
        activate(view);
        requestInitialFocus(view);
    }

    public void clear() {
        deactivate(currentView);
        dispose(currentView);
        host.getChildren().clear();
        currentView = null;
    }

    public Node getCurrentView() {
        return currentView;
    }

    public StackPane getHost() {
        return host;
    }

    public boolean isTransitionsEnabled() {
        return transitionsEnabled;
    }

    public void setTransitionsEnabled(boolean transitionsEnabled) {
        this.transitionsEnabled = transitionsEnabled;
    }

    private static void activate(Node view) {
        SrotsViewLifecycle lifecycle = findLifecycle(view);
        if (lifecycle != null) {
            lifecycle.onActivate();
        }
    }

    private static void deactivate(Node view) {
        SrotsViewLifecycle lifecycle = findLifecycle(view);
        if (lifecycle != null) {
            lifecycle.onDeactivate();
        }
    }

    private static void dispose(Node view) {
        SrotsViewLifecycle lifecycle = findLifecycle(view);
        if (lifecycle != null) {
            lifecycle.onDispose();
        }
    }

    private static SrotsViewLifecycle findLifecycle(Node view) {
        if (view instanceof SrotsViewLifecycle lifecycle) {
            return lifecycle;
        }
        if (view != null && view.getUserData() instanceof SrotsViewLifecycle lifecycle) {
            return lifecycle;
        }
        return null;
    }

    private static void requestInitialFocus(Node view) {
        if (view == null) {
            return;
        }
        if (view.isFocusTraversable()) {
            view.requestFocus();
            return;
        }
        if (view instanceof Parent parent) {
            parent.getChildrenUnmodifiable().stream()
                    .filter(Node::isFocusTraversable)
                    .findFirst()
                    .ifPresent(Node::requestFocus);
        }
    }
}
