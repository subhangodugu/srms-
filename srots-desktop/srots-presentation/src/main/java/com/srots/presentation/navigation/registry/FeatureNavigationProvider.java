package com.srots.presentation.navigation.registry;

import com.srots.presentation.navigation.model.NavigationItem;
import java.util.Collection;

/**
 * Supplies navigation items for a feature module.
 */
public interface FeatureNavigationProvider {

    Collection<NavigationItem> getNavigationItems();
}
