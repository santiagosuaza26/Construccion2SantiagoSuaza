package app.domain.model;

public class DiagnosticOrder extends Order {

    private int quantity;
    private boolean requiresSpecialist;
    private String specialistType;

    public DiagnosticOrder(String orderNumber, String patientId, String doctorId, String creationDate,
        int quantity, boolean requiresSpecialist, String specialistType) {
        super(orderNumber, patientId, doctorId, creationDate);
        this.quantity = quantity;
        this.requiresSpecialist = requiresSpecialist;
        this.specialistType = specialistType;
    }

    public int getQuantity() {
        return quantity;
    }

    public boolean isRequiresSpecialist() {
        return requiresSpecialist;
    }

    public String getSpecialistType() {
        return specialistType;
    }
}

