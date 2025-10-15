/**
 * Servicio de Gestión de Recursos Humanos
 * Implementa funcionalidades específicas para el rol RECURSOS_HUMANOS según especificaciones técnicas
 */
class HRManagementService {
    constructor() {
        this.users = [];
        this.filteredUsers = [];
        this.currentPage = 1;
        this.itemsPerPage = 10;
        this.searchTerm = '';
        this.selectedRoleFilter = 'ALL';
        this.selectedStatusFilter = 'ALL';
    }

    /**
     * Inicializa la página de Recursos Humanos
     */
    async initialize() {
        try {
            window.showInfo('Cargando sistema de Recursos Humanos...');
            await this.loadUsersData();
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
     * Carga los datos de usuarios desde la API
     */
    async loadUsersData() {
        try {
            const response = await window.userApi.findAllUsers();

            if (response && Array.isArray(response)) {
                this.users = response;
                this.filteredUsers = [...this.users];
                console.log(`✅ Cargados ${this.users.length} usuarios`);
            } else {
                throw new Error('Formato de respuesta inválido');
            }
        } catch (error) {
            console.error('Error cargando usuarios:', error);
            throw new Error('No se pudieron cargar los usuarios: ' + error.message);
        }
    }

    /**
     * Renderiza la página completa de Recursos Humanos
     */
    renderHRPage() {
        const contentArea = document.getElementById('content-area');
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
                            <div class="stat-value">${this.users.length}</div>
                            <div class="stat-label">Total Empleados</div>
                        </div>
                    </div>

                    <div class="hr-stat-card">
                        <div class="stat-icon">
                            <i class="fas fa-user-check"></i>
                        </div>
                        <div class="stat-content">
                            <div class="stat-value">${this.getActiveUsersCount()}</div>
                            <div class="stat-label">Empleados Activos</div>
                        </div>
                    </div>

                    <div class="hr-stat-card">
                        <div class="stat-icon">
                            <i class="fas fa-user-md"></i>
                        </div>
                        <div class="stat-content">
                            <div class="stat-value">${this.getRoleCount('DOCTOR')}</div>
                            <div class="stat-label">Médicos</div>
                        </div>
                    </div>

                    <div class="hr-stat-card">
                        <div class="stat-icon">
                            <i class="fas fa-user-nurse"></i>
                        </div>
                        <div class="stat-content">
                            <div class="stat-value">${this.getRoleCount('NURSE')}</div>
                            <div class="stat-label">Enfermeras</div>
                        </div>
                    </div>

                    <div class="hr-stat-card">
                        <div class="stat-icon">
                            <i class="fas fa-user-cog"></i>
                        </div>
                        <div class="stat-content">
                            <div class="stat-value">${this.getRoleCount('ADMINISTRATIVE_STAFF')}</div>
                            <div class="stat-label">Administrativos</div>
                        </div>
                    </div>

                    <div class="hr-stat-card">
                        <div class="stat-icon">
                            <i class="fas fa-headset"></i>
                        </div>
                        <div class="stat-content">
                            <div class="stat-value">${this.getRoleCount('SUPPORT_STAFF')}</div>
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
                                   value="${this.searchTerm}">
                        </div>
                    </div>

                    <div class="filters-section">
                        <div class="filter-group">
                            <label for="role-filter">Filtrar por Rol:</label>
                            <select id="role-filter" class="filter-select">
                                <option value="ALL">Todos los roles</option>
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
                                <option value="ALL">Todos los estados</option>
                                <option value="ACTIVE">Activos</option>
                                <option value="INACTIVE">Inactivos</option>
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
        this.applyFilters();
    }

    /**
     * Renderiza las filas de la tabla de usuarios
     */
    renderUsersTableRows() {
        const startIndex = (this.currentPage - 1) * this.itemsPerPage;
        const endIndex = startIndex + this.itemsPerPage;
        const usersToShow = this.filteredUsers.slice(startIndex, endIndex);

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
                    <span class="role-badge role-${this.getRoleBadgeClass(user.role)}">
                        <i class="fas ${this.getRoleIcon(user.role)}"></i>
                        ${this.getRoleDisplayName(user.role)}
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
                        <span>${this.formatDate(user.createdAt)}</span>
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

    /**
     * Renderiza la paginación
     */
    renderPagination() {
        const totalPages = Math.ceil(this.filteredUsers.length / this.itemsPerPage);

        if (totalPages <= 1) return '';

        let paginationHtml = `
            <div class="hr-pagination">
                <div class="pagination-info">
                    Mostrando ${((this.currentPage - 1) * this.itemsPerPage) + 1}-${Math.min(this.currentPage * this.itemsPerPage, this.filteredUsers.length)} de ${this.filteredUsers.length} empleados
                </div>
                <div class="pagination-controls">
        `;

        // Botón anterior
        if (this.currentPage > 1) {
            paginationHtml += `<button class="btn btn-sm btn-secondary" onclick="hrManagementService.goToPage(${this.currentPage - 1})">
                <i class="fas fa-chevron-left"></i> Anterior
            </button>`;
        }

        // Páginas
        const startPage = Math.max(1, this.currentPage - 2);
        const endPage = Math.min(totalPages, this.currentPage + 2);

        for (let i = startPage; i <= endPage; i++) {
            if (i === this.currentPage) {
                paginationHtml += `<button class="btn btn-sm btn-primary active">${i}</button>`;
            } else {
                paginationHtml += `<button class="btn btn-sm btn-secondary" onclick="hrManagementService.goToPage(${i})">${i}</button>`;
            }
        }

        // Botón siguiente
        if (this.currentPage < totalPages) {
            paginationHtml += `<button class="btn btn-sm btn-secondary" onclick="hrManagementService.goToPage(${this.currentPage + 1})">
                Siguiente <i class="fas fa-chevron-right"></i>
            </button>`;
        }

        paginationHtml += `
                </div>
            </div>
        `;

        return paginationHtml;
    }

    /**
     * Renderiza el modal de usuario
     */
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
                                        <input type="text" id="hr-user-cedula" name="cedula" required maxlength="20"
                                               placeholder="Número de documento">
                                        <small class="form-hint">Solo números, único en el sistema</small>
                                    </div>
                                    <div class="form-group">
                                        <label for="hr-user-fullname">Nombre Completo *</label>
                                        <input type="text" id="hr-user-fullname" name="fullName" required maxlength="100"
                                               placeholder="Nombres y apellidos">
                                        <small class="form-hint">Máximo 100 caracteres</small>
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
                                        <input type="tel" id="hr-user-phone" name="phoneNumber" required maxlength="10"
                                               placeholder="Número celular">
                                        <small class="form-hint">Solo números, 10 dígitos</small>
                                    </div>
                                </div>

                                <div class="form-group">
                                    <label for="hr-user-address">Dirección *</label>
                                    <input type="text" id="hr-user-address" name="address" required maxlength="30"
                                           placeholder="Dirección de residencia">
                                    <small class="form-hint">Máximo 30 caracteres</small>
                                </div>
                            </div>

                            <!-- Información de Acceso -->
                            <div class="form-section">
                                <h3><i class="fas fa-key"></i> Información de Acceso</h3>
                                <div class="form-row">
                                    <div class="form-group">
                                        <label for="hr-user-username">Nombre de Usuario *</label>
                                        <input type="text" id="hr-user-username" name="username" required maxlength="15"
                                               placeholder="Usuario único">
                                        <small class="form-hint">Solo letras y números, máximo 15 caracteres</small>
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
                                        <input type="password" id="hr-user-password" name="password" required minlength="8"
                                               placeholder="Mínimo 8 caracteres">
                                        <small class="form-hint">1 mayúscula, 1 número, 1 carácter especial</small>
                                    </div>
                                    <div class="form-group">
                                        <label for="hr-user-confirm-password">Confirmar Contraseña *</label>
                                        <input type="password" id="hr-user-confirm-password" name="confirmPassword" required minlength="8"
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

    /**
     * Configura los event listeners
     */
    setupEventListeners() {
        // Búsqueda
        const searchInput = document.getElementById('hr-user-search');
        if (searchInput) {
            searchInput.addEventListener('input', window.debounce((e) => {
                this.searchTerm = e.target.value;
                this.applyFilters();
            }, 300));
        }

        // Filtros
        const roleFilter = document.getElementById('role-filter');
        const statusFilter = document.getElementById('status-filter');

        if (roleFilter) {
            roleFilter.addEventListener('change', (e) => {
                this.selectedRoleFilter = e.target.value;
                this.applyFilters();
            });
        }

        if (statusFilter) {
            statusFilter.addEventListener('change', (e) => {
                this.selectedStatusFilter = e.target.value;
                this.applyFilters();
            });
        }

        // Validación del formulario en tiempo real
        this.setupFormValidation();
    }

    /**
     * Configura la validación del formulario en tiempo real
     */
    setupFormValidation() {
        const form = document.getElementById('hr-user-form');
        if (!form) return;

        // Validación de cédula
        const cedulaInput = document.getElementById('hr-user-cedula');
        if (cedulaInput) {
            cedulaInput.addEventListener('input', (e) => {
                this.validateCedula(e.target);
            });
        }

        // Validación de usuario
        const usernameInput = document.getElementById('hr-user-username');
        if (usernameInput) {
            usernameInput.addEventListener('input', (e) => {
                this.validateUsername(e.target);
            });
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
            passwordInput.addEventListener('input', (e) => {
                this.validatePassword(e.target);
            });
        }

        // Confirmación de contraseña
        const confirmPasswordInput = document.getElementById('hr-user-confirm-password');
        if (confirmPasswordInput) {
            confirmPasswordInput.addEventListener('input', (e) => {
                this.validatePasswordConfirmation(e.target);
            });
        }
    }

    /**
     * Valida el campo de cédula en tiempo real
     */
    validateCedula(input) {
        const cedula = input.value.trim();
        this.validateCedulaField(cedula);
    }

    /**
     * Valida el campo de usuario en tiempo real
     */
    validateUsername(input) {
        const username = input.value.trim();
        this.validateUsernameField(username);
    }

    /**
     * Valida el campo de email en tiempo real
     */
    validateEmail(input) {
        const email = input.value.trim();
        this.validateEmailField(email);
    }

    /**
     * Valida el campo de contraseña en tiempo real
     */
    validatePassword(input) {
        const password = input.value;
        this.validatePasswordField(password);
    }

    /**
     * Valida la confirmación de contraseña en tiempo real
     */
    validatePasswordConfirmation(input) {
        const confirmPassword = input.value;
        const password = document.getElementById('hr-user-password').value;

        if (!confirmPassword) {
            this.showFieldError('hr-user-confirm-password', 'La confirmación de contraseña es obligatoria');
            return false;
        }

        if (confirmPassword !== password) {
            this.showFieldError('hr-user-confirm-password', 'Las contraseñas no coinciden');
            return false;
        }

        this.clearFieldError('hr-user-confirm-password');
        return true;
    }

    /**
     * Aplica filtros de búsqueda y estado
     */
    applyFilters() {
        let filtered = [...this.users];

        // Aplicar búsqueda
        if (this.searchTerm) {
            const term = this.searchTerm.toLowerCase();
            filtered = filtered.filter(user =>
                user.fullName.toLowerCase().includes(term) ||
                user.cedula.includes(term) ||
                user.username.toLowerCase().includes(term) ||
                user.email.toLowerCase().includes(term)
            );
        }

        // Aplicar filtro de rol
        if (this.selectedRoleFilter !== 'ALL') {
            filtered = filtered.filter(user => user.role === this.selectedRoleFilter);
        }

        // Aplicar filtro de estado
        if (this.selectedStatusFilter !== 'ALL') {
            const isActive = this.selectedStatusFilter === 'ACTIVE';
            filtered = filtered.filter(user => user.active === isActive);
        }

        this.filteredUsers = filtered;
        this.currentPage = 1;
        this.refreshTable();
    }

    /**
     * Refresca la tabla sin recargar toda la página
     */
    refreshTable() {
        const tableBody = document.getElementById('hr-users-table-body');
        if (tableBody) {
            tableBody.innerHTML = this.renderUsersTableRows();
        }

        // Actualizar paginación
        const pagination = document.querySelector('.hr-pagination');
        if (pagination) {
            pagination.outerHTML = this.renderPagination();
        }
    }

    /**
     * Muestra el modal para crear usuario
     */
    showCreateUserModal() {
        this.currentUser = null;
        this.showUserModal();
        this.resetForm();
    }

    /**
     * Muestra el modal para editar usuario
     */
    async editUser(cedula) {
        try {
            window.showInfo('Cargando información del empleado...');

            const user = await window.userApi.findUserByCedula(cedula);

            if (user) {
                this.currentUser = user;
                this.showUserModal();
                this.populateForm(user);
                window.showSuccess('Empleado cargado para edición');
            } else {
                throw new Error('Empleado no encontrado');
            }

        } catch (error) {
            console.error('Error cargando empleado:', error);
            window.showError('Error al cargar empleado: ' + error.message);
        }
    }

    /**
     * Muestra el modal de usuario
     */
    showUserModal() {
        const modal = document.getElementById('hr-user-modal');
        if (modal) {
            modal.style.display = 'flex';
            setTimeout(() => {
                modal.classList.add('show');
            }, 10);
        }
    }

    /**
     * Cierra el modal de usuario
     */
    closeUserModal() {
        const modal = document.getElementById('hr-user-modal');
        if (modal) {
            modal.classList.remove('show');
            setTimeout(() => {
                modal.style.display = 'none';
            }, 300);
        }
        this.currentUser = null;
    }

    /**
     * Resetea el formulario
     */
    resetForm() {
        const form = document.getElementById('hr-user-form');
        if (form) {
            form.reset();
        }

        // Resetear título del modal
        const modalTitle = document.getElementById('hr-modal-title');
        if (modalTitle) {
            modalTitle.innerHTML = '<i class="fas fa-user-plus"></i> Crear Nuevo Empleado';
        }

        // Ocultar alertas
        this.hideFormAlert();

        // Resetear botón de guardar
        const saveBtn = document.getElementById('hr-save-btn-text');
        if (saveBtn) {
            saveBtn.textContent = 'Guardar Empleado';
        }
    }

    /**
     * Llena el formulario con datos del usuario
     */
    populateForm(user) {
        const modalTitle = document.getElementById('hr-modal-title');
        if (modalTitle) {
            modalTitle.innerHTML = '<i class="fas fa-user-edit"></i> Editar Empleado';
        }

        const saveBtn = document.getElementById('hr-save-btn-text');
        if (saveBtn) {
            saveBtn.textContent = 'Actualizar Empleado';
        }

        // Llenar campos
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

        // Formatear fecha de nacimiento
        if (user.birthDate) {
            const birthDate = new Date(user.birthDate);
            const birthDateInput = document.getElementById('hr-user-birthdate');
            if (birthDateInput) {
                birthDateInput.value = birthDate.toISOString().split('T')[0];
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
     * Guarda el usuario (crear o actualizar)
     */
    async saveUser() {
        if (!this.validateForm()) {
            return;
        }

        const userData = this.getFormData();
        const isEditing = !!this.currentUser;

        try {
            window.showInfo(isEditing ? 'Actualizando empleado...' : 'Creando empleado...');

            let result;
            if (isEditing) {
                result = await window.userApi.updateUser(this.currentUser.cedula, userData);
            } else {
                result = await window.userApi.createUser(userData);
            }

            if (result) {
                window.showSuccess(`Empleado ${isEditing ? 'actualizado' : 'creado'} exitosamente`);
                this.closeUserModal();
                await this.loadUsersData();
                this.renderHRPage();
            }

        } catch (error) {
            console.error('Error guardando empleado:', error);
            this.showFormAlert(error.message, 'error');
        }
    }

    /**
     * Valida el formulario completo
     */
    validateForm() {
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
        if (!this.currentUser || password) {
            if (!this.validatePasswordField(password)) {
                document.getElementById('hr-user-password')?.focus();
                return false;
            }
        }

        return true;
    }

    /**
     * Validaciones específicas según especificaciones técnicas
     */
    validateCedulaField(cedula) {
        if (!cedula) {
            this.showFieldError('hr-user-cedula', 'La cédula es obligatoria');
            return false;
        }

        if (!/^\d+$/.test(cedula)) {
            this.showFieldError('hr-user-cedula', 'La cédula debe contener solo números');
            return false;
        }

        if (cedula.length < 6 || cedula.length > 12) {
            this.showFieldError('hr-user-cedula', 'La cédula debe tener entre 6 y 12 dígitos');
            return false;
        }

        this.clearFieldError('hr-user-cedula');
        return true;
    }

    validateUsernameField(username) {
        if (!username) {
            this.showFieldError('hr-user-username', 'El nombre de usuario es obligatorio');
            return false;
        }

        if (!/^[a-zA-Z0-9]+$/.test(username)) {
            this.showFieldError('hr-user-username', 'El usuario debe contener solo letras y números');
            return false;
        }

        if (username.length > 15) {
            this.showFieldError('hr-user-username', 'El usuario no puede tener más de 15 caracteres');
            return false;
        }

        this.clearFieldError('hr-user-username');
        return true;
    }

    validateFullName(fullName) {
        if (!fullName) {
            this.showFieldError('hr-user-fullname', 'El nombre completo es obligatorio');
            return false;
        }

        if (fullName.length > 100) {
            this.showFieldError('hr-user-fullname', 'El nombre no puede tener más de 100 caracteres');
            return false;
        }

        this.clearFieldError('hr-user-fullname');
        return true;
    }

    validateEmailField(email) {
        if (!email) {
            this.showFieldError('hr-user-email', 'El correo electrónico es obligatorio');
            return false;
        }

        if (!window.isValidEmail ? true : window.isValidEmail(email)) {
            this.clearFieldError('hr-user-email');
            return true;
        } else {
            this.showFieldError('hr-user-email', 'Formato de correo electrónico inválido');
            return false;
        }
    }

    validatePhone(phone) {
        if (!phone) {
            this.showFieldError('hr-user-phone', 'El teléfono es obligatorio');
            return false;
        }

        if (!/^\d{10}$/.test(phone)) {
            this.showFieldError('hr-user-phone', 'El teléfono debe tener exactamente 10 dígitos');
            return false;
        }

        this.clearFieldError('hr-user-phone');
        return true;
    }

    validateAddress(address) {
        if (!address) {
            this.showFieldError('hr-user-address', 'La dirección es obligatoria');
            return false;
        }

        if (address.length > 30) {
            this.showFieldError('hr-user-address', 'La dirección no puede tener más de 30 caracteres');
            return false;
        }

        this.clearFieldError('hr-user-address');
        return true;
    }

    validateRole(role) {
        const validRoles = ['DOCTOR', 'NURSE', 'ADMINISTRATIVE_STAFF', 'SUPPORT_STAFF', 'HUMAN_RESOURCES'];

        if (!role) {
            this.showFieldError('hr-user-role', 'El rol es obligatorio');
            return false;
        }

        if (!validRoles.includes(role)) {
            this.showFieldError('hr-user-role', 'Rol seleccionado no válido');
            return false;
        }

        this.clearFieldError('hr-user-role');
        return true;
    }

    validateBirthDate(birthDate) {
        if (!birthDate) {
            this.showFieldError('hr-user-birthdate', 'La fecha de nacimiento es obligatoria');
            return false;
        }

        const birth = new Date(birthDate);
        const today = new Date();
        const age = today.getFullYear() - birth.getFullYear();

        if (age > 150) {
            this.showFieldError('hr-user-birthdate', 'La edad no puede ser mayor a 150 años');
            return false;
        }

        if (birth > today) {
            this.showFieldError('hr-user-birthdate', 'La fecha de nacimiento no puede ser futura');
            return false;
        }

        this.clearFieldError('hr-user-birthdate');
        return true;
    }

    validatePasswordField(password) {
        if (!password) {
            this.showFieldError('hr-user-password', 'La contraseña es obligatoria');
            return false;
        }

        if (password.length < 8) {
            this.showFieldError('hr-user-password', 'La contraseña debe tener al menos 8 caracteres');
            return false;
        }

        if (!/(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&])/.test(password)) {
            this.showFieldError('hr-user-password', 'Debe incluir: 1 mayúscula, 1 número, 1 carácter especial');
            return false;
        }

        this.clearFieldError('hr-user-password');
        return true;
    }

    /**
     * Muestra error en un campo específico
     */
    showFieldError(fieldId, message) {
        const field = document.getElementById(fieldId);
        if (field) {
            field.classList.add('field-error');

            // Crear o actualizar mensaje de error
            let errorElement = field.parentNode.querySelector('.field-error-message');
            if (!errorElement) {
                errorElement = document.createElement('div');
                errorElement.className = 'field-error-message';
                field.parentNode.appendChild(errorElement);
            }

            errorElement.textContent = message;
        }
    }

    /**
     * Limpia error de un campo específico
     */
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

    /**
     * Muestra alerta en el formulario
     */
    showFormAlert(message, type = 'error') {
        const alertElement = document.getElementById('hr-form-alert');
        if (alertElement) {
            alertElement.textContent = message;
            alertElement.className = `form-alert alert-${type}`;
            alertElement.style.display = 'block';
        }
    }

    /**
     * Oculta alerta del formulario
     */
    hideFormAlert() {
        const alertElement = document.getElementById('hr-form-alert');
        if (alertElement) {
            alertElement.style.display = 'none';
        }
    }

    /**
     * Obtiene los datos del formulario
     */
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

    /**
     * Cambia el estado de un usuario
     */
    async toggleUserStatus(cedula) {
        try {
            const user = this.users.find(u => u.cedula === cedula);
            if (!user) {
                throw new Error('Empleado no encontrado');
            }

            const action = user.active ? 'desactivar' : 'activar';
            const confirmMessage = `¿Está seguro de que desea ${action} a este empleado?`;

            if (!confirm(confirmMessage)) {
                return;
            }

            window.showInfo(`Cambiando estado del empleado...`);

            let result;
            if (user.active) {
                result = await window.userApi.deactivateUser(cedula);
            } else {
                result = await window.userApi.activateUser(cedula);
            }

            if (result) {
                window.showSuccess(`Empleado ${action}do exitosamente`);
                await this.loadUsersData();
                this.renderHRPage();
            }

        } catch (error) {
            console.error('Error cambiando estado:', error);
            window.showError('Error al cambiar estado del empleado: ' + error.message);
        }
    }

    /**
     * Elimina un usuario
     */
    async deleteUser(cedula) {
        if (!confirm('¿Está seguro de que desea eliminar este empleado? Esta acción registra el eliminadoPor y eliminadoEn, y puede requerir hard delete si no hay dependencias.')) {
            return;
        }

        try {
            window.showInfo('Eliminando empleado...');

            await window.userApi.deleteUserByCedula(cedula);

            window.showSuccess('Empleado eliminado exitosamente');
            await this.loadUsersData();
            this.renderHRPage();

        } catch (error) {
            console.error('Error eliminando empleado:', error);
            window.showError('Error al eliminar empleado: ' + error.message);
        }
    }

    /**
     * Cambia de página
     */
    goToPage(page) {
        this.currentPage = page;
        this.refreshTable();
    }

    /**
     * Obtiene el conteo de usuarios activos
     */
    getActiveUsersCount() {
        return this.users.filter(user => user.active).length;
    }

    /**
     * Obtiene el conteo de usuarios por rol
     */
    getRoleCount(role) {
        return this.users.filter(user => user.role === role).length;
    }

    /**
     * Obtiene el nombre para mostrar del rol
     */
    getRoleDisplayName(role) {
        const roleNames = {
            'HUMAN_RESOURCES': 'Recursos Humanos',
            'ADMINISTRATIVE_STAFF': 'Personal Administrativo',
            'DOCTOR': 'Médico',
            'NURSE': 'Enfermera',
            'SUPPORT_STAFF': 'Soporte de Información'
        };

        return roleNames[role] || role;
    }

    /**
     * Obtiene la clase CSS para el badge del rol
     */
    getRoleBadgeClass(role) {
        const roleClasses = {
            'HUMAN_RESOURCES': 'hr',
            'ADMINISTRATIVE_STAFF': 'admin',
            'DOCTOR': 'doctor',
            'NURSE': 'nurse',
            'SUPPORT_STAFF': 'support'
        };

        return roleClasses[role] || 'default';
    }

    /**
     * Obtiene el ícono para el rol
     */
    getRoleIcon(role) {
        const roleIcons = {
            'HUMAN_RESOURCES': 'fa-user-tie',
            'ADMINISTRATIVE_STAFF': 'fa-user-cog',
            'DOCTOR': 'fa-user-md',
            'NURSE': 'fa-user-nurse',
            'SUPPORT_STAFF': 'fa-headset'
        };

        return roleIcons[role] || 'fa-user';
    }

    /**
     * Formatea fecha
     */
    formatDate(dateString) {
        if (!dateString) return 'N/A';

        try {
            const date = new Date(dateString);
            return date.toLocaleDateString('es-CO', {
                year: 'numeric',
                month: 'short',
                day: 'numeric'
            });
        } catch (error) {
            return 'N/A';
        }
    }

    /**
     * Muestra estado de error
     */
    showErrorState() {
        const contentArea = document.getElementById('content-area');
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

// Crear instancia global del servicio de Recursos Humanos
window.hrManagementService = new HRManagementService();