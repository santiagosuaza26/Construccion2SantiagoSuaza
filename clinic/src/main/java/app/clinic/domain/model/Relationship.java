package app.clinic.domain.model;

/**
 * Enumeration representing possible relationships for emergency contacts.
 */
public enum Relationship {
    PADRE("Padre"),
    MADRE("Madre"),
    HIJO("Hijo"),
    HIJA("Hija"),
    HERMANO("Hermano"),
    HERMANA("Hermana"),
    ESPOSO("Esposo"),
    ESPOSA("Esposa"),
    PAREJA("Pareja"),
    AMIGO("Amigo"),
    AMIGA("Amiga"),
    TIO("Tío"),
    TIA("Tía"),
    ABUEL("Abuelo"),
    ABUELA("Abuela"),
    OTRO("Otro");

    private final String displayName;

    Relationship(String displayName) {
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