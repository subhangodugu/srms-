package com.srots.infrastructure.mock.scenario;

import com.srots.infrastructure.mock.configuration.MockConfiguration;
import com.srots.infrastructure.mock.configuration.MockFailurePolicy;
import com.srots.infrastructure.mock.configuration.MockLatency;
import com.srots.infrastructure.mock.configuration.MockScenarioType;
import com.srots.infrastructure.mock.seed.MockDataSeeder;

/** Applies a named mock scenario to configuration + dataset. */
public interface MockScenario {
    MockScenarioType type();
    void apply(MockConfiguration configuration, MockDataSeeder seeder);
}
