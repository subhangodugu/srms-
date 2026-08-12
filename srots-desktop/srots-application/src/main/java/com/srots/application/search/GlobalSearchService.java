package com.srots.application.search;

import java.util.concurrent.CompletableFuture;

/**
 * Application-facing global search façade.
 */
public interface GlobalSearchService {

    CompletableFuture<SearchResponse> search(SearchQuery query);

    SearchAccessContext accessContext();
}
