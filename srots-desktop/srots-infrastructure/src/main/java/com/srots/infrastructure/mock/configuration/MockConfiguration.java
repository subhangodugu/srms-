package com.srots.infrastructure.mock.configuration;

import java.time.LocalDate;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Mutable development-only mock configuration. Not for production use.
 */
public final class MockConfiguration {

    public static final String MOCK_DATA_VERSION = "1.0";
    public static final LocalDate MOCK_REFERENCE_DATE = LocalDate.of(2026, 8, 1);

    private volatile DataMode dataMode = DataMode.MOCK;
    private volatile MockScenarioType scenario = MockScenarioType.NORMAL;
    private volatile MockLatency latency = MockLatency.NONE;
    private volatile MockFailurePolicy failurePolicy = MockFailurePolicy.NEVER;
    private volatile String runtimeEnvironment = "development";
    private final AtomicInteger operationCounter = new AtomicInteger();

    public DataMode getDataMode() {
        return dataMode;
    }

    public void setDataMode(DataMode dataMode) {
        this.dataMode = Objects.requireNonNull(dataMode);
    }

    public MockScenarioType getScenario() {
        return scenario;
    }

    public void setScenario(MockScenarioType scenario) {
        this.scenario = Objects.requireNonNull(scenario);
    }

    public MockLatency getLatency() {
        return latency;
    }

    public void setLatency(MockLatency latency) {
        this.latency = Objects.requireNonNull(latency);
    }

    public MockFailurePolicy getFailurePolicy() {
        return failurePolicy;
    }

    public void setFailurePolicy(MockFailurePolicy failurePolicy) {
        this.failurePolicy = Objects.requireNonNull(failurePolicy);
    }

    public String getRuntimeEnvironment() {
        return runtimeEnvironment;
    }

    public void setRuntimeEnvironment(String runtimeEnvironment) {
        this.runtimeEnvironment = runtimeEnvironment == null ? "development" : runtimeEnvironment;
    }

    public String getDatasetVersion() {
        return MOCK_DATA_VERSION;
    }

    public LocalDate getReferenceDate() {
        return MOCK_REFERENCE_DATE;
    }

    public boolean isProduction() {
        return "production".equalsIgnoreCase(runtimeEnvironment);
    }

    /**
     * Returns true when the failure policy requires throwing for this operation.
     * OCCASIONAL fails every 7th call (deterministic).
     */
    public boolean shouldFail() {
        int n = operationCounter.incrementAndGet();
        return switch (failurePolicy) {
            case NEVER -> false;
            case ALWAYS -> true;
            case OCCASIONAL -> n % 7 == 0;
        };
    }

    public void applyLatency() {
        MockLatency lat = latency;
        if (lat == null || lat == MockLatency.NONE) {
            return;
        }
        int span = Math.max(0, lat.maxMillis() - lat.minMillis());
        int delay = lat.minMillis() + (span == 0 ? 0 : (operationCounter.get() % (span + 1)));
        if (delay <= 0) {
            return;
        }
        try {
            Thread.sleep(delay);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
