package com.srots.presentation.splash;

import java.util.Objects;

/**
 * Immutable startup progress snapshot for splash presentation.
 */
public record StartupProgress(StartupPhase phase, double progress, String message) {

    public StartupProgress {
        Objects.requireNonNull(phase, "phase");
        progress = Math.max(0.0, Math.min(1.0, progress));
        message = message == null || message.isBlank() ? phase.defaultMessage() : message;
    }

    public static StartupProgress of(StartupPhase phase) {
        return new StartupProgress(phase, phase.progress(), phase.defaultMessage());
    }

    public static StartupProgress of(StartupPhase phase, String message) {
        return new StartupProgress(phase, phase.progress(), message);
    }

    public static StartupProgress failed(String message) {
        return new StartupProgress(StartupPhase.FAILED, 0.0, message);
    }
}
