/**
 * Módulo avanzado para mejorar todos los botones de la aplicación
 * Proporciona funcionalidades avanzadas, accesibilidad y mejores prácticas
 */
class ButtonEnhancements {
    constructor() {
        this.buttons = new Map();
        this.init();
    }

    /**
     * Inicializa todas las mejoras de botones
     */
    init() {
        this.setupTogglePassword();
        this.setupLoginForm();
        this.setupNavigationButtons();
        this.setupLogoutButton();
        this.setupGlobalButtonEnhancements();
    }

    /**
     * Mejora el botón de toggle de contraseña
     */
    setupTogglePassword() {
        const toggleBtn = document.getElementById('toggle-password');
        const passwordInput = document.getElementById('password');

        if (!toggleBtn || !passwordInput) return;

        toggleBtn.addEventListener('click', () => {
            this.togglePasswordVisibility(toggleBtn, passwordInput);
        });

        // Mejorar accesibilidad
        toggleBtn.addEventListener('keydown', (e) => {
            if (e.key === 'Enter' || e.key === ' ') {
                e.preventDefault();
                this.togglePasswordVisibility(toggleBtn, passwordInput);
            }
        });

        this.buttons.set('toggle-password', {
            element: toggleBtn,
            type: 'toggle',
            state: 'hidden' // hidden | visible
        });
    }

    /**
     * Función para mostrar/ocultar contraseña
     */
    togglePasswordVisibility(button, input) {
        const isHidden = input.type === 'password';
        const newType = isHidden ? 'text' : 'password';

        input.type = newType;
        button.classList.toggle('active', !isHidden);

        // Actualizar aria-label para accesibilidad
        const label = isHidden ? 'Ocultar contraseña' : 'Mostrar contraseña';
        button.setAttribute('aria-label', label);

        // Anunciar cambio a lectores de pantalla
        this.announceToScreenReader(
            isHidden ? 'Contraseña visible' : 'Contraseña oculta'
        );

        // Efectos visuales
        button.style.transform = 'scale(0.9)';
        setTimeout(() => {
            button.style.transform = '';
        }, 150);
    }

    /**
     * Mejora el formulario de login
     */
    setupLoginForm() {
        const loginForm = document.getElementById('login-form');
        const submitBtn = document.getElementById('login-submit-btn');

        if (!loginForm || !submitBtn) return;

        loginForm.addEventListener('submit', async (e) => {
            e.preventDefault();
            await this.handleLoginSubmit(submitBtn, loginForm);
        });

        this.buttons.set('login-submit', {
            element: submitBtn,
            type: 'submit',
            form: loginForm
        });
    }

    /**
     * Maneja el envío del formulario de login
     */
    async handleLoginSubmit(button, form) {
        const formData = new FormData(form);
        const username = formData.get('username');
        const password = formData.get('password');

        // Validación básica
        if (!username || !password) {
            this.showFormError('Por favor complete todos los campos');
            return;
        }

        this.setButtonLoading(button, true);

        try {
            // Simular llamada a API
            await this.simulateLogin(username, password);

            // Éxito
            this.showFormSuccess('Inicio de sesión exitoso');
            button.classList.add('btn-success');

            // Redirigir después de éxito
            setTimeout(() => {
                this.navigateToDashboard();
            }, 1000);

        } catch (error) {
            this.showFormError(error.message);
            button.classList.add('btn-shake');
            setTimeout(() => {
                button.classList.remove('btn-shake');
            }, 500);
        } finally {
            this.setButtonLoading(button, false);
        }
    }

    /**
     * Simula el proceso de login
     */
    async simulateLogin(username, password) {
        return new Promise((resolve, reject) => {
            setTimeout(() => {
                // Simular diferentes escenarios
                if (username === 'admin' && password === 'admin') {
                    resolve({ user: 'admin', role: 'administrator' });
                } else if (username === 'user' && password === 'user') {
                    resolve({ user: 'user', role: 'user' });
                } else {
                    reject(new Error('Credenciales inválidas'));
                }
            }, 1500);
        });
    }

