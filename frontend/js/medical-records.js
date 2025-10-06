/**
 * Servicio de Historias Clínicas
 * Maneja las operaciones de historias clínicas médicas para doctores
 */
class MedicalRecordsService {
    constructor() {
        this.currentRecord = null;
        this.medicalRecords = [];
        this.filteredRecords = [];
        this.currentPage = 1;
        this.itemsPerPage = 10;
        this.searchTerm = '';
        this.selectedPatient = null;
    }

    /**
     * Carga la lista de pacientes para seleccionar
     */
    async loadPatientsList() {
        try {
            window.showInfo('Cargando pacientes...');

            const response = await window.patientApi.findAllPatients();

            if (response && Array.isArray(response)) {
                this.patients = response;
                this.renderPatientsSelection();
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
     * Carga las historias clínicas de un paciente
     */
    async loadMedicalRecords(patientCedula) {
        try {
            window.showInfo('Cargando historias clínicas...');

            const response = await window.medicalRecordApi.findMedicalRecordByPatientCedula(patientCedula);

            if (response) {
                this.medicalRecords = Array.isArray(response) ? response : [response];
                this.filteredRecords = [...this.medicalRecords];
                this.currentPage = 1;
                this.renderMedicalRecords();
                window.showSuccess(`Se cargaron ${this.medicalRecords.length} registros médicos`);
            } else {
                this.medicalRecords = [];
                this.filteredRecords = [];
                this.renderMedicalRecords();
                window.showInfo('El paciente no tiene historias clínicas registradas');
            }

        } catch (error) {
            console.error('Error loading medical records:', error);
            window.showError('Error al cargar historias clínicas: ' + error.message);
            this.showEmptyState();
        }
    }

    /**
     * Renderiza la selección de pacientes
     */
    renderPatientsSelection() {
        const contentArea = document.getElementById('content-area');
        if (!contentArea) return;

        contentArea.innerHTML = `
            <div class="content-header">
                <h2><i class="fas fa-file-medical"></i> Historias Clínicas</h2>
                <p>Gestión de historias clínicas digitales</p>
            </div>

            <div class="content-body">
                <div class="widget">
                    <h3>Seleccionar Paciente</h3>
                    <p>Selecciona un paciente para ver o crear su historia clínica</p>

                    <div class="search-box" style="margin-bottom: 1rem;">
                        <i class="fas fa-search search-icon"></i>
                        <input type="text" id="patient-search" class="search-input"
                               placeholder="Buscar paciente por nombre o cédula...">
                    </div>

                    <div class="patients-list" id="patients-list" style="max-height: 400px; overflow-y: auto;">
                        ${this.renderPatientsList()}
                    </div>
                </div>
            </div>
        `;

        this.setupPatientSearch();
    }

    /**
     * Renderiza la lista de pacientes para selección
     */
    renderPatientsList() {
        if (!this.patients || this.patients.length === 0) {
            return `
                <div class="empty-state">
                    <i class="fas fa-user-injured"></i>
                    <p>No hay pacientes registrados</p>
                </div>
            `;
        }

        return this.patients.map(patient => `
            <div class="patient-card" onclick="medicalRecordsService.selectPatient('${patient.cedula}')">
                <div class="patient-info">
                    <h4>${patient.fullName}</h4>
                    <p><strong>Cédula:</strong> ${window.formatCedula(patient.cedula)}</p>
                    <p><strong>Email:</strong> ${patient.email}</p>
                    <p><strong>Teléfono:</strong> ${window.formatPhoneNumber(patient.phoneNumber)}</p>
                </div>
                <div class="patient-actions">
                    <button class="btn btn-sm btn-primary">
                        <i class="fas fa-file-medical"></i> Ver Historia
                    </button>
                </div>
            </div>
        `).join('');
    }

    /**
     * Renderiza las historias clínicas del paciente seleccionado
     */
    renderMedicalRecords() {
        const contentArea = document.getElementById('content-area');
        if (!contentArea) return;

        const patient = this.selectedPatient;

        contentArea.innerHTML = `
            <div class="content-header">
                <h2><i class="fas fa-file-medical"></i> Historia Clínica</h2>
                <p>Paciente: ${patient ? patient.fullName : ''} | Cédula: ${patient ? window.formatCedula(patient.cedula) : ''}</p>
            </div>

            <div class="content-actions">
                <button class="btn btn-secondary" onclick="medicalRecordsService.backToPatientsList()">
                    <i class="fas fa-arrow-left"></i> Volver a Pacientes
                </button>
                <button class="btn btn-primary" onclick="medicalRecordsService.showCreateRecordModal()">
                    <i class="fas fa-plus"></i> Nueva Consulta
                </button>
            </div>

            <div class="stats-grid">
                <div class="stat-card">
                    <div class="stat-value">${this.medicalRecords.length}</div>
                    <div class="stat-label">Total Consultas</div>
                </div>
                <div class="stat-card">
                    <div class="stat-value">${this.getRecordsThisMonth()}</div>
                    <div class="stat-label">Este Mes</div>
                </div>
                <div class="stat-card">
                    <div class="stat-value">${this.getLastRecordDate()}</div>
                    <div class="stat-label">Última Consulta</div>
                </div>
            </div>

            <div class="records-container">
                ${this.filteredRecords.length === 0 ? this.renderEmptyRecords() : this.renderRecordsList()}
            </div>

            ${this.renderPagination()}

            <!-- Modal para crear/editar registro médico -->
            <div id="record-modal" class="modal-overlay" style="display: none;">
                <div class="modal large-modal">
                    <div class="modal-header">
                        <h3 id="record-modal-title">Nueva Consulta Médica</h3>
                        <button class="modal-close" onclick="medicalRecordsService.closeRecordModal()">
                            <i class="fas fa-times"></i>
                        </button>
                    </div>
                    <div class="modal-body">
                        <form id="record-form">
                            ${this.renderRecordForm()}
                        </form>
                    </div>
                    <div class="modal-footer">
                        <button class="btn btn-secondary" onclick="medicalRecordsService.closeRecordModal()">
                            Cancelar
                        </button>
                        <button class="btn btn-primary" onclick="medicalRecordsService.saveRecord()">
                            Guardar Consulta
                        </button>
                    </div>
                </div>
            </div>
        `;

        this.setupEventListeners();
    }

    /**
     * Renderiza la lista de registros médicos
     */
    renderRecordsList() {
        return `
            <div class="records-list">
                ${this.filteredRecords.map(record => `
                    <div class="record-card">
                        <div class="record-header">
                            <div class="record-date">
                                <i class="fas fa-calendar"></i>
                                ${window.formatDate(record.recordDate)}
                            </div>
                            <div class="record-actions">
                                <button class="btn btn-sm btn-primary" onclick="medicalRecordsService.editRecord('${record.id}')">
                                    <i class="fas fa-edit"></i> Editar
                                </button>
                                <button class="btn btn-sm btn-info" onclick="medicalRecordsService.viewRecord('${record.id}')">
                                    <i class="fas fa-eye"></i> Ver
                                </button>
                            </div>
                        </div>
                        <div class="record-body">
                            <div class="record-section">
                                <h4><i class="fas fa-user-md"></i> Consulta</h4>
                                <p><strong>Motivo:</strong> ${record.consultationReason || 'No especificado'}</p>
                                <p><strong>Síntomas:</strong> ${record.symptoms || 'No documentados'}</p>
                            </div>

                            ${record.diagnosis ? `
                                <div class="record-section">
                                    <h4><i class="fas fa-stethoscope"></i> Diagnóstico</h4>
                                    <p>${record.diagnosis}</p>
                                </div>
                            ` : ''}

                            ${record.medicationOrders && record.medicationOrders.length > 0 ? `
                                <div class="record-section">
                                    <h4><i class="fas fa-pills"></i> Medicamentos</h4>
                                    <div class="medications-list">
                                        ${record.medicationOrders.map(med => `
                                            <div class="medication-item">
                                                <span class="medication-name">${med.medicationName}</span>
                                                <span class="medication-dosage">${med.dosage}</span>
                                                <span class="medication-frequency">${med.frequency}</span>
                                            </div>
                                        `).join('')}
                                    </div>
                                </div>
                            ` : ''}

                            ${record.observations ? `
                                <div class="record-section">
                                    <h4><i class="fas fa-clipboard"></i> Observaciones</h4>
                                    <p>${record.observations}</p>
                                </div>
                            ` : ''}
                        </div>
                    </div>
                `).join('')}
            </div>
        `;
    }

    /**
     * Renderiza estado vacío de registros
     */
    renderEmptyRecords() {
        return `
            <div class="empty-state">
                <i class="fas fa-file-medical"></i>
                <h3>No hay consultas registradas</h3>
                <p>Este paciente no tiene consultas médicas registradas en el sistema.</p>
                <button class="btn btn-primary" onclick="medicalRecordsService.showCreateRecordModal()">
                    <i class="fas fa-plus"></i> Crear Primera Consulta
                </button>
            </div>
        `;
    }

    /**
     * Renderiza el formulario de registro médico
     */
    renderRecordForm() {
        return `
            <div class="form-section">
                <h4><i class="fas fa-user-injured"></i> Información del Paciente</h4>
                <div class="form-row">
                    <div class="form-group">
                        <label>Paciente:</label>
                        <input type="text" value="${this.selectedPatient ? this.selectedPatient.fullName : ''}" readonly>
                    </div>
                    <div class="form-group">
                        <label>Cédula:</label>
                        <input type="text" value="${this.selectedPatient ? window.formatCedula(this.selectedPatient.cedula) : ''}" readonly>
                    </div>
                </div>
            </div>

            <div class="form-section">
                <h4><i class="fas fa-stethoscope"></i> Consulta Médica</h4>
                <div class="form-row">
                    <div class="form-group">
                        <label for="consultation-reason">Motivo de Consulta *</label>
                        <select id="consultation-reason" name="consultationReason" required>
                            <option value="">Seleccionar motivo</option>
                            <option value="Consulta General">Consulta General</option>
                            <option value="Control Médico">Control Médico</option>
                            <option value="Emergencia">Emergencia</option>
                            <option value="Seguimiento">Seguimiento</option>
                            <option value="Especialista">Consulta con Especialista</option>
                            <option value="Otro">Otro</option>
                        </select>
                    </div>
                    <div class="form-group">
                        <label for="consultation-date">Fecha de Consulta *</label>
                        <input type="date" id="consultation-date" name="consultationDate" required>
                    </div>
                </div>

                <div class="form-row">
                    <div class="form-group">
                        <label for="symptoms">Síntomas</label>
                        <textarea id="symptoms" name="symptoms" rows="3" placeholder="Describa los síntomas presentados..."></textarea>
                    </div>
                </div>

                <div class="form-row">
                    <div class="form-group">
                        <label for="diagnosis">Diagnóstico</label>
                        <textarea id="diagnosis" name="diagnosis" rows="3" placeholder="Ingrese el diagnóstico médico..."></textarea>
                    </div>
                </div>

                <div class="form-row">
                    <div class="form-group">
                        <label for="observations">Observaciones</label>
                        <textarea id="observations" name="observations" rows="3" placeholder="Observaciones adicionales..."></textarea>
                    </div>
                </div>
            </div>

            <div class="form-section">
                <h4><i class="fas fa-pills"></i> Medicamentos Recetados</h4>
                <div id="medications-container">
                    <div class="medication-form">
                        <div class="form-row">
                            <div class="form-group">
                                <label for="medication-name-1">Medicamento</label>
                                <input type="text" id="medication-name-1" placeholder="Nombre del medicamento">
                            </div>
                            <div class="form-group">
                                <label for="medication-dosage-1">Dosis</label>
                                <input type="text" id="medication-dosage-1" placeholder="Ej: 500mg">
                            </div>
                            <div class="form-group">
                                <label for="medication-frequency-1">Frecuencia</label>
                                <input type="text" id="medication-frequency-1" placeholder="Ej: 2 veces al día">
                            </div>
                        </div>
                    </div>
                </div>
                <button type="button" class="btn btn-secondary" onclick="medicalRecordsService.addMedication()">
                    <i class="fas fa-plus"></i> Agregar Medicamento
                </button>
            </div>

            <div class="form-section">
                <h4><i class="fas fa-flask"></i> Ayudas Diagnósticas</h4>
                <div id="diagnostic-aids-container">
                    <div class="diagnostic-aid-form">
                        <div class="form-row">
                            <div class="form-group">
                                <label for="diagnostic-aid-1">Ayuda Diagnóstica</label>
                                <select id="diagnostic-aid-1">
                                    <option value="">Seleccionar ayuda</option>
                                    <option value="Hemograma">Hemograma</option>
                                    <option value="Química Sanguínea">Química Sanguínea</option>
                                    <option value="Radiografía">Radiografía</option>
                                    <option value="Ecografía">Ecografía</option>
                                    <option value="Tomografía">Tomografía</option>
                                    <option value="Resonancia Magnética">Resonancia Magnética</option>
                                    <option value="Endoscopía">Endoscopía</option>
                                    <option value="Colonoscopía">Colonoscopía</option>
                                </select>
                            </div>
                            <div class="form-group">
                                <label for="diagnostic-observations-1">Observaciones</label>
                                <input type="text" id="diagnostic-observations-1" placeholder="Observaciones de la ayuda diagnóstica">
                            </div>
                        </div>
                    </div>
                </div>
                <button type="button" class="btn btn-secondary" onclick="medicalRecordsService.addDiagnosticAid()">
                    <i class="fas fa-plus"></i> Agregar Ayuda Diagnóstica
                </button>
            </div>
        `;
    }

    /**
     * Configura los event listeners
     */
    setupEventListeners() {
        // Configurar fecha por defecto
        const today = new Date().toISOString().split('T')[0];
        const dateInput = document.getElementById('consultation-date');
        if (dateInput) {
            dateInput.value = today;
        }
    }

    /**
     * Configura la búsqueda de pacientes
     */
    setupPatientSearch() {
        const searchInput = document.getElementById('patient-search');
        if (searchInput) {
            searchInput.addEventListener('input', window.debounce((e) => {
                this.filterPatients(e.target.value);
            }, 300));
        }
    }

    /**
     * Filtra pacientes según término de búsqueda
     */
    filterPatients(term) {
        if (!term) {
            this.renderPatientsSelection();
            return;
        }

        const filteredPatients = this.patients.filter(patient =>
            patient.fullName.toLowerCase().includes(term.toLowerCase()) ||
            patient.cedula.includes(term)
        );

        const patientsList = document.getElementById('patients-list');
        if (patientsList) {
            patientsList.innerHTML = filteredPatients.map(patient => `
                <div class="patient-card" onclick="medicalRecordsService.selectPatient('${patient.cedula}')">
                    <div class="patient-info">
                        <h4>${patient.fullName}</h4>
                        <p><strong>Cédula:</strong> ${window.formatCedula(patient.cedula)}</p>
                        <p><strong>Email:</strong> ${patient.email}</p>
                    </div>
                    <div class="patient-actions">
                        <button class="btn btn-sm btn-primary">
                            <i class="fas fa-file-medical"></i> Ver Historia
                        </button>
                    </div>
                </div>
            `).join('');
        }
    }

    /**
     * Selecciona un paciente
     */
    async selectPatient(cedula) {
        try {
            window.showInfo('Cargando información del paciente...');

            const patient = await window.patientApi.findPatientByCedula(cedula);

            if (patient) {
                this.selectedPatient = patient;
                await this.loadMedicalRecords(cedula);
            } else {
                throw new Error('Paciente no encontrado');
            }

        } catch (error) {
            console.error('Error selecting patient:', error);
            window.showError('Error al seleccionar paciente: ' + error.message);
        }
    }

    /**
     * Vuelve a la lista de pacientes
     */
    backToPatientsList() {
        this.selectedPatient = null;
        this.medicalRecords = [];
        this.filteredRecords = [];
        this.renderPatientsSelection();
    }

    /**
     * Muestra el modal para crear registro médico
     */
    showCreateRecordModal() {
        if (!this.selectedPatient) {
            window.showError('Debe seleccionar un paciente primero');
            return;
        }

        this.currentRecord = null;
        this.showRecordModal();
    }

    /**
     * Muestra el modal de registro médico
     */
    showRecordModal() {
        const modal = document.getElementById('record-modal');
        if (modal) {
            modal.style.display = 'flex';
            setTimeout(() => {
                modal.classList.add('show');
            }, 10);
        }
    }

    /**
     * Cierra el modal de registro médico
     */
    closeRecordModal() {
        const modal = document.getElementById('record-modal');
        if (modal) {
            modal.classList.remove('show');
            setTimeout(() => {
                modal.style.display = 'none';
            }, 300);
        }
        this.currentRecord = null;
    }

    /**
     * Guarda el registro médico
     */
    async saveRecord() {
        const form = document.getElementById('record-form');
        if (!form) return;

        // Validar formulario
        if (!this.validateRecordForm()) {
            return;
        }

        const recordData = this.getRecordFormData();

        try {
            window.showInfo('Guardando consulta médica...');

            const result = await window.medicalRecordApi.createMedicalRecord(recordData);

            if (result) {
                window.showSuccess('Consulta médica guardada exitosamente');
                this.closeRecordModal();
                await this.loadMedicalRecords(this.selectedPatient.cedula);
            }

        } catch (error) {
            console.error('Error saving record:', error);
            window.showError('Error al guardar consulta: ' + error.message);
        }
    }

    /**
     * Valida el formulario de registro médico
     */
    validateRecordForm() {
        const consultationReason = document.getElementById('consultation-reason').value;
        const consultationDate = document.getElementById('consultation-date').value;

        if (!consultationReason) {
            window.showError('Debe seleccionar el motivo de consulta');
            return false;
        }

        if (!consultationDate) {
            window.showError('Debe seleccionar la fecha de consulta');
            return false;
        }

        return true;
    }

    /**
     * Obtiene los datos del formulario de registro médico
     */
    getRecordFormData() {
        const medications = [];
        const diagnosticAids = [];

        // Recopilar medicamentos
        const medicationContainer = document.getElementById('medications-container');
        if (medicationContainer) {
            const medicationForms = medicationContainer.querySelectorAll('.medication-form');
            medicationForms.forEach(form => {
                const nameInput = form.querySelector('input[id*="medication-name"]');
                const dosageInput = form.querySelector('input[id*="medication-dosage"]');
                const frequencyInput = form.querySelector('input[id*="medication-frequency"]');

                if (nameInput && nameInput.value.trim()) {
                    medications.push({
                        medicationName: nameInput.value.trim(),
                        dosage: dosageInput ? dosageInput.value.trim() : '',
                        frequency: frequencyInput ? frequencyInput.value.trim() : ''
                    });
                }
            });
        }

        // Recopilar ayudas diagnósticas
        const diagnosticContainer = document.getElementById('diagnostic-aids-container');
        if (diagnosticContainer) {
            const diagnosticForms = diagnosticContainer.querySelectorAll('.diagnostic-aid-form');
            diagnosticForms.forEach(form => {
                const aidSelect = form.querySelector('select[id*="diagnostic-aid"]');
                const obsInput = form.querySelector('input[id*="diagnostic-observations"]');

                if (aidSelect && aidSelect.value) {
                    diagnosticAids.push({
                        diagnosticAidName: aidSelect.value,
                        observations: obsInput ? obsInput.value.trim() : ''
                    });
                }
            });
        }

        return {
            patientCedula: this.selectedPatient.cedula,
            consultationReason: document.getElementById('consultation-reason').value,
            consultationDate: document.getElementById('consultation-date').value,
            symptoms: document.getElementById('symptoms').value.trim(),
            diagnosis: document.getElementById('diagnosis').value.trim(),
            observations: document.getElementById('observations').value.trim(),
            medicationOrders: medications,
            diagnosticAidOrders: diagnosticAids
        };
    }

    /**
     * Agrega un campo de medicamento
     */
    addMedication() {
        const container = document.getElementById('medications-container');
        if (!container) return;

        const medicationCount = container.querySelectorAll('.medication-form').length + 1;

        const medicationForm = document.createElement('div');
        medicationForm.className = 'medication-form';
        medicationForm.innerHTML = `
            <div class="form-row">
                <div class="form-group">
                    <label for="medication-name-${medicationCount}">Medicamento</label>
                    <input type="text" id="medication-name-${medicationCount}" placeholder="Nombre del medicamento">
                </div>
                <div class="form-group">
                    <label for="medication-dosage-${medicationCount}">Dosis</label>
                    <input type="text" id="medication-dosage-${medicationCount}" placeholder="Ej: 500mg">
                </div>
                <div class="form-group">
                    <label for="medication-frequency-${medicationCount}">Frecuencia</label>
                    <input type="text" id="medication-frequency-${medicationCount}" placeholder="Ej: 2 veces al día">
                </div>
                <div class="form-group">
                    <button type="button" class="btn btn-sm btn-danger" onclick="this.closest('.medication-form').remove()">
                        <i class="fas fa-trash"></i>
                    </button>
                </div>
            </div>
        `;

        container.appendChild(medicationForm);
    }

    /**
     * Agrega un campo de ayuda diagnóstica
     */
    addDiagnosticAid() {
        const container = document.getElementById('diagnostic-aids-container');
        if (!container) return;

        const diagnosticCount = container.querySelectorAll('.diagnostic-aid-form').length + 1;

        const diagnosticForm = document.createElement('div');
        diagnosticForm.className = 'diagnostic-aid-form';
        diagnosticForm.innerHTML = `
            <div class="form-row">
                <div class="form-group">
                    <label for="diagnostic-aid-${diagnosticCount}">Ayuda Diagnóstica</label>
                    <select id="diagnostic-aid-${diagnosticCount}">
                        <option value="">Seleccionar ayuda</option>
                        <option value="Hemograma">Hemograma</option>
                        <option value="Química Sanguínea">Química Sanguínea</option>
                        <option value="Radiografía">Radiografía</option>
                        <option value="Ecografía">Ecografía</option>
                        <option value="Tomografía">Tomografía</option>
                        <option value="Resonancia Magnética">Resonancia Magnética</option>
                        <option value="Endoscopía">Endoscopía</option>
                        <option value="Colonoscopía">Colonoscopía</option>
                    </select>
                </div>
                <div class="form-group">
                    <label for="diagnostic-observations-${diagnosticCount}">Observaciones</label>
                    <input type="text" id="diagnostic-observations-${diagnosticCount}" placeholder="Observaciones de la ayuda diagnóstica">
                </div>
                <div class="form-group">
                    <button type="button" class="btn btn-sm btn-danger" onclick="this.closest('.diagnostic-aid-form').remove()">
                        <i class="fas fa-trash"></i>
                    </button>
                </div>
            </div>
        `;

        container.appendChild(diagnosticForm);
    }

    /**
     * Edita un registro médico
     */
    async editRecord(recordId) {
        try {
            window.showInfo('Cargando registro médico...');

            // Buscar el registro en la lista actual
            const record = this.medicalRecords.find(r => r.id === recordId);

            if (record) {
                this.currentRecord = record;
                this.showRecordModal();
                this.populateRecordForm(record);
                window.showSuccess('Registro cargado para edición');
            } else {
                throw new Error('Registro médico no encontrado');
            }

        } catch (error) {
            console.error('Error loading record:', error);
            window.showError('Error al cargar registro: ' + error.message);
        }
    }

    /**
     * Llena el formulario con datos del registro
     */
    populateRecordForm(record) {
        document.getElementById('record-modal-title').textContent = 'Editar Consulta Médica';

        document.getElementById('consultation-reason').value = record.consultationReason || '';
        document.getElementById('consultation-date').value = record.consultationDate || '';
        document.getElementById('symptoms').value = record.symptoms || '';
        document.getElementById('diagnosis').value = record.diagnosis || '';
        document.getElementById('observations').value = record.observations || '';

        // Llenar medicamentos si existen
        if (record.medicationOrders && record.medicationOrders.length > 0) {
            // Limpiar medicamentos existentes
            const container = document.getElementById('medications-container');
            if (container) {
                container.innerHTML = '';

                record.medicationOrders.forEach((med, index) => {
                    const medicationForm = document.createElement('div');
                    medicationForm.className = 'medication-form';
                    medicationForm.innerHTML = `
                        <div class="form-row">
                            <div class="form-group">
                                <label for="medication-name-${index + 1}">Medicamento</label>
                                <input type="text" id="medication-name-${index + 1}" value="${med.medicationName || ''}">
                            </div>
                            <div class="form-group">
                                <label for="medication-dosage-${index + 1}">Dosis</label>
                                <input type="text" id="medication-dosage-${index + 1}" value="${med.dosage || ''}">
                            </div>
                            <div class="form-group">
                                <label for="medication-frequency-${index + 1}">Frecuencia</label>
                                <input type="text" id="medication-frequency-${index + 1}" value="${med.frequency || ''}">
                            </div>
                            <div class="form-group">
                                <button type="button" class="btn btn-sm btn-danger" onclick="this.closest('.medication-form').remove()">
                                    <i class="fas fa-trash"></i>
                                </button>
                            </div>
                        </div>
                    `;
                    container.appendChild(medicationForm);
                });
            }
        }
    }

    /**
     * Ver detalles de un registro médico
     */
    viewRecord(recordId) {
        const record = this.medicalRecords.find(r => r.id === recordId);
        if (!record) {
            window.showError('Registro médico no encontrado');
            return;
        }

        this.showRecordDetailsModal(record);
    }

    /**
     * Muestra modal con detalles del registro médico
     */
    showRecordDetailsModal(record) {
        const modal = document.createElement('div');
        modal.className = 'modal-overlay';
        modal.innerHTML = `
            <div class="modal large-modal">
                <div class="modal-header">
                    <h3>Consulta Médica - ${window.formatDate(record.recordDate)}</h3>
                    <button class="modal-close" onclick="this.closest('.modal-overlay').remove()">
                        <i class="fas fa-times"></i>
                    </button>
                </div>
                <div class="modal-body">
                    <div class="record-details">
                        <div class="detail-section">
                            <h4><i class="fas fa-user-injured"></i> Información del Paciente</h4>
                            <p><strong>Paciente:</strong> ${this.selectedPatient.fullName}</p>
                            <p><strong>Cédula:</strong> ${window.formatCedula(this.selectedPatient.cedula)}</p>
                        </div>

                        <div class="detail-section">
                            <h4><i class="fas fa-stethoscope"></i> Consulta</h4>
                            <p><strong>Motivo:</strong> ${record.consultationReason || 'No especificado'}</p>
                            <p><strong>Fecha:</strong> ${window.formatDate(record.consultationDate)}</p>
                            <p><strong>Síntomas:</strong> ${record.symptoms || 'No documentados'}</p>
                            ${record.diagnosis ? `<p><strong>Diagnóstico:</strong> ${record.diagnosis}</p>` : ''}
                            ${record.observations ? `<p><strong>Observaciones:</strong> ${record.observations}</p>` : ''}
                        </div>

                        ${record.medicationOrders && record.medicationOrders.length > 0 ? `
                            <div class="detail-section">
                                <h4><i class="fas fa-pills"></i> Medicamentos Recetados</h4>
                                <div class="medications-list">
                                    ${record.medicationOrders.map(med => `
                                        <div class="medication-item">
                                            <span class="medication-name">${med.medicationName}</span>
                                            <span class="medication-dosage">${med.dosage}</span>
                                            <span class="medication-frequency">${med.frequency}</span>
                                        </div>
                                    `).join('')}
                                </div>
                            </div>
                        ` : ''}

                        ${record.diagnosticAidOrders && record.diagnosticAidOrders.length > 0 ? `
                            <div class="detail-section">
                                <h4><i class="fas fa-flask"></i> Ayudas Diagnósticas</h4>
                                <div class="diagnostic-aids-list">
                                    ${record.diagnosticAidOrders.map(aid => `
                                        <div class="diagnostic-aid-item">
                                            <span class="aid-name">${aid.diagnosticAidName}</span>
                                            ${aid.observations ? `<span class="aid-observations">${aid.observations}</span>` : ''}
                                        </div>
                                    `).join('')}
                                </div>
                            </div>
                        ` : ''}
                    </div>
                </div>
                <div class="modal-footer">
                    <button class="btn btn-secondary" onclick="this.closest('.modal-overlay').remove()">
                        Cerrar
                    </button>
                    <button class="btn btn-primary" onclick="medicalRecordsService.editRecord('${record.id}')">
                        <i class="fas fa-edit"></i> Editar
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
     * Obtiene el número de registros del mes actual
     */
    getRecordsThisMonth() {
        const currentMonth = new Date().getMonth();
        const currentYear = new Date().getFullYear();

        return this.medicalRecords.filter(record => {
            const recordDate = new Date(record.recordDate);
            return recordDate.getMonth() === currentMonth && recordDate.getFullYear() === currentYear;
        }).length;
    }

    /**
     * Obtiene la fecha del último registro
     */
    getLastRecordDate() {
        if (this.medicalRecords.length === 0) return 'Nunca';

        const lastRecord = this.medicalRecords
            .filter(r => r.recordDate)
            .sort((a, b) => new Date(b.recordDate) - new Date(a.recordDate))[0];

        return lastRecord ? window.formatDate(lastRecord.recordDate) : 'Nunca';
    }

    /**
     * Muestra estado vacío
     */
    showEmptyState() {
        const contentArea = document.getElementById('content-area');
        if (contentArea) {
            contentArea.innerHTML = `
                <div class="empty-state">
                    <i class="fas fa-file-medical"></i>
                    <h3>No se pudieron cargar las historias clínicas</h3>
                    <p>Verifica tu conexión a internet e intenta nuevamente.</p>
                    <button class="btn btn-primary" onclick="medicalRecordsService.loadPatientsList()">
                        <i class="fas fa-refresh"></i> Reintentar
                    </button>
                </div>
            `;
        }
    }
}

// Crear instancia global del servicio de historias clínicas
window.medicalRecordsService = new MedicalRecordsService();