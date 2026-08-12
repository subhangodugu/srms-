package com.srots.application.dto;

import java.util.Objects;

/** Named numeric bucket for charts and distributions. */
public final class NamedCountDTO {

    private final String name;
    private final long count;

    public NamedCountDTO(String name, long count) {
        this.name = Objects.requireNonNull(name, "name");
        this.count = Math.max(0, count);
    }

    public String getName() {
        return name;
    }

    public long getCount() {
        return count;
    }
}
