package com.srots.infrastructure.mock.support;

import com.srots.infrastructure.mock.configuration.MockConfiguration;
import com.srots.infrastructure.mock.configuration.MockLatency;
import com.srots.infrastructure.mock.configuration.MockScenarioType;

/** Shared latency / failure / scenario behavior for mock repositories. */
public final class MockRepositoryBehavior {

    private final MockConfiguration configuration;

    public MockRepositoryBehavior(MockConfiguration configuration) {
        this.configuration = configuration;
    }

    public void beforeRead() {
        MockScenarioType scenario = configuration.getScenario();
        if (scenario == MockScenarioType.ERROR || scenario == MockScenarioType.OFFLINE || configuration.shouldFail()) {
            String message = scenario == MockScenarioType.OFFLINE
                    ? "Remote data source unavailable (offline scenario)"
                    : "Mock repository failure injected";
            throw new IllegalStateException(message);
        }
        if (scenario == MockScenarioType.LOADING && configuration.getLatency() == MockLatency.NONE) {
            configuration.setLatency(MockLatency.NORMAL);
        }
        configuration.applyLatency();
    }

    public void beforeWrite() {
        beforeRead();
    }

    public MockConfiguration configuration() {
        return configuration;
    }
}
