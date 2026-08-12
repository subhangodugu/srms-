package com.srots.presentation.navigation.service;

import java.util.Objects;
import java.util.Set;

/**
 * Static permission/role snapshot for tests and future auth wiring.
 * Not a security authority — presentation filtering only.
 */
public final class StaticUserAccessContext implements UserAccessContext {

    private final boolean authenticated;
    private final Set<String> permissions;
    private final Set<String> roles;

    public StaticUserAccessContext(boolean authenticated, Set<String> permissions, Set<String> roles) {
        this.authenticated = authenticated;
        this.permissions = permissions == null ? Set.of() : Set.copyOf(permissions);
        this.roles = roles == null ? Set.of() : Set.copyOf(roles);
    }

    public static StaticUserAccessContext admin() {
        return new StaticUserAccessContext(true, Set.of("*"), Set.of("ADMIN"));
    }

    public static StaticUserAccessContext employee() {
        return new StaticUserAccessContext(
                true,
                Set.of("OVERVIEW", "WORKSPACE", "PROJECTS", "TASKS", "KNOWLEDGE", "SUPPORT"),
                Set.of("EMPLOYEE"));
    }

    @Override
    public boolean hasPermission(String permission) {
        if (permission == null || permission.isBlank()) {
            return true;
        }
        if (permissions.contains("*")) {
            return true;
        }
        return permissions.contains(permission);
    }

    @Override
    public boolean hasRole(String role) {
        if (role == null || role.isBlank()) {
            return true;
        }
        return roles.contains(role);
    }

    @Override
    public boolean isAuthenticated() {
        return authenticated;
    }

    @Override
    public String toString() {
        return "StaticUserAccessContext{authenticated=" + authenticated
                + ", roles=" + roles
                + ", permissions=" + permissions.size()
                + "}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof StaticUserAccessContext that)) {
            return false;
        }
        return authenticated == that.authenticated
                && Objects.equals(permissions, that.permissions)
                && Objects.equals(roles, that.roles);
    }

    @Override
    public int hashCode() {
        return Objects.hash(authenticated, permissions, roles);
    }
}
