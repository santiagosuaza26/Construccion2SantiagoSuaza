// Main JavaScript module for Clinic CS2 Application
// Following Clean Architecture principles and SOLID design

// Global application configuration
const ApplicationConfiguration = {
    apiBaseUrl: window.location.origin + '/api',
    appVersion: '1.0.0',
    defaultLanguage: 'es',
    sessionTimeout: 30 * 60 * 1000, // 30 minutes
    maxRetryAttempts: 3
};

// Application state management
class ApplicationState {
    constructor() {
        this.currentUser = null;
        this.currentRole = null;
        this.isAuthenticated = false;
        this.sessionStartTime = null;
        this.apiStatus = 'unknown';
    }

    setUser(userData) {
        this.currentUser = userData;
        this.currentRole = userData?.role || null;
        this.isAuthenticated = true;
        this.sessionStartTime = Date.now();
        this.saveToStorage();
    }

    clearUser() {
        this.currentUser = null;
        this.currentRole = null;
        this.isAuthenticated = false;
        this.sessionStartTime = null;
        this.clearStorage();
    }

    isSessionExpired() {
        if (!this.sessionStartTime) return true;
        return (Date.now() - this.sessionStartTime) > ApplicationConfiguration.sessionTimeout;
    }

    saveToStorage() {
        try {
            localStorage.setItem('clinicApp_currentUser', JSON.stringify(this.currentUser));
            localStorage.setItem('clinicApp_sessionStart', this.sessionStartTime.toString());
        } catch (error) {
            console.error('Error saving application state:', error);
        }
    }

    clearStorage() {
        try {
            localStorage.removeItem('clinicApp_currentUser');
            localStorage.removeItem('clinicApp_sessionStart');
            localStorage.removeItem('clinicApp_authToken');
        } catch (error) {
            console.error('Error clearing application state:', error);
        }
    }

    loadFromStorage() {
        try {
            const storedUser = localStorage.getItem('clinicApp_currentUser');
            const storedSessionStart = localStorage.getItem('clinicApp_sessionStart');

            if (storedUser && storedSessionStart) {
                this.currentUser = JSON.parse(storedUser);
                this.currentRole = this.currentUser?.role || null;
                this.isAuthenticated = true;
                this.sessionStartTime = parseInt(storedSessionStart);
            }
        } catch (error) {
            console.error('Error loading application state:', error);
            this.clearStorage();
        }
    }
}

// Global application state instance
const applicationState = new ApplicationState();

// Application initialization
document.addEventListener('DOMContentLoaded', () => {
    ApplicationInitializer.initialize();
});

/**
 * Application initializer following Single Responsibility Principle
 */
class ApplicationInitializer {
    static initialize() {
        console.log(`🏥 Initializing Clinic CS2 v${ApplicationConfiguration.appVersion}`);

        try {
            // Load existing session if available
            this.loadExistingSession();

            // Setup global event listeners
            this.setupGlobalEventListeners();

            // Check API status
            this.checkApiStatus();

            // Initialize page-specific functionality
            this.initializePageSpecificFunctionality();

        } catch (error) {
            console.error('Error during application initialization:', error);
            this.handleInitializationError(error);
        }
    }

    static loadExistingSession() {
        applicationState.loadFromStorage();

        if (applicationState.isSessionExpired()) {
            applicationState.clearUser();
            console.log('Session expired, clearing user data');
        }
    }

    static setupGlobalEventListeners() {
        // Handle page visibility changes
        document.addEventListener('visibilitychange', () => {
            if (document.visibilityState === 'visible') {
                this.checkApiStatus();
            }
        });

        // Handle online/offline status
        window.addEventListener('online', () => {
            this.showSuccessMessage('Conexión restablecida');
            this.checkApiStatus();
        });

        window.addEventListener('offline', () => {
            this.showErrorMessage('Sin conexión a internet');
        });
    }

