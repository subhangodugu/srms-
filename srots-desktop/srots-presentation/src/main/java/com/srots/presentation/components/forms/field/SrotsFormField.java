package com.srots.presentation.components.forms.field;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

/**
 * Label + required marker + control + helper/error/warning messages.
 * Presentation only — no validation logic beyond display state.
 */
public class SrotsFormField extends VBox {

    private final Label label;
    private final Label requiredMark;
    private final Node control;
    private final Label helper;
    private final Label error;
    private final Label warning;
    private String helperText = "";

    public SrotsFormField(String labelText, Node control) {
        this(labelText, control, false, null);
    }

    public SrotsFormField(String labelText, Node control, boolean required, String helperText) {
        super(4);
        getStyleClass().add("srots-form-field");
        this.control = control;

        label = new Label(labelText == null ? "" : labelText);
        label.getStyleClass().add("srots-form-label");

        requiredMark = new Label("*");
        requiredMark.getStyleClass().add("srots-form-required");
        requiredMark.setVisible(required);
        requiredMark.setManaged(required);

        HBox labelRow = new HBox(4, label, requiredMark);
        labelRow.setAlignment(Pos.CENTER_LEFT);

        this.helperText = helperText == null ? "" : helperText;
        helper = new Label(this.helperText);
        helper.getStyleClass().add("srots-form-helper");
        setLabelVisible(helper, !this.helperText.isBlank());

        error = new Label();
        error.getStyleClass().add("srots-form-error");
        setLabelVisible(error, false);

        warning = new Label();
        warning.getStyleClass().add("srots-form-warning");
        setLabelVisible(warning, false);

        VBox.setVgrow(control, Priority.NEVER);
        getChildren().addAll(labelRow, control, helper, error, warning);
    }

    public void setRequired(boolean required) {
        requiredMark.setVisible(required);
        requiredMark.setManaged(required);
    }

    public void setHelperText(String text) {
        helperText = text == null ? "" : text;
        helper.setText(helperText);
        refreshMessageVisibility();
    }

    public void setError(String message) {
        boolean has = message != null && !message.isBlank();
        error.setText(has ? message : "");
        if (has) {
            warning.setText("");
            setLabelVisible(warning, false);
            if (!control.getStyleClass().contains("srots-invalid")) {
                control.getStyleClass().add("srots-invalid");
            }
        } else {
            control.getStyleClass().remove("srots-invalid");
        }
        setLabelVisible(error, has);
        refreshMessageVisibility();
    }

    public void setWarning(String message) {
        boolean has = message != null && !message.isBlank();
        warning.setText(has ? message : "");
        if (has) {
            error.setText("");
            setLabelVisible(error, false);
            control.getStyleClass().remove("srots-invalid");
        }
        setLabelVisible(warning, has);
        refreshMessageVisibility();
    }

    public void clearMessages() {
        error.setText("");
        warning.setText("");
        setLabelVisible(error, false);
        setLabelVisible(warning, false);
        control.getStyleClass().remove("srots-invalid");
        refreshMessageVisibility();
    }

    public Node getControl() {
        return control;
    }

    private void refreshMessageVisibility() {
        boolean showHelper = !helperText.isBlank()
                && !error.isVisible()
                && !warning.isVisible();
        setLabelVisible(helper, showHelper);
    }

    private static void setLabelVisible(Label label, boolean visible) {
        label.setVisible(visible);
        label.setManaged(visible);
    }
}
