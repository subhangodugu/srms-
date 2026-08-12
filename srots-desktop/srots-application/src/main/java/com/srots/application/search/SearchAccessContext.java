package com.srots.application.search;

import java.util.Set;

/**
 * Presentation-agnostic access snapshot for search authorization filtering.
 * Not a security authority — mirrors what the application/backend already decided.
 */
public interface SearchAccessContext {

    boolean isAuthenticated();

    boolean hasPermission(String permission);

    boolean canAccess(SearchEntityType type);

    static SearchAccessContext allowAll() {
        return new SearchAccessContext() {
            @Override
            public boolean isAuthenticated() {
                return true;
            }

            @Override
            public boolean hasPermission(String permission) {
                return true;
            }

            @Override
            public boolean canAccess(SearchEntityType type) {
                return true;
            }
        };
    }

    static SearchAccessContext of(boolean authenticated, Set<String> permissions) {
        Set<String> safe = permissions == null ? Set.of() : Set.copyOf(permissions);
        return new SearchAccessContext() {
            @Override
            public boolean isAuthenticated() {
                return authenticated;
            }

            @Override
            public boolean hasPermission(String permission) {
                if (permission == null || permission.isBlank()) {
                    return true;
                }
                return safe.contains("*") || safe.contains(permission);
            }

            @Override
            public boolean canAccess(SearchEntityType type) {
                if (!authenticated) {
                    return false;
                }
                if (safe.contains("*")) {
                    return true;
                }
                if (type == null) {
                    return false;
                }
                return switch (type) {
                    case EMPLOYEE -> hasPermission("COMPANY_EMPLOYEES") || hasPermission("OVERVIEW");
                    case PROJECT -> hasPermission("PROJECTS") || hasPermission("OVERVIEW");
                    case CUSTOMER -> hasPermission("SALES") || hasPermission("OVERVIEW");
                    case PRODUCT -> hasPermission("PRODUCTS") || hasPermission("OVERVIEW");
                    case TASK -> hasPermission("TASKS") || hasPermission("WORKSPACE") || hasPermission("OVERVIEW");
                    case RELEASE -> hasPermission("RELEASES") || hasPermission("OVERVIEW");
                    case SERVICE_DESK -> hasPermission("SUPPORT") || hasPermission("OVERVIEW");
                    case COMPTY -> hasPermission("COMPTY") || hasPermission("PRODUCTS") || hasPermission("OVERVIEW");
                    case KNOWLEDGE -> hasPermission("KNOWLEDGE") || hasPermission("OVERVIEW");
                    case SETTINGS -> hasPermission("SETTINGS") || hasPermission("*");
                };
            }
        };
    }
}
