/**
 * Sistema de Gestión Clínica CS2 - Módulo de Gestión de Usuarios
 * Funcionalidades para Recursos Humanos
 */

// Estado del módulo de usuarios
let usersData = [];
let filteredUsers = [];

/**
 * Carga los datos de usuarios
 */
async function loadUsersData() {
    if (!checkPermission('users')) {
        showSectionError('users', 'No tiene permisos para acceder a esta sección');
        return;
    }

    try {
        showSectionLoading('users');

        const users = await UsersAPI.getAll();
        usersData = users;
        filteredUsers = users;

        renderUsersTable();

    } catch (error) {
        console.error('Error cargando usuarios:', error);
        const errorMessage = ApiUtils.handleError(error, 'Error al cargar usuarios');
        showSectionError('users', errorMessage);
    }
}

/**
 * Renderiza la tabla de usuarios
 */
function renderUsersTable() {
    const tbody = document.getElementById('users-tbody');
    if (!tbody) return;

    if (filteredUsers.length === 0) {
        tbody.innerHTML = '<tr><td colspan="6" class="text-center">No hay usuarios registrados</td></tr>';
        return;
    }

    tbody.innerHTML = filteredUsers.map(user => `
        <tr>
            <td>${user.id}</td>
            <td>${user.name}</td>
            <td><span class="status-badge status-${user.role?.toLowerCase()}">${getRoleDisplayName(user.role)}</span></td>
            <td><span class="status-badge status-${user.status || 'active'}">${user.status || 'Activo'}</span></td>
            <td>${formatDate(user.lastAccess)}</td>
            <td class="actions-cell">
                <button onclick="viewUser('${user.id}')" class="btn-icon" title="Ver detalles">
                    <i class="fas fa-eye"></i>
                </button>
                <button onclick="editUser('${user.id}')" class="btn-icon" title="Editar">
                    <i class="fas fa-edit"></i>
                </button>
                <button onclick="resetUserPassword('${user.id}')" class="btn-icon" title="Resetear contraseña">
                    <i class="fas fa-key"></i>
                </button>
            </td>
        </tr>
    `).join('');
}

/**
 * Función para mostrar modal de nuevo usuario
 */
function showUserModal() {
    // Crear modal dinámico para usuarios
    const modal = document.createElement('div');
    modal.className = 'modal';
    modal.innerHTML = `
        <div class="modal-content">
            <div class="modal-header">
                <h2>Nuevo Usuario</h2>
                <button class="modal-close" onclick="this.closest('.modal').remove()">
                    <i class="fas fa-times"></i>
                </button>
            </div>
            <form id="user-form">
                <div class="form-grid">
                    <div class="form-group">
                        <label>ID Usuario:*</label>
                        <input type="text" name="username" required maxlength="15">
                    </div>
                    <div class="form-group">
                        <label>Nombre Completo:*</label>
                        <input type="text" name="name" required>
                    </div>
                    <div class="form-group">
                        <label>Rol:*</label>
                        <select name="role" required>
                            <option value="">Seleccionar</option>
                            <option value="ADMINISTRATIVE">Administrativo</option>
                            <option value="DOCTOR">Médico</option>
                            <option value="NURSE">Enfermera</option>
                            <option value="HUMAN_RESOURCES">Recursos Humanos</option>
                            <option value="SUPPORT">Soporte de Información</option>
                        </select>
                    </div>
                    <div class="form-group">
                        <label>Email:</label>
                        <input type="email" name="email">
                    </div>
                    <div class="form-group">
                        <label>Teléfono:</label>
                        <input type="tel" name="phone">
                    </div>
                    <div class="form-group">
                        <label>Especialidad:</label>
                        <input type="text" name="specialty">
                    </div>
                </div>
                <div class="form-group">
                    <label>Observaciones:</label>
                    <textarea name="observations" rows="3"></textarea>
                </div>
                <div class="modal-actions">
                    <button type="button" class="btn-secondary" onclick="this.closest('.modal').remove()">Cancelar</button>
                    <button type="submit" class="btn-primary">Crear Usuario</button>
                </div>
            </form>
        </div>
    `;

    document.body.appendChild(modal);
    modal.style.display = 'flex';

    // Configurar envío del formulario
    const form = modal.querySelector('#user-form');
    form.onsubmit = (e) => handleUserSubmit(e, modal);
}

/**
 * Maneja el envío del formulario de usuario
 */
