package com.srots.presentation.components.information.kpi;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

/** Equal-height KPI card: label, value, trend. */
public class SrotsKpiCard extends VBox {

    public enum TrendDirection {
        UP, DOWN, NEUTRAL
    }

    private final Label label = new Label();
    private final Label value = new Label();
    private final Label trend = new Label();

    private final StringProperty labelText = new SimpleStringProperty("");
    private final StringProperty valueText = new SimpleStringProperty("");
    private final StringProperty trendText = new SimpleStringProperty("");
    private final ObjectProperty<TrendDirection> trendDirection =
            new SimpleObjectProperty<>(TrendDirection.NEUTRAL);

    public SrotsKpiCard() {
        this("", "", null, TrendDirection.NEUTRAL);
    }

    public SrotsKpiCard(String labelText, String valueText) {
        this(labelText, valueText, null, TrendDirection.NEUTRAL);
    }

    public SrotsKpiCard(String labelText, String valueText, String trendText, TrendDirection direction) {
        getStyleClass().add("srots-kpi-card");
        setAlignment(Pos.TOP_LEFT);
        setFillWidth(true);
        setMinHeight(110);
        setPrefHeight(110);
        VBox.setVgrow(this, Priority.ALWAYS);

        label.getStyleClass().add("srots-kpi-label");
        value.getStyleClass().add("srots-kpi-value");

        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);

        getChildren().addAll(label, value, spacer, trend);

        this.labelText.addListener((o, a, b) -> label.setText(b == null ? "" : b));
        this.valueText.addListener((o, a, b) -> value.setText(b == null ? "" : b));
        this.trendText.addListener((o, a, b) -> applyTrendText(b));
        this.trendDirection.addListener((o, a, b) -> applyTrendTone(b));

        setLabel(labelText);
        setValue(valueText);
        setTrend(trendText, direction == null ? TrendDirection.NEUTRAL : direction);
    }

    public void setLabel(String text) {
        labelText.set(text == null ? "" : text);
    }

    public void setValue(String text) {
        valueText.set(text == null ? "" : text);
    }

    public void setTrend(String text, TrendDirection direction) {
        trendDirection.set(direction == null ? TrendDirection.NEUTRAL : direction);
        trendText.set(text == null ? "" : text);
    }

    public StringProperty labelProperty() {
        return labelText;
    }

    public StringProperty valueProperty() {
        return valueText;
    }

    public StringProperty trendProperty() {
        return trendText;
    }

    public ObjectProperty<TrendDirection> trendDirectionProperty() {
        return trendDirection;
    }

    private void applyTrendText(String text) {
        boolean show = text != null && !text.isBlank();
        trend.setText(show ? text : "");
        trend.setVisible(show);
        trend.setManaged(show);
    }

    private void applyTrendTone(TrendDirection direction) {
        trend.getStyleClass().removeAll(
                "srots-kpi-trend", "srots-kpi-trend-down", "srots-kpi-trend-neutral");
        TrendDirection dir = direction == null ? TrendDirection.NEUTRAL : direction;
        switch (dir) {
            case UP -> trend.getStyleClass().add("srots-kpi-trend");
            case DOWN -> trend.getStyleClass().add("srots-kpi-trend-down");
            case NEUTRAL -> trend.getStyleClass().add("srots-kpi-trend-neutral");
        }
    }
}
