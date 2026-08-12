package com.sorts.srms.backend.service;

import com.sorts.srms.backend.domain.model.Employee;
import com.sorts.srms.backend.domain.model.Project;
import com.sorts.srms.backend.dto.ReportRequestDTO;
import com.sorts.srms.backend.repository.EmployeeRepository;
import com.sorts.srms.backend.repository.ProjectRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReportService {

    private final EmployeeRepository employeeRepository;
    private final ProjectRepository projectRepository;

    public ReportService(EmployeeRepository employeeRepository, ProjectRepository projectRepository) {
        this.employeeRepository = employeeRepository;
        this.projectRepository = projectRepository;
    }

    @Transactional(readOnly = true)
    public byte[] generateReport(ReportRequestDTO request) {
        StringBuilder sb = new StringBuilder();
        String reportType = request.getReportType() != null ? request.getReportType() : "EXECUTIVE_SUMMARY";
        String format = request.getFormat() != null ? request.getFormat() : "CSV";

        if ("CSV".equalsIgnoreCase(format)) {
            if ("EMPLOYEE_DIRECTORY".equalsIgnoreCase(reportType)) {
                sb.append("Employee Code,First Name,Last Name,Email,Job Title,Employment Type,Status\n");
                List<Employee> employees = request.getCompanyId() != null ?
                        employeeRepository.findByCompanyId(request.getCompanyId()) : employeeRepository.findAll();
                for (Employee e : employees) {
                    sb.append(e.getEmployeeCode()).append(",")
                            .append(e.getUser().getFirstName()).append(",")
                            .append(e.getUser().getLastName()).append(",")
                            .append(e.getUser().getEmail()).append(",")
                            .append(e.getJobTitle()).append(",")
                            .append(e.getEmploymentType()).append(",")
                            .append(e.getStatus().name()).append("\n");
                }
            } else {
                sb.append("Project Code,Project Name,Status,Priority,Budget,Start Date,End Date\n");
                List<Project> projects = request.getCompanyId() != null ?
                        projectRepository.findByCompanyId(request.getCompanyId()) : projectRepository.findAll();
                for (Project p : projects) {
                    sb.append(p.getCode()).append(",")
                            .append("\"").append(p.getName()).append("\",")
                            .append(p.getStatus().name()).append(",")
                            .append(p.getPriority().name()).append(",")
                            .append(p.getBudget()).append(",")
                            .append(p.getStartDate()).append(",")
                            .append(p.getEndDate()).append("\n");
                }
            }
        } else { // PDF / Text format
            sb.append("=================================================================\n");
            sb.append(" SRMS ENTERPRISE REPORT: ").append(reportType).append("\n");
            sb.append(" Generated: ").append(LocalDateTime.now()).append("\n");
            sb.append("=================================================================\n\n");

            sb.append("Summary of Records:\n");
            List<Project> projects = projectRepository.findAll();
            for (Project p : projects) {
                sb.append(" - [").append(p.getCode()).append("] ").append(p.getName())
                        .append(" | Status: ").append(p.getStatus())
                        .append(" | Budget: $").append(p.getBudget()).append("\n");
            }
        }

        return sb.toString().getBytes(StandardCharsets.UTF_8);
    }
}
