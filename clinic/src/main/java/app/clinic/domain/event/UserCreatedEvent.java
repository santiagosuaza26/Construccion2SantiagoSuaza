package app.clinic.domain.event;

import java.time.LocalDateTime;
import java.util.UUID;

import app.clinic.domain.model.UserId;

/**
 * Evento del dominio que se publica cuando un usuario es creado.
 * Este evento representa un cambio importante en el estado del dominio médico.
 */
public class UserCreatedEvent implements DomainEvent {

    private final String eventId;
    private final UserId userId;
    private final LocalDateTime occurredOn;

    public UserCreatedEvent(UserId userId) {
        this.eventId = UUID.randomUUID().toString();
        this.userId = userId;
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
        return "USER_CREATED";
    }

    @Override
    public String getVersion() {
        return "1.0";
    }

    public UserId getUserId() {
        return userId;
    }

    @Override
    public String toString() {
        return String.format("UserCreatedEvent{eventId=%s, userId=%s, occurredOn=%s}",
                           eventId, userId, occurredOn);
    }
}