package app.clinic.domain.model;

import java.util.Objects;

/**
 * Value Object representing an individual diagnostic aid item within a diagnostic aid order.
 * Contains diagnostic aid details, quantity, and specialist assistance requirements.
 */
public class DiagnosticAidItem {
    private final ItemNumber itemNumber;
    private final InventoryItemId diagnosticAidId;
    private final Quantity quantity;
    private final RequiresSpecialistAssistance requiresSpecialistAssistance;
    private final MedicalSpecialty specialistType;

    private DiagnosticAidItem(ItemNumber itemNumber, InventoryItemId diagnosticAidId, Quantity quantity,
                             RequiresSpecialistAssistance requiresSpecialistAssistance, MedicalSpecialty specialistType) {
        this.itemNumber = itemNumber;
        this.diagnosticAidId = diagnosticAidId;
        this.quantity = quantity;
        this.requiresSpecialistAssistance = requiresSpecialistAssistance;
        this.specialistType = specialistType;
    }

    public static DiagnosticAidItem of(ItemNumber itemNumber, InventoryItemId diagnosticAidId, Quantity quantity) {
        return new DiagnosticAidItem(itemNumber, diagnosticAidId, quantity,
                                   RequiresSpecialistAssistance.no(), null);
    }

    public static DiagnosticAidItem of(ItemNumber itemNumber, InventoryItemId diagnosticAidId, Quantity quantity,
                                      RequiresSpecialistAssistance requiresSpecialistAssistance, MedicalSpecialty specialistType) {
        return new DiagnosticAidItem(itemNumber, diagnosticAidId, quantity,
                                   requiresSpecialistAssistance, specialistType);
    }

    public ItemNumber getItemNumber() {
        return itemNumber;
    }

    public InventoryItemId getDiagnosticAidId() {
        return diagnosticAidId;
    }

    public Quantity getQuantity() {
        return quantity;
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
        DiagnosticAidItem that = (DiagnosticAidItem) o;
        return Objects.equals(itemNumber, that.itemNumber) &&
               Objects.equals(diagnosticAidId, that.diagnosticAidId) &&
               Objects.equals(quantity, that.quantity) &&
               Objects.equals(requiresSpecialistAssistance, that.requiresSpecialistAssistance) &&
               Objects.equals(specialistType, that.specialistType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(itemNumber, diagnosticAidId, quantity,
                          requiresSpecialistAssistance, specialistType);
    }

    @Override
    public String toString() {
        return String.format("DiagnosticAidItem{itemNumber=%s, diagnosticAidId=%s, quantity=%s, specialist=%s}",
                           itemNumber, diagnosticAidId, quantity,
                           requiresSpecialistAssistance.isRequired() ? specialistType : "none");
    }
}