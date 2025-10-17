package app.clinic.domain.event;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.stereotype.Component;

/**
 * Publisher de eventos del dominio.
 * Maneja la publicación asíncrona de eventos del dominio sin dependencias externas.
 * Implementa el patrón Observer para comunicación entre componentes del dominio.
 */
@Component
public class DomainEventPublisher {

    private final List<DomainEventListener> listeners = new ArrayList<>();
    private final ExecutorService executor = Executors.newCachedThreadPool(r -> {
        Thread thread = new Thread(r, "domain-event-" + System.currentTimeMillis());
        thread.setDaemon(true);
        return thread;
    });

    /**
     * Suscribe un listener para recibir eventos del dominio.
     */
    public void subscribe(DomainEventListener listener) {
        synchronized (listeners) {
            listeners.add(listener);
        }
    }

    /**
     * Desuscribe un listener de eventos del dominio.
     */
    public void unsubscribe(DomainEventListener listener) {
        synchronized (listeners) {
            listeners.remove(listener);
        }
    }

    /**
     * Publica un evento del dominio de manera asíncrona.
     */
    public void publish(DomainEvent event) {
        List<DomainEventListener> currentListeners;
        synchronized (listeners) {
            currentListeners = new ArrayList<>(listeners);
        }

        // Publicar evento de manera asíncrona a todos los listeners
        for (DomainEventListener listener : currentListeners) {
            CompletableFuture.runAsync(() -> {
                try {
                    listener.handle(event);
                } catch (Exception e) {
                    System.err.println("Error handling domain event: " + e.getMessage());
                    e.printStackTrace();
                }
            }, executor);
        }
    }

    /**
     * Publica un evento del dominio de manera síncrona.
     */
    public void publishSync(DomainEvent event) {
        List<DomainEventListener> currentListeners;
        synchronized (listeners) {
            currentListeners = new ArrayList<>(listeners);
        }

        // Publicar evento de manera síncrona
        for (DomainEventListener listener : currentListeners) {
            try {
                listener.handle(event);
            } catch (Exception e) {
                System.err.println("Error handling domain event: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    /**
     * Obtiene el número de listeners suscritos.
     */
    public int getListenerCount() {
        synchronized (listeners) {
            return listeners.size();
        }
    }

    /**
     * Limpia todos los listeners (útil para testing).
     */
    public void clear() {
        synchronized (listeners) {
            listeners.clear();
        }
    }

    /**
     * Cierra el publisher y libera recursos.
     */
    public void shutdown() {
        executor.shutdown();
    }
}