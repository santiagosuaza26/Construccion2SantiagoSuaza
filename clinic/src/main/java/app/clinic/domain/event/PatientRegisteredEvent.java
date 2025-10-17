package app.clinic.domain.event;

import java.time.LocalDateTime;
import java.util.UUID;

import app.clinic.domain.model.PatientId;

/**
 * Evento del dominio que se publica cuando un paciente es registrado.
 * Este evento es independiente de frameworks externos y representa
 * un hecho importante en el dominio médico.
 */
public class PatientRegisteredEvent implements DomainEvent {

    private final String eventId;
    private final PatientId patientId;
    private final LocalDateTime occurredOn;

    public PatientRegisteredEvent(PatientId patientId) {
        this.eventId = UUID.randomUUID().toString();
        this.patientId = patientId;
        this.occurredOn = LocalDateTime.now();
    }

    @Override
    public String getEventId() {
        return eventId;
    }

    @Override
    public LocalDateTime getOccurredOn() {
        return occurredOn;
    }

    @Override
    public String getEventType() {
        return "PATIENT_REGISTERED";
    }

    @Override
    public String getVersion() {
        return "1.0";
    }

    public PatientId getPatientId() {
        return patientId;
    }

    @Override
    public String toString() {
        return String.format("PatientRegisteredEvent{eventId=%s, patientId=%s, occurredOn=%s}",
                           eventId, patientId, occurredOn);
    }
}