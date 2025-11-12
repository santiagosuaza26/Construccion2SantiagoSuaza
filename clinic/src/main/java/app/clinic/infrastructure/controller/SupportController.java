package app.clinic.infrastructure.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import app.clinic.application.mapper.SupportTicketMapper;
import app.clinic.application.usecase.ProvideTechnicalSupportUseCase;
import app.clinic.domain.model.DomainException;
import app.clinic.domain.model.entities.SupportTicket;
import app.clinic.domain.model.valueobject.Role;
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

    private Role getCurrentUserRole() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getAuthorities() != null) {
            String roleString = authentication.getAuthorities().iterator().next().getAuthority();
            if (roleString.startsWith("ROLE_")) {
                roleString = roleString.substring(5); // Remove "ROLE_" prefix
            }
            return Role.valueOf(roleString);
        }
        return null;
    }

    @PostMapping("/tickets")
    public ResponseEntity<SupportTicketDTO> createSupportTicket(@RequestBody CreateSupportTicketRequest request) {
        SupportTicket supportTicket = provideTechnicalSupportUseCase.execute(request.userId, request.issueDescription);

        SupportTicketDTO dto = SupportTicketMapper.toDTO(supportTicket);

        return ResponseEntity.ok(dto);
    }

    @GetMapping("/tickets/{id}")
    public ResponseEntity<SupportTicketDTO> getSupportTicket(@PathVariable String id) {
        return technicalSupportService.getSupportTicketById(new app.clinic.domain.model.valueobject.SupportTicketId(id))
            .map(ticket -> ResponseEntity.ok(SupportTicketMapper.toDTO(ticket)))
            .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/tickets/user/{userId}")
    public ResponseEntity<List<SupportTicketDTO>> getSupportTicketsByUser(@PathVariable String userId) {
        List<SupportTicket> tickets = technicalSupportService.getSupportTicketsByUserId(userId);
        List<SupportTicketDTO> dtos = tickets.stream()
            .map(SupportTicketMapper::toDTO)
            .collect(Collectors.toList());

        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/tickets")
    public ResponseEntity<List<SupportTicketDTO>> getAllSupportTickets() {
        List<SupportTicket> tickets = technicalSupportService.getAllSupportTickets();
        List<SupportTicketDTO> dtos = tickets.stream()
            .map(SupportTicketMapper::toDTO)
            .collect(Collectors.toList());

        return ResponseEntity.ok(dtos);
    }

    @PutMapping("/tickets/{id}/assign")
    public ResponseEntity<Void> assignSupportTicket(@PathVariable String id, @RequestBody AssignTicketRequest request) {
        Role currentRole = getCurrentUserRole();
        if (currentRole == null) {
            throw new DomainException("Usuario no autenticado");
        }
        technicalSupportService.assignSupportTicket(new app.clinic.domain.model.valueobject.SupportTicketId(id), request.assignedTo, currentRole);
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