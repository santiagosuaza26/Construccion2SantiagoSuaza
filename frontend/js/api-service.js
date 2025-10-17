/**
 * Servicio de API para comunicación con el backend de Spring Boot
 * Maneja todas las llamadas HTTP y la configuración de autenticación
 */
class ApiService {
    constructor() {
        this.baseURL = window.APP_CONFIG ? window.APP_CONFIG.API_BASE_URL : 'http://localhost:8080/api';
        this.token = localStorage.getItem(window.APP_CONFIG.AUTH.TOKEN_KEY);
        this.cache = new Map();
        this.pendingRequests = new Map();
        this.maxRetries = 3;
        this.retryDelay = 1000;
        this.cacheTimeout = 5 * 60 * 1000; // 5 minutos
    }

    /**
     * Configura los headers para las peticiones HTTP
     */
    getHeaders(includeAuth = true) {
        const headers = {
            'Content-Type': 'application/json',
        };

        if (includeAuth && this.token) {
            headers['Authorization'] = `Bearer ${this.token}`;
        }

        return headers;
    }

    /**
     * Maneja las respuestas de la API
     */
    async handleResponse(response) {
        if (!response.ok) {
            let errorData = {};
            try {
                errorData = await response.json();
            } catch (e) {
                // Si no se puede parsear el JSON, usar mensaje genérico
                errorData = {};
            }

            // Proporcionar mensajes de error más específicos según el código de estado
            let errorMessage = errorData.message || `Error HTTP ${response.status}`;

            if (response.status === 401) {
                errorMessage = 'No autorizado. Su sesión ha expirado.';
                // Limpiar sesión automáticamente en errores 401
                if (window.authService) {
                    window.authService.clearSession();
                    window.authService.showLoginPage();
                }
            } else if (response.status === 403) {
                errorMessage = 'Acceso denegado. No tiene permisos para esta acción.';
            } else if (response.status === 404) {
                errorMessage = 'Recurso no encontrado.';
            } else if (response.status === 422) {
                errorMessage = 'Datos inválidos. Verifique la información ingresada.';
            } else if (response.status === 429) {
                errorMessage = 'Demasiadas solicitudes. Espere un momento e intente nuevamente.';
            } else if (response.status >= 500) {
                errorMessage = 'Error interno del servidor. Intente nuevamente más tarde.';
            }

            // Crear error con información adicional para debugging
            const error = new Error(errorMessage);
            error.status = response.status;
            error.endpoint = response.url;
            error.timestamp = new Date().toISOString();

            throw error;
        }

        return response.json();
    }

    /**
     * Realiza una petición GET con caché y reintentos
     */
    async get(endpoint, params = {}, includeAuth = true, useCache = true) {
        const url = new URL(`${this.baseURL}${endpoint}`);
        Object.keys(params).forEach(key => {
            if (params[key] !== null && params[key] !== undefined) {
                url.searchParams.append(key, params[key]);
            }
        });

        const cacheKey = `${endpoint}:${url.searchParams.toString()}`;

        // Verificar caché si está habilitado
        if (useCache && this.cache.has(cacheKey)) {
            const cached = this.cache.get(cacheKey);
            if (Date.now() - cached.timestamp < this.cacheTimeout) {
                console.log(`📋 Cache hit para ${endpoint}`);
                return cached.data;
            } else {
                this.cache.delete(cacheKey);
            }
        }

        // Evitar múltiples peticiones simultáneas al mismo endpoint
        if (this.pendingRequests.has(cacheKey)) {
            console.log(`⏳ Esperando petición pendiente para ${endpoint}`);
            return this.pendingRequests.get(cacheKey);
        }

        const requestPromise = this.executeWithRetry(async () => {
            const response = await fetch(url, {
                method: 'GET',
                headers: this.getHeaders(includeAuth)
            });
            return this.handleResponse(response);
        });

        this.pendingRequests.set(cacheKey, requestPromise);

        try {
            const result = await requestPromise;

            // Guardar en caché si está habilitado
            if (useCache) {
                this.cache.set(cacheKey, {
                    data: result,
                    timestamp: Date.now()
                });
            }

            return result;
        } finally {
            this.pendingRequests.delete(cacheKey);
        }
    }

