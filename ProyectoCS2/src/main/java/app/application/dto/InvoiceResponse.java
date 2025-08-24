package app.application.dto;

public class InvoiceResponse {
    private String id;
    private String patientId;
    private double total;

    public InvoiceResponse(String id, String patientId, double total) {
        this.id = id;
        this.patientId = patientId;
        this.total = total;
    }

    // Getters
    public String getId() { return id; }
    public String getPatientId() { return patientId; }
    public double getTotal() { return total; }
}
