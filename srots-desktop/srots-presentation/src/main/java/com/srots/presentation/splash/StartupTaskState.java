package com.srots.presentation.splash;

/**
 * Background startup task execution state.
 */
public enum StartupTaskState {
    IDLE,
    RUNNING,
    COMPLETED,
    FAILED,
    CANCELLED
}
