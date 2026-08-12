package com.srots.presentation.window;

import com.srots.presentation.navigation.model.NavigationRouteId;
import com.srots.presentation.navigation.registry.CoreNavigationProvider;
import com.srots.presentation.navigation.registry.DefaultNavigationRegistry;
import com.srots.presentation.navigation.registry.NavigationRegistry;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SrotsWindowTitleServiceTest {

    @Test
    void titleFor_overviewUsesNavigationMetadata() {
        NavigationRegistry registry = new DefaultNavigationRegistry(new CoreNavigationProvider());
        SrotsWindowTitleService titles = new SrotsWindowTitleService("SROTS", registry);
        assertEquals("SROTS — Overview", titles.titleFor(NavigationRouteId.OVERVIEW));
    }

    @Test
    void titleFor_employeesUsesLeafTitle() {
        NavigationRegistry registry = new DefaultNavigationRegistry(new CoreNavigationProvider());
        SrotsWindowTitleService titles = new SrotsWindowTitleService("SROTS", registry);
        assertEquals("SROTS — Employees", titles.titleFor(NavigationRouteId.COMPANY_EMPLOYEES));
    }

    @Test
    void titleFor_comptyReleasesComposesParentBrand() {
        NavigationRegistry registry = new DefaultNavigationRegistry(new CoreNavigationProvider());
        SrotsWindowTitleService titles = new SrotsWindowTitleService("SROTS", registry);
        assertEquals("SROTS — COMPTY Releases", titles.titleFor(NavigationRouteId.COMPTY_RELEASES));
    }

    @Test
    void defaultTitle_isApplicationName() {
        SrotsWindowTitleService titles = new SrotsWindowTitleService("SROTS", null);
        assertEquals("SROTS", titles.defaultTitle());
    }

    @Test
    void humanize_convertsEnumNames() {
        assertEquals("Compty Releases", SrotsWindowTitleService.humanize("COMPTY_RELEASES"));
    }
}
