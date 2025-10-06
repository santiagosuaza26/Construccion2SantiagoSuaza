/**
 * Servicio de Dashboard
 * Maneja la lógica del dashboard principal según el rol del usuario
 */
class DashboardService {
    constructor() {
        this.currentRole = null;
        this.currentView = null;
    }

    /**
     * Inicializa el dashboard para un rol específico
     */
    initializeForRole(role) {
        this.currentRole = role;
        this.setupNavigation();
        this.loadDefaultView();
        console.log(`📊 Dashboard inicializado para rol: ${role}`);
    }

    /**
     * Configura la navegación según el rol
     */
    setupNavigation() {
        if (!window.authService) return;

        const user = window.authService.getCurrentUser();
        if (!user) return;

        window.authService.setupRoleBasedNavigation();
    }

    /**
     * Carga la vista por defecto según el rol
     */
    loadDefaultView() {
        if (!this.currentRole) return;

        const defaultViews = {
            'HUMAN_RESOURCES': 'nav-users',
            'ADMINISTRATIVE_STAFF': 'nav-patients',
            'DOCTOR': 'nav-patients',
            'NURSE': 'nav-patients',
            'INFORMATION_SUPPORT': 'nav-inventory'
        };

        const defaultView = defaultViews[this.currentRole];
        if (defaultView) {
            this.loadContent(defaultView);
        }
    }

    /**
     * Carga el contenido de una sección específica
     */
    loadContent(navId) {
        this.currentView = navId;
        const contentArea = document.getElementById('content-area');
        if (!contentArea) return;

        // Actualizar navegación activa
        document.querySelectorAll('.nav-item').forEach(item => {
            item.classList.remove('active');
        });
        document.getElementById(navId)?.classList.add('active');

        // Cargar contenido según la sección
        switch(navId) {
            case 'nav-users':
                this.loadUsersContent(contentArea);
                break;
            case 'nav-patients':
                this.loadPatientsContent(contentArea);
                break;
            case 'nav-medical-records':
                this.loadMedicalRecordsContent(contentArea);
                break;
            case 'nav-orders':
                this.loadOrdersContent(contentArea);
                break;
            case 'nav-inventory':
                this.loadInventoryContent(contentArea);
                break;
            case 'nav-billing':
                this.loadBillingContent(contentArea);
                break;
            case 'nav-appointments':
                this.loadAppointmentsContent(contentArea);
                break;
            case 'nav-patient-visits':
                this.loadPatientVisitsContent(contentArea);
                break;
            case 'nav-vital-signs':
                this.loadVitalSignsContent(contentArea);
                break;
            case 'nav-reports':
                this.loadReportsContent(contentArea);
                break;
            case 'nav-maintenance':
                this.loadMaintenanceContent(contentArea);
                break;
            default:
                this.loadDefaultContent(contentArea);
        }
    }

    /**
     * Carga contenido de usuarios
     */
    async loadUsersContent(container) {
        try {
            // Cargar datos reales si el servicio está disponible
            if (window.userManagementService) {
                await window.userManagementService.loadUsersList();
            } else {
                container.innerHTML = `
                     <div class="content-header">
                         <h2><i class="fas fa-users"></i> Gestión de Usuarios</h2>
                         <p>Administra los usuarios del sistema</p>
                     </div>
                     <div class="content-body">
                         <div class="widget">
                             <h3>Usuarios del Sistema</h3>
                             <p>Funcionalidad de gestión de usuarios próximamente disponible.</p>
                             <button class="btn-primary" onclick="alert('Funcionalidad en desarrollo')">
                                 <i class="fas fa-plus"></i> Agregar Usuario
                             </button>
                         </div>
                     </div>
                 `;
            }
        } catch (error) {
            console.error('Error loading users content:', error);
            container.innerHTML = `
                 <div class="content-header">
                     <h2><i class="fas fa-users"></i> Gestión de Usuarios</h2>
                     <p>Administra los usuarios del sistema</p>
                 </div>
                 <div class="content-body">
                     <div class="empty-state">
                         <i class="fas fa-users"></i>
                         <h3>Error al cargar usuarios</h3>
                         <p>No se pudieron cargar los usuarios del sistema.</p>
                         <button class="btn btn-primary" onclick="window.dashboardService.loadContent('nav-users')">
                             <i class="fas fa-refresh"></i> Reintentar
                         </button>
                     </div>
                 </div>
             `;
        }
    }

