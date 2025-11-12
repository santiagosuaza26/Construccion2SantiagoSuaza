package app.clinic.domain.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import app.clinic.domain.model.entities.SupportTicket;
import app.clinic.domain.model.valueobject.SupportTicketId;
import app.clinic.domain.repository.SupportTicketRepository;

public class TechnicalSupportService {
    private final SupportTicketRepository supportTicketRepository;
    private final RoleBasedAccessService roleBasedAccessService;

    public TechnicalSupportService(SupportTicketRepository supportTicketRepository, RoleBasedAccessService roleBasedAccessService) {
        this.supportTicketRepository = supportTicketRepository;
        this.roleBasedAccessService = roleBasedAccessService;
    }

    public SupportTicket createSupportTicket(String userId, String issueDescription) {
        SupportTicketId id = generateSupportTicketId();
        SupportTicket supportTicket = new SupportTicket(id, userId, issueDescription, LocalDateTime.now());
        supportTicketRepository.save(supportTicket);
        return supportTicket;
    }

    public Optional<SupportTicket> getSupportTicketById(SupportTicketId id) {
        return supportTicketRepository.findById(id);
    }

    public List<SupportTicket> getSupportTicketsByUserId(String userId) {
        return supportTicketRepository.findByUserId(userId);
    }

    public List<SupportTicket> getAllSupportTickets() {
        return supportTicketRepository.findAll();
    }

    public void assignSupportTicket(SupportTicketId id, String assignedTo, app.clinic.domain.model.valueobject.Role currentUserRole) {
        // Validar que solo administradores pueden asignar tickets
        roleBasedAccessService.checkAccess(currentUserRole, "support_ticket");

        Optional<SupportTicket> ticketOpt = supportTicketRepository.findById(id);
        if (ticketOpt.isPresent()) {
            SupportTicket ticket = ticketOpt.get();
            ticket.assignTo(assignedTo);
            supportTicketRepository.save(ticket);
        }
    }

    public void closeSupportTicket(SupportTicketId id) {
        Optional<SupportTicket> ticketOpt = supportTicketRepository.findById(id);
        if (ticketOpt.isPresent()) {
            SupportTicket ticket = ticketOpt.get();
            ticket.close();
            supportTicketRepository.save(ticket);
        }
    }

    private SupportTicketId generateSupportTicketId() {
        // Simple ID generation - in a real system, this would be more sophisticated
        String id = String.valueOf(System.currentTimeMillis());
        return new SupportTicketId(id);
    }
}