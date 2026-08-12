package com.srots.application.search.mock;

import com.srots.application.search.DefaultGlobalSearchService;
import com.srots.application.search.GlobalSearchService;
import com.srots.application.search.SearchAccessContext;
import com.srots.application.search.SearchEntityType;
import com.srots.application.search.SearchProvider;
import com.srots.application.search.SearchResult;

import java.util.List;

/**
 * Development-only mock provider factory. Production must not use this accidentally.
 */
public final class MockSearchProviders {

    private MockSearchProviders() {
    }

    public static List<SearchProvider> developmentProviders() {
        return List.of(
                new MockCatalogSearchProvider("employees", SearchEntityType.EMPLOYEE, List.of(
                        item(SearchEntityType.EMPLOYEE, "emp-john", "John Smith", "Engineering · Active", "COMPANY_EMPLOYEES"),
                        item(SearchEntityType.EMPLOYEE, "emp-priya", "Priya Kumar", "Product · Active", "COMPANY_EMPLOYEES"),
                        item(SearchEntityType.EMPLOYEE, "emp-eden", "Eden Employee", "Operations · Active", "COMPANY_EMPLOYEES")
                )),
                new MockCatalogSearchProvider("projects", SearchEntityType.PROJECT, List.of(
                        item(SearchEntityType.PROJECT, "proj-alpha", "Project Alpha", "Engineering · Active", "PROJECTS"),
                        item(SearchEntityType.PROJECT, "proj-beta", "Project Beta", "Platform · Planning", "PROJECTS"),
                        item(SearchEntityType.PROJECT, "proj-scan", "Scan Optimization", "COMPTY · Active", "PROJECTS")
                )),
                new MockCatalogSearchProvider("products", SearchEntityType.PRODUCT, List.of(
                        item(SearchEntityType.PRODUCT, "prod-srots", "SROTS", "Platform · Core", "SROTS_PRODUCT"),
                        item(SearchEntityType.PRODUCT, "prod-compty", "COMPTY", "Control plane", "COMPTY")
                )),
                new MockCatalogSearchProvider("releases", SearchEntityType.RELEASE, List.of(
                        item(SearchEntityType.RELEASE, "rel-compty-24", "COMPTY v2.4", "Production · Released", "COMPTY_RELEASES"),
                        item(SearchEntityType.RELEASE, "rel-srots-01", "SROTS 0.1.0", "Desktop · Development", "SROTS_RELEASES")
                )),
                new MockCatalogSearchProvider("tasks", SearchEntityType.TASK, List.of(
                        item(SearchEntityType.TASK, "task-scan", "Scan optimization task", "Assigned · High", "WORKSPACE_TASKS"),
                        item(SearchEntityType.TASK, "task-docs", "Update release notes", "Backlog", "TASKS")
                )),
                new MockCatalogSearchProvider("compty", SearchEntityType.COMPTY, List.of(
                        item(SearchEntityType.COMPTY, "compty-overview", "COMPTY Overview", "Product control plane", "COMPTY_OVERVIEW"),
                        item(SearchEntityType.COMPTY, "compty-releases", "COMPTY Releases", "Release board", "COMPTY_RELEASES")
                )),
                new MockCatalogSearchProvider("support", SearchEntityType.SERVICE_DESK, List.of(
                        item(SearchEntityType.SERVICE_DESK, "sd-214", "SD-214 Triage queue", "Open · Support", "SUPPORT")
                )),
                new MockCatalogSearchProvider("knowledge", SearchEntityType.KNOWLEDGE, List.of(
                        item(SearchEntityType.KNOWLEDGE, "kb-search", "Using Global Search", "Knowledge · Guide", "KNOWLEDGE")
                )),
                new MockCatalogSearchProvider("settings", SearchEntityType.SETTINGS, List.of(
                        item(SearchEntityType.SETTINGS, "settings-main", "Settings", "Governance and configuration", "SETTINGS")
                ))
        );
    }

    public static GlobalSearchService developmentService(SearchAccessContext access) {
        return new DefaultGlobalSearchService(developmentProviders(), access);
    }

    public static GlobalSearchService developmentService() {
        return developmentService(SearchAccessContext.allowAll());
    }

    private static SearchResult item(
            SearchEntityType type,
            String id,
            String title,
            String subtitle,
            String routeId) {
        return MockCatalogSearchProvider.item(id, type, title, subtitle, routeId);
    }
}
