package com.srots.presentation.components.information.timeline;

import java.util.List;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

/** Vertical timeline using srots-release-gate connector styles. */
public class SrotsTimeline extends VBox {

    public SrotsTimeline() {
        super(6);
        getStyleClass().add("srots-release-gate");
    }

    public void setItems(List<SrotsTimelineItem> items) {
        getChildren().clear();
        if (items == null) {
            return;
        }
        for (SrotsTimelineItem item : items) {
            if (item == null) {
                continue;
            }
            VBox step = new VBox(2);
            Label title = new Label(formatTitle(item));
            title.getStyleClass().addAll("srots-release-gate-step", styleFor(item.state()));
            step.getChildren().add(title);
            if (item.description() != null && !item.description().isBlank()) {
                Label desc = new Label(item.description());
                desc.getStyleClass().add("srots-caption");
                step.getChildren().add(desc);
            }
            getChildren().add(step);
        }
    }

    private static String formatTitle(SrotsTimelineItem item) {
        String marker = switch (item.state()) {
            case DONE -> "✓";
            case WARN -> "⚠";
            case PENDING -> "○";
        };
        String title = item.title() == null ? "" : item.title();
        return title.isBlank() ? marker : title + " " + marker;
    }

    private static String styleFor(SrotsTimelineItem.State state) {
        if (state == null) {
            return "srots-release-gate-pending";
        }
        return switch (state) {
            case DONE -> "srots-release-gate-done";
            case WARN -> "srots-release-gate-warn";
            case PENDING -> "srots-release-gate-pending";
        };
    }
}
