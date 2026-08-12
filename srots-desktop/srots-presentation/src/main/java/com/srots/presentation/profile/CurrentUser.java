package com.srots.presentation.profile;

/**
 * Presentation/session user snapshot for the profile UI.
 * Not an employee entity and contains no secrets.
 */
public record CurrentUser(
        String id,
        String displayName,
        String email,
        String role,
        String department,
        String avatarResourceKey) {

    public static CurrentUser fallback() {
        return new CurrentUser("unknown", "User", "", "", "", null);
    }

    public CurrentUser {
        id = blankTo(id, "unknown");
        displayName = blankTo(displayName, "User");
        email = email == null ? "" : email.trim();
        role = role == null ? "" : role.trim();
        department = department == null ? "" : department.trim();
        avatarResourceKey = avatarResourceKey == null || avatarResourceKey.isBlank()
                ? null
                : avatarResourceKey.trim();
    }

    public boolean hasEmail() {
        return !email.isBlank();
    }

    public boolean hasRole() {
        return !role.isBlank();
    }

    private static String blankTo(String value, String fallback) {
        return value == null || value.isBlank() ? fallback : value.trim();
    }
}
