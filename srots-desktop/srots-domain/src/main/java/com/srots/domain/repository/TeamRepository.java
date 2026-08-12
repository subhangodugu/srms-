package com.srots.domain.repository;

import com.srots.domain.team.Team;
import com.srots.shared.query.PageRequest;
import com.srots.shared.query.PageResult;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface TeamRepository {
    Optional<Team> findById(String id);
    List<Team> findAll();
    List<Team> search(String query);
    PageResult<Team> findPage(PageRequest pageRequest, String search, Map<String, String> filters);
    Team save(Team entity);
    boolean deleteById(String id);
}
