package com.srots.presentation.components.data.search;

import com.srots.presentation.components.utility.icons.SrotsIcon;
import java.util.function.Consumer;
import javafx.animation.PauseTransition;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.util.Duration;

/**
 * Debounced search field with clear button. Emits text via onSearch — does not query data.
 * Optional leading icon and shortcut chip for TopBar chrome.
 */
public class SrotsSearchField extends HBox {

    private final Label leadingIcon = new Label(SrotsIcon.SEARCH.getGlyph());
    private final TextField field = new TextField();
    private final Label shortcutChip = new Label("Ctrl K");
    private final Button clearButton = new Button("×");
    private final StringProperty text = new SimpleStringProperty("");
    private final ObjectProperty<Integer> debounceMillis = new SimpleObjectProperty<>(300);
    private final ObjectProperty<Consumer<String>> onSearch = new SimpleObjectProperty<>();
    private final PauseTransition debounce = new PauseTransition(Duration.millis(300));

    public SrotsSearchField() {
        this("Search…");
    }

    public SrotsSearchField(String prompt) {
        super(6);
        setAlignment(Pos.CENTER_LEFT);
        getStyleClass().add("srots-search-field");

        leadingIcon.getStyleClass().add("srots-search-leading-icon");
        leadingIcon.setMouseTransparent(true);
        leadingIcon.setVisible(false);
        leadingIcon.setManaged(false);

        field.getStyleClass().add("srots-global-search");
        field.setPromptText(prompt == null ? "Search…" : prompt);
        field.setAccessibleText("Search");
        HBox.setHgrow(field, Priority.ALWAYS);

        shortcutChip.getStyleClass().add("srots-search-shortcut");
        shortcutChip.setMouseTransparent(true);
        shortcutChip.setVisible(false);
        shortcutChip.setManaged(false);

        clearButton.getStyleClass().add("srots-icon-button");
        clearButton.setAccessibleText("Clear search");
        clearButton.setFocusTraversable(false);
        clearButton.setVisible(false);
        clearButton.setManaged(false);

        debounce.setOnFinished(e -> emitSearch());
        debounceMillis.addListener((obs, oldV, newV) ->
                debounce.setDuration(Duration.millis(Math.max(0, newV == null ? 300 : newV))));

        field.textProperty().addListener((obs, oldV, newV) -> {
            String value = newV == null ? "" : newV;
            text.set(value);
            boolean hasText = !value.isBlank();
            clearButton.setVisible(hasText);
            clearButton.setManaged(hasText);
            if (hasText) {
                shortcutChip.setVisible(false);
                shortcutChip.setManaged(false);
            } else if (getStyleClass().contains("srots-topbar-search")) {
                shortcutChip.setVisible(true);
                shortcutChip.setManaged(true);
            }
            debounce.stop();
            debounce.playFromStart();
        });

        clearButton.setOnAction(e -> {
            field.clear();
            field.requestFocus();
            emitSearch();
        });

        getChildren().addAll(leadingIcon, field, clearButton, shortcutChip);
    }

    /**
     * Enables TopBar chrome: search glyph + Ctrl K chip.
     */
    public void setTopBarChromeEnabled(boolean enabled) {
        leadingIcon.setVisible(enabled);
        leadingIcon.setManaged(enabled);
        boolean showChip = enabled && (field.getText() == null || field.getText().isBlank());
        shortcutChip.setVisible(showChip);
        shortcutChip.setManaged(showChip);
        if (enabled && !getStyleClass().contains("srots-topbar-search")) {
            getStyleClass().add("srots-topbar-search");
        }
    }

    public Label getShortcutChip() {
        return shortcutChip;
    }

    public Label getLeadingIcon() {
        return leadingIcon;
    }

    private void emitSearch() {
        Consumer<String> handler = onSearch.get();
        if (handler != null) {
            handler.accept(text.get());
        }
    }

    public StringProperty textProperty() {
        return text;
    }

    public String getText() {
        return text.get();
    }

    public void setText(String value) {
        field.setText(value == null ? "" : value);
    }

    public ObjectProperty<Integer> debounceMillisProperty() {
        return debounceMillis;
    }

    public int getDebounceMillis() {
        Integer value = debounceMillis.get();
        return value == null ? 300 : value;
    }

    public void setDebounceMillis(int millis) {
        debounceMillis.set(millis);
    }

    public ObjectProperty<Consumer<String>> onSearchProperty() {
        return onSearch;
    }

    public void setOnSearch(Consumer<String> handler) {
        onSearch.set(handler);
    }

    public Consumer<String> getOnSearch() {
        return onSearch.get();
    }

    public TextField getField() {
        return field;
    }
}
