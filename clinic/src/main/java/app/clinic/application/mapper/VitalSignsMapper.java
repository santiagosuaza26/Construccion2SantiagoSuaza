package app.clinic.application.mapper;

import app.clinic.domain.model.entities.VitalSigns;
import app.clinic.infrastructure.dto.VitalSignsDTO;

public class VitalSignsMapper {

    public static VitalSignsDTO toDTO(VitalSigns vitalSigns) {
        VitalSignsDTO dto = new VitalSignsDTO();
        dto.setPatientId(vitalSigns.getPatientIdentificationNumber());
        dto.setBloodPressure(vitalSigns.getBloodPressure());
        dto.setTemperature(vitalSigns.getTemperature());
        dto.setPulse(vitalSigns.getPulse());
        dto.setOxygenLevel(vitalSigns.getOxygenLevel());
        dto.setRecordedAt(vitalSigns.getDateTime().toString());
        return dto;
    }
}