/**
 * Constantes y configuraciones para HR Management
 */
const HR_CONSTANTS = {
    // Paginación
    DEFAULT_ITEMS_PER_PAGE: 10,
    MAX_CEDULA_LENGTH: 12,
    MIN_CEDULA_LENGTH: 6,
    MAX_FULLNAME_LENGTH: 100,
    MAX_ADDRESS_LENGTH: 30,
    MAX_USERNAME_LENGTH: 15,
    PHONE_LENGTH: 10,
    MIN_PASSWORD_LENGTH: 8,
    MAX_AGE: 150,

    // Roles válidos
    VALID_ROLES: ['DOCTOR', 'NURSE', 'ADMINISTRATIVE_STAFF', 'SUPPORT_STAFF', 'HUMAN_RESOURCES'],

    // Estados de filtro
    FILTER_ALL: 'ALL',
    FILTER_ACTIVE: 'ACTIVE',
    FILTER_INACTIVE: 'INACTIVE',

    // Mensajes de error
    ERRORS: {
        CEDULA_REQUIRED: 'La cédula es obligatoria',
        CEDULA_NUMERIC: 'La cédula debe contener solo números',
        CEDULA_LENGTH: 'La cédula debe tener entre 6 y 12 dígitos',
        USERNAME_REQUIRED: 'El nombre de usuario es obligatorio',
        USERNAME_ALPHANUMERIC: 'El usuario debe contener solo letras y números',
        USERNAME_LENGTH: 'El usuario no puede tener más de 15 caracteres',
        FULLNAME_REQUIRED: 'El nombre completo es obligatorio',
        FULLNAME_LENGTH: 'El nombre no puede tener más de 100 caracteres',
        EMAIL_REQUIRED: 'El correo electrónico es obligatorio',
        EMAIL_INVALID: 'Formato de correo electrónico inválido',
        PHONE_REQUIRED: 'El teléfono es obligatorio',
        PHONE_LENGTH: 'El teléfono debe tener exactamente 10 dígitos',
        ADDRESS_REQUIRED: 'La dirección es obligatoria',
        ADDRESS_LENGTH: 'La dirección no puede tener más de 30 caracteres',
        ROLE_REQUIRED: 'El rol es obligatorio',
        ROLE_INVALID: 'Rol seleccionado no válido',
        BIRTHDATE_REQUIRED: 'La fecha de nacimiento es obligatoria',
        BIRTHDATE_FUTURE: 'La fecha de nacimiento no puede ser futura',
        BIRTHDATE_TOO_OLD: 'La edad no puede ser mayor a 150 años',
        PASSWORD_REQUIRED: 'La contraseña es obligatoria',
        PASSWORD_LENGTH: 'La contraseña debe tener al menos 8 caracteres',
        PASSWORD_COMPLEXITY: 'Debe incluir: 1 mayúscula, 1 número, 1 carácter especial',
        PASSWORD_MISMATCH: 'Las contraseñas no coinciden',
        USER_NOT_FOUND: 'Empleado no encontrado',
        SAVE_FAILED: 'Error al guardar empleado',
        LOAD_FAILED: 'Error al cargar empleados',
        DELETE_FAILED: 'Error al eliminar empleado',
        TOGGLE_STATUS_FAILED: 'Error al cambiar estado del empleado'
    },

    // Mensajes de éxito
    SUCCESS: {
        USER_CREATED: 'Empleado creado exitosamente',
        USER_UPDATED: 'Empleado actualizado exitosamente',
        USER_DELETED: 'Empleado eliminado exitosamente',
        USER_STATUS_CHANGED: 'Estado del empleado cambiado exitosamente',
        USERS_LOADED: 'Empleados cargados exitosamente'
    },

    // Configuración de roles
    ROLE_DISPLAY_NAMES: {
        'HUMAN_RESOURCES': 'Recursos Humanos',
        'ADMINISTRATIVE_STAFF': 'Personal Administrativo',
        'DOCTOR': 'Médico',
        'NURSE': 'Enfermera',
        'SUPPORT_STAFF': 'Soporte de Información'
    },

    ROLE_BADGE_CLASSES: {
        'HUMAN_RESOURCES': 'hr',
        'ADMINISTRATIVE_STAFF': 'admin',
        'DOCTOR': 'doctor',
        'NURSE': 'nurse',
        'SUPPORT_STAFF': 'support'
    },

    ROLE_ICONS: {
        'HUMAN_RESOURCES': 'fa-user-tie',
        'ADMINISTRATIVE_STAFF': 'fa-user-cog',
        'DOCTOR': 'fa-user-md',
        'NURSE': 'fa-user-nurse',
        'SUPPORT_STAFF': 'fa-headset'
    },

    // Selectores DOM
    SELECTORS: {
        CONTENT_AREA: '#content-area',
        HR_USER_SEARCH: '#hr-user-search',
        ROLE_FILTER: '#role-filter',
        STATUS_FILTER: '#status-filter',
        HR_USERS_TABLE_BODY: '#hr-users-table-body',
        HR_USER_MODAL: '#hr-user-modal',
        HR_USER_FORM: '#hr-user-form',
        HR_FORM_ALERT: '#hr-form-alert',
        HR_MODAL_TITLE: '#hr-modal-title',
        HR_SAVE_BTN_TEXT: '#hr-save-btn-text'
    }
};

/**
 * Servicio de Gestión de Recursos Humanos
 * Implementa funcionalidades específicas para el rol RECURSOS_HUMANOS según especificaciones técnicas
 */
class HRManagementService {
    constructor() {
        this.users = [];
        this.filteredUsers = [];
        this.currentPage = 1;
        this.itemsPerPage = HR_CONSTANTS.DEFAULT_ITEMS_PER_PAGE;
        this.searchTerm = '';
        this.selectedRoleFilter = HR_CONSTANTS.FILTER_ALL;
        this.selectedStatusFilter = HR_CONSTANTS.FILTER_ALL;
        this.currentUser = null;

        // Cache para optimización de rendimiento
        this.lastSearchTerm = '';
        this.lastRoleFilter = HR_CONSTANTS.FILTER_ALL;
        this.lastStatusFilter = HR_CONSTANTS.FILTER_ALL;
        this.renderCache = new Map();

        // Handlers para cleanup
        this.searchHandler = null;
        this.roleFilterHandler = null;
        this.statusFilterHandler = null;

        // Inicializar módulos
        this.renderer = new HRRenderer(this);
        this.validator = new HRValidator();
        this.dataManager = new HRDataManager(this);

        // Configurar patrón Observer entre módulos
        this.setupObserverPattern();
    }

    /**
     * Configura el patrón Observer entre módulos para comunicación desacoplada
     */
    setupObserverPattern() {
        // El validador notifica al renderer sobre cambios de validación
        this.validator.subscribe({
            onValidationError: (data) => {
                console.log('Validación fallida:', data);
                // El renderer podría mostrar indicadores visuales de error
            },
            onValidationSuccess: (data) => {
                console.log('Validación exitosa');
                // El renderer podría limpiar indicadores visuales
            }
        });

        // El dataManager notifica al renderer sobre cambios de datos
        this.dataManager.subscribe({
            onDataChanged: (data) => {
                console.log('Datos cambiados, refrescando UI');
                this.renderer.refreshTable();
            },
            onUserCreated: (data) => {
                console.log('Usuario creado:', data);
                this.applyFilters(); // Refrescar filtros para incluir nuevo usuario
            },
            onUserUpdated: (data) => {
                console.log('Usuario actualizado:', data);
                this.applyFilters(); // Refrescar filtros
            },
            onUserDeleted: (data) => {
                console.log('Usuario eliminado:', data);
                this.applyFilters(); // Refrescar filtros
            }
        });
    }

