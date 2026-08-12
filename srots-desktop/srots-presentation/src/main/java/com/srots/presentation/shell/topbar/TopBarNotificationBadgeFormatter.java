package com.srots.presentation.shell.topbar;

/**
 * Formats notification unread counts for the TopBar badge.
 */
public final class TopBarNotificationBadgeFormatter {

    public static final int MAX_DISPLAY = 99;

    private TopBarNotificationBadgeFormatter() {
    }

    public static String format(int count) {
        if (count <= 0) {
            return "";
        }
        if (count > MAX_DISPLAY) {
            return MAX_DISPLAY + "+";
        }
        return Integer.toString(count);
    }

    public static boolean shouldShow(int count) {
        return count > 0;
    }
}
