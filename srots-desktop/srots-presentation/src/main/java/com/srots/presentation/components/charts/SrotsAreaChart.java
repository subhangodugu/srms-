package com.srots.presentation.components.charts;

import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

/**
 * Area chart host foundation (no javafx-charts dependency).
 */
public class SrotsAreaChart extends VBox {

    private final Label titleLabel = new Label();
    private final Label captionLabel = new Label("Area chart host");

    public SrotsAreaChart() {
        this("Area chart");
    }

    public SrotsAreaChart(String title) {
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
        captionLabel.setText(caption == null || caption.isBlank() ? "Area chart host" : caption);
    }
}
