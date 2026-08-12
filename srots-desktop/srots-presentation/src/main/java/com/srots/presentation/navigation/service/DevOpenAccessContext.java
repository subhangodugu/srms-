package com.srots.presentation.navigation.service;

/**
 * Open access context for desktop bootstrap before real authentication is wired.
 */
public final class DevOpenAccessContext implements UserAccessContext {

    @Override
    public boolean hasPermission(String permission) {
        return true;
    }

    @Override
    public boolean hasRole(String role) {
        return true;
    }

    @Override
    public boolean isAuthenticated() {
        return true;
    }
}
