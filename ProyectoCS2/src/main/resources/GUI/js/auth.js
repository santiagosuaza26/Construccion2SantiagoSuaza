/**
 * Sistema de Gestión Clínica CS2 - Manejo de Autenticación
 */

// Estado de la aplicación
let isLoggingIn = false;

/**
 * Muestra la pantalla de login
 */
function showLoginScreen() {
    const loginScreen = document.getElementById('login-screen');
    const dashboard = document.getElementById('dashboard');
    const loadingScreen = document.getElementById('loading-screen');

    // Ocultar otras pantallas
    if (dashboard) dashboard.style.display = 'none';
    if (loadingScreen) loadingScreen.classList.add('hidden');

    // Mostrar login
    if (loginScreen) {
        loginScreen.style.display = 'flex';

        // Enfocar en el campo de usuario
        setTimeout(() => {
            const usernameField = document.getElementById('username');
            if (usernameField) {
                usernameField.focus();
            }
        }, 100);
    }

    // Configurar evento del formulario
    const loginForm = document.getElementById('login-form');
    if (loginForm) {
        loginForm.onsubmit = handleLogin;
    }
}

/**
 * Oculta la pantalla de login
 */
function hideLoginScreen() {
    const loginScreen = document.getElementById('login-screen');
    if (loginScreen) {
        loginScreen.style.display = 'none';
    }
}

/**
 * Muestra el dashboard
 */
function showDashboard() {
    hideLoginScreen();
    hideLoadingScreen();

    const dashboard = document.getElementById('dashboard');
    if (dashboard) {
        dashboard.style.display = 'flex';
        setupRoleBasedMenu();
        showSection('dashboard-overview');
    }
}

/**
 * Oculta el dashboard
 */
function hideDashboard() {
    const dashboard = document.getElementById('dashboard');
    if (dashboard) {
        dashboard.style.display = 'none';
    }
}

/**
 * Muestra la pantalla de carga
 */
function showLoadingScreen() {
    const loadingScreen = document.getElementById('loading-screen');
    if (loadingScreen) {
        loadingScreen.classList.remove('hidden');
    }
}

/**
 * Oculta la pantalla de carga
 */
function hideLoadingScreen() {
    const loadingScreen = document.getElementById('loading-screen');
    if (loadingScreen) {
        loadingScreen.classList.add('hidden');
    }
}

/**
 * Maneja el envío del formulario de login con mejor manejo de errores
 */
async function handleLogin(event) {
    event.preventDefault();

    if (isLoggingIn) return;

    const username = document.getElementById('username').value.trim();
    const password = document.getElementById('password').value;

    // Validación básica
    if (!username || !password) {
        showLoginError('Por favor complete todos los campos');
        return;
    }

    if (username.length > 15) {
        showLoginError('El nombre de usuario no puede tener más de 15 caracteres');
        return;
    }

    if (password.length < 8) {
        showLoginError('La contraseña debe tener al menos 8 caracteres');
        return;
    }

    // Mostrar loading
    isLoggingIn = true;
    const submitBtn = document.querySelector('.login-btn');
    const originalText = submitBtn.innerHTML;

    submitBtn.innerHTML = '<i class="fas fa-spinner fa-spin"></i> Iniciando sesión...';
    submitBtn.disabled = true;

    try {
        // Verificar conexión con el servidor primero
        try {
            await AuthAPI.healthCheck();
        } catch (healthError) {
            console.warn('Health check falló:', healthError);
            // Continuar con el login aunque el health check falle
        }

        // Intentar hacer login
        const response = await AuthAPI.login({
            username: username,
            password: password
        });

        if (response && response.success && response.data) {
            // Guardar sesión
            AuthManager.setSession(response.data.user, response.data.token);

            // Mostrar dashboard
            showDashboard();

            // Cargar datos iniciales
            loadDashboardData();

            // Mostrar mensaje de bienvenida
            ApiUtils.showNotification(`¡Bienvenido, ${response.data.user.name}!`, 'success');

        } else {
            showLoginError(response.message || 'Error en las credenciales');
        }

    } catch (error) {
        console.error('Error durante login:', error);

        let errorMessage = 'Error al iniciar sesión';

        if (error?.message?.includes('Error de conexión')) {
            errorMessage = 'No se puede conectar con el servidor. Asegúrese de que el servidor esté ejecutándose en el puerto 8081.';
        } else if (error?.message?.includes('fetch')) {
            errorMessage = 'Error de red. Verifique su conexión a internet y que el servidor esté activo.';
        } else {
            errorMessage = error?.message || 'Credenciales incorrectas o problema con el servidor.';
        }

        showLoginError(errorMessage);
    } finally {
        // Restaurar botón
        isLoggingIn = false;
        submitBtn.innerHTML = originalText;
        submitBtn.disabled = false;
    }
}

