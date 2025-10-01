/**
 * Sistema de Gestión Clínica CS2 - Módulo de Gestión de Pacientes
 * Funcionalidades específicas para rol Administrativo
 */

// Estado del módulo de pacientes
let patientsData = [];
let filteredPatients = [];
let currentPatient = null;

/**
 * Carga los datos de pacientes
 */
async function loadPatientsData() {
    if (!checkPermission('patients')) {
        showSectionError('patients', 'No tiene permisos para acceder a esta sección');
        return;
    }

    try {
        showSectionLoading('patients');

        const patients = await PatientAPI.getAll();
        patientsData = patients;
        filteredPatients = patients;

        renderPatientsTable();
        updatePatientsCount();

    } catch (error) {
        console.error('Error cargando pacientes:', error);
        const errorMessage = ApiUtils.handleError(error, 'Error al cargar pacientes');
        showSectionError('patients', errorMessage);
    }
}

/**
 * Renderiza la tabla de pacientes
 */
function renderPatientsTable() {
    const tbody = document.getElementById('patients-tbody');
    if (!tbody) return;

    if (filteredPatients.length === 0) {
        tbody.innerHTML = '<tr><td colspan="6" class="text-center">No hay pacientes registrados</td></tr>';
        return;
    }

    tbody.innerHTML = filteredPatients.map(patient => `
        <tr>
            <td>${patient.idCard}</td>
            <td>${patient.name}</td>
            <td>${patient.phone || '-'}</td>
            <td>${patient.email || '-'}</td>
            <td><span class="status-badge status-${patient.status || 'active'}">${patient.status || 'Activo'}</span></td>
            <td class="actions-cell">
                <button onclick="viewPatient('${patient.idCard}')" class="btn-icon" title="Ver detalles">
                    <i class="fas fa-eye"></i>
                </button>
                <button onclick="editPatient('${patient.idCard}')" class="btn-icon" title="Editar">
                    <i class="fas fa-edit"></i>
                </button>
                <button onclick="deletePatient('${patient.idCard}')" class="btn-icon btn-danger" title="Eliminar">
                    <i class="fas fa-trash"></i>
                </button>
            </td>
        </tr>
    `).join('');
}

/**
 * Actualiza el contador de pacientes
 */
function updatePatientsCount() {
    const totalCount = patientsData.length;
    const activeCount = patientsData.filter(p => p.status === 'active').length;

    // Actualizar tarjetas del dashboard si estamos en esa sección
    if (currentSection === 'patients') {
        updateDashboardCard('total-patients', totalCount);
    }
}

/**
 * Muestra el modal de nuevo paciente
 */
function showPatientModal(patientId = null) {
    const modal = document.getElementById('patient-modal');
    const title = document.getElementById('patient-modal-title');
    const form = document.getElementById('patient-form');

    if (patientId) {
        title.textContent = 'Editar Paciente';
        loadPatientData(patientId);
    } else {
        title.textContent = 'Nuevo Paciente';
        clearPatientForm();
    }

    modal.style.display = 'flex';
    form.onsubmit = (e) => handlePatientSubmit(e, patientId);
}

/**
 * Cierra el modal de paciente
 */
function closePatientModal() {
    const modal = document.getElementById('patient-modal');
    modal.style.display = 'none';
    clearPatientForm();
}

/**
 * Limpia el formulario de paciente
 */
function clearPatientForm() {
    const form = document.getElementById('patient-form');
    if (form) {
        form.reset();
    }
    currentPatient = null;
}

/**
 * Carga datos de un paciente específico
 */
async function loadPatientData(patientId) {
    try {
        const patient = await PatientAPI.getById(patientId);
        currentPatient = patient;

        // Llenar formulario con datos del paciente
        document.getElementById('patient-id').value = patient.idCard;
        document.getElementById('patient-name').value = patient.name;
        document.getElementById('patient-birthdate').value = patient.birthDate;
        document.getElementById('patient-gender').value = patient.gender;
        document.getElementById('patient-phone').value = patient.phone;
        document.getElementById('patient-email').value = patient.email;
        document.getElementById('patient-address').value = patient.address;
        document.getElementById('patient-occupation').value = patient.occupation;

        // Información de emergencia
        if (patient.emergencyContact) {
            document.getElementById('emergency-name').value = patient.emergencyContact.name;
            document.getElementById('emergency-phone').value = patient.emergencyContact.phone;
            document.getElementById('emergency-relationship').value = patient.emergencyContact.relationship;
        }

        // Información de seguro
        if (patient.insurancePolicy) {
            document.getElementById('insurance-company').value = patient.insurancePolicy.company;
            document.getElementById('insurance-policy').value = patient.insurancePolicy.policyNumber;
            document.getElementById('insurance-expiry').value = patient.insurancePolicy.expiryDate;
        }

    } catch (error) {
        console.error('Error cargando datos del paciente:', error);
        ApiUtils.handleError(error, 'Error al cargar datos del paciente');
    }
}

