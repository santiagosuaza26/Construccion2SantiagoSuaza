/**
 * Sistema de Gestión Clínica CS2 - Configuración de API
 * Manejo de comunicaciones con el backend
 */

// Estado global de la aplicación
let currentUser = null;
let authToken = localStorage.getItem('authToken');

// Configuración mejorada para desarrollo local
const API_CONFIG = {
    BASE_URL: window.location.origin + '/api',
    ENDPOINTS: {
        // Autenticación
        LOGIN: '/auth/login',
        LOGOUT: '/auth/logout',
        ME: '/auth/me',
        HEALTH: '/auth/health',

        // Pacientes
        PATIENTS: '/patients',
        PATIENT_BY_ID: '/patients',

        // Historia Clínica
        CLINICAL_HISTORY: '/clinical-history',

        // Órdenes Médicas
        ORDERS: '/medical/orders',

        // Signos Vitales
        VITAL_SIGNS: '/medical/vital-signs',

        // Usuarios
        USERS: '/users',

        // Inventario
        INVENTORY: '/inventory',

        // Facturación
        BILLING: '/billing',
        INVOICES: '/billing/invoices',

        // Reportes
        REPORTS: '/reports'
    },
    HEADERS: {
        'Content-Type': 'application/json',
        'Accept': 'application/json'
    },
    // Timeout para peticiones (en ms)
    TIMEOUT: 10000,
    // Número máximo de reintentos
    MAX_RETRIES: 3,
    // Delay entre reintentos (en ms)
    RETRY_DELAY: 1000
};

/**
 * Clase para manejo de API con mejoras de conexión
 */
class ApiClient {
    constructor() {
        this.baseURL = API_CONFIG.BASE_URL;
        this.defaultHeaders = { ...API_CONFIG.HEADERS };
        this.timeout = API_CONFIG.TIMEOUT;
        this.maxRetries = API_CONFIG.MAX_RETRIES;
        this.retryDelay = API_CONFIG.RETRY_DELAY;
    }

    /**
     * Realiza una petición HTTP con mejoras de manejo de errores
     */
    async request(endpoint, options = {}, retryCount = 0) {
        const url = `${this.baseURL}${endpoint}`;
        const config = {
            headers: {
                ...this.defaultHeaders,
                ...options.headers
            },
            ...options
        };

        // Agregar token de autenticación si existe
        if (authToken) {
            config.headers['User-ID'] = currentUser?.id || 'unknown';
        }

        // Configurar timeout
        const controller = new AbortController();
        const timeoutId = setTimeout(() => controller.abort(), this.timeout);
        config.signal = controller.signal;

        try {
            console.log(`API Request: ${options.method || 'GET'} ${url} (intento ${retryCount + 1})`);

            const response = await fetch(url, config);
            clearTimeout(timeoutId);

            // Si la respuesta no es JSON, devolver como texto
            const contentType = response.headers.get('content-type');
            let data;

            if (contentType && contentType.includes('application/json')) {
                data = await response.json();
            } else {
                data = await response.text();
            }

            if (!response.ok) {
                throw new ApiError(response.status, data.message || data || 'Error en la petición', data);
            }

            return data;
        } catch (error) {
            clearTimeout(timeoutId);

            if (error instanceof ApiError) {
                throw error;
            }

            // Si es error de conexión y no hemos excedido los reintentos, intentar de nuevo
            if (retryCount < this.maxRetries && this.isRetryableError(error)) {
                console.log(`Reintentando petición (${retryCount + 1}/${this.maxRetries})...`);
                await this.delay(this.retryDelay * (retryCount + 1));
                return this.request(endpoint, options, retryCount + 1);
            }

            console.error('Error de conexión:', error);
            throw new ApiError(0, 'Error de conexión con el servidor. Verifique que el servidor esté ejecutándose.');
        }
    }

    /**
     * Determina si un error es reintentable
     */
    isRetryableError(error) {
        return error.name === 'AbortError' ||
               error.name === 'TypeError' ||
               error.message.includes('fetch');
    }

    /**
     * Delay utility
     */
    delay(ms) {
        return new Promise(resolve => setTimeout(resolve, ms));
    }

    /**
     * GET request
     */
    async get(endpoint, params = {}) {
        const queryString = new URLSearchParams(params).toString();
        const url = queryString ? `${endpoint}?${queryString}` : endpoint;

        return this.request(url, {
            method: 'GET'
        });
    }

    /**
     * POST request
     */
    async post(endpoint, data = {}) {
        return this.request(endpoint, {
            method: 'POST',
            body: JSON.stringify(data)
        });
    }

