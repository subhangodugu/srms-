package com.srots.presentation.components.charts;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Arc;
import javafx.scene.shape.ArcType;

/**
 * Simple Arc-based donut with percentage. Stroke colors via theme CSS classes.
 */
public class SrotsDonutChart extends VBox {

    private final Label titleLabel = new Label();
    private final Label percentLabel = new Label();
    private final Arc track = new Arc(0, 0, 48, 48, 90, 360);
    private final Arc valueArc = new Arc(0, 0, 48, 48, 90, 0);
    private double percentage;

    public SrotsDonutChart() {
        this("Donut", 0);
    }

    public SrotsDonutChart(String title, double percentage) {
        getStyleClass().addAll("srots-dashboard-chart-panel", "srots-chart-host");
        setSpacing(8);
        setAlignment(Pos.CENTER_LEFT);

        titleLabel.getStyleClass().add("srots-section-title");
        percentLabel.getStyleClass().add("srots-state-title");

        track.setType(ArcType.OPEN);
        track.setStrokeWidth(10);
        track.setFill(null);
        track.getStyleClass().add("srots-chart-donut-track");

        valueArc.setType(ArcType.OPEN);
        valueArc.setStrokeWidth(10);
        valueArc.setFill(null);
        valueArc.getStyleClass().add("srots-chart-donut-value");

        StackPane donut = new StackPane(track, valueArc, percentLabel);
        donut.setPrefSize(120, 120);

        getChildren().addAll(titleLabel, donut);
        setTitle(title);
        setPercentage(percentage);
    }

    public void setTitle(String title) {
        titleLabel.setText(title == null ? "" : title);
    }

    public void setPercentage(double percentage) {
        this.percentage = Math.max(0, Math.min(100, percentage));
        valueArc.setLength(-(this.percentage / 100.0) * 360.0);
        percentLabel.setText(Math.round(this.percentage) + "%");
    }

    public double getPercentage() {
        return percentage;
    }
}
