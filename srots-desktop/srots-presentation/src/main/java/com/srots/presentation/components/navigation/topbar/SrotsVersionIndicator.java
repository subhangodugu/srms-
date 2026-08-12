package com.srots.presentation.components.navigation.topbar;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.Label;

/** Displays application version text in the status bar. */
public class SrotsVersionIndicator extends Label {

    private final StringProperty version = new SimpleStringProperty("0.1.0-SNAPSHOT");

    public SrotsVersionIndicator() {
        getStyleClass().add("srots-secondary-text");
        textProperty().bind(version.map(v -> "Version " + v));
    }

    public SrotsVersionIndicator(String version) {
        this();
        setVersion(version);
    }

    public StringProperty versionProperty() {
        return version;
    }

    public void setVersion(String value) {
        version.set(value == null ? "" : value);
    }

    public String getVersion() {
        return version.get();
    }
}
