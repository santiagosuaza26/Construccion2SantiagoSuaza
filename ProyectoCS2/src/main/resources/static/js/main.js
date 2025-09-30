/**
 * Sistema de Gestión Clínica CS2 - Archivo Principal
 * Coordinación general de la aplicación
 */

// Estado global de la aplicación
let isInitialized = false;

/**
 * Inicialización principal de la aplicación
 */
async function initializeApp() {
    if (isInitialized) return;

    try {
        console.log('Inicializando Sistema Clínico CS2...');

        // Verificar conexión con el servidor
        await checkServerConnection();

        // Configurar manejadores de eventos globales
        setupGlobalEventHandlers();

        // Configurar navegación por hash
        setupHashNavigation();

        // Inicializar funcionalidades específicas
        initializeModals();
        initializeForms();
        initializeNotifications();

        isInitialized = true;
        console.log('Aplicación inicializada correctamente');

    } catch (error) {
        console.error('Error durante inicialización:', error);
        showNotification('Error al inicializar la aplicación', 'error');
    }
}

/**
 * Verifica la conexión con el servidor
 */
async function checkServerConnection() {
    try {
        await AuthAPI.healthCheck();
        console.log('Conexión con servidor establecida');
    } catch (error) {
        console.warn('No se pudo verificar la conexión con el servidor:', error);
        // No lanzar error aquí, permitir funcionamiento offline si es posible
    }
}

/**
 * Configura manejadores de eventos globales
 */
function setupGlobalEventHandlers() {
    // Manejo de errores no capturados
    window.addEventListener('error', function(event) {
        console.error('Error no capturado:', event.error);
        showNotification('Error inesperado en la aplicación', 'error');
    });

    // Manejo de promesas rechazadas
    window.addEventListener('unhandledrejection', function(event) {
        console.error('Promesa rechazada no manejada:', event.reason);
        showNotification('Error inesperado en la aplicación', 'error');
    });

    // Manejo de conexión offline/online
    window.addEventListener('online', function() {
        showNotification('Conexión restablecida', 'success');
        // Recargar datos si es necesario
        if (currentSection && currentSection !== 'login') {
            loadSectionData(currentSection);
        }
    });

    window.addEventListener('offline', function() {
        showNotification('Sin conexión a internet', 'warning');
    });

    // Manejo de visibilidad de página
    document.addEventListener('visibilitychange', function() {
        if (document.visibilityState === 'visible' && AuthManager.isAuthenticated()) {
            // Recargar datos cuando la página vuelva a ser visible
            loadSectionData(currentSection);
        }
    });
}

/**
 * Configura navegación por hash
 */
function setupHashNavigation() {
    // Manejar cambios en el hash de la URL
    window.addEventListener('hashchange', function() {
        const hash = window.location.hash.substring(1); // Remover el #

        if (hash === 'login' || !AuthManager.isAuthenticated()) {
            showLoginScreen();
        } else if (AuthManager.isAuthenticated()) {
            // Si el hash corresponde a una sección válida, mostrarla
            if (document.getElementById(hash)) {
                showSection(hash);
            } else {
                showSection('dashboard-overview');
            }
        }
    });

    // Configurar navegación inicial basada en el hash actual
    const currentHash = window.location.hash.substring(1);
    if (currentHash && AuthManager.isAuthenticated()) {
        if (document.getElementById(currentHash)) {
            showSection(currentHash);
        } else {
            window.location.hash = 'dashboard-overview';
        }
    }
}

/**
 * Inicializa modales
 */
function initializeModals() {
    // Cerrar modales al hacer clic fuera de ellos
    document.addEventListener('click', function(event) {
        const modals = document.querySelectorAll('.modal[style*="block"]');
        modals.forEach(modal => {
            if (event.target === modal) {
                modal.style.display = 'none';
            }
        });
    });

    // Cerrar modales con tecla Escape
    document.addEventListener('keydown', function(event) {
        if (event.key === 'Escape') {
            const modals = document.querySelectorAll('.modal[style*="block"]');
            modals.forEach(modal => {
                modal.style.display = 'none';
            });
        }
    });
}

/**
 * Inicializa formularios
 */
function initializeForms() {
    // Configurar validación automática de formularios
    const forms = document.querySelectorAll('form');
    forms.forEach(form => {
        form.addEventListener('submit', function(event) {
            if (!form.checkValidity()) {
                event.preventDefault();
                event.stopPropagation();
                showNotification('Por favor complete todos los campos requeridos', 'warning');
            }
            form.classList.add('was-validated');
        });
    });

    // Configurar inputs para validación en tiempo real
    const inputs = document.querySelectorAll('input, select, textarea');
    inputs.forEach(input => {
        input.addEventListener('blur', function() {
            validateField(this);
        });
    });
}

/**
 * Valida un campo individual
 */
