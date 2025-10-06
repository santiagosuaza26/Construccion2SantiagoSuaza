package app.clinic.domain.model;

import java.util.Objects;

/**
 * Value Object representing an individual medication item within a medication order.
 * Contains medication details, dosage, duration, and item number.
 */
public class MedicationItem {
    private final ItemNumber itemNumber;
    private final InventoryItemId medicationId;
    private final Dosage dosage;
    private final TreatmentDuration duration;

    private MedicationItem(ItemNumber itemNumber, InventoryItemId medicationId,
                          Dosage dosage, TreatmentDuration duration) {
        this.itemNumber = itemNumber;
        this.medicationId = medicationId;
        this.dosage = dosage;
        this.duration = duration;
    }

    public static MedicationItem of(ItemNumber itemNumber, InventoryItemId medicationId,
                                   Dosage dosage, TreatmentDuration duration) {
        return new MedicationItem(itemNumber, medicationId, dosage, duration);
    }

    public ItemNumber getItemNumber() {
        return itemNumber;
    }

    public InventoryItemId getMedicationId() {
        return medicationId;
    }

    public Dosage getDosage() {
        return dosage;
    }

    public TreatmentDuration getDuration() {
        return duration;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MedicationItem that = (MedicationItem) o;
        return Objects.equals(itemNumber, that.itemNumber) &&
               Objects.equals(medicationId, that.medicationId) &&
               Objects.equals(dosage, that.dosage) &&
               Objects.equals(duration, that.duration);
    }

    @Override
    public int hashCode() {
        return Objects.hash(itemNumber, medicationId, dosage, duration);
    }

    @Override
    public String toString() {
        return String.format("MedicationItem{itemNumber=%s, medicationId=%s, dosage=%s, duration=%s}",
                           itemNumber, medicationId, dosage, duration);
    }
}