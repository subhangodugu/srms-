package com.srots.domain.repository;

import com.srots.domain.activity.ActivityEntry;
import com.srots.shared.query.PageRequest;
import com.srots.shared.query.PageResult;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface ActivityRepository {
    Optional<ActivityEntry> findById(String id);
    List<ActivityEntry> findAll();
    List<ActivityEntry> search(String query);
    PageResult<ActivityEntry> findPage(PageRequest pageRequest, String search, Map<String, String> filters);
    ActivityEntry save(ActivityEntry entity);
    boolean deleteById(String id);
}
