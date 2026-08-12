package com.sorts.srms.backend.controller;

import com.sorts.srms.backend.dto.ProjectDTO;
import com.sorts.srms.backend.dto.ProjectTaskDTO;
import com.sorts.srms.backend.service.ProjectService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/projects")
public class ProjectController {

    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @GetMapping("/company/{companyId}")
    @PreAuthorize("hasAuthority('PROJECT_VIEW') or hasRole('SUPER_ADMIN')")
    public ResponseEntity<List<ProjectDTO>> getProjectsByCompany(@PathVariable String companyId) {
        return ResponseEntity.ok(projectService.getProjectsByCompany(companyId));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('PROJECT_VIEW') or hasRole('SUPER_ADMIN')")
    public ResponseEntity<ProjectDTO> getProjectById(@PathVariable String id) {
        return ResponseEntity.ok(projectService.getProjectById(id));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('PROJECT_CREATE') or hasRole('SUPER_ADMIN')")
    public ResponseEntity<ProjectDTO> createProject(@Valid @RequestBody ProjectDTO dto) {
        ProjectDTO created = projectService.createProject(dto);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @GetMapping("/{projectId}/tasks")
    @PreAuthorize("hasAuthority('PROJECT_VIEW') or hasRole('SUPER_ADMIN')")
    public ResponseEntity<List<ProjectTaskDTO>> getTasksByProject(@PathVariable String projectId) {
        return ResponseEntity.ok(projectService.getTasksByProject(projectId));
    }

    @PostMapping("/tasks")
    @PreAuthorize("hasAuthority('PROJECT_UPDATE') or hasRole('SUPER_ADMIN')")
    public ResponseEntity<ProjectTaskDTO> createTask(@Valid @RequestBody ProjectTaskDTO dto) {
        ProjectTaskDTO created = projectService.createTask(dto);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }
}
