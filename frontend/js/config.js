/**
 * Archivo de configuración del frontend
 * Centraliza todas las configuraciones de conexión y ajustes
 */

// Configuración de la API
window.APP_CONFIG = {
    // URL base de la API backend
    API_BASE_URL: 'http://localhost:8080/api',

    // Configuración de autenticación
    AUTH: {
        TOKEN_KEY: 'authToken',
        USER_KEY: 'currentUser',
        TOKEN_EXPIRY: 24 * 60 * 60 * 1000, // 24 horas en milisegundos
    },

    // Configuración de UI
    UI: {
        NOTIFICATION_DURATION: 5000,
        PAGINATION_ITEMS_PER_PAGE: 10,
        DEBOUNCE_DELAY: 300,
        THEME: 'light'
    },

    // Configuración de validación
    VALIDATION: {
        PASSWORD_MIN_LENGTH: 8,
        PHONE_LENGTH: 10,
        CEDULA_MIN_LENGTH: 8,
        CEDULA_MAX_LENGTH: 10,
        USERNAME_MAX_LENGTH: 15,
        ADDRESS_MAX_LENGTH: 30,
        NAME_MAX_LENGTH: 100
    },

    // Configuración de roles y permisos
    ROLES: {
        HUMAN_RESOURCES: {
            name: 'Recursos Humanos',
            permissions: ['manage-users', 'view-patients', 'manage-reports'],
            color: 'primary'
        },
        ADMINISTRATIVE_STAFF: {
            name: 'Personal Administrativo',
            permissions: ['register-patients', 'manage-appointments', 'manage-billing'],
            color: 'info'
        },
        DOCTOR: {
            name: 'Médico',
            permissions: ['view-patients', 'manage-medical-records', 'manage-orders'],
            color: 'success'
        },
        NURSE: {
            name: 'Enfermera',
            permissions: ['view-patients', 'manage-patient-visits', 'manage-vital-signs'],
            color: 'warning'
        },
        INFORMATION_SUPPORT: {
            name: 'Soporte de Información',
            permissions: ['manage-inventory', 'system-maintenance'],
            color: 'secondary'
        }
    },

    // Configuración de endpoints críticos
    ENDPOINTS: {
        HEALTH_CHECK: '/public/health',
        LOGIN: '/auth/login',
        USERS: '/users',
        PATIENTS: '/patients',
        MEDICAL_RECORDS: '/medical-records',
        ORDERS: '/orders',
        PATIENT_VISITS: '/patient-visits',
        BILLING: '/billing',
        INVENTORY: '/inventory',
        APPOINTMENTS: '/appointments'
    },

    // Configuración de desarrollo
    DEVELOPMENT: {
        ENABLE_LOGS: true,
        MOCK_DATA: false,
        DEBUG_MODE: true,
        AUTO_LOGIN: false // Para desarrollo rápido
    }
};

// Función para obtener configuración
window.getConfig = function(key) {
    return window.APP_CONFIG[key];
};

// Función para verificar conexión con backend
window.checkBackendConnection = async function() {
    try {
        const response = await fetch(`${window.APP_CONFIG.API_BASE_URL}${window.APP_CONFIG.ENDPOINTS.HEALTH_CHECK}`, {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json'
            }
        });

        if (response.ok) {
            const data = await response.json();
            console.log('✅ Backend conectado:', data);
            return { connected: true, data };
        } else {
            console.warn('⚠️ Backend respondió con error:', response.status);
            return { connected: false, error: response.status };
        }
    } catch (error) {
        console.error('❌ Error conectando con backend:', error);
        return { connected: false, error: error.message };
    }
};

// Función para inicializar configuración
window.initializeConfig = function() {
    // Verificar conexión con backend al cargar
    setTimeout(async () => {
        const connectionStatus = await window.checkBackendConnection();

        if (window.notificationService) {
            if (connectionStatus.connected) {
                window.notificationService.success('Conexión con backend establecida');
            } else {
                window.notificationService.warning('No se pudo conectar con el backend. Algunas funciones podrían no estar disponibles.');
            }
        }
    }, 1000);

    // Configurar logs de desarrollo
    if (window.APP_CONFIG.DEVELOPMENT.ENABLE_LOGS) {
        console.log('🔧 Configuración cargada:', window.APP_CONFIG);
    }

    // Auto-login para desarrollo (si está habilitado)
    if (window.APP_CONFIG.DEVELOPMENT.AUTO_LOGIN && window.authService) {
        console.log('🚀 Auto-login habilitado para desarrollo');
        // Puedes configurar credenciales específicas aquí para desarrollo rápido
    }
};

// Inicializar configuración cuando el DOM esté listo
if (document.readyState === 'loading') {
    document.addEventListener('DOMContentLoaded', window.initializeConfig);
} else {
    window.initializeConfig();
}