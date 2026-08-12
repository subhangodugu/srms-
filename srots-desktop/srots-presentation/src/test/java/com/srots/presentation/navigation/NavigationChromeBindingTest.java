package com.srots.presentation.navigation;

import com.srots.presentation.components.navigation.breadcrumb.SrotsBreadcrumb;
import com.srots.presentation.components.navigation.sidebar.SrotsNavigationItem;
import com.srots.presentation.components.support.JavaFxTestSupport;
import com.srots.presentation.navigation.model.NavigationRouteId;
import com.srots.presentation.navigation.service.NavigationChromeBinder;
import javafx.scene.layout.VBox;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * UI binding coverage for registry-driven sidebar / breadcrumb sync (TestFX-style via toolkit).
 */
class NavigationChromeBindingTest {

    @BeforeAll
    static void bootFx() throws Exception {
        JavaFxTestSupport.ensureToolkit();
    }

    @Test
    void sidebarAndBreadcrumb_followNavigationService() throws Exception {
        JavaFxTestSupport.runOnFxThread(() -> {
            NavigationModule module = NavigationModule.createDefault();
            VBox nav = new VBox();
            SrotsBreadcrumb breadcrumb = new SrotsBreadcrumb();
            NavigationChromeBinder binder = module.chromeBinder();
            binder.bindSidebar(nav);
            binder.bindBreadcrumb(breadcrumb);

            module.navigationService().navigate(NavigationRouteId.OVERVIEW);
            module.navigationService().navigate(NavigationRouteId.COMPTY_RELEASES);

            assertFalse(nav.getChildren().isEmpty());
            boolean hasItems = nav.getChildren().stream().anyMatch(n ->
                    n instanceof SrotsNavigationItem
                            || (n instanceof com.srots.presentation.components.navigation.sidebar.SrotsNavigationGroup g
                            && g.getItemsHost().getChildren().stream().anyMatch(SrotsNavigationItem.class::isInstance)));
            assertTrue(hasItems);
            assertFalse(breadcrumb.getChildren().isEmpty());
            assertEquals(NavigationRouteId.COMPTY_RELEASES, module.navigationService().currentRoute());

            boolean anyActive = nav.getChildren().stream().anyMatch(n -> {
                if (n instanceof SrotsNavigationItem item) {
                    return item.isActive();
                }
                if (n instanceof com.srots.presentation.components.navigation.sidebar.SrotsNavigationGroup g) {
                    return g.getItemsHost().getChildren().stream()
                            .filter(SrotsNavigationItem.class::isInstance)
                            .map(SrotsNavigationItem.class::cast)
                            .anyMatch(SrotsNavigationItem::isActive);
                }
                return false;
            });
            assertTrue(anyActive);
        });
    }
}
