package com.srots.domain.release;

import com.srots.domain.model.enums.GatePhaseStatus;
import java.util.Objects;

public final class ReleasePipelineGate {
    private final String name;
    private final GatePhaseStatus status;
    private final int order;

    public ReleasePipelineGate(String name, GatePhaseStatus status, int order) {
        this.name = Objects.requireNonNull(name);
        this.status = Objects.requireNonNull(status);
        this.order = order;
    }

    public String getName() { return name; }
    public GatePhaseStatus getStatus() { return status; }
    public int getOrder() { return order; }
}
