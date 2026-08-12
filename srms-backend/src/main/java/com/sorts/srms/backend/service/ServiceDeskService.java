package com.sorts.srms.backend.service;

import com.sorts.srms.backend.domain.enums.Priority;
import com.sorts.srms.backend.domain.enums.TicketStatus;
import com.sorts.srms.backend.domain.model.Company;
import com.sorts.srms.backend.domain.model.Employee;
import com.sorts.srms.backend.domain.model.ServiceTicket;
import com.sorts.srms.backend.dto.TicketDTO;
import com.sorts.srms.backend.exception.ResourceNotFoundException;
import com.sorts.srms.backend.repository.CompanyRepository;
import com.sorts.srms.backend.repository.EmployeeRepository;
import com.sorts.srms.backend.repository.ServiceTicketRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ServiceDeskService {

    private final ServiceTicketRepository ticketRepository;
    private final CompanyRepository companyRepository;
    private final EmployeeRepository employeeRepository;

    public ServiceDeskService(ServiceTicketRepository ticketRepository, CompanyRepository companyRepository, EmployeeRepository employeeRepository) {
        this.ticketRepository = ticketRepository;
        this.companyRepository = companyRepository;
        this.employeeRepository = employeeRepository;
    }

    @Transactional(readOnly = true)
    public List<TicketDTO> getTicketsByCompany(String companyId) {
        return ticketRepository.findByCompanyId(companyId).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public TicketDTO getTicketById(String id) {
        ServiceTicket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ticket not found: " + id));
        return mapToDTO(ticket);
    }

    @Transactional
    public TicketDTO createTicket(TicketDTO dto, String requesterEmployeeId) {
        Company company = companyRepository.findById(dto.getCompanyId())
                .orElseThrow(() -> new ResourceNotFoundException("Company not found: " + dto.getCompanyId()));

        Employee requester = employeeRepository.findById(requesterEmployeeId)
                .orElseGet(() -> employeeRepository.findByCompanyId(company.getId()).stream().findFirst()
                        .orElseThrow(() -> new ResourceNotFoundException("Requester employee not found")));

        ServiceTicket ticket = new ServiceTicket();
        ticket.setId(UUID.randomUUID().toString());
        ticket.setCompany(company);
        ticket.setTicketNumber("TKT-" + (100000 + new Random().nextInt(900000)));
        ticket.setTitle(dto.getTitle());
        ticket.setDescription(dto.getDescription());
        ticket.setRequester(requester);
        ticket.setPriority(dto.getPriority() != null ? Priority.valueOf(dto.getPriority()) : Priority.MEDIUM);
        ticket.setStatus(TicketStatus.OPEN);
        ticket.setCategory(dto.getCategory() != null ? dto.getCategory() : "GENERAL");

        if (dto.getAssigneeId() != null && !dto.getAssigneeId().isBlank()) {
            employeeRepository.findById(dto.getAssigneeId()).ifPresent(ticket::setAssignee);
        }

        return mapToDTO(ticketRepository.save(ticket));
    }

    @Transactional
    public TicketDTO updateTicketStatus(String id, String newStatus, String assigneeId) {
        ServiceTicket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ticket not found: " + id));

        ticket.setStatus(TicketStatus.valueOf(newStatus));
        if (assigneeId != null && !assigneeId.isBlank()) {
            employeeRepository.findById(assigneeId).ifPresent(ticket::setAssignee);
        }

        return mapToDTO(ticketRepository.save(ticket));
    }

    private TicketDTO mapToDTO(ServiceTicket t) {
        TicketDTO dto = new TicketDTO();
        dto.setId(t.getId());
        dto.setCompanyId(t.getCompany().getId());
        dto.setTicketNumber(t.getTicketNumber());
        dto.setTitle(t.getTitle());
        dto.setDescription(t.getDescription());
        dto.setRequesterId(t.getRequester().getId());
        dto.setRequesterName(t.getRequester().getUser().getFirstName() + " " + t.getRequester().getUser().getLastName());
        if (t.getAssignee() != null) {
            dto.setAssigneeId(t.getAssignee().getId());
            dto.setAssigneeName(t.getAssignee().getUser().getFirstName() + " " + t.getAssignee().getUser().getLastName());
        }
        dto.setPriority(t.getPriority().name());
        dto.setStatus(t.getStatus().name());
        dto.setCategory(t.getCategory());
        dto.setCreatedAt(t.getCreatedAt());
        dto.setUpdatedAt(t.getUpdatedAt());
        return dto;
    }
}
