package com.srots.presentation.components.information.badge;

/**
 * Semantic status vocabulary for badges.
 * Always pair with text/icon — never color alone.
 */
public enum SrotsStatus {
    ACTIVE("Active", "✓"),
    INACTIVE("Inactive", "○"),
    PENDING("Pending", "⚠"),
    APPROVED("Approved", "✓"),
    REJECTED("Rejected", "✕"),
    BLOCKED("Blocked", "✕"),
    FAILED("Failed", "✕"),
    WARNING("Warning", "⚠"),
    HEALTHY("Healthy", "✓"),
    DEGRADED("Degraded", "⚠"),
    PRODUCTION("Production", "●"),
    STAGING("Staging", "●"),
    DEVELOPMENT("Development", "●"),
    INFO("Info", "●");

    private final String label;
    private final String icon;

    SrotsStatus(String label, String icon) {
        this.label = label;
        this.icon = icon;
    }

    public String getLabel() {
        return label;
    }

    public String getIcon() {
        return icon;
    }
}