    /**
     * Inicializa la página de Recursos Humanos
     */
    async initialize() {
        try {
            window.showInfo('Cargando sistema de Recursos Humanos...');
            await this.dataManager.loadUsersData();
            this.renderHRPage();
            this.setupEventListeners();
            window.showSuccess('Sistema de Recursos Humanos listo');
        } catch (error) {
            console.error('Error inicializando HR:', error);
            window.showError('Error al inicializar Recursos Humanos: ' + error.message);
            this.showErrorState();
        }
    }

    /**
     * Carga los datos de usuarios desde localStorage - delegada al dataManager
     */
    async loadUsersData() {
        return this.dataManager.loadUsersData();
    }

    /**
     * Renderiza la página completa de Recursos Humanos
     */
    renderHRPage() {
        this.renderer.renderMainPage();
        this.applyFilters();
    }

    /**
     * Refresca la tabla sin recargar toda la página
     */
    refreshTable() {
        this.renderer.refreshTable();
    }

    /**
     * Configura los event listeners con optimización de memoria
     */
    setupEventListeners() {
        // Limpiar event listeners previos para evitar memory leaks
        this.cleanupEventListeners();

        // Búsqueda con debounce optimizado
        const searchInput = document.getElementById(HR_CONSTANTS.SELECTORS.HR_USER_SEARCH);
        if (searchInput) {
            this.searchHandler = window.debounce((e) => {
                this.searchTerm = e.target.value;
                this.applyFilters();
            }, 300);
            searchInput.addEventListener('input', this.searchHandler);
        }

        // Filtros con optimización
        const roleFilter = document.getElementById('role-filter');
        const statusFilter = document.getElementById('status-filter');

        if (roleFilter) {
            this.roleFilterHandler = (e) => {
                this.selectedRoleFilter = e.target.value;
                this.applyFilters();
            };
            roleFilter.addEventListener('change', this.roleFilterHandler);
        }

        if (statusFilter) {
            this.statusFilterHandler = (e) => {
                this.selectedStatusFilter = e.target.value;
                this.applyFilters();
            };
            statusFilter.addEventListener('change', this.statusFilterHandler);
        }

        // Validación del formulario en tiempo real
        this.validator.setupFormValidation();
    }

    /**
     * Limpia los event listeners para evitar memory leaks
     */
    cleanupEventListeners() {
        const searchInput = document.getElementById(HR_CONSTANTS.SELECTORS.HR_USER_SEARCH);
        if (searchInput && this.searchHandler) {
            searchInput.removeEventListener('input', this.searchHandler);
        }

        const roleFilter = document.getElementById('role-filter');
        if (roleFilter && this.roleFilterHandler) {
            roleFilter.removeEventListener('change', this.roleFilterHandler);
        }

        const statusFilter = document.getElementById('status-filter');
        if (statusFilter && this.statusFilterHandler) {
            statusFilter.removeEventListener('change', this.statusFilterHandler);
        }
    }

    /**
     * Configura la validación del formulario en tiempo real
     */
    setupFormValidation() {
        this.validator.setupFormValidation();
    }

    /**
     * Valida el campo de cédula en tiempo real
     */
    validateCedula(input) {
        return this.validator.validateCedula(input);
    }

    /**
     * Valida el campo de usuario en tiempo real
     */
    validateUsername(input) {
        return this.validator.validateUsername(input);
    }

    /**
     * Valida el campo de email en tiempo real
     */
    validateEmail(input) {
        return this.validator.validateEmail(input);
    }

    /**
     * Valida el campo de contraseña en tiempo real
     */
    validatePassword(input) {
        return this.validator.validatePassword(input);
    }

    /**
     * Valida la confirmación de contraseña en tiempo real
     */
    validatePasswordConfirmation(input) {
        return this.validator.validatePasswordConfirmation(input);
    }

    /**
     * Aplica filtros de búsqueda y estado con optimización de rendimiento y cache
     */
    applyFilters() {
        // Verificar si los filtros cambiaron para evitar procesamiento innecesario
        if (this.searchTerm === this.lastSearchTerm &&
            this.selectedRoleFilter === this.lastRoleFilter &&
            this.selectedStatusFilter === this.lastStatusFilter) {
            return; // No hay cambios, no procesar
        }

        // Actualizar cache
        this.lastSearchTerm = this.searchTerm;
        this.lastRoleFilter = this.selectedRoleFilter;
        this.lastStatusFilter = this.selectedStatusFilter;

        // Usar requestAnimationFrame para evitar bloqueos en el hilo principal
        requestAnimationFrame(() => {
            try {
                let filtered = [...this.users];

                // Aplicar búsqueda con normalización y optimización
                if (this.searchTerm && this.searchTerm.trim()) {
                    const term = this.searchTerm.toLowerCase().trim();
                    filtered = filtered.filter(user => {
                        // Verificar existencia de propiedades antes de acceder
                        if (!user) return false;

                        // Usar búsqueda más eficiente con indexOf
                        return (
                            (user.fullName && user.fullName.toLowerCase().indexOf(term) !== -1) ||
                            (user.cedula && user.cedula.indexOf(term) !== -1) ||
                            (user.username && user.username.toLowerCase().indexOf(term) !== -1) ||
                            (user.email && user.email.toLowerCase().indexOf(term) !== -1)
                        );
                    });
                }

                // Aplicar filtro de rol
                if (this.selectedRoleFilter !== HR_CONSTANTS.FILTER_ALL) {
                    filtered = filtered.filter(user => user && user.role === this.selectedRoleFilter);
                }

                // Aplicar filtro de estado
                if (this.selectedStatusFilter !== HR_CONSTANTS.FILTER_ALL) {
                    const isActive = this.selectedStatusFilter === HR_CONSTANTS.FILTER_ACTIVE;
                    filtered = filtered.filter(user => user && user.active === isActive);
                }

                this.filteredUsers = filtered;
                this.currentPage = 1;
                this.refreshTable();
            } catch (error) {
                console.error('Error aplicando filtros:', error);
                window.showError('Error al aplicar filtros de búsqueda');
            }
        });
    }

    /**
     * Refresca la tabla sin recargar toda la página con optimización de rendimiento
     */
    refreshTable() {
        // Usar requestAnimationFrame para optimizar el renderizado
        requestAnimationFrame(() => {
            const tableBody = document.getElementById(HR_CONSTANTS.SELECTORS.HR_USERS_TABLE_BODY);
            if (tableBody) {
                const newContent = this.renderUsersTableRows();
                // Solo actualizar si el contenido cambió
                if (tableBody.innerHTML !== newContent) {
                    tableBody.innerHTML = newContent;
                }
            }

            // Actualizar paginación solo si es necesario
            const pagination = document.querySelector('.hr-pagination');
            if (pagination) {
                const newPagination = this.renderPagination();
                if (pagination.outerHTML !== newPagination) {
                    pagination.outerHTML = newPagination;
                }
            }
        });
    }

