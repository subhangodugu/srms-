package com.srots.presentation.components.navigation.sidebar;

/**
 * Formats navigation badge values for presentation (no business rules).
 */
public final class NavigationBadgeFormatter {

    public static final int MAX_DISPLAY = 999;

    private NavigationBadgeFormatter() {
    }

    /**
     * @return display text, or empty when there is nothing to show
     */
    public static String format(Integer count) {
        if (count == null || count <= 0) {
            return "";
        }
        if (count > MAX_DISPLAY) {
            return MAX_DISPLAY + "+";
        }
        return Integer.toString(count);
    }

    public static boolean shouldShow(Integer count) {
        return count != null && count > 0;
    }
}
