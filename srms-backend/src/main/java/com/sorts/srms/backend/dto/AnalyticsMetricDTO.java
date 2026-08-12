package com.sorts.srms.backend.dto;

import java.util.Map;

public class AnalyticsMetricDTO {

    private long totalCompanies;
    private long totalEmployees;
    private long totalProjects;
    private long activeProjects;
    private long totalTickets;
    private long openTickets;
    private long urgentTickets;
    private long totalAssets;
    private double totalAssetValue;
    private double projectCompletionRate;
    private double ticketResolutionSlaPercentage;
    private Map<String, Long> projectsByStatus;
    private Map<String, Long> ticketsByPriority;
    private Map<String, Long> assetsByCategory;

    public AnalyticsMetricDTO() {}

    public long getTotalCompanies() { return totalCompanies; }
    public void setTotalCompanies(long totalCompanies) { this.totalCompanies = totalCompanies; }
    public long getTotalEmployees() { return totalEmployees; }
    public void setTotalEmployees(long totalEmployees) { this.totalEmployees = totalEmployees; }
    public long getTotalProjects() { return totalProjects; }
    public void setTotalProjects(long totalProjects) { this.totalProjects = totalProjects; }
    public long getActiveProjects() { return activeProjects; }
    public void setActiveProjects(long activeProjects) { this.activeProjects = activeProjects; }
    public long getTotalTickets() { return totalTickets; }
    public void setTotalTickets(long totalTickets) { this.totalTickets = totalTickets; }
    public long getOpenTickets() { return openTickets; }
    public void setOpenTickets(long openTickets) { this.openTickets = openTickets; }
    public long getUrgentTickets() { return urgentTickets; }
    public void setUrgentTickets(long urgentTickets) { this.urgentTickets = urgentTickets; }
    public long getTotalAssets() { return totalAssets; }
    public void setTotalAssets(long totalAssets) { this.totalAssets = totalAssets; }
    public double getTotalAssetValue() { return totalAssetValue; }
    public void setTotalAssetValue(double totalAssetValue) { this.totalAssetValue = totalAssetValue; }
    public double getProjectCompletionRate() { return projectCompletionRate; }
    public void setProjectCompletionRate(double projectCompletionRate) { this.projectCompletionRate = projectCompletionRate; }
    public double getTicketResolutionSlaPercentage() { return ticketResolutionSlaPercentage; }
    public void setTicketResolutionSlaPercentage(double ticketResolutionSlaPercentage) { this.ticketResolutionSlaPercentage = ticketResolutionSlaPercentage; }
    public Map<String, Long> getProjectsByStatus() { return projectsByStatus; }
    public void setProjectsByStatus(Map<String, Long> projectsByStatus) { this.projectsByStatus = projectsByStatus; }
    public Map<String, Long> getTicketsByPriority() { return ticketsByPriority; }
    public void setTicketsByPriority(Map<String, Long> ticketsByPriority) { this.ticketsByPriority = ticketsByPriority; }
    public Map<String, Long> getAssetsByCategory() { return assetsByCategory; }
    public void setAssetsByCategory(Map<String, Long> assetsByCategory) { this.assetsByCategory = assetsByCategory; }
}
