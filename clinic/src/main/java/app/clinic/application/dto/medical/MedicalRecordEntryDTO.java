package app.clinic.application.dto.medical;

/**
 * Data Transfer Object for a single medical record entry.
 * Used for API responses containing individual medical record data.
 */
public class MedicalRecordEntryDTO {
    private String doctorCedula;
    private String consultationReason;
    private String symptoms;
    private String diagnosis;
    private MedicalRecordDataDTO data;

    // Default constructor
    public MedicalRecordEntryDTO() {}

    // Constructor with parameters
    public MedicalRecordEntryDTO(String doctorCedula, String consultationReason,
                                String symptoms, String diagnosis, MedicalRecordDataDTO data) {
        this.doctorCedula = doctorCedula;
        this.consultationReason = consultationReason;
        this.symptoms = symptoms;
        this.diagnosis = diagnosis;
        this.data = data;
    }

    // Getters and Setters
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

    public MedicalRecordDataDTO getData() {
        return data;
    }

    public void setData(MedicalRecordDataDTO data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return String.format("MedicalRecordEntryDTO{doctorCedula='%s', consultationReason='%s', diagnosis='%s'}",
                           doctorCedula, consultationReason, diagnosis);
    }
}