package com.sorts.srms.backend.repository;

import com.sorts.srms.backend.domain.model.ServiceTicket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ServiceTicketRepository extends JpaRepository<ServiceTicket, String> {
    List<ServiceTicket> findByCompanyId(String companyId);
    List<ServiceTicket> findByRequesterId(String requesterId);
    List<ServiceTicket> findByAssigneeId(String assigneeId);
    Optional<ServiceTicket> findByTicketNumber(String ticketNumber);
}
