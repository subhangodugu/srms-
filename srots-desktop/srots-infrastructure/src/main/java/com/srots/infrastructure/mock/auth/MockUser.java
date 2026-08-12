package com.srots.infrastructure.mock.auth;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

/**
 * DEVELOPMENT ONLY mock principal. Never ship credentials; roles are labels for UI testing.
 */
public final class MockUser {

    private final String id;
    private final String displayName;
    private final String role;
    private final Set<String> permissions;

    public MockUser(String id, String displayName, String role, Set<String> permissions) {
        this.id = Objects.requireNonNull(id);
        this.displayName = Objects.requireNonNull(displayName);
        this.role = Objects.requireNonNull(role);
        this.permissions = Collections.unmodifiableSet(new LinkedHashSet<>(permissions));
    }

    public String getId() { return id; }
    public String getDisplayName() { return displayName; }
    public String getRole() { return role; }
    public Set<String> getPermissions() { return permissions; }

    public boolean hasPermission(String permission) {
        return permission != null && (permissions.contains("*") || permissions.contains(permission));
    }
}
