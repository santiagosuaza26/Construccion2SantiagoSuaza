/**
 * Servicio de Gestión de Pacientes
 * Maneja las operaciones CRUD de pacientes para personal administrativo
 */
class PatientManagementService {
    constructor() {
        this.currentPatient = null;
        this.patients = [];
        this.filteredPatients = [];
        this.currentPage = 1;
        this.itemsPerPage = 10;
        this.searchTerm = '';
    }

    /**
     * Carga la lista de pacientes
     */
    async loadPatientsList() {
        try {
            window.showInfo('Cargando pacientes...');

            const response = await window.patientApi.findAllPatients();

            if (response && Array.isArray(response)) {
                this.patients = response;
                this.filteredPatients = [...this.patients];
                this.renderPatientsList();
                window.showSuccess(`Se cargaron ${this.patients.length} pacientes`);
            } else {
                throw new Error('Formato de respuesta inválido');
            }

        } catch (error) {
            console.error('Error loading patients:', error);
            window.showError('Error al cargar pacientes: ' + error.message);
            this.showEmptyState();
        }
    }

    /**
     * Renderiza la lista de pacientes
     */
    renderPatientsList() {
        const contentArea = document.getElementById('content-area');
        if (!contentArea) return;

        const startIndex = (this.currentPage - 1) * this.itemsPerPage;
        const endIndex = startIndex + this.itemsPerPage;
        const patientsToShow = this.filteredPatients.slice(startIndex, endIndex);

        contentArea.innerHTML = `
            <div class="content-header">
                <h2><i class="fas fa-user-injured"></i> Gestión de Pacientes</h2>
                <p>Administra la información de los pacientes registrados</p>
            </div>

            <div class="content-actions">
                <div class="search-box">
                    <i class="fas fa-search search-icon"></i>
                    <input type="text" id="patient-search" class="search-input"
                           placeholder="Buscar por nombre, cédula o email..."
                           value="${this.searchTerm}">
                </div>
                <button class="btn btn-primary" onclick="patientManagementService.showCreatePatientModal()">
                    <i class="fas fa-plus"></i> Registrar Paciente
                </button>
            </div>

            <div class="stats-grid">
                <div class="stat-card">
                    <div class="stat-value">${this.patients.length}</div>
                    <div class="stat-label">Total Pacientes</div>
                </div>
                <div class="stat-card">
                    <div class="stat-value">${this.getPatientsWithInsuranceCount()}</div>
                    <div class="stat-label">Con Seguro</div>
                </div>
                <div class="stat-card">
                    <div class="stat-value">${this.getActivePatientsCount()}</div>
                    <div class="stat-label">Activos</div>
                </div>
                <div class="stat-card">
                    <div class="stat-value">${this.getPatientsByGenderCount('FEMENINO')}</div>
                    <div class="stat-label">Mujeres</div>
                </div>
            </div>

            <div class="table-container">
                <table class="table">
                    <thead>
                        <tr>
                            <th>Cédula</th>
                            <th>Nombre</th>
                            <th>Email</th>
                            <th>Teléfono</th>
                            <th>Género</th>
                            <th>Seguro</th>
                            <th>Acciones</th>
                        </tr>
                    </thead>
                    <tbody id="patients-table-body">
                        ${this.renderPatientsTableRows(patientsToShow)}
                    </tbody>
                </table>
            </div>

            ${this.renderPagination()}

            <!-- Modal para crear/editar paciente -->
            <div id="patient-modal" class="modal-overlay" style="display: none;">
                <div class="modal">
                    <div class="modal-header">
                        <h3 id="patient-modal-title">Registrar Paciente</h3>
                        <button class="modal-close" onclick="patientManagementService.closePatientModal()">
                            <i class="fas fa-times"></i>
                        </button>
                    </div>
                    <div class="modal-body">
                        <form id="patient-form">
                            ${this.renderPatientForm()}
                        </form>
                    </div>
                    <div class="modal-footer">
                        <button class="btn btn-secondary" onclick="patientManagementService.closePatientModal()">
                            Cancelar
                        </button>
                        <button class="btn btn-primary" onclick="patientManagementService.savePatient()">
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
     * Renderiza las filas de la tabla de pacientes
     */
    renderPatientsTableRows(patients) {
        if (patients.length === 0) {
            return `
                <tr>
                    <td colspan="7" class="text-center" style="padding: 2rem;">
                        <i class="fas fa-user-injured" style="font-size: 3rem; color: var(--text-secondary); margin-bottom: 1rem;"></i>
                        <p style="color: var(--text-secondary);">No se encontraron pacientes</p>
                    </td>
                </tr>
            `;
        }

        return patients.map(patient => `
            <tr>
                <td>${window.formatCedula(patient.cedula)}</td>
                <td>${patient.fullName}</td>
                <td>${patient.email}</td>
                <td>${window.formatPhoneNumber(patient.phoneNumber)}</td>
                <td>
                    <span class="badge ${patient.gender === 'MASCULINO' ? 'badge-primary' : 'badge-info'}">
                        ${patient.gender === 'MASCULINO' ? 'Masculino' : 'Femenino'}
                    </span>
                </td>
                <td>
                    <span class="badge ${patient.insurancePolicy && patient.insurancePolicy.isActive ? 'badge-success' : 'badge-secondary'}">
                        ${patient.insurancePolicy && patient.insurancePolicy.isActive ? 'Activo' : 'Sin Seguro'}
                    </span>
                </td>
                <td>
                    <div class="btn-group">
                        <button class="btn btn-sm btn-primary"
                                onclick="patientManagementService.editPatient('${patient.cedula}')"
                                title="Editar">
                            <i class="fas fa-edit"></i>
                        </button>
                        <button class="btn btn-sm btn-info"
                                onclick="patientManagementService.viewPatient('${patient.cedula}')"
                                title="Ver Detalles">
                            <i class="fas fa-eye"></i>
                        </button>
                        <button class="btn btn-sm btn-danger"
                                onclick="patientManagementService.deletePatient('${patient.cedula}')"
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
        const totalPages = Math.ceil(this.filteredPatients.length / this.itemsPerPage);

        if (totalPages <= 1) return '';

        let paginationHtml = `
            <div class="pagination">
        `;

        // Botón anterior
        if (this.currentPage > 1) {
            paginationHtml += `<button class="pagination-btn" onclick="patientManagementService.goToPage(${this.currentPage - 1})">
                <i class="fas fa-chevron-left"></i>
            </button>`;
        }

        // Páginas
        for (let i = 1; i <= totalPages; i++) {
            if (i === this.currentPage) {
                paginationHtml += `<button class="pagination-btn active">${i}</button>`;
            } else if (i >= this.currentPage - 2 && i <= this.currentPage + 2) {
                paginationHtml += `<button class="pagination-btn" onclick="patientManagementService.goToPage(${i})">${i}</button>`;
            }
        }

        // Botón siguiente
        if (this.currentPage < totalPages) {
            paginationHtml += `<button class="pagination-btn" onclick="patientManagementService.goToPage(${this.currentPage + 1})">
                <i class="fas fa-chevron-right"></i>
            </button>`;
        }

        paginationHtml += '</div>';
        return paginationHtml;
    }

