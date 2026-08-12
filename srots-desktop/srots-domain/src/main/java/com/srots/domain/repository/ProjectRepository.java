package com.srots.domain.repository;

import com.srots.domain.project.Project;
import com.srots.shared.query.PageRequest;
import com.srots.shared.query.PageResult;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface ProjectRepository {
    Optional<Project> findById(String id);
    List<Project> findAll();
    List<Project> search(String query);
    PageResult<Project> findPage(PageRequest pageRequest, String search, Map<String, String> filters);
    Project save(Project entity);
    boolean deleteById(String id);
}
