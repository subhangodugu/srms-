package com.srots.infrastructure.mock.diagnostics;

import com.srots.infrastructure.mock.configuration.DataMode;
import com.srots.infrastructure.mock.configuration.MockFailurePolicy;
import com.srots.infrastructure.mock.configuration.MockLatency;
import com.srots.infrastructure.mock.configuration.MockScenarioType;

import java.util.Map;

/**
 * Snapshot for the development-only diagnostics panel.
 */
public record MockDiagnosticsSnapshot(
        DataMode dataMode,
        MockScenarioType scenario,
        String mockUserRole,
        String mockUserDisplayName,
        MockLatency latency,
        MockFailurePolicy failurePolicy,
        String datasetVersion,
        Map<String, Integer> recordCounts,
        boolean developmentOnly) {
}
