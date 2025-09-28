// JavaScript para manejo de autenticación

// Configuración de autenticación
const AUTH_CONFIG = {
    API_URL: window.location.origin + '/api',
    TOKEN_KEY: 'authToken',
    USER_KEY: 'currentUser',
    LOGIN_ENDPOINT: '/auth/login',
    LOGOUT_ENDPOINT: '/auth/logout',
    USER_INFO_ENDPOINT: '/auth/me'
};

/**
 * Maneja el envío del formulario de login
 */
document.addEventListener('DOMContentLoaded', function() {
    const loginForm = document.getElementById('loginForm');
    if (loginForm) {
        loginForm.addEventListener('submit', handleLogin);
    }
});

/**
 * Maneja el proceso de login
 */
async function handleLogin(event) {
    event.preventDefault();

    const username = document.getElementById('username').value.trim();
    const password = document.getElementById('password').value;
    const remember = document.getElementById('remember').checked;

    // Validación básica
    if (!username || !password) {
        showError('Por favor complete todos los campos');
        return;
    }

    if (username.length < 3) {
        showError('El usuario debe tener al menos 3 caracteres');
        return;
    }

    if (password.length < 6) {
        showError('La contraseña debe tener al menos 6 caracteres');
        return;
    }

    showLoading('Iniciando sesión...');

    try {
        const loginData = {
            username: username,
            password: password
        };

        console.log('Attempting login for user:', username);

        const response = await fetch(`${AUTH_CONFIG.API_URL}${AUTH_CONFIG.LOGIN_ENDPOINT}`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(loginData)
        });

        const result = await response.json();

        if (response.ok && result.success) {
            // Login exitoso
            console.log('Login successful:', result.data);

            // Guardar sesión
            saveSession(result.data.sessionToken || 'demo-token', result.data);

            // Mostrar mensaje de éxito
            showSuccess(`¡Bienvenido, ${result.data.fullName}!`);

            // Redirigir al dashboard después de un breve delay
            setTimeout(() => {
                window.location.href = 'dashboard.html';
            }, 1500);

        } else {
            // Login fallido
            console.error('Login failed:', result.message);
            showError(result.message || 'Usuario o contraseña incorrectos');
            hideLoading();
        }

    } catch (error) {
        console.error('Login error:', error);
        showError('Error de conexión. Verifique que el servidor esté funcionando.');
        hideLoading();
    }
}

/**
 * Intenta login con credenciales de demostración
 */
function tryDemoLogin(role) {
    const demoCredentials = {
        'ADMINISTRATIVE': { username: 'admin', password: 'admin123' },
        'DOCTOR': { username: 'doctor', password: 'doctor123' },
        'NURSE': { username: 'nurse', password: 'nurse123' },
        'HUMAN_RESOURCES': { username: 'hr', password: 'hr123' },
        'SUPPORT': { username: 'support', password: 'support123' }
    };

    const creds = demoCredentials[role];
    if (creds) {
        document.getElementById('username').value = creds.username;
        document.getElementById('password').value = creds.password;

        // Simular login automático para demostración
        setTimeout(() => {
            handleLogin(new Event('submit'));
        }, 500);
    }
}

/**
 * Carga la información del usuario actual
 */
async function loadCurrentUser() {
    try {
        const token = localStorage.getItem(AUTH_CONFIG.TOKEN_KEY);
        if (!token) {
            redirectToLogin();
            return null;
        }

        const response = await fetch(`${AUTH_CONFIG.API_URL}${AUTH_CONFIG.USER_INFO_ENDPOINT}`, {
            headers: {
                'Authorization': `Bearer ${token}`,
                'User-ID': getCurrentUserId()
            }
        });

        if (response.ok) {
            const result = await response.json();
            if (result.success) {
                currentUser = result.data;
                return result.data;
            }
        }

        // Si falla la validación del token, limpiar sesión
        clearSession();
        redirectToLogin();
        return null;

    } catch (error) {
        console.error('Error loading current user:', error);
        clearSession();
        redirectToLogin();
        return null;
    }
}

/**
 * Obtiene el ID del usuario actual
 */
function getCurrentUserId() {
    const user = localStorage.getItem(AUTH_CONFIG.USER_KEY);
    if (user) {
        try {
            const userData = JSON.parse(user);
            return userData.idCard;
        } catch (e) {
            return null;
        }
    }
    return null;
}

/**
 * Obtiene el usuario actual del localStorage
 */
function getCurrentUser() {
    const user = localStorage.getItem(AUTH_CONFIG.USER_KEY);
    if (user) {
        try {
            return JSON.parse(user);
        } catch (e) {
            return null;
        }
    }
    return null;
}

/**
 * Realiza logout
 */