    /**
     * Realiza una petición POST
     */
    async post(endpoint, data = {}, includeAuth = true) {
        const response = await fetch(`${this.baseURL}${endpoint}`, {
            method: 'POST',
            headers: this.getHeaders(includeAuth),
            body: JSON.stringify(data)
        });

        return this.handleResponse(response);
    }

    /**
     * Realiza una petición PUT
     */
    async put(endpoint, data = {}, includeAuth = true) {
        const response = await fetch(`${this.baseURL}${endpoint}`, {
            method: 'PUT',
            headers: this.getHeaders(includeAuth),
            body: JSON.stringify(data)
        });

        return this.handleResponse(response);
    }

    /**
     * Realiza una petición DELETE
     */
    async delete(endpoint, includeAuth = true) {
        const response = await fetch(`${this.baseURL}${endpoint}`, {
            method: 'DELETE',
            headers: this.getHeaders(includeAuth)
        });

        return this.handleResponse(response);
    }

    /**
     * Establece el token de autenticación
     */
    setToken(token) {
        this.token = token;
        if (token) {
            localStorage.setItem('authToken', token);
        } else {
            localStorage.removeItem('authToken');
        }
    }

    /**
     * Obtiene el token actual
     */
    getToken() {
        return this.token;
    }

    /**
     * Verifica si el usuario está autenticado
     */
    isAuthenticated() {
        return !!this.token;
    }

    /**
     * Ejecuta una petición con reintentos automáticos
     */
    async executeWithRetry(operation, attempt = 1) {
        try {
            return await operation();
        } catch (error) {
            // No reintentar errores de autenticación o validación
            if (error.status === 401 || error.status === 403 || error.status === 422) {
                throw error;
            }

            // Reintentar en errores de red o servidor (máximo 3 intentos)
            if (attempt < this.maxRetries && (error.message.includes('fetch') || error.status >= 500)) {
                console.log(`🔄 Reintentando petición (intento ${attempt + 1}/${this.maxRetries})...`);
                await this.delay(this.retryDelay * attempt);
                return this.executeWithRetry(operation, attempt + 1);
            }

            throw error;
        }
    }

    /**
     * Retraso para reintentos
     */
    delay(ms) {
        return new Promise(resolve => setTimeout(resolve, ms));
    }

    /**
     * Limpia la caché
     */
    clearCache() {
        this.cache.clear();
        console.log('🗑️ Caché limpiado');
    }

    /**
     * Obtiene estadísticas de caché
     */
    getCacheStats() {
        return {
            size: this.cache.size,
            pendingRequests: this.pendingRequests.size,
            hitRate: this.cache.size > 0 ? 'N/A' : 0
        };
    }

    /**
     * Pre-carga datos comunes en caché
     */
    async preloadCommonData() {
        try {
            console.log('🚀 Pre-cargando datos comunes...');

            // Pre-cargar datos que se usan frecuentemente
            const promises = [
                this.get('/users', {}, true, true), // Usuarios activos
                this.get('/patients', {}, true, true), // Lista de pacientes
            ];

            await Promise.allSettled(promises);
            console.log('✅ Datos comunes pre-cargados');

        } catch (error) {
            console.warn('⚠️ Error en pre-carga de datos:', error);
        }
    }
}

// Endpoints públicos
const publicApi = {
    /**
     * Verifica el estado de la API
     */
    async healthCheck() {
        const api = new ApiService();
        return api.get('/public/health', {}, false);
    },

    /**
     * Obtiene información de bienvenida
     */
    async getWelcomeInfo() {
        const api = new ApiService();
        return api.get('/public/welcome', {}, false);
    },

    /**
     * Obtiene información de la API
     */
    async getApiInfo() {
        const api = new ApiService();
        return api.get('/public/info', {}, false);
    }
};