function validateField(field) {
    const value = field.value.trim();
    let isValid = true;
    let message = '';

    // Validar requerido
    if (field.hasAttribute('required') && !value) {
        isValid = false;
        message = 'Este campo es requerido';
    }

    // Validar tipo email
    if (field.type === 'email' && value && !isValidEmail(value)) {
        isValid = false;
        message = 'Email inválido';
    }

    // Validar longitud mínima
    if (field.hasAttribute('minlength')) {
        const minLength = parseInt(field.getAttribute('minlength'));
        if (value.length < minLength) {
            isValid = false;
            message = `Mínimo ${minLength} caracteres`;
        }
    }

    // Validar patrón
    if (field.hasAttribute('pattern')) {
        const pattern = new RegExp(field.getAttribute('pattern'));
        if (value && !pattern.test(value)) {
            isValid = false;
            message = 'Formato inválido';
        }
    }

    // Mostrar/ocultar error
    showFieldError(field, isValid ? null : message);

    return isValid;
}

/**
 * Muestra error de campo
 */
function showFieldError(field, message) {
    // Remover error anterior
    const existingError = field.parentNode.querySelector('.field-error');
    if (existingError) {
        existingError.remove();
    }

    if (message) {
        const errorElement = document.createElement('div');
        errorElement.className = 'field-error';
        errorElement.textContent = message;
        errorElement.style.cssText = `
            color: var(--danger-color);
            font-size: 0.75rem;
            margin-top: 0.25rem;
        `;

        field.parentNode.appendChild(errorElement);
    }
}

/**
 * Inicializa sistema de notificaciones
 */
function initializeNotifications() {
    // Crear contenedor de notificaciones si no existe
    if (!document.querySelector('.notifications-container')) {
        const container = document.createElement('div');
        container.className = 'notifications-container';
        container.style.cssText = `
            position: fixed;
            top: 70px;
            right: 1rem;
            z-index: 3000;
            max-width: 400px;
        `;
        document.body.appendChild(container);
    }
}

/**
 * Función global para mostrar notificaciones
 */
function showNotification(message, type = 'info') {
    return ApiUtils.showNotification(message, type);
}

/**
 * Función para mostrar mensajes de carga
 */
function showLoading(message = 'Cargando...') {
    const loading = document.createElement('div');
    loading.className = 'loading-overlay';
    loading.innerHTML = `
        <div class="loading-content">
            <i class="fas fa-spinner fa-spin"></i>
            <p>${message}</p>
        </div>
    `;

    loading.style.cssText = `
        position: absolute;
        top: 0;
        left: 0;
        width: 100%;
        height: 100%;
        background: rgba(255, 255, 255, 0.9);
        display: flex;
        align-items: center;
        justify-content: center;
        z-index: 1000;
        border-radius: 0.5rem;
    `;

    return loading;
}

/**
 * Función para formatear fechas
 */
function formatDate(dateString) {
    return ApiUtils.formatDate(dateString);
}

/**
 * Función para formatear moneda
 */
function formatCurrency(amount) {
    return ApiUtils.formatCurrency(amount);
}

/**
 * Función para validar email
 */
function isValidEmail(email) {
    return ApiUtils.isValidEmail(email);
}

/**
 * Función para validar cédula
 */
function isValidIdCard(idCard) {
    return ApiUtils.isValidIdCard(idCard);
}

/**
 * Función para validar teléfono
 */
function isValidPhone(phone) {
    return ApiUtils.isValidPhone(phone);
}

/**
 * Función para confirmar acciones
 */
function confirmAction(message, callback) {
    if (confirm(message)) {
        callback();
    }
}

/**
 * Función para hacer logout desde cualquier lugar
 */
function globalLogout() {
    logout();
}

/**
 * Función para refrescar datos actuales
 */
function refreshData() {
    loadSectionData(currentSection);
    showNotification('Datos actualizados', 'success');
}

/**
 * Función para mostrar información de la aplicación
 */
function showAppInfo() {
    const info = `
        <div style="text-align: left;">
            <h4>Sistema de Gestión Clínica CS2</h4>
            <p><strong>Versión:</strong> 1.0</p>
            <p><strong>Servidor:</strong> ${window.location.origin}</p>
            <p><strong>Estado:</strong> <span style="color: var(--success-color);">Conectado</span></p>
            <p><strong>Usuario:</strong> ${currentUser ? currentUser.name : 'No autenticado'}</p>
            <p><strong>Rol:</strong> ${currentUser ? getRoleDisplayName(currentUser.role) : 'N/A'}</p>
        </div>
    `;
    showNotification(info, 'info');
}

/**
 * Función para diagnosticar problemas de conexión
 */
