package com.srots.presentation.profile;

/**
 * Coordinates exclusive TopBar popups (profile / notifications / palette).
 */
public final class SrotsPopupManager {

    public enum PopupKind {
        NONE,
        PROFILE,
        NOTIFICATIONS,
        COMMAND_PALETTE,
        GLOBAL_SEARCH
    }

    private PopupKind openKind = PopupKind.NONE;
    private Runnable closeCurrent;

    public synchronized void requestOpen(PopupKind kind, Runnable closer) {
        if (kind == null || kind == PopupKind.NONE) {
            return;
        }
        if (openKind == kind) {
            return;
        }
        closeCurrentQuietly();
        openKind = kind;
        closeCurrent = closer;
    }

    public synchronized void notifyClosed(PopupKind kind) {
        if (kind != null && openKind == kind) {
            openKind = PopupKind.NONE;
            closeCurrent = null;
        }
    }

    public synchronized void closeAll() {
        closeCurrentQuietly();
        openKind = PopupKind.NONE;
        closeCurrent = null;
    }

    public synchronized PopupKind getOpenKind() {
        return openKind;
    }

    public synchronized boolean isOpen(PopupKind kind) {
        return openKind == kind;
    }

    private void closeCurrentQuietly() {
        Runnable closer = closeCurrent;
        closeCurrent = null;
        openKind = PopupKind.NONE;
        if (closer != null) {
            try {
                closer.run();
            } catch (RuntimeException ignored) {
                // presentation-only coordination
            }
        }
    }
}