    /**
     * Carga contenido de pacientes
     */
    async loadPatientsContent(container) {
        try {
            // Cargar datos reales si el servicio está disponible
            if (window.patientManagementService) {
                await window.patientManagementService.loadPatientsList();
            } else {
                container.innerHTML = `
                     <div class="content-header">
                         <h2><i class="fas fa-user-injured"></i> Gestión de Pacientes</h2>
                         <p>Administra la información de los pacientes</p>
                     </div>
                     <div class="content-body">
                         <div class="widget">
                             <h3>Pacientes Registrados</h3>
                             <p>Lista de pacientes próximamente disponible.</p>
                             <button class="btn-primary" onclick="alert('Funcionalidad en desarrollo')">
                                 <i class="fas fa-plus"></i> Registrar Paciente
                             </button>
                         </div>
                     </div>
                 `;
            }
        } catch (error) {
            console.error('Error loading patients content:', error);
            container.innerHTML = `
                 <div class="content-header">
                     <h2><i class="fas fa-user-injured"></i> Gestión de Pacientes</h2>
                     <p>Administra la información de los pacientes</p>
                 </div>
                 <div class="content-body">
                     <div class="empty-state">
                         <i class="fas fa-user-injured"></i>
                         <h3>Error al cargar pacientes</h3>
                         <p>No se pudieron cargar los pacientes del sistema.</p>
                         <button class="btn btn-primary" onclick="window.dashboardService.loadContent('nav-patients')">
                             <i class="fas fa-refresh"></i> Reintentar
                         </button>
                     </div>
                 </div>
             `;
        }
    }

    /**
     * Carga contenido de historias clínicas
     */
    async loadMedicalRecordsContent(container) {
        try {
            // Cargar datos reales si el servicio está disponible
            if (window.medicalRecordsService) {
                await window.medicalRecordsService.loadPatientsList();
            } else {
                container.innerHTML = `
                     <div class="content-header">
                         <h2><i class="fas fa-file-medical"></i> Historias Clínicas</h2>
                         <p>Gestión de historias clínicas digitales</p>
                     </div>
                     <div class="content-body">
                         <div class="widget">
                             <h3>Historias Clínicas</h3>
                             <p>Funcionalidad de historias clínicas próximamente disponible.</p>
                         </div>
                     </div>
                 `;
            }
        } catch (error) {
            console.error('Error loading medical records content:', error);
            container.innerHTML = `
                 <div class="content-header">
                     <h2><i class="fas fa-file-medical"></i> Historias Clínicas</h2>
                     <p>Gestión de historias clínicas digitales</p>
                 </div>
                 <div class="content-body">
                     <div class="empty-state">
                         <i class="fas fa-file-medical"></i>
                         <h3>Error al cargar historias clínicas</h3>
                         <p>No se pudieron cargar las historias clínicas del sistema.</p>
                         <button class="btn btn-primary" onclick="window.dashboardService.loadContent('nav-medical-records')">
                             <i class="fas fa-refresh"></i> Reintentar
                         </button>
                     </div>
                 </div>
             `;
        }
    }

    /**
     * Carga contenido de órdenes médicas
     */
    loadOrdersContent(container) {
        container.innerHTML = `
            <div class="content-header">
                <h2><i class="fas fa-prescription-bottle"></i> Órdenes Médicas</h2>
                <p>Gestión de órdenes médicas y prescripciones</p>
            </div>
            <div class="content-body">
                <div class="widget">
                    <h3>Órdenes Médicas</h3>
                    <p>Funcionalidad de órdenes médicas próximamente disponible.</p>
                    <button class="btn-primary" onclick="alert('Funcionalidad en desarrollo')">
                        <i class="fas fa-plus"></i> Nueva Orden
                    </button>
                </div>
            </div>
        `;
    }

