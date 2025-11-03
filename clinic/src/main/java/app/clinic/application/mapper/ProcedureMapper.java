package app.clinic.application.mapper;

import app.clinic.domain.model.entities.Procedure;
import app.clinic.infrastructure.dto.ProcedureDTO;

public class ProcedureMapper {

    public static ProcedureDTO toDTO(Procedure procedure) {
        ProcedureDTO dto = new ProcedureDTO();
        dto.setId(procedure.getId().getValue());
        dto.setName(procedure.getName());
        dto.setCost(procedure.getCost());
        dto.setRequiresSpecialist(procedure.isRequiresSpecialist());
        dto.setSpecialistType(procedure.getSpecialistType().getValue());
        return dto;
    }
}