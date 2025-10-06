package app.clinic.domain.model;

/**
 * Enumeration representing medical specialties in the clinic.
 */
public enum MedicalSpecialty {
    CARDIOLOGIA("Cardiología"),
    DERMATOLOGIA("Dermatología"),
    NEUROLOGIA("Neurología"),
    OFTALMOLOGIA("Oftalmología"),
    PEDIATRIA("Pediatría"),
    GINECOLOGIA("Ginecología"),
    UROLOGIA("Urología"),
    TRAUMATOLOGIA("Traumatología"),
    GASTROENTEROLOGIA("Gastroenterología"),
    ENDOCRINOLOGIA("Endocrinología"),
    NEUMOLOGIA("Neumología"),
    PSIQUIATRIA("Psiquiatría"),
    RADIOLOGIA("Radiología"),
    ANESTESIOLOGIA("Anestesiología"),
    CIRUGIA_GENERAL("Cirugía General"),
    MEDICINA_INTERNA("Medicina Interna"),
    OTORRINOLARINGOLOGIA("Otorrinolaringología"),
    GENERAL("General");

    private final String displayName;

    MedicalSpecialty(String displayName) {
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