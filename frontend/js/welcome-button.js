/**
 * Módulo avanzado para el botón de bienvenida
 * Maneja estados, errores, accesibilidad y mejores prácticas
 */
class WelcomeButton {
    constructor() {
        this.startBtn = null;
        this.loadingBtn = null;
        this.errorBtn = null;
        this.statusDiv = null;
        this.container = null;

        this.isLoading = false;
        this.hasError = false;
        this.retryCount = 0;
        this.maxRetries = 3;

        this.init();
    }

    /**
     * Inicializa el componente del botón
     */
    init() {
        this.getElements();
        this.bindEvents();
        this.setupAccessibility();
        this.preloadResources();
    }

    /**
     * Obtiene referencias a los elementos del DOM
     */
    getElements() {
        this.container = document.getElementById('start-button-container');
        this.startBtn = document.getElementById('start-btn');
        this.loadingBtn = document.getElementById('start-btn-loading');
        this.errorBtn = document.getElementById('start-btn-error');
        this.statusDiv = document.getElementById('start-btn-status');

        if (!this.container || !this.startBtn) {
            console.error('WelcomeButton: Elementos requeridos no encontrados');
            return;
        }
    }

    /**
     * Configura eventos del botón
     */
    bindEvents() {
        if (!this.startBtn) return;

        // Evento principal de click
        this.startBtn.addEventListener('click', this.handleStartClick.bind(this));

        // Eventos de accesibilidad
        this.startBtn.addEventListener('keydown', this.handleKeyDown.bind(this));

        // Eventos para errores
        if (this.errorBtn) {
            this.errorBtn.addEventListener('click', this.handleRetry.bind(this));
        }

        // Eventos para estados de hover/focus
        this.startBtn.addEventListener('mouseenter', this.handleMouseEnter.bind(this));
        this.startBtn.addEventListener('mouseleave', this.handleMouseLeave.bind(this));
    }

    /**
     * Configura características de accesibilidad
     */
    setupAccessibility() {
        if (!this.startBtn) return;

        // Anunciar estado inicial a lectores de pantalla
        this.announceStatus('Botón de inicio listo. Presione para iniciar sesión en el sistema.');

        // Configurar atributos ARIA adicionales
        this.startBtn.setAttribute('aria-busy', 'false');
        this.startBtn.setAttribute('aria-describedby', 'welcome-description');
    }

    /**
     * Precarga recursos necesarios
     */
    preloadResources() {
        // Precargar sonidos o recursos adicionales si es necesario
        // Por ejemplo, sonidos de feedback o imágenes adicionales
    }

    /**
     * Maneja el click principal del botón
     */
    async handleStartClick(event) {
        event.preventDefault();

        if (this.isLoading || this.hasError) return;

        try {
            await this.startApplication();
        } catch (error) {
            this.handleError(error);
        }
    }

    /**
     * Inicia la aplicación con manejo de errores robusto
     */
    async startApplication() {
        this.showLoadingState();

        try {
            // Simular verificación de conexión
            await this.checkConnection();

            // Simular carga de recursos
            await this.loadApplication();

            // Si todo va bien, proceder con la navegación
            this.showSuccessState();

            // Navegar a la página de login
            setTimeout(() => {
                this.navigateToLogin();
            }, 1000);

        } catch (error) {
            throw error;
        }
    }

    /**
     * Verifica la conexión con el servidor
     */
    async checkConnection() {
        return new Promise((resolve, reject) => {
            // Simular delay de red
            setTimeout(async () => {
                try {
                    // Intentar hacer ping al servidor
                    const response = await fetch('/api/health', {
                        method: 'GET',
                        timeout: 5000
                    });

                    if (response.ok) {
                        resolve(response);
                    } else {
                        throw new Error('Servidor no responde correctamente');
                    }
                } catch (error) {
                    reject(new Error('No se puede conectar con el servidor'));
                }
            }, 500);
        });
    }

    /**
     * Carga recursos de la aplicación
     */
    async loadApplication() {
        return new Promise((resolve, reject) => {
            setTimeout(() => {
                // Simular carga de recursos críticos
                const shouldFail = Math.random() < 0.1; // 10% de probabilidad de fallo

                if (shouldFail) {
                    reject(new Error('Error al cargar recursos de la aplicación'));
                } else {
                    resolve();
                }
            }, 1500);
        });
    }