// API de usuarios (requiere autenticación según el rol)
const userApi = {
    /**
     * Crea un nuevo usuario (Solo Recursos Humanos)
     */
    async createUser(userData) {
        const api = new ApiService();
        return api.post('/users', userData);
    },

    /**
     * Actualiza un usuario existente (Solo Recursos Humanos)
     */
    async updateUser(cedula, userData) {
        const api = new ApiService();
        return api.put(`/users/${cedula}`, userData);
    },

    /**
     * Busca un usuario por cédula
     */
    async findUserByCedula(cedula) {
        const api = new ApiService();
        return api.get(`/users/cedula/${cedula}`);
    },

    /**
     * Busca un usuario por nombre de usuario
     */
    async findUserByUsername(username) {
        const api = new ApiService();
        return api.get(`/users/username/${username}`);
    },

    /**
     * Busca un usuario por ID
     */
    async findUserById(userId) {
        const api = new ApiService();
        return api.get(`/users/id/${userId}`);
    },

    /**
     * Obtiene usuarios por rol
     */
    async findUsersByRole(role) {
        const api = new ApiService();
        return api.get(`/users/role/${role}`);
    },

    /**
     * Obtiene todos los usuarios activos
     */
    async findAllActiveUsers() {
        const api = new ApiService();
        return api.get('/users/active');
    },

    /**
     * Obtiene todos los usuarios
     */
    async findAllUsers() {
        const api = new ApiService();
        return api.get('/users');
    },

    /**
     * Elimina un usuario por cédula (Solo Recursos Humanos)
     */
    async deleteUserByCedula(cedula) {
        const api = new ApiService();
        return api.delete(`/users/cedula/${cedula}`);
    },

    /**
     * Elimina un usuario por ID (Solo Recursos Humanos)
     */
    async deleteUserById(userId) {
        const api = new ApiService();
        return api.delete(`/users/id/${userId}`);
    },

    /**
     * Activa un usuario (Solo Recursos Humanos)
     */
    async activateUser(cedula) {
        const api = new ApiService();
        return api.put(`/users/${cedula}/activate`);
    },

    /**
     * Desactiva un usuario (Solo Recursos Humanos)
     */
    async deactivateUser(cedula) {
        const api = new ApiService();
        return api.put(`/users/${cedula}/deactivate`);
    },

    /**
     * Verifica si un usuario puede ver información de pacientes
     */
    async canViewPatientInfo(cedula) {
        const api = new ApiService();
        return api.get(`/users/${cedula}/can-view-patients`);
    },

    /**
     * Verifica si un usuario puede gestionar usuarios
     */
    async canManageUsers(cedula) {
        const api = new ApiService();
        return api.get(`/users/${cedula}/can-manage-users`);
    },

    /**
     * Verifica si un usuario puede registrar pacientes
     */
    async canRegisterPatients(cedula) {
        const api = new ApiService();
        return api.get(`/users/${cedula}/can-register-patients`);
    }
};

// API de pacientes (requiere autenticación según el rol)
const patientApi = {
    /**
     * Registra un nuevo paciente (Solo Personal Administrativo)
     */
    async registerPatient(patientData) {
        const api = new ApiService();
        return api.post('/patients', patientData);
    },

    /**
     * Actualiza un paciente existente (Solo Personal Administrativo)
     */
    async updatePatient(cedula, patientData) {
        const api = new ApiService();
        return api.put(`/patients/${cedula}`, patientData);
    },

    /**
     * Busca un paciente por cédula
     */
    async findPatientByCedula(cedula) {
        const api = new ApiService();
        return api.get(`/patients/cedula/${cedula}`);
    },

    /**
     * Busca un paciente por nombre de usuario
     */
    async findPatientByUsername(username) {
        const api = new ApiService();
        return api.get(`/patients/username/${username}`);
    },

    /**
     * Busca un paciente por ID
     */
    async findPatientById(patientId) {
        const api = new ApiService();
        return api.get(`/patients/id/${patientId}`);
    },

    /**
     * Obtiene todos los pacientes
     */
    async findAllPatients() {
        const api = new ApiService();
        return api.get('/patients');
    },

    /**
     * Elimina un paciente por cédula (Solo Personal Administrativo)
     */
    async deletePatientByCedula(cedula) {
        const api = new ApiService();
        return api.delete(`/patients/cedula/${cedula}`);
    },

    /**
     * Elimina un paciente por ID (Solo Personal Administrativo)
     */
    async deletePatientById(patientId) {
        const api = new ApiService();
        return api.delete(`/patients/id/${patientId}`);
    },

    /**
     * Verifica si un paciente tiene seguro médico activo
     */
    async hasActiveInsurance(cedula) {
        const api = new ApiService();
        return api.get(`/patients/${cedula}/has-active-insurance`);
    },

    /**
     * Obtiene la edad de un paciente
     */
    async getPatientAge(cedula) {
        const api = new ApiService();
        return api.get(`/patients/${cedula}/age`);
    }
};

