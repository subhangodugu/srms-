package com.srots.presentation.navigation.registry;

import com.srots.presentation.navigation.model.NavigationItem;
import java.util.Collection;

/**
 * Core platform navigation entries from {@link DefaultNavigationCatalog}.
 */
public final class CoreNavigationProvider implements FeatureNavigationProvider {

    @Override
    public Collection<NavigationItem> getNavigationItems() {
        return DefaultNavigationCatalog.createDefaultItems();
    }
}