    /**
     * Carga contenido de inventario
     */
    loadInventoryContent(container) {
        container.innerHTML = `
            <div class="content-header">
                <h2><i class="fas fa-boxes"></i> Gestión de Inventario</h2>
                <p>Control y seguimiento de inventario médico</p>
            </div>
            <div class="content-body">
                <div class="widget">
                    <h3>Inventario Actual</h3>
                    <p>Información de inventario próximamente disponible.</p>
                    <button class="btn-primary" onclick="alert('Funcionalidad en desarrollo')">
                        <i class="fas fa-plus"></i> Agregar Ítem
                    </button>
                </div>
            </div>
        `;
    }

    /**
     * Carga contenido de facturación
     */
    async loadBillingContent(container) {
        try {
            // Cargar datos reales si el servicio está disponible
            if (window.billingService) {
                await window.billingService.loadInvoicesList();
            } else {
                container.innerHTML = `
                     <div class="content-header">
                         <h2><i class="fas fa-file-invoice-dollar"></i> Facturación</h2>
                         <p>Sistema de facturación y pagos</p>
                     </div>
                     <div class="content-body">
                         <div class="widget">
                             <h3>Facturación</h3>
                             <p>Funcionalidad de facturación próximamente disponible.</p>
                         </div>
                     </div>
                 `;
            }
        } catch (error) {
            console.error('Error loading billing content:', error);
            container.innerHTML = `
                 <div class="content-header">
                     <h2><i class="fas fa-file-invoice-dollar"></i> Facturación</h2>
                     <p>Sistema de facturación y pagos</p>
                 </div>
                 <div class="content-body">
                     <div class="empty-state">
                         <i class="fas fa-file-invoice-dollar"></i>
                         <h3>Error al cargar facturación</h3>
                         <p>No se pudieron cargar las facturas del sistema.</p>
                         <button class="btn btn-primary" onclick="window.dashboardService.loadContent('nav-billing')">
                             <i class="fas fa-refresh"></i> Reintentar
                         </button>
                     </div>
                 </div>
             `;
        }
    }

    /**
     * Carga contenido por defecto con estadísticas según el rol
     */
    async loadDefaultContent(container) {
        const currentUser = window.authService?.getCurrentUser();

        if (!currentUser) {
            container.innerHTML = `
                 <div class="content-header">
                     <h2><i class="fas fa-home"></i> Bienvenido al Sistema</h2>
                     <p>Selecciona una opción del menú para comenzar</p>
                 </div>
                 <div class="content-body">
                     <div class="widget">
                         <h3>Inicio</h3>
                         <p>Contenido por defecto del dashboard.</p>
                     </div>
                 </div>
             `;
            return;
        }

        try {
            // Crear contenido personalizado según el rol
            const roleContent = await this.getRoleSpecificContent(currentUser.role);

            container.innerHTML = `
                <div class="content-header">
                    <h2><i class="fas fa-home"></i> Bienvenido, ${currentUser.fullName || currentUser.username}</h2>
                    <p>Panel de control personalizado para tu rol</p>
                </div>

                <div class="content-body">
                    ${roleContent}

                    <!-- Información general del sistema -->
                    <div class="dashboard-info">
                        <div class="info-section">
                            <h3><i class="fas fa-info-circle"></i> Información del Sistema</h3>
                            <div class="info-grid">
                                <div class="info-item">
                                    <span class="info-label">Rol:</span>
                                    <span class="info-value">${this.getRoleDisplayName(currentUser.role)}</span>
                                </div>
                                <div class="info-item">
                                    <span class="info-label">Último acceso:</span>
                                    <span class="info-value">${new Date().toLocaleDateString('es-CO')}</span>
                                </div>
                                <div class="info-item">
                                    <span class="info-label">Estado del sistema:</span>
                                    <span class="info-value">
                                        <i class="fas fa-circle success"></i> Operativo
                                    </span>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            `;

        } catch (error) {
            console.error('Error loading default content:', error);
            container.innerHTML = `
                 <div class="content-header">
                     <h2><i class="fas fa-home"></i> Bienvenido al Sistema</h2>
                     <p>Selecciona una opción del menú para comenzar</p>
                 </div>
                 <div class="content-body">
                     <div class="empty-state">
                         <i class="fas fa-home"></i>
                         <h3>Error al cargar el dashboard</h3>
                         <p>No se pudo cargar el contenido personalizado del dashboard.</p>
                         <button class="btn btn-primary" onclick="window.location.reload()">
                             <i class="fas fa-refresh"></i> Recargar página
                         </button>
                     </div>
                 </div>
             `;
        }
    }

