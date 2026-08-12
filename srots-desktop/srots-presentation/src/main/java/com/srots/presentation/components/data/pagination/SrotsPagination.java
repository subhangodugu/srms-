package com.srots.presentation.components.data.pagination;

import java.util.function.Consumer;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.LongProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

/**
 * Pagination controls bound to page state. Emits page changes — does not fetch data.
 */
public class SrotsPagination extends HBox {

    private final IntegerProperty page = new SimpleIntegerProperty(0);
    private final IntegerProperty pageSize = new SimpleIntegerProperty(20);
    private final LongProperty totalRecords = new SimpleLongProperty(0);
    private final ObjectProperty<Consumer<Integer>> onPageChange = new SimpleObjectProperty<>();

    private final Button first = secondary("First");
    private final Button previous = secondary("Prev");
    private final Label pageLabel = new Label();
    private final Button next = secondary("Next");
    private final Button last = secondary("Last");

    public SrotsPagination() {
        super(8);
        setAlignment(Pos.CENTER_LEFT);
        getStyleClass().add("srots-pagination");

        pageLabel.getStyleClass().add("srots-pagination-label");

        first.setOnAction(e -> goTo(0));
        previous.setOnAction(e -> goTo(page.get() - 1));
        next.setOnAction(e -> goTo(page.get() + 1));
        last.setOnAction(e -> goTo(totalPages() - 1));

        page.addListener((o, a, b) -> refresh());
        pageSize.addListener((o, a, b) -> refresh());
        totalRecords.addListener((o, a, b) -> refresh());

        getChildren().addAll(first, previous, pageLabel, next, last);
        refresh();
    }

    public void bind(PaginationState state) {
        if (state == null) {
            return;
        }
        page.set(state.getPage());
        pageSize.set(state.getPageSize());
        totalRecords.set(state.getTotalRecords());
    }

    public void applyTo(PaginationState state) {
        if (state == null) {
            return;
        }
        state.setPageSize(pageSize.get());
        state.setTotalRecords(totalRecords.get());
        state.setPage(page.get());
    }

    private void goTo(int target) {
        int clamped = clamp(target);
        if (clamped != page.get()) {
            page.set(clamped);
            Consumer<Integer> handler = onPageChange.get();
            if (handler != null) {
                handler.accept(clamped);
            }
        } else {
            refresh();
        }
    }

    private void refresh() {
        int total = totalPages();
        int current = page.get() + 1;
        pageLabel.setText("Page " + current + " of " + total);
        first.setDisable(page.get() <= 0);
        previous.setDisable(page.get() <= 0);
        next.setDisable(page.get() >= total - 1);
        last.setDisable(page.get() >= total - 1);
    }

    private int totalPages() {
        long records = Math.max(0, totalRecords.get());
        int size = Math.max(1, pageSize.get());
        if (records == 0) {
            return 1;
        }
        return (int) Math.ceil((double) records / size);
    }

    private int clamp(int candidate) {
        int max = Math.max(0, totalPages() - 1);
        return Math.max(0, Math.min(candidate, max));
    }

    private static Button secondary(String text) {
        Button button = new Button(text);
        button.getStyleClass().addAll("srots-button", "srots-secondary-button");
        return button;
    }

    public IntegerProperty pageProperty() {
        return page;
    }

    public IntegerProperty pageSizeProperty() {
        return pageSize;
    }

    public LongProperty totalRecordsProperty() {
        return totalRecords;
    }

    public void setOnPageChange(Consumer<Integer> handler) {
        onPageChange.set(handler);
    }

    public Consumer<Integer> getOnPageChange() {
        return onPageChange.get();
    }
}