    /**
     * Muestra el modal para crear usuario
     */
    showCreateUserModal() {
        try {
            this.currentUser = null;
            this.showUserModal();
            this.resetForm();
        } catch (error) {
            console.error('Error mostrando modal de creación:', error);
            window.showError('Error al abrir el formulario de nuevo empleado');
        }
    }

    /**
     * Muestra el modal para editar usuario
     */
    async editUser(cedula) {
        if (!cedula || typeof cedula !== 'string') {
            window.showError('Cédula inválida para editar empleado');
            return;
        }

        try {
            window.showInfo('Cargando información del empleado...');

            const user = window.localStorageService.getUserById(cedula);

            if (!user) {
                throw new Error(HR_CONSTANTS.ERRORS.USER_NOT_FOUND);
            }

            this.currentUser = user;
            this.showUserModal();
            this.populateForm(user);
            window.showSuccess('Empleado cargado para edición');

        } catch (error) {
            console.error('Error cargando empleado:', error);
            window.showError('Error al cargar empleado: ' + error.message);
        }
    }

    /**
     * Muestra el modal de usuario con validaciones
     */
    showUserModal() {
        const modal = document.getElementById(HR_CONSTANTS.SELECTORS.HR_USER_MODAL);
        if (!modal) {
            console.error('Modal no encontrado');
            return;
        }

        modal.style.display = 'flex';
        // Usar requestAnimationFrame para animaciones suaves
        requestAnimationFrame(() => {
            modal.classList.add('show');
        });
    }

    /**
     * Cierra el modal de usuario
     */
    closeUserModal() {
        const modal = document.getElementById(HR_CONSTANTS.SELECTORS.HR_USER_MODAL);
        if (!modal) return;

        modal.classList.remove('show');
        setTimeout(() => {
            modal.style.display = 'none';
        }, 300);

        this.currentUser = null;
    }

    /**
     * Resetea el formulario con validaciones
     */
    resetForm() {
        const form = document.getElementById(HR_CONSTANTS.SELECTORS.HR_USER_FORM);
        if (form) {
            form.reset();
        }

        // Resetear título del modal
        const modalTitle = document.getElementById(HR_CONSTANTS.SELECTORS.HR_MODAL_TITLE);
        if (modalTitle) {
            modalTitle.innerHTML = '<i class="fas fa-user-plus"></i> Crear Nuevo Empleado';
        }

        // Ocultar alertas
        this.validator.hideFormAlert();

        // Resetear botón de guardar
        const saveBtn = document.getElementById(HR_CONSTANTS.SELECTORS.HR_SAVE_BTN_TEXT);
        if (saveBtn) {
            saveBtn.textContent = 'Guardar Empleado';
        }
    }

    /**
     * Llena el formulario con datos del usuario con validaciones
     */
    populateForm(user) {
        if (!user) {
            console.error('Usuario no válido para popular formulario');
            return;
        }

        const modalTitle = document.getElementById(HR_CONSTANTS.SELECTORS.HR_MODAL_TITLE);
        if (modalTitle) {
            modalTitle.innerHTML = '<i class="fas fa-user-edit"></i> Editar Empleado';
        }

        const saveBtn = document.getElementById(HR_CONSTANTS.SELECTORS.HR_SAVE_BTN_TEXT);
        if (saveBtn) {
            saveBtn.textContent = 'Actualizar Empleado';
        }

        // Llenar campos con validación de existencia
        const fields = [
            { id: 'hr-user-cedula', value: user.cedula },
            { id: 'hr-user-username', value: user.username },
            { id: 'hr-user-fullname', value: user.fullName },
            { id: 'hr-user-email', value: user.email },
            { id: 'hr-user-phone', value: user.phoneNumber },
            { id: 'hr-user-address', value: user.address },
            { id: 'hr-user-role', value: user.role }
        ];

        fields.forEach(field => {
            const element = document.getElementById(field.id);
            if (element) {
                element.value = field.value || '';
            }
        });

        // Formatear fecha de nacimiento con validación
        if (user.birthDate) {
            try {
                const birthDate = new Date(user.birthDate);
                if (!isNaN(birthDate.getTime())) {
                    const birthDateInput = document.getElementById('hr-user-birthdate');
                    if (birthDateInput) {
                        birthDateInput.value = birthDate.toISOString().split('T')[0];
                    }
                }
            } catch (error) {
                console.warn('Fecha de nacimiento inválida:', user.birthDate);
            }
        }

        // Limpiar campos de contraseña para edición
        const passwordFields = ['hr-user-password', 'hr-user-confirm-password'];
        passwordFields.forEach(fieldId => {
            const field = document.getElementById(fieldId);
            if (field) {
                field.value = '';
                field.required = false;
            }
        });
    }

    /**
     * Guarda el usuario (crear o actualizar) con mejor manejo de errores
     */
    async saveUser() {
        try {
            if (!this.validator.validateForm(this.currentUser)) {
                return;
            }

            const userData = this.validator.getFormData();
            if (!userData) {
                throw new Error('No se pudieron obtener los datos del formulario');
            }

            await this.dataManager.saveUser(userData);
        } catch (error) {
            console.error('Error guardando empleado:', error);
            this.validator.showFormAlert(HR_CONSTANTS.ERRORS.SAVE_FAILED + ': ' + error.message, 'error');
        }
    }

    /**
     * Valida el formulario completo
     */
    validateForm() {
        return this.validator.validateForm(this.currentUser);
    }

    // Validaciones específicas delegadas al módulo de validación

    /**
     * Delegar métodos de manejo de errores al validador
     */
    showFieldError(fieldId, message) {
        this.validator.showFieldError(fieldId, message);
    }

    clearFieldError(fieldId) {
        this.validator.clearFieldError(fieldId);
    }

    showFormAlert(message, type = 'error') {
        this.validator.showFormAlert(message, type);
    }

    hideFormAlert() {
        this.validator.hideFormAlert();
    }

    /**
     * Obtiene los datos del formulario
     */
    getFormData() {
        return this.validator.getFormData();
    }

    /**
     * Cambia el estado de un usuario
     */
    async toggleUserStatus(cedula) {
        return this.dataManager.toggleUserStatus(cedula);
    }

    /**
     * Elimina un usuario
     */
    async deleteUser(cedula) {
        return this.dataManager.deleteUser(cedula);
    }

    /**
     * Cambia de página con validaciones
     */
    goToPage(page) {
        if (typeof page !== 'number' || page < 1) {
            console.warn('Número de página inválido:', page);
            return;
        }

        const totalPages = Math.ceil(this.filteredUsers.length / this.itemsPerPage);
        if (page > totalPages) {
            console.warn('Página fuera de rango:', page, 'total:', totalPages);
            return;
        }

        this.currentPage = page;
        this.refreshTable();
    }

    /**
     * Obtiene el conteo de usuarios activos con validaciones
     */
    getActiveUsersCount() {
        if (!Array.isArray(this.users)) {
            console.warn('Lista de usuarios no válida');
            return 0;
        }
        return this.users.filter(user => user && user.active === true).length;
    }

    /**
     * Obtiene el conteo de usuarios por rol con validaciones
     */
    getRoleCount(role) {
        if (!Array.isArray(this.users) || !role) {
            return 0;
        }
        return this.users.filter(user => user && user.role === role).length;
    }

