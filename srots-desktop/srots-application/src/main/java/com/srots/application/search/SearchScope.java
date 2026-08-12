package com.srots.application.search;

/**
 * Search scope filter applied to providers.
 */
public enum SearchScope {
    ALL,
    EMPLOYEES,
    PROJECTS,
    CUSTOMERS,
    PRODUCTS,
    TASKS,
    RELEASES,
    SERVICE_DESK,
    COMPTY,
    KNOWLEDGE,
    SETTINGS;

    public boolean includes(SearchEntityType type) {
        if (this == ALL || type == null) {
            return true;
        }
        return switch (this) {
            case EMPLOYEES -> type == SearchEntityType.EMPLOYEE;
            case PROJECTS -> type == SearchEntityType.PROJECT;
            case CUSTOMERS -> type == SearchEntityType.CUSTOMER;
            case PRODUCTS -> type == SearchEntityType.PRODUCT;
            case TASKS -> type == SearchEntityType.TASK;
            case RELEASES -> type == SearchEntityType.RELEASE;
            case SERVICE_DESK -> type == SearchEntityType.SERVICE_DESK;
            case COMPTY -> type == SearchEntityType.COMPTY;
            case KNOWLEDGE -> type == SearchEntityType.KNOWLEDGE;
            case SETTINGS -> type == SearchEntityType.SETTINGS;
            case ALL -> true;
        };
    }
}
