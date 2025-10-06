package app.clinic.domain.model;

/**
 * Enumeration representing the status of medical orders.
 */
public enum OrderStatus {
    CREADA("Creada"),
    EN_PROCESO("En Proceso"),
    COMPLETADA("Completada"),
    CANCELADA("Cancelada");

    private final String displayName;

    OrderStatus(String displayName) {
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