    /**
     * Utilidades delegadas a constantes y módulos
     */
    getRoleDisplayName(role) {
        return HR_CONSTANTS.ROLE_DISPLAY_NAMES[role] || role;
    }

    getRoleBadgeClass(role) {
        return HR_CONSTANTS.ROLE_BADGE_CLASSES[role] || 'default';
    }

    getRoleIcon(role) {
        return HR_CONSTANTS.ROLE_ICONS[role] || 'fa-user';
    }

    formatDate(dateString) {
        if (!dateString) return 'N/A';
        try {
            const date = new Date(dateString);
            if (isNaN(date.getTime())) {
                console.warn('Fecha inválida:', dateString);
                return 'N/A';
            }

            return date.toLocaleDateString('es-CO', {
                year: 'numeric',
                month: 'short',
                day: 'numeric'
            });
        } catch (error) {
            console.warn('Error formateando fecha:', dateString, error);
            return 'N/A';
        }
    }

    /**
     * Método principal de coordinación - delega responsabilidades a módulos especializados
     */
    showErrorState() {
        this.renderer.showErrorState();
    }

    /**
     * Método de limpieza para liberar recursos
     */
    destroy() {
        // Limpiar event listeners
        this.cleanupEventListeners();

        // Limpiar caches
        this.renderCache.clear();

        // Limpiar referencias circulares
        this.renderer = null;
        this.validator = null;
        this.dataManager = null;

        console.log('HRManagementService destruido correctamente');
    }

    // Métodos delegados al renderer para mantener consistencia
    renderUsersTableRows() {
        return this.renderer.renderUsersTableRows();
    }

    renderPagination() {
        return this.renderer.renderPagination();
    }
}

/**
 * Módulo de Renderizado para HR Management
 */
class HRRenderer {
    constructor(mainService) {
        this.mainService = mainService;
    }

    renderMainPage() {
        const contentArea = document.getElementById(HR_CONSTANTS.SELECTORS.CONTENT_AREA);
        if (!contentArea) {
            console.error('No se encontró el área de contenido');
            return;
        }

        contentArea.innerHTML = `
            <div class="hr-management-container">
                <!-- Header específico de RRHH -->
                <div class="hr-header">
                    <div class="hr-title-section">
                        <i class="fas fa-user-tie hr-icon"></i>
                        <div class="hr-title-content">
                            <h1>Recursos Humanos</h1>
                            <p class="hr-subtitle">Gestión integral del personal y permisos de acceso</p>
                        </div>
                    </div>
                    <div class="hr-actions">
                        <button class="btn btn-primary btn-lg" onclick="hrManagementService.showCreateUserModal()">
                            <i class="fas fa-user-plus"></i>
                            <span>Nuevo Empleado</span>
                        </button>
                    </div>
                </div>

                <!-- Estadísticas específicas de RRHH -->
                <div class="hr-stats-grid">
                    <div class="hr-stat-card">
                        <div class="stat-icon">
                            <i class="fas fa-users"></i>
                        </div>
                        <div class="stat-content">
                            <div class="stat-value">${this.mainService.users.length}</div>
                            <div class="stat-label">Total Empleados</div>
                        </div>
                    </div>

                    <div class="hr-stat-card">
                        <div class="stat-icon">
                            <i class="fas fa-user-check"></i>
                        </div>
                        <div class="stat-content">
                            <div class="stat-value">${this.mainService.getActiveUsersCount()}</div>
                            <div class="stat-label">Empleados Activos</div>
                        </div>
                    </div>

                    <div class="hr-stat-card">
                        <div class="stat-icon">
                            <i class="fas fa-user-md"></i>
                        </div>
                        <div class="stat-content">
                            <div class="stat-value">${this.mainService.getRoleCount('DOCTOR')}</div>
                            <div class="stat-label">Médicos</div>
                        </div>
                    </div>

                    <div class="hr-stat-card">
                        <div class="stat-icon">
                            <i class="fas fa-user-nurse"></i>
                        </div>
                        <div class="stat-content">
                            <div class="stat-value">${this.mainService.getRoleCount('NURSE')}</div>
                            <div class="stat-label">Enfermeras</div>
                        </div>
                    </div>

                    <div class="hr-stat-card">
                        <div class="stat-icon">
                            <i class="fas fa-user-cog"></i>
                        </div>
                        <div class="stat-content">
                            <div class="stat-value">${this.mainService.getRoleCount('ADMINISTRATIVE_STAFF')}</div>
                            <div class="stat-label">Administrativos</div>
                        </div>
                    </div>

                    <div class="hr-stat-card">
                        <div class="stat-icon">
                            <i class="fas fa-headset"></i>
                        </div>
                        <div class="stat-content">
                            <div class="stat-value">${this.mainService.getRoleCount('SUPPORT_STAFF')}</div>
                            <div class="stat-label">Soporte TI</div>
                        </div>
                    </div>
                </div>

                <!-- Controles de búsqueda y filtros -->
                <div class="hr-controls">
                    <div class="search-section">
                        <div class="search-box">
                            <i class="fas fa-search search-icon"></i>
                            <input type="text" id="hr-user-search" class="search-input"
                                   placeholder="Buscar por nombre, cédula, usuario o email..."
                                   value="${this.mainService.searchTerm}">
                        </div>
                    </div>

                    <div class="filters-section">
                        <div class="filter-group">
                            <label for="role-filter">Filtrar por Rol:</label>
                            <select id="role-filter" class="filter-select">
                                <option value="${HR_CONSTANTS.FILTER_ALL}">Todos los roles</option>
                                <option value="DOCTOR">Médicos</option>
                                <option value="NURSE">Enfermeras</option>
                                <option value="ADMINISTRATIVE_STAFF">Administrativos</option>
                                <option value="SUPPORT_STAFF">Soporte TI</option>
                                <option value="HUMAN_RESOURCES">Recursos Humanos</option>
                            </select>
                        </div>

                        <div class="filter-group">
                            <label for="status-filter">Filtrar por Estado:</label>
                            <select id="status-filter" class="filter-select">
                                <option value="${HR_CONSTANTS.FILTER_ALL}">Todos los estados</option>
                                <option value="${HR_CONSTANTS.FILTER_ACTIVE}">Activos</option>
                                <option value="${HR_CONSTANTS.FILTER_INACTIVE}">Inactivos</option>
                            </select>
                        </div>
                    </div>
                </div>

                <!-- Tabla de empleados -->
                <div class="hr-table-container">
                    <table class="hr-table">
                        <thead>
                            <tr>
                                <th>Cédula</th>
                                <th>Nombre Completo</th>
                                <th>Usuario</th>
                                <th>Email</th>
                                <th>Rol</th>
                                <th>Estado</th>
                                <th>Fecha de Creación</th>
                                <th>Acciones</th>
                            </tr>
                        </thead>
                        <tbody id="hr-users-table-body">
                            ${this.renderUsersTableRows()}
                        </tbody>
                    </table>
                </div>

                <!-- Paginación -->
                ${this.renderPagination()}

                <!-- Información de seguridad -->
                <div class="hr-security-info">
                    <div class="security-banner">
                        <i class="fas fa-shield-alt"></i>
                        <div class="security-content">
                            <h4>Políticas de Seguridad</h4>
                            <p>Solo personal autorizado puede gestionar cuentas de empleados. Todas las acciones son auditadas.</p>
                        </div>
                    </div>
                </div>
            </div>

            <!-- Modal para crear/editar empleado -->
            ${this.renderUserModal()}
        `;

        // Aplicar filtros actuales
        this.mainService.applyFilters();
    }

