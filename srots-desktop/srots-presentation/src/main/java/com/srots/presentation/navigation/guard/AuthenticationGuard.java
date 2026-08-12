package com.srots.presentation.navigation.guard;

import com.srots.presentation.navigation.model.NavigationContext;
import com.srots.presentation.navigation.model.NavigationRouteId;
import com.srots.presentation.navigation.service.UserAccessContext;
import java.util.Objects;

/**
 * Blocks navigation when the user is not authenticated (except LOGIN).
 */
public final class AuthenticationGuard implements NavigationGuard {

    public static final String AUTHENTICATION_REQUIRED = "AUTHENTICATION_REQUIRED";

    private final UserAccessContext accessContext;

    public AuthenticationGuard(UserAccessContext accessContext) {
        this.accessContext = Objects.requireNonNull(accessContext, "accessContext");
    }

    @Override
    public GuardDecision evaluate(NavigationContext from, NavigationContext to) {
        Objects.requireNonNull(to, "to");
        if (to.route() == NavigationRouteId.LOGIN) {
            return GuardDecision.allow();
        }
        if (!accessContext.isAuthenticated()) {
            return GuardDecision.block(AUTHENTICATION_REQUIRED);
        }
        return GuardDecision.allow();
    }
}