async function performLogout() {
    try {
        const token = localStorage.getItem(AUTH_CONFIG.TOKEN_KEY);
        const userId = getCurrentUserId();

        if (token && userId) {
            await fetch(`${AUTH_CONFIG.API_URL}${AUTH_CONFIG.LOGOUT_ENDPOINT}`, {
                method: 'POST',
                headers: {
                    'Authorization': `Bearer ${token}`,
                    'User-ID': userId,
                    'Content-Type': 'application/json'
                }
            });
        }
    } catch (error) {
        console.error('Logout error:', error);
    } finally {
        clearSession();
        window.location.href = 'index.html';
    }
}

/**
 * Limpia la sesión local
 */
function clearSession() {
    localStorage.removeItem(AUTH_CONFIG.TOKEN_KEY);
    localStorage.removeItem(AUTH_CONFIG.USER_KEY);
    currentUser = null;
    isAuthenticated = false;
}

/**
 * Redirige al login si no hay sesión
 */
function redirectToLogin() {
    if (!window.location.pathname.includes('login')) {
        window.location.href = 'login.html';
    }
}

/**
 * Verifica si el usuario está autenticado
 */
function isAuthenticated() {
    const token = localStorage.getItem(AUTH_CONFIG.TOKEN_KEY);
    const user = localStorage.getItem(AUTH_CONFIG.USER_KEY);
    return token && user;
}

/**
 * Obtiene los permisos del usuario actual
 */
function getUserPermissions() {
    const user = getCurrentUser();
    return user ? (user.permissions || []) : [];
}

/**
 * Verifica si el usuario tiene un permiso específico
 */
function hasPermission(permission) {
    const permissions = getUserPermissions();
    return permissions.includes(permission);
}

/**
 * Verifica si el usuario puede acceder a un recurso
 */
function canAccessResource(resource, action) {
    return hasPermission(`${resource}_${action}`);
}

/**
 * Obtiene el rol del usuario actual
 */
function getCurrentRole() {
    const user = getCurrentUser();
    return user ? user.role : null;
}

/**
 * Configura el autocompletado de demostración
 */
function setupDemoCredentials() {
    // Agregar event listeners a los campos de demostración
    const demoSections = document.querySelectorAll('.demo-credentials .credential-item');

    demoSections.forEach(section => {
        section.style.cursor = 'pointer';
        section.title = 'Haga clic para autocompletar';

        section.addEventListener('click', function() {
            const text = this.textContent;
            const roleMatch = text.match(/(\w+):/);
            if (roleMatch) {
                const role = roleMatch[1];
                tryDemoLogin(role);
            }
        });

        section.addEventListener('mouseenter', function() {
            this.style.backgroundColor = 'rgba(37, 99, 235, 0.1)';
        });

        section.addEventListener('mouseleave', function() {
            this.style.backgroundColor = 'transparent';
        });
    });
}

/**
 * Inicializa la página de login
 */
function initializeLoginPage() {
    // Configurar autocompletado de demostración
    setupDemoCredentials();

    // Verificar si ya hay una sesión activa
    if (isAuthenticated()) {
        window.location.href = 'dashboard.html';
        return;
    }

    // Configurar toggle de contraseña
    const passwordInput = document.getElementById('password');
    const toggleBtn = document.querySelector('.toggle-password');

    if (passwordInput && toggleBtn) {
        toggleBtn.addEventListener('click', function() {
            const icon = this.querySelector('i');

            if (passwordInput.type === 'password') {
                passwordInput.type = 'text';
                icon.classList.remove('fa-eye');
                icon.classList.add('fa-eye-slash');
            } else {
                passwordInput.type = 'password';
                icon.classList.remove('fa-eye-slash');
                icon.classList.add('fa-eye');
            }
        });
    }

    // Auto-focus en el campo de usuario
    const usernameInput = document.getElementById('username');
    if (usernameInput) {
        usernameInput.focus();
    }
}

/**
 * Valida las credenciales antes del envío
 */
function validateCredentials(username, password) {
    const errors = [];

    if (!username || username.trim().length < 3) {
        errors.push('El usuario debe tener al menos 3 caracteres');
    }

    if (!password || password.length < 6) {
        errors.push('La contraseña debe tener al menos 6 caracteres');
    }

    return errors;
}

/**
 * Muestra errores de validación en el formulario
 */
function showValidationErrors(errors) {
    // Remover errores anteriores
    const existingErrors = document.querySelectorAll('.validation-error');
    existingErrors.forEach(error => error.remove());

    // Mostrar nuevos errores
    errors.forEach(error => {
        const errorDiv = document.createElement('div');
        errorDiv.className = 'validation-error';
        errorDiv.style.cssText = `
            color: var(--danger-color);
            font-size: 0.85rem;
            margin-top: 5px;
            display: flex;
            align-items: center;
            gap: 5px;
        `;
        errorDiv.innerHTML = `<i class="fas fa-exclamation-circle"></i> ${error}`;

        // Insertar después del campo correspondiente
        const formGroups = document.querySelectorAll('.form-group');
        formGroups[formGroups.length - 1].appendChild(errorDiv);
    });
}

// Inicializar página de login si estamos en ella
if (window.location.pathname.includes('login')) {
    document.addEventListener('DOMContentLoaded', initializeLoginPage);
}