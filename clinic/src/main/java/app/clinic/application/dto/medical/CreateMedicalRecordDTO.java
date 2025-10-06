package app.clinic.application.dto.medical;

import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Data Transfer Object for creating new medical records.
 * Contains validation annotations for input data.
 */
public class CreateMedicalRecordDTO {

    @NotBlank(message = "Patient cedula is required")
    @Size(max = 20, message = "Patient cedula must not exceed 20 characters")
    private String patientCedula;

    @NotBlank(message = "Doctor cedula is required")
    @Size(max = 10, message = "Doctor cedula must not exceed 10 characters")
    private String doctorCedula;

    @NotBlank(message = "Consultation reason is required")
    @Size(max = 200, message = "Consultation reason must not exceed 200 characters")
    private String consultationReason;

    @NotBlank(message = "Symptoms are required")
    @Size(max = 500, message = "Symptoms must not exceed 500 characters")
    private String symptoms;

    @NotBlank(message = "Diagnosis is required")
    @Size(max = 300, message = "Diagnosis must not exceed 300 characters")
    private String diagnosis;

    private List<MedicationRecordDTO> medications;
    private List<ProcedureRecordDTO> procedures;
    private List<DiagnosticAidRecordDTO> diagnosticAids;

    // Default constructor
    public CreateMedicalRecordDTO() {}

    // Constructor with parameters
    public CreateMedicalRecordDTO(String patientCedula, String doctorCedula,
                                  String consultationReason, String symptoms, String diagnosis) {
        this.patientCedula = patientCedula;
        this.doctorCedula = doctorCedula;
        this.consultationReason = consultationReason;
        this.symptoms = symptoms;
        this.diagnosis = diagnosis;
    }

    // Constructor with all parameters
    public CreateMedicalRecordDTO(String patientCedula, String doctorCedula,
                                  String consultationReason, String symptoms, String diagnosis,
                                  List<MedicationRecordDTO> medications,
                                  List<ProcedureRecordDTO> procedures,
                                  List<DiagnosticAidRecordDTO> diagnosticAids) {
        this.patientCedula = patientCedula;
        this.doctorCedula = doctorCedula;
        this.consultationReason = consultationReason;
        this.symptoms = symptoms;
        this.diagnosis = diagnosis;
        this.medications = medications;
        this.procedures = procedures;
        this.diagnosticAids = diagnosticAids;
    }

    // Getters and Setters
    public String getPatientCedula() {
        return patientCedula;
    }

    public void setPatientCedula(String patientCedula) {
        this.patientCedula = patientCedula;
    }

    public String getDoctorCedula() {
        return doctorCedula;
    }

    public void setDoctorCedula(String doctorCedula) {
        this.doctorCedula = doctorCedula;
    }

    public String getConsultationReason() {
        return consultationReason;
    }

    public void setConsultationReason(String consultationReason) {
        this.consultationReason = consultationReason;
    }

    public String getSymptoms() {
        return symptoms;
    }

    public void setSymptoms(String symptoms) {
        this.symptoms = symptoms;
    }

    public String getDiagnosis() {
        return diagnosis;
    }

    public void setDiagnosis(String diagnosis) {
        this.diagnosis = diagnosis;
    }

    public List<MedicationRecordDTO> getMedications() {
        return medications;
    }

    public void setMedications(List<MedicationRecordDTO> medications) {
        this.medications = medications;
    }

    public List<ProcedureRecordDTO> getProcedures() {
        return procedures;
    }

    public void setProcedures(List<ProcedureRecordDTO> procedures) {
        this.procedures = procedures;
    }

    public List<DiagnosticAidRecordDTO> getDiagnosticAids() {
        return diagnosticAids;
    }

    public void setDiagnosticAids(List<DiagnosticAidRecordDTO> diagnosticAids) {
        this.diagnosticAids = diagnosticAids;
    }

    @Override
    public String toString() {
        return String.format("CreateMedicalRecordDTO{patientCedula='%s', doctorCedula='%s', consultationReason='%s', medications=%d, procedures=%d, diagnosticAids=%d}",
                           patientCedula, doctorCedula, consultationReason,
                           medications != null ? medications.size() : 0,
                           procedures != null ? procedures.size() : 0,
                           diagnosticAids != null ? diagnosticAids.size() : 0);
    }
}