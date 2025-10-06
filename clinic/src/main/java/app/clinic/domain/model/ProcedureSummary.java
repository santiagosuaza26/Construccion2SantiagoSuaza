package app.clinic.domain.model;

import java.util.Objects;

/**
 * Value Object representing a summary of procedure applied to a patient.
 * Contains procedure name and cost.
 */
public class ProcedureSummary {
    private final InventoryItemName procedureName;
    private final Cost cost;

    private ProcedureSummary(InventoryItemName procedureName, Cost cost) {
        this.procedureName = procedureName;
        this.cost = cost;
    }

    public static ProcedureSummary of(InventoryItemName procedureName, Cost cost) {
        return new ProcedureSummary(procedureName, cost);
    }

    public InventoryItemName getProcedureName() {
        return procedureName;
    }

    public Cost getCost() {
        return cost;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProcedureSummary that = (ProcedureSummary) o;
        return Objects.equals(procedureName, that.procedureName) &&
               Objects.equals(cost, that.cost);
    }

    @Override
    public int hashCode() {
        return Objects.hash(procedureName, cost);
    }

    @Override
    public String toString() {
        return String.format("ProcedureSummary{name=%s, cost=%s}", procedureName, cost);
    }
}