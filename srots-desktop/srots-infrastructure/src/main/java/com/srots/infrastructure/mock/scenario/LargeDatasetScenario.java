package com.srots.infrastructure.mock.scenario;

import com.srots.infrastructure.mock.configuration.MockConfiguration;
import com.srots.infrastructure.mock.configuration.MockFailurePolicy;
import com.srots.infrastructure.mock.configuration.MockLatency;
import com.srots.infrastructure.mock.configuration.MockScenarioType;
import com.srots.infrastructure.mock.seed.MockDataSeeder;

public final class LargeDatasetScenario implements MockScenario {
    @Override public MockScenarioType type() { return MockScenarioType.LARGE; }
    @Override public void apply(MockConfiguration configuration, MockDataSeeder seeder) {
        configuration.setScenario(MockScenarioType.LARGE);
        configuration.setLatency(MockLatency.NONE);
        configuration.setFailurePolicy(MockFailurePolicy.NEVER);
        seeder.seed(MockScenarioType.LARGE);
    }
}
