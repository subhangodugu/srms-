package com.srots.presentation.search;

import java.util.ArrayList;
import java.util.List;

/**
 * Recent query history. Outside UI node storage.
 */
public final class SearchHistoryService {

    public static final int DEFAULT_LIMIT = 8;

    private final int limit;
    private final List<String> recent = new ArrayList<>();

    public SearchHistoryService() {
        this(DEFAULT_LIMIT);
    }

    public SearchHistoryService(int limit) {
        this.limit = Math.max(1, limit);
    }

    public synchronized void record(String query) {
        if (query == null || query.isBlank()) {
            return;
        }
        String normalized = query.trim();
        recent.removeIf(q -> q.equalsIgnoreCase(normalized));
        recent.add(0, normalized);
        while (recent.size() > limit) {
            recent.remove(recent.size() - 1);
        }
    }

    public synchronized List<String> recent() {
        return List.copyOf(recent);
    }

    public synchronized void clear() {
        recent.clear();
    }
}
