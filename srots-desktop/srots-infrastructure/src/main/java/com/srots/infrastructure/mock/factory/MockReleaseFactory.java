package com.srots.infrastructure.mock.factory;

import com.srots.domain.model.enums.GatePhaseStatus;
import com.srots.domain.model.enums.ReleaseStatus;
import com.srots.domain.release.Release;
import com.srots.domain.release.ReleasePipelineGate;
import com.srots.infrastructure.mock.configuration.MockConfiguration;
import com.srots.infrastructure.mock.seed.MockDataSeeder;

import java.util.List;

public final class MockReleaseFactory {

    private MockReleaseFactory() {
    }

    public static Release compty190() {
        return new Release(
                "REL-001",
                MockDataSeeder.PRODUCT_COMPTY,
                "VER-C-190",
                "COMPTY v1.9.0",
                ReleaseStatus.READY_FOR_APPROVAL,
                "EMP-010",
                MockConfiguration.MOCK_REFERENCE_DATE.plusDays(10),
                GatePhaseStatus.PASSED,
                GatePhaseStatus.PASSED,
                GatePhaseStatus.WARNING,
                GatePhaseStatus.PENDING,
                87,
                List.of(
                        new ReleasePipelineGate("Development", GatePhaseStatus.PASSED, 1),
                        new ReleasePipelineGate("Unit Tests", GatePhaseStatus.PASSED, 2),
                        new ReleasePipelineGate("Integration", GatePhaseStatus.PASSED, 3),
                        new ReleasePipelineGate("QA", GatePhaseStatus.PASSED, 4),
                        new ReleasePipelineGate("Security", GatePhaseStatus.PASSED, 5),
                        new ReleasePipelineGate("Approval", GatePhaseStatus.WARNING, 6),
                        new ReleasePipelineGate("Deployment", GatePhaseStatus.PENDING, 7),
                        new ReleasePipelineGate("Production", GatePhaseStatus.PENDING, 8)));
    }
}
