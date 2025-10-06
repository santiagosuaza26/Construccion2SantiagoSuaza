/**
 * Servicio de gestión de usuarios para Recursos Humanos
 * Maneja la creación, edición, eliminación y visualización de usuarios
 */
class UserManagementService {
    constructor() {
        this.currentUser = null;
        this.users = [];
        this.filteredUsers = [];
        this.currentPage = 1;
        this.itemsPerPage = 10;
        this.searchTerm = '';
    }

    /**
     * Carga la lista de usuarios
     */
    async loadUsersList() {
        try {
            window.showInfo('Cargando usuarios...');

            const response = await window.userApi.findAllUsers();

            if (response && Array.isArray(response)) {
                this.users = response;
                this.filteredUsers = [...this.users];
                this.renderUsersList();
                window.showSuccess(`Se cargaron ${this.users.length} usuarios`);
            } else {
                throw new Error('Formato de respuesta inválido');
            }

        } catch (error) {
            console.error('Error loading users:', error);
            window.showError('Error al cargar usuarios: ' + error.message);
            this.showEmptyState();
        }
    }

    /**
     * Renderiza la lista de usuarios
     */
    renderUsersList() {
        const contentArea = document.getElementById('content-area');
        if (!contentArea) return;

        const startIndex = (this.currentPage - 1) * this.itemsPerPage;
        const endIndex = startIndex + this.itemsPerPage;
        const usersToShow = this.filteredUsers.slice(startIndex, endIndex);

        contentArea.innerHTML = `
            <div class="content-header">
                <h2><i class="fas fa-users"></i> Gestión de Usuarios</h2>
                <p>Administra los usuarios del sistema y sus permisos</p>
            </div>

            <div class="content-actions">
                <div class="search-box">
                    <i class="fas fa-search search-icon"></i>
                    <input type="text" id="user-search" class="search-input"
                           placeholder="Buscar por nombre, cédula o usuario..."
                           value="${this.searchTerm}">
                </div>
                <button class="btn btn-primary" onclick="userManagementService.showCreateUserModal()">
                    <i class="fas fa-plus"></i> Nuevo Usuario
                </button>
            </div>

            <div class="stats-grid">
                <div class="stat-card">
                    <div class="stat-value">${this.users.length}</div>
                    <div class="stat-label">Total Usuarios</div>
                </div>
                <div class="stat-card">
                    <div class="stat-value">${this.getActiveUsersCount()}</div>
                    <div class="stat-label">Usuarios Activos</div>
                </div>
                <div class="stat-card">
                    <div class="stat-value">${this.getRoleCount('HUMAN_RESOURCES')}</div>
                    <div class="stat-label">RR.HH.</div>
                </div>
                <div class="stat-card">
                    <div class="stat-value">${this.getRoleCount('ADMINISTRATIVE_STAFF')}</div>
                    <div class="stat-label">Administrativos</div>
                </div>
            </div>

            <div class="table-container">
                <table class="table">
                    <thead>
                        <tr>
                            <th>Cédula</th>
                            <th>Nombre</th>
                            <th>Usuario</th>
                            <th>Rol</th>
                            <th>Estado</th>
                            <th>Acciones</th>
                        </tr>
                    </thead>
                    <tbody id="users-table-body">
                        ${this.renderUsersTableRows(usersToShow)}
                    </tbody>
                </table>
            </div>

            ${this.renderPagination()}

            <!-- Modal para crear/editar usuario -->
            <div id="user-modal" class="modal-overlay" style="display: none;">
                <div class="modal">
                    <div class="modal-header">
                        <h3 id="user-modal-title">Crear Usuario</h3>
                        <button class="modal-close" onclick="userManagementService.closeUserModal()">
                            <i class="fas fa-times"></i>
                        </button>
                    </div>
                    <div class="modal-body">
                        <form id="user-form">
                            ${this.renderUserForm()}
                        </form>
                    </div>
                    <div class="modal-footer">
                        <button class="btn btn-secondary" onclick="userManagementService.closeUserModal()">
                            Cancelar
                        </button>
                        <button class="btn btn-primary" onclick="userManagementService.saveUser()">
                            Guardar
                        </button>
                    </div>
                </div>
            </div>
        `;

        // Configurar event listeners
        this.setupEventListeners();
    }

