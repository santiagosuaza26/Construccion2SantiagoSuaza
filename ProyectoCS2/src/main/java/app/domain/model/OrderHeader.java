package app.domain.model;

import java.time.LocalDate;

public class OrderHeader {
    private final String orderNumber; // max 6 digits (en el repositorio se garantiza unicidad)
    private final String patientIdCard;
    private final String doctorIdCard;
    private final LocalDate creationDate;

    public OrderHeader(String orderNumber, String patientIdCard, String doctorIdCard, LocalDate creationDate) {
        this.orderNumber = orderNumber;
        this.patientIdCard = patientIdCard;
        this.doctorIdCard = doctorIdCard;
        this.creationDate = creationDate;
        validate();
    }

    public String getOrderNumber() { return orderNumber; }
    public String getPatientIdCard() { return patientIdCard; }
    public String getDoctorIdCard() { return doctorIdCard; }
    public LocalDate getCreationDate() { return creationDate; }

    private void validate() {
        if (orderNumber == null || !orderNumber.matches("\\d{1,6}")) throw new IllegalArgumentException("Invalid order number");
        if (patientIdCard == null || !patientIdCard.matches("\\d+")) throw new IllegalArgumentException("Invalid patient id");
        if (doctorIdCard == null || !doctorIdCard.matches("\\d+")) throw new IllegalArgumentException("Invalid doctor id");
        if (creationDate == null) throw new IllegalArgumentException("Creation date required");
    }
}