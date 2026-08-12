package com.sorts.srms.backend.dto;

import jakarta.validation.constraints.NotBlank;
import java.time.LocalDate;

public class ReportRequestDTO {

    @NotBlank(message = "Report Type is required")
    private String reportType; // EXECUTIVE_SUMMARY, EMPLOYEE_DIRECTORY, PROJECT_STATUS, ASSET_INVENTORY, SERVICEDESK_SLA

    @NotBlank(message = "Format is required")
    private String format; // PDF, CSV

    private String companyId;
    private LocalDate startDate;
    private LocalDate endDate;

    public ReportRequestDTO() {}

    public String getReportType() { return reportType; }
    public void setReportType(String reportType) { this.reportType = reportType; }
    public String getFormat() { return format; }
    public void setFormat(String format) { this.format = format; }
    public String getCompanyId() { return companyId; }
    public void setCompanyId(String companyId) { this.companyId = companyId; }
    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }
    public LocalDate getEndDate() { return endDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }
}
