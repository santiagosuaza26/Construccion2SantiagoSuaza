package app.clinic.domain.model.entities;

import app.clinic.domain.model.valueobject.Id;
import app.clinic.domain.model.valueobject.OrderNumber;

public class DiagnosticAidOrder {
    private final OrderNumber orderNumber;
    private final int item;
    private final Id diagnosticAidId;
    private final String quantity;
    private final boolean requiresSpecialist;
    private final Id specialistId;
    private final double cost;

    public DiagnosticAidOrder(OrderNumber orderNumber, int item, Id diagnosticAidId, String quantity, boolean requiresSpecialist, Id specialistId, double cost) {
        this.orderNumber = orderNumber;
        this.item = item;
        this.diagnosticAidId = diagnosticAidId;
        this.quantity = quantity;
        this.requiresSpecialist = requiresSpecialist;
        this.specialistId = specialistId;
        this.cost = cost;
    }

    public OrderNumber getOrderNumber() {
        return orderNumber;
    }

    public int getItem() {
        return item;
    }

    public Id getDiagnosticAidId() {
        return diagnosticAidId;
    }

    public String getQuantity() {
        return quantity;
    }

    public boolean isRequiresSpecialist() {
        return requiresSpecialist;
    }

    public Id getSpecialistId() {
        return specialistId;
    }

    public double getCost() {
        return cost;
    }

    public String getDiagnosticAidName() {
        // This would be fetched from inventory, but for domain, return id
        return diagnosticAidId.getValue();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DiagnosticAidOrder that = (DiagnosticAidOrder) o;
        return item == that.item && orderNumber.equals(that.orderNumber);
    }

    @Override
    public int hashCode() {
        return orderNumber.hashCode() + item;
    }

    @Override
    public String toString() {
        return "DiagnosticAidOrder " + orderNumber + " item " + item;
    }
}