    renderUsersTableRows() {
        const startIndex = (this.mainService.currentPage - 1) * this.mainService.itemsPerPage;
        const endIndex = startIndex + this.mainService.itemsPerPage;
        const usersToShow = this.mainService.filteredUsers.slice(startIndex, endIndex);

        if (usersToShow.length === 0) {
            return `
                <tr>
                    <td colspan="8" class="hr-empty-state">
                        <i class="fas fa-users"></i>
                        <p>No se encontraron empleados</p>
                        <small>Intente ajustar los filtros de búsqueda</small>
                    </td>
                </tr>
            `;
        }

        return usersToShow.map(user => `
            <tr class="hr-user-row ${user.active ? 'user-active' : 'user-inactive'}">
                <td class="user-cedula">
                    <span class="cedula-value">${window.formatCedula ? window.formatCedula(user.cedula) : user.cedula}</span>
                </td>
                <td class="user-fullname">
                    <div class="user-info">
                        <div class="user-name">${user.fullName}</div>
                        <div class="user-details">
                            <small class="user-phone">
                                <i class="fas fa-phone"></i>
                                ${user.phoneNumber || 'N/A'}
                            </small>
                        </div>
                    </div>
                </td>
                <td class="user-username">
                    <code class="username-badge">${user.username}</code>
                </td>
                <td class="user-email">
                    <div class="email-info">
                        <i class="fas fa-envelope"></i>
                        <span>${user.email}</span>
                    </div>
                </td>
                <td class="user-role">
                    <span class="role-badge role-${this.mainService.getRoleBadgeClass(user.role)}">
                        <i class="fas ${this.mainService.getRoleIcon(user.role)}"></i>
                        ${this.mainService.getRoleDisplayName(user.role)}
                    </span>
                </td>
                <td class="user-status">
                    <span class="status-badge ${user.active ? 'status-active' : 'status-inactive'}">
                        <i class="fas ${user.active ? 'fa-check-circle' : 'fa-ban'}"></i>
                        ${user.active ? 'Activo' : 'Inactivo'}
                    </span>
                </td>
                <td class="user-created-date">
                    <div class="date-info">
                        <i class="fas fa-calendar"></i>
                        <span>${this.mainService.formatDate(user.createdAt)}</span>
                    </div>
                </td>
                <td class="user-actions">
                    <div class="actions-menu">
                        <button class="btn btn-sm btn-primary" onclick="hrManagementService.editUser('${user.cedula}')" title="Editar empleado">
                            <i class="fas fa-edit"></i>
                            Editar
                        </button>
                        <button class="btn btn-sm ${user.active ? 'btn-warning' : 'btn-success'}"
                                onclick="hrManagementService.toggleUserStatus('${user.cedula}')"
                                title="${user.active ? 'Desactivar' : 'Activar'} empleado">
                            <i class="fas ${user.active ? 'fa-ban' : 'fa-check'}"></i>
                            ${user.active ? 'Desactivar' : 'Activar'}
                        </button>
                        <button class="btn btn-sm btn-danger" onclick="hrManagementService.deleteUser('${user.cedula}')" title="Eliminar empleado">
                            <i class="fas fa-trash"></i>
                            Eliminar
                        </button>
                    </div>
                </td>
            </tr>
        `).join('');
    }

    renderPagination() {
        const totalPages = Math.ceil(this.mainService.filteredUsers.length / this.mainService.itemsPerPage);

        if (totalPages <= 1) return '';

        let paginationHtml = `
            <div class="hr-pagination">
                <div class="pagination-info">
                    Mostrando ${((this.mainService.currentPage - 1) * this.mainService.itemsPerPage) + 1}-${Math.min(this.mainService.currentPage * this.mainService.itemsPerPage, this.mainService.filteredUsers.length)} de ${this.mainService.filteredUsers.length} empleados
                </div>
                <div class="pagination-controls">
        `;

        // Botón anterior
        if (this.mainService.currentPage > 1) {
            paginationHtml += `<button class="btn btn-sm btn-secondary" onclick="hrManagementService.goToPage(${this.mainService.currentPage - 1})">
                <i class="fas fa-chevron-left"></i> Anterior
            </button>`;
        }

        // Páginas
        const startPage = Math.max(1, this.mainService.currentPage - 2);
        const endPage = Math.min(totalPages, this.mainService.currentPage + 2);

        for (let i = startPage; i <= endPage; i++) {
            if (i === this.mainService.currentPage) {
                paginationHtml += `<button class="btn btn-sm btn-primary active">${i}</button>`;
            } else {
                paginationHtml += `<button class="btn btn-sm btn-secondary" onclick="hrManagementService.goToPage(${i})">${i}</button>`;
            }
        }

        // Botón siguiente
        if (this.mainService.currentPage < totalPages) {
            paginationHtml += `<button class="btn btn-sm btn-secondary" onclick="hrManagementService.goToPage(${this.mainService.currentPage + 1})">
                Siguiente <i class="fas fa-chevron-right"></i>
            </button>`;
        }

        paginationHtml += `
                </div>
            </div>
        `;

        return paginationHtml;
    }

