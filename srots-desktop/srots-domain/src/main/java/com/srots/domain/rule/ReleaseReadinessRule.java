package com.srots.domain.rule;

import com.srots.domain.model.ReleaseGate;
import com.srots.domain.valueobject.GateStatus;
import java.util.List;

public final class ReleaseReadinessRule {

    private ReleaseReadinessRule() {}

    public static boolean isReleaseReady(List<ReleaseGate> gates) {
        if (gates == null || gates.isEmpty()) {
            return false;
        }
        return gates.stream().allMatch(gate -> gate.getStatus() == GateStatus.PASSED);
    }
}