/**
 * Muestra error en el formulario de login
 */
function showLoginError(message) {
    const errorDiv = document.getElementById('login-error');
    if (errorDiv) {
        errorDiv.textContent = message;
        errorDiv.style.display = 'block';

        // Ocultar después de 5 segundos
        setTimeout(() => {
            errorDiv.style.display = 'none';
        }, 5000);
    }
}

/**
 * Función para hacer logout
 */
async function logout() {
    try {
        // Llamar al API de logout
        await AuthAPI.logout();

        // Limpiar sesión local
        AuthManager.clearSession();

        // Mostrar pantalla de login
        showLoginScreen();

        // Mostrar mensaje
        ApiUtils.showNotification('Sesión cerrada correctamente', 'success');

    } catch (error) {
        console.error('Error durante logout:', error);

        // Incluso si hay error, limpiar sesión local
        AuthManager.clearSession();
        showLoginScreen();

        ApiUtils.showNotification('Sesión cerrada', 'info');
    }
}

/**
 * Configura el menú basado en el rol del usuario
 */
function setupRoleBasedMenu() {
    if (!currentUser) return;

    const role = currentUser.role;
    const userName = currentUser.name || 'Usuario';
    const userRoleText = getRoleDisplayName(role);

    // Actualizar información del usuario en el header
    const userNameElement = document.getElementById('user-name');
    const userRoleElement = document.getElementById('user-role');

    if (userNameElement) userNameElement.textContent = userName;
    if (userRoleElement) userRoleElement.textContent = userRoleText;

    // Mostrar/ocultar menús según el rol
    const menuVisibility = {
        'admin-menu': role === 'ADMINISTRATIVE',
        'doctor-menu': role === 'DOCTOR',
        'nurse-menu': role === 'NURSE',
        'hr-menu': role === 'HUMAN_RESOURCES',
        'support-menu': role === 'SUPPORT'
    };

    // Aplicar visibilidad de menús
    Object.entries(menuVisibility).forEach(([menuId, isVisible]) => {
        const menu = document.getElementById(menuId);
        if (menu) {
            menu.style.display = isVisible ? 'block' : 'none';
        }
    });

    // Actualizar mensaje de bienvenida según el rol
    updateWelcomeMessage(role);
}

/**
 * Obtiene el nombre de display para un rol
 */
function getRoleDisplayName(role) {
    const roleNames = {
        'ADMINISTRATIVE': 'Administrativo',
        'DOCTOR': 'Médico',
        'NURSE': 'Enfermera',
        'HUMAN_RESOURCES': 'Recursos Humanos',
        'SUPPORT': 'Soporte de Información'
    };

    return roleNames[role] || 'Usuario';
}

/**
 * Actualiza el mensaje de bienvenida según el rol
 */
function updateWelcomeMessage(role) {
    const welcomeMessage = document.getElementById('welcome-message');

    if (!welcomeMessage) return;

    const messages = {
        'ADMINISTRATIVE': 'Gestión de pacientes, citas y facturación',
        'DOCTOR': 'Historia clínica y órdenes médicas',
        'NURSE': 'Signos vitales y atención al paciente',
        'HUMAN_RESOURCES': 'Gestión de usuarios y personal',
        'SUPPORT': 'Inventario y soporte técnico'
    };

    welcomeMessage.textContent = messages[role] || 'Panel de control';
}

/**
 * Función para mostrar/ocultar contraseña
 */
function togglePassword() {
    const passwordField = document.getElementById('password');
    const toggleBtn = document.querySelector('.toggle-password i');

    if (passwordField) {
        if (passwordField.type === 'password') {
            passwordField.type = 'text';
            if (toggleBtn) {
                toggleBtn.classList.remove('fa-eye');
                toggleBtn.classList.add('fa-eye-slash');
            }
        } else {
            passwordField.type = 'password';
            if (toggleBtn) {
                toggleBtn.classList.remove('fa-eye-slash');
                toggleBtn.classList.add('fa-eye');
            }
        }
    }
}

