package com.srots.shared.query;

/**
 * UI / use-case load state independent of data source.
 */
public enum LoadState {
    IDLE,
    LOADING,
    SUCCESS,
    EMPTY,
    ERROR,
    OFFLINE
}