    static async checkApiStatus() {
        try {
            const response = await fetch(`${ApplicationConfiguration.apiBaseUrl}/actuator/health`);
            const healthData = await response.json();

            applicationState.apiStatus = healthData.status;

            const statusElement = document.getElementById('apiStatus');
            if (statusElement) {
                this.updateApiStatusElement(statusElement, healthData.status);
            }
        } catch (error) {
            console.error('API status check failed:', error);
            applicationState.apiStatus = 'error';

            const statusElement = document.getElementById('apiStatus');
            if (statusElement) {
                this.updateApiStatusElement(statusElement, 'error');
            }
        }
    }

    static updateApiStatusElement(element, status) {
        const statusConfig = {
            'UP': { icon: 'fas fa-check-circle', text: 'API Funcional', class: 'success' },
            'DOWN': { icon: 'fas fa-times-circle', text: 'API No Disponible', class: 'error' },
            'error': { icon: 'fas fa-exclamation-triangle', text: 'Error de Conexión', class: 'error' }
        };

        const config = statusConfig[status] || statusConfig.error;

        element.innerHTML = `<i class="${config.icon}"></i> ${config.text}`;
        element.className = `status-${config.class}`;
    }

    static initializePageSpecificFunctionality() {
        const currentPath = window.location.pathname;

        if (currentPath.includes('login')) {
            LoginPageHandler.initialize();
        } else if (currentPath.includes('dashboard')) {
            DashboardPageHandler.initialize();
        } else if (currentPath === '/' || currentPath.endsWith('index.html')) {
            WelcomePageHandler.initialize();
        }
    }

    static handleInitializationError(error) {
        this.showErrorMessage('Error al inicializar la aplicación');
        console.error('Initialization error:', error);
    }

    static showSuccessMessage(message) {
        this.showToastMessage(message, 'success');
    }

    static showErrorMessage(message) {
        this.showToastMessage(message, 'error');
    }

    static showToastMessage(message, type) {
        // Create toast element if it doesn't exist
        let toastContainer = document.getElementById('toastContainer');
        if (!toastContainer) {
            toastContainer = document.createElement('div');
            toastContainer.id = 'toastContainer';
            toastContainer.style.cssText = `
                position: fixed;
                top: 20px;
                right: 20px;
                z-index: 5000;
            `;
            document.body.appendChild(toastContainer);
        }

        const toast = document.createElement('div');
        toast.className = `toast toast-${type}`;
        toast.style.cssText = `
            background: ${type === 'success' ? 'var(--success-color)' : 'var(--danger-color)'};
            color: white;
            padding: 15px 20px;
            border-radius: var(--border-radius);
            margin-bottom: 10px;
            box-shadow: var(--shadow-lg);
            animation: slideInRight 0.3s ease;
        `;
        toast.textContent = message;

        toastContainer.appendChild(toast);

        // Remove toast after 5 seconds
        setTimeout(() => {
            toast.remove();
        }, 5000);
    }
}

/**
 * Verifica si hay una sesión existente
 */
function checkExistingSession() {
    const token = localStorage.getItem('authToken');
    const user = localStorage.getItem('currentUser');

    if (token && user) {
        try {
            currentUser = JSON.parse(user);
            currentRole = currentUser.role;
            isAuthenticated = true;

            // Si estamos en la página de login o welcome, redirigir al dashboard
            if (window.location.pathname.includes('login') ||
                window.location.pathname === '/' ||
                !window.location.pathname.includes('dashboard')) {
                window.location.href = 'dashboard.html';
            } else {
                // Si estamos en dashboard, cargar la interfaz
                loadDashboard();
            }
        } catch (e) {
            console.error('Error parsing stored user data:', e);
            clearSession();
        }
    }
}

/**
 * Configura event listeners globales
 */
