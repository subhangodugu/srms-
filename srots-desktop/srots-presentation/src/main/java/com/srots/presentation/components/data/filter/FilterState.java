package com.srots.presentation.components.data.filter;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Pure-Java filter state for unit tests and view-models.
 * No JavaFX dependency.
 */
public final class FilterState {

    private final Map<String, String> selectedFilters = new LinkedHashMap<>();
    private String searchText = "";

    public FilterState() {}

    public FilterState(Map<String, String> selectedFilters, String searchText) {
        if (selectedFilters != null) {
            this.selectedFilters.putAll(selectedFilters);
        }
        this.searchText = searchText == null ? "" : searchText;
    }

    public void set(String key, String value) {
        Objects.requireNonNull(key, "key");
        if (value == null || value.isBlank()) {
            selectedFilters.remove(key);
        } else {
            selectedFilters.put(key, value);
        }
    }

    public void setSearchText(String searchText) {
        this.searchText = searchText == null ? "" : searchText;
    }

    public String getSearchText() {
        return searchText;
    }

    public String get(String key) {
        return selectedFilters.get(key);
    }

    public Map<String, String> getSelectedFilters() {
        return Collections.unmodifiableMap(selectedFilters);
    }

    public void clear() {
        selectedFilters.clear();
        searchText = "";
    }

    public FilterState copy() {
        return new FilterState(selectedFilters, searchText);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FilterState that)) {
            return false;
        }
        return selectedFilters.equals(that.selectedFilters)
                && Objects.equals(searchText, that.searchText);
    }

    @Override
    public int hashCode() {
        return Objects.hash(selectedFilters, searchText);
    }
}