    renderUserModal() {
        return `
            <div id="hr-user-modal" class="hr-modal-overlay" style="display: none;">
                <div class="hr-modal">
                    <div class="hr-modal-header">
                        <h2 id="hr-modal-title">
                            <i class="fas fa-user-plus"></i>
                            Crear Nuevo Empleado
                        </h2>
                        <button class="hr-modal-close" onclick="hrManagementService.closeUserModal()">
                            <i class="fas fa-times"></i>
                        </button>
                    </div>

                    <div class="hr-modal-body">
                        <form id="hr-user-form" class="hr-user-form">
                            <div class="form-alert" id="hr-form-alert" style="display: none;"></div>

                            <!-- Información Personal -->
                            <div class="form-section">
                                <h3><i class="fas fa-id-card"></i> Información Personal</h3>
                                <div class="form-row">
                                    <div class="form-group">
                                        <label for="hr-user-cedula">Cédula *</label>
                                        <input type="text" id="hr-user-cedula" name="cedula" required maxlength="${HR_CONSTANTS.MAX_CEDULA_LENGTH}"
                                               placeholder="Número de documento">
                                        <small class="form-hint">Solo números, único en el sistema</small>
                                    </div>
                                    <div class="form-group">
                                        <label for="hr-user-fullname">Nombre Completo *</label>
                                        <input type="text" id="hr-user-fullname" name="fullName" required maxlength="${HR_CONSTANTS.MAX_FULLNAME_LENGTH}"
                                               placeholder="Nombres y apellidos">
                                        <small class="form-hint">Máximo ${HR_CONSTANTS.MAX_FULLNAME_LENGTH} caracteres</small>
                                    </div>
                                </div>

                                <div class="form-row">
                                    <div class="form-group">
                                        <label for="hr-user-birthdate">Fecha de Nacimiento *</label>
                                        <input type="date" id="hr-user-birthdate" name="birthDate" required>
                                        <small class="form-hint">Se calculará automáticamente la edad</small>
                                    </div>
                                    <div class="form-group">
                                        <label for="hr-user-phone">Teléfono *</label>
                                        <input type="tel" id="hr-user-phone" name="phoneNumber" required maxlength="${HR_CONSTANTS.PHONE_LENGTH}"
                                               placeholder="Número celular">
                                        <small class="form-hint">Solo números, ${HR_CONSTANTS.PHONE_LENGTH} dígitos</small>
                                    </div>
                                </div>

                                <div class="form-group">
                                    <label for="hr-user-address">Dirección *</label>
                                    <input type="text" id="hr-user-address" name="address" required maxlength="${HR_CONSTANTS.MAX_ADDRESS_LENGTH}"
                                           placeholder="Dirección de residencia">
                                    <small class="form-hint">Máximo ${HR_CONSTANTS.MAX_ADDRESS_LENGTH} caracteres</small>
                                </div>
                            </div>

                            <!-- Información de Acceso -->
                            <div class="form-section">
                                <h3><i class="fas fa-key"></i> Información de Acceso</h3>
                                <div class="form-row">
                                    <div class="form-group">
                                        <label for="hr-user-username">Nombre de Usuario *</label>
                                        <input type="text" id="hr-user-username" name="username" required maxlength="${HR_CONSTANTS.MAX_USERNAME_LENGTH}"
                                               placeholder="Usuario único">
                                        <small class="form-hint">Solo letras y números, máximo ${HR_CONSTANTS.MAX_USERNAME_LENGTH} caracteres</small>
                                    </div>
                                    <div class="form-group">
                                        <label for="hr-user-email">Correo Electrónico *</label>
                                        <input type="email" id="hr-user-email" name="email" required
                                               placeholder="correo@ejemplo.com">
                                        <small class="form-hint">Debe tener formato válido y dominio existente</small>
                                    </div>
                                </div>

                                <div class="form-row">
                                    <div class="form-group">
                                        <label for="hr-user-role">Rol en el Sistema *</label>
                                        <select id="hr-user-role" name="role" required>
                                            <option value="">Seleccionar rol</option>
                                            <option value="DOCTOR">Médico</option>
                                            <option value="NURSE">Enfermera</option>
                                            <option value="ADMINISTRATIVE_STAFF">Personal Administrativo</option>
                                            <option value="SUPPORT_STAFF">Soporte de Información</option>
                                            <option value="HUMAN_RESOURCES">Recursos Humanos</option>
                                        </select>
                                        <small class="form-hint">Define los permisos y acceso al sistema</small>
                                    </div>
                                </div>

                                <div class="form-row">
                                    <div class="form-group">
                                        <label for="hr-user-password">Contraseña *</label>
                                        <input type="password" id="hr-user-password" name="password" required minlength="${HR_CONSTANTS.MIN_PASSWORD_LENGTH}"
                                               placeholder="Mínimo ${HR_CONSTANTS.MIN_PASSWORD_LENGTH} caracteres">
                                        <small class="form-hint">1 mayúscula, 1 número, 1 carácter especial</small>
                                    </div>
                                    <div class="form-group">
                                        <label for="hr-user-confirm-password">Confirmar Contraseña *</label>
                                        <input type="password" id="hr-user-confirm-password" name="confirmPassword" required minlength="${HR_CONSTANTS.MIN_PASSWORD_LENGTH}"
                                               placeholder="Repetir contraseña">
                                        <small class="form-hint">Debe coincidir con la contraseña anterior</small>
                                    </div>
                                </div>
                            </div>

                            <!-- Políticas de seguridad -->
                            <div class="form-section">
                                <div class="security-policies">
                                    <h4><i class="fas fa-shield-alt"></i> Políticas de Seguridad</h4>
                                    <div class="policy-list">
                                        <div class="policy-item">
                                            <i class="fas fa-check-circle"></i>
                                            <span>La contraseña debe cumplir con estándares de seguridad</span>
                                        </div>
                                        <div class="policy-item">
                                            <i class="fas fa-check-circle"></i>
                                            <span>Solo personal autorizado puede gestionar cuentas</span>
                                        </div>
                                        <div class="policy-item">
                                            <i class="fas fa-check-circle"></i>
                                            <span>Todas las acciones son auditadas en el sistema</span>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </form>
                    </div>

                    <div class="hr-modal-footer">
                        <button class="btn btn-secondary" onclick="hrManagementService.closeUserModal()">
                            <i class="fas fa-times"></i>
                            Cancelar
                        </button>
                        <button class="btn btn-primary" onclick="hrManagementService.saveUser()">
                            <i class="fas fa-save"></i>
                            <span id="hr-save-btn-text">Guardar Empleado</span>
                        </button>
                    </div>
                </div>
            </div>
        `;
    }

    refreshTable() {
        const tableBody = document.getElementById(HR_CONSTANTS.SELECTORS.HR_USERS_TABLE_BODY);
        if (tableBody) {
            tableBody.innerHTML = this.renderUsersTableRows();
        }

        // Actualizar paginación
        const pagination = document.querySelector('.hr-pagination');
        if (pagination) {
            pagination.outerHTML = this.renderPagination();
        }
    }

    showErrorState() {
        const contentArea = document.getElementById(HR_CONSTANTS.SELECTORS.CONTENT_AREA);
        if (contentArea) {
            contentArea.innerHTML = `
                <div class="hr-error-state">
                    <i class="fas fa-exclamation-triangle"></i>
                    <h2>Error al cargar Recursos Humanos</h2>
                    <p>No se pudo inicializar el sistema de gestión de empleados.</p>
                    <button class="btn btn-primary" onclick="hrManagementService.initialize()">
                        <i class="fas fa-refresh"></i> Reintentar
                    </button>
                </div>
            `;
        }
    }
}

/**
 * Módulo de Validaciones para HR Management con patrón Observer
 */
class HRValidator {
    constructor() {
        this.debounceTimers = new Map();
        this.observers = new Set();
    }

    /**
     * Patrón Observer: permite que otros módulos se suscriban a cambios de validación
     */
    subscribe(observer) {
        this.observers.add(observer);
    }

    unsubscribe(observer) {
        this.observers.delete(observer);
    }

    notify(event, data) {
        this.observers.forEach(observer => {
            if (typeof observer[event] === 'function') {
                observer[event](data);
            }
        });
    }

    setupFormValidation() {
        const form = document.getElementById(HR_CONSTANTS.SELECTORS.HR_USER_FORM);
        if (!form) return;

        // Validación de cédula
        const cedulaInput = document.getElementById('hr-user-cedula');
        if (cedulaInput) {
            cedulaInput.addEventListener('input', window.debounce((e) => {
                this.validateCedula(e.target);
            }, 300));
        }

        // Validación de usuario
        const usernameInput = document.getElementById('hr-user-username');
        if (usernameInput) {
            usernameInput.addEventListener('input', window.debounce((e) => {
                this.validateUsername(e.target);
            }, 300));
        }

        // Validación de email
        const emailInput = document.getElementById('hr-user-email');
        if (emailInput) {
            emailInput.addEventListener('blur', (e) => {
                this.validateEmail(e.target);
            });
        }

        // Validación de contraseña
        const passwordInput = document.getElementById('hr-user-password');
        if (passwordInput) {
            passwordInput.addEventListener('input', window.debounce((e) => {
                this.validatePassword(e.target);
            }, 300));
        }

        // Confirmación de contraseña
        const confirmPasswordInput = document.getElementById('hr-user-confirm-password');
        if (confirmPasswordInput) {
            confirmPasswordInput.addEventListener('input', window.debounce((e) => {
                this.validatePasswordConfirmation(e.target);
            }, 300));
        }
    }

