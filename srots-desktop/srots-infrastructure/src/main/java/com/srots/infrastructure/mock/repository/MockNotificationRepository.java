package com.srots.infrastructure.mock.repository;

import com.srots.domain.notification.AppNotification;
import com.srots.domain.repository.NotificationRepository;
import com.srots.infrastructure.mock.state.MockStateStore;
import com.srots.infrastructure.mock.support.MockQuerySupport;
import com.srots.infrastructure.mock.support.MockRepositoryBehavior;
import com.srots.shared.query.PageRequest;
import com.srots.shared.query.PageResult;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public final class MockNotificationRepository implements NotificationRepository {

    private final MockStateStore store;
    private final MockRepositoryBehavior behavior;

    public MockNotificationRepository(MockStateStore store, MockRepositoryBehavior behavior) {
        this.store = store;
        this.behavior = behavior;
    }

    @Override
    public Optional<AppNotification> findById(String id) {
        behavior.beforeRead();
        return store.notifications().stream().filter(e -> e.getId().equals(id)).findFirst();
    }

    @Override
    public List<AppNotification> findAll() {
        behavior.beforeRead();
        return store.notifications();
    }

    @Override
    public List<AppNotification> search(String query) {
        behavior.beforeRead();
        return MockQuerySupport.search(store.notifications(), query, e -> e.getTitle() + " " + nullToEmpty(e.getDescription()) + " " + e.getId());
    }

    @Override
    public PageResult<AppNotification> findPage(PageRequest pageRequest, String search, Map<String, String> filters) {
        behavior.beforeRead();
        return MockQuerySupport.query(
                store.notifications(),
                pageRequest,
                search,
                filters,
                e -> e.getTitle() + " " + nullToEmpty(e.getDescription()) + " " + e.getId(),
                e -> Map.of("type", e.getType().name(), "read", String.valueOf(e.isRead())),
                field -> {
                    return Comparator.comparing(AppNotification::getTimestamp, Comparator.nullsLast(Comparator.naturalOrder())).reversed();
                });
    }

    @Override
    public AppNotification save(AppNotification entity) {
        behavior.beforeWrite();
        return store.putNotification(entity);
    }

    @Override
    public boolean deleteById(String id) {
        behavior.beforeWrite();
        return store.removeNotification(id);
    }

    private static String nullToEmpty(String value) {
        return value == null ? "" : value;
    }
}
