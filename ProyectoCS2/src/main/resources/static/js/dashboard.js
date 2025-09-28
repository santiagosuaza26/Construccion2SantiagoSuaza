// JavaScript para el dashboard según roles

// Configuración del dashboard
const DASHBOARD_CONFIG = {
    ROLES: {
        ADMINISTRATIVE: {
            name: 'Administrativo',
            icon: 'fas fa-user-tie',
            color: '#2563eb',
            permissions: [
                'USER_CREATE', 'USER_READ', 'USER_UPDATE', 'USER_DELETE',
                'PATIENT_CREATE', 'PATIENT_READ', 'PATIENT_UPDATE',
                'INVOICE_CREATE', 'INVOICE_READ', 'INVOICE_PRINT'
            ],
            menu: [
                { id: 'dashboard', name: 'Dashboard', icon: 'fas fa-tachometer-alt', section: 'dashboard' },
                { id: 'users', name: 'Usuarios', icon: 'fas fa-users', section: 'users' },
                { id: 'patients', name: 'Pacientes', icon: 'fas fa-user-md', section: 'patients' },
                { id: 'invoices', name: 'Facturación', icon: 'fas fa-file-invoice-dollar', section: 'invoices' },
                { id: 'reports', name: 'Reportes', icon: 'fas fa-chart-bar', section: 'reports' }
            ]
        },
        DOCTOR: {
            name: 'Médico',
            icon: 'fas fa-stethoscope',
            color: '#059669',
            permissions: [
                'PATIENT_READ', 'CLINICAL_HISTORY_CREATE', 'CLINICAL_HISTORY_READ',
                'MEDICAL_ORDER_CREATE', 'MEDICATION_PRESCRIBE', 'PROCEDURE_ORDER'
            ],
            menu: [
                { id: 'dashboard', name: 'Dashboard', icon: 'fas fa-tachometer-alt', section: 'dashboard' },
                { id: 'patients', name: 'Mis Pacientes', icon: 'fas fa-user-md', section: 'patients' },
                { id: 'clinical-history', name: 'Historias Clínicas', icon: 'fas fa-file-medical', section: 'clinical-history' },
                { id: 'medical-orders', name: 'Órdenes Médicas', icon: 'fas fa-prescription', section: 'medical-orders' }
            ]
        },
        NURSE: {
            name: 'Enfermera',
            icon: 'fas fa-user-nurse',
            color: '#dc2626',
            permissions: [
                'PATIENT_READ', 'VITAL_SIGNS_CREATE', 'VITAL_SIGNS_READ',
                'PATIENT_VISIT_CREATE', 'CLINICAL_HISTORY_READ'
            ],
            menu: [
                { id: 'dashboard', name: 'Dashboard', icon: 'fas fa-tachometer-alt', section: 'dashboard' },
                { id: 'patients', name: 'Pacientes', icon: 'fas fa-user-md', section: 'patients' },
                { id: 'vital-signs', name: 'Signos Vitales', icon: 'fas fa-heartbeat', section: 'vital-signs' },
                { id: 'visits', name: 'Visitas', icon: 'fas fa-calendar-check', section: 'visits' }
            ]
        },
        HUMAN_RESOURCES: {
            name: 'Recursos Humanos',
            icon: 'fas fa-users-cog',
            color: '#7c3aed',
            permissions: [
                'USER_CREATE', 'USER_READ', 'USER_UPDATE', 'USER_DELETE'
            ],
            menu: [
                { id: 'dashboard', name: 'Dashboard', icon: 'fas fa-tachometer-alt', section: 'dashboard' },
                { id: 'employees', name: 'Empleados', icon: 'fas fa-users', section: 'employees' },
                { id: 'payroll', name: 'Nómina', icon: 'fas fa-money-bill-wave', section: 'payroll' }
            ]
        },
        SUPPORT: {
            name: 'Soporte',
            icon: 'fas fa-tools',
            color: '#ea580c',
            permissions: [
                'MEDICATION_CREATE', 'MEDICATION_READ', 'PROCEDURE_CREATE',
                'INVENTORY_MANAGE', 'SPECIALTY_CREATE'
            ],
            menu: [
                { id: 'dashboard', name: 'Dashboard', icon: 'fas fa-tachometer-alt', section: 'dashboard' },
                { id: 'inventory', name: 'Inventario', icon: 'fas fa-boxes', section: 'inventory' },
                { id: 'medications', name: 'Medicamentos', icon: 'fas fa-pills', section: 'medications' },
                { id: 'maintenance', name: 'Mantenimiento', icon: 'fas fa-wrench', section: 'maintenance' }
            ]
        }
    }
};