// API de historias clínicas (requiere autenticación según el rol)
const medicalRecordApi = {
    /**
     * Crea una nueva entrada en la historia clínica (Solo Médicos)
     */
    async createMedicalRecord(recordData) {
        const api = new ApiService();
        return api.post('/medical-records', recordData);
    },

    /**
     * Busca la historia clínica de un paciente
     */
    async findMedicalRecordByPatientCedula(patientCedula) {
        const api = new ApiService();
        return api.get(`/medical-records/patient/${patientCedula}`);
    },

    /**
     * Busca una entrada específica de historia clínica
     */
    async findMedicalRecordEntry(patientCedula, recordDate) {
        const api = new ApiService();
        return api.get(`/medical-records/patient/${patientCedula}/date/${recordDate}`);
    },

    /**
     * Verifica si un paciente tiene historias clínicas
     */
    async hasMedicalRecords(patientCedula) {
        const api = new ApiService();
        return api.get(`/medical-records/patient/${patientCedula}/exists`);
    },

    /**
     * Obtiene el número de entradas de historia clínica de un paciente
     */
    async getMedicalRecordCount(patientCedula) {
        const api = new ApiService();
        return api.get(`/medical-records/patient/${patientCedula}/count`);
    }
};

// API de órdenes médicas (requiere autenticación según el rol)
const orderApi = {
    /**
     * Crea una nueva orden médica (Solo Médicos)
     */
    async createOrder(orderData) {
        const api = new ApiService();
        return api.post('/orders', orderData);
    },

    /**
     * Obtiene todas las órdenes
     */
    async findAllOrders() {
        const api = new ApiService();
        return api.get('/orders');
    },

    /**
     * Busca órdenes por paciente
     */
    async findOrdersByPatient(patientCedula) {
        const api = new ApiService();
        return api.get(`/orders/patient/${patientCedula}`);
    },

    /**
     * Busca órdenes por médico
     */
    async findOrdersByDoctor(doctorCedula) {
        const api = new ApiService();
        return api.get(`/orders/doctor/${doctorCedula}`);
    },

    /**
     * Busca una orden por número
     */
    async findOrderByNumber(orderNumber) {
        const api = new ApiService();
        return api.get(`/orders/number/${orderNumber}`);
    }
};

// API de visitas de pacientes (requiere autenticación según el rol)
const patientVisitApi = {
    /**
     * Crea una nueva visita de paciente (Enfermeras y Médicos)
     */
    async createPatientVisit(visitData) {
        const api = new ApiService();
        return api.post('/patient-visits', visitData);
    },

    /**
     * Obtiene todas las visitas de pacientes
     */
    async findAllPatientVisits() {
        const api = new ApiService();
        return api.get('/patient-visits');
    },

    /**
     * Busca visitas por paciente
     */
    async findVisitsByPatient(patientCedula) {
        const api = new ApiService();
        return api.get(`/patient-visits/patient/${patientCedula}`);
    },

    /**
     * Busca visitas por fecha
     */
    async findVisitsByDate(date) {
        const api = new ApiService();
        return api.get('/patient-visits/date', { date });
    }
};

