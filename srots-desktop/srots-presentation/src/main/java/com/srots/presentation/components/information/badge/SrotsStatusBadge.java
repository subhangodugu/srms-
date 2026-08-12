package com.srots.presentation.components.information.badge;

import javafx.scene.control.Label;

/**
 * Status badge with optional icon. Maps {@link SrotsStatus} to semantic CSS classes.
 */
public class SrotsStatusBadge extends Label {

    private SrotsStatus status;
    private boolean showIcon = true;

    public SrotsStatusBadge(SrotsStatus status) {
        getStyleClass().add("srots-badge");
        setStatus(status);
    }

    public void setStatus(SrotsStatus status) {
        this.status = status;
        removeToneClasses();
        if (status == null) {
            setText("");
            getStyleClass().add("srots-badge-neutral");
            return;
        }
        getStyleClass().add(toneClassFor(status));
        refreshText();
    }

    public SrotsStatus getStatus() {
        return status;
    }

    public void setShowIcon(boolean showIcon) {
        this.showIcon = showIcon;
        refreshText();
    }

    public boolean isShowIcon() {
        return showIcon;
    }

    private void refreshText() {
        if (status == null) {
            setText("");
            return;
        }
        if (showIcon) {
            setText(status.getIcon() + " " + status.getLabel());
        } else {
            setText(status.getLabel());
        }
    }

    private void removeToneClasses() {
        getStyleClass().removeAll(
                "srots-badge-success",
                "srots-badge-warning",
                "srots-badge-danger",
                "srots-badge-info",
                "srots-badge-neutral");
    }

    private static String toneClassFor(SrotsStatus status) {
        return switch (status) {
            case ACTIVE, APPROVED, HEALTHY, PRODUCTION -> "srots-badge-success";
            case PENDING, WARNING, DEGRADED, STAGING -> "srots-badge-warning";
            case REJECTED, BLOCKED, FAILED, INACTIVE -> "srots-badge-danger";
            case INFO, DEVELOPMENT -> "srots-badge-info";
        };
    }
}
