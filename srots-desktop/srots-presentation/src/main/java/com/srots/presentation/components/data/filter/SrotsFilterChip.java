package com.srots.presentation.components.data.filter;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

/** Active filter chip with clear (×). Presentation only. */
public class SrotsFilterChip extends HBox {

    private final Label textLabel = new Label();
    private Runnable onClear;

    public SrotsFilterChip(String text) {
        this(text, null);
    }

    public SrotsFilterChip(String text, Runnable onClear) {
        super(4);
        setAlignment(Pos.CENTER_LEFT);
        getStyleClass().add("srots-chip");
        this.onClear = onClear;

        textLabel.setText(text == null ? "" : text);
        textLabel.getStyleClass().add("srots-chip-label");

        Button clear = new Button("×");
        clear.getStyleClass().add("srots-chip-clear");
        clear.setAccessibleText("Remove filter " + textLabel.getText());
        clear.setFocusTraversable(false);
        clear.setOnAction(e -> {
            if (this.onClear != null) {
                this.onClear.run();
            }
        });

        getChildren().addAll(textLabel, clear);
    }

    public void setText(String text) {
        textLabel.setText(text == null ? "" : text);
    }

    public String getText() {
        return textLabel.getText();
    }

    public void setOnClear(Runnable onClear) {
        this.onClear = onClear;
    }
}
