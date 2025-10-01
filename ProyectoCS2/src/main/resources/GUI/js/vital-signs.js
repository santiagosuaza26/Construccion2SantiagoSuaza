/**
 * Sistema de Gestión Clínica CS2 - Módulo de Signos Vitales
 * Funcionalidades para Médicos y Enfermeras
 */

// Estado del módulo de signos vitales
let currentPatientVitals = [];
let vitalsHistory = [];

/**
 * Carga signos vitales de un paciente
 */
async function loadPatientVitals() {
    const patientIdInput = document.getElementById('vital-patient-id');
    if (!patientIdInput) return;

    const patientId = patientIdInput.value.trim();
    if (!patientId) {
        showNotification('Ingrese la cédula del paciente', 'warning');
        return;
    }

    if (!isValidIdCard(patientId)) {
        showNotification('La cédula debe contener solo números', 'warning');
        return;
    }

    try {
        // Mostrar formulario
        document.getElementById('vital-signs-form').style.display = 'block';

        // Cargar historial de signos vitales
        const vitals = await VitalSignsAPI.getByPatient(patientId);
        vitalsHistory = vitals || [];

        renderVitalsHistory();

    } catch (error) {
        console.error('Error cargando signos vitales:', error);
        const errorMessage = ApiUtils.handleError(error, 'Error al cargar signos vitales');
        showNotification(errorMessage, 'error');
    }
}

/**
 * Renderiza el historial de signos vitales
 */
function renderVitalsHistory() {
    const historyContainer = document.getElementById('vitals-history-list');
    if (!historyContainer) return;

    if (vitalsHistory.length === 0) {
        historyContainer.innerHTML = '<p>No hay registros de signos vitales para este paciente</p>';
        return;
    }

    // Mostrar sección de historial
    document.getElementById('vitals-history').style.display = 'block';

    historyContainer.innerHTML = vitalsHistory.map(vitals => `
        <div class="vitals-record">
            <div class="vital-item">
                <div class="vital-label">Presión Arterial</div>
                <div class="vital-value">${vitals.bloodPressure || '-'}</div>
                <div class="vital-unit">mmHg</div>
            </div>
            <div class="vital-item">
                <div class="vital-label">Temperatura</div>
                <div class="vital-value">${vitals.temperature || '-'}</div>
                <div class="vital-unit">°C</div>
            </div>
            <div class="vital-item">
                <div class="vital-label">Frecuencia Cardíaca</div>
                <div class="vital-value">${vitals.heartRate || '-'}</div>
                <div class="vital-unit">lpm</div>
            </div>
            <div class="vital-item">
                <div class="vital-label">Frecuencia Respiratoria</div>
                <div class="vital-value">${vitals.respiratoryRate || '-'}</div>
                <div class="vital-unit">rpm</div>
            </div>
            <div class="vital-item">
                <div class="vital-label">Saturación O2</div>
                <div class="vital-value">${vitals.oxygenSaturation || '-'}</div>
                <div class="vital-unit">%</div>
            </div>
            <div class="vital-item">
                <div class="vital-label">Peso</div>
                <div class="vital-value">${vitals.weight || '-'}</div>
                <div class="vital-unit">kg</div>
            </div>
            <div class="vital-item" style="grid-column: 1 / -1; margin-top: 1rem; padding-top: 1rem; border-top: 1px solid var(--border-light);">
                <div class="vital-label">Fecha y Observaciones</div>
                <div style="font-size: 0.875rem; color: var(--text-secondary); margin-top: 0.5rem;">
                    <strong>Fecha:</strong> ${formatDate(vitals.recordedAt)} |
                    <strong>Observaciones:</strong> ${vitals.observations || 'Sin observaciones'}
                </div>
            </div>
        </div>
    `).join('');
}

/**
 * Maneja el envío del formulario de signos vitales
 */
async function handleVitalSignsSubmit(event) {
    event.preventDefault();

    const patientId = document.getElementById('vital-patient-id').value.trim();
    if (!patientId) {
        showNotification('Primero ingrese la cédula del paciente', 'warning');
        return;
    }

    const vitalsData = {
        bloodPressure: document.getElementById('blood-pressure').value.trim(),
        temperature: parseFloat(document.getElementById('temperature').value),
        heartRate: parseInt(document.getElementById('heart-rate').value),
        respiratoryRate: parseInt(document.getElementById('respiratory-rate').value),
        oxygenSaturation: parseInt(document.getElementById('oxygen-saturation').value),
        weight: parseFloat(document.getElementById('weight').value),
        observations: document.getElementById('vitals-observations').value.trim()
    };

    // Validar datos
    const validationErrors = validateVitalsData(vitalsData);
    if (validationErrors.length > 0) {
        showValidationErrors(validationErrors);
        return;
    }

    try {
        const result = await VitalSignsAPI.add(patientId, vitalsData);
        showNotification('Signos vitales registrados correctamente', 'success');

        // Limpiar formulario
        document.getElementById('vitals-form').reset();

        // Recargar historial
        await loadPatientVitals();

    } catch (error) {
        console.error('Error guardando signos vitales:', error);
        const errorMessage = ApiUtils.handleError(error, 'Error al guardar signos vitales');
        showNotification(errorMessage, 'error');
    }
}

/**
 * Valida datos de signos vitales
 */