    /**
     * Renderiza las filas de la tabla de usuarios
     */
    renderUsersTableRows(users) {
        if (users.length === 0) {
            return `
                <tr>
                    <td colspan="6" class="text-center" style="padding: 2rem;">
                        <i class="fas fa-users" style="font-size: 3rem; color: var(--text-secondary); margin-bottom: 1rem;"></i>
                        <p style="color: var(--text-secondary);">No se encontraron usuarios</p>
                    </td>
                </tr>
            `;
        }

        return users.map(user => `
            <tr class="${user.active ? 'table-row-active' : 'table-row-inactive'}">
                <td>${window.formatCedula(user.cedula)}</td>
                <td>${user.fullName}</td>
                <td>${user.username}</td>
                <td>
                    <span class="badge badge-${this.getRoleBadgeClass(user.role)}">
                        ${this.getRoleDisplayName(user.role)}
                    </span>
                </td>
                <td>
                    <span class="badge ${user.active ? 'badge-success' : 'badge-error'}">
                        ${user.active ? 'Activo' : 'Inactivo'}
                    </span>
                </td>
                <td>
                    <div class="btn-group">
                        <button class="btn btn-sm btn-primary"
                                onclick="userManagementService.editUser('${user.cedula}')"
                                title="Editar">
                            <i class="fas fa-edit"></i>
                        </button>
                        <button class="btn btn-sm ${user.active ? 'btn-warning' : 'btn-success'}"
                                onclick="userManagementService.toggleUserStatus('${user.cedula}')"
                                title="${user.active ? 'Desactivar' : 'Activar'}">
                            <i class="fas ${user.active ? 'fa-ban' : 'fa-check'}"></i>
                        </button>
                        <button class="btn btn-sm btn-danger"
                                onclick="userManagementService.deleteUser('${user.cedula}')"
                                title="Eliminar">
                            <i class="fas fa-trash"></i>
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
            <div class="pagination">
        `;

        // Botón anterior
        if (this.currentPage > 1) {
            paginationHtml += `<button class="pagination-btn" onclick="userManagementService.goToPage(${this.currentPage - 1})">
                <i class="fas fa-chevron-left"></i>
            </button>`;
        }

        // Páginas
        for (let i = 1; i <= totalPages; i++) {
            if (i === this.currentPage) {
                paginationHtml += `<button class="pagination-btn active">${i}</button>`;
            } else if (i >= this.currentPage - 2 && i <= this.currentPage + 2) {
                paginationHtml += `<button class="pagination-btn" onclick="userManagementService.goToPage(${i})">${i}</button>`;
            }
        }

        // Botón siguiente
        if (this.currentPage < totalPages) {
            paginationHtml += `<button class="pagination-btn" onclick="userManagementService.goToPage(${this.currentPage + 1})">
                <i class="fas fa-chevron-right"></i>
            </button>`;
        }

        paginationHtml += '</div>';
        return paginationHtml;
    }

    /**
     * Renderiza el formulario de usuario
     */
    renderUserForm() {
        return `
            <div class="form-row">
                <div class="form-group">
                    <label for="user-cedula">Cédula *</label>
                    <input type="text" id="user-cedula" name="cedula" required maxlength="20">
                </div>
                <div class="form-group">
                    <label for="user-username">Nombre de Usuario *</label>
                    <input type="text" id="user-username" name="username" required maxlength="15">
                </div>
            </div>

            <div class="form-row">
                <div class="form-group">
                    <label for="user-fullname">Nombre Completo *</label>
                    <input type="text" id="user-fullname" name="fullName" required maxlength="100">
                </div>
                <div class="form-group">
                    <label for="user-email">Correo Electrónico *</label>
                    <input type="email" id="user-email" name="email" required>
                </div>
            </div>

            <div class="form-row">
                <div class="form-group">
                    <label for="user-phone">Teléfono *</label>
                    <input type="tel" id="user-phone" name="phoneNumber" required maxlength="10">
                </div>
                <div class="form-group">
                    <label for="user-birthdate">Fecha de Nacimiento *</label>
                    <input type="date" id="user-birthdate" name="birthDate" required>
                </div>
            </div>

            <div class="form-row">
                <div class="form-group">
                    <label for="user-address">Dirección *</label>
                    <input type="text" id="user-address" name="address" required maxlength="30">
                </div>
                <div class="form-group">
                    <label for="user-role">Rol *</label>
                    <select id="user-role" name="role" required>
                        <option value="">Seleccionar rol</option>
                        <option value="HUMAN_RESOURCES">Recursos Humanos</option>
                        <option value="ADMINISTRATIVE_STAFF">Personal Administrativo</option>
                        <option value="DOCTOR">Médico</option>
                        <option value="NURSE">Enfermera</option>
                        <option value="SUPPORT_STAFF">Soporte de Información</option>
                    </select>
                </div>
            </div>

            <div class="form-row">
                <div class="form-group">
                    <label for="user-password">Contraseña *</label>
                    <input type="password" id="user-password" name="password" required minlength="8">
                </div>
                <div class="form-group">
                    <label for="user-confirm-password">Confirmar Contraseña *</label>
                    <input type="password" id="user-confirm-password" name="confirmPassword" required minlength="8">
                </div>
            </div>
        `;
    }

    /**
     * Configura los event listeners
     */
    setupEventListeners() {
        // Búsqueda
        const searchInput = document.getElementById('user-search');
        if (searchInput) {
            searchInput.addEventListener('input', window.debounce((e) => {
                this.searchTerm = e.target.value;
                this.filterUsers();
            }, 300));
        }

        // Validación del formulario
        const userForm = document.getElementById('user-form');
        if (userForm) {
            userForm.addEventListener('submit', (e) => {
                e.preventDefault();
                this.saveUser();
            });
        }
    }

    /**
     * Filtra usuarios según término de búsqueda
     */
    filterUsers() {
        if (!this.searchTerm) {
            this.filteredUsers = [...this.users];
        } else {
            const term = this.searchTerm.toLowerCase();
            this.filteredUsers = this.users.filter(user =>
                user.fullName.toLowerCase().includes(term) ||
                user.cedula.includes(term) ||
                user.username.toLowerCase().includes(term) ||
                this.getRoleDisplayName(user.role).toLowerCase().includes(term)
            );
        }

        this.currentPage = 1;
        this.renderUsersList();
    }

    /**
     * Muestra el modal para crear usuario
     */
    showCreateUserModal() {
        this.currentUser = null;
        this.showUserModal();
    }

    /**
     * Muestra el modal para editar usuario
     */
    async editUser(cedula) {
        try {
            window.showInfo('Cargando usuario...');

            const user = await window.userApi.findUserByCedula(cedula);

            if (user) {
                this.currentUser = user;
                this.showUserModal();
                this.populateUserForm(user);
                window.showSuccess('Usuario cargado para edición');
            } else {
                throw new Error('Usuario no encontrado');
            }

        } catch (error) {
            console.error('Error loading user:', error);
            window.showError('Error al cargar usuario: ' + error.message);
        }
    }

    /**
     * Muestra el modal de usuario
     */
    showUserModal() {
        const modal = document.getElementById('user-modal');
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
        const modal = document.getElementById('user-modal');
        if (modal) {
            modal.classList.remove('show');
            setTimeout(() => {
                modal.style.display = 'none';
            }, 300);
        }
        this.currentUser = null;
    }

    /**
     * Llena el formulario con datos del usuario
     */
    populateUserForm(user) {
        document.getElementById('user-modal-title').textContent = 'Editar Usuario';

        document.getElementById('user-cedula').value = user.cedula;
        document.getElementById('user-username').value = user.username;
        document.getElementById('user-fullname').value = user.fullName;
        document.getElementById('user-email').value = user.email;
        document.getElementById('user-phone').value = user.phoneNumber;
        document.getElementById('user-address').value = user.address;
        document.getElementById('user-role').value = user.role;

        // Formatear fecha de nacimiento
        if (user.birthDate) {
            const birthDate = new Date(user.birthDate);
            document.getElementById('user-birthdate').value = birthDate.toISOString().split('T')[0];
        }

        // Limpiar campos de contraseña para edición
        document.getElementById('user-password').value = '';
        document.getElementById('user-confirm-password').value = '';
        document.getElementById('user-password').required = false;
        document.getElementById('user-confirm-password').required = false;
    }

    /**
     * Guarda el usuario (crear o actualizar)
     */
    async saveUser() {
        const form = document.getElementById('user-form');
        if (!form) return;

        // Validar formulario
        if (!this.validateUserForm()) {
            return;
        }

        const userData = this.getFormData();

        try {
            window.showInfo(this.currentUser ? 'Actualizando usuario...' : 'Creando usuario...');

            let result;
            if (this.currentUser) {
                result = await window.userApi.updateUser(this.currentUser.cedula, userData);
            } else {
                result = await window.userApi.createUser(userData);
            }

            if (result) {
                window.showSuccess(`Usuario ${this.currentUser ? 'actualizado' : 'creado'} exitosamente`);
                this.closeUserModal();
                this.loadUsersList(); // Recargar lista
            }

        } catch (error) {
            console.error('Error saving user:', error);
            window.showError('Error al guardar usuario: ' + error.message);
        }
    }

    /**
     * Valida el formulario de usuario
     */
    validateUserForm() {
        const cedula = document.getElementById('user-cedula').value.trim();
        const username = document.getElementById('user-username').value.trim();
        const fullName = document.getElementById('user-fullname').value.trim();
        const email = document.getElementById('user-email').value.trim();
        const phone = document.getElementById('user-phone').value.trim();
        const address = document.getElementById('user-address').value.trim();
        const role = document.getElementById('user-role').value;
        const password = document.getElementById('user-password').value;
        const confirmPassword = document.getElementById('user-confirm-password').value;

        // Validaciones básicas
        if (!cedula || !window.isValidCedula(cedula)) {
            window.showError('Cédula inválida');
            return false;
        }

        if (!username || !/^[a-zA-Z0-9]+$/.test(username)) {
            window.showError('Nombre de usuario debe contener solo letras y números');
            return false;
        }

        if (!fullName) {
            window.showError('Nombre completo es requerido');
            return false;
        }

        if (!window.isValidEmail(email)) {
            window.showError('Correo electrónico inválido');
            return false;
        }

        if (!window.isValidPhone(phone)) {
            window.showError('Teléfono debe tener 10 dígitos');
            return false;
        }

        if (!address) {
            window.showError('Dirección es requerida');
            return false;
        }

        if (!role) {
            window.showError('Rol es requerido');
            return false;
        }

        // Validar contraseña solo si es creación o si se proporciona
        if (!this.currentUser || password) {
            if (password.length < 8) {
                window.showError('Contraseña debe tener al menos 8 caracteres');
                return false;
            }

            if (!/(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&])/.test(password)) {
                window.showError('Contraseña debe incluir mayúscula, minúscula, número y carácter especial');
                return false;
            }

            if (password !== confirmPassword) {
                window.showError('Las contraseñas no coinciden');
                return false;
            }
        }

        return true;
    }

