package com.srots.shared.query;

import java.util.Objects;

/**
 * Zero-based page request shared by all repository implementations.
 */
public final class PageRequest {

    private final int page;
    private final int size;
    private final String sortField;
    private final SortDirection direction;

    public PageRequest(int page, int size, String sortField, SortDirection direction) {
        if (page < 0) {
            throw new IllegalArgumentException("page must be >= 0");
        }
        if (size < 1) {
            throw new IllegalArgumentException("size must be >= 1");
        }
        this.page = page;
        this.size = size;
        this.sortField = sortField;
        this.direction = direction == null ? SortDirection.ASC : direction;
    }

    public static PageRequest of(int page, int size) {
        return new PageRequest(page, size, null, SortDirection.ASC);
    }

    public static PageRequest of(int page, int size, String sortField, SortDirection direction) {
        return new PageRequest(page, size, sortField, direction);
    }

    public int page() {
        return page;
    }

    public int size() {
        return size;
    }

    public String sortField() {
        return sortField;
    }

    public SortDirection direction() {
        return direction;
    }

    public int offset() {
        return page * size;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PageRequest that)) {
            return false;
        }
        return page == that.page
                && size == that.size
                && Objects.equals(sortField, that.sortField)
                && direction == that.direction;
    }

    @Override
    public int hashCode() {
        return Objects.hash(page, size, sortField, direction);
    }
}