    /**
     * PUT request
     */
    async put(endpoint, data = {}) {
        return this.request(endpoint, {
            method: 'PUT',
            body: JSON.stringify(data)
        });
    }

    /**
     * DELETE request
     */
    async delete(endpoint) {
        return this.request(endpoint, {
            method: 'DELETE'
        });
    }
}

/**
 * Error personalizado para manejo de errores de API
 */
class ApiError extends Error {
    constructor(status, message, data = null) {
        super(message);
        this.name = 'ApiError';
        this.status = status;
        this.data = data;
    }
}

// Instancia global del cliente API
const apiClient = new ApiClient();

/**
 * Servicios de API por módulo
 */

// Servicio de autenticación
const AuthAPI = {
    async login(credentials) {
        const response = await apiClient.post(API_CONFIG.ENDPOINTS.LOGIN, credentials);
        return response.data;
    },

    async logout() {
        const response = await apiClient.post(API_CONFIG.ENDPOINTS.LOGOUT);
        return response;
    },

    async getCurrentUser() {
        const response = await apiClient.get(API_CONFIG.ENDPOINTS.ME);
        return response.data;
    },

    async healthCheck() {
        const response = await apiClient.get(API_CONFIG.ENDPOINTS.HEALTH);
        return response;
    }
};

// Servicio de pacientes
const PatientAPI = {
    async getAll() {
        const response = await apiClient.get(API_CONFIG.ENDPOINTS.PATIENTS);
        return response.data || [];
    },

    async getById(idCard) {
        const response = await apiClient.get(`${API_CONFIG.ENDPOINTS.PATIENT_BY_ID}/${idCard}`);
        return response.data;
    },

    async create(patientData) {
        const response = await apiClient.post(API_CONFIG.ENDPOINTS.PATIENTS, patientData);
        return response.data;
    },

    async update(idCard, patientData) {
        const response = await apiClient.put(`${API_CONFIG.ENDPOINTS.PATIENT_BY_ID}/${idCard}`, patientData);
        return response.data;
    },

    async delete(idCard) {
        const response = await apiClient.delete(`${API_CONFIG.ENDPOINTS.PATIENT_BY_ID}/${idCard}`);
        return response;
    }
};

// Servicio de historia clínica
const ClinicalHistoryAPI = {
    async getByPatientId(patientId) {
        const response = await apiClient.get(`${API_CONFIG.ENDPOINTS.CLINICAL_HISTORY}/${patientId}`);
        return response.data || {};
    },

    async addEntry(patientId, entryData) {
        const response = await apiClient.post(`${API_CONFIG.ENDPOINTS.CLINICAL_HISTORY}/${patientId}`, entryData);
        return response.data;
    },

    async updateEntry(patientId, date, entryData) {
        const response = await apiClient.put(`${API_CONFIG.ENDPOINTS.CLINICAL_HISTORY}/${patientId}/${date}`, entryData);
        return response.data;
    }
};

// Servicio de órdenes médicas
const OrdersAPI = {
    async getAll() {
        const response = await apiClient.get(API_CONFIG.ENDPOINTS.ORDERS);
        return response.data || [];
    },

    async getById(orderId) {
        const response = await apiClient.get(`${API_CONFIG.ENDPOINTS.ORDERS}/${orderId}`);
        return response.data;
    },

    async create(orderData) {
        const response = await apiClient.post(API_CONFIG.ENDPOINTS.ORDERS, orderData);
        return response.data;
    },

    async updateStatus(orderId, status) {
        const response = await apiClient.put(`${API_CONFIG.ENDPOINTS.ORDERS}/${orderId}/status`, { status });
        return response.data;
    }
};

// Servicio de signos vitales
const VitalSignsAPI = {
    async getByPatient(patientId) {
        const response = await apiClient.get(`${API_CONFIG.ENDPOINTS.VITAL_SIGNS}/${patientId}`);
        return response.data || [];
    },

    async add(patientId, vitalSignsData) {
        const response = await apiClient.post(`${API_CONFIG.ENDPOINTS.VITAL_SIGNS}/${patientId}`, vitalSignsData);
        return response.data;
    }
};

