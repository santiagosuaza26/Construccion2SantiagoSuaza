/**
 * Servicio de autenticación y autorización
 * Maneja el login, logout y la gestión de sesiones de usuario
 */
class AuthService {
    constructor() {
        this.currentUser = null;
        this.apiService = new ApiService();
        this.loadSession();
    }

    /**
     * Carga la sesión del usuario desde localStorage
     */
    loadSession() {
        const userKey = window.APP_CONFIG ? window.APP_CONFIG.AUTH.USER_KEY : 'currentUser';
        const tokenKey = window.APP_CONFIG ? window.APP_CONFIG.AUTH.TOKEN_KEY : 'authToken';

        const userData = localStorage.getItem(userKey);
        const token = localStorage.getItem(tokenKey);

        if (userData && token) {
            try {
                this.currentUser = JSON.parse(userData);
                this.apiService.setToken(token);
            } catch (error) {
                console.error('Error loading session:', error);
                this.clearSession();
            }
        }
    }

    /**
     * Guarda la sesión del usuario en localStorage
     */
    saveSession(user, token) {
        this.currentUser = user;
        this.apiService.setToken(token);

        localStorage.setItem('currentUser', JSON.stringify(user));
        localStorage.setItem('authToken', token);
    }

    /**
     * Limpia la sesión del usuario
     */
    clearSession() {
        this.currentUser = null;
        this.apiService.setToken(null);

        const userKey = window.APP_CONFIG ? window.APP_CONFIG.AUTH.USER_KEY : 'currentUser';
        const tokenKey = window.APP_CONFIG ? window.APP_CONFIG.AUTH.TOKEN_KEY : 'authToken';

        localStorage.removeItem(userKey);
        localStorage.removeItem(tokenKey);
    }

