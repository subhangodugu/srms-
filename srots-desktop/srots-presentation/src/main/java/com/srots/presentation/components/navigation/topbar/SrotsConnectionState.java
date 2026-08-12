package com.srots.presentation.components.navigation.topbar;

/** Connection / sync presentation states for status indicators. */
public enum SrotsConnectionState {
    ONLINE,
    OFFLINE,
    SYNCING,
    SYNC_ERROR,
    /** Status cannot be determined — never pretend Online. */
    UNKNOWN
}
