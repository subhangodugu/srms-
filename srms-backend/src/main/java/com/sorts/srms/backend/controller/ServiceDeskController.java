package com.sorts.srms.backend.controller;

import com.sorts.srms.backend.dto.TicketDTO;
import com.sorts.srms.backend.security.CustomUserDetails;
import com.sorts.srms.backend.service.ServiceDeskService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/tickets")
public class ServiceDeskController {

    private final ServiceDeskService serviceDeskService;

    public ServiceDeskController(ServiceDeskService serviceDeskService) {
        this.serviceDeskService = serviceDeskService;
    }

    @GetMapping("/company/{companyId}")
    @PreAuthorize("hasAuthority('TICKET_VIEW') or hasRole('SUPER_ADMIN')")
    public ResponseEntity<List<TicketDTO>> getTicketsByCompany(@PathVariable String companyId) {
        return ResponseEntity.ok(serviceDeskService.getTicketsByCompany(companyId));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('TICKET_VIEW') or hasRole('SUPER_ADMIN')")
    public ResponseEntity<TicketDTO> getTicketById(@PathVariable String id) {
        return ResponseEntity.ok(serviceDeskService.getTicketById(id));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('TICKET_CREATE') or hasRole('SUPER_ADMIN')")
    public ResponseEntity<TicketDTO> createTicket(
            @Valid @RequestBody TicketDTO dto,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        String requesterId = userDetails != null ? userDetails.getUser().getId() : dto.getRequesterId();
        TicketDTO created = serviceDeskService.createTicket(dto, requesterId);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @PutMapping("/{id}/status")
    @PreAuthorize("hasAuthority('TICKET_UPDATE') or hasRole('SUPER_ADMIN')")
    public ResponseEntity<TicketDTO> updateTicketStatus(
            @PathVariable String id,
            @RequestParam String status,
            @RequestParam(required = false) String assigneeId) {
        return ResponseEntity.ok(serviceDeskService.updateTicketStatus(id, status, assigneeId));
    }
}
