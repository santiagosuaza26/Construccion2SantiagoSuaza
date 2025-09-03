package app.application.dto;

import app.domain.model.Patient;

public class PatientResponse {
    public String idCard;
    public String fullName;
    public String gender;
    public String email;
    public String phone;

    public static PatientResponse fromDomain(Patient patient) {
        PatientResponse dto = new PatientResponse();
        dto.idCard = patient.getIdCard();
        dto.fullName = patient.getFullName();
        dto.gender = patient.getGender();
        dto.email = patient.getEmail();
        dto.phone = patient.getPhone();
        return dto;
    }
}