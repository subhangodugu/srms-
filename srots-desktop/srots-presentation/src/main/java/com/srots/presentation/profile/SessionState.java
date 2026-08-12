package com.srots.presentation.profile;

/**
 * Session lifecycle for profile / chrome presentation.
 * Authentication mechanics live outside the UI.
 */
public enum SessionState {
    AUTHENTICATED,
    SIGNING_OUT,
    SIGNED_OUT,
    SESSION_EXPIRED
}