    validateCedula(input) {
        const cedula = input.value.trim();
        return this.validateCedulaField(cedula);
    }

    validateUsername(input) {
        const username = input.value.trim();
        return this.validateUsernameField(username);
    }

    validateEmail(input) {
        const email = input.value.trim();
        return this.validateEmailField(email);
    }

    validatePassword(input) {
        const password = input.value;
        return this.validatePasswordField(password);
    }

    validatePasswordConfirmation(input) {
        const confirmPassword = input.value;
        const password = document.getElementById('hr-user-password').value;

        if (!confirmPassword) {
            this.showFieldError('hr-user-confirm-password', HR_CONSTANTS.ERRORS.PASSWORD_MISMATCH);
            return false;
        }

        if (confirmPassword !== password) {
            this.showFieldError('hr-user-confirm-password', HR_CONSTANTS.ERRORS.PASSWORD_MISMATCH);
            return false;
        }

        this.clearFieldError('hr-user-confirm-password');
        return true;
    }

    validateForm(currentUser) {
        const validations = [
            { field: 'hr-user-cedula', validator: (value) => this.validateCedulaField(value) },
            { field: 'hr-user-username', validator: (value) => this.validateUsernameField(value) },
            { field: 'hr-user-fullname', validator: (value) => this.validateFullName(value) },
            { field: 'hr-user-email', validator: (value) => this.validateEmailField(value) },
            { field: 'hr-user-phone', validator: (value) => this.validatePhone(value) },
            { field: 'hr-user-address', validator: (value) => this.validateAddress(value) },
            { field: 'hr-user-role', validator: (value) => this.validateRole(value) },
            { field: 'hr-user-birthdate', validator: (value) => this.validateBirthDate(value) }
        ];

        for (const validation of validations) {
            const element = document.getElementById(validation.field);
            const value = element ? element.value.trim() : '';

            if (!validation.validator(value)) {
                element?.focus();
                return false;
            }
        }

        // Validar contraseña si es creación o si se proporciona
        const password = document.getElementById('hr-user-password').value;
        if (!currentUser || password) {
            if (!this.validatePasswordField(password)) {
                document.getElementById('hr-user-password')?.focus();
                return false;
            }
        }

        return true;
    }

    validateCedulaField(cedula) {
        if (!cedula) {
            this.showFieldError('hr-user-cedula', HR_CONSTANTS.ERRORS.CEDULA_REQUIRED);
            return false;
        }

        if (!/^\d+$/.test(cedula)) {
            this.showFieldError('hr-user-cedula', HR_CONSTANTS.ERRORS.CEDULA_NUMERIC);
            return false;
        }

        if (cedula.length < HR_CONSTANTS.MIN_CEDULA_LENGTH || cedula.length > HR_CONSTANTS.MAX_CEDULA_LENGTH) {
            this.showFieldError('hr-user-cedula', HR_CONSTANTS.ERRORS.CEDULA_LENGTH);
            return false;
        }

        this.clearFieldError('hr-user-cedula');
        return true;
    }

    validateUsernameField(username) {
        if (!username) {
            this.showFieldError('hr-user-username', HR_CONSTANTS.ERRORS.USERNAME_REQUIRED);
            return false;
        }

        if (!/^[a-zA-Z0-9]+$/.test(username)) {
            this.showFieldError('hr-user-username', HR_CONSTANTS.ERRORS.USERNAME_ALPHANUMERIC);
            return false;
        }

        if (username.length > HR_CONSTANTS.MAX_USERNAME_LENGTH) {
            this.showFieldError('hr-user-username', HR_CONSTANTS.ERRORS.USERNAME_LENGTH);
            return false;
        }

        this.clearFieldError('hr-user-username');
        return true;
    }

    validateFullName(fullName) {
        if (!fullName) {
            this.showFieldError('hr-user-fullname', HR_CONSTANTS.ERRORS.FULLNAME_REQUIRED);
            return false;
        }

        if (fullName.length > HR_CONSTANTS.MAX_FULLNAME_LENGTH) {
            this.showFieldError('hr-user-fullname', HR_CONSTANTS.ERRORS.FULLNAME_LENGTH);
            return false;
        }

        this.clearFieldError('hr-user-fullname');
        return true;
    }

    validateEmailField(email) {
        if (!email) {
            this.showFieldError('hr-user-email', HR_CONSTANTS.ERRORS.EMAIL_REQUIRED);
            return false;
        }

        if (!window.isValidEmail ? true : window.isValidEmail(email)) {
            this.clearFieldError('hr-user-email');
            return true;
        } else {
            this.showFieldError('hr-user-email', HR_CONSTANTS.ERRORS.EMAIL_INVALID);
            return false;
        }
    }

    validatePhone(phone) {
        if (!phone) {
            this.showFieldError('hr-user-phone', HR_CONSTANTS.ERRORS.PHONE_REQUIRED);
            return false;
        }

        if (!/^\d{10}$/.test(phone)) {
            this.showFieldError('hr-user-phone', HR_CONSTANTS.ERRORS.PHONE_LENGTH);
            return false;
        }

        this.clearFieldError('hr-user-phone');
        return true;
    }

    validateAddress(address) {
        if (!address) {
            this.showFieldError('hr-user-address', HR_CONSTANTS.ERRORS.ADDRESS_REQUIRED);
            return false;
        }

        if (address.length > HR_CONSTANTS.MAX_ADDRESS_LENGTH) {
            this.showFieldError('hr-user-address', HR_CONSTANTS.ERRORS.ADDRESS_LENGTH);
            return false;
        }

        this.clearFieldError('hr-user-address');
        return true;
    }

    validateRole(role) {
        if (!role) {
            this.showFieldError('hr-user-role', HR_CONSTANTS.ERRORS.ROLE_REQUIRED);
            return false;
        }

        if (!HR_CONSTANTS.VALID_ROLES.includes(role)) {
            this.showFieldError('hr-user-role', HR_CONSTANTS.ERRORS.ROLE_INVALID);
            return false;
        }

        this.clearFieldError('hr-user-role');
        return true;
    }

    validateBirthDate(birthDate) {
        if (!birthDate) {
            this.showFieldError('hr-user-birthdate', HR_CONSTANTS.ERRORS.BIRTHDATE_REQUIRED);
            return false;
        }

        const birth = new Date(birthDate);
        const today = new Date();
        const age = today.getFullYear() - birth.getFullYear();

        if (age > HR_CONSTANTS.MAX_AGE) {
            this.showFieldError('hr-user-birthdate', HR_CONSTANTS.ERRORS.BIRTHDATE_TOO_OLD);
            return false;
        }

        if (birth > today) {
            this.showFieldError('hr-user-birthdate', HR_CONSTANTS.ERRORS.BIRTHDATE_FUTURE);
            return false;
        }

        this.clearFieldError('hr-user-birthdate');
        return true;
    }

