package com.srots.presentation.components.actions.icon;

import com.srots.presentation.components.utility.SrotsSize;
import com.srots.presentation.components.utility.SrotsStyleClasses;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;

/**
 * Icon-only button requiring tooltip and accessible text.
 */
public class SrotsIconButton extends Button {

    private final ObjectProperty<SrotsSize> size =
            new SimpleObjectProperty<>(SrotsSize.STANDARD);

    public SrotsIconButton(String graphicText, String tooltip, String accessibleText) {
        this(graphicText, tooltip, accessibleText, SrotsSize.STANDARD);
    }

    public SrotsIconButton(String graphicText, String tooltip, String accessibleText, SrotsSize size) {
        super(graphicText == null ? "" : graphicText);
        if (tooltip == null || tooltip.isBlank()) {
            throw new IllegalArgumentException("tooltip is required");
        }
        if (accessibleText == null || accessibleText.isBlank()) {
            throw new IllegalArgumentException("accessibleText is required");
        }
        getStyleClass().add(SrotsStyleClasses.ICON_BUTTON);
        setTooltip(new Tooltip(tooltip));
        setAccessibleText(accessibleText);
        this.size.addListener((obs, oldS, newS) -> applySize(newS));
        setSize(size == null ? SrotsSize.STANDARD : size);
    }

    public ObjectProperty<SrotsSize> sizeProperty() {
        return size;
    }

    public SrotsSize getSize() {
        return size.get();
    }

    public void setSize(SrotsSize value) {
        size.set(value == null ? SrotsSize.STANDARD : value);
    }

    private void applySize(SrotsSize value) {
        getStyleClass().removeAll("srots-size-small", "srots-size-standard", "srots-size-large");
        switch (value) {
            case SMALL -> getStyleClass().add("srots-size-small");
            case LARGE -> getStyleClass().add("srots-size-large");
            case STANDARD -> getStyleClass().add("srots-size-standard");
        }
    }
}
