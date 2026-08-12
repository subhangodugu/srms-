package com.srots.presentation.navigation.guard;

import com.srots.presentation.navigation.model.NavigationContext;
import com.srots.presentation.navigation.model.NavigationItem;
import com.srots.presentation.navigation.registry.NavigationRegistry;
import com.srots.presentation.navigation.service.UserAccessContext;
import java.util.Objects;
import java.util.Optional;

/**
 * Blocks navigation when the destination requires a permission the user lacks.
 * Unknown routes (not in registry) are allowed — handled elsewhere.
 */
public final class PermissionGuard implements NavigationGuard {

    public static final String PERMISSION_DENIED = "PERMISSION_DENIED";

    private final NavigationRegistry registry;
    private final UserAccessContext accessContext;

    public PermissionGuard(NavigationRegistry registry, UserAccessContext accessContext) {
        this.registry = Objects.requireNonNull(registry, "registry");
        this.accessContext = Objects.requireNonNull(accessContext, "accessContext");
    }

    @Override
    public GuardDecision evaluate(NavigationContext from, NavigationContext to) {
        Objects.requireNonNull(to, "to");
        Optional<NavigationItem> item = registry.find(to.route());
        if (item.isEmpty()) {
            return GuardDecision.allow();
        }

        String permission = item.get().getRequiredPermission();
        if (permission != null && !permission.isBlank() && !accessContext.hasPermission(permission)) {
            return GuardDecision.block(PERMISSION_DENIED);
        }
        return GuardDecision.allow();
    }
}
