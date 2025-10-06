/**
 * Archivo principal de la aplicación
 * Inicializa todos los servicios y componentes
 */

// Esperar a que el DOM esté completamente cargado
document.addEventListener('DOMContentLoaded', function() {
    console.log('🏥 Iniciando Sistema de Gestión Clínica');

    // Inicializar servicios
    initializeServices();

    // Inicializar aplicación
    initializeApp();
});

/**
 * Inicializa todos los servicios
 */
function initializeServices() {
    console.log('🔧 Inicializando servicios...');

    // Los servicios ya están creados como instancias globales en sus respectivos archivos
    // auth-service.js crea window.authService
    // notification-service.js crea window.notificationService
    // api-service.js crea las APIs globales

    console.log('✅ Servicios inicializados');
}

/**
 * Inicializa la aplicación
 */
function initializeApp() {
    console.log('🚀 Inicializando aplicación...');

    // Inicializar los event listeners de autenticación
    if (window.authService) {
        window.authService.initializeAuthListeners();
    }

    // Verificar estado de autenticación
    checkAuthentication();

    // Configurar navegación basada en roles
    setupRoleBasedNavigation();

    // Inicializar funcionalidades específicas según el rol
    initializeRoleSpecificFeatures();

    console.log('✅ Aplicación inicializada');
}

/**
 * Verifica el estado de autenticación del usuario
 */
function checkAuthentication() {
    if (window.authService) {
        window.authService.checkAuthentication();
    }
}

/**
 * Configura la navegación basada en el rol del usuario
 */
function setupRoleBasedNavigation() {
    if (window.authService) {
        window.authService.setupRoleBasedNavigation();
    }
}

/**
 * Inicializa funcionalidades específicas según el rol
 */
function initializeRoleSpecificFeatures() {
    const currentUser = window.authService?.getCurrentUser();

    if (!currentUser) {
        return;
    }

    console.log(`👤 Inicializando funcionalidades para rol: ${currentUser.role}`);

    switch (currentUser.role) {
        case 'HUMAN_RESOURCES':
            initializeHRFeatures();
            break;
        case 'ADMINISTRATIVE_STAFF':
            initializeAdminFeatures();
            break;
        case 'DOCTOR':
            initializeDoctorFeatures();
            break;
        case 'NURSE':
            initializeNurseFeatures();
            break;
        case 'SUPPORT_STAFF':
            initializeSupportFeatures();
            break;
        default:
            console.warn(`⚠️ Rol desconocido: ${currentUser.role}`);
    }
}

/**
 * Inicializa funcionalidades de Recursos Humanos
 */
function initializeHRFeatures() {
    console.log('👥 Inicializando funcionalidades de Recursos Humanos');

    // Crear instancia del servicio de gestión de usuarios si no existe
    if (!window.userManagementService && window.UserManagementService) {
        window.userManagementService = new UserManagementService();
    }

    // Cargar vista de usuarios por defecto
    setTimeout(() => {
        if (window.userManagementService) {
            window.userManagementService.loadUsersList();
        }
    }, 100);
}

/**
 * Inicializa funcionalidades de Personal Administrativo
 */
function initializeAdminFeatures() {
    console.log('🏢 Inicializando funcionalidades de Personal Administrativo');

    // Crear instancia del servicio de gestión de pacientes si no existe
    if (!window.patientManagementService && window.PatientManagementService) {
        window.patientManagementService = new PatientManagementService();
    }

    // Cargar vista de pacientes por defecto
    setTimeout(() => {
        if (window.patientManagementService) {
            window.patientManagementService.loadPatientsList();
        }
    }, 100);
}

/**
 * Inicializa funcionalidades de Médicos
 */
function initializeDoctorFeatures() {
    console.log('👨‍⚕️ Inicializando funcionalidades de Médicos');

    // Crear instancia del servicio de historias clínicas si no existe
    if (!window.medicalRecordsService && window.MedicalRecordsService) {
        window.medicalRecordsService = new MedicalRecordsService();
    }

    // Cargar vista de pacientes por defecto
    setTimeout(() => {
        if (window.medicalRecordsService) {
            window.medicalRecordsService.loadPatientsList();
        }
    }, 100);
}

/**
 * Inicializa funcionalidades de Enfermeras
 */
function initializeNurseFeatures() {
    console.log('👩‍⚕️ Inicializando funcionalidades de Enfermeras');

    // Crear instancia del servicio de visitas de pacientes si no existe
    if (!window.patientVisitService && window.PatientVisitService) {
        window.patientVisitService = new PatientVisitService();
    }

    // Cargar vista de pacientes por defecto
    setTimeout(() => {
        if (window.patientVisitService) {
            window.patientVisitService.loadPatientsList();
        }
    }, 100);
}

/**
 * Inicializa funcionalidades de Soporte de Información
 */
function initializeSupportFeatures() {
    console.log('🔧 Inicializando funcionalidades de Soporte de Información');

    // Crear instancia del servicio de inventario si no existe
    if (!window.inventoryService && window.InventoryService) {
        window.inventoryService = new InventoryService();
    }

    // Cargar vista de inventario por defecto
    setTimeout(() => {
        if (window.inventoryService) {
            window.inventoryService.loadInventoryList();
        }
    }, 100);
}