function setupGlobalEventListeners() {
    // Prevenir navegación hacia atrás en páginas de login/dashboard
    if (window.location.pathname.includes('login') ||
        window.location.pathname.includes('dashboard')) {
        window.addEventListener('beforeunload', function() {
            // No hacer nada especial
        });
    }
}

/**
 * Verifica el estado de la API
 */
async function checkApiStatus() {
    try {
        const response = await fetch(`${API_BASE_URL}/actuator/health`);
        const data = await response.json();

        const statusElement = document.getElementById('apiStatus');
        if (statusElement) {
            if (data.status === 'UP') {
                statusElement.innerHTML = '<i class="fas fa-check-circle"></i> API Funcional';
                statusElement.style.color = 'var(--success-color)';
            } else {
                statusElement.innerHTML = '<i class="fas fa-exclamation-triangle"></i> API No Disponible';
                statusElement.style.color = 'var(--danger-color)';
            }
        }
    } catch (error) {
        console.error('Error checking API status:', error);
        const statusElement = document.getElementById('apiStatus');
        if (statusElement) {
            statusElement.innerHTML = '<i class="fas fa-times-circle"></i> Error de Conexión';
            statusElement.style.color = 'var(--danger-color)';
        }
    }
}

// UI Management Module
class UiManager {
    static showLoading(message = 'Loading...') {
        const overlay = document.getElementById('loadingOverlay');
        if (overlay) {
            const loadingText = overlay.querySelector('p');
            if (loadingText) {
                loadingText.textContent = message;
            }
            overlay.style.display = 'flex';
        }
    }

    static hideLoading() {
        const overlay = document.getElementById('loadingOverlay');
        if (overlay) {
            overlay.style.display = 'none';
        }
    }

    static showModal(modalId) {
        const modal = document.getElementById(modalId);
        if (modal) {
            modal.style.display = 'block';
        }
    }

    static hideModal(modalId) {
        const modal = document.getElementById(modalId);
        if (modal) {
            modal.style.display = 'none';
        }
    }

    static updateElementText(elementId, text) {
        const element = document.getElementById(elementId);
        if (element) {
            element.textContent = text;
        }
    }

    static showNotification(message, type = 'info', duration = 5000) {
        const notificationContainer = this.getNotificationContainer();

        const notification = document.createElement('div');
        notification.className = `notification notification-${type}`;
        notification.style.cssText = `
            background: ${this.getNotificationColor(type)};
            color: white;
            padding: 15px 20px;
            border-radius: var(--border-radius);
            margin-bottom: 10px;
            box-shadow: var(--shadow-lg);
            animation: slideInRight 0.3s ease;
        `;

        notification.innerHTML = `
            <div style="display: flex; align-items: center; gap: 10px;">
                <i class="${this.getNotificationIcon(type)}"></i>
                <span>${message}</span>
            </div>
        `;

        notificationContainer.appendChild(notification);

        // Auto-remove after duration
        setTimeout(() => {
            notification.remove();
        }, duration);
    }

    static getNotificationContainer() {
        let container = document.getElementById('notificationContainer');
        if (!container) {
            container = document.createElement('div');
            container.id = 'notificationContainer';
            container.style.cssText = `
                position: fixed;
                top: 20px;
                right: 20px;
                z-index: 5000;
                max-width: 400px;
            `;
            document.body.appendChild(container);
        }
        return container;
    }

    static getNotificationColor(type) {
        const colors = {
            'success': 'var(--success-color)',
            'error': 'var(--danger-color)',
            'warning': 'var(--warning-color)',
            'info': 'var(--info-color)'
        };
        return colors[type] || colors.info;
    }

    static getNotificationIcon(type) {
        const icons = {
            'success': 'fas fa-check-circle',
            'error': 'fas fa-exclamation-circle',
            'warning': 'fas fa-exclamation-triangle',
            'info': 'fas fa-info-circle'
        };
        return icons[type] || icons.info;
    }
}

/**
 * Muestra un mensaje de error
 */
