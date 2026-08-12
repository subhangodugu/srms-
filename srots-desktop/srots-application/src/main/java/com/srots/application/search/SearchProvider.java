package com.srots.application.search;

import java.util.List;
import java.util.Set;

/**
 * Extensible search provider contract. Implementations must not depend on JavaFX.
 */
public interface SearchProvider {

    String id();

    Set<SearchEntityType> supportedTypes();

    default boolean supports(SearchScope scope) {
        if (scope == null || scope == SearchScope.ALL) {
            return true;
        }
        return supportedTypes().stream().anyMatch(scope::includes);
    }

    /**
     * Executes a search. May throw; the aggregator treats failures as partial errors.
     */
    List<SearchResult> search(SearchQuery query, SearchAccessContext access);
}
