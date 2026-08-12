package com.srots.presentation.components.information.avatar;

/**
 * Initials formatter for avatar fallbacks.
 */
public final class UserInitials {

    private static final int MAX_INITIALS = 3;

    private UserInitials() {
    }

    /**
     * Examples: "Subhan Godogu" → SG, "Subhan" → S, "John Michael Smith" → JMS.
     */
    public static String fromDisplayName(String displayName) {
        if (displayName == null || displayName.isBlank()) {
            return "";
        }
        String trimmed = displayName.trim().replaceAll("\\s+", " ");
        String[] parts = trimmed.split(" ");
        if (parts.length == 1) {
            return parts[0].substring(0, 1).toUpperCase();
        }
        StringBuilder initials = new StringBuilder();
        int limit = Math.min(parts.length, MAX_INITIALS);
        for (int i = 0; i < limit; i++) {
            String part = parts[i];
            if (!part.isBlank()) {
                initials.append(Character.toUpperCase(part.charAt(0)));
            }
        }
        return initials.toString();
    }
}
