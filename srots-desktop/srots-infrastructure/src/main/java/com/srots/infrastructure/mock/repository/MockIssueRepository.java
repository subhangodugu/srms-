package com.srots.infrastructure.mock.repository;

import com.srots.domain.issue.Issue;
import com.srots.domain.repository.IssueRepository;
import com.srots.infrastructure.mock.state.MockStateStore;
import com.srots.infrastructure.mock.support.MockQuerySupport;
import com.srots.infrastructure.mock.support.MockRepositoryBehavior;
import com.srots.shared.query.PageRequest;
import com.srots.shared.query.PageResult;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public final class MockIssueRepository implements IssueRepository {

    private final MockStateStore store;
    private final MockRepositoryBehavior behavior;

    public MockIssueRepository(MockStateStore store, MockRepositoryBehavior behavior) {
        this.store = store;
        this.behavior = behavior;
    }

    @Override
    public Optional<Issue> findById(String id) {
        behavior.beforeRead();
        return store.issue(id);
    }

    @Override
    public List<Issue> findAll() {
        behavior.beforeRead();
        return store.issues();
    }

    @Override
    public List<Issue> search(String query) {
        behavior.beforeRead();
        return MockQuerySupport.search(store.issues(), query, e -> e.getTitle() + " " + nullToEmpty(e.getDescription()) + " " + e.getId());
    }

    @Override
    public PageResult<Issue> findPage(PageRequest pageRequest, String search, Map<String, String> filters) {
        behavior.beforeRead();
        return MockQuerySupport.query(
                store.issues(),
                pageRequest,
                search,
                filters,
                e -> e.getTitle() + " " + nullToEmpty(e.getDescription()) + " " + e.getId(),
                e -> Map.of("status", e.getStatus().name(), "priority", e.getPriority().name(), "productId", nullToEmpty(e.getProductId())),
                field -> {
                    if ("title".equals(field)) return Comparator.comparing(Issue::getTitle, String.CASE_INSENSITIVE_ORDER);
                    if ("updatedDate".equals(field)) return Comparator.comparing(Issue::getUpdatedDate, Comparator.nullsLast(Comparator.naturalOrder()));
                    return Comparator.comparing(Issue::getId);
                });
    }

    @Override
    public Issue save(Issue entity) {
        behavior.beforeWrite();
        return store.putIssue(entity);
    }

    @Override
    public boolean deleteById(String id) {
        behavior.beforeWrite();
        return store.removeIssue(id);
    }

    private static String nullToEmpty(String value) {
        return value == null ? "" : value;
    }
}
