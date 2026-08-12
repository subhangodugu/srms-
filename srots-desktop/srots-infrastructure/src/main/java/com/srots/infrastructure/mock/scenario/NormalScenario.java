package com.srots.infrastructure.mock.scenario;

import com.srots.infrastructure.mock.configuration.MockConfiguration;
import com.srots.infrastructure.mock.configuration.MockFailurePolicy;
import com.srots.infrastructure.mock.configuration.MockLatency;
import com.srots.infrastructure.mock.configuration.MockScenarioType;
import com.srots.infrastructure.mock.seed.MockDataSeeder;

public final class NormalScenario implements MockScenario {
    @Override public MockScenarioType type() { return MockScenarioType.NORMAL; }
    @Override public void apply(MockConfiguration configuration, MockDataSeeder seeder) {
        configuration.setScenario(MockScenarioType.NORMAL);
        configuration.setLatency(MockLatency.NONE);
        configuration.setFailurePolicy(MockFailurePolicy.NEVER);
        seeder.seed(MockScenarioType.NORMAL);
    }
}
