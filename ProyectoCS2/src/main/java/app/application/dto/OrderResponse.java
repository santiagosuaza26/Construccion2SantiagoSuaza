package app.application.dto;

public class OrderResponse {
    private String orderNumber;
    private String patientId;
    private String description;
    private double amount;

    public OrderResponse(String orderNumber, String patientId, String description, double amount) {
        this.orderNumber = orderNumber;
        this.patientId = patientId;
        this.description = description;
        this.amount = amount;
    }

    // Getters
    public String getOrderNumber() { return orderNumber; }
    public String getPatientId() { return patientId; }
    public String getDescription() { return description; }
    public double getAmount() { return amount; }
}