package app.clinic.domain.model;

import java.util.Objects;

/**
 * Value Object representing a patient record entry with additional data.
 * Combines basic entry information with structured data for medications, procedures, and diagnostic aids.
 */
public class PatientRecordEntryWithData {
    private final DoctorCedula doctorCedula;
    private final ConsultationReason consultationReason;
    private final Symptoms symptoms;
    private final Diagnosis diagnosis;
    private final PatientRecordEntryData data;

    private PatientRecordEntryWithData(DoctorCedula doctorCedula, ConsultationReason consultationReason,
                                      Symptoms symptoms, Diagnosis diagnosis, PatientRecordEntryData data) {
        this.doctorCedula = doctorCedula;
        this.consultationReason = consultationReason;
        this.symptoms = symptoms;
        this.diagnosis = diagnosis;
        this.data = data != null ? data : PatientRecordEntryData.empty();
    }

    public static PatientRecordEntryWithData of(DoctorCedula doctorCedula, ConsultationReason consultationReason,
                                               Symptoms symptoms, Diagnosis diagnosis) {
        return new PatientRecordEntryWithData(doctorCedula, consultationReason, symptoms, diagnosis,
                                            PatientRecordEntryData.empty());
    }

    public static PatientRecordEntryWithData of(DoctorCedula doctorCedula, ConsultationReason consultationReason,
                                               Symptoms symptoms, Diagnosis diagnosis, PatientRecordEntryData data) {
        return new PatientRecordEntryWithData(doctorCedula, consultationReason, symptoms, diagnosis, data);
    }

    public DoctorCedula getDoctorCedula() {
        return doctorCedula;
    }

    public ConsultationReason getConsultationReason() {
        return consultationReason;
    }

    public Symptoms getSymptoms() {
        return symptoms;
    }

    public Diagnosis getDiagnosis() {
        return diagnosis;
    }

    public PatientRecordEntryData getData() {
        return data;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PatientRecordEntryWithData that = (PatientRecordEntryWithData) o;
        return Objects.equals(doctorCedula, that.doctorCedula) &&
               Objects.equals(consultationReason, that.consultationReason) &&
               Objects.equals(symptoms, that.symptoms) &&
               Objects.equals(diagnosis, that.diagnosis) &&
               Objects.equals(data, that.data);
    }

    @Override
    public int hashCode() {
        return Objects.hash(doctorCedula, consultationReason, symptoms, diagnosis, data);
    }

    @Override
    public String toString() {
        return String.format("PatientRecordEntryWithData{doctorCedula=%s, consultationReason=%s, symptoms=%s, diagnosis=%s}",
                           doctorCedula, consultationReason, symptoms, diagnosis);
    }
}