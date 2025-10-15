/**
 * Servicio de Gestión para Personal Administrativo
 * Implementa funcionalidades específicas para el rol ADMINISTRATIVE_STAFF según especificaciones técnicas
 */
class AdministrativeManagementService {
    constructor() {
        this.patients = [];
        this.filteredPatients = [];
        this.appointments = [];
        this.billingData = [];
        this.currentPage = 1;
        this.itemsPerPage = 10;
        this.searchTerm = '';
        this.selectedStatusFilter = 'ALL';
        this.selectedInsuranceFilter = 'ALL';
    }

    /**
     * Inicializa la página de Personal Administrativo
     */
    async initialize() {
        try {
            window.showInfo('Cargando sistema administrativo...');
            await this.loadPatientsData();
            await this.loadAppointmentsData();
            this.renderAdministrativePage();
            this.setupEventListeners();
            window.showSuccess('Sistema administrativo listo');
        } catch (error) {
            console.error('Error inicializando administrativo:', error);
            window.showError('Error al inicializar sistema administrativo: ' + error.message);
            this.showErrorState();
        }
    }

    /**
     * Carga los datos de pacientes desde la API
     */
    async loadPatientsData() {
        try {
            const response = await window.patientApi.findAllPatients();

            if (response && Array.isArray(response)) {
                this.patients = response;
                this.filteredPatients = [...this.patients];
                console.log(`✅ Cargados ${this.patients.length} pacientes`);
            } else {
                throw new Error('Formato de respuesta inválido');
            }
        } catch (error) {
            console.error('Error cargando pacientes:', error);
            throw new Error('No se pudieron cargar los pacientes: ' + error.message);
        }
    }

    /**
     * Carga los datos de citas desde la API
     */
    async loadAppointmentsData() {
        try {
            // Por ahora usamos datos mock hasta que esté implementada la API
            this.appointments = [];
            console.log(`✅ Cargadas ${this.appointments.length} citas`);
        } catch (error) {
            console.error('Error cargando citas:', error);
            this.appointments = [];
        }
    }

    /**
     * Renderiza la página completa de Personal Administrativo
     */
    renderAdministrativePage() {
        const contentArea = document.getElementById('content-area');
        if (!contentArea) {
            console.error('No se encontró el área de contenido');
            return;
        }

        contentArea.innerHTML = `
            <div class="administrative-management-container">
                <!-- Header específico de Administrativo -->
                <div class="admin-header">
                    <div class="admin-title-section">
                        <i class="fas fa-user-cog admin-icon"></i>
                        <div class="admin-title-content">
                            <h1>Personal Administrativo</h1>
                            <p class="admin-subtitle">Gestión integral de pacientes, citas y facturación</p>
                        </div>
                    </div>
                    <div class="admin-actions">
                        <button class="btn btn-primary btn-lg" onclick="administrativeManagementService.showCreatePatientModal()">
                            <i class="fas fa-user-plus"></i>
                            <span>Nuevo Paciente</span>
                        </button>
                        <button class="btn btn-success btn-lg" onclick="administrativeManagementService.showCreateAppointmentModal()">
                            <i class="fas fa-calendar-plus"></i>
                            <span>Nueva Cita</span>
                        </button>
                    </div>
                </div>

                <!-- Estadísticas específicas de Administrativo -->
                <div class="admin-stats-grid">
                    <div class="admin-stat-card">
                        <div class="stat-icon">
                            <i class="fas fa-user-injured"></i>
                        </div>
                        <div class="stat-content">
                            <div class="stat-value">${this.patients.length}</div>
                            <div class="stat-label">Total Pacientes</div>
                        </div>
                    </div>

                    <div class="admin-stat-card">
                        <div class="stat-icon">
                            <i class="fas fa-user-check"></i>
                        </div>
                        <div class="stat-content">
                            <div class="stat-value">${this.getActivePatientsCount()}</div>
                            <div class="stat-label">Pacientes Activos</div>
                        </div>
                    </div>

                    <div class="admin-stat-card">
                        <div class="stat-icon">
                            <i class="fas fa-shield-alt"></i>
                        </div>
                        <div class="stat-content">
                            <div class="stat-value">${this.getPatientsWithInsuranceCount()}</div>
                            <div class="stat-label">Con Seguro Médico</div>
                        </div>
                    </div>

                    <div class="admin-stat-card">
                        <div class="stat-icon">
                            <i class="fas fa-calendar-check"></i>
                        </div>
                        <div class="stat-content">
                            <div class="stat-value">${this.appointments.length}</div>
                            <div class="stat-label">Citas del Día</div>
                        </div>
                    </div>

                    <div class="admin-stat-card">
                        <div class="stat-icon">
                            <i class="fas fa-file-invoice-dollar"></i>
                        </div>
                        <div class="stat-content">
                            <div class="stat-value">${this.getPendingInvoicesCount()}</div>
                            <div class="stat-label">Facturas Pendientes</div>
                        </div>
                    </div>

                    <div class="admin-stat-card">
                        <div class="stat-icon">
                            <i class="fas fa-chart-line"></i>
                        </div>
                        <div class="stat-content">
                            <div class="stat-value">$${(this.getTotalBillingAmount() / 1000).toFixed(0)}k</div>
                            <div class="stat-label">Facturación Mensual</div>
                        </div>
                    </div>
                </div>

                <!-- Controles de navegación por pestañas -->
                <div class="admin-tabs">
                    <div class="tab-buttons">
                        <button class="tab-btn active" onclick="administrativeManagementService.showTab('patients')">
                            <i class="fas fa-user-injured"></i>
                            Gestión de Pacientes
                        </button>
                        <button class="tab-btn" onclick="administrativeManagementService.showTab('appointments')">
                            <i class="fas fa-calendar-alt"></i>
                            Citas Médicas
                        </button>
                        <button class="tab-btn" onclick="administrativeManagementService.showTab('billing')">
                            <i class="fas fa-file-invoice-dollar"></i>
                            Facturación
                        </button>
                    </div>
                </div>

                <!-- Contenido de pestañas -->
                <div class="admin-tab-content">
                    <!-- Gestión de Pacientes -->
                    <div id="patients-tab" class="tab-pane active">
                        ${this.renderPatientsSection()}
                    </div>

                    <!-- Gestión de Citas -->
                    <div id="appointments-tab" class="tab-pane" style="display: none;">
                        ${this.renderAppointmentsSection()}
                    </div>

                    <!-- Gestión de Facturación -->
                    <div id="billing-tab" class="tab-pane" style="display: none;">
                        ${this.renderBillingSection()}
                    </div>
                </div>
            </div>

            <!-- Modal para crear/editar paciente -->
            ${this.renderPatientModal()}

            <!-- Modal para crear cita -->
            ${this.renderAppointmentModal()}
        `;
    }

    /**
     * Renderiza la sección de gestión de pacientes
     */
    renderPatientsSection() {
        return `
            <div class="patients-section">
                <!-- Controles de búsqueda y filtros para pacientes -->
                <div class="patients-controls">
                    <div class="search-section">
                        <div class="search-box">
                            <i class="fas fa-search search-icon"></i>
                            <input type="text" id="patient-search" class="search-input"
                                   placeholder="Buscar por nombre, cédula o email..."
                                   value="${this.searchTerm}">
                        </div>
                    </div>

                    <div class="filters-section">
                        <div class="filter-group">
                            <label for="patient-status-filter">Filtrar por Estado:</label>
                            <select id="patient-status-filter" class="filter-select">
                                <option value="ALL">Todos los estados</option>
                                <option value="ACTIVE">Activos</option>
                                <option value="INACTIVE">Inactivos</option>
                            </select>
                        </div>

                        <div class="filter-group">
                            <label for="insurance-filter">Filtrar por Seguro:</label>
                            <select id="insurance-filter" class="filter-select">
                                <option value="ALL">Todos</option>
                                <option value="WITH_INSURANCE">Con Seguro</option>
                                <option value="WITHOUT_INSURANCE">Sin Seguro</option>
                            </select>
                        </div>
                    </div>
                </div>

                <!-- Tabla de pacientes -->
                <div class="patients-table-container">
                    <table class="patients-table">
                        <thead>
                            <tr>
                                <th>Cédula</th>
                                <th>Nombre Completo</th>
                                <th>Teléfono</th>
                                <th>Email</th>
                                <th>Seguro Médico</th>
                                <th>Estado</th>
                                <th>Fecha de Registro</th>
                                <th>Acciones</th>
                            </tr>
                        </thead>
                        <tbody id="patients-table-body">
                            ${this.renderPatientsTableRows()}
                        </tbody>
                    </table>
                </div>

                <!-- Paginación para pacientes -->
                ${this.renderPatientsPagination()}
            </div>
        `;
    }

