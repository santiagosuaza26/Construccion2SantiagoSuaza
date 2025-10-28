package app.clinic.domain.model.entities;

import app.clinic.domain.model.valueobject.Id;
import app.clinic.domain.model.valueobject.OrderNumber;

public class ProcedureOrder {
    private final OrderNumber orderNumber;
    private final int item;
    private final Id procedureId;
    private final String quantity;
    private final String frequency;
    private final boolean requiresSpecialist;
    private final Id specialistId;
    private final double cost;

    public ProcedureOrder(OrderNumber orderNumber, int item, Id procedureId, String quantity, String frequency, boolean requiresSpecialist, Id specialistId, double cost) {
        this.orderNumber = orderNumber;
        this.item = item;
        this.procedureId = procedureId;
        this.quantity = quantity;
        this.frequency = frequency;
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

    public Id getProcedureId() {
        return procedureId;
    }

    public String getQuantity() {
        return quantity;
    }

    public String getFrequency() {
        return frequency;
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

    public String getProcedureName() {
        // This would be fetched from inventory, but for domain, return id
        return procedureId.getValue();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProcedureOrder that = (ProcedureOrder) o;
        return item == that.item && orderNumber.equals(that.orderNumber);
    }

    @Override
    public int hashCode() {
        return orderNumber.hashCode() + item;
    }

    @Override
    public String toString() {
        return "ProcedureOrder " + orderNumber + " item " + item;
    }
}