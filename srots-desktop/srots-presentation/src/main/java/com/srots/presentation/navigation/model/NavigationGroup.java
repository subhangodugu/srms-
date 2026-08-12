package com.srots.presentation.navigation.model;

/**
 * Sidebar / catalog grouping for navigation items.
 */
public enum NavigationGroup {

    OVERVIEW("Overview"),
    WORKSPACE("My Workspace"),
    COMPANY("Company"),
    WORK("Work"),
    PRODUCTS("Products"),
    ENGINEERING("Engineering"),
    RELEASE("Release"),
    BUSINESS("Business"),
    SUPPORT("Support"),
    SYSTEM("System");

    private final String displayTitle;

    NavigationGroup(String displayTitle) {
        this.displayTitle = displayTitle;
    }

    public String displayTitle() {
        return displayTitle;
    }
}
