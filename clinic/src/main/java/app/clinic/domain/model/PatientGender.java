package app.clinic.domain.model;

/**
 * Enumeration representing gender options for patients.
 */
public enum PatientGender {
    MASCULINO("Masculino"),
    FEMENINO("Femenino"),
    OTRO("Otro");

    private final String displayName;

    PatientGender(String displayName) {
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