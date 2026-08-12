package com.srots.presentation.navigation.model;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Immutable route parameter map (presentation-only).
 */
public final class RouteParameters {

    private static final RouteParameters EMPTY = new RouteParameters(Map.of());

    private final Map<String, String> values;

    private RouteParameters(Map<String, String> values) {
        this.values = Collections.unmodifiableMap(new LinkedHashMap<>(values));
    }

    public static RouteParameters empty() {
        return EMPTY;
    }

    public static RouteParameters of(String key, String value) {
        Objects.requireNonNull(key, "key");
        Map<String, String> map = new LinkedHashMap<>();
        map.put(key, value);
        return new RouteParameters(map);
    }

    public String get(String key) {
        return values.get(key);
    }

    public Map<String, String> asMap() {
        return Collections.unmodifiableMap(new LinkedHashMap<>(values));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RouteParameters that)) {
            return false;
        }
        return values.equals(that.values);
    }

    @Override
    public int hashCode() {
        return values.hashCode();
    }

    @Override
    public String toString() {
        return "RouteParameters" + values;
    }
}