    /**
     * Muestra el estado de carga
     */
    showLoadingState() {
        this.isLoading = true;
        this.hasError = false;

        if (this.startBtn && this.loadingBtn) {
            this.startBtn.style.display = 'none';
            this.loadingBtn.style.display = 'inline-flex';
            this.loadingBtn.setAttribute('aria-label', 'Cargando aplicación...');
        }

        this.announceStatus('Cargando aplicación. Por favor espere...');
    }

    /**
     * Muestra el estado de error
     */
    showErrorState(error) {
        this.isLoading = false;
        this.hasError = true;

        if (this.loadingBtn && this.errorBtn) {
            this.loadingBtn.style.display = 'none';
            this.errorBtn.style.display = 'inline-flex';
        }

        this.announceStatus(`Error: ${error.message}. Presione el botón para reintentar.`);
    }

    /**
     * Muestra el estado de éxito
     */
    showSuccessState() {
        this.isLoading = false;
        this.hasError = false;

        // Animación de éxito
        if (this.startBtn) {
            this.startBtn.classList.add('btn-success');
            this.startBtn.querySelector('.btn-icon')?.classList.add('fa-check');
        }

        this.announceStatus('Aplicación cargada exitosamente. Redirigiendo...');
    }

    /**
     * Maneja errores con lógica de reintento
     */
    handleError(error) {
        console.error('WelcomeButton Error:', error);

        if (this.retryCount < this.maxRetries) {
            this.retryCount++;
            this.showErrorState(error);
        } else {
            this.showFatalError(error);
        }
    }

    /**
     * Maneja errores fatales después de múltiples reintentos
     */
    showFatalError(error) {
        this.announceStatus(`Error crítico: ${error.message}. Por favor, recargue la página o contacte al administrador.`);

        // Podría mostrar una interfaz de error más elaborada
        if (this.errorBtn) {
            this.errorBtn.innerHTML = `
                <i class="fas fa-exclamation-circle btn-icon" aria-hidden="true"></i>
                <span class="btn-text">Error Crítico</span>
            `;
            this.errorBtn.classList.add('btn-danger');
            this.errorBtn.classList.remove('btn-shake');
        }
    }

    /**
     * Maneja el reintento después de un error
     */
    async handleRetry() {
        if (this.isLoading) return;

        this.retryCount = 0;
        this.showErrorState({ message: 'Reintentando...' });

        setTimeout(async () => {
            try {
                await this.startApplication();
            } catch (error) {
                this.handleError(error);
            }
        }, 1000);
    }

    /**
     * Maneja eventos de teclado para accesibilidad
     */
    handleKeyDown(event) {
        if (event.key === 'Enter' || event.key === ' ') {
            event.preventDefault();
            this.handleStartClick(event);
        }
    }

    /**
     * Maneja eventos de mouse para efectos visuales
     */
    handleMouseEnter() {
        if (this.isLoading || this.hasError) return;

        // Efectos de hover adicionales podrían ir aquí
    }

    handleMouseLeave() {
        // Limpiar efectos de hover si es necesario
    }

    /**
     * Anuncia mensajes a lectores de pantalla
     */
    announceStatus(message) {
        if (this.statusDiv) {
            this.statusDiv.textContent = message;
        }
    }

    /**
     * Navega a la página de login
     */
    navigateToLogin() {
        // Mostrar página de login
        const welcomePage = document.getElementById('welcome-page');
        const loginPage = document.getElementById('login-page');

        if (welcomePage && loginPage) {
            welcomePage.style.display = 'none';
            loginPage.style.display = 'block';

            // Enfocar el primer campo del formulario de login
            const usernameField = loginPage.querySelector('#username');
            if (usernameField) {
                usernameField.focus();
            }
        }
    }

    /**
     * Método público para resetear el botón
     */
    reset() {
        this.isLoading = false;
        this.hasError = false;
        this.retryCount = 0;

        if (this.startBtn && this.loadingBtn && this.errorBtn) {
            this.startBtn.style.display = 'inline-flex';
            this.loadingBtn.style.display = 'none';
            this.errorBtn.style.display = 'none';

            this.startBtn.classList.remove('btn-success');
            this.startBtn.setAttribute('aria-busy', 'false');
        }

        this.announceStatus('Botón de inicio listo.');
    }
}

// Inicializar el componente cuando el DOM esté listo
document.addEventListener('DOMContentLoaded', () => {
    window.welcomeButton = new WelcomeButton();
});

// Exportar para posibles pruebas o uso externo
if (typeof module !== 'undefined' && module.exports) {
    module.exports = WelcomeButton;
}