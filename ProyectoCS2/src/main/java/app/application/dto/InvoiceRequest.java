package app.application.dto;

public class InvoiceRequest {
    private String id;
    private String patientId;
    private double total;

    // Getters y Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getPatientId() { return patientId; }
    public void setPatientId(String patientId) { this.patientId = patientId; }

    public double getTotal() { return total; }
    public void setTotal(double total) { this.total = total; }
}
