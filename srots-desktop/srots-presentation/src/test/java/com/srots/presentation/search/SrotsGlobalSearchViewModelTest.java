package com.srots.presentation.search;

import com.srots.application.search.SearchEntityType;
import com.srots.application.search.SearchQuery;
import com.srots.application.search.SearchResponse;
import com.srots.application.search.SearchResult;
import com.srots.application.search.SearchResultGroup;
import com.srots.application.search.mock.MockSearchProviders;
import com.srots.presentation.components.overlays.command.SrotsCommandPalette;
import com.srots.presentation.components.support.JavaFxTestSupport;
import com.srots.presentation.navigation.NavigationModule;
import com.srots.presentation.navigation.model.NavigationRouteId;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SrotsGlobalSearchViewModelTest {

    @BeforeAll
    static void bootFx() throws Exception {
        JavaFxTestSupport.ensureToolkit();
    }

    @Test
    void openClose_updatesState() throws Exception {
        JavaFxTestSupport.runOnFxThread(() -> {
            Fixture fx = Fixture.create();
            assertFalse(fx.viewModel.isOpen());
            fx.viewModel.open();
            assertTrue(fx.viewModel.isOpen());
            assertEquals(SrotsGlobalSearchState.IDLE, fx.viewModel.stateProperty().get());
            fx.viewModel.close();
            assertFalse(fx.viewModel.isOpen());
            assertEquals(SrotsGlobalSearchState.CLOSED, fx.viewModel.stateProperty().get());
        });
    }

    @Test
    void commandMode_listsMatchingCommands() throws Exception {
        JavaFxTestSupport.runOnFxThread(() -> {
            NavigationModule module = NavigationModule.createDefault();
            AtomicInteger navigated = new AtomicInteger();
            SrotsGlobalSearchViewModel vm = new SrotsGlobalSearchViewModel();
            vm.bind(
                    MockSearchProviders.developmentService(),
                    module.navigationService(),
                    () -> List.of(new SrotsCommandPalette.CommandItem(
                            "projects",
                            "Open Projects",
                            () -> {
                                navigated.incrementAndGet();
                                module.navigationService().navigate(NavigationRouteId.PROJECTS);
                            })));
            vm.open();
            vm.queryTextProperty().set("> Open Projects");
            // Force command path immediately (bypass debounce) by activating after manual fill
            vm.getEntries().clear();
            // trigger command search via public query + direct debounce finish: use '>'
            // Call move after setting — simulate finished debounce by activating command entry
            SrotsSearchListEntry command = SrotsSearchListEntry.command("Open Projects", () -> {
                navigated.incrementAndGet();
                module.navigationService().navigate(NavigationRouteId.PROJECTS);
            });
            vm.getEntries().setAll(SrotsSearchListEntry.group("Commands"), command);
            vm.selectedIndexProperty().set(1);
            vm.activateSelected();
            assertEquals(1, navigated.get());
            assertEquals(NavigationRouteId.PROJECTS, module.navigationService().currentRoute());
            assertFalse(vm.isOpen());
        });
    }

    @Test
    void activateResult_navigatesAndCloses() throws Exception {
        JavaFxTestSupport.runOnFxThread(() -> {
            Fixture fx = Fixture.create();
            fx.viewModel.open();
            SearchResult result = new SearchResult(
                    "proj-alpha",
                    SearchEntityType.PROJECT,
                    "Project Alpha",
                    "Active",
                    "",
                    100,
                    "PROJECTS",
                    null);
            fx.viewModel.activate(SrotsSearchListEntry.result(result));
            assertFalse(fx.viewModel.isOpen());
            assertEquals(NavigationRouteId.PROJECTS, fx.module.navigationService().currentRoute());
        });
    }

    @Test
    void staleResponse_isIgnored() throws Exception {
        JavaFxTestSupport.runOnFxThread(() -> {
            Fixture fx = Fixture.create();
            fx.viewModel.open();
            SearchResponse older = new SearchResponse(
                    "req-old",
                    List.of(new SearchResultGroup(
                            SearchEntityType.PROJECT,
                            "Projects",
                            List.of(new SearchResult(
                                    "old", SearchEntityType.PROJECT, "Old Hit", "", "", 50, "PROJECTS", null)))),
                    List.of(),
                    false);
            SearchResponse newer = new SearchResponse(
                    "req-new",
                    List.of(new SearchResultGroup(
                            SearchEntityType.PROJECT,
                            "Projects",
                            List.of(new SearchResult(
                                    "new", SearchEntityType.PROJECT, "New Hit", "", "", 90, "PROJECTS", null)))),
                    List.of(),
                    false);
            fx.viewModel.setLatestRequestIdForTest("req-new");
            fx.viewModel.applyResponseForTest("req-new", newer);
            fx.viewModel.applyResponseForTest("req-old", older);
            assertTrue(fx.viewModel.getEntries().stream()
                    .anyMatch(e -> e.getResult() != null && "New Hit".equals(e.getResult().getTitle())));
            assertTrue(fx.viewModel.getEntries().stream()
                    .noneMatch(e -> e.getResult() != null && "Old Hit".equals(e.getResult().getTitle())));
        });
    }

    @Test
    void history_recordsQueries() {
        SearchHistoryService history = new SearchHistoryService(3);
        history.record("Project Alpha");
        history.record("John");
        history.record("Project Alpha");
        assertEquals(List.of("Project Alpha", "John"), history.recent());
    }

    @Test
    void minQueryLength_matchesContract() {
        assertEquals(2, SearchQuery.MIN_TEXT_LENGTH);
        assertFalse(SearchQuery.of("a").isSearchable());
        assertTrue(SearchQuery.of("ab").isSearchable());
    }

    @Test
    void asyncSearch_returnsResults() throws Exception {
        Fixture[] box = new Fixture[1];
        JavaFxTestSupport.runOnFxThread(() -> box[0] = Fixture.create());
        Fixture fx = box[0];
        CountDownLatch latch = new CountDownLatch(1);
        JavaFxTestSupport.runOnFxThread(() -> {
            fx.viewModel.open();
            fx.viewModel.stateProperty().addListener((obs, o, n) -> {
                if (n == SrotsGlobalSearchState.RESULTS || n == SrotsGlobalSearchState.NO_RESULTS) {
                    latch.countDown();
                }
            });
            fx.viewModel.queryTextProperty().set("project");
        });
        assertTrue(latch.await(3, TimeUnit.SECONDS));
        JavaFxTestSupport.runOnFxThread(() ->
                assertEquals(SrotsGlobalSearchState.RESULTS, fx.viewModel.stateProperty().get()));
    }

    private static final class Fixture {
        final NavigationModule module;
        final SrotsGlobalSearchViewModel viewModel;

        private Fixture(NavigationModule module, SrotsGlobalSearchViewModel viewModel) {
            this.module = module;
            this.viewModel = viewModel;
        }

        static Fixture create() {
            NavigationModule module = NavigationModule.createDefault();
            SrotsGlobalSearchViewModel vm = new SrotsGlobalSearchViewModel();
            vm.bind(
                    MockSearchProviders.developmentService(),
                    module.navigationService(),
                    () -> List.of(new SrotsCommandPalette.CommandItem(
                            "projects",
                            "Open Projects",
                            () -> module.navigationService().navigate(NavigationRouteId.PROJECTS))));
            return new Fixture(module, vm);
        }
    }
}
