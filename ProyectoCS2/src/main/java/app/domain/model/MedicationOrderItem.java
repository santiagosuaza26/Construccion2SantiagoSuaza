package app.domain.model;

public class MedicationOrderItem extends OrderItem {
    private final String medicationId;
    private final String medicationName;
    private final String dosage;
    private final String treatmentDuration;
    private final Money cost;

    public MedicationOrderItem(String orderNumber, int itemNumber,
                            String medicationId, String medicationName,
                            String dosage, String treatmentDuration, long cost) {
        super(orderNumber, itemNumber, OrderItemType.MEDICATION);
        this.medicationId = medicationId;
        this.medicationName = medicationName;
        this.dosage = dosage;
        this.treatmentDuration = treatmentDuration;
        this.cost = Money.of(cost);
        validateSelf();
    }
    
    // Constructor alternativo que acepta Money directamente
    public MedicationOrderItem(String orderNumber, int itemNumber,
                            String medicationId, String medicationName,
                            String dosage, String treatmentDuration, Money cost) {
        super(orderNumber, itemNumber, OrderItemType.MEDICATION);
        this.medicationId = medicationId;
        this.medicationName = medicationName;
        this.dosage = dosage;
        this.treatmentDuration = treatmentDuration;
        this.cost = cost;
        validateSelf();
    }

    public String getMedicationId() { return medicationId; }
    public String getMedicationName() { return medicationName; }
    public String getDosage() { return dosage; }
    public String getTreatmentDuration() { return treatmentDuration; }
    
    // Mantener compatibilidad con long para persistencia
    public long getCost() { return cost.getAmount(); }
    
    // Nuevo método para obtener Money
    public Money getCostAsMoney() { return cost; }

    private void validateSelf() {
        if (medicationId == null || medicationId.isBlank()) 
            throw new IllegalArgumentException("Medication id required");
        if (medicationName == null || medicationName.isBlank()) 
            throw new IllegalArgumentException("Medication name required");
        if (cost == null || cost.isZero()) 
            throw new IllegalArgumentException("Invalid cost");
    }
}