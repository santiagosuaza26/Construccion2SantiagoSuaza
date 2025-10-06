package app.clinic.domain.model;

import java.util.Objects;

/**
 * Value Object representing a summary of diagnostic aid applied to a patient.
 * Contains diagnostic aid name and cost.
 */
public class DiagnosticAidSummary {
    private final InventoryItemName diagnosticAidName;
    private final Cost cost;

    private DiagnosticAidSummary(InventoryItemName diagnosticAidName, Cost cost) {
        this.diagnosticAidName = diagnosticAidName;
        this.cost = cost;
    }

    public static DiagnosticAidSummary of(InventoryItemName diagnosticAidName, Cost cost) {
        return new DiagnosticAidSummary(diagnosticAidName, cost);
    }

    public InventoryItemName getDiagnosticAidName() {
        return diagnosticAidName;
    }

    public Cost getCost() {
        return cost;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DiagnosticAidSummary that = (DiagnosticAidSummary) o;
        return Objects.equals(diagnosticAidName, that.diagnosticAidName) &&
               Objects.equals(cost, that.cost);
    }

    @Override
    public int hashCode() {
        return Objects.hash(diagnosticAidName, cost);
    }

    @Override
    public String toString() {
        return String.format("DiagnosticAidSummary{name=%s, cost=%s}", diagnosticAidName, cost);
    }
}