package app.clinic.domain.model;

import java.util.List;
import java.util.Objects;

/**
 * Value Object representing a summary of clinical orders for billing purposes.
 * Contains information about medications, procedures, and diagnostic aids applied.
 */
public class OrderSummary {
    private final OrderNumber orderNumber;
    private final List<MedicationSummary> medications;
    private final List<ProcedureSummary> procedures;
    private final List<DiagnosticAidSummary> diagnosticAids;

    private OrderSummary(OrderNumber orderNumber, List<MedicationSummary> medications,
                        List<ProcedureSummary> procedures, List<DiagnosticAidSummary> diagnosticAids) {
        this.orderNumber = orderNumber;
        this.medications = List.copyOf(medications != null ? medications : List.of());
        this.procedures = List.copyOf(procedures != null ? procedures : List.of());
        this.diagnosticAids = List.copyOf(diagnosticAids != null ? diagnosticAids : List.of());
    }

    public static OrderSummary of(OrderNumber orderNumber, List<MedicationSummary> medications,
                                 List<ProcedureSummary> procedures, List<DiagnosticAidSummary> diagnosticAids) {
        return new OrderSummary(orderNumber, medications, procedures, diagnosticAids);
    }

    public OrderNumber getOrderNumber() {
        return orderNumber;
    }

    public List<MedicationSummary> getMedications() {
        return medications;
    }

    public List<ProcedureSummary> getProcedures() {
        return procedures;
    }

    public List<DiagnosticAidSummary> getDiagnosticAids() {
        return diagnosticAids;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderSummary that = (OrderSummary) o;
        return Objects.equals(orderNumber, that.orderNumber) &&
               Objects.equals(medications, that.medications) &&
               Objects.equals(procedures, that.procedures) &&
               Objects.equals(diagnosticAids, that.diagnosticAids);
    }

    @Override
    public int hashCode() {
        return Objects.hash(orderNumber, medications, procedures, diagnosticAids);
    }

    @Override
    public String toString() {
        return String.format("OrderSummary{orderNumber=%s, medications=%d, procedures=%d, diagnosticAids=%d}",
                           orderNumber, medications.size(), procedures.size(), diagnosticAids.size());
    }
}