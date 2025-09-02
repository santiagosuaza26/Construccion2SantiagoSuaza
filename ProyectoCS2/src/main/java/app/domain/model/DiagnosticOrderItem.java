package app.domain.model;

public class DiagnosticOrderItem extends OrderItem {
    private final String diagnosticId;
    private final String diagnosticName;
    private final int quantity;
    private final boolean specialistRequired;
    private final String specialtyId;
    private final long cost;

    public DiagnosticOrderItem(String orderNumber, int itemNumber,
                            String diagnosticId, String diagnosticName,
                            int quantity, boolean specialistRequired,
                            String specialtyId, long cost) {
        super(orderNumber, itemNumber, OrderItemType.DIAGNOSTIC);
        this.diagnosticId = diagnosticId;
        this.diagnosticName = diagnosticName;
        this.quantity = quantity;
        this.specialistRequired = specialistRequired;
        this.specialtyId = specialtyId;
        this.cost = cost;
        validateSelf();
    }

    public String getDiagnosticId() { return diagnosticId; }
    public String getDiagnosticName() { return diagnosticName; }
    public int getQuantity() { return quantity; }
    public boolean isSpecialistRequired() { return specialistRequired; }
    public String getSpecialtyId() { return specialtyId; }
    public long getCost() { return cost; }

    private void validateSelf() {
        if (diagnosticId == null || diagnosticId.isBlank()) throw new IllegalArgumentException("Diagnostic id required");
        if (diagnosticName == null || diagnosticName.isBlank()) throw new IllegalArgumentException("Diagnostic name required");
        if (quantity < 1) throw new IllegalArgumentException("Quantity must be >= 1");
        if (cost < 0) throw new IllegalArgumentException("Invalid cost");
        if (!specialistRequired && specialtyId != null) {
            throw new IllegalArgumentException("SpecialtyId only when specialist is required");
        }
    }
}