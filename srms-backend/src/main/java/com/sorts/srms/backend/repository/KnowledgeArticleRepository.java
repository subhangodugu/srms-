package com.sorts.srms.backend.repository;

import com.sorts.srms.backend.domain.model.KnowledgeArticle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface KnowledgeArticleRepository extends JpaRepository<KnowledgeArticle, String> {
    List<KnowledgeArticle> findByCategory(String category);
    List<KnowledgeArticle> findByTitleContainingIgnoreCaseOrContentContainingIgnoreCase(String titleQuery, String contentQuery);
}
