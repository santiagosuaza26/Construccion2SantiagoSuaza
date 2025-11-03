package app.clinic.domain.model.entities;

import java.time.LocalDate;

public class ProcedureEntry implements MedicalEntry {
    private final LocalDate date;
    private final String doctorId;
    private final String orderNumber;
    private final String procedureId;
    private final String quantity;
    private final String frequency;
    private final boolean requiresSpecialist;
    private final String specialistId;

    public ProcedureEntry(LocalDate date, String doctorId, String orderNumber, String procedureId, String quantity, String frequency, boolean requiresSpecialist, String specialistId) {
        this.date = date;
        this.doctorId = doctorId;
        this.orderNumber = orderNumber;
        this.procedureId = procedureId;
        this.quantity = quantity != null ? quantity : "";
        this.frequency = frequency != null ? frequency : "";
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

    public String getProcedureId() {
        return procedureId;
    }

    public String getQuantity() {
        return quantity;
    }

    public String getFrequency() {
        return frequency;
    }

    public boolean isRequiresSpecialist() {
        return requiresSpecialist;
    }

    public String getSpecialistId() {
        return specialistId;
    }

    @Override
    public String toString() {
        return "ProcedureEntry{" +
                "date=" + date +
                ", doctorId='" + doctorId + '\'' +
                ", orderNumber='" + orderNumber + '\'' +
                ", procedureId='" + procedureId + '\'' +
                ", quantity='" + quantity + '\'' +
                ", frequency='" + frequency + '\'' +
                ", requiresSpecialist=" + requiresSpecialist +
                ", specialistId='" + specialistId + '\'' +
                '}';
    }
}