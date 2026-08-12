package com.srots.infrastructure.mock.repository;

import com.srots.domain.team.Team;
import com.srots.domain.repository.TeamRepository;
import com.srots.infrastructure.mock.state.MockStateStore;
import com.srots.infrastructure.mock.support.MockQuerySupport;
import com.srots.infrastructure.mock.support.MockRepositoryBehavior;
import com.srots.shared.query.PageRequest;
import com.srots.shared.query.PageResult;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public final class MockTeamRepository implements TeamRepository {

    private final MockStateStore store;
    private final MockRepositoryBehavior behavior;

    public MockTeamRepository(MockStateStore store, MockRepositoryBehavior behavior) {
        this.store = store;
        this.behavior = behavior;
    }

    @Override
    public Optional<Team> findById(String id) {
        behavior.beforeRead();
        return store.teams().stream().filter(e -> e.getId().equals(id)).findFirst();
    }

    @Override
    public List<Team> findAll() {
        behavior.beforeRead();
        return store.teams();
    }

    @Override
    public List<Team> search(String query) {
        behavior.beforeRead();
        return MockQuerySupport.search(store.teams(), query, e -> e.getName() + " " + e.getId());
    }

    @Override
    public PageResult<Team> findPage(PageRequest pageRequest, String search, Map<String, String> filters) {
        behavior.beforeRead();
        return MockQuerySupport.query(
                store.teams(),
                pageRequest,
                search,
                filters,
                e -> e.getName() + " " + e.getId(),
                e -> Map.of("departmentId", nullToEmpty(e.getDepartmentId())),
                field -> {
                    return Comparator.comparing(Team::getName, String.CASE_INSENSITIVE_ORDER);
                });
    }

    @Override
    public Team save(Team entity) {
        behavior.beforeWrite();
        return store.putTeam(entity);
    }

    @Override
    public boolean deleteById(String id) {
        behavior.beforeWrite();
        return store.removeTeam(id);
    }

    private static String nullToEmpty(String value) {
        return value == null ? "" : value;
    }
}
