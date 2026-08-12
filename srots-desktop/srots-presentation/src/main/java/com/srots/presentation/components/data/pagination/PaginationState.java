package com.srots.presentation.components.data.pagination;

/**
 * Pure-Java pagination state for unit tests and view-models.
 * Page index is 0-based. No JavaFX dependency.
 */
public final class PaginationState {

    private int page;
    private int pageSize;
    private long totalRecords;

    public PaginationState() {
        this(0, 20, 0);
    }

    public PaginationState(int page, int pageSize, long totalRecords) {
        this.pageSize = Math.max(1, pageSize);
        this.totalRecords = Math.max(0, totalRecords);
        this.page = clampPage(page);
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = clampPage(page);
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = Math.max(1, pageSize);
        this.page = clampPage(this.page);
    }

    public long getTotalRecords() {
        return totalRecords;
    }

    public void setTotalRecords(long totalRecords) {
        this.totalRecords = Math.max(0, totalRecords);
        this.page = clampPage(this.page);
    }

    public int getTotalPages() {
        if (totalRecords == 0) {
            return 1;
        }
        return (int) Math.ceil((double) totalRecords / pageSize);
    }

    public boolean canNext() {
        return page < getTotalPages() - 1;
    }

    public boolean canPrevious() {
        return page > 0;
    }

    public void next() {
        if (canNext()) {
            page++;
        }
    }

    public void previous() {
        if (canPrevious()) {
            page--;
        }
    }

    public void first() {
        page = 0;
    }

    public void last() {
        page = Math.max(0, getTotalPages() - 1);
    }

    public PaginationState copy() {
        return new PaginationState(page, pageSize, totalRecords);
    }

    private int clampPage(int candidate) {
        int max = Math.max(0, getTotalPages() - 1);
        if (candidate < 0) {
            return 0;
        }
        return Math.min(candidate, max);
    }
}
