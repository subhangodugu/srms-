package com.srots.presentation.profile;

import com.srots.presentation.navigation.model.NavigationRouteId;
import com.srots.presentation.navigation.service.NavigationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

/**
 * Presentation authentication adapter. Clears session via {@link SessionService} only.
 */
public final class DefaultAuthenticationService implements AuthenticationService {

    private static final Logger log = LoggerFactory.getLogger(DefaultAuthenticationService.class);

    private final SessionService sessionService;
    private final NavigationService navigationService;
    private final AtomicBoolean signingOut = new AtomicBoolean(false);
    private final Consumer<String> errorHandler;

    public DefaultAuthenticationService(
            SessionService sessionService,
            NavigationService navigationService) {
        this(sessionService, navigationService, message -> log.warn("Sign-out failed: {}", message));
    }

    public DefaultAuthenticationService(
            SessionService sessionService,
            NavigationService navigationService,
            Consumer<String> errorHandler) {
        this.sessionService = Objects.requireNonNull(sessionService, "sessionService");
        this.navigationService = Objects.requireNonNull(navigationService, "navigationService");
        this.errorHandler = errorHandler == null ? message -> {
        } : errorHandler;
    }

    @Override
    public void signOut() {
        SessionState state = sessionService.getSessionState();
        if (state == SessionState.SIGNED_OUT || state == SessionState.SESSION_EXPIRED) {
            return;
        }
        if (!signingOut.compareAndSet(false, true)) {
            return;
        }
        CurrentUser previous = sessionService.getCurrentUser();
        try {
            sessionService.beginSignOut();
            sessionService.completeSignOut();
            navigationService.navigate(NavigationRouteId.LOGIN);
        } catch (RuntimeException ex) {
            log.warn("Sign-out failed", ex);
            sessionService.restoreAuthenticated(previous);
            errorHandler.accept("Unable to sign out. Please try again.");
        } finally {
            signingOut.set(false);
        }
    }

    @Override
    public boolean isSigningOut() {
        return signingOut.get() || sessionService.getSessionState() == SessionState.SIGNING_OUT;
    }
}
