package com.srots.presentation.navigation.lifecycle;

/**
 * Optional lifecycle hooks for feature views hosted in ContentHost.
 */
public interface SrotsViewLifecycle {

    default void onActivate() {
    }

    default void onDeactivate() {
    }

    default void onDispose() {
    }
}
