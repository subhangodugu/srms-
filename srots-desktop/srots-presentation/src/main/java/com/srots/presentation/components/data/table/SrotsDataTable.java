package com.srots.presentation.components.data.table;

import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

/**
 * Generic table wrapper with density, loading, and empty states. No data access.
 */
public class SrotsDataTable<T> extends StackPane {

    private final TableView<T> tableView = new TableView<>();
    private final VBox overlay = new VBox(8);
    private final ProgressIndicator loadingIndicator = new ProgressIndicator();
    private final Label placeholderLabel = new Label();
    private String emptyMessage = "No data";
    private boolean loading;

    public SrotsDataTable() {
        getStyleClass().add("srots-data-table");
        tableView.getStyleClass().add("srots-table");
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);
        tableView.setPlaceholder(new Label(emptyMessage));

        overlay.setAlignment(Pos.CENTER);
        overlay.getStyleClass().add("srots-table-overlay");
        overlay.setVisible(false);
        overlay.setManaged(false);
        loadingIndicator.setMaxSize(36, 36);
        placeholderLabel.getStyleClass().add("srots-caption");
        overlay.getChildren().addAll(loadingIndicator, placeholderLabel);

        getChildren().addAll(tableView, overlay);
        setDensity(SrotsTableDensity.STANDARD);
    }

    public TableView<T> getTableView() {
        return tableView;
    }

    public void setItems(ObservableList<T> items) {
        tableView.setItems(items);
        refreshPlaceholder();
    }

    public void setDensity(SrotsTableDensity density) {
        tableView.getStyleClass().removeAll("srots-density-compact", "srots-density-comfortable");
        if (density == null) {
            return;
        }
        switch (density) {
            case COMPACT -> tableView.getStyleClass().add("srots-density-compact");
            case COMFORTABLE -> tableView.getStyleClass().add("srots-density-comfortable");
            case STANDARD -> { /* default table spacing */ }
        }
    }

    public void setLoading(boolean loading) {
        this.loading = loading;
        overlay.setVisible(loading);
        overlay.setManaged(loading);
        loadingIndicator.setVisible(loading);
        placeholderLabel.setText(loading ? "Loading…" : emptyMessage);
        tableView.setDisable(loading);
    }

    public boolean isLoading() {
        return loading;
    }

    public void setEmptyMessage(String message) {
        emptyMessage = message == null || message.isBlank() ? "No data" : message;
        refreshPlaceholder();
    }

    public void enableMultiSelect(boolean enabled) {
        tableView.getSelectionModel().setSelectionMode(
                enabled ? SelectionMode.MULTIPLE : SelectionMode.SINGLE);
    }

    private void refreshPlaceholder() {
        if (!loading) {
            tableView.setPlaceholder(new Label(emptyMessage));
        }
    }
}
