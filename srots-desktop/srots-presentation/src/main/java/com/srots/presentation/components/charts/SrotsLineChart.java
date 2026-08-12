package com.srots.presentation.components.charts;

import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

/**
 * Line chart host foundation (no javafx-charts dependency).
 */
public class SrotsLineChart extends VBox {

    private final Label titleLabel = new Label();
    private final Label captionLabel = new Label("Line chart host");

    public SrotsLineChart() {
        this("Line chart");
    }

    public SrotsLineChart(String title) {
        getStyleClass().add("srots-dashboard-chart-panel");
        setSpacing(8);
        titleLabel.getStyleClass().add("srots-section-title");
        captionLabel.getStyleClass().add("srots-caption");
        setTitle(title);
        getChildren().addAll(titleLabel, captionLabel);
    }

    public void setTitle(String title) {
        titleLabel.setText(title == null ? "" : title);
    }

    public void setCaption(String caption) {
        captionLabel.setText(caption == null || caption.isBlank() ? "Line chart host" : caption);
    }
}