// Servicio de usuarios
const UsersAPI = {
    async getAll() {
        const response = await apiClient.get(API_CONFIG.ENDPOINTS.USERS);
        return response.data || [];
    },

    async getById(userId) {
        const response = await apiClient.get(`${API_CONFIG.ENDPOINTS.USERS}/${userId}`);
        return response.data;
    },

    async create(userData) {
        const response = await apiClient.post(API_CONFIG.ENDPOINTS.USERS, userData);
        return response.data;
    },

    async update(userId, userData) {
        const response = await apiClient.put(`${API_CONFIG.ENDPOINTS.USERS}/${userId}`, userData);
        return response.data;
    },

    async delete(userId) {
        const response = await apiClient.delete(`${API_CONFIG.ENDPOINTS.USERS}/${userId}`);
        return response;
    }
};

// Servicio de inventario
const InventoryAPI = {
    async getAll() {
        const response = await apiClient.get(API_CONFIG.ENDPOINTS.INVENTORY);
        return response.data || [];
    },

    async getByCategory(category) {
        const response = await apiClient.get(`${API_CONFIG.ENDPOINTS.INVENTORY}/category/${category}`);
        return response.data || [];
    },

    async create(itemData) {
        const response = await apiClient.post(API_CONFIG.ENDPOINTS.INVENTORY, itemData);
        return response.data;
    },

    async update(itemId, itemData) {
        const response = await apiClient.put(`${API_CONFIG.ENDPOINTS.INVENTORY}/${itemId}`, itemData);
        return response.data;
    },

    async delete(itemId) {
        const response = await apiClient.delete(`${API_CONFIG.ENDPOINTS.INVENTORY}/${itemId}`);
        return response;
    }
};

// Servicio de facturación
const BillingAPI = {
    async getAll() {
        const response = await apiClient.get(API_CONFIG.ENDPOINTS.INVOICES);
        return response.data || [];
    },

    async getById(invoiceId) {
        const response = await apiClient.get(`${API_CONFIG.ENDPOINTS.INVOICES}/${invoiceId}`);
        return response.data;
    },

    async generateInvoice(invoiceData) {
        const response = await apiClient.post(API_CONFIG.ENDPOINTS.INVOICES, invoiceData);
        return response.data;
    },

    async getPatientInvoices(patientId) {
        const response = await apiClient.get(`${API_CONFIG.ENDPOINTS.INVOICES}/patient/${patientId}`);
        return response.data || [];
    }
};

// Servicio de reportes
const ReportsAPI = {
    async generatePatientReport(filters = {}) {
        const response = await apiClient.post(`${API_CONFIG.ENDPOINTS.REPORTS}/patients`, filters);
        return response.data;
    },

    async generateFinancialReport(filters = {}) {
        const response = await apiClient.post(`${API_CONFIG.ENDPOINTS.REPORTS}/financial`, filters);
        return response.data;
    },

    async generateOrdersReport(filters = {}) {
        const response = await apiClient.post(`${API_CONFIG.ENDPOINTS.REPORTS}/orders`, filters);
        return response.data;
    }
};

/**
 * Funciones de utilidad para manejo de respuestas
 */
const ApiUtils = {
    /**
     * Maneja errores de API y muestra mensajes apropiados
     */
    handleError(error, defaultMessage = 'Ha ocurrido un error') {
        console.error('API Error:', error);

        let message = defaultMessage;
        let type = 'error';

        if (error instanceof ApiError) {
            switch (error.status) {
                case 401:
                    message = 'No tiene permisos para realizar esta acción';
                    // Redirigir al login si es error de autenticación
                    if (window.location.hash !== '#login') {
                        window.location.hash = 'login';
                        showLoginScreen();
                    }
                    break;
                case 403:
                    message = 'Acceso denegado';
                    break;
                case 404:
                    message = 'Recurso no encontrado';
                    break;
                case 422:
                    message = 'Datos inválidos';
                    break;
                case 500:
                    message = 'Error interno del servidor';
                    break;
                default:
                    message = error.message || defaultMessage;
            }
        } else if (error.message === 'Error de conexión con el servidor') {
            message = 'No se puede conectar con el servidor. Verifique su conexión a internet.';
        }

        this.showNotification(message, type);
        return message;
    },

    /**
     * Muestra notificaciones al usuario
     */
    showNotification(message, type = 'info') {
        // Crear elemento de notificación
        const notification = document.createElement('div');
        notification.className = `alert alert-${type}`;
        notification.innerHTML = `
            <i class="fas fa-${type === 'success' ? 'check-circle' : type === 'error' ? 'exclamation-triangle' : 'info-circle'}"></i>
            ${message}
        `;

        // Agregar al contenedor de notificaciones
        let container = document.querySelector('.notifications-container');
        if (!container) {
            container = document.createElement('div');
            container.className = 'notifications-container';
            document.body.appendChild(container);
        }

        container.appendChild(notification);

        // Mostrar con animación
        setTimeout(() => {
            notification.style.transform = 'translateX(0)';
            notification.style.opacity = '1';
        }, 100);

        // Ocultar después de 5 segundos
        setTimeout(() => {
            notification.style.transform = 'translateX(100%)';
            notification.style.opacity = '0';
            setTimeout(() => {
                if (notification.parentNode) {
                    notification.parentNode.removeChild(notification);
                }
            }, 300);
        }, 5000);
    },

    /**
     * Formatea fechas para mostrar al usuario
     */
    formatDate(dateString) {
        if (!dateString) return '';
        const date = new Date(dateString);
        return date.toLocaleDateString('es-CO', {
            year: 'numeric',
            month: '2-digit',
            day: '2-digit',
            hour: '2-digit',
            minute: '2-digit'
        });
    },

    /**
     * Formatea números como moneda
     */
    formatCurrency(amount) {
        return new Intl.NumberFormat('es-CO', {
            style: 'currency',
            currency: 'COP',
            minimumFractionDigits: 0
        }).format(amount);
    },

    /**
     * Valida formato de email
     */
    isValidEmail(email) {
        const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        return emailRegex.test(email);
    },

    /**
     * Valida formato de cédula (números y máximo 20 caracteres)
     */
    isValidIdCard(idCard) {
        return /^\d{1,20}$/.test(idCard);
    },

    /**
     * Valida formato de teléfono
     */
    isValidPhone(phone) {
        return /^\d{7,15}$/.test(phone.replace(/\s+/g, ''));
    }
};

