package com.sorts.srms.backend.controller;

import com.sorts.srms.backend.dto.KnowledgeArticleDTO;
import com.sorts.srms.backend.security.CustomUserDetails;
import com.sorts.srms.backend.service.KnowledgeService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/knowledge")
public class KnowledgeController {

    private final KnowledgeService knowledgeService;

    public KnowledgeController(KnowledgeService knowledgeService) {
        this.knowledgeService = knowledgeService;
    }

    @GetMapping
    @PreAuthorize("hasAuthority('KB_VIEW') or hasRole('SUPER_ADMIN')")
    public ResponseEntity<List<KnowledgeArticleDTO>> getAllArticles(@RequestParam(required = false) String query) {
        return ResponseEntity.ok(knowledgeService.searchArticles(query));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('KB_VIEW') or hasRole('SUPER_ADMIN')")
    public ResponseEntity<KnowledgeArticleDTO> getArticleById(@PathVariable String id) {
        return ResponseEntity.ok(knowledgeService.getArticleById(id));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('KB_MANAGE') or hasRole('SUPER_ADMIN')")
    public ResponseEntity<KnowledgeArticleDTO> createArticle(
            @Valid @RequestBody KnowledgeArticleDTO dto,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        String authorId = userDetails != null ? userDetails.getUser().getId() : dto.getAuthorId();
        KnowledgeArticleDTO created = knowledgeService.createArticle(dto, authorId);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }
}
