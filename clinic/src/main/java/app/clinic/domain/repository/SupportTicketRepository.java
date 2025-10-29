package app.clinic.domain.repository;

import java.util.List;
import java.util.Optional;

import app.clinic.domain.model.entities.SupportTicket;
import app.clinic.domain.model.valueobject.SupportTicketId;

public interface SupportTicketRepository {
    void save(SupportTicket supportTicket);
    Optional<SupportTicket> findById(SupportTicketId id);
    List<SupportTicket> findByUserId(String userId);
    List<SupportTicket> findAll();
    boolean existsById(SupportTicketId id);
}