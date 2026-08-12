package com.srots.infrastructure.mock.configuration;

/**
 * Simulated repository latency for loading-state UI tests.
 */
public enum MockLatency {
    NONE(0, 0),
    FAST(50, 100),
    NORMAL(200, 500),
    SLOW(1000, 2000);

    private final int minMillis;
    private final int maxMillis;

    MockLatency(int minMillis, int maxMillis) {
        this.minMillis = minMillis;
        this.maxMillis = maxMillis;
    }

    public int minMillis() {
        return minMillis;
    }

    public int maxMillis() {
        return maxMillis;
    }
}
