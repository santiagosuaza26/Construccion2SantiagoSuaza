package app.clinic.domain.model.entities;

import app.clinic.domain.model.valueobject.Id;
import app.clinic.domain.model.valueobject.OrderNumber;

public class MedicationOrder {
    private final OrderNumber orderNumber;
    private final int item;
    private final Id medicationId;
    private final String dosage;
    private final String duration;
    private final double cost;

    public MedicationOrder(OrderNumber orderNumber, int item, Id medicationId, String dosage, String duration, double cost) {
        this.orderNumber = orderNumber;
        this.item = item;
        this.medicationId = medicationId;
        this.dosage = dosage;
        this.duration = duration;
        this.cost = cost;
    }

    public OrderNumber getOrderNumber() {
        return orderNumber;
    }

    public int getItem() {
        return item;
    }

    public Id getMedicationId() {
        return medicationId;
    }

    public String getDosage() {
        return dosage;
    }

    public String getDuration() {
        return duration;
    }

    public double getCost() {
        return cost;
    }

    public String getMedicationName() {
        // This would be fetched from inventory, but for domain, return id
        return medicationId.getValue();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MedicationOrder that = (MedicationOrder) o;
        return item == that.item && orderNumber.equals(that.orderNumber);
    }

    @Override
    public int hashCode() {
        return orderNumber.hashCode() + item;
    }

    @Override
    public String toString() {
        return "MedicationOrder " + orderNumber + " item " + item;
    }
}