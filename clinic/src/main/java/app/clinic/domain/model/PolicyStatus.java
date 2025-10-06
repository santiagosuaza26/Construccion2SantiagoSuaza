package app.clinic.domain.model;

/**
 * Enumeration representing the status of an insurance policy.
 */
public enum PolicyStatus {
    ACTIVA("Activa"),
    INACTIVA("Inactiva"),
    VENCIDA("Vencida"),
    CANCELADA("Cancelada");

    private final String displayName;

    PolicyStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public boolean isActive() {
        return this == ACTIVA;
    }

    @Override
    public String toString() {
        return displayName;
    }
}