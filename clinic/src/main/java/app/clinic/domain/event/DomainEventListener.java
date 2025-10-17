package app.clinic.domain.event;

/**
 * Interfaz para listeners de eventos del dominio.
 * Los componentes que necesiten reaccionar a eventos del dominio
 * deben implementar esta interfaz.
 */
public interface DomainEventListener {

    /**
     * Maneja un evento del dominio.
     *
     * @param event El evento del dominio a manejar
     */
    void handle(DomainEvent event);

    /**
     * Obtiene los tipos de eventos que este listener puede manejar.
     * Por defecto, maneja todos los eventos.
     *
     * @return Array de tipos de eventos que puede manejar
     */
    default String[] getSupportedEventTypes() {
        return new String[]{"*"}; // Maneja todos los eventos por defecto
    }

    /**
     * Determina si este listener puede manejar el tipo de evento especificado.
     *
     * @param eventType El tipo de evento a verificar
     * @return true si puede manejar el evento, false en caso contrario
     */
    default boolean canHandle(String eventType) {
        String[] supportedTypes = getSupportedEventTypes();
        for (String supportedType : supportedTypes) {
            if ("*".equals(supportedType) || supportedType.equals(eventType)) {
                return true;
            }
        }
        return false;
    }
}