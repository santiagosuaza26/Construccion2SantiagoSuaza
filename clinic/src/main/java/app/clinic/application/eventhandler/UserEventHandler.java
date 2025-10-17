package app.clinic.application.eventhandler;

import org.springframework.stereotype.Service;

import app.clinic.domain.event.DomainEvent;
import app.clinic.domain.event.DomainEventListener;
import app.clinic.domain.event.UserCreatedEvent;
import app.clinic.domain.model.UserId;

/**
 * Manejador de eventos de usuario en la capa de aplicación.
 * Responde a eventos del dominio y ejecuta lógica de aplicación correspondiente.
 */
@Service
public class UserEventHandler implements DomainEventListener {

    @Override
    public void handle(DomainEvent event) {
        if (event instanceof UserCreatedEvent userCreatedEvent) {
            handleUserCreatedEvent(userCreatedEvent);
        }
    }

    @Override
    public String[] getSupportedEventTypes() {
        return new String[]{"USER_CREATED"};
    }

    /**
     * Maneja el evento de creación de usuario.
     */
    private void handleUserCreatedEvent(UserCreatedEvent event) {
        System.out.println("📧 Application Layer: Handling UserCreatedEvent");
        System.out.println("   User ID: " + event.getUserId());
        System.out.println("   Occurred On: " + event.getOccurredOn());

        // Aquí se podría implementar lógica como:
        // - Enviar email de bienvenida
        // - Crear registro de auditoría
        // - Notificar a otros sistemas
        // - Actualizar cachés
        // - Generar reportes

        try {
            // Simular envío de email de bienvenida
            sendWelcomeEmail(event.getUserId());

            // Simular registro de auditoría
            logUserCreation(event.getUserId());

            System.out.println("✅ UserCreatedEvent handled successfully");
        } catch (Exception e) {
            System.err.println("❌ Error handling UserCreatedEvent: " + e.getMessage());
        }
    }

    /**
     * Envía email de bienvenida al nuevo usuario.
     */
    private void sendWelcomeEmail(UserId userId) {
        // Implementación de envío de email
        System.out.println("   📧 Welcome email sent to user: " + userId);
    }

    /**
     * Registra la creación del usuario en el sistema de auditoría.
     */
    private void logUserCreation(UserId userId) {
        // Implementación de logging de auditoría
        System.out.println("   📋 User creation logged: " + userId);
    }
}