package com.sorts.srms.backend.service;

import com.sorts.srms.backend.domain.enums.Priority;
import com.sorts.srms.backend.domain.enums.ProjectStatus;
import com.sorts.srms.backend.domain.model.Company;
import com.sorts.srms.backend.domain.model.Employee;
import com.sorts.srms.backend.domain.model.Project;
import com.sorts.srms.backend.domain.model.ProjectTask;
import com.sorts.srms.backend.dto.ProjectDTO;
import com.sorts.srms.backend.dto.ProjectTaskDTO;
import com.sorts.srms.backend.exception.BusinessValidationException;
import com.sorts.srms.backend.exception.ResourceNotFoundException;
import com.sorts.srms.backend.repository.CompanyRepository;
import com.sorts.srms.backend.repository.EmployeeRepository;
import com.sorts.srms.backend.repository.ProjectRepository;
import com.sorts.srms.backend.repository.ProjectTaskRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final ProjectTaskRepository taskRepository;
    private final CompanyRepository companyRepository;
    private final EmployeeRepository employeeRepository;

    public ProjectService(ProjectRepository projectRepository, ProjectTaskRepository taskRepository,
                          CompanyRepository companyRepository, EmployeeRepository employeeRepository) {
        this.projectRepository = projectRepository;
        this.taskRepository = taskRepository;
        this.companyRepository = companyRepository;
        this.employeeRepository = employeeRepository;
    }

    @Transactional(readOnly = true)
    public List<ProjectDTO> getProjectsByCompany(String companyId) {
        return projectRepository.findByCompanyId(companyId).stream()
                .map(this::mapProjectToDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ProjectDTO getProjectById(String id) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found: " + id));
        return mapProjectToDTO(project);
    }

    @Transactional
    public ProjectDTO createProject(ProjectDTO dto) {
        if (projectRepository.existsByCode(dto.getCode())) {
            throw new BusinessValidationException("Project code already exists: " + dto.getCode());
        }

        Company company = companyRepository.findById(dto.getCompanyId())
                .orElseThrow(() -> new ResourceNotFoundException("Company not found: " + dto.getCompanyId()));

        Project project = new Project();
        project.setId(UUID.randomUUID().toString());
        project.setCompany(company);
        project.setCode(dto.getCode());
        project.setName(dto.getName());
        project.setDescription(dto.getDescription());
        project.setStatus(dto.getStatus() != null ? ProjectStatus.valueOf(dto.getStatus()) : ProjectStatus.PLANNING);
        project.setPriority(dto.getPriority() != null ? Priority.valueOf(dto.getPriority()) : Priority.MEDIUM);
        project.setBudget(dto.getBudget() != null ? dto.getBudget() : BigDecimal.ZERO);
        project.setStartDate(dto.getStartDate());
        project.setEndDate(dto.getEndDate());

        if (dto.getManagerId() != null && !dto.getManagerId().isBlank()) {
            employeeRepository.findById(dto.getManagerId()).ifPresent(project::setManager);
        }

        return mapProjectToDTO(projectRepository.save(project));
    }

    @Transactional(readOnly = true)
    public List<ProjectTaskDTO> getTasksByProject(String projectId) {
        return taskRepository.findByProjectId(projectId).stream()
                .map(this::mapTaskToDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public ProjectTaskDTO createTask(ProjectTaskDTO dto) {
        Project project = projectRepository.findById(dto.getProjectId())
                .orElseThrow(() -> new ResourceNotFoundException("Project not found: " + dto.getProjectId()));

        ProjectTask task = new ProjectTask();
        task.setId(UUID.randomUUID().toString());
        task.setProject(project);
        task.setTitle(dto.getTitle());
        task.setDescription(dto.getDescription());
        task.setPriority(dto.getPriority() != null ? Priority.valueOf(dto.getPriority()) : Priority.MEDIUM);
        task.setStatus(dto.getStatus() != null ? dto.getStatus() : "TODO");
        task.setEstimatedHours(dto.getEstimatedHours() != null ? dto.getEstimatedHours() : BigDecimal.ZERO);
        task.setLoggedHours(dto.getLoggedHours() != null ? dto.getLoggedHours() : BigDecimal.ZERO);
        task.setDueDate(dto.getDueDate());

        if (dto.getAssigneeId() != null && !dto.getAssigneeId().isBlank()) {
            employeeRepository.findById(dto.getAssigneeId()).ifPresent(task::setAssignee);
        }

        return mapTaskToDTO(taskRepository.save(task));
    }

    private ProjectDTO mapProjectToDTO(Project p) {
        ProjectDTO dto = new ProjectDTO();
        dto.setId(p.getId());
        dto.setCompanyId(p.getCompany().getId());
        dto.setCompanyName(p.getCompany().getName());
        dto.setCode(p.getCode());
        dto.setName(p.getName());
        dto.setDescription(p.getDescription());
        if (p.getManager() != null) {
            dto.setManagerId(p.getManager().getId());
            dto.setManagerName(p.getManager().getUser().getFirstName() + " " + p.getManager().getUser().getLastName());
        }
        dto.setStatus(p.getStatus().name());
        dto.setPriority(p.getPriority().name());
        dto.setBudget(p.getBudget());
        dto.setStartDate(p.getStartDate());
        dto.setEndDate(p.getEndDate());

        List<ProjectTask> tasks = taskRepository.findByProjectId(p.getId());
        dto.setTaskCount(tasks.size());
        dto.setCompletedTaskCount((int) tasks.stream().filter(t -> "DONE".equalsIgnoreCase(t.getStatus())).count());

        return dto;
    }

    private ProjectTaskDTO mapTaskToDTO(ProjectTask t) {
        ProjectTaskDTO dto = new ProjectTaskDTO();
        dto.setId(t.getId());
        dto.setProjectId(t.getProject().getId());
        dto.setProjectName(t.getProject().getName());
        dto.setTitle(t.getTitle());
        dto.setDescription(t.getDescription());
        if (t.getAssignee() != null) {
            dto.setAssigneeId(t.getAssignee().getId());
            dto.setAssigneeName(t.getAssignee().getUser().getFirstName() + " " + t.getAssignee().getUser().getLastName());
        }
        dto.setPriority(t.getPriority().name());
        dto.setStatus(t.getStatus());
        dto.setEstimatedHours(t.getEstimatedHours());
        dto.setLoggedHours(t.getLoggedHours());
        dto.setDueDate(t.getDueDate());
        return dto;
    }
}
