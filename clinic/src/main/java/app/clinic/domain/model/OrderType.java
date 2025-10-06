package app.clinic.domain.model;

/**
 * Enumeration representing types of medical orders.
 */
public enum OrderType {
    MEDICAMENTO("Medicamento"),
    PROCEDIMIENTO("Procedimiento"),
    AYUDA_DIAGNOSTICA("Ayuda Diagnóstica");

    private final String displayName;

    OrderType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }
}