package app.clinic.application.mapper;

import app.clinic.domain.model.entities.Patient;
import app.clinic.infrastructure.dto.PatientDTO;

public class PatientMapper {

    public static PatientDTO toDTO(Patient patient) {
        PatientDTO dto = new PatientDTO();
        dto.setIdentificationNumber(patient.getIdentificationNumber().getValue());
        dto.setFullName(patient.getFullName());
        dto.setDateOfBirth(patient.getDateOfBirth().toString());
        dto.setGender(patient.getGender().toString());
        dto.setAddress(patient.getAddress().getValue());
        dto.setPhone(patient.getPhone().getValue());
        dto.setEmail(patient.getEmail().getValue());
        dto.setEmergencyName(patient.getEmergencyContact().getName());
        dto.setEmergencyRelation(patient.getEmergencyContact().getRelation());
        dto.setEmergencyPhone(patient.getEmergencyContact().getPhone().getValue());
        dto.setCompanyName(patient.getInsurance().getCompanyName());
        dto.setPolicyNumber(patient.getInsurance().getPolicyNumber());
        dto.setInsuranceActive(patient.getInsurance().isActive());
        dto.setValidityDate(patient.getInsurance().getValidityDate().toString());
        return dto;
    }
}