    /**
     * Mejora los botones de navegación
     */
    setupNavigationButtons() {
        const backBtn = document.getElementById('back-to-welcome');

        if (!backBtn) return;

        backBtn.addEventListener('click', () => {
            this.handleBackToWelcome();
        });

        this.buttons.set('back-to-welcome', {
            element: backBtn,
            type: 'navigation'
        });
    }

    /**
     * Maneja el botón de volver al welcome
     */
    handleBackToWelcome() {
        const loginPage = document.getElementById('login-page');
        const welcomePage = document.getElementById('welcome-page');

        if (loginPage && welcomePage) {
            // Animación de salida
            loginPage.style.opacity = '0';
            loginPage.style.transform = 'translateX(-20px)';

            setTimeout(() => {
                loginPage.style.display = 'none';
                welcomePage.style.display = 'block';

                // Animación de entrada
                welcomePage.style.opacity = '0';
                welcomePage.style.transform = 'translateY(20px)';

                setTimeout(() => {
                    welcomePage.style.opacity = '1';
                    welcomePage.style.transform = 'translateY(0)';
                }, 50);
            }, 300);

            // Resetear formulario
            const loginForm = document.getElementById('login-form');
            if (loginForm) {
                loginForm.reset();
                this.clearFormErrors();
            }

            // Enfocar botón de inicio
            setTimeout(() => {
                const startBtn = document.getElementById('start-btn');
                if (startBtn) {
                    startBtn.focus();
                }
            }, 600);
        }
    }

    /**
     * Mejora el botón de logout
     */
    setupLogoutButton() {
        const logoutBtn = document.getElementById('logout-btn');

        if (!logoutBtn) return;

        logoutBtn.addEventListener('click', () => {
            this.handleLogout();
        });

        this.buttons.set('logout', {
            element: logoutBtn,
            type: 'action'
        });
    }

    /**
     * Maneja el proceso de logout
     */
    handleLogout() {
        // Mostrar confirmación
        if (!confirm('¿Está seguro que desea cerrar sesión?')) {
            return;
        }

        this.setButtonLoading(logoutBtn, true);

        // Simular proceso de logout
        setTimeout(() => {
            // Limpiar datos de sesión
            this.clearSession();

            // Navegar a welcome
            this.navigateToWelcome();

            this.setButtonLoading(logoutBtn, false);
        }, 1000);
    }

    /**
     * Configura mejoras globales para todos los botones
     */
    setupGlobalButtonEnhancements() {
        // Mejorar todos los botones con la clase .btn
        const buttons = document.querySelectorAll('.btn');

        buttons.forEach(button => {
            this.enhanceButtonAccessibility(button);
            this.addButtonFeedback(button);
        });
    }

    /**
     * Mejora la accesibilidad de un botón
     */
    enhanceButtonAccessibility(button) {
        // Agregar role si no existe
        if (!button.getAttribute('role') && button.type !== 'submit') {
            button.setAttribute('role', 'button');
        }

        // Mejorar tabindex
        if (!button.hasAttribute('tabindex') && button.type !== 'submit') {
            button.setAttribute('tabindex', '0');
        }

        // Agregar aria-label si no tiene texto visible
        if (!button.textContent.trim() && !button.getAttribute('aria-label')) {
            const icon = button.querySelector('i');
            if (icon) {
                const iconClass = icon.className;
                if (iconClass.includes('fa-eye')) {
                    button.setAttribute('aria-label', 'Mostrar contraseña');
                }
            }
        }
    }

    /**
     * Agrega feedback visual a los botones
     */
    addButtonFeedback(button) {
        button.addEventListener('mouseenter', () => {
            if (!button.disabled) {
                this.addHoverEffect(button);
            }
        });

        button.addEventListener('mouseleave', () => {
            this.removeHoverEffect(button);
        });

        button.addEventListener('focus', () => {
            if (!button.disabled) {
                this.addFocusEffect(button);
            }
        });

        button.addEventListener('blur', () => {
            this.removeFocusEffect(button);
        });
    }

