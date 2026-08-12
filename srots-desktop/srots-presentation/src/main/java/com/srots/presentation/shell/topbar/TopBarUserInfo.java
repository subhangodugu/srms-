package com.srots.presentation.shell.topbar;

/**
 * Safe display snapshot for the TopBar profile slot (no secrets).
 */
public record TopBarUserInfo(String displayName, String roleLabel) {

    public static TopBarUserInfo fallback() {
        return new TopBarUserInfo("User", "");
    }

    public TopBarUserInfo {
        displayName = displayName == null || displayName.isBlank() ? "User" : displayName.trim();
        roleLabel = roleLabel == null ? "" : roleLabel.trim();
    }
}