/**
 * Función para alternar el sidebar en móviles
 */
function toggleSidebar() {
    const sidebar = document.getElementById('sidebar');
    const overlay = document.querySelector('.sidebar-overlay');

    if (window.innerWidth <= 768) {
        if (sidebar) {
            sidebar.classList.toggle('show');
        }

        // Crear overlay si no existe
        if (!overlay) {
            const newOverlay = document.createElement('div');
            newOverlay.className = 'sidebar-overlay';
            newOverlay.onclick = () => {
                if (sidebar) sidebar.classList.remove('show');
                newOverlay.classList.remove('show');
            };
            document.body.appendChild(newOverlay);

            // Pequeño delay para animación
            setTimeout(() => {
                newOverlay.classList.add('show');
            }, 10);
        } else {
            overlay.classList.toggle('show');
        }
    }
}

/**
 * Carga datos iniciales del dashboard
 */
async function loadDashboardData() {
    if (!AuthManager.isAuthenticated()) return;

    try {
        // Cargar datos según el rol del usuario
        const role = currentUser.role;

        switch (role) {
            case 'ADMINISTRATIVE':
                await loadAdministrativeData();
                break;
            case 'DOCTOR':
                await loadDoctorData();
                break;
            case 'NURSE':
                await loadNurseData();
                break;
            case 'HUMAN_RESOURCES':
                await loadHRData();
                break;
            case 'SUPPORT':
                await loadSupportData();
                break;
        }

        // Cargar actividad reciente
        await loadRecentActivity();

    } catch (error) {
        console.error('Error cargando datos del dashboard:', error);
        ApiUtils.handleError(error, 'Error al cargar datos del dashboard');
    }
}

/**
 * Carga datos para rol administrativo
 */
async function loadAdministrativeData() {
    try {
        // Cargar estadísticas básicas
        const patients = await PatientAPI.getAll();
        const invoices = await BillingAPI.getAll();

        // Actualizar tarjetas del dashboard
        updateDashboardCard('total-patients', patients.length);
        updateDashboardCard('pending-bills', invoices.filter(inv => inv.status === 'PENDING').length);

        // Cargar citas del día (simulado por ahora)
        updateDashboardCard('today-appointments', Math.floor(Math.random() * 20) + 5);

    } catch (error) {
        console.error('Error cargando datos administrativos:', error);
    }
}

/**
 * Carga datos para rol médico
 */
async function loadDoctorData() {
    try {
        const patients = await PatientAPI.getAll();
        const orders = await OrdersAPI.getAll();

        updateDashboardCard('total-patients', patients.length);
        updateDashboardCard('today-appointments', orders.filter(order =>
            order.status === 'PENDING' || order.status === 'IN_PROGRESS'
        ).length);

    } catch (error) {
        console.error('Error cargando datos médicos:', error);
    }
}

/**
 * Carga datos para rol de enfermería
 */
async function loadNurseData() {
    try {
        const patients = await PatientAPI.getAll();

        updateDashboardCard('total-patients', patients.length);
        updateDashboardCard('today-appointments', Math.floor(Math.random() * 15) + 3);

    } catch (error) {
        console.error('Error cargando datos de enfermería:', error);
    }
}

/**
 * Carga datos para recursos humanos
 */
async function loadHRData() {
    try {
        const users = await UsersAPI.getAll();

        updateDashboardCard('total-patients', users.length);

    } catch (error) {
        console.error('Error cargando datos de RRHH:', error);
    }
}

/**
 * Carga datos para soporte de información
 */
async function loadSupportData() {
    try {
        const inventory = await InventoryAPI.getAll();

        updateDashboardCard('low-stock', inventory.filter(item =>
            item.stock <= item.minStock
        ).length);

    } catch (error) {
        console.error('Error cargando datos de soporte:', error);
    }
}

/**
 * Actualiza una tarjeta del dashboard
 */
function updateDashboardCard(cardId, value) {
    const element = document.getElementById(cardId);
    if (element) {
        element.textContent = value.toString();
    }
}

/**
 * Carga actividad reciente
 */