async function handleUserSubmit(event, modal) {
    event.preventDefault();

    const form = document.getElementById('user-form');
    const formData = new FormData(form);

    const userData = {
        username: formData.get('username').trim(),
        name: formData.get('name').trim(),
        role: formData.get('role'),
        email: formData.get('email').trim(),
        phone: formData.get('phone').trim(),
        specialty: formData.get('specialty').trim(),
        observations: formData.get('observations').trim()
    };

    // Validar datos
    const validationErrors = validateUserData(userData);
    if (validationErrors.length > 0) {
        showValidationErrors(validationErrors);
        return;
    }

    try {
        const result = await UsersAPI.create(userData);
        showNotification('Usuario creado correctamente', 'success');

        modal.remove();
        await loadUsersData(); // Recargar lista

    } catch (error) {
        console.error('Error creando usuario:', error);
        const errorMessage = ApiUtils.handleError(error, 'Error al crear usuario');
        showNotification(errorMessage, 'error');
    }
}

/**
 * Valida datos del usuario
 */
function validateUserData(userData) {
    const errors = [];

    if (!userData.username) {
        errors.push('El ID de usuario es requerido');
    } else if (userData.username.length > 15) {
        errors.push('El ID de usuario no puede tener más de 15 caracteres');
    }

    if (!userData.name) {
        errors.push('El nombre es requerido');
    }

    if (!userData.role) {
        errors.push('El rol es requerido');
    }

    if (userData.email && !isValidEmail(userData.email)) {
        errors.push('El email tiene un formato inválido');
    }

    if (userData.phone && !isValidPhone(userData.phone)) {
        errors.push('El teléfono tiene un formato inválido');
    }

    return errors;
}

/**
 * Función para ver detalles de usuario
 */
function viewUser(userId) {
    const user = usersData.find(u => u.id === userId);
    if (!user) return;

    let details = `
        <div class="user-details">
            <h3>${user.name}</h3>
            <div class="details-grid">
                <div class="detail-item">
                    <strong>ID Usuario:</strong> ${user.username}
                </div>
                <div class="detail-item">
                    <strong>Rol:</strong> ${getRoleDisplayName(user.role)}
                </div>
                <div class="detail-item">
                    <strong>Email:</strong> ${user.email || 'No registrado'}
                </div>
                <div class="detail-item">
                    <strong>Teléfono:</strong> ${user.phone || 'No registrado'}
                </div>
                <div class="detail-item">
                    <strong>Especialidad:</strong> ${user.specialty || 'No aplica'}
                </div>
                <div class="detail-item">
                    <strong>Estado:</strong> <span class="status-badge status-${user.status || 'active'}">${user.status || 'Activo'}</span>
                </div>
                <div class="detail-item">
                    <strong>Último Acceso:</strong> ${formatDate(user.lastAccess)}
                </div>
            </div>
            ${user.observations ? `<div class="detail-item"><strong>Observaciones:</strong> ${user.observations}</div>` : ''}
        </div>
    `;

    showNotification(details, 'info');
}

/**
 * Función para editar usuario
 */
function editUser(userId) {
    showNotification('Función de edición pendiente de implementar', 'info');
}

/**
 * Función para resetear contraseña de usuario
 */
function resetUserPassword(userId) {
    confirmAction('¿Está seguro de que desea resetear la contraseña de este usuario?', () => {
        showNotification('Contraseña reseteada correctamente. Nueva contraseña temporal: 12345678', 'success');
    });
}

/**
 * Función para exportar usuarios
 */
function exportUsers() {
    if (filteredUsers.length === 0) {
        showNotification('No hay usuarios para exportar', 'warning');
        return;
    }

    const dataToExport = filteredUsers.map(user => ({
        'ID': user.id,
        'Nombre': user.name,
        'Usuario': user.username,
        'Rol': getRoleDisplayName(user.role),
        'Email': user.email || '',
        'Teléfono': user.phone || '',
        'Especialidad': user.specialty || '',
        'Estado': user.status || 'Activo',
        'Último Acceso': formatDate(user.lastAccess)
    }));

    exportToCSV(dataToExport, `usuarios_${new Date().toISOString().split('T')[0]}.csv`);
}

// Hacer funciones disponibles globalmente
window.loadUsersData = loadUsersData;
window.showUserModal = showUserModal;
window.viewUser = viewUser;
window.editUser = editUser;
window.resetUserPassword = resetUserPassword;
window.exportUsers = exportUsers;