/**
 * Función global para mostrar notificaciones
 */
window.showNotification = function(message, type = 'info', duration = 5000) {
    if (window.notificationService) {
        return window.notificationService.show(message, type, duration);
    }
};

/**
 * Función global para mostrar errores
 */
window.showError = function(message, duration = 7000) {
    return window.showNotification(message, 'error', duration);
};

/**
 * Función global para mostrar mensajes de éxito
 */
window.showSuccess = function(message, duration = 5000) {
    return window.showNotification(message, 'success', duration);
};

/**
 * Función global para mostrar advertencias
 */
window.showWarning = function(message, duration = 6000) {
    return window.showNotification(message, 'warning', duration);
};

/**
 * Función global para mostrar información
 */
window.showInfo = function(message, duration = 5000) {
    return window.showNotification(message, 'info', duration);
};

/**
 * Función utilitaria para formatear fechas
 */
window.formatDate = function(dateString) {
    if (!dateString) return '';

    const date = new Date(dateString);
    return date.toLocaleDateString('es-CO', {
        year: 'numeric',
        month: '2-digit',
        day: '2-digit'
    });
};

/**
 * Función utilitaria para formatear números de teléfono
 */
window.formatPhoneNumber = function(phoneNumber) {
    if (!phoneNumber) return '';

    const cleaned = phoneNumber.replace(/\D/g, '');
    if (cleaned.length === 10) {
        return `${cleaned.slice(0, 3)}-${cleaned.slice(3, 6)}-${cleaned.slice(6)}`;
    }

    return phoneNumber;
};

/**
 * Función utilitaria para formatear números de cédula
 */
window.formatCedula = function(cedula) {
    if (!cedula) return '';

    const cleaned = cedula.replace(/\D/g, '');
    if (cleaned.length >= 8) {
        return `${cleaned.slice(0, cleaned.length - 1)}-${cleaned.slice(-1)}`;
    }

    return cedula;
};

/**
 * Función utilitaria para validar email
 */
window.isValidEmail = function(email) {
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    return emailRegex.test(email);
};

/**
 * Función utilitaria para validar teléfono
 */
window.isValidPhone = function(phone) {
    const phoneRegex = /^\d{10}$/;
    return phoneRegex.test(phone.replace(/\D/g, ''));
};

/**
 * Función utilitaria para validar cédula
 */
window.isValidCedula = function(cedula) {
    const cedulaRegex = /^\d{8,10}$/;
    return cedulaRegex.test(cedula.replace(/\D/g, ''));
};

/**
 * Función utilitaria para debounce
 */
window.debounce = function(func, wait) {
    let timeout;
    return function executedFunction(...args) {
        const later = () => {
            clearTimeout(timeout);
            func(...args);
        };
        clearTimeout(timeout);
        timeout = setTimeout(later, wait);
    };
};

/**
 * Función utilitaria para throttle
 */
window.throttle = function(func, limit) {
    let inThrottle;
    return function() {
        const args = arguments;
        const context = this;
        if (!inThrottle) {
            func.apply(context, args);
            inThrottle = true;
            setTimeout(() => inThrottle = false, limit);
        }
    };
};

/**
 * Manejo global de errores
 */
window.addEventListener('error', function(event) {
    console.error('Error global:', event.error);

    if (window.notificationService) {
        // Proporcionar mensajes más específicos según el tipo de error
        let errorMessage = 'Ha ocurrido un error inesperado. Por favor recargue la página.';

        if (event.error && event.error.message) {
            if (event.error.message.includes('fetch') || event.error.message.includes('NetworkError')) {
                errorMessage = 'Error de conexión. Verifique su conexión a internet y que el backend esté corriendo.';
            } else if (event.error.message.includes('CORS')) {
                errorMessage = 'Error de configuración del servidor. Contacte al administrador.';
            } else if (event.error.message.includes('401') || event.error.message.includes('403')) {
                errorMessage = 'Sesión expirada. Por favor inicie sesión nuevamente.';
            }
        }

        window.notificationService.error(errorMessage);
    }
});

/**
 * Manejo de errores de promesas no manejadas
 */
window.addEventListener('unhandledrejection', function(event) {
    console.error('Promesa rechazada no manejada:', event.reason);

    if (window.notificationService) {
        let errorMessage = 'Error de conexión. Verifique su conexión a internet.';

        if (event.reason && event.reason.message) {
            if (event.reason.message.includes('fetch') || event.reason.message.includes('NetworkError')) {
                errorMessage = 'Error de conexión con el servidor. Verifique que el backend esté corriendo en el puerto 8080.';
            } else if (event.reason.message.includes('CORS')) {
                errorMessage = 'Error de configuración del servidor. Contacte al administrador.';
            } else if (event.reason.message.includes('timeout')) {
                errorMessage = 'Tiempo de espera agotado. El servidor podría estar sobrecargado.';
            } else {
                errorMessage = event.reason.message;
            }
        }

        window.notificationService.error(errorMessage);
    }
});

console.log('🏥 Sistema de Gestión Clínica cargado exitosamente');