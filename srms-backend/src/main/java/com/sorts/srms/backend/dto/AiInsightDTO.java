package com.sorts.srms.backend.dto;

import java.util.List;

public class AiInsightDTO {

    private String insightId;
    private String category;
    private String title;
    private String summary;
    private double confidenceScore;
    private String riskLevel; // LOW, MEDIUM, HIGH, CRITICAL
    private List<String> recommendedActions;
    private String targetEntity;
    private String targetEntityId;

    public AiInsightDTO() {}

    public AiInsightDTO(String insightId, String category, String title, String summary, double confidenceScore, String riskLevel, List<String> recommendedActions, String targetEntity, String targetEntityId) {
        this.insightId = insightId;
        this.category = category;
        this.title = title;
        this.summary = summary;
        this.confidenceScore = confidenceScore;
        this.riskLevel = riskLevel;
        this.recommendedActions = recommendedActions;
        this.targetEntity = targetEntity;
        this.targetEntityId = targetEntityId;
    }

    public String getInsightId() { return insightId; }
    public void setInsightId(String insightId) { this.insightId = insightId; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getSummary() { return summary; }
    public void setSummary(String summary) { this.summary = summary; }
    public double getConfidenceScore() { return confidenceScore; }
    public void setConfidenceScore(double confidenceScore) { this.confidenceScore = confidenceScore; }
    public String getRiskLevel() { return riskLevel; }
    public void setRiskLevel(String riskLevel) { this.riskLevel = riskLevel; }
    public List<String> getRecommendedActions() { return recommendedActions; }
    public void setRecommendedActions(List<String> recommendedActions) { this.recommendedActions = recommendedActions; }
    public String getTargetEntity() { return targetEntity; }
    public void setTargetEntity(String targetEntity) { this.targetEntity = targetEntity; }
    public String getTargetEntityId() { return targetEntityId; }
    public void setTargetEntityId(String targetEntityId) { this.targetEntityId = targetEntityId; }
}
