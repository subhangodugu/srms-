package com.srots.domain.repository;

import com.srots.domain.issue.Issue;
import com.srots.shared.query.PageRequest;
import com.srots.shared.query.PageResult;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface IssueRepository {
    Optional<Issue> findById(String id);
    List<Issue> findAll();
    List<Issue> search(String query);
    PageResult<Issue> findPage(PageRequest pageRequest, String search, Map<String, String> filters);
    Issue save(Issue entity);
    boolean deleteById(String id);
}
