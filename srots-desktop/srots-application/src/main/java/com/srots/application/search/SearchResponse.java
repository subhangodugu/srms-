package com.srots.application.search;

import java.util.List;

/**
 * Aggregated response for a search request.
 */
public record SearchResponse(
        String requestId,
        List<SearchResultGroup> groups,
        List<String> providerErrors,
        boolean partialFailure) {

    public SearchResponse {
        requestId = requestId == null ? "" : requestId;
        groups = groups == null ? List.of() : List.copyOf(groups);
        providerErrors = providerErrors == null ? List.of() : List.copyOf(providerErrors);
    }

    public boolean isEmpty() {
        return groups.stream().allMatch(g -> g.results().isEmpty());
    }

    public int totalResults() {
        return groups.stream().mapToInt(g -> g.results().size()).sum();
    }
}
