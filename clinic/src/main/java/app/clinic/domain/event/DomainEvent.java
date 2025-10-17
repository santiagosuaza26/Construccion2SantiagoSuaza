package app.clinic.domain.event;

import java.time.LocalDateTime;

/**
 * Interfaz base para todos los eventos del dominio.
 * Los eventos del dominio representan hechos importantes que ocurren
 * en el negocio y que otros componentes pueden necesitar saber.
 */
public interface DomainEvent {

    /**
     * Obtiene la identificación única del evento.
     */
    String getEventId();

    /**
     * Obtiene la fecha y hora en que ocurrió el evento.
     */
    LocalDateTime getOccurredOn();

    /**
     * Obtiene el tipo del evento.
     */
    String getEventType();

    /**
     * Obtiene la versión del evento para manejo de versiones.
     */
    String getVersion();
}