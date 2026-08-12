package com.srots.presentation.components.information.metric;

import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

/** Compact label + value metric. */
public class SrotsMetric extends VBox {

    private final Label label = new Label();
    private final Label value = new Label();

    public SrotsMetric(String labelText, String valueText) {
        super(2);
        getStyleClass().add("srots-metric");

        label.getStyleClass().add("srots-metric-label");
        value.getStyleClass().add("srots-metric-value");

        setLabel(labelText);
        setValue(valueText);
        getChildren().addAll(label, value);
    }

    public void setLabel(String text) {
        label.setText(text == null ? "" : text);
    }

    public void setValue(String text) {
        value.setText(text == null ? "" : text);
    }

    public Label getLabel() {
        return label;
    }

    public Label getValueLabel() {
        return value;
    }
}
