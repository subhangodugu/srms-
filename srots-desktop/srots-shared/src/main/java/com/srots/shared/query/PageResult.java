package com.srots.shared.query;

import java.util.List;
import java.util.Objects;

/**
 * Paginated result contract for repository queries.
 */
public final class PageResult<T> {

    private final List<T> items;
    private final long totalCount;
    private final int page;
    private final int pageSize;

    public PageResult(List<T> items, long totalCount, int page, int pageSize) {
        this.items = List.copyOf(Objects.requireNonNull(items, "items"));
        this.totalCount = totalCount;
        this.page = page;
        this.pageSize = pageSize;
    }

    public static <T> PageResult<T> empty(int page, int pageSize) {
        return new PageResult<>(List.of(), 0, page, pageSize);
    }

    public List<T> items() {
        return items;
    }

    public long totalCount() {
        return totalCount;
    }

    public int page() {
        return page;
    }

    public int pageSize() {
        return pageSize;
    }

    public boolean isEmpty() {
        return items.isEmpty();
    }
}
