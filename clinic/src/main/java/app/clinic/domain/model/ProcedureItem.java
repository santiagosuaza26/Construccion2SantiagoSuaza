package app.clinic.domain.model;

import java.util.Objects;

/**
 * Value Object representing an individual procedure item within a procedure order.
 * Contains procedure details, quantity, frequency, and specialist assistance requirements.
 */
public class ProcedureItem {
    private final ItemNumber itemNumber;
    private final InventoryItemId procedureId;
    private final Quantity quantity;
    private final Frequency frequency;
    private final RequiresSpecialistAssistance requiresSpecialistAssistance;
    private final MedicalSpecialty specialistType;

    private ProcedureItem(ItemNumber itemNumber, InventoryItemId procedureId, Quantity quantity,
                         Frequency frequency, RequiresSpecialistAssistance requiresSpecialistAssistance,
                         MedicalSpecialty specialistType) {
        this.itemNumber = itemNumber;
        this.procedureId = procedureId;
        this.quantity = quantity;
        this.frequency = frequency;
        this.requiresSpecialistAssistance = requiresSpecialistAssistance;
        this.specialistType = specialistType;
    }

    public static ProcedureItem of(ItemNumber itemNumber, InventoryItemId procedureId, Quantity quantity,
                                  Frequency frequency) {
        return new ProcedureItem(itemNumber, procedureId, quantity, frequency,
                               RequiresSpecialistAssistance.no(), null);
    }

    public static ProcedureItem of(ItemNumber itemNumber, InventoryItemId procedureId, Quantity quantity,
                                  Frequency frequency, RequiresSpecialistAssistance requiresSpecialistAssistance,
                                  MedicalSpecialty specialistType) {
        return new ProcedureItem(itemNumber, procedureId, quantity, frequency,
                               requiresSpecialistAssistance, specialistType);
    }

    public ItemNumber getItemNumber() {
        return itemNumber;
    }

    public InventoryItemId getProcedureId() {
        return procedureId;
    }

    public Quantity getQuantity() {
        return quantity;
    }

    public Frequency getFrequency() {
        return frequency;
    }

    public RequiresSpecialistAssistance getRequiresSpecialistAssistance() {
        return requiresSpecialistAssistance;
    }

    public MedicalSpecialty getSpecialistType() {
        return specialistType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProcedureItem that = (ProcedureItem) o;
        return Objects.equals(itemNumber, that.itemNumber) &&
               Objects.equals(procedureId, that.procedureId) &&
               Objects.equals(quantity, that.quantity) &&
               Objects.equals(frequency, that.frequency) &&
               Objects.equals(requiresSpecialistAssistance, that.requiresSpecialistAssistance) &&
               Objects.equals(specialistType, that.specialistType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(itemNumber, procedureId, quantity, frequency,
                          requiresSpecialistAssistance, specialistType);
    }

    @Override
    public String toString() {
        return String.format("ProcedureItem{itemNumber=%s, procedureId=%s, quantity=%s, frequency=%s, specialist=%s}",
                           itemNumber, procedureId, quantity, frequency,
                           requiresSpecialistAssistance.isRequired() ? specialistType : "none");
    }
}