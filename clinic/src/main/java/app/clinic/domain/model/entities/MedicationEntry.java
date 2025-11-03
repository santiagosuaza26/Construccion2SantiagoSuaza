package app.clinic.domain.model.entities;

import java.time.LocalDate;

public class MedicationEntry implements MedicalEntry {
    private final LocalDate date;
    private final String doctorId;
    private final String orderNumber;
    private final String medicationId;
    private final String dosage;
    private final String duration;

    public MedicationEntry(LocalDate date, String doctorId, String orderNumber, String medicationId, String dosage, String duration) {
        this.date = date;
        this.doctorId = doctorId;
        this.orderNumber = orderNumber;
        this.medicationId = medicationId;
        this.dosage = dosage != null ? dosage : "";
        this.duration = duration != null ? duration : "";
    }

    @Override
    public LocalDate getDate() {
        return date;
    }

    @Override
    public String getDoctorId() {
        return doctorId;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public String getMedicationId() {
        return medicationId;
    }

    public String getDosage() {
        return dosage;
    }

    public String getDuration() {
        return duration;
    }

    @Override
    public String toString() {
        return "MedicationEntry{" +
                "date=" + date +
                ", doctorId='" + doctorId + '\'' +
                ", orderNumber='" + orderNumber + '\'' +
                ", medicationId='" + medicationId + '\'' +
                ", dosage='" + dosage + '\'' +
                ", duration='" + duration + '\'' +
                '}';
    }
}