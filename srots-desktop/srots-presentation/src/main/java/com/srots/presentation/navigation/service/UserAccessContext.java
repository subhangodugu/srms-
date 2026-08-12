package com.srots.presentation.navigation.service;

/**
 * Minimal access snapshot used by navigation visibility and guards.
 * Not a security authority — presentation-only checks.
 */
public interface UserAccessContext {

    boolean hasPermission(String permission);

    boolean hasRole(String role);

    boolean isAuthenticated();
}
