package com.srots.presentation.components.data.filter;

import java.util.List;
import com.srots.presentation.components.data.search.SrotsSearchField;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;

/**
 * Filter bar: search slot, filter dropdowns, active chips, clear-all.
 * Presentation only.
 */
public class SrotsFilterBar extends HBox {

    private final HBox searchSlot = new HBox();
    private final HBox filtersSlot = new HBox(8);
    private final FlowPane chipsPane = new FlowPane(6, 6);
    private final Button clearAll = new Button("Clear all");
    private Runnable onClearAll;

    public SrotsFilterBar() {
        super(12);
        setAlignment(Pos.CENTER_LEFT);
        getStyleClass().add("srots-filter-bar");

        searchSlot.setAlignment(Pos.CENTER_LEFT);
        filtersSlot.setAlignment(Pos.CENTER_LEFT);
        chipsPane.getStyleClass().add("srots-filter-chips");
        HBox.setHgrow(chipsPane, Priority.ALWAYS);

        clearAll.getStyleClass().addAll("srots-button", "srots-tertiary-button");
        clearAll.setOnAction(e -> {
            if (onClearAll != null) {
                onClearAll.run();
            }
        });

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.SOMETIMES);

        getChildren().addAll(searchSlot, filtersSlot, chipsPane, spacer, clearAll);
    }

    public void setSearchField(SrotsSearchField searchField) {
        searchSlot.getChildren().setAll(searchField == null ? List.<Node>of() : List.of(searchField));
    }

    public void addFilter(SrotsFilterDropdown<?> filter) {
        if (filter != null) {
            filtersSlot.getChildren().add(filter);
        }
    }

    public void addFilter(Node filterNode) {
        if (filterNode != null) {
            filtersSlot.getChildren().add(filterNode);
        }
    }

    public void clearFilters() {
        filtersSlot.getChildren().clear();
    }

    public void setActiveFilters(List<SrotsFilterChip> chips) {
        chipsPane.getChildren().clear();
        if (chips != null) {
            chipsPane.getChildren().addAll(chips);
        }
    }

    public void onClearAll(Runnable handler) {
        this.onClearAll = handler;
    }

    public FlowPane getChipsPane() {
        return chipsPane;
    }
}
