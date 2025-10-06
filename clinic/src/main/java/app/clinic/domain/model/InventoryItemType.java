package app.clinic.domain.model;

/**
 * Enumeration representing types of inventory items in the clinic.
 */
public enum InventoryItemType {
    MEDICAMENTO("Medicamento"),
    PROCEDIMIENTO("Procedimiento"),
    AYUDA_DIAGNOSTICA("Ayuda Diagnóstica");

    private final String displayName;

    InventoryItemType(String displayName) {
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