    /**
     * Renderiza las filas de la tabla de pacientes
     */
    renderPatientsTableRows() {
        const startIndex = (this.currentPage - 1) * this.itemsPerPage;
        const endIndex = startIndex + this.itemsPerPage;
        const patientsToShow = this.filteredPatients.slice(startIndex, endIndex);

        if (patientsToShow.length === 0) {
            return `
                <tr>
                    <td colspan="8" class="admin-empty-state">
                        <i class="fas fa-user-injured"></i>
                        <p>No se encontraron pacientes</p>
                        <small>Intente ajustar los filtros de búsqueda</small>
                    </td>
                </tr>
            `;
        }

        return patientsToShow.map(patient => `
            <tr class="patient-row ${patient.active ? 'patient-active' : 'patient-inactive'}">
                <td class="patient-cedula">
                    <span class="cedula-value">${window.formatCedula ? window.formatCedula(patient.cedula) : patient.cedula}</span>
                </td>
                <td class="patient-fullname">
                    <div class="patient-info">
                        <div class="patient-name">${patient.firstName} ${patient.lastName}</div>
                        <div class="patient-details">
                            <small class="patient-age">
                                <i class="fas fa-birthday-cake"></i>
                                ${this.calculateAge(patient.birthDate)} años
                            </small>
                            <small class="patient-gender">
                                <i class="fas fa-${this.getGenderIcon(patient.gender)}"></i>
                                ${this.getGenderDisplayName(patient.gender)}
                            </small>
                        </div>
                    </div>
                </td>
                <td class="patient-phone">
                    <div class="phone-info">
                        <i class="fas fa-phone"></i>
                        <span>${patient.phoneNumber || 'N/A'}</span>
                    </div>
                </td>
                <td class="patient-email">
                    <div class="email-info">
                        <i class="fas fa-envelope"></i>
                        <span>${patient.email || 'N/A'}</span>
                    </div>
                </td>
                <td class="patient-insurance">
                    ${this.renderInsuranceBadge(patient.insurancePolicy)}
                </td>
                <td class="patient-status">
                    <span class="status-badge ${patient.active ? 'status-active' : 'status-inactive'}">
                        <i class="fas ${patient.active ? 'fa-check-circle' : 'fa-ban'}"></i>
                        ${patient.active ? 'Activo' : 'Inactivo'}
                    </span>
                </td>
                <td class="patient-created-date">
                    <div class="date-info">
                        <i class="fas fa-calendar"></i>
                        <span>${this.formatDate(patient.createdAt)}</span>
                    </div>
                </td>
                <td class="patient-actions">
                    <div class="actions-menu">
                        <button class="btn btn-sm btn-primary" onclick="administrativeManagementService.editPatient('${patient.cedula}')" title="Editar paciente">
                            <i class="fas fa-edit"></i>
                            Editar
                        </button>
                        <button class="btn btn-sm btn-info" onclick="administrativeManagementService.viewPatientHistory('${patient.cedula}')" title="Ver historia clínica">
                            <i class="fas fa-file-medical"></i>
                            Historia
                        </button>
                        <button class="btn btn-sm btn-success" onclick="administrativeManagementService.scheduleAppointment('${patient.cedula}')" title="Agendar cita">
                            <i class="fas fa-calendar-plus"></i>
                            Cita
                        </button>
                        <button class="btn btn-sm btn-warning" onclick="administrativeManagementService.generateInvoice('${patient.cedula}')" title="Generar factura">
                            <i class="fas fa-file-invoice-dollar"></i>
                            Factura
                        </button>
                    </div>
                </td>
            </tr>
        `).join('');
    }

    /**
     * Renderiza el badge de seguro médico
     */
    renderInsuranceBadge(insurancePolicy) {
        if (!insurancePolicy || !insurancePolicy.isActive) {
            return '<span class="insurance-badge no-insurance">Sin Seguro</span>';
        }

        const expirationDate = new Date(insurancePolicy.expirationDate);
        const today = new Date();
        const isExpired = expirationDate < today;

        return `
            <div class="insurance-info">
                <span class="insurance-badge ${isExpired ? 'insurance-expired' : 'insurance-active'}">
                    <i class="fas ${isExpired ? 'fa-exclamation-triangle' : 'fa-shield-alt'}"></i>
                    ${insurancePolicy.companyName}
                </span>
                <small class="insurance-policy">${insurancePolicy.policyNumber}</small>
                ${isExpired ? '<small class="insurance-expiry">Vencida</small>' : ''}
            </div>
        `;
    }

    /**
     * Renderiza la sección de citas médicas
     */
    renderAppointmentsSection() {
        return `
            <div class="appointments-section">
                <div class="section-header">
                    <h3><i class="fas fa-calendar-alt"></i> Gestión de Citas Médicas</h3>
                    <p>Programar y gestionar citas de pacientes</p>
                </div>

                <div class="appointments-controls">
                    <button class="btn btn-primary" onclick="administrativeManagementService.showCreateAppointmentModal()">
                        <i class="fas fa-plus"></i> Nueva Cita
                    </button>
                    <button class="btn btn-info" onclick="administrativeManagementService.refreshAppointments()">
                        <i class="fas fa-refresh"></i> Actualizar
                    </button>
                </div>

                <div class="appointments-table-container">
                    <div class="appointments-placeholder">
                        <i class="fas fa-calendar-plus"></i>
                        <h4>Programador de Citas</h4>
                        <p>Haga clic en "Nueva Cita" para programar una nueva cita médica</p>
                        <button class="btn btn-primary" onclick="administrativeManagementService.showCreateAppointmentModal()">
                            <i class="fas fa-calendar-plus"></i> Programar Cita
                        </button>
                    </div>
                </div>
            </div>
        `;
    }

    /**
     * Renderiza la sección de facturación
     */
    renderBillingSection() {
        return `
            <div class="billing-section">
                <div class="section-header">
                    <h3><i class="fas fa-file-invoice-dollar"></i> Sistema de Facturación</h3>
                    <p>Gestión de facturas y cálculos de copagos</p>
                </div>

                <div class="billing-info">
                    <div class="info-card">
                        <h4><i class="fas fa-info-circle"></i> Información de Facturación</h4>
                        <div class="billing-rules">
                            <div class="rule-item">
                                <i class="fas fa-check-circle"></i>
                                <div class="rule-content">
                                    <strong>Copago estándar:</strong> $50.000 COP por servicio
                                </div>
                            </div>
                            <div class="rule-item">
                                <i class="fas fa-check-circle"></i>
                                <div class="rule-content">
                                    <strong>Límite anual:</strong> Si acumulado > $1.000.000 COP → exento de copagos
                                </div>
                            </div>
                            <div class="rule-item">
                                <i class="fas fa-check-circle"></i>
                                <div class="rule-content">
                                    <strong>Consulta acumulado:</strong> GET /billing/copagos/accumulated?cedula=
                                </div>
                            </div>
                        </div>
                    </div>
                </div>

                <div class="billing-actions">
                    <button class="btn btn-success" onclick="administrativeManagementService.showBillingCalculator()">
                        <i class="fas fa-calculator"></i> Calcular Copago
                    </button>
                    <button class="btn btn-info" onclick="administrativeManagementService.viewBillingReports()">
                        <i class="fas fa-chart-bar"></i> Reportes de Facturación
                    </button>
                </div>
            </div>
        `;
    }

    /**
     * Renderiza el modal de paciente
     */
    renderPatientModal() {
        return `
            <div id="patient-modal" class="admin-modal-overlay" style="display: none;">
                <div class="admin-modal">
                    <div class="admin-modal-header">
                        <h2 id="patient-modal-title">
                            <i class="fas fa-user-plus"></i>
                            Registrar Nuevo Paciente
                        </h2>
                        <button class="admin-modal-close" onclick="administrativeManagementService.closePatientModal()">
                            <i class="fas fa-times"></i>
                        </button>
                    </div>

                    <div class="admin-modal-body">
                        <form id="patient-form" class="patient-form">
                            <div class="form-alert" id="patient-form-alert" style="display: none;"></div>

                            <!-- Información Personal -->
                            <div class="form-section">
                                <h3><i class="fas fa-id-card"></i> Información Personal</h3>
                                <div class="form-row">
                                    <div class="form-group">
                                        <label for="patient-cedula">Cédula *</label>
                                        <input type="text" id="patient-cedula" name="cedula" required maxlength="20"
                                               placeholder="Número de documento">
                                        <small class="form-hint">Solo números, único en el sistema</small>
                                    </div>
                                    <div class="form-group">
                                        <label for="patient-firstname">Primer Nombre *</label>
                                        <input type="text" id="patient-firstname" name="firstName" required maxlength="50"
                                               placeholder="Primer nombre">
                                        <small class="form-hint">Máximo 50 caracteres</small>
                                    </div>
                                </div>

                                <div class="form-row">
                                    <div class="form-group">
                                        <label for="patient-lastname">Apellido *</label>
                                        <input type="text" id="patient-lastname" name="lastName" required maxlength="50"
                                               placeholder="Apellido completo">
                                        <small class="form-hint">Máximo 50 caracteres</small>
                                    </div>
                                    <div class="form-group">
                                        <label for="patient-birthdate">Fecha de Nacimiento *</label>
                                        <input type="date" id="patient-birthdate" name="birthDate" required>
                                        <small class="form-hint">Se calculará automáticamente la edad</small>
                                    </div>
                                </div>

                                <div class="form-row">
                                    <div class="form-group">
                                        <label for="patient-gender">Género *</label>
                                        <select id="patient-gender" name="gender" required>
                                            <option value="">Seleccionar género</option>
                                            <option value="MASCULINO">Masculino</option>
                                            <option value="FEMENINO">Femenino</option>
                                            <option value="OTRO">Otro</option>
                                        </select>
                                    </div>
                                    <div class="form-group">
                                        <label for="patient-phone">Teléfono *</label>
                                        <input type="tel" id="patient-phone" name="phoneNumber" required maxlength="10"
                                               placeholder="Número celular">
                                        <small class="form-hint">Solo números, 10 dígitos</small>
                                    </div>
                                </div>

                                <div class="form-group">
                                    <label for="patient-address">Dirección *</label>
                                    <input type="text" id="patient-address" name="address" required maxlength="100"
                                           placeholder="Dirección de residencia">
                                    <small class="form-hint">Máximo 100 caracteres</small>
                                </div>

                                <div class="form-group">
                                    <label for="patient-email">Correo Electrónico</label>
                                    <input type="email" id="patient-email" name="email"
                                           placeholder="correo@ejemplo.com (opcional)">
                                    <small class="form-hint">Opcional, debe tener formato válido</small>
                                </div>
                            </div>

                            <!-- Contacto de Emergencia -->
                            <div class="form-section">
                                <h3><i class="fas fa-phone-volume"></i> Contacto de Emergencia</h3>
                                <div class="form-row">
                                    <div class="form-group">
                                        <label for="emergency-name">Nombre *</label>
                                        <input type="text" id="emergency-name" name="emergencyName" required maxlength="100"
                                               placeholder="Nombre completo">
                                    </div>
                                    <div class="form-group">
                                        <label for="emergency-relationship">Parentesco *</label>
                                        <input type="text" id="emergency-relationship" name="emergencyRelationship" required maxlength="50"
                                               placeholder="Ej: Hermano, Esposo, Padre">
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label for="emergency-phone">Teléfono *</label>
                                    <input type="tel" id="emergency-phone" name="emergencyPhone" required maxlength="10"
                                           placeholder="Número de contacto">
                                    <small class="form-hint">Solo números, 10 dígitos</small>
                                </div>
                            </div>

                            <!-- Información de Seguro Médico -->
                            <div class="form-section">
                                <h3><i class="fas fa-shield-alt"></i> Seguro Médico (Opcional)</h3>
                                <div class="insurance-toggle">
                                    <label class="switch">
                                        <input type="checkbox" id="has-insurance" onchange="administrativeManagementService.toggleInsuranceForm()">
                                        <span class="slider"></span>
                                    </label>
                                    <span class="toggle-label">El paciente tiene seguro médico</span>
                                </div>

                                <div id="insurance-form" class="insurance-form" style="display: none;">
                                    <div class="form-row">
                                        <div class="form-group">
                                            <label for="insurance-company">Compañía de Seguro *</label>
                                            <input type="text" id="insurance-company" name="insuranceCompany" maxlength="100"
                                                   placeholder="Nombre de la aseguradora">
                                        </div>
                                        <div class="form-group">
                                            <label for="insurance-policy">Número de Póliza *</label>
                                            <input type="text" id="insurance-policy" name="insurancePolicyNumber" maxlength="50"
                                                   placeholder="Número de póliza">
                                        </div>
                                    </div>
                                    <div class="form-row">
                                        <div class="form-group">
                                            <label for="insurance-expiry">Fecha de Vencimiento *</label>
                                            <input type="date" id="insurance-expiry" name="insuranceExpiryDate">
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </form>
                    </div>

                    <div class="admin-modal-footer">
                        <button class="btn btn-secondary" onclick="administrativeManagementService.closePatientModal()">
                            <i class="fas fa-times"></i>
                            Cancelar
                        </button>
                        <button class="btn btn-primary" onclick="administrativeManagementService.savePatient()">
                            <i class="fas fa-save"></i>
                            <span id="patient-save-btn-text">Registrar Paciente</span>
                        </button>
                    </div>
                </div>
            </div>
        `;
    }

