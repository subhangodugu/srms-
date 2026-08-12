package com.srots.infrastructure.mock.datasource;

import com.srots.domain.project.Project;
import com.srots.infrastructure.mock.state.MockStateStore;

import java.util.List;
import java.util.Optional;

public final class MockProjectDataSource {
    private final MockStateStore store;

    public MockProjectDataSource(MockStateStore store) { this.store = store; }

    public List<Project> findAll() { return store.projects(); }
    public Optional<Project> findById(String id) { return store.project(id); }
    public Project save(Project project) { return store.putProject(project); }
    public boolean deleteById(String id) { return store.removeProject(id); }
}
