package com.srots.presentation.search;

import com.srots.application.search.SearchScope;

/**
 * Presentation filter chips mapped to {@link SearchScope}.
 */
public enum SrotsSearchFilter {
    ALL(SearchScope.ALL, "All"),
    EMPLOYEES(SearchScope.EMPLOYEES, "Employees"),
    PROJECTS(SearchScope.PROJECTS, "Projects"),
    PRODUCTS(SearchScope.PRODUCTS, "Products"),
    RELEASES(SearchScope.RELEASES, "Releases"),
    TASKS(SearchScope.TASKS, "Tasks"),
    COMPTY(SearchScope.COMPTY, "COMPTY");

    private final SearchScope scope;
    private final String label;

    SrotsSearchFilter(SearchScope scope, String label) {
        this.scope = scope;
        this.label = label;
    }

    public SearchScope scope() {
        return scope;
    }

    public String label() {
        return label;
    }
}