// Variables globales del dashboard
let currentRole = null;
let currentUser = null;

/**
 * Inicializa el dashboard
 */
async function initializeDashboard() {
    try {
        // Cargar información del usuario
        currentUser = await loadCurrentUser();
        if (!currentUser) {
            window.location.href = 'login.html';
            return;
        }

        currentRole = currentUser.role;
        console.log('Dashboard initialized for role:', currentRole);

        // Configurar la interfaz según el rol
        setupRoleInterface();

        // Cargar datos iniciales
        await loadDashboardData();

    } catch (error) {
        console.error('Error initializing dashboard:', error);
        showError('Error al cargar el dashboard');
    }
}

/**
 * Configura la interfaz según el rol del usuario
 */
function setupRoleInterface() {
    const roleConfig = DASHBOARD_CONFIG.ROLES[currentRole];

    if (!roleConfig) {
        showError('Rol de usuario no reconocido');
        return;
    }

    // Actualizar información del usuario en el sidebar
    document.getElementById('userName').textContent = currentUser.fullName;
    document.getElementById('userRole').textContent = roleConfig.name;

    // Actualizar color del avatar
    const avatar = document.querySelector('.user-avatar');
    if (avatar) {
        avatar.style.backgroundColor = roleConfig.color;
    }

    // Cargar menú según el rol
    loadRoleMenu(roleConfig.menu);

    // Configurar dashboard inicial
    showSection('dashboard');
}

/**
 * Carga el menú según el rol
 */
function loadRoleMenu(menuItems) {
    const navMenu = document.getElementById('navMenu');
    if (!navMenu) return;

    navMenu.innerHTML = '';

    menuItems.forEach(item => {
        const li = document.createElement('li');
        li.className = 'nav-item';

        const a = document.createElement('a');
        a.href = '#';
        a.className = 'nav-link';
        a.onclick = (e) => {
            e.preventDefault();
            showSection(item.section);
        };

        a.innerHTML = `
            <i class="${item.icon}"></i>
            <span>${item.name}</span>
        `;

        li.appendChild(a);
        navMenu.appendChild(li);
    });
}

/**
 * Muestra una sección específica del dashboard
 */
function showSection(sectionId) {
    // Actualizar menú activo
    document.querySelectorAll('.nav-link').forEach(link => {
        link.classList.remove('active');
    });

    const activeLink = Array.from(document.querySelectorAll('.nav-link'))
        .find(link => link.textContent.trim().includes(
            sectionId.charAt(0).toUpperCase() + sectionId.slice(1)
        ));

    if (activeLink) {
        activeLink.classList.add('active');
    }

    // Actualizar título de la página
    const sectionTitles = {
        'dashboard': 'Dashboard',
        'users': 'Gestión de Usuarios',
        'patients': 'Gestión de Pacientes',
        'invoices': 'Sistema de Facturación',
        'clinical-history': 'Historias Clínicas',
        'vital-signs': 'Signos Vitales',
        'inventory': 'Inventario',
        'medications': 'Medicamentos'
    };

    document.getElementById('pageTitle').textContent = sectionTitles[sectionId] || 'Dashboard';

    // Cargar contenido de la sección
    loadSectionContent(sectionId);
}

/**
 * Carga el contenido de una sección específica
 */
async function loadSectionContent(sectionId) {
    const mainGrid = document.getElementById('mainGrid');
    if (!mainGrid) return;

    showLoading('Cargando sección...');

    try {
        switch (sectionId) {
            case 'dashboard':
                await loadDashboardOverview();
                break;
            case 'users':
                await loadUsersSection();
                break;
            case 'patients':
                await loadPatientsSection();
                break;
            case 'invoices':
                await loadInvoicesSection();
                break;
            case 'clinical-history':
                await loadClinicalHistorySection();
                break;
            case 'vital-signs':
                await loadVitalSignsSection();
                break;
            case 'inventory':
                await loadInventorySection();
                break;
            default:
                mainGrid.innerHTML = '<p>Sección no implementada aún</p>';
        }
    } catch (error) {
        console.error('Error loading section:', error);
        mainGrid.innerHTML = '<p>Error al cargar la sección</p>';
    } finally {
        hideLoading();
    }
}