function validateVitalsData(vitalsData) {
    const errors = [];

    if (vitalsData.temperature && (vitalsData.temperature < 30 || vitalsData.temperature > 45)) {
        errors.push('La temperatura debe estar entre 30°C y 45°C');
    }

    if (vitalsData.heartRate && (vitalsData.heartRate < 30 || vitalsData.heartRate > 250)) {
        errors.push('La frecuencia cardíaca debe estar entre 30 y 250 lpm');
    }

    if (vitalsData.respiratoryRate && (vitalsData.respiratoryRate < 5 || vitalsData.respiratoryRate > 60)) {
        errors.push('La frecuencia respiratoria debe estar entre 5 y 60 rpm');
    }

    if (vitalsData.oxygenSaturation && (vitalsData.oxygenSaturation < 50 || vitalsData.oxygenSaturation > 100)) {
        errors.push('La saturación de oxígeno debe estar entre 50% y 100%');
    }

    if (vitalsData.weight && (vitalsData.weight < 0.5 || vitalsData.weight > 500)) {
        errors.push('El peso debe estar entre 0.5 kg y 500 kg');
    }

    return errors;
}

/**
 * Función para mostrar tendencias de signos vitales
 */
function showVitalsTrends() {
    if (vitalsHistory.length < 2) {
        showNotification('Se necesitan al menos 2 registros para mostrar tendencias', 'info');
        return;
    }

    // Crear gráfico simple con datos de signos vitales
    const trendData = vitalsHistory.slice(0, 10).reverse(); // Últimos 10 registros

    let trendInfo = `
        <div class="vitals-trends">
            <h4>Tendencias de Signos Vitales</h4>
            <div class="trend-summary">
                <p><strong>Temperatura promedio:</strong> ${calculateAverage(trendData.map(v => v.temperature)).toFixed(1)}°C</p>
                <p><strong>Frecuencia cardíaca promedio:</strong> ${Math.round(calculateAverage(trendData.map(v => v.heartRate)))} lpm</p>
                <p><strong>Saturación promedio:</strong> ${Math.round(calculateAverage(trendData.map(v => v.oxygenSaturation)))}%</p>
            </div>
        </div>
    `;

    showNotification(trendInfo, 'info');
}

/**
 * Calcula el promedio de un array de números
 */
function calculateAverage(numbers) {
    const validNumbers = numbers.filter(n => n != null && !isNaN(n));
    return validNumbers.length > 0 ? validNumbers.reduce((sum, n) => sum + n, 0) / validNumbers.length : 0;
}

/**
 * Función para exportar signos vitales
 */
function exportVitals() {
    if (vitalsHistory.length === 0) {
        showNotification('No hay signos vitales para exportar', 'warning');
        return;
    }

    const dataToExport = vitalsHistory.map(vitals => ({
        'Fecha': formatDate(vitals.recordedAt),
        'Presión Arterial': vitals.bloodPressure || '',
        'Temperatura': vitals.temperature || '',
        'Frecuencia Cardíaca': vitals.heartRate || '',
        'Frecuencia Respiratoria': vitals.respiratoryRate || '',
        'Saturación O2': vitals.oxygenSaturation || '',
        'Peso': vitals.weight || '',
        'Observaciones': vitals.observations || ''
    }));

    exportToCSV(dataToExport, `signos_vitales_${document.getElementById('vital-patient-id').value}_${new Date().toISOString().split('T')[0]}.csv`);
}

// Eventos específicos del módulo de signos vitales
document.addEventListener('DOMContentLoaded', function() {
    // Configurar envío del formulario de signos vitales
    const vitalsForm = document.getElementById('vitals-form');
    if (vitalsForm) {
        vitalsForm.onsubmit = handleVitalSignsSubmit;
    }

    // Configurar búsqueda cuando se presione Enter
    const patientIdInput = document.getElementById('vital-patient-id');
    if (patientIdInput) {
        patientIdInput.addEventListener('keypress', function(event) {
            if (event.key === 'Enter') {
                loadPatientVitals();
            }
        });

        // Botón de cargar
        const loadBtn = patientIdInput.parentNode.querySelector('button');
        if (loadBtn) {
            loadBtn.addEventListener('click', loadPatientVitals);
        }
    }

    // Agregar botones de acción adicionales si no existen
    const sectionHeader = document.querySelector('#vital-signs .section-header');
    if (sectionHeader && !sectionHeader.querySelector('.export-btn')) {
        const exportBtn = document.createElement('button');
        exportBtn.className = 'btn-secondary';
        exportBtn.innerHTML = '<i class="fas fa-download"></i> Exportar';
        exportBtn.onclick = exportVitals;
        exportBtn.title = 'Exportar signos vitales';

        const trendsBtn = document.createElement('button');
        trendsBtn.className = 'btn-secondary';
        trendsBtn.innerHTML = '<i class="fas fa-chart-line"></i> Tendencias';
        trendsBtn.onclick = showVitalsTrends;
        trendsBtn.title = 'Ver tendencias';

        sectionHeader.appendChild(trendsBtn);
        sectionHeader.appendChild(exportBtn);
    }
});

// Hacer funciones disponibles globalmente
window.loadPatientVitals = loadPatientVitals;
window.exportVitals = exportVitals;
window.showVitalsTrends = showVitalsTrends;