    /**
     * Obtiene los datos del formulario
     */
    getFormData() {
        const data = {
            cedula: document.getElementById('user-cedula').value.trim(),
            username: document.getElementById('user-username').value.trim(),
            fullName: document.getElementById('user-fullname').value.trim(),
            email: document.getElementById('user-email').value.trim(),
            phoneNumber: document.getElementById('user-phone').value.trim(),
            address: document.getElementById('user-address').value.trim(),
            role: document.getElementById('user-role').value,
            birthDate: document.getElementById('user-birthdate').value
        };

        // Incluir contraseña solo si se proporciona
        const password = document.getElementById('user-password').value;
        if (password) {
            data.password = password;
        }

        return data;
    }

    /**
     * Cambia el estado de un usuario (activar/desactivar)
     */
    async toggleUserStatus(cedula) {
        try {
            const user = this.users.find(u => u.cedula === cedula);
            if (!user) {
                throw new Error('Usuario no encontrado');
            }

            let result;
            if (user.active) {
                result = await window.userApi.deactivateUser(cedula);
                window.showInfo('Usuario desactivado');
            } else {
                result = await window.userApi.activateUser(cedula);
                window.showInfo('Usuario activado');
            }

            if (result) {
                this.loadUsersList(); // Recargar lista
            }

        } catch (error) {
            console.error('Error toggling user status:', error);
            window.showError('Error al cambiar estado del usuario: ' + error.message);
        }
    }