/**
 * Carga la vista general del dashboard
 */
async function loadDashboardOverview() {
    const mainGrid = document.getElementById('mainGrid');
    const roleConfig = DASHBOARD_CONFIG.ROLES[currentRole];

    let content = `
        <div class="content-card">
            <h2 class="card-title">
                <i class="fas fa-tachometer-alt"></i>
                Bienvenido, ${currentUser.fullName}
            </h2>
            <p>Panel de control para ${roleConfig.name}</p>

            <div class="dashboard-stats">
                ${getRoleSpecificStats()}
            </div>
        </div>

        <div class="content-card">
            <h3 class="card-title">
                <i class="fas fa-chart-line"></i>
                Actividad Reciente
            </h3>
            <div id="recentActivity">
                <p>Cargando actividad reciente...</p>
            </div>
        </div>
    `;

    mainGrid.innerHTML = content;

    // Cargar actividad reciente
    await loadRecentActivity();
}

/**
 * Obtiene estadísticas específicas según el rol
 */
function getRoleSpecificStats() {
    const stats = {
        ADMINISTRATIVE: `
            <div class="stat-item">
                <i class="fas fa-users"></i>
                <span>Usuarios Totales: <strong id="totalUsers">-</strong></span>
            </div>
            <div class="stat-item">
                <i class="fas fa-user-md"></i>
                <span>Pacientes Activos: <strong id="activePatients">-</strong></span>
            </div>
            <div class="stat-item">
                <i class="fas fa-file-invoice-dollar"></i>
                <span>Facturas Pendientes: <strong id="pendingInvoices">-</strong></span>
            </div>
        `,
        DOCTOR: `
            <div class="stat-item">
                <i class="fas fa-user-md"></i>
                <span>Mis Pacientes: <strong id="myPatients">-</strong></span>
            </div>
            <div class="stat-item">
                <i class="fas fa-calendar-check"></i>
                <span>Citas Hoy: <strong id="todayAppointments">-</strong></span>
            </div>
            <div class="stat-item">
                <i class="fas fa-prescription"></i>
                <span>Órdenes Pendientes: <strong id="pendingOrders">-</strong></span>
            </div>
        `,
        NURSE: `
            <div class="stat-item">
                <i class="fas fa-heartbeat"></i>
                <span>Signos Vitales Hoy: <strong id="vitalSignsToday">-</strong></span>
            </div>
            <div class="stat-item">
                <i class="fas fa-calendar-alt"></i>
                <span>Visitas Programadas: <strong id="scheduledVisits">-</strong></span>
            </div>
        `
    };

    return stats[currentRole] || '<p>Estadísticas no disponibles para este rol</p>';
}

/**
 * Carga la sección de usuarios
 */
async function loadUsersSection() {
    const mainGrid = document.getElementById('mainGrid');

    let content = `
        <div class="content-card">
            <div class="card-header">
                <h3 class="card-title">
                    <i class="fas fa-users"></i>
                    Gestión de Usuarios
                </h3>
                <button class="btn-primary" onclick="showCreateUserModal()">
                    <i class="fas fa-plus"></i>
                    Nuevo Usuario
                </button>
            </div>

            <div class="filters" style="margin-bottom: 20px;">
                <input type="text" id="userSearch" placeholder="Buscar usuarios..."
                       onkeyup="filterUsers()" style="padding: 8px; border: 1px solid var(--border-color); border-radius: var(--border-radius);">
            </div>

            <div id="usersTable">
                <p>Cargando usuarios...</p>
            </div>
        </div>
    `;

    mainGrid.innerHTML = content;
    await loadUsersTable();
}

/**
 * Carga la tabla de usuarios
 */
