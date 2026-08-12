package com.srots.presentation.lifecycle;

import com.srots.presentation.components.overlays.dialog.SrotsConfirmationDialog;
import javafx.stage.Window;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.BooleanSupplier;

/**
 * Application close / shutdown policy for the primary window.
 * Does not contain feature business logic.
 */
public final class ApplicationLifecycleService {

    private static final Logger log = LoggerFactory.getLogger(ApplicationLifecycleService.class);

    public enum CloseDecision {
        ALLOW,
        CANCEL
    }

    private final AtomicReference<BooleanSupplier> unsavedWork = new AtomicReference<>(() -> false);

    public void setUnsavedWorkDetector(BooleanSupplier detector) {
        unsavedWork.set(detector == null ? () -> false : detector);
    }

    /**
     * @return {@link CloseDecision#ALLOW} to proceed with close, {@link CloseDecision#CANCEL} to abort.
     */
    public CloseDecision confirmClose(Window owner) {
        BooleanSupplier detector = unsavedWork.get();
        if (detector == null || !detector.getAsBoolean()) {
            return CloseDecision.ALLOW;
        }
        log.info("Close requested with unsaved work");
        boolean discard = SrotsConfirmationDialog.show(
                owner,
                "Unsaved changes",
                "You have unsaved changes. Discard and close?",
                "Discard",
                true).join();
        return discard ? CloseDecision.ALLOW : CloseDecision.CANCEL;
    }

    public void onForcedShutdown(Throwable error) {
        log.error("Forced application shutdown", Objects.requireNonNullElse(error, new IllegalStateException("shutdown")));
    }
}
