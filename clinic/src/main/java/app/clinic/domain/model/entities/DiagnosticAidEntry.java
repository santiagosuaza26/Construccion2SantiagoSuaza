package app.clinic.domain.model.entities;

import java.time.LocalDate;

public class DiagnosticAidEntry implements MedicalEntry {
    private final LocalDate date;
    private final String doctorId;
    private final String orderNumber;
    private final String diagnosticAidId;
    private final String quantity;
    private final boolean requiresSpecialist;
    private final String specialistId;

    public DiagnosticAidEntry(LocalDate date, String doctorId, String orderNumber, String diagnosticAidId, String quantity, boolean requiresSpecialist, String specialistId) {
        this.date = date;
        this.doctorId = doctorId;
        this.orderNumber = orderNumber;
        this.diagnosticAidId = diagnosticAidId;
        this.quantity = quantity != null ? quantity : "";
        this.requiresSpecialist = requiresSpecialist;
        this.specialistId = specialistId != null ? specialistId : "";
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

    public String getDiagnosticAidId() {
        return diagnosticAidId;
    }

    public String getQuantity() {
        return quantity;
    }

    public boolean isRequiresSpecialist() {
        return requiresSpecialist;
    }

    public String getSpecialistId() {
        return specialistId;
    }

    @Override
    public String toString() {
        return "DiagnosticAidEntry{" +
                "date=" + date +
                ", doctorId='" + doctorId + '\'' +
                ", orderNumber='" + orderNumber + '\'' +
                ", diagnosticAidId='" + diagnosticAidId + '\'' +
                ", quantity='" + quantity + '\'' +
                ", requiresSpecialist=" + requiresSpecialist +
                ", specialistId='" + specialistId + '\'' +
                '}';
    }
}