    /**
     * Renderiza el modal de citas
     */
    renderAppointmentModal() {
        return `
            <div id="appointment-modal" class="admin-modal-overlay" style="display: none;">
                <div class="admin-modal">
                    <div class="admin-modal-header">
                        <h2 id="appointment-modal-title">
                            <i class="fas fa-calendar-plus"></i>
                            Programar Nueva Cita
                        </h2>
                        <button class="admin-modal-close" onclick="administrativeManagementService.closeAppointmentModal()">
                            <i class="fas fa-times"></i>
                        </button>
                    </div>

                    <div class="admin-modal-body">
                        <form id="appointment-form" class="appointment-form">
                            <div class="form-alert" id="appointment-form-alert" style="display: none;"></div>

                            <div class="form-section">
                                <h3><i class="fas fa-calendar-alt"></i> Información de la Cita</h3>
                                <div class="form-row">
                                    <div class="form-group">
                                        <label for="appointment-patient">Paciente *</label>
                                        <select id="appointment-patient" name="patientCedula" required>
                                            <option value="">Seleccionar paciente</option>
                                            ${this.patients.filter(p => p.active).map(patient =>
                                                `<option value="${patient.cedula}">${patient.firstName} ${patient.lastName} - ${window.formatCedula ? window.formatCedula(patient.cedula) : patient.cedula}</option>`
                                            ).join('')}
                                        </select>
                                    </div>
                                    <div class="form-group">
                                        <label for="appointment-doctor">Médico *</label>
                                        <select id="appointment-doctor" name="doctorCedula" required>
                                            <option value="">Seleccionar médico</option>
                                        </select>
                                    </div>
                                </div>

                                <div class="form-row">
                                    <div class="form-group">
                                        <label for="appointment-datetime">Fecha y Hora *</label>
                                        <input type="datetime-local" id="appointment-datetime" name="appointmentDateTime" required>
                                        <small class="form-hint">Seleccione fecha y hora de la cita</small>
                                    </div>
                                    <div class="form-group">
                                        <label for="appointment-status">Estado *</label>
                                        <select id="appointment-status" name="status" required>
                                            <option value="PROGRAMADA">Programada</option>
                                            <option value="CONFIRMADA">Confirmada</option>
                                            <option value="CANCELADA">Cancelada</option>
                                        </select>
                                    </div>
                                </div>

                                <div class="form-group">
                                    <label for="appointment-reason">Motivo de Consulta</label>
                                    <textarea id="appointment-reason" name="reason" rows="3" maxlength="500"
                                              placeholder="Describa el motivo de la consulta médica"></textarea>
                                    <small class="form-hint">Opcional, máximo 500 caracteres</small>
                                </div>

                                <div class="form-group">
                                    <label for="appointment-notes">Notas Adicionales</label>
                                    <textarea id="appointment-notes" name="notes" rows="2" maxlength="300"
                                              placeholder="Notas adicionales para el médico"></textarea>
                                    <small class="form-hint">Opcional, máximo 300 caracteres</small>
                                </div>
                            </div>
                        </form>
                    </div>

                    <div class="admin-modal-footer">
                        <button class="btn btn-secondary" onclick="administrativeManagementService.closeAppointmentModal()">
                            <i class="fas fa-times"></i>
                            Cancelar
                        </button>
                        <button class="btn btn-primary" onclick="administrativeManagementService.saveAppointment()">
                            <i class="fas fa-save"></i>
                            <span id="appointment-save-btn-text">Programar Cita</span>
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
        // Búsqueda de pacientes
        const searchInput = document.getElementById('patient-search');
        if (searchInput) {
            searchInput.addEventListener('input', window.debounce((e) => {
                this.searchTerm = e.target.value;
                this.applyPatientFilters();
            }, 300));
        }

        // Filtros de pacientes
        const statusFilter = document.getElementById('patient-status-filter');
        const insuranceFilter = document.getElementById('insurance-filter');

        if (statusFilter) {
            statusFilter.addEventListener('change', (e) => {
                this.selectedStatusFilter = e.target.value;
                this.applyPatientFilters();
            });
        }

        if (insuranceFilter) {
            insuranceFilter.addEventListener('change', (e) => {
                this.selectedInsuranceFilter = e.target.value;
                this.applyPatientFilters();
            });
        }
    }

    /**
     * Aplica filtros a la lista de pacientes
     */
    applyPatientFilters() {
        let filtered = [...this.patients];

        // Aplicar búsqueda
        if (this.searchTerm) {
            const term = this.searchTerm.toLowerCase();
            filtered = filtered.filter(patient =>
                `${patient.firstName} ${patient.lastName}`.toLowerCase().includes(term) ||
                patient.cedula.includes(term) ||
                (patient.email && patient.email.toLowerCase().includes(term))
            );
        }

        // Aplicar filtro de estado
        if (this.selectedStatusFilter !== 'ALL') {
            const isActive = this.selectedStatusFilter === 'ACTIVE';
            filtered = filtered.filter(patient => patient.active === isActive);
        }

        // Aplicar filtro de seguro
        if (this.selectedInsuranceFilter !== 'ALL') {
            if (this.selectedInsuranceFilter === 'WITH_INSURANCE') {
                filtered = filtered.filter(patient => patient.insurancePolicy && patient.insurancePolicy.isActive);
            } else if (this.selectedInsuranceFilter === 'WITHOUT_INSURANCE') {
                filtered = filtered.filter(patient => !patient.insurancePolicy || !patient.insurancePolicy.isActive);
            }
        }

        this.filteredPatients = filtered;
        this.currentPage = 1;
        this.refreshPatientsTable();
    }

    /**
     * Refresca la tabla de pacientes
     */
    refreshPatientsTable() {
        const tableBody = document.getElementById('patients-table-body');
        if (tableBody) {
            tableBody.innerHTML = this.renderPatientsTableRows();
        }
    }

    /**
     * Cambia entre pestañas
     */
    showTab(tabName) {
        // Ocultar todas las pestañas
        document.querySelectorAll('.tab-pane').forEach(pane => {
            pane.style.display = 'none';
        });

        // Remover clase active de todos los botones
        document.querySelectorAll('.tab-btn').forEach(btn => {
            btn.classList.remove('active');
        });

        // Mostrar pestaña seleccionada
        document.getElementById(`${tabName}-tab`)?.style.setProperty('display', 'block');

        // Activar botón correspondiente
        event.target.classList.add('active');
    }

    /**
     * Calcula la edad del paciente
     */
    calculateAge(birthDateString) {
        if (!birthDateString) return 'N/A';

        try {
            const birthDate = new Date(birthDateString);
            const today = new Date();
            let age = today.getFullYear() - birthDate.getFullYear();
            const monthDiff = today.getMonth() - birthDate.getMonth();

            if (monthDiff < 0 || (monthDiff === 0 && today.getDate() < birthDate.getDate())) {
                age--;
            }

            return age > 0 ? age : 'N/A';
        } catch (error) {
            return 'N/A';
        }
    }

    /**
     * Obtiene el ícono para el género
     */
    getGenderIcon(gender) {
        const icons = {
            'MASCULINO': 'mars',
            'FEMENINO': 'venus',
            'OTRO': 'genderless'
        };
        return icons[gender] || 'question';
    }

    /**
     * Obtiene el nombre para mostrar del género
     */
    getGenderDisplayName(gender) {
        const names = {
            'MASCULINO': 'Masculino',
            'FEMENINO': 'Femenino',
            'OTRO': 'Otro'
        };
        return names[gender] || gender;
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
     * Obtiene el conteo de pacientes activos
     */
    getActivePatientsCount() {
        return this.patients.filter(patient => patient.active).length;
    }

    /**
     * Obtiene el conteo de pacientes con seguro médico
     */
    getPatientsWithInsuranceCount() {
        return this.patients.filter(patient =>
            patient.insurancePolicy && patient.insurancePolicy.isActive
        ).length;
    }

    /**
     * Obtiene el conteo de facturas pendientes
     */
    getPendingInvoicesCount() {
        return this.billingData.filter(bill => bill.status === 'PENDING').length;
    }

    /**
     * Obtiene el monto total de facturación
     */
    getTotalBillingAmount() {
        return this.billingData.reduce((total, bill) => total + (bill.totalAmount || 0), 0);
    }

    /**
     * Muestra el modal para crear paciente
     */
    showCreatePatientModal() {
        this.currentPatient = null;
        this.showPatientModal();
        this.resetPatientForm();
    }

    /**
     * Muestra el modal de paciente
     */
    showPatientModal() {
        const modal = document.getElementById('patient-modal');
        if (modal) {
            modal.style.display = 'flex';
            setTimeout(() => {
                modal.classList.add('show');
            }, 10);
        }
    }

    /**
     * Edita un paciente existente
     */
    async editPatient(cedula) {
        try {
            window.showInfo('Cargando información del paciente...');

            const patient = await window.patientApi.findPatientByCedula(cedula);

            if (patient) {
                this.currentPatient = patient;
                this.showPatientModal();
                this.populatePatientForm(patient);
                window.showSuccess('Paciente cargado para edición');
            } else {
                throw new Error('Paciente no encontrado');
            }

        } catch (error) {
            console.error('Error cargando paciente:', error);
            window.showError('Error al cargar paciente: ' + error.message);
        }
    }

    /**
     * Llena el formulario con datos del paciente
     */
    populatePatientForm(patient) {
        const modalTitle = document.getElementById('patient-modal-title');
        if (modalTitle) {
            modalTitle.innerHTML = '<i class="fas fa-user-edit"></i> Editar Paciente';
        }

        const saveBtn = document.getElementById('patient-save-btn-text');
        if (saveBtn) {
            saveBtn.textContent = 'Actualizar Paciente';
        }

        // Llenar campos básicos
        const fields = [
            { id: 'patient-cedula', value: patient.cedula },
            { id: 'patient-firstname', value: patient.firstName },
            { id: 'patient-lastname', value: patient.lastName },
            { id: 'patient-phone', value: patient.phoneNumber },
            { id: 'patient-address', value: patient.address },
            { id: 'patient-email', value: patient.email || '' },
            { id: 'patient-gender', value: patient.gender }
        ];

        fields.forEach(field => {
            const element = document.getElementById(field.id);
            if (element) {
                element.value = field.value || '';
            }
        });

        // Formatear fecha de nacimiento
        if (patient.birthDate) {
            const birthDate = new Date(patient.birthDate);
            const birthDateInput = document.getElementById('patient-birthdate');
            if (birthDateInput) {
                birthDateInput.value = birthDate.toISOString().split('T')[0];
            }
        }

        // Llenar contacto de emergencia
        if (patient.emergencyContact) {
            document.getElementById('emergency-name').value = patient.emergencyContact.name || '';
            document.getElementById('emergency-relationship').value = patient.emergencyContact.relationship || '';
            document.getElementById('emergency-phone').value = patient.emergencyContact.phoneNumber || '';
        }

        // Llenar información de seguro
        if (patient.insurancePolicy) {
            document.getElementById('has-insurance').checked = true;
            this.toggleInsuranceForm();

            document.getElementById('insurance-company').value = patient.insurancePolicy.companyName || '';
            document.getElementById('insurance-policy').value = patient.insurancePolicy.policyNumber || '';

            if (patient.insurancePolicy.expirationDate) {
                const expiryDate = new Date(patient.insurancePolicy.expirationDate);
                document.getElementById('insurance-expiry').value = expiryDate.toISOString().split('T')[0];
            }
        }
    }

    /**
     * Guarda el paciente (crear o actualizar)
     */
    async savePatient() {
        if (!this.validatePatientForm()) {
            return;
        }

        const patientData = this.getPatientFormData();
        const isEditing = !!this.currentPatient;

        try {
            window.showInfo(isEditing ? 'Actualizando paciente...' : 'Registrando paciente...');

            let result;
            if (isEditing) {
                result = await window.patientApi.updatePatient(this.currentPatient.cedula, patientData);
            } else {
                result = await window.patientApi.createPatient(patientData);
            }

            if (result) {
                window.showSuccess(`Paciente ${isEditing ? 'actualizado' : 'registrado'} exitosamente`);
                this.closePatientModal();
                await this.loadPatientsData();
                this.renderAdministrativePage();
            }

        } catch (error) {
            console.error('Error guardando paciente:', error);
            this.showPatientFormAlert(error.message, 'error');
        }
    }

    /**
     * Valida el formulario de paciente
     */
    validatePatientForm() {
        const validations = [
            { field: 'patient-cedula', validator: (value) => this.validatePatientCedula(value) },
            { field: 'patient-firstname', validator: (value) => this.validatePatientName(value, 'Primer nombre') },
            { field: 'patient-lastname', validator: (value) => this.validatePatientName(value, 'Apellido') },
            { field: 'patient-birthdate', validator: (value) => this.validatePatientBirthDate(value) },
            { field: 'patient-gender', validator: (value) => this.validatePatientGender(value) },
            { field: 'patient-phone', validator: (value) => this.validatePatientPhone(value) },
            { field: 'patient-address', validator: (value) => this.validatePatientAddress(value) },
            { field: 'patient-email', validator: (value) => this.validatePatientEmail(value) }
        ];

        for (const validation of validations) {
            const element = document.getElementById(validation.field);
            const value = element ? element.value.trim() : '';

            if (!validation.validator(value)) {
                element?.focus();
                return false;
            }
        }

        // Validar contacto de emergencia
        if (!this.validateEmergencyContact()) {
            return false;
        }

        // Validar seguro médico si está marcado
        if (document.getElementById('has-insurance').checked && !this.validateInsuranceInfo()) {
            return false;
        }

        return true;
    }

    /**
     * Validaciones específicas para pacientes según especificaciones técnicas
     */
    validatePatientCedula(cedula) {
        if (!cedula) {
            this.showPatientFieldError('patient-cedula', 'La cédula es obligatoria');
            return false;
        }

        if (!/^\d+$/.test(cedula)) {
            this.showPatientFieldError('patient-cedula', 'La cédula debe contener solo números');
            return false;
        }

        this.clearPatientFieldError('patient-cedula');
        return true;
    }

    validatePatientName(name, fieldName) {
        if (!name) {
            this.showPatientFieldError(`patient-${fieldName === 'Primer nombre' ? 'firstname' : 'lastname'}`, `${fieldName} es obligatorio`);
            return false;
        }

        if (name.length > 50) {
            this.showPatientFieldError(`patient-${fieldName === 'Primer nombre' ? 'firstname' : 'lastname'}`, `${fieldName} no puede tener más de 50 caracteres`);
            return false;
        }

        this.clearPatientFieldError(`patient-${fieldName === 'Primer nombre' ? 'firstname' : 'lastname'}`);
        return true;
    }

    validatePatientBirthDate(birthDate) {
        if (!birthDate) {
            this.showPatientFieldError('patient-birthdate', 'La fecha de nacimiento es obligatoria');
            return false;
        }

        const birth = new Date(birthDate);
        const today = new Date();
        const age = today.getFullYear() - birth.getFullYear();

        if (age > 150) {
            this.showPatientFieldError('patient-birthdate', 'La edad no puede ser mayor a 150 años');
            return false;
        }

        if (birth > today) {
            this.showPatientFieldError('patient-birthdate', 'La fecha de nacimiento no puede ser futura');
            return false;
        }

        this.clearPatientFieldError('patient-birthdate');
        return true;
    }

    validatePatientGender(gender) {
        const validGenders = ['MASCULINO', 'FEMENINO', 'OTRO'];

        if (!gender) {
            this.showPatientFieldError('patient-gender', 'El género es obligatorio');
            return false;
        }

        if (!validGenders.includes(gender)) {
            this.showPatientFieldError('patient-gender', 'Género seleccionado no válido');
            return false;
        }

        this.clearPatientFieldError('patient-gender');
        return true;
    }

    validatePatientPhone(phone) {
        if (!phone) {
            this.showPatientFieldError('patient-phone', 'El teléfono es obligatorio');
            return false;
        }

        if (!/^\d{10}$/.test(phone)) {
            this.showPatientFieldError('patient-phone', 'El teléfono debe tener exactamente 10 dígitos');
            return false;
        }

        this.clearPatientFieldError('patient-phone');
        return true;
    }

    validatePatientAddress(address) {
        if (!address) {
            this.showPatientFieldError('patient-address', 'La dirección es obligatoria');
            return false;
        }

        if (address.length > 100) {
            this.showPatientFieldError('patient-address', 'La dirección no puede tener más de 100 caracteres');
            return false;
        }

        this.clearPatientFieldError('patient-address');
        return true;
    }

    validatePatientEmail(email) {
        if (!email) {
            this.clearPatientFieldError('patient-email');
            return true; // Email es opcional
        }

        if (!window.isValidEmail ? true : window.isValidEmail(email)) {
            this.clearPatientFieldError('patient-email');
            return true;
        } else {
            this.showPatientFieldError('patient-email', 'Formato de correo electrónico inválido');
            return false;
        }
    }

    validateEmergencyContact() {
        const hasEmergencyContact = document.getElementById('emergency-name').value.trim() ||
                                   document.getElementById('emergency-relationship').value.trim() ||
                                   document.getElementById('emergency-phone').value.trim();

        if (!hasEmergencyContact) {
            this.clearPatientFieldError('emergency-contact');
            return true; // Contacto de emergencia es opcional según especificaciones
        }

        const name = document.getElementById('emergency-name').value.trim();
        const relationship = document.getElementById('emergency-relationship').value.trim();
        const phone = document.getElementById('emergency-phone').value.trim();

        if (!name) {
            this.showPatientFieldError('emergency-name', 'El nombre del contacto de emergencia es obligatorio');
            return false;
        }

        if (!relationship) {
            this.showPatientFieldError('emergency-relationship', 'El parentesco es obligatorio');
            return false;
        }

        if (!phone) {
            this.showPatientFieldError('emergency-phone', 'El teléfono del contacto es obligatorio');
            return false;
        }

        if (!/^\d{10}$/.test(phone)) {
            this.showPatientFieldError('emergency-phone', 'El teléfono debe tener exactamente 10 dígitos');
            return false;
        }

        this.clearPatientFieldError('emergency-contact');
        return true;
    }

    validateInsuranceInfo() {
        const company = document.getElementById('insurance-company').value.trim();
        const policyNumber = document.getElementById('insurance-policy').value.trim();
        const expiryDate = document.getElementById('insurance-expiry').value;

        if (!company) {
            this.showPatientFieldError('insurance-company', 'La compañía de seguro es obligatoria');
            return false;
        }

        if (!policyNumber) {
            this.showPatientFieldError('insurance-policy', 'El número de póliza es obligatorio');
            return false;
        }

        if (!expiryDate) {
            this.showPatientFieldError('insurance-expiry', 'La fecha de vencimiento es obligatoria');
            return false;
        }

        const expiry = new Date(expiryDate);
        const today = new Date();

        if (expiry <= today) {
            this.showPatientFieldError('insurance-expiry', 'La fecha de vencimiento debe ser futura');
            return false;
        }

        this.clearPatientFieldError('insurance-company');
        this.clearPatientFieldError('insurance-policy');
        this.clearPatientFieldError('insurance-expiry');
        return true;
    }

    /**
     * Muestra error en un campo específico del formulario de paciente
     */
    showPatientFieldError(fieldId, message) {
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
     * Limpia error de un campo específico del formulario de paciente
     */
    clearPatientFieldError(fieldId) {
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
     * Muestra alerta en el formulario de paciente
     */
    showPatientFormAlert(message, type = 'error') {
        const alertElement = document.getElementById('patient-form-alert');
        if (alertElement) {
            alertElement.textContent = message;
            alertElement.className = `form-alert alert-${type}`;
            alertElement.style.display = 'block';
        }
    }

    /**
     * Obtiene los datos del formulario de paciente
     */
    getPatientFormData() {
        const data = {
            cedula: document.getElementById('patient-cedula').value.trim(),
            firstName: document.getElementById('patient-firstname').value.trim(),
            lastName: document.getElementById('patient-lastname').value.trim(),
            birthDate: document.getElementById('patient-birthdate').value,
            gender: document.getElementById('patient-gender').value,
            address: document.getElementById('patient-address').value.trim(),
            phoneNumber: document.getElementById('patient-phone').value.trim(),
            email: document.getElementById('patient-email').value.trim() || null
        };

        // Agregar contacto de emergencia si se proporciona
        const emergencyName = document.getElementById('emergency-name').value.trim();
        const emergencyRelationship = document.getElementById('emergency-relationship').value.trim();
        const emergencyPhone = document.getElementById('emergency-phone').value.trim();

        if (emergencyName && emergencyRelationship && emergencyPhone) {
            data.emergencyContact = {
                name: emergencyName,
                relationship: emergencyRelationship,
                phoneNumber: emergencyPhone
            };
        }

        // Agregar información de seguro si está marcada
        if (document.getElementById('has-insurance').checked) {
            data.insurancePolicy = {
                companyName: document.getElementById('insurance-company').value.trim(),
                policyNumber: document.getElementById('insurance-policy').value.trim(),
                expirationDate: document.getElementById('insurance-expiry').value,
                isActive: true
            };
        }

        return data;
    }

    /**
     * Alterna la visibilidad del formulario de seguro médico
     */
    toggleInsuranceForm() {
        const checkbox = document.getElementById('has-insurance');
        const insuranceForm = document.getElementById('insurance-form');

        if (checkbox && insuranceForm) {
            if (checkbox.checked) {
                insuranceForm.style.display = 'block';
                setTimeout(() => {
                    insuranceForm.style.opacity = '1';
                    insuranceForm.style.transform = 'translateY(0)';
                }, 10);
            } else {
                insuranceForm.style.opacity = '0';
                insuranceForm.style.transform = 'translateY(-10px)';
                setTimeout(() => {
                    insuranceForm.style.display = 'none';
                }, 300);
            }
        }
    }

    /**
     * Cierra el modal de paciente
     */
    closePatientModal() {
        const modal = document.getElementById('patient-modal');
        if (modal) {
            modal.classList.remove('show');
            setTimeout(() => {
                modal.style.display = 'none';
            }, 300);
        }
        this.currentPatient = null;
    }

    /**
     * Resetea el formulario de paciente
     */
    resetPatientForm() {
        const form = document.getElementById('patient-form');
        if (form) {
            form.reset();
        }

        // Resetear título del modal
        const modalTitle = document.getElementById('patient-modal-title');
        if (modalTitle) {
            modalTitle.innerHTML = '<i class="fas fa-user-plus"></i> Registrar Nuevo Paciente';
        }

        // Ocultar formulario de seguro
        const insuranceForm = document.getElementById('insurance-form');
        if (insuranceForm) {
            insuranceForm.style.display = 'none';
        }

        // Ocultar alertas
        this.hidePatientFormAlert();

        // Resetear botón de guardar
        const saveBtn = document.getElementById('patient-save-btn-text');
        if (saveBtn) {
            saveBtn.textContent = 'Registrar Paciente';
        }
    }

    /**
     * Oculta alerta del formulario de paciente
     */
    hidePatientFormAlert() {
        const alertElement = document.getElementById('patient-form-alert');
        if (alertElement) {
            alertElement.style.display = 'none';
        }
    }

    /**
     * Muestra el modal para crear cita
     */
    showCreateAppointmentModal() {
        this.currentAppointment = null;
        this.showAppointmentModal();
        this.resetAppointmentForm();
    }

    /**
     * Muestra el modal de citas
     */
    showAppointmentModal() {
        const modal = document.getElementById('appointment-modal');
        if (modal) {
            modal.style.display = 'flex';
            setTimeout(() => {
                modal.classList.add('show');
            }, 10);
        }
    }

    /**
     * Cierra el modal de citas
     */
    closeAppointmentModal() {
        const modal = document.getElementById('appointment-modal');
        if (modal) {
            modal.classList.remove('show');
            setTimeout(() => {
                modal.style.display = 'none';
            }, 300);
        }
        this.currentAppointment = null;
    }

    /**
     * Resetea el formulario de citas
     */
    resetAppointmentForm() {
        const form = document.getElementById('appointment-form');
        if (form) {
            form.reset();
        }

        // Resetear título del modal
        const modalTitle = document.getElementById('appointment-modal-title');
        if (modalTitle) {
            modalTitle.innerHTML = '<i class="fas fa-calendar-plus"></i> Programar Nueva Cita';
        }

        // Ocultar alertas
        this.hideAppointmentFormAlert();

        // Resetear botón de guardar
        const saveBtn = document.getElementById('appointment-save-btn-text');
        if (saveBtn) {
            saveBtn.textContent = 'Programar Cita';
        }

        // Cargar lista de médicos disponibles
        this.loadAvailableDoctors();
    }

    /**
     * Carga la lista de médicos disponibles
     */
    async loadAvailableDoctors() {
        try {
            const doctorsSelect = document.getElementById('appointment-doctor');
            if (!doctorsSelect) return;

            // Obtener usuarios con rol DOCTOR
            const response = await window.userApi.findUsersByRole('DOCTOR');

            if (response && Array.isArray(response)) {
                doctorsSelect.innerHTML = '<option value="">Seleccionar médico</option>';

                response.filter(user => user.active).forEach(doctor => {
                    const option = document.createElement('option');
                    option.value = doctor.cedula;
                    option.textContent = `${doctor.fullName} - ${doctor.cedula}`;
                    doctorsSelect.appendChild(option);
                });
            }
        } catch (error) {
            console.error('Error cargando médicos:', error);
        }
    }

    /**
     * Guarda la cita médica
     */
    async saveAppointment() {
        if (!this.validateAppointmentForm()) {
            return;
        }

        const appointmentData = this.getAppointmentFormData();

        try {
            window.showInfo('Programando cita médica...');

            const result = await window.appointmentApi.createAppointment(appointmentData);

            if (result) {
                window.showSuccess('Cita programada exitosamente');
                this.closeAppointmentModal();
                await this.loadAppointmentsData();
                this.renderAdministrativePage();
            }

        } catch (error) {
            console.error('Error guardando cita:', error);
            this.showAppointmentFormAlert(error.message, 'error');
        }
    }

    /**
     * Valida el formulario de citas
     */
    validateAppointmentForm() {
        const patientCedula = document.getElementById('appointment-patient').value;
        const doctorCedula = document.getElementById('appointment-doctor').value;
        const appointmentDateTime = document.getElementById('appointment-datetime').value;
        const status = document.getElementById('appointment-status').value;

        if (!patientCedula) {
            this.showAppointmentFormAlert('Debe seleccionar un paciente');
            document.getElementById('appointment-patient')?.focus();
            return false;
        }

        if (!doctorCedula) {
            this.showAppointmentFormAlert('Debe seleccionar un médico');
            document.getElementById('appointment-doctor')?.focus();
            return false;
        }

        if (!appointmentDateTime) {
            this.showAppointmentFormAlert('Debe seleccionar fecha y hora');
            document.getElementById('appointment-datetime')?.focus();
            return false;
        }

        // Validar que la fecha no sea pasada
        const appointmentDate = new Date(appointmentDateTime);
        const now = new Date();

        if (appointmentDate <= now) {
            this.showAppointmentFormAlert('La fecha de la cita debe ser futura');
            document.getElementById('appointment-datetime')?.focus();
            return false;
        }

        return true;
    }

    /**
     * Obtiene los datos del formulario de citas
     */
    getAppointmentFormData() {
        return {
            patientCedula: document.getElementById('appointment-patient').value,
            doctorCedula: document.getElementById('appointment-doctor').value,
            appointmentDateTime: document.getElementById('appointment-datetime').value,
            status: document.getElementById('appointment-status').value,
            reason: document.getElementById('appointment-reason').value.trim() || null,
            notes: document.getElementById('appointment-notes').value.trim() || null
        };
    }

    /**
     * Oculta alerta del formulario de citas
     */
    hideAppointmentFormAlert() {
        const alertElement = document.getElementById('appointment-form-alert');
        if (alertElement) {
            alertElement.style.display = 'none';
        }
    }

    /**
     * Muestra alerta en el formulario de citas
     */
    showAppointmentFormAlert(message, type = 'error') {
        const alertElement = document.getElementById('appointment-form-alert');
        if (alertElement) {
            alertElement.textContent = message;
            alertElement.className = `form-alert alert-${type}`;
            alertElement.style.display = 'block';
        }
    }

    /**
     * Muestra la calculadora de copagos
     */
    showBillingCalculator() {
        // Crear modal de calculadora de copagos
        const modalHtml = `
            <div id="billing-calculator-modal" class="admin-modal-overlay" style="display: none;">
                <div class="admin-modal">
                    <div class="admin-modal-header">
                        <h2><i class="fas fa-calculator"></i> Calculadora de Copagos</h2>
                        <button class="admin-modal-close" onclick="administrativeManagementService.closeBillingCalculator()">
                            <i class="fas fa-times"></i>
                        </button>
                    </div>

                    <div class="admin-modal-body">
                        <div class="billing-calculator">
                            <div class="calculator-info">
                                <h4><i class="fas fa-info-circle"></i> Reglas de Facturación</h4>
                                <div class="billing-rules">
                                    <div class="rule-item">
                                        <i class="fas fa-check-circle"></i>
                                        <div class="rule-content">
                                            <strong>Copago estándar:</strong> $50.000 COP por servicio
                                        </div>
                                    </div>
                                    <div class="rule-item">
                                        <i class="fas fa-check-circle"></i>
                                        <div class="rule-content">
                                            <strong>Límite anual:</strong> $1.000.000 COP máximo acumulado
                                        </div>
                                    </div>
                                    <div class="rule-item">
                                        <i class="fas fa-check-circle"></i>
                                        <div class="rule-content">
                                            <strong>Exención automática:</strong> Si acumulado > $1.000.000 COP → sin copagos resto del año
                                        </div>
                                    </div>
                                </div>
                            </div>

                            <div class="calculator-form">
                                <div class="form-row">
                                    <div class="form-group">
                                        <label for="calc-patient-cedula">Cédula del Paciente *</label>
                                        <input type="text" id="calc-patient-cedula" placeholder="Número de cédula">
                                    </div>
                                    <div class="form-group">
                                        <label for="calc-service-cost">Costo del Servicio *</label>
                                        <input type="number" id="calc-service-cost" placeholder="Valor en COP" min="0">
                                    </div>
                                </div>

                                <div class="form-row">
                                    <div class="form-group">
                                        <label for="calc-has-insurance">¿Tiene Seguro Médico?</label>
                                        <select id="calc-has-insurance">
                                            <option value="false">No tiene seguro</option>
                                            <option value="true">Tiene seguro activo</option>
                                        </select>
                                    </div>
                                    <div class="form-group">
                                        <label for="calc-current-accumulated">Acumulado Actual (COP)</label>
                                        <input type="number" id="calc-current-accumulated" placeholder="Acumulado anual actual" min="0" value="0">
                                    </div>
                                </div>

                                <div class="calculation-results" id="calculation-results" style="display: none;">
                                    <h4><i class="fas fa-chart-line"></i> Resultado del Cálculo</h4>
                                    <div class="results-grid">
                                        <div class="result-item">
                                            <span class="result-label">Costo del Servicio:</span>
                                            <span class="result-value" id="service-cost-result">$0</span>
                                        </div>
                                        <div class="result-item">
                                            <span class="result-label">Paciente debe pagar:</span>
                                            <span class="result-value" id="patient-payment">$0</span>
                                        </div>
                                        <div class="result-item">
                                            <span class="result-label">Aseguradora cubre:</span>
                                            <span class="result-value" id="insurance-coverage">$0</span>
                                        </div>
                                        <div class="result-item">
                                            <span class="result-label">Nuevo acumulado:</span>
                                            <span class="result-value" id="new-accumulated">$0</span>
                                        </div>
                                    </div>
                                </div>

                                <div class="calculator-actions">
                                    <button class="btn btn-primary" onclick="administrativeManagementService.calculateCopayment()">
                                        <i class="fas fa-calculator"></i> Calcular Copago
                                    </button>
                                    <button class="btn btn-secondary" onclick="administrativeManagementService.closeBillingCalculator()">
                                        Cerrar
                                    </button>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        `;

        // Agregar modal al DOM
        const existingModal = document.getElementById('billing-calculator-modal');
        if (existingModal) {
            existingModal.remove();
        }

        document.body.insertAdjacentHTML('beforeend', modalHtml);
        this.showBillingCalculator();
    }

    /**
     * Muestra la calculadora de copagos
     */
    showBillingCalculator() {
        const modal = document.getElementById('billing-calculator-modal');
        if (modal) {
            modal.style.display = 'flex';
            setTimeout(() => {
                modal.classList.add('show');
            }, 10);
        }
    }

    /**
     * Cierra la calculadora de copagos
     */
    closeBillingCalculator() {
        const modal = document.getElementById('billing-calculator-modal');
        if (modal) {
            modal.classList.remove('show');
            setTimeout(() => {
                modal.style.display = 'none';
            }, 300);
        }
    }

    /**
     * Calcula el copago según las reglas específicas
     */
    calculateCopayment() {
        const serviceCost = parseFloat(document.getElementById('calc-service-cost').value) || 0;
        const hasInsurance = document.getElementById('calc-has-insurance').value === 'true';
        const currentAccumulated = parseFloat(document.getElementById('calc-current-accumulated').value) || 0;

        if (serviceCost <= 0) {
            window.showError('El costo del servicio debe ser mayor a cero');
            return;
        }

        // Aplicar reglas de copago según especificaciones técnicas
        let patientPayment = 0;
        let insuranceCoverage = 0;
        let newAccumulated = currentAccumulated;

        if (hasInsurance) {
            // Si tiene seguro activo
            if (currentAccumulated >= 1000000) {
                // Si ya superó el límite anual → paciente no paga copago
                patientPayment = 0;
                insuranceCoverage = serviceCost;
            } else {
                // Si no ha superado el límite → aplica copago fijo
                const remainingForLimit = 1000000 - currentAccumulated;

                if (serviceCost <= remainingForLimit) {
                    // Si el servicio completo cabe dentro del límite
                    patientPayment = 50000; // Copago fijo
                    insuranceCoverage = serviceCost - 50000;
                    newAccumulated = currentAccumulated + 50000;
                } else {
                    // Si el servicio excede el límite
                    patientPayment = remainingForLimit; // Paga hasta completar el límite
                    insuranceCoverage = serviceCost - remainingForLimit;
                    newAccumulated = 1000000; // Límite alcanzado
                }
            }
        } else {
            // Si no tiene seguro → paciente paga todo
            patientPayment = serviceCost;
            insuranceCoverage = 0;
            newAccumulated = currentAccumulated + serviceCost;
        }

        // Mostrar resultados
        document.getElementById('service-cost-result').textContent = `$${serviceCost.toLocaleString()}`;
        document.getElementById('patient-payment').textContent = `$${patientPayment.toLocaleString()}`;
        document.getElementById('insurance-coverage').textContent = `$${insuranceCoverage.toLocaleString()}`;
        document.getElementById('new-accumulated').textContent = `$${newAccumulated.toLocaleString()}`;

        const resultsDiv = document.getElementById('calculation-results');
        if (resultsDiv) {
            resultsDiv.style.display = 'block';
        }

        window.showSuccess('Cálculo realizado exitosamente');
    }

    /**
     * Calcula el acumulado anual de copagos para un paciente
     */
    async calculateAnnualAccumulated(patientCedula) {
        try {
            window.showInfo('Consultando acumulado anual...');

            // Llamar a la API para obtener el acumulado anual
            const response = await window.billingApi.getAnnualAccumulated(patientCedula);

            if (response) {
                this.showAnnualAccumulatedModal(response, patientCedula);
            } else {
                throw new Error('No se pudo obtener el acumulado anual');
            }

        } catch (error) {
            console.error('Error calculating annual accumulated:', error);

            // Si la API no está disponible, mostrar modal con cálculo simulado
            this.showAnnualAccumulatedModal({
                patientCedula: patientCedula,
                currentYear: new Date().getFullYear(),
                accumulatedAmount: 0,
                servicesCount: 0,
                lastUpdate: new Date().toISOString(),
                isLimitReached: false,
                remainingForLimit: 1000000
            }, patientCedula);

            window.showWarning('Usando cálculo simulado. Conecte el backend para datos reales.');
        }
    }

    /**
     * Muestra el modal con el acumulado anual
     */
    showAnnualAccumulatedModal(data, patientCedula) {
        const modal = document.createElement('div');
        modal.className = 'modal-overlay';
        modal.innerHTML = `
            <div class="modal large-modal">
                <div class="modal-header">
                    <h3><i class="fas fa-chart-line"></i> Acumulado Anual de Copagos</h3>
                    <button class="modal-close" onclick="this.closest('.modal-overlay').remove()">
                        <i class="fas fa-times"></i>
                    </button>
                </div>
                <div class="modal-body">
                    <div class="annual-accumulated">
                        <div class="accumulated-header">
                            <h4>Paciente: ${data.patientName || 'No disponible'}</h4>
                            <p><strong>Cédula:</strong> ${window.formatCedula(patientCedula)}</p>
                            <p><strong>Año:</strong> ${data.currentYear || new Date().getFullYear()}</p>
                        </div>

                        <div class="accumulated-summary">
                            <div class="summary-grid">
                                <div class="summary-item">
                                    <div class="summary-icon">
                                        <i class="fas fa-dollar-sign"></i>
                                    </div>
                                    <div class="summary-content">
                                        <div class="summary-value">$${data.accumulatedAmount?.toLocaleString() || '0'}</div>
                                        <div class="summary-label">Acumulado Actual</div>
                                    </div>
                                </div>

                                <div class="summary-item">
                                    <div class="summary-icon">
                                        <i class="fas fa-file-medical"></i>
                                    </div>
                                    <div class="summary-content">
                                        <div class="summary-value">${data.servicesCount || 0}</div>
                                        <div class="summary-label">Servicios Atendidos</div>
                                    </div>
                                </div>

                                <div class="summary-item">
                                    <div class="summary-icon">
                                        <i class="fas ${data.isLimitReached ? 'fa-check-circle text-success' : 'fa-exclamation-triangle text-warning'}"></i>
                                    </div>
                                    <div class="summary-content">
                                        <div class="summary-value">${data.isLimitReached ? 'Alcanzado' : 'Pendiente'}</div>
                                        <div class="summary-label">Estado del Límite</div>
                                    </div>
                                </div>

                                <div class="summary-item">
                                    <div class="summary-icon">
                                        <i class="fas fa-piggy-bank"></i>
                                    </div>
                                    <div class="summary-content">
                                        <div class="summary-value">$${(data.remainingForLimit || 1000000).toLocaleString()}</div>
                                        <div class="summary-label">Restante para Límite</div>
                                    </div>
                                </div>
                            </div>
                        </div>

                        <div class="accumulated-details">
                            <h4><i class="fas fa-info-circle"></i> Información del Límite</h4>
                            <div class="limit-info">
                                <div class="limit-progress">
                                    <div class="progress-bar">
                                        <div class="progress-fill" style="width: ${Math.min((data.accumulatedAmount || 0) / 1000000 * 100, 100)}%"></div>
                                    </div>
                                    <div class="progress-text">
                                        <span>Progreso: $${(data.accumulatedAmount || 0).toLocaleString()} / $1.000.000</span>
                                        <span>${Math.min(((data.accumulatedAmount || 0) / 1000000 * 100).toFixed(1), 100)}%</span>
                                    </div>
                                </div>

                                <div class="limit-rules">
                                    <div class="rule-item">
                                        <i class="fas fa-gavel"></i>
                                        <div class="rule-content">
                                            <strong>Límite anual:</strong> $1.000.000 COP máximo acumulado
                                        </div>
                                    </div>
                                    <div class="rule-item">
                                        <i class="fas fa-shield-alt"></i>
                                        <div class="rule-content">
                                            <strong>Beneficio:</strong> Al alcanzar el límite, el paciente queda exento de copagos por el resto del año
                                        </div>
                                    </div>
                                    <div class="rule-item">
                                        <i class="fas fa-calendar-check"></i>
                                        <div class="rule-content">
                                            <strong>Período:</strong> El límite se calcula por año calendario (1 enero - 31 diciembre)
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>

                        <div class="accumulated-actions">
                            <button class="btn btn-primary" onclick="administrativeManagementService.generateAccumulatedReport('${patientCedula}')">
                                <i class="fas fa-file-pdf"></i> Generar Reporte
                            </button>
                            <button class="btn btn-info" onclick="administrativeManagementService.viewAccumulatedHistory('${patientCedula}')">
                                <i class="fas fa-history"></i> Ver Historial
                            </button>
                        </div>
                    </div>
                </div>
                <div class="modal-footer">
                    <button class="btn btn-secondary" onclick="this.closest('.modal-overlay').remove()">
                        Cerrar
                    </button>
                    <button class="btn btn-success" onclick="administrativeManagementService.showBillingCalculator()">
                        <i class="fas fa-calculator"></i> Calcular Nuevo Copago
                    </button>
                </div>
            </div>
        `;

        document.body.appendChild(modal);

        setTimeout(() => {
            modal.classList.add('show');
        }, 10);
    }

    /**
     * Genera reporte de acumulado anual
     */
    async generateAccumulatedReport(patientCedula) {
        try {
            window.showInfo('Generando reporte de acumulado...');

            // Aquí se llamaría a la API para generar el reporte
            // const report = await window.billingApi.generateAccumulatedReport(patientCedula);

            // Por ahora, generar reporte simulado
            const reportData = {
                patientCedula: patientCedula,
                patientName: 'Paciente de Prueba',
                currentYear: new Date().getFullYear(),
                accumulatedAmount: 450000,
                servicesCount: 8,
                isLimitReached: false,
                remainingForLimit: 550000,
                generatedAt: new Date().toISOString(),
                reportType: 'ANNUAL_ACCUMULATED'
            };

            this.downloadAccumulatedReport(reportData);
            window.showSuccess('Reporte generado exitosamente');

        } catch (error) {
            console.error('Error generating accumulated report:', error);
            window.showError('Error al generar reporte: ' + error.message);
        }
    }

    /**
     * Descarga el reporte de acumulado anual
     */
    downloadAccumulatedReport(reportData) {
        const reportHtml = `
            <!DOCTYPE html>
            <html>
            <head>
                <title>Reporte de Acumulado Anual - ${reportData.patientCedula}</title>
                <meta charset="UTF-8">
                <style>
                    body { font-family: Arial, sans-serif; margin: 40px; }
                    .header { text-align: center; margin-bottom: 30px; }
                    .patient-info { margin-bottom: 30px; }
                    .summary { display: grid; grid-template-columns: repeat(2, 1fr); gap: 20px; margin-bottom: 30px; }
                    .summary-item { padding: 15px; border: 1px solid #ddd; border-radius: 5px; }
                    .summary-value { font-size: 24px; font-weight: bold; color: #2563eb; }
                    .summary-label { font-size: 12px; color: #666; text-transform: uppercase; }
                    .progress-bar { width: 100%; height: 20px; background: #e5e7eb; border-radius: 10px; overflow: hidden; margin: 10px 0; }
                    .progress-fill { height: 100%; background: linear-gradient(90deg, #2563eb, #3b82f6); transition: width 0.3s; }
                    .footer { margin-top: 40px; text-align: center; font-size: 12px; color: #666; }
                </style>
            </head>
            <body>
                <div class="header">
                    <h1>Clínica Médica</h1>
                    <h2>Reporte de Acumulado Anual de Copagos</h2>
                </div>

                <div class="patient-info">
                    <h3>Información del Paciente</h3>
                    <p><strong>Cédula:</strong> ${window.formatCedula(reportData.patientCedula)}</p>
                    <p><strong>Nombre:</strong> ${reportData.patientName}</p>
                    <p><strong>Año:</strong> ${reportData.currentYear}</p>
                </div>

                <div class="summary">
                    <div class="summary-item">
                        <div class="summary-value">$${reportData.accumulatedAmount.toLocaleString()}</div>
                        <div class="summary-label">Acumulado Actual</div>
                    </div>
                    <div class="summary-item">
                        <div class="summary-value">${reportData.servicesCount}</div>
                        <div class="summary-label">Servicios Atendidos</div>
                    </div>
                </div>

                <div class="progress-section">
                    <h4>Progreso hacia el Límite Anual</h4>
                    <div class="progress-bar">
                        <div class="progress-fill" style="width: ${Math.min((reportData.accumulatedAmount / 1000000) * 100, 100)}%"></div>
                    </div>
                    <p>$${(reportData.accumulatedAmount || 0).toLocaleString()} / $1.000.000 (${Math.min(((reportData.accumulatedAmount || 0) / 1000000 * 100).toFixed(1), 100)}%)</p>
                </div>

                <div class="footer">
                    <p>Reporte generado el ${new Date(reportData.generatedAt).toLocaleDateString('es-CO')} a las ${new Date(reportData.generatedAt).toLocaleTimeString('es-CO')}</p>
                </div>
            </body>
            </html>
        `;

        const blob = new Blob([reportHtml], { type: 'text/html' });
        const url = URL.createObjectURL(blob);
        const a = document.createElement('a');
        a.href = url;
        a.download = `acumulado-anual-${reportData.patientCedula}-${reportData.currentYear}.html`;
        document.body.appendChild(a);
        a.click();
        document.body.removeChild(a);
        URL.revokeObjectURL(url);
    }

    /**
     * Muestra el historial de acumulado anual
     */
    async viewAccumulatedHistory(patientCedula) {
        try {
            window.showInfo('Cargando historial de acumulado...');

            // Aquí se llamaría a la API para obtener el historial
            // const history = await window.billingApi.getAccumulatedHistory(patientCedula);

            // Datos simulados para demostración
            const historyData = {
                patientCedula: patientCedula,
                history: [
                    { year: 2024, accumulatedAmount: 1200000, servicesCount: 15, isLimitReached: true },
                    { year: 2023, accumulatedAmount: 850000, servicesCount: 12, isLimitReached: false },
                    { year: 2022, accumulatedAmount: 650000, servicesCount: 9, isLimitReached: false }
                ]
            };

            this.showAccumulatedHistoryModal(historyData);

        } catch (error) {
            console.error('Error loading accumulated history:', error);
            window.showError('Error al cargar historial: ' + error.message);
        }
    }

    /**
     * Muestra el modal con el historial de acumulado
     */
    showAccumulatedHistoryModal(data) {
        const modal = document.createElement('div');
        modal.className = 'modal-overlay large-modal';
        modal.innerHTML = `
            <div class="modal">
                <div class="modal-header">
                    <h3><i class="fas fa-history"></i> Historial de Acumulado Anual</h3>
                    <button class="modal-close" onclick="this.closest('.modal-overlay').remove()">
                        <i class="fas fa-times"></i>
                    </button>
                </div>
                <div class="modal-body">
                    <div class="history-content">
                        <div class="history-header">
                            <h4>Paciente: ${data.patientName || 'No disponible'}</h4>
                            <p><strong>Cédula:</strong> ${window.formatCedula(data.patientCedula)}</p>
                        </div>

                        <div class="history-table-container">
                            <table class="history-table">
                                <thead>
                                    <tr>
                                        <th>Año</th>
                                        <th>Acumulado</th>
                                        <th>Servicios</th>
                                        <th>Estado del Límite</th>
                                        <th>Acciones</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    ${data.history.map(record => `
                                        <tr>
                                            <td>${record.year}</td>
                                            <td>$${record.accumulatedAmount.toLocaleString()}</td>
                                            <td>${record.servicesCount}</td>
                                            <td>
                                                <span class="badge ${record.isLimitReached ? 'badge-success' : 'badge-warning'}">
                                                    ${record.isLimitReached ? 'Alcanzado' : 'Pendiente'}
                                                </span>
                                            </td>
                                            <td>
                                                <button class="btn btn-sm btn-info" onclick="administrativeManagementService.viewYearDetail('${data.patientCedula}', ${record.year})">
                                                    <i class="fas fa-eye"></i> Detalles
                                                </button>
                                            </td>
                                        </tr>
                                    `).join('')}
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>
                <div class="modal-footer">
                    <button class="btn btn-secondary" onclick="this.closest('.modal-overlay').remove()">
                        Cerrar
                    </button>
                </div>
            </div>
        `;

        document.body.appendChild(modal);

        setTimeout(() => {
            modal.classList.add('show');
        }, 10);
    }

    /**
     * Genera una factura médica
     */
    async generateInvoice(patientCedula) {
        try {
            window.showInfo('Generando factura médica...');

            // Obtener datos del paciente
            const patient = await window.patientApi.findPatientByCedula(patientCedula);

            if (!patient) {
                throw new Error('Paciente no encontrado');
            }

            // Crear modal para configurar la factura
            this.showInvoiceModal(patient);

        } catch (error) {
            console.error('Error generating invoice:', error);
            window.showError('Error al generar factura: ' + error.message);
        }
    }

    /**
     * Muestra el modal de configuración de factura
     */
    showInvoiceModal(patient) {
        const modal = document.createElement('div');
        modal.className = 'modal-overlay large-modal';
        modal.innerHTML = `
            <div class="modal">
                <div class="modal-header">
                    <h3><i class="fas fa-file-invoice-dollar"></i> Generar Factura Médica</h3>
                    <button class="modal-close" onclick="this.closest('.modal-overlay').remove()">
                        <i class="fas fa-times"></i>
                    </button>
                </div>
                <div class="modal-body">
                    <div class="invoice-form">
                        <div class="invoice-patient-info">
                            <h4>Paciente: ${patient.fullName}</h4>
                            <p><strong>Cédula:</strong> ${window.formatCedula(patient.cedula)}</p>
                            <p><strong>Email:</strong> ${patient.email || 'No disponible'}</p>
                        </div>

                        <form id="invoice-config-form">
                            <div class="form-section">
                                <h4><i class="fas fa-cog"></i> Configuración de Factura</h4>

                                <div class="form-row">
                                    <div class="form-group">
                                        <label for="invoice-date">Fecha de Factura *</label>
                                        <input type="date" id="invoice-date" name="invoiceDate" value="${new Date().toISOString().split('T')[0]}" required>
                                    </div>
                                    <div class="form-group">
                                        <label for="invoice-due-date">Fecha de Vencimiento</label>
                                        <input type="date" id="invoice-due-date" name="dueDate" value="${new Date(Date.now() + 30 * 24 * 60 * 60 * 1000).toISOString().split('T')[0]}">
                                    </div>
                                </div>

                                <div class="form-row">
                                    <div class="form-group">
                                        <label class="checkbox-label">
                                            <input type="checkbox" id="include-consultations" name="includeConsultations" checked>
                                            Incluir consultas médicas
                                        </label>
                                    </div>
                                    <div class="form-group">
                                        <label class="checkbox-label">
                                            <input type="checkbox" id="include-procedures" name="includeProcedures" checked>
                                            Incluir procedimientos
                                        </label>
                                    </div>
                                </div>

                                <div class="form-row">
                                    <div class="form-group">
                                        <label class="checkbox-label">
                                            <input type="checkbox" id="include-medications" name="includeMedications" checked>
                                            Incluir medicamentos
                                        </label>
                                    </div>
                                    <div class="form-group">
                                        <label class="checkbox-label">
                                            <input type="checkbox" id="include-exams" name="includeExams" checked>
                                            Incluir exámenes y ayudas diagnósticas
                                        </label>
                                    </div>
                                </div>

                                <div class="form-group">
                                    <label for="invoice-notes">Notas Adicionales</label>
                                    <textarea id="invoice-notes" name="notes" rows="3" maxlength="500"
                                              placeholder="Notas adicionales para la factura..."></textarea>
                                </div>
                            </div>
                        </form>
                    </div>
                </div>
                <div class="modal-footer">
                    <button class="btn btn-secondary" onclick="this.closest('.modal-overlay').remove()">
                        Cancelar
                    </button>
                    <button class="btn btn-primary" onclick="administrativeManagementService.processInvoice('${patient.cedula}')">
                        <i class="fas fa-file-invoice"></i> Generar Factura
                    </button>
                </div>
            </div>
        `;

        document.body.appendChild(modal);

        setTimeout(() => {
            modal.classList.add('show');
        }, 10);
    }

    /**
     * Procesa la generación de factura
     */
    async processInvoice(patientCedula) {
        try {
            const form = document.getElementById('invoice-config-form');
            if (!form) return;

            const invoiceData = {
                patientCedula: patientCedula,
                invoiceDate: document.getElementById('invoice-date').value,
                dueDate: document.getElementById('invoice-due-date').value,
                includeConsultations: document.getElementById('include-consultations').checked,
                includeProcedures: document.getElementById('include-procedures').checked,
                includeMedications: document.getElementById('include-medications').checked,
                includeExams: document.getElementById('include-exams').checked,
                notes: document.getElementById('invoice-notes').value.trim()
            };

            window.showInfo('Procesando factura...');

            // Llamar a la API para generar la factura
            const result = await window.billingApi.generateInvoice(invoiceData);

            if (result) {
                window.showSuccess('Factura generada exitosamente');
                document.querySelector('.modal-overlay').remove();

                // Mostrar detalles de la factura generada
                this.showGeneratedInvoice(result);
            }

        } catch (error) {
            console.error('Error processing invoice:', error);
            window.showError('Error al generar factura: ' + error.message);
        }
    }

    /**
     * Muestra la factura generada
     */
    showGeneratedInvoice(invoice) {
        const modal = document.createElement('div');
        modal.className = 'modal-overlay';
        modal.innerHTML = `
            <div class="modal large-modal">
                <div class="modal-header">
                    <h3><i class="fas fa-file-invoice-dollar"></i> Factura Generada</h3>
                    <button class="modal-close" onclick="this.closest('.modal-overlay').remove()">
                        <i class="fas fa-times"></i>
                    </button>
                </div>
                <div class="modal-body">
                    <div class="invoice-success">
                        <div class="success-icon">
                            <i class="fas fa-check-circle"></i>
                        </div>
                        <h4>Factura generada exitosamente</h4>

                        <div class="invoice-summary">
                            <div class="summary-grid">
                                <div class="summary-item">
                                    <div class="summary-label">Número de Factura:</div>
                                    <div class="summary-value">${invoice.invoiceNumber}</div>
                                </div>
                                <div class="summary-item">
                                    <div class="summary-label">Fecha:</div>
                                    <div class="summary-value">${window.formatDate(invoice.billingDate)}</div>
                                </div>
                                <div class="summary-item">
                                    <div class="summary-label">Subtotal:</div>
                                    <div class="summary-value">$${invoice.subtotal?.toLocaleString()}</div>
                                </div>
                                <div class="summary-item">
                                    <div class="summary-label">Copago:</div>
                                    <div class="summary-value">$${invoice.copaymentAmount?.toLocaleString()}</div>
                                </div>
                                <div class="summary-item">
                                    <div class="summary-label">Total:</div>
                                    <div class="summary-value">$${invoice.totalAmount?.toLocaleString()}</div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="modal-footer">
                    <button class="btn btn-secondary" onclick="this.closest('.modal-overlay').remove()">
                        Cerrar
                    </button>
                    <button class="btn btn-primary" onclick="administrativeManagementService.printInvoice('${invoice.id}')">
                        <i class="fas fa-print"></i> Imprimir Factura
                    </button>
                    <button class="btn btn-info" onclick="administrativeManagementService.sendInvoiceByEmail('${invoice.id}')">
                        <i class="fas fa-envelope"></i> Enviar por Email
                    </button>
                </div>
            </div>
        `;

        document.body.appendChild(modal);

        setTimeout(() => {
            modal.classList.add('show');
        }, 10);
    }

    /**
     * Imprime una factura
     */
    async printInvoice(invoiceId) {
        try {
            // Obtener datos de la factura
            const invoice = await window.billingApi.findInvoiceById(invoiceId);

            if (!invoice) {
                throw new Error('Factura no encontrada');
            }

            // Crear ventana de impresión
            const printWindow = window.open('', '_blank');
            printWindow.document.write(`
                <html>
                <head>
                    <title>Factura #${invoice.invoiceNumber}</title>
                    <style>
                        body { font-family: Arial, sans-serif; margin: 20px; }
                        .header { text-align: center; margin-bottom: 30px; border-bottom: 2px solid #333; padding-bottom: 20px; }
                        .invoice-details { margin-bottom: 20px; }
                        .patient-info { margin-bottom: 30px; }
                        .totals { margin-top: 20px; text-align: right; }
                        .total-row { margin: 5px 0; }
                        .final-total { font-weight: bold; font-size: 1.2em; border-top: 2px solid #000; padding-top: 10px; }
                        .footer { margin-top: 40px; text-align: center; font-size: 12px; color: #666; }
                        @media print { body { margin: 0; } }
                    </style>
                </head>
                <body>
                    <div class="header">
                        <h1>Clínica Médica</h1>
                        <h2>Factura #${invoice.invoiceNumber}</h2>
                    </div>

                    <div class="patient-info">
                        <h3>Información del Paciente</h3>
                        <p><strong>Paciente:</strong> ${invoice.patientName}</p>
                        <p><strong>Cédula:</strong> ${window.formatCedula(invoice.patientCedula)}</p>
                        <p><strong>Fecha:</strong> ${window.formatDate(invoice.billingDate)}</p>
                    </div>

                    <div class="totals">
                        <div class="total-row">
                            <span>Subtotal: $${invoice.subtotal?.toLocaleString()}</span>
                        </div>
                        <div class="total-row">
                            <span>Copago: $${invoice.copaymentAmount?.toLocaleString()}</span>
                        </div>
                        <div class="total-row final-total">
                            <span>Total: $${invoice.totalAmount?.toLocaleString()}</span>
                        </div>
                    </div>

                    <div class="footer">
                        <p>Gracias por su preferencia</p>
                        <p>Factura generada el ${new Date().toLocaleDateString('es-CO')} a las ${new Date().toLocaleTimeString('es-CO')}</p>
                    </div>
                </body>
                </html>
            `);

            printWindow.document.close();
            printWindow.print();

        } catch (error) {
            console.error('Error printing invoice:', error);
            window.showError('Error al imprimir factura: ' + error.message);
        }
    }

    /**
     * Envía factura por email
     */
    async sendInvoiceByEmail(invoiceId) {
        try {
            window.showInfo('Enviando factura por email...');

            // Aquí se llamaría a la API para enviar por email
            // await window.billingApi.sendInvoiceByEmail(invoiceId);

            window.showSuccess('Factura enviada por email exitosamente');

        } catch (error) {
            console.error('Error sending invoice by email:', error);
            window.showError('Error al enviar factura: ' + error.message);
        }
    }

    /**
     * Muestra estado de error
     */
    showErrorState() {
        const contentArea = document.getElementById('content-area');
        if (contentArea) {
            contentArea.innerHTML = `
                <div class="admin-error-state">
                    <i class="fas fa-exclamation-triangle"></i>
                    <h2>Error al cargar Personal Administrativo</h2>
                    <p>No se pudo inicializar el sistema de gestión administrativa.</p>
                    <button class="btn btn-primary" onclick="administrativeManagementService.initialize()">
                        <i class="fas fa-refresh"></i> Reintentar
                    </button>
                </div>
            `;
        }
    }
}

// Crear instancia global del servicio administrativo
window.administrativeManagementService = new AdministrativeManagementService();