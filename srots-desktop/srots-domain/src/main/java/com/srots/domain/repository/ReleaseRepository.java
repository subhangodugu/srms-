package com.srots.domain.repository;

import com.srots.domain.release.Release;
import com.srots.shared.query.PageRequest;
import com.srots.shared.query.PageResult;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface ReleaseRepository {
    Optional<Release> findById(String id);
    List<Release> findAll();
    List<Release> search(String query);
    PageResult<Release> findPage(PageRequest pageRequest, String search, Map<String, String> filters);
    Release save(Release entity);
    boolean deleteById(String id);
}