    /**
     * Obtiene contenido específico según el rol
     */
    async getRoleSpecificContent(role) {
        switch (role) {
            case 'HUMAN_RESOURCES':
                return await this.getHRDashboardContent();
            case 'ADMINISTRATIVE_STAFF':
                return await this.getAdminDashboardContent();
            case 'DOCTOR':
                return await this.getDoctorDashboardContent();
            case 'NURSE':
                return await this.getNurseDashboardContent();
            case 'INFORMATION_SUPPORT':
                return await this.getSupportDashboardContent();
            default:
                return this.getDefaultDashboardContent();
        }
    }

    /**
     * Contenido del dashboard para Recursos Humanos
     */
    async getHRDashboardContent() {
        try {
            let usersCount = 0;
            let activeUsersCount = 0;

            if (window.userApi) {
                const users = await window.userApi.findAllUsers();
                usersCount = users ? users.length : 0;
                activeUsersCount = users ? users.filter(u => u.active).length : 0;
            }

            return `
                <div class="dashboard-section">
                    <h3><i class="fas fa-users"></i> Gestión de Personal</h3>
                    <div class="stats-grid">
                        <div class="stat-card">
                            <div class="stat-value">${usersCount}</div>
                            <div class="stat-label">Total Empleados</div>
                            <div class="stat-action">
                                <button class="btn btn-sm btn-primary" onclick="window.dashboardService.loadContent('nav-users')">
                                    Ver Usuarios
                                </button>
                            </div>
                        </div>
                        <div class="stat-card">
                            <div class="stat-value">${activeUsersCount}</div>
                            <div class="stat-label">Empleados Activos</div>
                            <div class="stat-action">
                                <button class="btn btn-sm btn-success" onclick="window.userManagementService?.loadUsersList()">
                                    Gestionar
                                </button>
                            </div>
                        </div>
                    </div>
                </div>
            `;
        } catch (error) {
            return this.getDefaultDashboardContent();
        }
    }

    /**
     * Contenido del dashboard para Personal Administrativo
     */
    async getAdminDashboardContent() {
        try {
            let patientsCount = 0;

            if (window.patientApi) {
                const patients = await window.patientApi.findAllPatients();
                patientsCount = patients ? patients.length : 0;
            }

            return `
                <div class="dashboard-section">
                    <h3><i class="fas fa-user-injured"></i> Gestión de Pacientes</h3>
                    <div class="stats-grid">
                        <div class="stat-card">
                            <div class="stat-value">${patientsCount}</div>
                            <div class="stat-label">Pacientes Registrados</div>
                            <div class="stat-action">
                                <button class="btn btn-sm btn-primary" onclick="window.dashboardService.loadContent('nav-patients')">
                                    Ver Pacientes
                                </button>
                            </div>
                        </div>
                        <div class="stat-card">
                            <div class="stat-value">0</div>
                            <div class="stat-label">Citas del Día</div>
                            <div class="stat-action">
                                <button class="btn btn-sm btn-info" onclick="window.dashboardService.loadContent('nav-appointments')">
                                    Ver Citas
                                </button>
                            </div>
                        </div>
                    </div>
                </div>
            `;
        } catch (error) {
            return this.getDefaultDashboardContent();
        }
    }

    /**
     * Contenido del dashboard para Médicos
     */
    async getDoctorDashboardContent() {
        try {
            return `
                <div class="dashboard-section">
                    <h3><i class="fas fa-stethoscope"></i> Consultas Médicas</h3>
                    <div class="stats-grid">
                        <div class="stat-card">
                            <div class="stat-value">0</div>
                            <div class="stat-label">Pacientes del Día</div>
                            <div class="stat-action">
                                <button class="btn btn-sm btn-primary" onclick="window.dashboardService.loadContent('nav-patients')">
                                    Ver Pacientes
                                </button>
                            </div>
                        </div>
                        <div class="stat-card">
                            <div class="stat-value">0</div>
                            <div class="stat-label">Historias Clínicas</div>
                            <div class="stat-action">
                                <button class="btn btn-sm btn-success" onclick="window.dashboardService.loadContent('nav-medical-records')">
                                    Gestionar
                                </button>
                            </div>
                        </div>
                    </div>
                </div>
            `;
        } catch (error) {
            return this.getDefaultDashboardContent();
        }
    }