function showError(message, duration = 5000) {
    // Crear elemento de error si no existe
    let errorDiv = document.getElementById('errorMessage');
    if (!errorDiv) {
        errorDiv = document.createElement('div');
        errorDiv.id = 'errorMessage';
        errorDiv.style.cssText = `
            position: fixed;
            top: 20px;
            right: 20px;
            background: var(--danger-color);
            color: white;
            padding: 15px 20px;
            border-radius: var(--border-radius);
            box-shadow: var(--shadow-lg);
            z-index: 4000;
            max-width: 300px;
        `;
        document.body.appendChild(errorDiv);
    }

    errorDiv.textContent = message;
    errorDiv.style.display = 'block';

    // Ocultar automáticamente después del tiempo especificado
    setTimeout(() => {
        errorDiv.style.display = 'none';
    }, duration);
}

/**
 * Muestra un mensaje de éxito
 */
function showSuccess(message, duration = 3000) {
    let successDiv = document.getElementById('successMessage');
    if (!successDiv) {
        successDiv = document.createElement('div');
        successDiv.id = 'successMessage';
        successDiv.style.cssText = `
            position: fixed;
            top: 20px;
            right: 20px;
            background: var(--success-color);
            color: white;
            padding: 15px 20px;
            border-radius: var(--border-radius);
            box-shadow: var(--shadow-lg);
            z-index: 4000;
            max-width: 300px;
        `;
        document.body.appendChild(successDiv);
    }

    successDiv.textContent = message;
    successDiv.style.display = 'block';

    setTimeout(() => {
        successDiv.style.display = 'none';
    }, duration);
}

// API Communication Module
class ApiClient {
    constructor(baseUrl = ApplicationConfiguration.apiBaseUrl) {
        this.baseUrl = baseUrl;
        this.defaultHeaders = {
            'Content-Type': 'application/json'
        };
    }

    async makeRequest(endpoint, options = {}) {
        const url = `${this.baseUrl}${endpoint}`;
        const requestOptions = {
            headers: { ...this.defaultHeaders, ...options.headers }
        };

        // Add authentication token if available
        const authToken = this.getStoredAuthToken();
        if (authToken) {
            requestOptions.headers['Authorization'] = `Bearer ${authToken}`;
        }

        // Add user ID if available
        const userId = this.getCurrentUserId();
        if (userId) {
            requestOptions.headers['User-ID'] = userId;
        }

        try {
            const response = await fetch(url, { ...requestOptions, ...options });
            return await this.handleResponse(response);
        } catch (error) {
            return await this.handleNetworkError(error);
        }
    }

    async get(endpoint, headers = {}) {
        return this.makeRequest(endpoint, { method: 'GET', headers });
    }

    async post(endpoint, data = null, headers = {}) {
        const options = {
            method: 'POST',
            headers,
            body: data ? JSON.stringify(data) : null
        };
        return this.makeRequest(endpoint, options);
    }

    async put(endpoint, data = null, headers = {}) {
        const options = {
            method: 'PUT',
            headers,
            body: data ? JSON.stringify(data) : null
        };
        return this.makeRequest(endpoint, options);
    }

    async delete(endpoint, headers = {}) {
        return this.makeRequest(endpoint, { method: 'DELETE', headers });
    }

    async handleResponse(response) {
        let responseData;

        try {
            responseData = await response.json();
        } catch (error) {
            // Response is not JSON
            responseData = { message: 'Invalid response format' };
        }

        if (!response.ok) {
            await this.handleHttpError(response.status, responseData);
        }

        return {
            success: true,
            status: response.status,
            data: responseData
        };
    }