    /**
     * Elimina un usuario
     */
    async deleteUser(cedula) {
        if (!confirm('¿Está seguro de que desea eliminar este usuario? Esta acción no se puede deshacer.')) {
            return;
        }

        try {
            window.showInfo('Eliminando usuario...');

            await window.userApi.deleteUserByCedula(cedula);

            window.showSuccess('Usuario eliminado exitosamente');
            this.loadUsersList(); // Recargar lista

        } catch (error) {
            console.error('Error deleting user:', error);
            window.showError('Error al eliminar usuario: ' + error.message);
        }
    }

    /**
     * Cambia de página
     */
    goToPage(page) {
        this.currentPage = page;
        this.renderUsersList();
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
            'HUMAN_RESOURCES': 'primary',
            'ADMINISTRATIVE_STAFF': 'info',
            'DOCTOR': 'success',
            'NURSE': 'warning',
            'SUPPORT_STAFF': 'secondary'
        };

        return roleClasses[role] || 'secondary';
    }

    /**
     * Muestra estado vacío
     */
    showEmptyState() {
        const contentArea = document.getElementById('content-area');
        if (contentArea) {
            contentArea.innerHTML = `
                <div class="empty-state">
                    <i class="fas fa-users"></i>
                    <h3>No se pudieron cargar los usuarios</h3>
                    <p>Verifica tu conexión a internet e intenta nuevamente.</p>
                    <button class="btn btn-primary" onclick="userManagementService.loadUsersList()">
                        <i class="fas fa-refresh"></i> Reintentar
                    </button>
                </div>
            `;
        }
    }
}

// Crear instancia global del servicio de gestión de usuarios
window.userManagementService = new UserManagementService();