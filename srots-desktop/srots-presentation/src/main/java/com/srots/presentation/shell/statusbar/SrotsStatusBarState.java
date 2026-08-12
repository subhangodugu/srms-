package com.srots.presentation.shell.statusbar;

/**
 * Aggregate StatusBar presentation state with deterministic priority.
 */
public enum SrotsStatusBarState {
    ERROR,
    OFFLINE,
    DEGRADED,
    SYNCING,
    BUSY,
    READY;

    public static SrotsStatusBarState resolve(
            boolean hasError,
            boolean offline,
            boolean degraded,
            boolean syncing,
            boolean busy) {
        if (hasError) {
            return ERROR;
        }
        if (offline) {
            return OFFLINE;
        }
        if (degraded) {
            return DEGRADED;
        }
        if (syncing) {
            return SYNCING;
        }
        if (busy) {
            return BUSY;
        }
        return READY;
    }
}
