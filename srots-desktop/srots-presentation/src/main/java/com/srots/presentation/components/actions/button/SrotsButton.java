package com.srots.presentation.components.actions.button;

import com.srots.presentation.components.utility.SrotsSize;
import com.srots.presentation.components.utility.SrotsStyleClasses;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.Button;

/**
 * SROTS button with variant, size, and loading presentation states.
 */
public class SrotsButton extends Button {

    private final ObjectProperty<SrotsButtonVariant> variant =
            new SimpleObjectProperty<>(SrotsButtonVariant.PRIMARY);
    private final ObjectProperty<SrotsSize> size =
            new SimpleObjectProperty<>(SrotsSize.STANDARD);
    private final BooleanProperty loading = new SimpleBooleanProperty(false);

    public SrotsButton() {
        this("", SrotsButtonVariant.PRIMARY, SrotsSize.STANDARD);
    }

    public SrotsButton(String text) {
        this(text, SrotsButtonVariant.PRIMARY, SrotsSize.STANDARD);
    }

    public SrotsButton(String text, SrotsButtonVariant variant) {
        this(text, variant, SrotsSize.STANDARD);
    }

    public SrotsButton(String text, SrotsButtonVariant variant, SrotsSize size) {
        super(text == null ? "" : text);
        getStyleClass().add(SrotsStyleClasses.BUTTON);
        this.variant.addListener((obs, oldV, newV) -> applyVariant(newV));
        this.size.addListener((obs, oldS, newS) -> applySize(newS));
        this.loading.addListener((obs, was, isLoading) -> applyLoading(isLoading));
        textProperty().addListener((obs, oldT, newT) -> setAccessibleText(newT));
        setAccessibleText(getText());
        setVariant(variant == null ? SrotsButtonVariant.PRIMARY : variant);
        setSize(size == null ? SrotsSize.STANDARD : size);
        applyLoading(false);
    }

    public static SrotsButton primary(String text) {
        return new SrotsButton(text, SrotsButtonVariant.PRIMARY);
    }

    public static SrotsButton secondary(String text) {
        return new SrotsButton(text, SrotsButtonVariant.SECONDARY);
    }

    public static SrotsButton tertiary(String text) {
        return new SrotsButton(text, SrotsButtonVariant.TERTIARY);
    }

    public static SrotsButton danger(String text) {
        return new SrotsButton(text, SrotsButtonVariant.DANGER);
    }

    public ObjectProperty<SrotsButtonVariant> variantProperty() {
        return variant;
    }

    public SrotsButtonVariant getVariant() {
        return variant.get();
    }

    public void setVariant(SrotsButtonVariant value) {
        variant.set(value == null ? SrotsButtonVariant.PRIMARY : value);
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

    public BooleanProperty loadingProperty() {
        return loading;
    }

    public boolean isLoading() {
        return loading.get();
    }

    public void setLoading(boolean value) {
        loading.set(value);
    }

    private void applyVariant(SrotsButtonVariant value) {
        getStyleClass().removeAll(
                SrotsStyleClasses.PRIMARY_BUTTON,
                SrotsStyleClasses.SECONDARY_BUTTON,
                SrotsStyleClasses.TERTIARY_BUTTON,
                SrotsStyleClasses.DANGER_BUTTON
        );
        switch (value) {
            case PRIMARY -> getStyleClass().add(SrotsStyleClasses.PRIMARY_BUTTON);
            case SECONDARY -> getStyleClass().add(SrotsStyleClasses.SECONDARY_BUTTON);
            case TERTIARY -> getStyleClass().add(SrotsStyleClasses.TERTIARY_BUTTON);
            case DANGER -> getStyleClass().add(SrotsStyleClasses.DANGER_BUTTON);
        }
    }

    private void applySize(SrotsSize value) {
        getStyleClass().removeAll("srots-size-small", "srots-size-standard", "srots-size-large");
        switch (value) {
            case SMALL -> getStyleClass().add("srots-size-small");
            case LARGE -> getStyleClass().add("srots-size-large");
            case STANDARD -> getStyleClass().add("srots-size-standard");
        }
    }

    private void applyLoading(boolean isLoading) {
        setDisable(isLoading);
        if (isLoading) {
            if (!getStyleClass().contains("srots-button-loading")) {
                getStyleClass().add("srots-button-loading");
            }
            setOpacity(0.75);
        } else {
            getStyleClass().remove("srots-button-loading");
            setOpacity(1.0);
        }
    }
}
