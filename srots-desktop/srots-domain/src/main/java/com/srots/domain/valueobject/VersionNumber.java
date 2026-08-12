package com.srots.domain.valueobject;

import java.util.Objects;

public final class VersionNumber {

    private final String value;

    public VersionNumber(String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("VersionNumber cannot be null or empty");
        }
        this.value = value.trim();
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VersionNumber versionNumber = (VersionNumber) o;
        return Objects.equals(value, versionNumber.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return value;
    }
}