async function loadUsersTable() {
    try {
        const response = await fetch(`${API_BASE_URL}/users`);
        const result = await response.json();

        if (result.success) {
            const users = result.data;

            let tableHTML = `
                <table>
                    <thead>
                        <tr>
                            <th>ID</th>
                            <th>Nombre</th>
                            <th>Email</th>
                            <th>Rol</th>
                            <th>Estado</th>
                            <th>Acciones</th>
                        </tr>
                    </thead>
                    <tbody>
            `;

            users.forEach(user => {
                tableHTML += `
                    <tr>
                        <td>${user.idCard}</td>
                        <td>${user.fullName}</td>
                        <td>${user.email}</td>
                        <td>${user.role}</td>
                        <td><span class="status-badge active">Activo</span></td>
                        <td>
                            <button class="btn-action btn-edit" onclick="editUser('${user.idCard}')" title="Editar">
                                <i class="fas fa-edit"></i>
                            </button>
                            <button class="btn-action btn-view" onclick="viewUser('${user.idCard}')" title="Ver">
                                <i class="fas fa-eye"></i>
                            </button>
                            <button class="btn-action btn-delete" onclick="deleteUser('${user.idCard}')" title="Eliminar">
                                <i class="fas fa-trash"></i>
                            </button>
                        </td>
                    </tr>
                `;
            });

            tableHTML += '</tbody></table>';
            document.getElementById('usersTable').innerHTML = tableHTML;

        } else {
            document.getElementById('usersTable').innerHTML = '<p>Error al cargar usuarios</p>';
        }

    } catch (error) {
        console.error('Error loading users:', error);
        document.getElementById('usersTable').innerHTML = '<p>Error de conexión</p>';
    }
}

/**
 * Funciones placeholder para acciones
 */
function showCreateUserModal() {
    alert('Función de crear usuario - por implementar');
}

function editUser(userId) {
    alert(`Editar usuario: ${userId}`);
}

function viewUser(userId) {
    alert(`Ver usuario: ${userId}`);
}

function deleteUser(userId) {
    if (confirm(`¿Está seguro de eliminar el usuario ${userId}?`)) {
        alert(`Eliminar usuario: ${userId}`);
    }
}

function filterUsers() {
    const searchTerm = document.getElementById('userSearch').value.toLowerCase();
    const rows = document.querySelectorAll('#usersTable tbody tr');

    rows.forEach(row => {
        const text = row.textContent.toLowerCase();
        row.style.display = text.includes(searchTerm) ? '' : 'none';
    });
}

// Funciones para otras secciones (placeholders)
async function loadPatientsSection() {
    const mainGrid = document.getElementById('mainGrid');
    mainGrid.innerHTML = `
        <div class="content-card">
            <h3 class="card-title"><i class="fas fa-user-md"></i> Gestión de Pacientes</h3>
            <p>Sección de pacientes - por implementar</p>
        </div>
    `;
}

async function loadInvoicesSection() {
    const mainGrid = document.getElementById('mainGrid');
    mainGrid.innerHTML = `
        <div class="content-card">
            <h3 class="card-title"><i class="fas fa-file-invoice-dollar"></i> Sistema de Facturación</h3>
            <p>Sección de facturación - por implementar</p>
        </div>
    `;
}

async function loadClinicalHistorySection() {
    const mainGrid = document.getElementById('mainGrid');
    mainGrid.innerHTML = `
        <div class="content-card">
            <h3 class="card-title"><i class="fas fa-file-medical"></i> Historias Clínicas</h3>
            <p>Sección de historias clínicas - por implementar</p>
        </div>
    `;
}

async function loadVitalSignsSection() {
    const mainGrid = document.getElementById('mainGrid');
    mainGrid.innerHTML = `
        <div class="content-card">
            <h3 class="card-title"><i class="fas fa-heartbeat"></i> Signos Vitales</h3>
            <p>Sección de signos vitales - por implementar</p>
        </div>
    `;
}

async function loadInventorySection() {
    const mainGrid = document.getElementById('mainGrid');
    mainGrid.innerHTML = `
        <div class="content-card">
            <h3 class="card-title"><i class="fas fa-boxes"></i> Inventario</h3>
            <p>Sección de inventario - por implementar</p>
        </div>
    `;
}

async function loadDashboardData() {
    // Cargar datos del dashboard
    await loadStatsCards();
    await loadRecentActivity();
}

async function loadStatsCards() {
    // Implementar carga de estadísticas
}

async function loadRecentActivity() {
    const activityContainer = document.getElementById('recentActivity');
    if (activityContainer) {
        activityContainer.innerHTML = `
            <div class="activity-item">
                <i class="fas fa-info-circle"></i>
                <span>Dashboard cargado correctamente</span>
                <small>${new Date().toLocaleTimeString()}</small>
            </div>
        `;
    }
}

// Inicializar dashboard si estamos en esa página
if (window.location.pathname.includes('dashboard')) {
    document.addEventListener('DOMContentLoaded', initializeDashboard);
}