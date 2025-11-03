package app.clinic.domain.model.entities;

import java.time.LocalDate;

public class DiagnosisEntry implements MedicalEntry {
    private final LocalDate date;
    private final String doctorId;
    private final String diagnosis;
    private final String symptoms;

    public DiagnosisEntry(LocalDate date, String doctorId, String diagnosis, String symptoms) {
        this.date = date;
        this.doctorId = doctorId;
        this.diagnosis = diagnosis != null ? diagnosis : "";
        this.symptoms = symptoms != null ? symptoms : "";
    }

    @Override
    public LocalDate getDate() {
        return date;
    }

    @Override
    public String getDoctorId() {
        return doctorId;
    }

    public String getDiagnosis() {
        return diagnosis;
    }

    public String getSymptoms() {
        return symptoms;
    }

    @Override
    public String toString() {
        return "DiagnosisEntry{" +
                "date=" + date +
                ", doctorId='" + doctorId + '\'' +
                ", diagnosis='" + diagnosis + '\'' +
                ", symptoms='" + symptoms + '\'' +
                '}';
    }
}