async function loadRecentActivity() {
    const activityList = document.getElementById('recent-activity');
    if (!activityList) return;

    try {
        // Simular actividad reciente (en producción vendría de un endpoint)
        const activities = [
            {
                time: new Date(Date.now() - 1000 * 60 * 30), // 30 minutos atrás
                description: 'Paciente registrado',
                details: 'María González - Cédula: 12345678'
            },
            {
                time: new Date(Date.now() - 1000 * 60 * 60 * 2), // 2 horas atrás
                description: 'Orden médica creada',
                details: 'Orden #000123 - Laboratorio'
            },
            {
                time: new Date(Date.now() - 1000 * 60 * 60 * 4), // 4 horas atrás
                description: 'Factura generada',
                details: 'Factura #001234 - $150,000'
            }
        ];

        activityList.innerHTML = activities.map(activity => `
            <div class="activity-item">
                <div class="activity-content">
                    <div class="activity-time">${ApiUtils.formatDate(activity.time)}</div>
                    <div class="activity-description">${activity.description}</div>
                    <div class="activity-details">${activity.details}</div>
                </div>
            </div>
        `).join('');

    } catch (error) {
        console.error('Error cargando actividad reciente:', error);
        activityList.innerHTML = '<p>Error al cargar actividad reciente</p>';
    }
}

/**
 * Función para verificar permisos antes de acciones
 */
function checkPermission(resource, action = 'read') {
    if (!AuthManager.canAccess(resource, action)) {
        ApiUtils.showNotification('No tiene permisos para realizar esta acción', 'error');
        return false;
    }
    return true;
}

/**
 * Función para formatear el nombre de usuario para display
 */
function formatUserName(user) {
    if (!user) return 'Usuario';

    if (user.name) {
        return user.name.split(' ')[0]; // Solo primer nombre
    }

    return user.username || 'Usuario';
}

/**
 * Función para validar sesión activa
 */
function validateActiveSession() {
    if (!AuthManager.isAuthenticated()) {
        showLoginScreen();
        return false;
    }
    return true;
}

/**
 * Función para manejar errores de permisos
 */
function handlePermissionError() {
    ApiUtils.showNotification('No tiene permisos para acceder a esta funcionalidad', 'error');

    // Redirigir al dashboard si es posible
    if (AuthManager.isAuthenticated()) {
        showSection('dashboard-overview');
    } else {
        showLoginScreen();
    }
}

// Eventos de teclado para mejorar UX
document.addEventListener('keydown', function(event) {
    // Enter en formulario de login
    const loginScreen = document.getElementById('login-screen');
    if (event.key === 'Enter' && loginScreen && loginScreen.style.display !== 'none') {
        const loginForm = document.getElementById('login-form');
        if (loginForm && !isLoggingIn) {
            handleLogin(event);
        }
    }

    // Escape para cerrar modales y sidebar móvil
    if (event.key === 'Escape') {
        // Cerrar sidebar móvil
        const sidebar = document.getElementById('sidebar');
        const overlay = document.querySelector('.sidebar-overlay');

        if (sidebar && sidebar.classList.contains('show')) {
            sidebar.classList.remove('show');
            if (overlay) overlay.classList.remove('show');
        }

        // Cerrar modales
        const modals = document.querySelectorAll('.modal[style*="block"]');
        modals.forEach(modal => {
            modal.style.display = 'none';
        });
    }
});

// Manejo de clics fuera del sidebar móvil
document.addEventListener('click', function(event) {
    const sidebar = document.getElementById('sidebar');
    const menuToggle = document.querySelector('.menu-toggle');
    const overlay = document.querySelector('.sidebar-overlay');

    if (window.innerWidth <= 768 &&
        sidebar &&
        sidebar.classList.contains('show') &&
        !sidebar.contains(event.target) &&
        !menuToggle?.contains(event.target) &&
        !overlay?.contains(event.target)) {

        sidebar.classList.remove('show');
        if (overlay) overlay.classList.remove('show');
    }
});

// Inicializar eventos cuando el DOM esté listo
document.addEventListener('DOMContentLoaded', function() {
    // Configurar toggle de contraseña
    const togglePasswordBtn = document.querySelector('.toggle-password');
    if (togglePasswordBtn) {
        togglePasswordBtn.addEventListener('click', togglePassword);
    }

    // Configurar búsqueda en header
    const searchInput = document.querySelector('.search-box input');
    if (searchInput) {
        searchInput.addEventListener('input', function(event) {
            // Implementar búsqueda global aquí
            console.log('Búsqueda:', event.target.value);
        });
    }
});