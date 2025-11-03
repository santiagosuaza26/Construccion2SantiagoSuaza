package app.clinic.application.mapper;

import app.clinic.domain.model.entities.DiagnosticAid;
import app.clinic.infrastructure.dto.DiagnosticAidDTO;

public class DiagnosticAidMapper {

    public static DiagnosticAidDTO toDTO(DiagnosticAid diagnosticAid) {
        DiagnosticAidDTO dto = new DiagnosticAidDTO();
        dto.setId(diagnosticAid.getId().getValue());
        dto.setName(diagnosticAid.getName());
        dto.setCost(diagnosticAid.getCost());
        dto.setRequiresSpecialist(diagnosticAid.isRequiresSpecialist());
        dto.setSpecialistType(diagnosticAid.getSpecialistType().getValue());
        return dto;
    }
}