    async handleHttpError(statusCode, responseData) {
        const errorMessages = {
            400: 'Solicitud inválida',
            401: 'No autorizado - sesión expirada',
            403: 'Acceso denegado',
            404: 'Recurso no encontrado',
            409: 'Conflicto de datos',
            422: 'Datos de validación incorrectos',
            500: 'Error interno del servidor'
        };

        const errorMessage = responseData.message || errorMessages[statusCode] || 'Error desconocido';

        switch (statusCode) {
            case 401:
                // Session expired
                applicationState.clearUser();
                if (!window.location.pathname.includes('login')) {
                    window.location.href = 'login.html';
                }
                break;
            case 403:
                UiManager.showNotification(errorMessage, 'error');
                break;
            case 404:
                UiManager.showNotification(errorMessage, 'warning');
                break;
            default:
                UiManager.showNotification(errorMessage, 'error');
        }

        throw new Error(errorMessage);
    }

    async handleNetworkError(error) {
        console.error('Network error:', error);
        UiManager.showNotification('Error de conexión de red', 'error');

        throw new Error('Network connection failed');
    }

    getStoredAuthToken() {
        try {
            return localStorage.getItem('clinicApp_authToken');
        } catch (error) {
            console.error('Error retrieving auth token:', error);
            return null;
        }
    }

    getCurrentUserId() {
        try {
            const userData = localStorage.getItem('clinicApp_currentUser');
            if (userData) {
                const user = JSON.parse(userData);
                return user?.idCard || null;
            }
        } catch (error) {
            console.error('Error retrieving current user ID:', error);
        }
        return null;
    }

    storeAuthToken(token) {
        try {
            localStorage.setItem('clinicApp_authToken', token);
        } catch (error) {
            console.error('Error storing auth token:', error);
        }
    }

    clearAuthToken() {
        try {
            localStorage.removeItem('clinicApp_authToken');
        } catch (error) {
            console.error('Error clearing auth token:', error);
        }
    }
}

// Global API client instance
const apiClient = new ApiClient();

// Session Management Module
class SessionManager {
    static saveSession(authToken, userData) {
        try {
            // Store authentication token
            apiClient.storeAuthToken(authToken);

            // Store user data
            applicationState.setUser(userData);

            console.log('Session saved successfully for user:', userData.fullName);
        } catch (error) {
            console.error('Error saving session:', error);
            throw new Error('Failed to save session');
        }
    }

    static clearSession() {
        try {
            applicationState.clearUser();
            apiClient.clearAuthToken();
            console.log('Session cleared successfully');
        } catch (error) {
            console.error('Error clearing session:', error);
        }
    }

    static isValidSession() {
        return applicationState.isAuthenticated && !applicationState.isSessionExpired();
    }

    static getCurrentUser() {
        return applicationState.currentUser;
    }

    static getCurrentRole() {
        return applicationState.currentRole;
    }

    static hasPermission(requiredPermission) {
        const userPermissions = applicationState.currentUser?.permissions || [];
        return userPermissions.includes(requiredPermission);
    }

    static canAccessResource(resourceType, action) {
        const requiredPermission = `${resourceType}_${action}`;
        return this.hasPermission(requiredPermission);
    }
}

/**
 * Obtiene el rol del usuario actual
 */
function getCurrentRole() {
    return currentRole;
}

/**
 * Verifica si el usuario tiene un permiso específico
 */
function hasPermission(permission) {
    if (!currentUser || !currentUser.permissions) {
        return false;
    }

    return currentUser.permissions.includes(permission);
}

/**
 * Verifica si el usuario puede acceder a un recurso
 */
function canAccessResource(resource, action) {
    return hasPermission(`${resource}_${action}`);
}

/**
 * Formatea una fecha para mostrar
 */
function formatDate(dateString) {
    const date = new Date(dateString);
    return date.toLocaleDateString('es-CO', {
        year: 'numeric',
        month: 'long',
        day: 'numeric'
    });
}

/**
 * Formatea una fecha y hora para mostrar
 */
function formatDateTime(dateString) {
    const date = new Date(dateString);
    return date.toLocaleString('es-CO', {
        year: 'numeric',
        month: 'long',
        day: 'numeric',
        hour: '2-digit',
        minute: '2-digit'
    });
}

