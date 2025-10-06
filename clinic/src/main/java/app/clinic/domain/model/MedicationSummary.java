package app.clinic.domain.model;

import java.util.Objects;

/**
 * Value Object representing a summary of medication applied to a patient.
 * Contains medication name, cost, and dosage applied.
 */
public class MedicationSummary {
    private final InventoryItemName medicationName;
    private final Cost cost;
    private final Dosage dosage;

    private MedicationSummary(InventoryItemName medicationName, Cost cost, Dosage dosage) {
        this.medicationName = medicationName;
        this.cost = cost;
        this.dosage = dosage;
    }

    public static MedicationSummary of(InventoryItemName medicationName, Cost cost, Dosage dosage) {
        return new MedicationSummary(medicationName, cost, dosage);
    }

    public InventoryItemName getMedicationName() {
        return medicationName;
    }

    public Cost getCost() {
        return cost;
    }

    public Dosage getDosage() {
        return dosage;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MedicationSummary that = (MedicationSummary) o;
        return Objects.equals(medicationName, that.medicationName) &&
               Objects.equals(cost, that.cost) &&
               Objects.equals(dosage, that.dosage);
    }

    @Override
    public int hashCode() {
        return Objects.hash(medicationName, cost, dosage);
    }

    @Override
    public String toString() {
        return String.format("MedicationSummary{name=%s, cost=%s, dosage=%s}",
                           medicationName, cost, dosage);
    }
}