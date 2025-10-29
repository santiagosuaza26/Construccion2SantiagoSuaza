package app.clinic.infrastructure.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import app.clinic.application.usecase.ProvideTechnicalSupportUseCase;
import app.clinic.domain.model.entities.SupportTicket;
import app.clinic.domain.service.TechnicalSupportService;
import app.clinic.infrastructure.dto.SupportTicketDTO;

@RestController
@RequestMapping("/api/support")
public class SupportController {
    private final ProvideTechnicalSupportUseCase provideTechnicalSupportUseCase;
    private final TechnicalSupportService technicalSupportService;

    public SupportController(ProvideTechnicalSupportUseCase provideTechnicalSupportUseCase,
                           TechnicalSupportService technicalSupportService) {
        this.provideTechnicalSupportUseCase = provideTechnicalSupportUseCase;
        this.technicalSupportService = technicalSupportService;
    }

    @PostMapping("/tickets")
    public ResponseEntity<SupportTicketDTO> createSupportTicket(@RequestBody CreateSupportTicketRequest request) {
        SupportTicket supportTicket = provideTechnicalSupportUseCase.execute(request.userId, request.issueDescription);

        SupportTicketDTO dto = new SupportTicketDTO(
            supportTicket.getId().getValue(),
            supportTicket.getUserId(),
            supportTicket.getIssueDescription(),
            supportTicket.getCreatedAt(),
            supportTicket.getStatus(),
            supportTicket.getAssignedTo(),
            supportTicket.getUpdatedAt()
        );

        return ResponseEntity.ok(dto);
    }

    @GetMapping("/tickets/{id}")
    public ResponseEntity<SupportTicketDTO> getSupportTicket(@PathVariable String id) {
        return technicalSupportService.getSupportTicketById(new app.clinic.domain.model.valueobject.SupportTicketId(id))
            .map(ticket -> {
                SupportTicketDTO dto = new SupportTicketDTO(
                    ticket.getId().getValue(),
                    ticket.getUserId(),
                    ticket.getIssueDescription(),
                    ticket.getCreatedAt(),
                    ticket.getStatus(),
                    ticket.getAssignedTo(),
                    ticket.getUpdatedAt()
                );
                return ResponseEntity.ok(dto);
            })
            .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/tickets/user/{userId}")
    public ResponseEntity<List<SupportTicketDTO>> getSupportTicketsByUser(@PathVariable String userId) {
        List<SupportTicket> tickets = technicalSupportService.getSupportTicketsByUserId(userId);
        List<SupportTicketDTO> dtos = tickets.stream()
            .map(ticket -> new SupportTicketDTO(
                ticket.getId().getValue(),
                ticket.getUserId(),
                ticket.getIssueDescription(),
                ticket.getCreatedAt(),
                ticket.getStatus(),
                ticket.getAssignedTo(),
                ticket.getUpdatedAt()
            ))
            .collect(Collectors.toList());

        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/tickets")
    public ResponseEntity<List<SupportTicketDTO>> getAllSupportTickets() {
        List<SupportTicket> tickets = technicalSupportService.getAllSupportTickets();
        List<SupportTicketDTO> dtos = tickets.stream()
            .map(ticket -> new SupportTicketDTO(
                ticket.getId().getValue(),
                ticket.getUserId(),
                ticket.getIssueDescription(),
                ticket.getCreatedAt(),
                ticket.getStatus(),
                ticket.getAssignedTo(),
                ticket.getUpdatedAt()
            ))
            .collect(Collectors.toList());

        return ResponseEntity.ok(dtos);
    }

    @PutMapping("/tickets/{id}/assign")
    public ResponseEntity<Void> assignSupportTicket(@PathVariable String id, @RequestBody AssignTicketRequest request) {
        technicalSupportService.assignSupportTicket(new app.clinic.domain.model.valueobject.SupportTicketId(id), request.assignedTo);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/tickets/{id}/close")
    public ResponseEntity<Void> closeSupportTicket(@PathVariable String id) {
        technicalSupportService.closeSupportTicket(new app.clinic.domain.model.valueobject.SupportTicketId(id));
        return ResponseEntity.ok().build();
    }

    public static class CreateSupportTicketRequest {
        public String userId;
        public String issueDescription;
    }

    public static class AssignTicketRequest {
        public String assignedTo;
    }
}