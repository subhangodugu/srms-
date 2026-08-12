package com.srots.presentation.navigation.guard;

import com.srots.presentation.navigation.model.NavigationContext;
import java.util.Objects;
import java.util.function.BooleanSupplier;
import java.util.function.Supplier;

/**
 * Cancels navigation when unsaved changes exist and the user declines discard.
 */
public final class UnsavedChangesGuard implements NavigationGuard {

    public static final String DISCARD_DECLINED = "UNSAVED_CHANGES_DISCARD_DECLINED";

    private final BooleanSupplier hasUnsavedChanges;
    private final Supplier<Boolean> confirmDiscard;

    public UnsavedChangesGuard(BooleanSupplier hasUnsavedChanges, Supplier<Boolean> confirmDiscard) {
        this.hasUnsavedChanges = Objects.requireNonNull(hasUnsavedChanges, "hasUnsavedChanges");
        this.confirmDiscard = Objects.requireNonNull(confirmDiscard, "confirmDiscard");
    }

    @Override
    public GuardDecision evaluate(NavigationContext from, NavigationContext to) {
        if (!hasUnsavedChanges.getAsBoolean()) {
            return GuardDecision.allow();
        }
        Boolean discard = confirmDiscard.get();
        if (Boolean.TRUE.equals(discard)) {
            return GuardDecision.allow();
        }
        return GuardDecision.cancel(DISCARD_DECLINED);
    }
}