    /**
     * Contenido del dashboard para Enfermeras
     */
    async getNurseDashboardContent() {
        try {
            return `
                <div class="dashboard-section">
                    <h3><i class="fas fa-user-nurse"></i> Atención de Pacientes</h3>
                    <div class="stats-grid">
                        <div class="stat-card">
                            <div class="stat-value">0</div>
                            <div class="stat-label">Visitas del Día</div>
                            <div class="stat-action">
                                <button class="btn btn-sm btn-primary" onclick="window.dashboardService.loadContent('nav-patient-visits')">
                                    Ver Visitas
                                </button>
                            </div>
                        </div>
                        <div class="stat-card">
                            <div class="stat-value">0</div>
                            <div class="stat-label">Signos Vitales</div>
                            <div class="stat-action">
                                <button class="btn btn-sm btn-warning" onclick="window.dashboardService.loadContent('nav-vital-signs')">
                                    Registrar
                                </button>
                            </div>
                        </div>
                    </div>
                </div>
            `;
        } catch (error) {
            return this.getDefaultDashboardContent();
        }
    }

    /**
     * Contenido del dashboard para Soporte de Información
     */
    async getSupportDashboardContent() {
        try {
            let inventoryCount = 0;
            let lowStockCount = 0;

            if (window.inventoryApi) {
                const items = await window.inventoryApi.findAllInventoryItems();
                inventoryCount = items ? items.length : 0;
                lowStockCount = items ? items.filter(i => i.quantity <= i.minimumStock).length : 0;
            }

            return `
                <div class="dashboard-section">
                    <h3><i class="fas fa-boxes"></i> Gestión de Inventario</h3>
                    <div class="stats-grid">
                        <div class="stat-card">
                            <div class="stat-value">${inventoryCount}</div>
                            <div class="stat-label">Items en Inventario</div>
                            <div class="stat-action">
                                <button class="btn btn-sm btn-primary" onclick="window.dashboardService.loadContent('nav-inventory')">
                                    Ver Inventario
                                </button>
                            </div>
                        </div>
                        <div class="stat-card ${lowStockCount > 0 ? 'warning' : ''}">
                            <div class="stat-value">${lowStockCount}</div>
                            <div class="stat-label">Stock Bajo</div>
                            <div class="stat-action">
                                <button class="btn btn-sm btn-warning" onclick="window.inventoryManagementService?.loadInventoryList()">
                                    Revisar
                                </button>
                            </div>
                        </div>
                    </div>
                </div>
            `;
        } catch (error) {
            return this.getDefaultDashboardContent();
        }
    }

    /**
     * Contenido por defecto del dashboard
     */
    getDefaultDashboardContent() {
        return `
            <div class="dashboard-section">
                <h3><i class="fas fa-chart-line"></i> Resumen General</h3>
                <div class="stats-grid">
                    <div class="stat-card">
                        <div class="stat-value">-</div>
                        <div class="stat-label">Datos no disponibles</div>
                        <div class="stat-action">
                            <button class="btn btn-sm btn-secondary" onclick="window.location.reload()">
                                Recargar
                            </button>
                        </div>
                    </div>
                </div>
            </div>
        `;
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
            'INFORMATION_SUPPORT': 'Soporte de Información'
        };

        return roleNames[role] || role;
    }

    // Métodos adicionales para cada sección
    loadAppointmentsContent = (container) => this.loadDefaultContent(container);
    loadPatientVisitsContent = (container) => this.loadDefaultContent(container);
    loadVitalSignsContent = (container) => this.loadDefaultContent(container);
    loadReportsContent = (container) => this.loadDefaultContent(container);
    loadMaintenanceContent = (container) => this.loadDefaultContent(container);
}

// Crear instancia global del servicio de dashboard
window.dashboardService = new DashboardService();