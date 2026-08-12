package com.sorts.srms.backend.service;

import com.sorts.srms.backend.domain.model.Employee;
import com.sorts.srms.backend.domain.model.KnowledgeArticle;
import com.sorts.srms.backend.dto.KnowledgeArticleDTO;
import com.sorts.srms.backend.exception.ResourceNotFoundException;
import com.sorts.srms.backend.repository.EmployeeRepository;
import com.sorts.srms.backend.repository.KnowledgeArticleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class KnowledgeService {

    private final KnowledgeArticleRepository knowledgeRepository;
    private final EmployeeRepository employeeRepository;

    public KnowledgeService(KnowledgeArticleRepository knowledgeRepository, EmployeeRepository employeeRepository) {
        this.knowledgeRepository = knowledgeRepository;
        this.employeeRepository = employeeRepository;
    }

    @Transactional(readOnly = true)
    public List<KnowledgeArticleDTO> getAllArticles() {
        return knowledgeRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<KnowledgeArticleDTO> searchArticles(String query) {
        if (query == null || query.isBlank()) {
            return getAllArticles();
        }
        return knowledgeRepository.findByTitleContainingIgnoreCaseOrContentContainingIgnoreCase(query, query).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public KnowledgeArticleDTO getArticleById(String id) {
        KnowledgeArticle article = knowledgeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Knowledge article not found: " + id));
        article.setViews(article.getViews() + 1);
        return mapToDTO(knowledgeRepository.save(article));
    }

    @Transactional
    public KnowledgeArticleDTO createArticle(KnowledgeArticleDTO dto, String authorEmployeeId) {
        Employee author = employeeRepository.findById(authorEmployeeId)
                .orElseGet(() -> employeeRepository.findAll().stream().findFirst()
                        .orElseThrow(() -> new ResourceNotFoundException("Author employee not found")));

        KnowledgeArticle article = new KnowledgeArticle();
        article.setId(UUID.randomUUID().toString());
        article.setAuthor(author);
        article.setTitle(dto.getTitle());
        article.setCategory(dto.getCategory());
        article.setTags(dto.getTags());
        article.setContent(dto.getContent());
        article.setStatus(dto.getStatus() != null ? dto.getStatus() : "PUBLISHED");
        article.setViews(0);

        return mapToDTO(knowledgeRepository.save(article));
    }

    private KnowledgeArticleDTO mapToDTO(KnowledgeArticle a) {
        KnowledgeArticleDTO dto = new KnowledgeArticleDTO();
        dto.setId(a.getId());
        dto.setAuthorId(a.getAuthor().getId());
        dto.setAuthorName(a.getAuthor().getUser().getFirstName() + " " + a.getAuthor().getUser().getLastName());
        dto.setTitle(a.getTitle());
        dto.setCategory(a.getCategory());
        dto.setTags(a.getTags());
        dto.setContent(a.getContent());
        dto.setViews(a.getViews());
        dto.setStatus(a.getStatus());
        dto.setCreatedAt(a.getCreatedAt());
        dto.setUpdatedAt(a.getUpdatedAt());
        return dto;
    }
}
