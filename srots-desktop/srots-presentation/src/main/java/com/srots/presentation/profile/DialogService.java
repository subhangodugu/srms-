package com.srots.presentation.profile;

import com.srots.presentation.components.overlays.dialog.SrotsConfirmationDialog;
import com.srots.presentation.components.overlays.dialog.SrotsInformationDialog;
import javafx.stage.Window;

import java.util.concurrent.CompletableFuture;

/**
 * Thin dialog façade for profile actions. Uses existing SROTS dialogs.
 */
public interface DialogService {

    void showAbout(Window owner, AboutInfo info);

    void showError(Window owner, String title, String message);

    CompletableFuture<Boolean> confirmSignOut(Window owner);

    final class Default implements DialogService {
        @Override
        public void showAbout(Window owner, AboutInfo info) {
            AboutInfo safe = info == null ? AboutInfo.of("SROTS", "0.0.0", "desktop") : info;
            SrotsInformationDialog.show(owner, "About " + safe.applicationName(), safe.formatMessage());
        }

        @Override
        public void showError(Window owner, String title, String message) {
            SrotsInformationDialog.show(
                    owner,
                    title == null || title.isBlank() ? "Error" : title,
                    message == null ? "" : message);
        }

        @Override
        public CompletableFuture<Boolean> confirmSignOut(Window owner) {
            return SrotsConfirmationDialog.show(
                    owner,
                    "Sign out",
                    "Sign out of SROTS?",
                    "Sign out",
                    false);
        }
    }
}