/**
 * Valida un email
 */
function isValidEmail(email) {
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    return emailRegex.test(email);
}

/**
 * Valida una cédula (formato colombiano)
 */
function isValidIdCard(idCard) {
    // Para Colombia: 6-10 dígitos
    const idCardRegex = /^\d{6,10}$/;
    return idCardRegex.test(idCard);
}

/**
 * Valida un número de teléfono
 */
function isValidPhone(phone) {
    // Formato: 10 dígitos empezando por 3
    const phoneRegex = /^3\d{9}$/;
    return phoneRegex.test(phone);
}

/**
 * Capitaliza la primera letra de una cadena
 */
function capitalizeFirst(str) {
    return str.charAt(0).toUpperCase() + str.slice(1).toLowerCase();
}

/**
 * Genera un ID único
 */
function generateId() {
    return Date.now().toString(36) + Math.random().toString(36).substr(2);
}

/**
 * Debounce para evitar múltiples llamadas rápidas
 */
function debounce(func, wait) {
    let timeout;
    return function executedFunction(...args) {
        const later = () => {
            clearTimeout(timeout);
            func(...args);
        };
        clearTimeout(timeout);
        timeout = setTimeout(later, wait);
    };
}

/**
 * Throttle para limitar la frecuencia de ejecución
 */
function throttle(func, limit) {
    let inThrottle;
    return function() {
        const args = arguments;
        const context = this;
        if (!inThrottle) {
            func.apply(context, args);
            inThrottle = true;
            setTimeout(() => inThrottle = false, limit);
        }
    }
}

// Funciones específicas para cada página

/**
 * Funciones para index.html (página de bienvenida)
 */
function startApplication() {
    window.location.href = 'login.html';
}

function showInfo() {
    const modal = document.getElementById('infoModal');
    if (modal) {
        modal.style.display = 'block';
    }
}

function closeModal() {
    const modal = document.getElementById('infoModal');
    if (modal) {
        modal.style.display = 'none';
    }
}

/**
 * Funciones para login.html
 */
function togglePassword() {
    const passwordInput = document.getElementById('password');
    const toggleBtn = document.querySelector('.toggle-password i');

    if (passwordInput.type === 'password') {
        passwordInput.type = 'text';
        toggleBtn.classList.remove('fa-eye');
        toggleBtn.classList.add('fa-eye-slash');
    } else {
        passwordInput.type = 'password';
        toggleBtn.classList.remove('fa-eye-slash');
        toggleBtn.classList.add('fa-eye');
    }
}

function showForgotPassword() {
    const modal = document.getElementById('forgotPasswordModal');
    if (modal) {
        modal.style.display = 'block';
    }
}

function closeForgotPasswordModal() {
    const modal = document.getElementById('forgotPasswordModal');
    if (modal) {
        modal.style.display = 'none';
    }
}

/**
 * Funciones para dashboard.html
 */
function toggleSidebar() {
    const sidebar = document.getElementById('sidebar');
    const mainContent = document.getElementById('mainContent');

    if (sidebar && mainContent) {
        sidebar.classList.toggle('collapsed');
        mainContent.classList.toggle('sidebar-collapsed');
    }
}

function logout() {
    if (confirm('¿Está seguro de que desea cerrar sesión?')) {
        clearSession();
        window.location.href = 'index.html';
    }
}

function refreshData() {
    showLoading('Actualizando datos...');
    // Implementar lógica de actualización según la página actual
    setTimeout(() => {
        hideLoading();
        showSuccess('Datos actualizados correctamente');
    }, 1000);
}

function showNotifications() {
    const modal = document.getElementById('notificationsModal');
    if (modal) {
        modal.style.display = 'block';
        loadNotifications();
    }
}