    /**
     * Renderiza el formulario de paciente
     */
    renderPatientForm() {
        return `
            <div class="form-row">
                <div class="form-group">
                    <label for="patient-cedula">Cédula *</label>
                    <input type="text" id="patient-cedula" name="cedula" required maxlength="20">
                </div>
                <div class="form-group">
                    <label for="patient-username">Nombre de Usuario *</label>
                    <input type="text" id="patient-username" name="username" required maxlength="15">
                </div>
            </div>

            <div class="form-row">
                <div class="form-group">
                    <label for="patient-fullname">Nombre Completo *</label>
                    <input type="text" id="patient-fullname" name="fullName" required maxlength="100">
                </div>
                <div class="form-group">
                    <label for="patient-birthdate">Fecha de Nacimiento *</label>
                    <input type="date" id="patient-birthdate" name="birthDate" required>
                </div>
            </div>

            <div class="form-row">
                <div class="form-group">
                    <label for="patient-gender">Género *</label>
                    <select id="patient-gender" name="gender" required>
                        <option value="">Seleccionar género</option>
                        <option value="MASCULINO">Masculino</option>
                        <option value="FEMENINO">Femenino</option>
                    </select>
                </div>
                <div class="form-group">
                    <label for="patient-phone">Teléfono *</label>
                    <input type="tel" id="patient-phone" name="phoneNumber" required maxlength="10">
                </div>
            </div>

            <div class="form-row">
                <div class="form-group">
                    <label for="patient-email">Correo Electrónico *</label>
                    <input type="email" id="patient-email" name="email" required>
                </div>
                <div class="form-group">
                    <label for="patient-address">Dirección *</label>
                    <input type="text" id="patient-address" name="address" required maxlength="30">
                </div>
            </div>

            <div class="form-row">
                <div class="form-group">
                    <label for="patient-password">Contraseña *</label>
                    <input type="password" id="patient-password" name="password" required minlength="8">
                </div>
                <div class="form-group">
                    <label for="patient-confirm-password">Confirmar Contraseña *</label>
                    <input type="password" id="patient-confirm-password" name="confirmPassword" required minlength="8">
                </div>
            </div>

            <div class="form-section">
                <h4><i class="fas fa-address-book"></i> Información de Emergencia</h4>
                <div class="form-row">
                    <div class="form-group">
                        <label for="emergency-name">Nombre de Contacto *</label>
                        <input type="text" id="emergency-name" name="emergencyName" required maxlength="100">
                    </div>
                    <div class="form-group">
                        <label for="emergency-relationship">Relación *</label>
                        <select id="emergency-relationship" name="emergencyRelationship" required>
                            <option value="">Seleccionar relación</option>
                            <option value="Padre">Padre</option>
                            <option value="Madre">Madre</option>
                            <option value="Esposo">Esposo</option>
                            <option value="Esposa">Esposa</option>
                            <option value="Hijo">Hijo</option>
                            <option value="Hija">Hija</option>
                            <option value="Hermano">Hermano</option>
                            <option value="Hermana">Hermana</option>
                            <option value="Otro Familiar">Otro Familiar</option>
                            <option value="Amigo">Amigo</option>
                        </select>
                    </div>
                </div>
                <div class="form-row">
                    <div class="form-group">
                        <label for="emergency-phone">Teléfono de Emergencia *</label>
                        <input type="tel" id="emergency-phone" name="emergencyPhone" required maxlength="10">
                    </div>
                </div>
            </div>

            <div class="form-section">
                <h4><i class="fas fa-shield-alt"></i> Información de Seguro Médico</h4>
                <div class="form-row">
                    <div class="form-group">
                        <label for="insurance-company">Compañía de Seguros</label>
                        <select id="insurance-company" name="insuranceCompany">
                            <option value="">Seleccionar compañía</option>
                            <option value="Sura EPS">Sura EPS</option>
                            <option value="Seguros Bolívar">Seguros Bolívar</option>
                            <option value="Coomeva EPS">Coomeva EPS</option>
                            <option value="Compensar EPS">Compensar EPS</option>
                            <option value="Nueva EPS">Nueva EPS</option>
                            <option value="Otro">Otro</option>
                        </select>
                    </div>
                    <div class="form-group">
                        <label for="policy-number">Número de Póliza</label>
                        <input type="text" id="policy-number" name="policyNumber" maxlength="20">
                    </div>
                </div>
                <div class="form-row">
                    <div class="form-group">
                        <label for="policy-expiration">Fecha de Vencimiento</label>
                        <input type="date" id="policy-expiration" name="policyExpiration">
                    </div>
                    <div class="form-group">
                        <label class="checkbox-label">
                            <input type="checkbox" id="policy-active" name="policyActive" checked>
                            Póliza Activa
                        </label>
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
        const searchInput = document.getElementById('patient-search');
        if (searchInput) {
            searchInput.addEventListener('input', window.debounce((e) => {
                this.searchTerm = e.target.value;
                this.filterPatients();
            }, 300));
        }

        // Validación del formulario
        const patientForm = document.getElementById('patient-form');
        if (patientForm) {
            patientForm.addEventListener('submit', (e) => {
                e.preventDefault();
                this.savePatient();
            });
        }
    }

    /**
     * Filtra pacientes según término de búsqueda
     */
    filterPatients() {
        if (!this.searchTerm) {
            this.filteredPatients = [...this.patients];
        } else {
            const term = this.searchTerm.toLowerCase();
            this.filteredPatients = this.patients.filter(patient =>
                patient.fullName.toLowerCase().includes(term) ||
                patient.cedula.includes(term) ||
                patient.email.toLowerCase().includes(term)
            );
        }

        this.currentPage = 1;
        this.renderPatientsList();
    }

    /**
     * Muestra el modal para crear paciente
     */
    showCreatePatientModal() {
        this.currentPatient = null;
        this.showPatientModal();
    }

    /**
     * Muestra el modal para editar paciente
     */
    async editPatient(cedula) {
        try {
            window.showInfo('Cargando paciente...');

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
            console.error('Error loading patient:', error);
            window.showError('Error al cargar paciente: ' + error.message);
        }
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
     * Llena el formulario con datos del paciente
     */
    populatePatientForm(patient) {
        document.getElementById('patient-modal-title').textContent = 'Editar Paciente';

        document.getElementById('patient-cedula').value = patient.cedula;
        document.getElementById('patient-username').value = patient.username;
        document.getElementById('patient-fullname').value = patient.fullName;
        document.getElementById('patient-email').value = patient.email;
        document.getElementById('patient-phone').value = patient.phoneNumber;
        document.getElementById('patient-address').value = patient.address;
        document.getElementById('patient-gender').value = patient.gender;

        // Formatear fecha de nacimiento
        if (patient.birthDate) {
            const birthDate = new Date(patient.birthDate);
            document.getElementById('patient-birthdate').value = birthDate.toISOString().split('T')[0];
        }

        // Información de emergencia
        if (patient.emergencyContact) {
            document.getElementById('emergency-name').value = patient.emergencyContact.name;
            document.getElementById('emergency-relationship').value = patient.emergencyContact.relationship;
            document.getElementById('emergency-phone').value = patient.emergencyContact.phoneNumber;
        }

        // Información de seguro
        if (patient.insurancePolicy) {
            document.getElementById('insurance-company').value = patient.insurancePolicy.companyName;
            document.getElementById('policy-number').value = patient.insurancePolicy.policyNumber;
            document.getElementById('policy-active').checked = patient.insurancePolicy.isActive;

            if (patient.insurancePolicy.expirationDate) {
                const expirationDate = new Date(patient.insurancePolicy.expirationDate);
                document.getElementById('policy-expiration').value = expirationDate.toISOString().split('T')[0];
            }
        }

        // Limpiar campos de contraseña para edición
        document.getElementById('patient-password').value = '';
        document.getElementById('patient-confirm-password').value = '';
        document.getElementById('patient-password').required = false;
        document.getElementById('patient-confirm-password').required = false;
    }

    /**
     * Guarda el paciente (crear o actualizar)
     */
    async savePatient() {
        const form = document.getElementById('patient-form');
        if (!form) return;

        // Validar formulario
        if (!this.validatePatientForm()) {
            return;
        }

        const patientData = this.getFormData();

        try {
            window.showInfo(this.currentPatient ? 'Actualizando paciente...' : 'Registrando paciente...');

            let result;
            if (this.currentPatient) {
                result = await window.patientApi.updatePatient(this.currentPatient.cedula, patientData);
            } else {
                result = await window.patientApi.registerPatient(patientData);
            }

            if (result) {
                window.showSuccess(`Paciente ${this.currentPatient ? 'actualizado' : 'registrado'} exitosamente`);
                this.closePatientModal();
                this.loadPatientsList(); // Recargar lista
            }

        } catch (error) {
            console.error('Error saving patient:', error);
            window.showError('Error al guardar paciente: ' + error.message);
        }
    }

    /**
     * Valida el formulario de paciente
     */
    validatePatientForm() {
        const cedula = document.getElementById('patient-cedula').value.trim();
        const username = document.getElementById('patient-username').value.trim();
        const fullName = document.getElementById('patient-fullname').value.trim();
        const email = document.getElementById('patient-email').value.trim();
        const phone = document.getElementById('patient-phone').value.trim();
        const address = document.getElementById('patient-address').value.trim();
        const gender = document.getElementById('patient-gender').value;
        const password = document.getElementById('patient-password').value;
        const confirmPassword = document.getElementById('patient-confirm-password').value;

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

        if (!gender) {
            window.showError('Género es requerido');
            return false;
        }

        // Validar contraseña solo si es creación o si se proporciona
        if (!this.currentPatient || password) {
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
            cedula: document.getElementById('patient-cedula').value.trim(),
            username: document.getElementById('patient-username').value.trim(),
            fullName: document.getElementById('patient-fullname').value.trim(),
            email: document.getElementById('patient-email').value.trim(),
            phoneNumber: document.getElementById('patient-phone').value.trim(),
            address: document.getElementById('patient-address').value.trim(),
            gender: document.getElementById('patient-gender').value,
            birthDate: document.getElementById('patient-birthdate').value,
            emergencyContact: {
                name: document.getElementById('emergency-name').value.trim(),
                relationship: document.getElementById('emergency-relationship').value,
                phoneNumber: document.getElementById('emergency-phone').value.trim()
            }
        };

        // Incluir contraseña solo si se proporciona
        const password = document.getElementById('patient-password').value;
        if (password) {
            data.password = password;
        }

        // Información de seguro médico
        const insuranceCompany = document.getElementById('insurance-company').value;
        if (insuranceCompany) {
            data.insurancePolicy = {
                companyName: insuranceCompany,
                policyNumber: document.getElementById('policy-number').value.trim(),
                isActive: document.getElementById('policy-active').checked,
                expirationDate: document.getElementById('policy-expiration').value || null
            };
        }

        return data;
    }

    /**
     * Ver detalles del paciente
     */
    async viewPatient(cedula) {
        try {
            window.showInfo('Cargando detalles del paciente...');

            const patient = await window.patientApi.findPatientByCedula(cedula);

            if (patient) {
                this.showPatientDetailsModal(patient);
            } else {
                throw new Error('Paciente no encontrado');
            }

        } catch (error) {
            console.error('Error loading patient details:', error);
            window.showError('Error al cargar detalles del paciente: ' + error.message);
        }
    }

    /**
     * Muestra modal con detalles del paciente
     */
    showPatientDetailsModal(patient) {
        const modal = document.createElement('div');
        modal.className = 'modal-overlay';
        modal.innerHTML = `
            <div class="modal">
                <div class="modal-header">
                    <h3>Detalles del Paciente</h3>
                    <button class="modal-close" onclick="this.closest('.modal-overlay').remove()">
                        <i class="fas fa-times"></i>
                    </button>
                </div>
                <div class="modal-body">
                    <div class="patient-details">
                        <div class="detail-section">
                            <h4>Información Personal</h4>
                            <p><strong>Cédula:</strong> ${window.formatCedula(patient.cedula)}</p>
                            <p><strong>Nombre:</strong> ${patient.fullName}</p>
                            <p><strong>Usuario:</strong> ${patient.username}</p>
                            <p><strong>Email:</strong> ${patient.email}</p>
                            <p><strong>Teléfono:</strong> ${window.formatPhoneNumber(patient.phoneNumber)}</p>
                            <p><strong>Dirección:</strong> ${patient.address}</p>
                            <p><strong>Género:</strong> ${patient.gender === 'MASCULINO' ? 'Masculino' : 'Femenino'}</p>
                            <p><strong>Fecha de Nacimiento:</strong> ${window.formatDate(patient.birthDate)}</p>
                        </div>
                        ${patient.emergencyContact ? `
                            <div class="detail-section">
                                <h4>Contacto de Emergencia</h4>
                                <p><strong>Nombre:</strong> ${patient.emergencyContact.name}</p>
                                <p><strong>Relación:</strong> ${patient.emergencyContact.relationship}</p>
                                <p><strong>Teléfono:</strong> ${window.formatPhoneNumber(patient.emergencyContact.phoneNumber)}</p>
                            </div>
                        ` : ''}
                        ${patient.insurancePolicy ? `
                            <div class="detail-section">
                                <h4>Seguro Médico</h4>
                                <p><strong>Compañía:</strong> ${patient.insurancePolicy.companyName}</p>
                                <p><strong>Número de Póliza:</strong> ${patient.insurancePolicy.policyNumber}</p>
                                <p><strong>Estado:</strong> ${patient.insurancePolicy.isActive ? 'Activo' : 'Inactivo'}</p>
                                ${patient.insurancePolicy.expirationDate ? `<p><strong>Vencimiento:</strong> ${window.formatDate(patient.insurancePolicy.expirationDate)}</p>` : ''}
                            </div>
                        ` : ''}
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
     * Elimina un paciente
     */
    async deletePatient(cedula) {
        if (!confirm('¿Está seguro de que desea eliminar este paciente? Esta acción no se puede deshacer.')) {
            return;
        }

        try {
            window.showInfo('Eliminando paciente...');

            await window.patientApi.deletePatientByCedula(cedula);

            window.showSuccess('Paciente eliminado exitosamente');
            this.loadPatientsList(); // Recargar lista

        } catch (error) {
            console.error('Error deleting patient:', error);
            window.showError('Error al eliminar paciente: ' + error.message);
        }
    }

    /**
     * Cambia de página
     */
    goToPage(page) {
        this.currentPage = page;
        this.renderPatientsList();
    }

    /**
     * Obtiene el conteo de pacientes activos
     */
    getActivePatientsCount() {
        return this.patients.length; // Por ahora todos están activos
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
     * Obtiene el conteo de pacientes por género
     */
    getPatientsByGenderCount(gender) {
        return this.patients.filter(patient => patient.gender === gender).length;
    }

    /**
     * Muestra estado vacío
     */
    showEmptyState() {
        const contentArea = document.getElementById('content-area');
        if (contentArea) {
            contentArea.innerHTML = `
                <div class="empty-state">
                    <i class="fas fa-user-injured"></i>
                    <h3>No se pudieron cargar los pacientes</h3>
                    <p>Verifica tu conexión a internet e intenta nuevamente.</p>
                    <button class="btn btn-primary" onclick="patientManagementService.loadPatientsList()">
                        <i class="fas fa-refresh"></i> Reintentar
                    </button>
                </div>
            `;
        }
    }
}

// Crear instancia global del servicio de gestión de pacientes
window.patientManagementService = new PatientManagementService();