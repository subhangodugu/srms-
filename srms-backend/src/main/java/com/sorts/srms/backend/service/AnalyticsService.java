package com.sorts.srms.backend.service;

import com.sorts.srms.backend.domain.enums.Priority;
import com.sorts.srms.backend.domain.enums.ProjectStatus;
import com.sorts.srms.backend.domain.enums.TicketStatus;
import com.sorts.srms.backend.domain.model.Asset;
import com.sorts.srms.backend.domain.model.Project;
import com.sorts.srms.backend.domain.model.ServiceTicket;
import com.sorts.srms.backend.dto.AnalyticsMetricDTO;
import com.sorts.srms.backend.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AnalyticsService {

    private final CompanyRepository companyRepository;
    private final EmployeeRepository employeeRepository;
    private final ProjectRepository projectRepository;
    private final ServiceTicketRepository ticketRepository;
    private final AssetRepository assetRepository;

    public AnalyticsService(CompanyRepository companyRepository, EmployeeRepository employeeRepository,
                            ProjectRepository projectRepository, ServiceTicketRepository ticketRepository,
                            AssetRepository assetRepository) {
        this.companyRepository = companyRepository;
        this.employeeRepository = employeeRepository;
        this.projectRepository = projectRepository;
        this.ticketRepository = ticketRepository;
        this.assetRepository = assetRepository;
    }

    @Transactional(readOnly = true)
    public AnalyticsMetricDTO getMetricsForCompany(String companyId) {
        AnalyticsMetricDTO metrics = new AnalyticsMetricDTO();

        metrics.setTotalCompanies(companyRepository.count());
        metrics.setTotalEmployees(employeeRepository.findByCompanyId(companyId).size());

        List<Project> projects = projectRepository.findByCompanyId(companyId);
        metrics.setTotalProjects(projects.size());
        metrics.setActiveProjects(projects.stream().filter(p -> p.getStatus() == ProjectStatus.IN_PROGRESS).count());

        Map<String, Long> projectsByStatus = new HashMap<>();
        for (ProjectStatus status : ProjectStatus.values()) {
            projectsByStatus.put(status.name(), projects.stream().filter(p -> p.getStatus() == status).count());
        }
        metrics.setProjectsByStatus(projectsByStatus);

        List<ServiceTicket> tickets = ticketRepository.findByCompanyId(companyId);
        metrics.setTotalTickets(tickets.size());
        metrics.setOpenTickets(tickets.stream().filter(t -> t.getStatus() == TicketStatus.OPEN || t.getStatus() == TicketStatus.IN_PROGRESS).count());
        metrics.setUrgentTickets(tickets.stream().filter(t -> t.getPriority() == Priority.CRITICAL || t.getPriority() == Priority.HIGH).count());

        Map<String, Long> ticketsByPriority = new HashMap<>();
        for (Priority p : Priority.values()) {
            ticketsByPriority.put(p.name(), tickets.stream().filter(t -> t.getPriority() == p).count());
        }
        metrics.setTicketsByPriority(ticketsByPriority);

        List<Asset> assets = assetRepository.findByCompanyId(companyId);
        metrics.setTotalAssets(assets.size());
        double assetSum = assets.stream()
                .map(Asset::getPurchaseCost)
                .filter(c -> c != null)
                .mapToDouble(BigDecimal::doubleValue)
                .sum();
        metrics.setTotalAssetValue(assetSum);

        Map<String, Long> assetsByCategory = new HashMap<>();
        for (Asset a : assets) {
            assetsByCategory.put(a.getCategory(), assetsByCategory.getOrDefault(a.getCategory(), 0L) + 1);
        }
        metrics.setAssetsByCategory(assetsByCategory);

        long completedProjects = projects.stream().filter(p -> p.getStatus() == ProjectStatus.COMPLETED).count();
        metrics.setProjectCompletionRate(projects.size() > 0 ? (double) completedProjects / projects.size() * 100.0 : 0.0);

        long resolvedTickets = tickets.stream().filter(t -> t.getStatus() == TicketStatus.RESOLVED || t.getStatus() == TicketStatus.CLOSED).count();
        metrics.setTicketResolutionSlaPercentage(tickets.size() > 0 ? (double) resolvedTickets / tickets.size() * 100.0 : 92.5);

        return metrics;
    }
}