function closeNotificationsModal() {
    const modal = document.getElementById('notificationsModal');
    if (modal) {
        modal.style.display = 'none';
    }
}

// Cerrar modal al hacer clic fuera de él
window.onclick = function(event) {
    const modals = document.querySelectorAll('.modal');
    modals.forEach(modal => {
        if (event.target === modal) {
            modal.style.display = 'none';
        }
    });
}

// Funciones de utilidad para formateo
const Utils = {
    formatCurrency: function(amount) {
        return new Intl.NumberFormat('es-CO', {
            style: 'currency',
            currency: 'COP'
        }).format(amount);
    },

    formatNumber: function(number) {
        return new Intl.NumberFormat('es-CO').format(number);
    },

    truncateText: function(text, maxLength) {
        if (text.length <= maxLength) return text;
        return text.substr(0, maxLength) + '...';
    }
};

// Page Handlers
class WelcomePageHandler {
    static initialize() {
        console.log('Welcome page initialized');
        // Add welcome page specific functionality
    }
}

class LoginPageHandler {
    static initialize() {
        console.log('Login page initialized');
        // Add login page specific functionality
    }
}

class DashboardPageHandler {
    static initialize() {
        console.log('Dashboard page initialized');
        // Add dashboard page specific functionality
    }
}

// Legacy function wrappers for backward compatibility
function showLoading(message = 'Loading...') {
    UiManager.showLoading(message);
}

function hideLoading() {
    UiManager.hideLoading();
}

function showError(message, duration = 5000) {
    UiManager.showNotification(message, 'error', duration);
}

function showSuccess(message, duration = 3000) {
    UiManager.showNotification(message, 'success', duration);
}

function saveSession(authToken, userData) {
    SessionManager.saveSession(authToken, userData);
}

function clearSession() {
    SessionManager.clearSession();
}

function getCurrentRole() {
    return SessionManager.getCurrentRole();
}

function hasPermission(permission) {
    return SessionManager.hasPermission(permission);
}

function canAccessResource(resource, action) {
    return SessionManager.canAccessResource(resource, action);
}

function formatDate(dateString) {
    return UtilityFunctions.formatDate(dateString);
}

function formatDateTime(dateString) {
    return UtilityFunctions.formatDateTime(dateString);
}

function isValidEmail(email) {
    return UtilityFunctions.isValidEmail(email);
}

function isValidIdCard(idCard) {
    return UtilityFunctions.isValidIdCard(idCard);
}

function isValidPhone(phone) {
    return UtilityFunctions.isValidPhoneNumber(phone);
}

function capitalizeFirst(str) {
    return UtilityFunctions.capitalizeFirstLetter(str);
}

function generateId() {
    return UtilityFunctions.generateUniqueId();
}

function debounce(func, wait) {
    return UtilityFunctions.debounce(func, wait);
}

function throttle(func, limit) {
    return UtilityFunctions.throttle(func, limit);
}

// Global application object
window.ClinicApp = {
    // Configuration
    config: ApplicationConfiguration,

    // State
    state: applicationState,

    // Core modules
    ui: UiManager,
    api: apiClient,
    session: SessionManager,
    utils: UtilityFunctions,

    // Page handlers
    pages: {
        welcome: WelcomePageHandler,
        login: LoginPageHandler,
        dashboard: DashboardPageHandler
    },

    // Legacy functions (for backward compatibility)
    showLoading,
    hideLoading,
    showError,
    showSuccess,
    saveSession,
    clearSession,
    getCurrentRole,
    hasPermission,
    canAccessResource,
    formatDate,
    formatDateTime,
    isValidEmail,
    isValidIdCard,
    isValidPhone: UtilityFunctions.isValidPhoneNumber,
    capitalizeFirst: UtilityFunctions.capitalizeFirstLetter,
    generateId: UtilityFunctions.generateUniqueId,
    debounce: UtilityFunctions.debounce,
    throttle: UtilityFunctions.throttle
};