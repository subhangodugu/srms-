package com.sorts.srms.backend.controller;

import com.sorts.srms.backend.dto.AiInsightDTO;
import com.sorts.srms.backend.service.AiService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/ai")
public class AiController {

    private final AiService aiService;

    public AiController(AiService aiService) {
        this.aiService = aiService;
    }

    @GetMapping("/insights/company/{companyId}")
    @PreAuthorize("hasAuthority('AI_VIEW') or hasRole('SUPER_ADMIN')")
    public ResponseEntity<List<AiInsightDTO>> getInsightsForCompany(@PathVariable String companyId) {
        return ResponseEntity.ok(aiService.generateInsightsForCompany(companyId));
    }
}
