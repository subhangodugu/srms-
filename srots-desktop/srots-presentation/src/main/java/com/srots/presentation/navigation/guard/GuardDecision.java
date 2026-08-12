package com.srots.presentation.navigation.guard;

/**
 * Result of evaluating a {@link NavigationGuard}.
 */
public record GuardDecision(boolean allowed, boolean cancelled, String message) {

    public static GuardDecision allow() {
        return new GuardDecision(true, false, null);
    }

    public static GuardDecision block(String message) {
        return new GuardDecision(false, false, message);
    }

    public static GuardDecision cancel(String message) {
        return new GuardDecision(false, true, message);
    }
}
