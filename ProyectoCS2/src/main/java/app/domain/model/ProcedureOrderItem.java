package app.domain.model;

public class ProcedureOrderItem extends OrderItem {
    private final String procedureId;
    private final String procedureName;
    private final int quantity;
    private final String frequency;
    private final boolean specialistRequired;
    private final String specialtyId;
    private final Money cost; // Usar Money internamente

    public ProcedureOrderItem(String orderNumber, int itemNumber,
                            String procedureId, String procedureName,
                            int quantity, String frequency,
                            boolean specialistRequired, String specialtyId,
                            long cost) {
        super(orderNumber, itemNumber, OrderItemType.PROCEDURE);
        this.procedureId = procedureId;
        this.procedureName = procedureName;
        this.quantity = quantity;
        this.frequency = frequency;
        this.specialistRequired = specialistRequired;
        this.specialtyId = specialtyId;
        this.cost = Money.of(cost); // Convert to Money
        validateSelf();
    }
    
    // Constructor alternativo que acepta Money directamente
    public ProcedureOrderItem(String orderNumber, int itemNumber,
                            String procedureId, String procedureName,
                            int quantity, String frequency,
                            boolean specialistRequired, String specialtyId,
                            Money cost) {
        super(orderNumber, itemNumber, OrderItemType.PROCEDURE);
        this.procedureId = procedureId;
        this.procedureName = procedureName;
        this.quantity = quantity;
        this.frequency = frequency;
        this.specialistRequired = specialistRequired;
        this.specialtyId = specialtyId;
        this.cost = cost;
        validateSelf();
    }

    public String getProcedureId() { return procedureId; }
    public String getProcedureName() { return procedureName; }
    public int getQuantity() { return quantity; }
    public String getFrequency() { return frequency; }
    public boolean isSpecialistRequired() { return specialistRequired; }
    public String getSpecialtyId() { return specialtyId; }
    
    // Mantener compatibilidad con long para persistencia
    public long getCost() { return cost.getAmount(); }
    
    // Nuevo método para obtener Money
    public Money getCostAsMoney() { return cost; }

    private void validateSelf() {
        if (procedureId == null || procedureId.isBlank()) 
            throw new IllegalArgumentException("Procedure id required");
        if (procedureName == null || procedureName.isBlank()) 
            throw new IllegalArgumentException("Procedure name required");
        if (quantity < 1) 
            throw new IllegalArgumentException("Quantity must be >= 1");
        if (cost == null || cost.isZero()) 
            throw new IllegalArgumentException("Invalid cost");
        if (!specialistRequired && specialtyId != null) {
            throw new IllegalArgumentException("SpecialtyId only when specialist is required");
        }
    }
}