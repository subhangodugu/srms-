package com.srots.presentation.navigation.model;

/**
 * Origin of a navigation request. All sources converge on {@code NavigationService}.
 */
public enum NavigationSource {
    SIDEBAR,
    TOPBAR,
    SEARCH,
    NOTIFICATION,
    COMMAND,
    BREADCRUMB,
    PROFILE,
    SYSTEM,
    HISTORY,
    SHORTCUT,
    UNKNOWN
}
