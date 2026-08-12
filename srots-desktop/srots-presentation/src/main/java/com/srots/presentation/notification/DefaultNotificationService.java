package com.srots.presentation.notification;

import com.srots.presentation.navigation.model.NavigationRouteId;
import com.srots.presentation.shell.topbar.TopBarApplicationState;
import javafx.application.Platform;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * In-memory notification service for desktop chrome. Transport-agnostic façade.
 */
public final class DefaultNotificationService implements NotificationService {

    private static final Logger log = LoggerFactory.getLogger(DefaultNotificationService.class);
    public static final int INITIAL_LIMIT = 50;

    private final NotificationState state = new NotificationState();
    private final TopBarApplicationState topBarApplicationState;
    private final AtomicBoolean refreshing = new AtomicBoolean(false);
    private final AtomicBoolean failNextRefresh = new AtomicBoolean(false);
    private List<SrotsNotification> store = new ArrayList<>();

    public DefaultNotificationService(TopBarApplicationState topBarApplicationState) {
        this.topBarApplicationState = topBarApplicationState;
        this.store = new ArrayList<>(developmentSeed());
        publishToState(store);
    }

    public static DefaultNotificationService developmentDefaults(TopBarApplicationState topBarState) {
        return new DefaultNotificationService(topBarState);
    }

    @Override
    public NotificationState state() {
        return state;
    }

    @Override
    public void refresh() {
        if (!refreshing.compareAndSet(false, true)) {
            return;
        }
        runOnFx(() -> state.setLoading());
        CompletableFuture.supplyAsync(() -> {
            if (failNextRefresh.getAndSet(false)) {
                throw new IllegalStateException("Simulated notification refresh failure");
            }
            return List.copyOf(store);
        }).whenComplete((items, error) -> runOnFx(() -> {
            try {
                if (error != null) {
                    log.warn("Notification refresh failed", error);
                    state.setError("Unable to load notifications.");
                } else {
                    publishToState(items);
                }
            } finally {
                refreshing.set(false);
            }
        }));
    }

    @Override
    public void markAsRead(String id) {
        if (id == null || id.isBlank()) {
            return;
        }
        runOnFx(() -> {
            List<SrotsNotification> next = new ArrayList<>(store.size());
            for (SrotsNotification notification : store) {
                if (Objects.equals(notification.getId(), id) && !notification.isRead()) {
                    next.add(notification.withRead(true));
                } else {
                    next.add(notification);
                }
            }
            store = next;
            state.markAsRead(id);
            syncBadge();
        });
    }

    @Override
    public void markAllAsRead() {
        runOnFx(() -> {
            List<SrotsNotification> next = new ArrayList<>(store.size());
            for (SrotsNotification notification : store) {
                next.add(notification.isRead() ? notification : notification.withRead(true));
            }
            store = next;
            state.markAllAsRead();
            syncBadge();
        });
    }

    /** Test helper — inject failure on next refresh. */
    void failNextRefresh() {
        failNextRefresh.set(true);
    }

    /** Test / realtime helper — append a notification through the service. */
    public void publish(SrotsNotification notification) {
        if (notification == null) {
            return;
        }
        runOnFx(() -> {
            List<SrotsNotification> next = new ArrayList<>(store);
            next.removeIf(n -> Objects.equals(n.getId(), notification.getId()));
            next.add(notification);
            store = next;
            state.upsert(notification);
            syncBadge();
        });
    }

    private void publishToState(List<SrotsNotification> items) {
        List<SrotsNotification> limited = items == null ? List.of() : items;
        if (limited.size() > INITIAL_LIMIT) {
            limited = limited.subList(0, INITIAL_LIMIT);
        }
        state.replaceAll(limited);
        syncBadge();
    }

    private void syncBadge() {
        if (topBarApplicationState != null) {
            topBarApplicationState.setNotificationCount(state.getUnreadCount());
        }
    }

    private static void runOnFx(Runnable action) {
        if (Platform.isFxApplicationThread()) {
            action.run();
        } else {
            Platform.runLater(action);
        }
    }

    static List<SrotsNotification> developmentSeed() {
        Instant now = Instant.now();
        return List.of(
                new SrotsNotification(
                        "n-release-1",
                        NotificationKind.RELEASE,
                        "Release deployed",
                        "COMPTY v2.4 was successfully deployed.",
                        now.minus(5, ChronoUnit.MINUTES),
                        false,
                        NotificationPriority.NORMAL,
                        NotificationAction.navigate(NavigationRouteId.COMPTY_RELEASES)),
                new SrotsNotification(
                        "n-approval-1",
                        NotificationKind.APPROVAL,
                        "Approval required",
                        "Release approval pending for COMPTY v2.5.",
                        now.minus(25, ChronoUnit.MINUTES),
                        false,
                        NotificationPriority.HIGH,
                        NotificationAction.navigate(NavigationRouteId.RELEASES)),
                new SrotsNotification(
                        "n-task-1",
                        NotificationKind.TASK,
                        "New task assigned",
                        "Scan optimization task assigned to you.",
                        now.minus(1, ChronoUnit.HOURS),
                        false,
                        NotificationPriority.NORMAL,
                        NotificationAction.navigate(NavigationRouteId.WORKSPACE_TASKS)),
                new SrotsNotification(
                        "n-security-1",
                        NotificationKind.SECURITY,
                        "Security alert",
                        "Unusual sign-in attempt was blocked.",
                        now.minus(3, ChronoUnit.HOURS),
                        true,
                        NotificationPriority.CRITICAL,
                        NotificationAction.navigate(NavigationRouteId.SETTINGS)),
                new SrotsNotification(
                        "n-support-1",
                        NotificationKind.SERVICE_DESK,
                        "Support request assigned",
                        "Ticket SD-214 is waiting for triage.",
                        now.minus(26, ChronoUnit.HOURS),
                        true,
                        NotificationPriority.NORMAL,
                        NotificationAction.navigate(NavigationRouteId.SUPPORT)),
                new SrotsNotification(
                        "n-project-1",
                        NotificationKind.PROJECT,
                        "Milestone completed",
                        "Platform hardening milestone marked complete.",
                        now.minus(2, ChronoUnit.DAYS),
                        true,
                        NotificationPriority.LOW,
                        NotificationAction.navigate(NavigationRouteId.PROJECTS))
        );
    }
}
