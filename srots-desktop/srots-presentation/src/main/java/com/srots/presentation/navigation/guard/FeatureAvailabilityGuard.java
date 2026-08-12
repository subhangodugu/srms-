package com.srots.presentation.navigation.guard;

import com.srots.presentation.navigation.model.NavigationContext;
import com.srots.presentation.navigation.model.NavigationItem;
import com.srots.presentation.navigation.model.NavigationVisibility;
import com.srots.presentation.navigation.registry.NavigationRegistry;
import java.util.Objects;
import java.util.Optional;

/**
 * Blocks navigation to destinations marked {@link NavigationVisibility#DISABLED}.
 */
public final class FeatureAvailabilityGuard implements NavigationGuard {

    public static final String FEATURE_UNAVAILABLE = "Feature unavailable";

    private final NavigationRegistry registry;

    public FeatureAvailabilityGuard(NavigationRegistry registry) {
        this.registry = Objects.requireNonNull(registry, "registry");
    }

    @Override
    public GuardDecision evaluate(NavigationContext from, NavigationContext to) {
        Objects.requireNonNull(to, "to");
        Optional<NavigationItem> item = registry.find(to.route());
        if (item.isEmpty()) {
            return GuardDecision.allow();
        }
        if (item.get().getVisibility() == NavigationVisibility.DISABLED) {
            return GuardDecision.block(FEATURE_UNAVAILABLE);
        }
        return GuardDecision.allow();
    }
}