/**
 * Maneja el envío del formulario de paciente
 */
async function handlePatientSubmit(event, patientId = null) {
    event.preventDefault();

    const form = document.getElementById('patient-form');
    const formData = new FormData(form);

    // Crear objeto de paciente
    const patientData = {
        idCard: formData.get('patient-id').trim(),
        name: formData.get('patient-name').trim(),
        birthDate: formData.get('patient-birthdate'),
        gender: formData.get('patient-gender'),
        phone: formData.get('patient-phone').trim(),
        email: formData.get('patient-email').trim(),
        address: formData.get('patient-address').trim(),
        occupation: formData.get('patient-occupation').trim(),
        emergencyContact: {
            name: formData.get('emergency-name').trim(),
            phone: formData.get('emergency-phone').trim(),
            relationship: formData.get('emergency-relationship').trim()
        },
        insurancePolicy: {
            company: formData.get('insurance-company').trim(),
            policyNumber: formData.get('insurance-policy').trim(),
            expiryDate: formData.get('insurance-expiry')
        }
    };

    // Validar datos
    const validationErrors = validatePatientData(patientData);
    if (validationErrors.length > 0) {
        showValidationErrors(validationErrors);
        return;
    }

    try {
        let result;

        if (patientId) {
            // Actualizar paciente existente
            result = await PatientAPI.update(patientId, patientData);
            showNotification('Paciente actualizado correctamente', 'success');
        } else {
            // Crear nuevo paciente
            result = await PatientAPI.create(patientData);
            showNotification('Paciente registrado correctamente', 'success');
        }

        closePatientModal();
        await loadPatientsData(); // Recargar lista

    } catch (error) {
        console.error('Error guardando paciente:', error);
        const errorMessage = ApiUtils.handleError(error, 'Error al guardar paciente');
        showNotification(errorMessage, 'error');
    }
}

/**
 * Valida datos del paciente
 */
function validatePatientData(patientData) {
    const errors = [];

    if (!patientData.idCard) {
        errors.push('La cédula es requerida');
    } else if (!isValidIdCard(patientData.idCard)) {
        errors.push('La cédula debe contener solo números (máximo 20 dígitos)');
    }

    if (!patientData.name) {
        errors.push('El nombre es requerido');
    }

    if (!patientData.birthDate) {
        errors.push('La fecha de nacimiento es requerida');
    }

    if (!patientData.gender) {
        errors.push('El género es requerido');
    }

    if (patientData.email && !isValidEmail(patientData.email)) {
        errors.push('El email tiene un formato inválido');
    }

    if (patientData.phone && !isValidPhone(patientData.phone)) {
        errors.push('El teléfono tiene un formato inválido');
    }

    return errors;
}

/**
 * Función para ver detalles de un paciente
 */
function viewPatient(patientId) {
    // Por ahora, abrir el modal en modo de solo lectura
    showPatientModal(patientId);

    // Deshabilitar campos para solo lectura
    setTimeout(() => {
        const inputs = document.querySelectorAll('#patient-form input, #patient-form select, #patient-form textarea');
        inputs.forEach(input => {
            input.disabled = true;
        });

        const submitBtn = document.querySelector('#patient-form button[type="submit"]');
        if (submitBtn) {
            submitBtn.style.display = 'none';
        }

        // Agregar botón de editar
        const actionsDiv = document.querySelector('.modal-actions');
        if (actionsDiv && !actionsDiv.querySelector('.edit-btn')) {
            const editBtn = document.createElement('button');
            editBtn.type = 'button';
            editBtn.className = 'btn-primary';
            editBtn.innerHTML = '<i class="fas fa-edit"></i> Editar';
            editBtn.onclick = () => {
                // Re-habilitar campos
                inputs.forEach(input => {
                    input.disabled = false;
                });
                submitBtn.style.display = 'block';
                editBtn.remove();
            };
            actionsDiv.insertBefore(editBtn, actionsDiv.firstChild);
        }
    }, 100);
}

/**
 * Función para editar un paciente
 */
function editPatient(patientId) {
    showPatientModal(patientId);
}

/**
 * Función para eliminar un paciente
 */
function deletePatient(patientId) {
    confirmAction('¿Está seguro de que desea eliminar este paciente? Esta acción no se puede deshacer.', async () => {
        try {
            await PatientAPI.delete(patientId);
            showNotification('Paciente eliminado correctamente', 'success');
            await loadPatientsData(); // Recargar lista
        } catch (error) {
            console.error('Error eliminando paciente:', error);
            const errorMessage = ApiUtils.handleError(error, 'Error al eliminar paciente');
            showNotification(errorMessage, 'error');
        }
    });
}

/**
 * Función para buscar pacientes
 */
function searchPatients(query) {
    if (!query) {
        filteredPatients = patientsData;
    } else {
        const searchTerm = query.toLowerCase();
        filteredPatients = patientsData.filter(patient =>
            patient.name.toLowerCase().includes(searchTerm) ||
            patient.idCard.includes(searchTerm) ||
            (patient.email && patient.email.toLowerCase().includes(searchTerm))
        );
    }

    renderPatientsTable();
}