/**
 * Funciones de manejo de autenticación
 */
const AuthManager = {
    /**
     * Guarda la sesión del usuario
     */
    setSession(user, token) {
        currentUser = user;
        authToken = token;
        localStorage.setItem('authToken', token);
        localStorage.setItem('currentUser', JSON.stringify(user));
    },

    /**
     * Cierra la sesión del usuario
     */
    clearSession() {
        currentUser = null;
        authToken = null;
        localStorage.removeItem('authToken');
        localStorage.removeItem('currentUser');
    },

    /**
     * Obtiene el usuario actual desde localStorage
     */
    getCurrentUser() {
        if (!currentUser) {
            const userStr = localStorage.getItem('currentUser');
            if (userStr) {
                try {
                    currentUser = JSON.parse(userStr);
                } catch (e) {
                    console.error('Error parsing current user:', e);
                }
            }
        }
        return currentUser;
    },

    /**
     * Verifica si el usuario está autenticado
     */
    isAuthenticated() {
        return currentUser !== null && authToken !== null;
    },

    /**
     * Verifica si el usuario tiene un rol específico
     */
    hasRole(role) {
        return currentUser?.role === role;
    },

    /**
     * Verifica si el usuario tiene permisos para una acción
     */
    canAccess(resource, action) {
        if (!currentUser) return false;

        // Lógica básica de permisos por rol
        const permissions = {
            ADMINISTRATIVE: ['patients', 'appointments', 'billing', 'reports'],
            DOCTOR: ['clinical-history', 'medical-orders', 'vital-signs', 'patients'],
            NURSE: ['vital-signs', 'clinical-history', 'patients'],
            HUMAN_RESOURCES: ['users', 'staff'],
            SUPPORT: ['inventory', 'reports']
        };

        return permissions[currentUser.role]?.includes(resource) || false;
    }
};

/**
 * Inicialización de la aplicación
 */
document.addEventListener('DOMContentLoaded', function() {
    // Restaurar sesión si existe
    const user = AuthManager.getCurrentUser();
    if (user && authToken) {
        currentUser = user;
        showDashboard();
        loadDashboardData();
    } else {
        showLoginScreen();
    }

    // Configurar manejadores globales de errores
    window.addEventListener('unhandledrejection', function(event) {
        console.error('Unhandled promise rejection:', event.reason);
        ApiUtils.handleError(event.reason, 'Error inesperado en la aplicación');
    });
});

// Exportar para uso en otros módulos
window.API_CONFIG = API_CONFIG;
window.apiClient = apiClient;
window.AuthAPI = AuthAPI;
window.PatientAPI = PatientAPI;
window.ClinicalHistoryAPI = ClinicalHistoryAPI;
window.OrdersAPI = OrdersAPI;
window.VitalSignsAPI = VitalSignsAPI;
window.UsersAPI = UsersAPI;
window.InventoryAPI = InventoryAPI;
window.BillingAPI = BillingAPI;
window.ReportsAPI = ReportsAPI;
window.ApiUtils = ApiUtils;
window.AuthManager = AuthManager;