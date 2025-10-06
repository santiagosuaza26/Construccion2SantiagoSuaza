package app.clinic.domain.model;

import java.util.Objects;

/**
 * Value Object representing a single entry in a patient's medical record.
 * Contains doctor cedula, consultation reason, symptoms, diagnosis, and unstructured data.
 */
public class PatientRecordEntry {
    private final DoctorCedula doctorCedula;
    private final ConsultationReason consultationReason;
    private final Symptoms symptoms;
    private final Diagnosis diagnosis;
    private final PatientRecordData data;

    private PatientRecordEntry(DoctorCedula doctorCedula, ConsultationReason consultationReason,
                              Symptoms symptoms, Diagnosis diagnosis, PatientRecordData data) {
        this.doctorCedula = doctorCedula;
        this.consultationReason = consultationReason;
        this.symptoms = symptoms;
        this.diagnosis = diagnosis;
        this.data = data != null ? data : PatientRecordData.empty();
    }

    public static PatientRecordEntry of(DoctorCedula doctorCedula, ConsultationReason consultationReason,
                                       Symptoms symptoms, Diagnosis diagnosis) {
        return new PatientRecordEntry(doctorCedula, consultationReason, symptoms, diagnosis, PatientRecordData.empty());
    }

    public static PatientRecordEntry of(DoctorCedula doctorCedula, ConsultationReason consultationReason,
                                       Symptoms symptoms, Diagnosis diagnosis, PatientRecordData data) {
        return new PatientRecordEntry(doctorCedula, consultationReason, symptoms, diagnosis, data);
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

    public PatientRecordData getData() {
        return data;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PatientRecordEntry that = (PatientRecordEntry) o;
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
        return String.format("PatientRecordEntry{doctorCedula=%s, consultationReason=%s, symptoms=%s, diagnosis=%s}",
                           doctorCedula, consultationReason, symptoms, diagnosis);
    }
}