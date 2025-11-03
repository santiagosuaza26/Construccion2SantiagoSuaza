package app.clinic.application.mapper;

import app.clinic.domain.model.entities.SupportTicket;
import app.clinic.infrastructure.dto.SupportTicketDTO;

public class SupportTicketMapper {

    public static SupportTicketDTO toDTO(SupportTicket supportTicket) {
        SupportTicketDTO dto = new SupportTicketDTO();
        dto.setId(supportTicket.getId().getValue());
        dto.setUserId(supportTicket.getUserId());
        dto.setIssueDescription(supportTicket.getIssueDescription());
        dto.setCreatedAt(supportTicket.getCreatedAt());
        dto.setStatus(supportTicket.getStatus());
        dto.setAssignedTo(supportTicket.getAssignedTo());
        dto.setUpdatedAt(supportTicket.getUpdatedAt());
        return dto;
    }
}