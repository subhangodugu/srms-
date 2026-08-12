package com.srots.infrastructure.mock.scenario;

import com.srots.infrastructure.mock.configuration.MockConfiguration;
import com.srots.infrastructure.mock.configuration.MockFailurePolicy;
import com.srots.infrastructure.mock.configuration.MockLatency;
import com.srots.infrastructure.mock.configuration.MockScenarioType;
import com.srots.infrastructure.mock.seed.MockDataSeeder;

public final class LoadingScenario implements MockScenario {
    @Override public MockScenarioType type() { return MockScenarioType.LOADING; }
    @Override public void apply(MockConfiguration configuration, MockDataSeeder seeder) {
        configuration.setScenario(MockScenarioType.LOADING);
        configuration.setLatency(MockLatency.NORMAL);
        configuration.setFailurePolicy(MockFailurePolicy.NEVER);
        seeder.seed(MockScenarioType.NORMAL);
    }
}