// API de facturación (requiere autenticación según el rol)
const billingApi = {
    /**
     * Calcula la facturación para un paciente
     */
    async calculateBilling(patientCedula) {
        const api = new ApiService();
        return api.get(`/billing/calculate/${patientCedula}`);
    },

    /**
     * Obtiene todas las facturas
     */
    async findAllInvoices() {
        const api = new ApiService();
        return api.get('/billing/invoices');
    },

    /**
     * Busca factura por paciente
     */
    async findInvoicesByPatient(patientCedula) {
        const api = new ApiService();
        return api.get(`/billing/invoices/patient/${patientCedula}`);
    },

    /**
     * Genera una nueva factura
     */
    async generateInvoice(invoiceData) {
        const api = new ApiService();
        return api.post('/billing/generate', invoiceData);
    },

    /**
     * Obtiene el acumulado anual de copagos de un paciente
     */
    async getAnnualAccumulated(patientCedula) {
        const api = new ApiService();
        return api.get(`/billing/copagos/accumulated`, { cedula: patientCedula });
    },

    /**
     * Obtiene el historial de acumulado anual de un paciente
     */
    async getAccumulatedHistory(patientCedula) {
        const api = new ApiService();
        return api.get(`/billing/copagos/history/${patientCedula}`);
    },

    /**
     * Genera reporte de acumulado anual
     */
    async generateAccumulatedReport(patientCedula) {
        const api = new ApiService();
        return api.get(`/billing/copagos/report/${patientCedula}`);
    }
};

// API de inventario (requiere autenticación según el rol)
const inventoryApi = {
    /**
     * Crea un nuevo ítem de inventario (Solo Soporte de Información)
     */
    async createInventoryItem(itemData) {
        const api = new ApiService();
        return api.post('/inventory', itemData);
    },

    /**
     * Obtiene todos los ítems de inventario
     */
    async findAllInventoryItems() {
        const api = new ApiService();
        return api.get('/inventory');
    },

    /**
     * Busca ítem de inventario por ID
     */
    async findInventoryItemById(itemId) {
        const api = new ApiService();
        return api.get(`/inventory/id/${itemId}`);
    },

    /**
     * Busca ítems de inventario por tipo
     */
    async findInventoryItemsByType(type) {
        const api = new ApiService();
        return api.get(`/inventory/type/${type}`);
    },

    /**
     * Actualiza un ítem de inventario
     */
    async updateInventoryItem(itemId, itemData) {
        const api = new ApiService();
        return api.put(`/inventory/${itemId}`, itemData);
    },

    /**
     * Elimina un ítem de inventario
     */
    async deleteInventoryItem(itemId) {
        const api = new ApiService();
        return api.delete(`/inventory/${itemId}`);
    }
};

// API de citas (requiere autenticación según el rol)
const appointmentApi = {
    /**
     * Crea una nueva cita (Personal Administrativo)
     */
    async createAppointment(appointmentData) {
        const api = new ApiService();
        return api.post('/appointments', appointmentData);
    },

    /**
     * Obtiene todas las citas
     */
    async findAllAppointments() {
        const api = new ApiService();
        return api.get('/appointments');
    },

    /**
     * Busca citas por paciente
     */
    async findAppointmentsByPatient(patientCedula) {
        const api = new ApiService();
        return api.get(`/appointments/patient/${patientCedula}`);
    },

    /**
     * Busca citas por médico
     */
    async findAppointmentsByDoctor(doctorCedula) {
        const api = new ApiService();
        return api.get(`/appointments/doctor/${doctorCedula}`);
    },

    /**
     * Busca citas por fecha
     */
    async findAppointmentsByDate(date) {
        const api = new ApiService();
        return api.get('/appointments/date', { date });
    },

    /**
     * Actualiza una cita
     */
    async updateAppointment(appointmentId, appointmentData) {
        const api = new ApiService();
        return api.put(`/appointments/${appointmentId}`, appointmentData);
    },

    /**
     * Cancela una cita
     */
    async cancelAppointment(appointmentId) {
        const api = new ApiService();
        return api.put(`/appointments/${appointmentId}/cancel`);
    }
};

// Exportar todas las APIs
window.publicApi = publicApi;
window.userApi = userApi;
window.patientApi = patientApi;
window.medicalRecordApi = medicalRecordApi;
window.orderApi = orderApi;
window.patientVisitApi = patientVisitApi;
window.billingApi = billingApi;
window.inventoryApi = inventoryApi;
window.appointmentApi = appointmentApi;
window.ApiService = ApiService;