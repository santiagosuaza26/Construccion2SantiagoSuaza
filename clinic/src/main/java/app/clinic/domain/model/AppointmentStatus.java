package app.clinic.domain.model;

/**
 * Enumeration representing the status of an appointment.
 */
public enum AppointmentStatus {
    PROGRAMADA("Programada"),
    CONFIRMADA("Confirmada"),
    EN_CURSO("En Curso"),
    COMPLETADA("Completada"),
    CANCELADA("Cancelada"),
    NO_ASISTIO("No Asistió");

    private final String displayName;

    AppointmentStatus(String displayName) {
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