    /**
     * Inicia sesión con usuario y contraseña
     */
    async login(username, password) {
        try {
            // Verificar conexión con el backend primero
            const connectionCheck = await this.checkBackendConnection();
            if (!connectionCheck.connected) {
                throw new Error('No se puede conectar con el servidor. Verifique que el backend esté corriendo.');
            }

            // Usar el endpoint de autenticación real
            const response = await fetch(`${this.apiService.baseURL}/auth/login`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({
                    username: username,
                    password: password
                })
            });

            if (!response.ok) {
                const errorData = await response.json().catch(() => ({}));

                // Manejar diferentes tipos de errores HTTP
                if (response.status === 401 || response.status === 403) {
                    throw new Error('Credenciales inválidas. Verifique su usuario y contraseña.');
                } else if (response.status === 404) {
                    throw new Error('Servicio de autenticación no encontrado. Contacte al administrador.');
                } else if (response.status >= 500) {
                    throw new Error('Error interno del servidor. Intente nuevamente más tarde.');
                } else {
                    throw new Error(errorData.message || `Error del servidor (${response.status})`);
                }
            }

            const data = await response.json();

            if (data.success && data.user && data.token) {
                this.saveSession(data.user, data.token);
                return {
                    success: true,
                    user: data.user,
                    token: data.token
                };
            } else {
                throw new Error(data.message || 'Error de autenticación');
            }

        } catch (error) {
            console.error('Login error:', error);

            // Proporcionar mensajes de error más específicos
            if (error.message.includes('fetch')) {
                throw new Error('Error de conexión. Verifique su conexión a internet y que el backend esté corriendo en el puerto 8080.');
            } else if (error.message.includes('CORS')) {
                throw new Error('Error de configuración del servidor. Contacte al administrador.');
            } else {
                throw new Error(error.message || 'Error inesperado durante el inicio de sesión');
            }
        }
    }

    /**
     * Cierra la sesión del usuario
     */
    logout() {
        this.clearSession();
        this.showLoginPage();
    }

    /**
     * Verifica si el usuario está autenticado
     */
    isAuthenticated() {
        return this.currentUser !== null && this.apiService.isAuthenticated();
    }

    /**
     * Verifica la conexión con el backend
     */
    async checkBackendConnection() {
        try {
            const response = await fetch(`${this.apiService.baseURL}/public/health`, {
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
    }

    /**
     * Obtiene el usuario actual
     */
    getCurrentUser() {
        return this.currentUser;
    }

    /**
     * Obtiene el rol del usuario actual
     */
    getCurrentUserRole() {
        return this.currentUser ? this.currentUser.role : null;
    }

    /**
     * Verifica si el usuario tiene un rol específico
     */
    hasRole(role) {
        return this.currentUser && this.currentUser.role === role;
    }

    /**
     * Verifica si el usuario tiene alguno de los roles especificados
     */
    hasAnyRole(roles) {
        return this.currentUser && roles.includes(this.currentUser.role);
    }

    /**
     * Verifica si el usuario puede acceder a una funcionalidad específica
     */
    canAccess(feature) {
        if (!this.currentUser) return false;

        const permissions = {
            'manage-users': ['HUMAN_RESOURCES'],
            'register-patients': ['ADMINISTRATIVE_STAFF', 'HUMAN_RESOURCES'],
            'view-patients': ['ADMINISTRATIVE_STAFF', 'DOCTOR', 'NURSE', 'HUMAN_RESOURCES'],
            'manage-medical-records': ['DOCTOR', 'HUMAN_RESOURCES'],
            'manage-orders': ['DOCTOR'],
            'manage-patient-visits': ['DOCTOR', 'NURSE', 'HUMAN_RESOURCES'],
            'manage-billing': ['ADMINISTRATIVE_STAFF', 'HUMAN_RESOURCES'],
            'manage-inventory': ['INFORMATION_SUPPORT', 'HUMAN_RESOURCES'],
            'manage-appointments': ['ADMINISTRATIVE_STAFF', 'HUMAN_RESOURCES']
        };

        const allowedRoles = permissions[feature];
        return allowedRoles ? this.hasAnyRole(allowedRoles) : false;
    }

    /**
     * Genera un token mock para simular autenticación JWT
     * En producción, esto debería hacerse en el servidor
     */
    generateMockToken(user) {
        const header = btoa(JSON.stringify({ alg: 'HS256', typ: 'JWT' }));
        const payload = btoa(JSON.stringify({
            sub: user.cedula,
            username: user.username,
            role: user.role,
            exp: Math.floor(Date.now() / 1000) + (24 * 60 * 60) // 24 horas
        }));
        const signature = btoa('mock-signature');

        return `${header}.${payload}.${signature}`;
    }

    /**
     * Valida el token JWT (simulado)
     */
    validateToken(token) {
        try {
            const parts = token.split('.');
            if (parts.length !== 3) return false;

            const payload = JSON.parse(atob(parts[1]));
            const currentTime = Math.floor(Date.now() / 1000);

            return payload.exp > currentTime;
        } catch (error) {
            return false;
        }
    }

    /**
     * Muestra la página de login
     */
    showLoginPage() {
        document.getElementById('welcome-page').style.display = 'none';
        document.getElementById('login-page').style.display = 'flex';
        document.getElementById('dashboard').style.display = 'none';
    }

    /**
     * Muestra la página de bienvenida
     */
    showWelcomePage() {
        document.getElementById('welcome-page').style.display = 'flex';
        document.getElementById('login-page').style.display = 'none';
        document.getElementById('dashboard').style.display = 'none';
    }

    /**
     * Muestra el dashboard
     */
    showDashboard() {
        document.getElementById('welcome-page').style.display = 'none';
        document.getElementById('login-page').style.display = 'none';
        document.getElementById('dashboard').style.display = 'flex';
    }

    /**
     * Inicializa los event listeners de autenticación
     */
    initializeAuthListeners() {
        // Botón de inicio (welcome page)
        const startBtn = document.getElementById('start-btn');
        if (startBtn) {
            startBtn.addEventListener('click', () => {
                this.showLoginPage();
            });
        }

        // Botón de volver (login page)
        const backBtn = document.getElementById('back-to-welcome');
        if (backBtn) {
            backBtn.addEventListener('click', () => {
                this.showWelcomePage();
            });
        }

        // Formulario de login
        const loginForm = document.getElementById('login-form');
        if (loginForm) {
            loginForm.addEventListener('submit', async (e) => {
                e.preventDefault();
                await this.handleLogin();
            });
        }

        // Toggle de contraseña
        const togglePassword = document.getElementById('toggle-password');
        if (togglePassword) {
            togglePassword.addEventListener('click', () => {
                this.togglePasswordVisibility();
            });
        }

        // Botón de logout
        const logoutBtn = document.getElementById('logout-btn');
        if (logoutBtn) {
            logoutBtn.addEventListener('click', () => {
                this.logout();
            });
        }

        // Verificar autenticación al cargar la página
        this.checkAuthentication();
    }

    /**
     * Maneja el proceso de login
     */
    async handleLogin() {
        const username = document.getElementById('username').value.trim();
        const password = document.getElementById('password').value;

        if (!username || !password) {
            this.showLoginError('Por favor ingrese usuario y contraseña');
            return;
        }

        // Validar formato del usuario
        if (username.length < 3) {
            this.showLoginError('El nombre de usuario debe tener al menos 3 caracteres');
            return;
        }

        // Validar contraseña
        if (password.length < 6) {
            this.showLoginError('La contraseña debe tener al menos 6 caracteres');
            return;
        }

        try {
            // Mostrar loading con mensaje específico
            this.showLoading(true, 'Verificando conexión...');

            // Verificar conexión primero
            const connectionCheck = await this.checkBackendConnection();
            if (!connectionCheck.connected) {
                this.showLoading(false);
                this.showLoginError('No se puede conectar con el servidor. Asegúrese de que el backend esté corriendo en el puerto 8080.');
                return;
            }

            this.showLoading(true, 'Iniciando sesión...');

            // Intentar login
            const result = await this.login(username, password);

            if (result.success) {
                // Ocultar error si existe
                this.hideLoginError();

                // Mostrar dashboard
                this.showDashboard();

                // Actualizar información del usuario en el header
                this.updateUserInfo();

                // Inicializar dashboard según el rol
                if (window.dashboardService) {
                    window.dashboardService.initializeForRole(result.user.role);
                }

                // Mostrar mensaje de éxito
                if (window.notificationService) {
                    window.notificationService.success(`¡Bienvenido, ${result.user.fullName || result.user.username}!`);
                }
            }

        } catch (error) {
            console.error('Login error:', error);

            // Mostrar diferentes tipos de errores con mensajes específicos
            let errorMessage = error.message;

            if (error.message.includes('Error de conexión')) {
                errorMessage = 'Error de conexión. Verifique que el backend esté corriendo correctamente.';
            } else if (error.message.includes('Credenciales inválidas')) {
                errorMessage = 'Usuario o contraseña incorrectos. Verifique sus credenciales.';
            } else if (error.message.includes('Usuario no encontrado')) {
                errorMessage = 'Usuario no encontrado. Contacte al administrador.';
            } else if (error.message.includes('Usuario inactivo')) {
                errorMessage = 'Usuario inactivo. Contacte al administrador del sistema.';
            }

            this.showLoginError(errorMessage);
        } finally {
            this.showLoading(false);
        }
    }

    /**
     * Muestra/oculta el indicador de carga
     */
    showLoading(show, message = 'Cargando...') {
        const loadingOverlay = document.getElementById('loading-overlay');
        if (loadingOverlay) {
            loadingOverlay.style.display = show ? 'flex' : 'none';

            // Actualizar mensaje de carga si existe el elemento
            const loadingMessage = loadingOverlay.querySelector('p');
            if (loadingMessage && show) {
                loadingMessage.textContent = message;
            }
        }
    }

    /**
     * Muestra error en el formulario de login
     */
    showLoginError(message) {
        const errorElement = document.getElementById('login-error');
        if (errorElement) {
            errorElement.textContent = message;
            errorElement.style.display = 'block';
        }
    }

    /**
     * Oculta el error en el formulario de login
     */
    hideLoginError() {
        const errorElement = document.getElementById('login-error');
        if (errorElement) {
            errorElement.style.display = 'none';
        }
    }

    /**
     * Toggle para mostrar/ocultar contraseña
     */
    togglePasswordVisibility() {
        const passwordInput = document.getElementById('password');
        const toggleBtn = document.getElementById('toggle-password');

        if (passwordInput && toggleBtn) {
            const isPassword = passwordInput.type === 'password';
            passwordInput.type = isPassword ? 'text' : 'password';

            const icon = toggleBtn.querySelector('i');
            if (icon) {
                icon.className = isPassword ? 'fas fa-eye-slash' : 'fas fa-eye';
            }
        }
    }

    /**
     * Actualiza la información del usuario en el header
     */
    updateUserInfo() {
        if (!this.currentUser) return;

        const userNameElement = document.getElementById('user-name');
        const userRoleElement = document.getElementById('user-role');

        if (userNameElement) {
            userNameElement.textContent = this.currentUser.fullName || this.currentUser.username;
        }

        if (userRoleElement) {
            const roleNames = {
                'HUMAN_RESOURCES': 'Recursos Humanos',
                'ADMINISTRATIVE_STAFF': 'Personal Administrativo',
                'DOCTOR': 'Médico',
                'NURSE': 'Enfermera',
                'INFORMATION_SUPPORT': 'Soporte de Información'
            };

            userRoleElement.textContent = roleNames[this.currentUser.role] || this.currentUser.role;
        }
    }

    /**
     * Verifica la autenticación al cargar la página
     */
    checkAuthentication() {
        if (this.isAuthenticated()) {
            // Validar token
            const token = this.apiService.getToken();
            if (token && this.validateToken(token)) {
                this.showDashboard();
                this.updateUserInfo();

                if (window.dashboardService) {
                    window.dashboardService.initializeForRole(this.currentUser.role);
                }
            } else {
                // Token inválido o expirado
                this.clearSession();
                this.showWelcomePage();
            }
        } else {
            this.showWelcomePage();
        }
    }

    /**
     * Configura la navegación según el rol del usuario
     */
    setupRoleBasedNavigation() {
        if (!this.currentUser) return;

        const nav = document.getElementById('main-nav');
        if (!nav) return;

        const roleNavigations = {
            'HUMAN_RESOURCES': [
                { id: 'nav-users', icon: 'fas fa-users', text: 'Usuarios', active: true },
                { id: 'nav-patients', icon: 'fas fa-user-injured', text: 'Pacientes' },
                { id: 'nav-reports', icon: 'fas fa-chart-bar', text: 'Reportes' }
            ],
            'ADMINISTRATIVE_STAFF': [
                { id: 'nav-patients', icon: 'fas fa-user-injured', text: 'Pacientes', active: true },
                { id: 'nav-appointments', icon: 'fas fa-calendar-check', text: 'Citas' },
                { id: 'nav-billing', icon: 'fas fa-file-invoice-dollar', text: 'Facturación' }
            ],
            'DOCTOR': [
                { id: 'nav-patients', icon: 'fas fa-user-injured', text: 'Pacientes', active: true },
                { id: 'nav-medical-records', icon: 'fas fa-file-medical', text: 'Historias Clínicas' },
                { id: 'nav-orders', icon: 'fas fa-prescription-bottle', text: 'Órdenes Médicas' }
            ],
            'NURSE': [
                { id: 'nav-patients', icon: 'fas fa-user-injured', text: 'Pacientes', active: true },
                { id: 'nav-patient-visits', icon: 'fas fa-stethoscope', text: 'Visitas' },
                { id: 'nav-vital-signs', icon: 'fas fa-heartbeat', text: 'Signos Vitales' }
            ],
            'INFORMATION_SUPPORT': [
                { id: 'nav-inventory', icon: 'fas fa-boxes', text: 'Inventario', active: true },
                { id: 'nav-maintenance', icon: 'fas fa-cog', text: 'Mantenimiento' }
            ]
        };

        const navigation = roleNavigations[this.currentUser.role] || [];
        nav.innerHTML = '';

        navigation.forEach(item => {
            const navItem = document.createElement('a');
            navItem.href = '#';
            navItem.id = item.id;
            navItem.className = `nav-item ${item.active ? 'active' : ''}`;
            navItem.innerHTML = `<i class="${item.icon}"></i> ${item.text}`;

            navItem.addEventListener('click', (e) => {
                e.preventDefault();
                this.handleNavigation(item.id);
            });

            nav.appendChild(navItem);
        });
    }

    /**
     * Maneja la navegación según el ítem seleccionado
     */
    handleNavigation(navId) {
        // Remover clase active de todos los nav items
        document.querySelectorAll('.nav-item').forEach(item => {
            item.classList.remove('active');
        });

        // Agregar clase active al ítem seleccionado
        document.getElementById(navId).classList.add('active');

        // Cargar el contenido correspondiente
        if (window.dashboardService) {
            window.dashboardService.loadContent(navId);
        }
    }
}

// Crear instancia global del servicio de autenticación
window.authService = new AuthService();