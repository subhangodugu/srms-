package com.srots.application.search;

import com.srots.application.search.mock.MockCatalogSearchProvider;
import com.srots.application.search.mock.MockSearchProviders;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;
import java.util.concurrent.Executor;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DefaultGlobalSearchServiceTest {

    private static final Executor DIRECT = Runnable::run;

    @Test
    void ranking_prefersExactThenPrefixThenContains() {
        assertTrue(SearchRanking.score("alpha", "Alpha Project", "", "")
                > SearchRanking.score("alpha", "Project Alpha", "", ""));
        assertTrue(SearchRanking.score("proj", "Project Alpha", "", "")
                > SearchRanking.score("proj", "My Project", "", ""));
        assertEquals(100, SearchRanking.score("project alpha", "Project Alpha", "", ""), 0.001);
    }

    @Test
    void shortQuery_returnsEmptyWithoutCallingProviders() {
        DefaultGlobalSearchService service = new DefaultGlobalSearchService(
                MockSearchProviders.developmentProviders(),
                SearchAccessContext.allowAll(),
                DIRECT,
                10);
        SearchResponse response = service.executeSync(SearchQuery.of("p"));
        assertTrue(response.isEmpty());
    }

    @Test
    void search_findsProjectAlphaGrouped() {
        DefaultGlobalSearchService service = new DefaultGlobalSearchService(
                MockSearchProviders.developmentProviders(),
                SearchAccessContext.allowAll(),
                DIRECT,
                10);
        SearchResponse response = service.executeSync(SearchQuery.of("project alpha"));
        assertFalse(response.isEmpty());
        assertTrue(response.groups().stream()
                .anyMatch(g -> g.type() == SearchEntityType.PROJECT
                        && g.results().stream().anyMatch(r -> r.getTitle().equals("Project Alpha"))));
    }

    @Test
    void scope_projectsOnly() {
        DefaultGlobalSearchService service = new DefaultGlobalSearchService(
                MockSearchProviders.developmentProviders(),
                SearchAccessContext.allowAll(),
                DIRECT,
                10);
        SearchResponse response = service.executeSync(SearchQuery.builder()
                .text("project")
                .scope(SearchScope.PROJECTS)
                .build());
        assertTrue(response.groups().stream().allMatch(g -> g.type() == SearchEntityType.PROJECT));
    }

    @Test
    void permission_hidesUnauthorizedTypes() {
        SearchAccessContext limited = SearchAccessContext.of(true, Set.of("PROJECTS"));
        DefaultGlobalSearchService service = new DefaultGlobalSearchService(
                MockSearchProviders.developmentProviders(),
                limited,
                DIRECT,
                10);
        SearchResponse response = service.executeSync(SearchQuery.of("john"));
        assertTrue(response.groups().stream().noneMatch(g -> g.type() == SearchEntityType.EMPLOYEE));
    }

    @Test
    void providerFailure_returnsPartialResults() {
        List<SearchProvider> providers = List.of(
                new MockCatalogSearchProvider(
                        "projects",
                        SearchEntityType.PROJECT,
                        List.of(MockCatalogSearchProvider.item(
                                "proj-alpha", SearchEntityType.PROJECT, "Project Alpha", "Active", "PROJECTS"))),
                new MockCatalogSearchProvider(
                        "employees",
                        SearchEntityType.EMPLOYEE,
                        List.of(),
                        true));
        DefaultGlobalSearchService service = new DefaultGlobalSearchService(
                providers, SearchAccessContext.allowAll(), DIRECT, 10);
        SearchResponse response = service.executeSync(SearchQuery.of("project"));
        assertFalse(response.isEmpty());
        assertTrue(response.partialFailure());
        assertFalse(response.providerErrors().isEmpty());
    }
}
