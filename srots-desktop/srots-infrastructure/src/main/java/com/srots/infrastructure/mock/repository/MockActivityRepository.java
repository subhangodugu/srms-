package com.srots.infrastructure.mock.repository;

import com.srots.domain.activity.ActivityEntry;
import com.srots.domain.repository.ActivityRepository;
import com.srots.infrastructure.mock.state.MockStateStore;
import com.srots.infrastructure.mock.support.MockQuerySupport;
import com.srots.infrastructure.mock.support.MockRepositoryBehavior;
import com.srots.shared.query.PageRequest;
import com.srots.shared.query.PageResult;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public final class MockActivityRepository implements ActivityRepository {

    private final MockStateStore store;
    private final MockRepositoryBehavior behavior;

    public MockActivityRepository(MockStateStore store, MockRepositoryBehavior behavior) {
        this.store = store;
        this.behavior = behavior;
    }

    @Override
    public Optional<ActivityEntry> findById(String id) {
        behavior.beforeRead();
        return store.activities().stream().filter(e -> e.getId().equals(id)).findFirst();
    }

    @Override
    public List<ActivityEntry> findAll() {
        behavior.beforeRead();
        return store.activities();
    }

    @Override
    public List<ActivityEntry> search(String query) {
        behavior.beforeRead();
        return MockQuerySupport.search(store.activities(), query, e -> e.getSummary() + " " + e.getId());
    }

    @Override
    public PageResult<ActivityEntry> findPage(PageRequest pageRequest, String search, Map<String, String> filters) {
        behavior.beforeRead();
        return MockQuerySupport.query(
                store.activities(),
                pageRequest,
                search,
                filters,
                e -> e.getSummary() + " " + e.getId(),
                e -> Map.of("type", e.getType().name()),
                field -> {
                    return Comparator.comparing(ActivityEntry::getTimestamp, Comparator.nullsLast(Comparator.naturalOrder())).reversed();
                });
    }

    @Override
    public ActivityEntry save(ActivityEntry entity) {
        behavior.beforeWrite();
        return store.putActivity(entity);
    }

    @Override
    public boolean deleteById(String id) {
        behavior.beforeWrite();
        return store.removeActivity(id);
    }

    private static String nullToEmpty(String value) {
        return value == null ? "" : value;
    }
}
