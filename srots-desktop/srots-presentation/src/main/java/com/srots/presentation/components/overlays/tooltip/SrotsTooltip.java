package com.srots.presentation.components.overlays.tooltip;

import javafx.scene.Node;
import javafx.scene.control.Tooltip;

/** Installs a styled SROTS tooltip on a node. */
public final class SrotsTooltip {

    private SrotsTooltip() {}

    public static Tooltip install(Node node, String text) {
        Tooltip tip = new Tooltip(text == null ? "" : text);
        tip.getStyleClass().add("srots-tooltip");
        if (node != null) {
            Tooltip.install(node, tip);
        }
        return tip;
    }
}
