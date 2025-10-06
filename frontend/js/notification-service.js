/**
 * Servicio de notificaciones
 * Maneja la creación y visualización de notificaciones para el usuario
 */
class NotificationService {
    constructor() {
        this.container = null;
        this.notifications = [];
        this.createContainer();
    }

    /**
     * Crea el contenedor de notificaciones
     */
    createContainer() {
        this.container = document.getElementById('notification-container');
        if (!this.container) {
            this.container = document.createElement('div');
            this.container.id = 'notification-container';
            this.container.className = 'notification-container';
            document.body.appendChild(this.container);
        }
    }

    /**
     * Muestra una notificación
     */
    show(message, type = 'info', duration = 5000) {
        const notification = this.createNotification(message, type);
        this.container.appendChild(notification);

        // Auto-remover después del tiempo especificado
        if (duration > 0) {
            setTimeout(() => {
                this.remove(notification);
            }, duration);
        }

        return notification;
    }

    /**
     * Crea una notificación
     */
    createNotification(message, type) {
        const notification = document.createElement('div');
        notification.className = `notification notification-${type}`;

        const icon = this.getIconForType(type);

        notification.innerHTML = `
            <div class="notification-content">
                <i class="${icon}"></i>
                <span class="notification-message">${message}</span>
                <button class="notification-close" onclick="notificationService.remove(this.parentElement)">
                    <i class="fas fa-times"></i>
                </button>
            </div>
        `;

        // Animación de entrada
        setTimeout(() => {
            notification.classList.add('show');
        }, 10);

        this.notifications.push(notification);
        return notification;
    }

    /**
     * Remueve una notificación
     */
    remove(notification) {
        if (typeof notification === 'string') {
            notification = document.getElementById(notification);
        }

        if (notification && notification.parentElement) {
            notification.classList.remove('show');
            setTimeout(() => {
                if (notification.parentElement) {
                    notification.parentElement.removeChild(notification);
                }
                this.notifications = this.notifications.filter(n => n !== notification);
            }, 300);
        }
    }

    /**
     * Limpia todas las notificaciones
     */
    clear() {
        this.notifications.forEach(notification => {
            this.remove(notification);
        });
    }

    /**
     * Obtiene el ícono según el tipo de notificación
     */
    getIconForType(type) {
        const icons = {
            'success': 'fas fa-check-circle',
            'error': 'fas fa-exclamation-circle',
            'warning': 'fas fa-exclamation-triangle',
            'info': 'fas fa-info-circle'
        };

        return icons[type] || icons.info;
    }

    /**
     * Muestra una notificación de éxito
     */
    success(message, duration) {
        return this.show(message, 'success', duration);
    }

    /**
     * Muestra una notificación de error
     */
    error(message, duration) {
        return this.show(message, 'error', duration);
    }

    /**
     * Muestra una notificación de advertencia
     */
    warning(message, duration) {
        return this.show(message, 'warning', duration);
    }

    /**
     * Muestra una notificación informativa
     */
    info(message, duration) {
        return this.show(message, 'info', duration);
    }
}

// Crear instancia global del servicio de notificaciones
window.notificationService = new NotificationService();