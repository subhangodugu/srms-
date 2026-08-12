package com.srots.domain.repository;

import com.srots.domain.notification.AppNotification;
import com.srots.shared.query.PageRequest;
import com.srots.shared.query.PageResult;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface NotificationRepository {
    Optional<AppNotification> findById(String id);
    List<AppNotification> findAll();
    List<AppNotification> search(String query);
    PageResult<AppNotification> findPage(PageRequest pageRequest, String search, Map<String, String> filters);
    AppNotification save(AppNotification entity);
    boolean deleteById(String id);
}
