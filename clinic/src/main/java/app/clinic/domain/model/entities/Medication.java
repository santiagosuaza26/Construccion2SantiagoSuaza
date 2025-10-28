package app.clinic.domain.model.entities;

import app.clinic.domain.model.valueobject.Id;

public class Medication {
    private final Id id;
    private final String name;
    private final double cost;
    private final boolean requiresSpecialist;
    private final Id specialistType;

    public Medication(Id id, String name, double cost, boolean requiresSpecialist, Id specialistType) {
        this.id = id;
        this.name = name;
        this.cost = cost;
        this.requiresSpecialist = requiresSpecialist;
        this.specialistType = specialistType;
    }

    public Id getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getCost() {
        return cost;
    }

    public boolean isRequiresSpecialist() {
        return requiresSpecialist;
    }

    public Id getSpecialistType() {
        return specialistType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Medication that = (Medication) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString() {
        return name + " (ID: " + id + ")";
    }
}