    validatePasswordField(password) {
        if (!password) {
            this.showFieldError('hr-user-password', HR_CONSTANTS.ERRORS.PASSWORD_REQUIRED);
            return false;
        }

        if (password.length < HR_CONSTANTS.MIN_PASSWORD_LENGTH) {
            this.showFieldError('hr-user-password', HR_CONSTANTS.ERRORS.PASSWORD_LENGTH);
            return false;
        }

        if (!/(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&])/.test(password)) {
            this.showFieldError('hr-user-password', HR_CONSTANTS.ERRORS.PASSWORD_COMPLEXITY);
            return false;
        }

        this.clearFieldError('hr-user-password');
        return true;
    }

    showFieldError(fieldId, message) {
        const field = document.getElementById(fieldId);
        if (field) {
            field.classList.add('field-error');

            let errorElement = field.parentNode.querySelector('.field-error-message');
            if (!errorElement) {
                errorElement = document.createElement('div');
                errorElement.className = 'field-error-message';
                field.parentNode.appendChild(errorElement);
            }

            errorElement.textContent = message;
        }
    }

    clearFieldError(fieldId) {
        const field = document.getElementById(fieldId);
        if (field) {
            field.classList.remove('field-error');

            const errorElement = field.parentNode.querySelector('.field-error-message');
            if (errorElement) {
                errorElement.remove();
            }
        }
    }

    showFormAlert(message, type = 'error') {
        const alertElement = document.getElementById(HR_CONSTANTS.SELECTORS.HR_FORM_ALERT);
        if (alertElement) {
            alertElement.textContent = message;
            alertElement.className = `form-alert alert-${type}`;
            alertElement.style.display = 'block';

            // Notificar a observadores sobre el cambio de estado de validación
            this.notify('onValidationError', { message, type });
        }
    }

    hideFormAlert() {
        const alertElement = document.getElementById(HR_CONSTANTS.SELECTORS.HR_FORM_ALERT);
        if (alertElement) {
            alertElement.style.display = 'none';

            // Notificar a observadores sobre el cambio de estado de validación
            this.notify('onValidationSuccess', {});
        }
    }

    getFormData() {
        const data = {
            cedula: document.getElementById('hr-user-cedula').value.trim(),
            username: document.getElementById('hr-user-username').value.trim(),
            fullName: document.getElementById('hr-user-fullname').value.trim(),
            email: document.getElementById('hr-user-email').value.trim(),
            phoneNumber: document.getElementById('hr-user-phone').value.trim(),
            address: document.getElementById('hr-user-address').value.trim(),
            role: document.getElementById('hr-user-role').value,
            birthDate: document.getElementById('hr-user-birthdate').value
        };

        // Incluir contraseña solo si se proporciona
        const password = document.getElementById('hr-user-password').value;
        if (password) {
            data.password = password;
        }

        return data;
    }
}

/**
 * Módulo de Manejo de Datos para HR Management con patrón Observer
 */
class HRDataManager {
    constructor(mainService) {
        this.mainService = mainService;
        this.observers = new Set();
    }

    /**
     * Patrón Observer: permite que otros módulos se suscriban a cambios de datos
     */
    subscribe(observer) {
        this.observers.add(observer);
    }

    unsubscribe(observer) {
        this.observers.delete(observer);
    }

    notify(event, data) {
        this.observers.forEach(observer => {
            if (typeof observer[event] === 'function') {
                observer[event](data);
            }
        });
    }

    async loadUsersData() {
        try {
            console.log('🔄 Cargando usuarios desde localStorage...');

            const users = window.localStorageService.getUsers();

            if (users && Array.isArray(users)) {
                this.mainService.users = users;
                this.mainService.filteredUsers = [...this.mainService.users];
                console.log(`✅ Cargados ${this.mainService.users.length} usuarios desde localStorage`);
            } else {
                console.warn('⚠️ No se encontraron usuarios, inicializando con usuario por defecto');
                const defaultUser = {
                    id: 'hr_default',
                    cedula: '1234567890',
                    username: 'admin_hr',
                    password: 'password',
                    fullName: 'Admin Recursos Humanos',
                    email: 'admin@clinic.com',
                    phoneNumber: '3001234567',
                    birthDate: '01/01/1980',
                    address: 'Calle 123 #45-67',
                    role: 'HUMAN_RESOURCES',
                    active: true,
                    createdAt: new Date().toISOString()
                };
                window.localStorageService.saveUser(defaultUser);
                this.mainService.users = [defaultUser];
                this.mainService.filteredUsers = [...this.mainService.users];
            }
        } catch (error) {
            console.error('❌ Error cargando usuarios:', error);
            throw new Error('No se pudieron cargar los usuarios: ' + error.message);
        }
    }

    async toggleUserStatus(cedula) {
        try {
            const user = this.mainService.users.find(u => u.cedula === cedula);
            if (!user) {
                throw new Error(HR_CONSTANTS.ERRORS.USER_NOT_FOUND);
            }

            const action = user.active ? 'desactivar' : 'activar';
            const confirmMessage = `¿Está seguro de que desea ${action} a este empleado?`;

            if (!confirm(confirmMessage)) {
                return;
            }

            window.showInfo(`Cambiando estado del empleado...`);

            user.active = !user.active;
            const result = window.localStorageService.saveUser(user);

            if (result) {
                window.showSuccess(HR_CONSTANTS.SUCCESS.USER_STATUS_CHANGED);
                await this.loadUsersData();
                this.mainService.renderHRPage();

                // Notificar a observadores sobre el cambio
                this.notify('onDataChanged', { type: 'status_changed', cedula, newStatus: user.active });
            }

        } catch (error) {
            console.error('Error cambiando estado:', error);
            window.showError(HR_CONSTANTS.ERRORS.TOGGLE_STATUS_FAILED + ': ' + error.message);
        }
    }

    async deleteUser(cedula) {
        if (!confirm('¿Está seguro de que desea eliminar este empleado? Esta acción registra el eliminadoPor y eliminadoEn, y puede requerir hard delete si no hay dependencias.')) {
            return;
        }

        try {
            window.showInfo('Eliminando empleado...');

            window.localStorageService.deleteUser(cedula);

            window.showSuccess(HR_CONSTANTS.SUCCESS.USER_DELETED);
            await this.loadUsersData();
            this.mainService.renderHRPage();

            // Notificar a observadores sobre el cambio
            this.notify('onUserDeleted', { cedula });

        } catch (error) {
            console.error('Error eliminando empleado:', error);
            window.showError(HR_CONSTANTS.ERRORS.DELETE_FAILED + ': ' + error.message);
        }
    }

    async saveUser(userData) {
        const isEditing = !!this.mainService.currentUser;

        try {
            window.showInfo(isEditing ? 'Actualizando empleado...' : 'Creando empleado...');

            const result = window.localStorageService.saveUser(userData);

            if (result) {
                window.showSuccess(isEditing ? HR_CONSTANTS.SUCCESS.USER_UPDATED : HR_CONSTANTS.SUCCESS.USER_CREATED);
                this.mainService.closeUserModal();
                await this.loadUsersData();
                this.mainService.renderHRPage();

                // Notificar a observadores sobre el cambio
                this.notify(isEditing ? 'onUserUpdated' : 'onUserCreated', { userData, result });
            }

        } catch (error) {
            console.error('Error guardando empleado:', error);
            this.mainService.validator.showFormAlert(HR_CONSTANTS.ERRORS.SAVE_FAILED + ': ' + error.message, 'error');
        }
    }
}

// Crear instancia global del servicio de Recursos Humanos
window.hrManagementService = new HRManagementService();