package app.clinic.application.dto.visit;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Data Transfer Object for creating new patient visits.
 * Contains validation annotations for input data.
 */
public class CreatePatientVisitDTO {

    @NotBlank(message = "Patient cedula is required")
    @Size(max = 20, message = "Patient cedula must not exceed 20 characters")
    private String patientCedula;

    @NotBlank(message = "Visit date time is required")
    private String visitDateTime;

    @Valid
    private VitalSignsDTO vitalSigns;

    @Valid
    private NurseRecordDTO nurseRecord;

    // Default constructor
    public CreatePatientVisitDTO() {}

    // Constructor with parameters
    public CreatePatientVisitDTO(String patientCedula, String visitDateTime,
                               VitalSignsDTO vitalSigns, NurseRecordDTO nurseRecord) {
        this.patientCedula = patientCedula;
        this.visitDateTime = visitDateTime;
        this.vitalSigns = vitalSigns;
        this.nurseRecord = nurseRecord;
    }

    // Getters and Setters
    public String getPatientCedula() {
        return patientCedula;
    }

    public void setPatientCedula(String patientCedula) {
        this.patientCedula = patientCedula;
    }

    public String getVisitDateTime() {
        return visitDateTime;
    }

    public void setVisitDateTime(String visitDateTime) {
        this.visitDateTime = visitDateTime;
    }

    public VitalSignsDTO getVitalSigns() {
        return vitalSigns;
    }

    public void setVitalSigns(VitalSignsDTO vitalSigns) {
        this.vitalSigns = vitalSigns;
    }

    public NurseRecordDTO getNurseRecord() {
        return nurseRecord;
    }

    public void setNurseRecord(NurseRecordDTO nurseRecord) {
        this.nurseRecord = nurseRecord;
    }

    @Override
    public String toString() {
        return String.format("CreatePatientVisitDTO{patientCedula='%s', visitDateTime='%s'}",
                           patientCedula, visitDateTime);
    }
}