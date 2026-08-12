package com.srots.infrastructure.mock.scenario;

import com.srots.infrastructure.mock.configuration.MockConfiguration;
import com.srots.infrastructure.mock.configuration.MockFailurePolicy;
import com.srots.infrastructure.mock.configuration.MockLatency;
import com.srots.infrastructure.mock.configuration.MockScenarioType;
import com.srots.infrastructure.mock.seed.MockDataSeeder;

public final class ErrorScenario implements MockScenario {
    @Override public MockScenarioType type() { return MockScenarioType.ERROR; }
    @Override public void apply(MockConfiguration configuration, MockDataSeeder seeder) {
        configuration.setScenario(MockScenarioType.ERROR);
        configuration.setLatency(MockLatency.NONE);
        configuration.setFailurePolicy(MockFailurePolicy.ALWAYS);
        seeder.seed(MockScenarioType.NORMAL);
    }
}
