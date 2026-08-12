package com.srots.presentation.splash;

/**
 * Discrete application startup phases reported to the splash UI.
 */
public enum StartupPhase {
    BOOTSTRAP(0.05, "Starting SROTS..."),
    CONFIGURATION(0.15, "Loading configuration..."),
    LOGGING(0.20, "Preparing logging..."),
    DEPENDENCIES(0.40, "Initializing application services..."),
    DATA(0.55, "Preparing workspace data..."),
    NAVIGATION(0.70, "Preparing workspace..."),
    THEME(0.80, "Loading interface theme..."),
    UI(0.90, "Loading interface..."),
    READY(1.0, "Application ready."),
    FAILED(0.0, "SROTS could not start.");

    private final double progress;
    private final String defaultMessage;

    StartupPhase(double progress, String defaultMessage) {
        this.progress = progress;
        this.defaultMessage = defaultMessage;
    }

    public double progress() {
        return progress;
    }

    public String defaultMessage() {
        return defaultMessage;
    }
}
