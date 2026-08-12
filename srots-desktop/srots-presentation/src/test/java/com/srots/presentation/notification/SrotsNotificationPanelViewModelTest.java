package com.srots.presentation.notification;

import com.srots.presentation.components.support.JavaFxTestSupport;
import com.srots.presentation.components.utility.icons.SrotsIcon;
import com.srots.presentation.navigation.NavigationModule;
import com.srots.presentation.navigation.model.NavigationRouteId;
import com.srots.presentation.shell.topbar.TopBarApplicationState;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SrotsNotificationPanelViewModelTest {

    @BeforeAll
    static void bootFx() throws Exception {
        JavaFxTestSupport.ensureToolkit();
    }

    @Test
    void seed_ordersNewestFirstAndComputesUnread() throws Exception {
        JavaFxTestSupport.runOnFxThread(() -> {
            Fixture fx = Fixture.create();
            assertEquals(3, fx.viewModel.getUnreadCount());
            assertEquals(3, fx.topBarState.getNotificationCount());
            List<SrotsNotification> items = fx.service.state().getNotifications();
            assertFalse(items.isEmpty());
            for (int i = 1; i < items.size(); i++) {
                assertTrue(!items.get(i - 1).getTimestamp().isBefore(items.get(i).getTimestamp()));
            }
        });
    }

    @Test
    void unreadFilter_showsOnlyUnread() throws Exception {
        JavaFxTestSupport.runOnFxThread(() -> {
            Fixture fx = Fixture.create();
            fx.viewModel.setFilter(NotificationFilter.UNREAD);
            assertTrue(fx.viewModel.getVisibleNotifications().stream().noneMatch(SrotsNotification::isRead));
            assertEquals(3, fx.viewModel.getVisibleNotifications().size());
        });
    }

    @Test
    void markAsRead_decrementsBadge() throws Exception {
        JavaFxTestSupport.runOnFxThread(() -> {
            Fixture fx = Fixture.create();
            String id = fx.viewModel.getVisibleNotifications().get(0).getId();
            fx.service.markAsRead(id);
            assertEquals(2, fx.viewModel.getUnreadCount());
            assertEquals(2, fx.topBarState.getNotificationCount());
        });
    }

    @Test
    void markAllAsRead_clearsBadge() throws Exception {
        JavaFxTestSupport.runOnFxThread(() -> {
            Fixture fx = Fixture.create();
            fx.viewModel.markAllAsRead();
            assertEquals(0, fx.viewModel.getUnreadCount());
            assertEquals(0, fx.topBarState.getNotificationCount());
            fx.viewModel.setFilter(NotificationFilter.UNREAD);
            assertTrue(fx.viewModel.getVisibleNotifications().isEmpty());
            assertTrue(fx.viewModel.emptyProperty().get());
        });
    }

    @Test
    void activate_marksReadAndNavigates() throws Exception {
        JavaFxTestSupport.runOnFxThread(() -> {
            Fixture fx = Fixture.create();
            SrotsNotification release = fx.service.state().getNotifications().stream()
                    .filter(n -> "n-release-1".equals(n.getId()))
                    .findFirst()
                    .orElseThrow();
            fx.viewModel.openPanel();
            fx.viewModel.activate(release);
            assertEquals(NavigationRouteId.COMPTY_RELEASES, fx.module.navigationService().currentRoute());
            assertFalse(fx.viewModel.isPanelOpen());
            assertEquals(2, fx.viewModel.getUnreadCount());
        });
    }

    @Test
    void emptyState_messagesDifferByFilter() throws Exception {
        JavaFxTestSupport.runOnFxThread(() -> {
            Fixture fx = Fixture.create();
            fx.viewModel.markAllAsRead();
            fx.viewModel.setFilter(NotificationFilter.ALL);
            assertEquals("You're all caught up.", fx.viewModel.emptyMessageProperty().get());
            fx.viewModel.setFilter(NotificationFilter.UNREAD);
            assertEquals("No unread notifications.", fx.viewModel.emptyMessageProperty().get());
        });
    }

    @Test
    void refreshError_setsSafeMessage() throws Exception {
        Fixture[] box = new Fixture[1];
        JavaFxTestSupport.runOnFxThread(() -> box[0] = Fixture.create());
        Fixture fx = box[0];

        CountDownLatch latch = new CountDownLatch(1);
        JavaFxTestSupport.runOnFxThread(() -> {
            fx.service.failNextRefresh();
            fx.service.state().loadStatusProperty().addListener((obs, o, n) -> {
                if (n == NotificationState.LoadStatus.ERROR) {
                    latch.countDown();
                }
            });
            fx.service.refresh();
        });

        assertTrue(latch.await(2, TimeUnit.SECONDS));
        JavaFxTestSupport.runOnFxThread(() -> {
            assertTrue(fx.viewModel.errorProperty().get());
            assertEquals("Unable to load notifications.", fx.viewModel.errorMessageProperty().get());
            assertFalse(fx.service.state().getNotifications().isEmpty());
        });
    }

    @Test
    void timestampFormatter_relativeAndAbsolute() {
        Instant now = Instant.parse("2026-08-12T12:00:00Z");
        NotificationTimestampFormatter formatter = new NotificationTimestampFormatter(
                Clock.fixed(now, ZoneOffset.UTC), ZoneOffset.UTC);
        assertEquals("Just now", formatter.format(now.minusSeconds(10)));
        assertEquals("5 min ago", formatter.format(now.minus(5, ChronoUnit.MINUTES)));
        assertEquals("1 hour ago", formatter.format(now.minus(1, ChronoUnit.HOURS)));
        assertEquals("Yesterday", formatter.format(now.minus(26, ChronoUnit.HOURS)));
        assertEquals("10 Aug 2026", formatter.format(now.minus(2, ChronoUnit.DAYS)));
    }

    @Test
    void iconResolver_usesCentralMapping() {
        assertEquals(SrotsIcon.RELEASE, NotificationIconResolver.resolve(NotificationKind.RELEASE));
        assertEquals(SrotsIcon.SECURITY, NotificationIconResolver.resolve(NotificationKind.SECURITY));
        assertEquals(SrotsIcon.BELL, NotificationIconResolver.resolve(null));
    }

    @Test
    void panelOpenClose_togglesState() throws Exception {
        JavaFxTestSupport.runOnFxThread(() -> {
            Fixture fx = Fixture.create();
            assertFalse(fx.viewModel.isPanelOpen());
            fx.viewModel.openPanel();
            assertTrue(fx.viewModel.isPanelOpen());
            fx.viewModel.closePanel();
            assertFalse(fx.viewModel.isPanelOpen());
        });
    }

    private static final class Fixture {
        final NavigationModule module;
        final TopBarApplicationState topBarState;
        final DefaultNotificationService service;
        final SrotsNotificationPanelViewModel viewModel;

        private Fixture(
                NavigationModule module,
                TopBarApplicationState topBarState,
                DefaultNotificationService service,
                SrotsNotificationPanelViewModel viewModel) {
            this.module = module;
            this.topBarState = topBarState;
            this.service = service;
            this.viewModel = viewModel;
        }

        static Fixture create() {
            NavigationModule module = NavigationModule.createDefault();
            TopBarApplicationState topBarState = new TopBarApplicationState();
            DefaultNotificationService service = DefaultNotificationService.developmentDefaults(topBarState);
            SrotsNotificationPanelViewModel vm = new SrotsNotificationPanelViewModel();
            vm.bind(service, module.navigationService());
            return new Fixture(module, topBarState, service, vm);
        }
    }
}
