package com.srots.application.search;

import java.util.List;

/**
 * Grouped search results by entity type.
 */
public record SearchResultGroup(SearchEntityType type, String label, List<SearchResult> results) {

    public SearchResultGroup {
        type = type == null ? SearchEntityType.SETTINGS : type;
        label = label == null || label.isBlank() ? type.name() : label.trim();
        results = results == null ? List.of() : List.copyOf(results);
    }
}
