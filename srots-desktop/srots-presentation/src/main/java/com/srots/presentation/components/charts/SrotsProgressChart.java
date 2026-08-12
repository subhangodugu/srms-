package com.srots.presentation.components.charts;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

/** Progress bar + percentage label. */
public class SrotsProgressChart extends VBox {

    private final Label titleLabel = new Label();
    private final ProgressBar progressBar = new ProgressBar(0);
    private final Label percentLabel = new Label("0%");

    public SrotsProgressChart() {
        this("Progress");
    }

    public SrotsProgressChart(String title) {
        getStyleClass().add("srots-dashboard-chart-panel");
        setSpacing(8);

        titleLabel.getStyleClass().add("srots-section-title");
        setTitle(title);

        progressBar.getStyleClass().add("srots-progress");
        progressBar.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(progressBar, Priority.ALWAYS);

        percentLabel.getStyleClass().add("srots-caption");

        HBox row = new HBox(12, progressBar, percentLabel);
        row.setAlignment(Pos.CENTER_LEFT);
        getChildren().addAll(titleLabel, row);
    }

    public void setTitle(String title) {
        titleLabel.setText(title == null ? "" : title);
    }

    /** @param value 0.0–1.0 */
    public void setProgress(double value) {
        double clamped = Math.max(0, Math.min(1, value));
        progressBar.setProgress(clamped);
        percentLabel.setText(Math.round(clamped * 100) + "%");
    }

    public void setPercentage(double percentage) {
        setProgress(percentage / 100.0);
    }
}
