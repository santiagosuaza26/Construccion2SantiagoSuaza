package app.clinic.application.mapper;

import app.clinic.domain.model.entities.Medication;
import app.clinic.infrastructure.dto.MedicationDTO;

public class MedicationMapper {

    public static MedicationDTO toDTO(Medication medication) {
        MedicationDTO dto = new MedicationDTO();
        dto.setId(medication.getId().getValue());
        dto.setName(medication.getName());
        dto.setCost(medication.getCost());
        dto.setRequiresSpecialist(medication.isRequiresSpecialist());
        dto.setSpecialistType(medication.getSpecialistType().getValue());
        return dto;
    }
}