async function diagnoseConnection() {
    const diagnosis = {
        timestamp: new Date().toISOString(),
        serverUrl: window.location.origin + '/api',
        healthCheck: false,
        staticFiles: false,
        authentication: false,
        errors: []
    };

    showNotification('Diagnóstico de conexión iniciado...', 'info');

    try {
        // 1. Verificar archivos estáticos
        try {
            const staticResponse = await fetch(window.location.origin + '/api/');
            diagnosis.staticFiles = staticResponse.ok;
            if (!staticResponse.ok) {
                diagnosis.errors.push(`Archivos estáticos: ${staticResponse.status} ${staticResponse.statusText}`);
            }
        } catch (error) {
            diagnosis.errors.push(`Error archivos estáticos: ${error.message}`);
        }

        // 2. Verificar health check
        try {
            await AuthAPI.healthCheck();
            diagnosis.healthCheck = true;
        } catch (error) {
            diagnosis.errors.push(`Health check: ${error.message}`);
        }

        // 3. Verificar si hay usuarios de prueba
        if (AuthManager.isAuthenticated()) {
            diagnosis.authentication = true;
        }

        // Mostrar resultados
        const results = `
            <div style="text-align: left;">
                <h4>Diagnóstico de Conexión</h4>
                <p><strong>Servidor:</strong> ${diagnosis.serverUrl}</p>
                <p><strong>Archivos estáticos:</strong> ${diagnosis.staticFiles ? '✅ OK' : '❌ Error'}</p>
                <p><strong>Health Check:</strong> ${diagnosis.healthCheck ? '✅ OK' : '❌ Error'}</p>
                <p><strong>Autenticación:</strong> ${diagnosis.authentication ? '✅ OK' : '⚠️ Pendiente'}</p>
                ${diagnosis.errors.length > 0 ? `<p><strong>Errores:</strong></p><ul>${diagnosis.errors.map(e => `<li>${e}</li>`).join('')}</ul>` : '<p><strong>Estado:</strong> Todo funcionando correctamente</p>'}
                <p><strong>¿Necesita ayuda?</strong> Asegúrese de que:</p>
                <ul>
                    <li>El servidor esté corriendo en el puerto 8081</li>
                    <li>Los datos de prueba se hayan inicializado</li>
                    <li>Las credenciales sean correctas</li>
                </ul>
            </div>
        `;

        showNotification(results, diagnosis.errors.length > 0 ? 'warning' : 'success');

    } catch (error) {
        showNotification(`Error durante diagnóstico: ${error.message}`, 'error');
    }
}

/**
 * Función para mostrar ayuda
 */
function showHelp() {
    const helpContent = `
        <div style="text-align: left;">
            <h4>Sistema de Gestión Clínica CS2</h4>
            <p><strong>Teclas rápidas:</strong></p>
            <ul>
                <li><kbd>Ctrl</kbd> + <kbd>K</kbd>: Buscar</li>
                <li><kbd>F5</kbd>: Actualizar datos</li>
                <li><kbd>Esc</kbd>: Cerrar modales</li>
            </ul>
            <p><strong>Soporte:</strong> Contacte al administrador del sistema</p>
        </div>
    `;

    showNotification(helpContent, 'info');
}

// Atajos de teclado globales
document.addEventListener('keydown', function(event) {
    // Ctrl + K para buscar
    if (event.ctrlKey && event.key === 'k') {
        event.preventDefault();
        const searchInput = document.querySelector('.search-box input');
        if (searchInput) {
            searchInput.focus();
            searchInput.select();
        }
    }

    // F5 para refrescar
    if (event.key === 'F5') {
        event.preventDefault();
        refreshData();
    }

    // F1 para ayuda
    if (event.key === 'F1') {
        event.preventDefault();
        showHelp();
    }

    // Ctrl + Shift + L para logout
    if (event.ctrlKey && event.shiftKey && event.key === 'L') {
        event.preventDefault();
        globalLogout();
    }
});

// Inicializar aplicación cuando el DOM esté listo
document.addEventListener('DOMContentLoaded', function() {
    initializeApp();

    // Configurar menú contextual si es necesario
    document.addEventListener('contextmenu', function(event) {
        // Prevenir menú contextual por defecto si es necesario
        // event.preventDefault();
    });

    // Configurar service worker si está disponible (para funcionalidad offline)
    if ('serviceWorker' in navigator) {
        window.addEventListener('load', function() {
            // Implementar service worker si es necesario para funcionalidad offline
        });
    }

    console.log('DOM inicializado - Sistema Clínico CS2 listo');
});

/**
 * Función para exportar configuración de debug
 */
function exportDebugInfo() {
    const debugInfo = {
        user: currentUser,
        currentSection: currentSection,
        isAuthenticated: AuthManager.isAuthenticated(),
        timestamp: new Date().toISOString(),
        userAgent: navigator.userAgent,
        url: window.location.href,
        viewport: {
            width: window.innerWidth,
            height: window.innerHeight
        }
    };

    console.log('Debug Info:', debugInfo);
    return debugInfo;
}

// Hacer funciones disponibles globalmente
window.showNotification = showNotification;
window.formatDate = formatDate;
window.formatCurrency = formatCurrency;
window.isValidEmail = isValidEmail;
window.isValidIdCard = isValidIdCard;
window.isValidPhone = isValidPhone;
window.confirmAction = confirmAction;
window.globalLogout = globalLogout;
window.refreshData = refreshData;
window.showAppInfo = showAppInfo;
window.showHelp = showHelp;
window.diagnoseConnection = diagnoseConnection;
window.exportDebugInfo = exportDebugInfo;