/**
 * Función para filtrar pacientes
 */
function filterPatients() {
    const filterSelect = document.getElementById('patient-filter');
    const filterValue = filterSelect ? filterSelect.value : '';

    if (!filterValue) {
        filteredPatients = patientsData;
    } else {
        filteredPatients = patientsData.filter(patient =>
            patient.status === filterValue
        );
    }

    renderPatientsTable();
}

/**
 * Configura la búsqueda de pacientes
 */
function setupPatientSearch() {
    const searchInput = document.getElementById('patient-search');
    if (searchInput) {
        const debouncedSearch = debounce(function(value) {
            searchPatients(value);
        }, 300);

        searchInput.addEventListener('input', function() {
            debouncedSearch(this.value);
        });
    }
}

/**
 * Función para exportar pacientes a CSV
 */
function exportPatients() {
    if (filteredPatients.length === 0) {
        showNotification('No hay pacientes para exportar', 'warning');
        return;
    }

    const dataToExport = filteredPatients.map(patient => ({
        'Cédula': patient.idCard,
        'Nombre': patient.name,
        'Fecha Nacimiento': formatDate(patient.birthDate),
        'Género': patient.gender,
        'Teléfono': patient.phone,
        'Email': patient.email,
        'Dirección': patient.address,
        'Ocupación': patient.occupation,
        'Estado': patient.status || 'Activo'
    }));

    exportToCSV(dataToExport, `pacientes_${new Date().toISOString().split('T')[0]}.csv`);
}

/**
 * Función para imprimir lista de pacientes
 */
function printPatients() {
    printContent('patients');
}

/**
 * Función para mostrar estadísticas de pacientes
 */
function showPatientsStats() {
    const total = patientsData.length;
    const active = patientsData.filter(p => p.status === 'active').length;
    const inactive = total - active;

    const stats = `
        <div class="patients-stats">
            <div class="stat-item">
                <span class="stat-label">Total Pacientes:</span>
                <span class="stat-value">${total}</span>
            </div>
            <div class="stat-item">
                <span class="stat-label">Pacientes Activos:</span>
                <span class="stat-value">${active}</span>
            </div>
            <div class="stat-item">
                <span class="stat-label">Pacientes Inactivos:</span>
                <span class="stat-value">${inactive}</span>
            </div>
        </div>
    `;

    showNotification(stats, 'info');
}

// Eventos específicos del módulo de pacientes
document.addEventListener('DOMContentLoaded', function() {
    // Configurar búsqueda cuando se muestre la sección de pacientes
    const patientsSection = document.getElementById('patients');
    if (patientsSection) {
        const observer = new MutationObserver(function(mutations) {
            mutations.forEach(function(mutation) {
                if (mutation.type === 'attributes' && mutation.attributeName === 'class') {
                    if (patientsSection.classList.contains('active')) {
                        setupPatientSearch();
                    }
                }
            });
        });

        observer.observe(patientsSection, {
            attributes: true,
            attributeFilter: ['class']
        });
    }

    // Configurar filtros
    const filterSelect = document.getElementById('patient-filter');
    if (filterSelect) {
        filterSelect.addEventListener('change', filterPatients);
    }

    // Agregar botones de acción adicionales si no existen
    const sectionHeader = document.querySelector('#patients .section-header');
    if (sectionHeader && !sectionHeader.querySelector('.export-btn')) {
        const exportBtn = document.createElement('button');
        exportBtn.className = 'btn-secondary';
        exportBtn.innerHTML = '<i class="fas fa-download"></i> Exportar';
        exportBtn.onclick = exportPatients;
        exportBtn.title = 'Exportar a CSV';

        const printBtn = document.createElement('button');
        printBtn.className = 'btn-secondary';
        printBtn.innerHTML = '<i class="fas fa-print"></i> Imprimir';
        printBtn.onclick = printPatients;
        printBtn.title = 'Imprimir lista';

        const statsBtn = document.createElement('button');
        statsBtn.className = 'btn-secondary';
        statsBtn.innerHTML = '<i class="fas fa-chart-bar"></i> Estadísticas';
        statsBtn.onclick = showPatientsStats;
        statsBtn.title = 'Ver estadísticas';

        sectionHeader.appendChild(statsBtn);
        sectionHeader.appendChild(exportBtn);
        sectionHeader.appendChild(printBtn);
    }
});

// Hacer funciones disponibles globalmente
window.loadPatientsData = loadPatientsData;
window.showPatientModal = showPatientModal;
window.closePatientModal = closePatientModal;
window.viewPatient = viewPatient;
window.editPatient = editPatient;
window.deletePatient = deletePatient;
window.searchPatients = searchPatients;
window.filterPatients = filterPatients;
window.exportPatients = exportPatients;
window.printPatients = printPatients;
window.showPatientsStats = showPatientsStats;