    /**
     * Efectos auxiliares para feedback visual
     */
    addHoverEffect(button) {
        button.style.transform = 'translateY(-1px)';
    }

    removeHoverEffect(button) {
        button.style.transform = '';
    }

    addFocusEffect(button) {
        button.style.boxShadow = '0 0 0 3px rgba(37, 99, 235, 0.2)';
    }

    removeFocusEffect(button) {
        button.style.boxShadow = '';
    }

    /**
     * Utilidades para manejo de estados de botones
     */
    setButtonLoading(button, isLoading) {
        if (!button) return;

        button.disabled = isLoading;
        button.setAttribute('aria-busy', isLoading.toString());

        if (isLoading) {
            button.classList.add('btn-loading');
        } else {
            button.classList.remove('btn-loading');
        }
    }

    /**
     * Utilidades para manejo de errores de formulario
     */
    showFormError(message) {
        const errorDiv = document.getElementById('login-error');
        if (errorDiv) {
            errorDiv.textContent = message;
            errorDiv.style.display = 'block';
            errorDiv.classList.add('error-shake');

            setTimeout(() => {
                errorDiv.classList.remove('error-shake');
            }, 500);
        }
    }

    showFormSuccess(message) {
        const errorDiv = document.getElementById('login-error');
        if (errorDiv) {
            errorDiv.textContent = message;
            errorDiv.style.display = 'block';
            errorDiv.style.backgroundColor = '#dcfce7';
            errorDiv.style.color = '#166534';
            errorDiv.style.borderColor = '#bbf7d0';
        }
    }

    clearFormErrors() {
        const errorDiv = document.getElementById('login-error');
        if (errorDiv) {
            errorDiv.style.display = 'none';
            errorDiv.style.backgroundColor = '';
            errorDiv.style.color = '';
            errorDiv.style.borderColor = '';
        }
    }

    /**
     * Utilidades para navegación
     */
    navigateToDashboard() {
        const loginPage = document.getElementById('login-page');
        const dashboard = document.getElementById('dashboard');

        if (loginPage && dashboard) {
            loginPage.style.display = 'none';
            dashboard.style.display = 'block';
        }
    }

    navigateToWelcome() {
        const dashboard = document.getElementById('dashboard');
        const welcomePage = document.getElementById('welcome-page');

        if (dashboard && welcomePage) {
            dashboard.style.display = 'none';
            welcomePage.style.display = 'block';
        }
    }

    /**
     * Utilidades para manejo de sesión
     */
    clearSession() {
        // Limpiar datos almacenados
        localStorage.removeItem('user');
        localStorage.removeItem('token');
        localStorage.removeItem('role');

        // Resetear interfaz
        this.resetUserInterface();
    }

    resetUserInterface() {
        // Resetear nombre de usuario
        const userName = document.getElementById('user-name');
        const userRole = document.getElementById('user-role');

        if (userName) userName.textContent = '';
        if (userRole) userRole.textContent = '';

        // Ocultar elementos del dashboard
        const dashboard = document.getElementById('dashboard');
        if (dashboard) {
            dashboard.style.display = 'none';
        }
    }

    /**
     * Utilidad para anuncios a lectores de pantalla
     */
    announceToScreenReader(message) {
        const announcement = document.createElement('div');
        announcement.setAttribute('aria-live', 'polite');
        announcement.setAttribute('aria-atomic', 'true');
        announcement.className = 'sr-only';
        announcement.textContent = message;

        document.body.appendChild(announcement);

        setTimeout(() => {
            document.body.removeChild(announcement);
        }, 1000);
    }
}

// Inicializar mejoras cuando el DOM esté listo
document.addEventListener('DOMContentLoaded', () => {
    window.buttonEnhancements = new ButtonEnhancements();
});

// Exportar para posibles pruebas
if (typeof module !== 'undefined' && module.exports) {
    module.exports = ButtonEnhancements;
}