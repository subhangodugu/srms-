package com.srots.presentation.profile;

/**
 * Authentication boundary used by the profile menu.
 * Implementations own token/session clearing — the UI never touches secrets.
 */
public interface AuthenticationService {

    /**
     * Requests sign-out. Safe to call repeatedly; concurrent requests are ignored.
     */
    void signOut();

    boolean isSigningOut();
}
