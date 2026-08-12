package com.srots.infrastructure.mock.configuration;

/**
 * Deterministic failure injection for mock repositories.
 */
public enum MockFailurePolicy {
    NEVER,
    OCCASIONAL,
    ALWAYS
}
