package com.srots.presentation.components.charts;

import java.util.ArrayList;
import java.util.List;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;

/**
 * Lightweight bar visualization without javafx-charts dependency.
 * Colors come from theme style classes — not raw hex in Java.
 */
public class SrotsBarChart extends VBox {

    public enum Series {
        PRIMARY("srots-chart-bar"),
        SUCCESS("srots-chart-bar-success"),
        WARNING("srots-chart-bar-warning"),
        DANGER("srots-chart-bar-danger"),
        INFO("srots-chart-bar-info"),
        NEUTRAL("srots-chart-bar-neutral");

        private final String styleClass;

        Series(String styleClass) {
            this.styleClass = styleClass;
        }

        public String getStyleClass() {
            return styleClass;
        }
    }

    public record BarPoint(String label, double value, Series series) {
        public BarPoint(String label, double value) {
            this(label, value, Series.PRIMARY);
        }
    }

    private final Label titleLabel = new Label();
    private final HBox bars = new HBox(12);
    private final List<BarPoint> data = new ArrayList<>();

    public SrotsBarChart() {
        this("Bar chart");
    }

    public SrotsBarChart(String title) {
        getStyleClass().addAll("srots-dashboard-chart-panel", "srots-chart-host");
        setSpacing(8);

        titleLabel.getStyleClass().add("srots-section-title");
        setTitle(title);

        bars.setAlignment(Pos.BOTTOM_LEFT);
        getChildren().addAll(titleLabel, bars);
    }

    public void setTitle(String title) {
        titleLabel.setText(title == null ? "" : title);
    }

    public void setData(List<BarPoint> points) {
        data.clear();
        if (points != null) {
            data.addAll(points);
        }
        rebuild();
    }

    private void rebuild() {
        bars.getChildren().clear();
        double max = data.stream().mapToDouble(BarPoint::value).max().orElse(1);
        if (max <= 0) {
            max = 1;
        }
        for (BarPoint point : data) {
            double height = Math.max(8, (point.value() / max) * 120);
            Rectangle rect = new Rectangle(40, height);
            rect.setArcWidth(6);
            rect.setArcHeight(6);
            Series series = point.series() == null ? Series.PRIMARY : point.series();
            rect.getStyleClass().add(series.getStyleClass());
            Label caption = new Label(point.label() == null ? "" : point.label());
            caption.getStyleClass().add("srots-caption");
            VBox col = new VBox(6, rect, caption);
            col.setAlignment(Pos.BOTTOM_CENTER);
            bars.getChildren().add(col);
        }
    }
}
