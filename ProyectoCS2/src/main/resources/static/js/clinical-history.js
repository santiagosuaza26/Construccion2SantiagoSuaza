/**
 * Sistema de Gestión Clínica CS2 - Módulo de Historia Clínica
 * Funcionalidades para Médicos y Enfermeras
 */

// Estado del módulo de historia clínica
let currentPatientHistory = {};
let patientHistoryEntries = [];

/**
 * Busca la historia clínica de un paciente
 */
async function searchPatientHistory() {
    const patientIdInput = document.getElementById('patient-id-search');
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
        showSectionLoading('clinical-history');

        const historyData = await ClinicalHistoryAPI.getByPatientId(patientId);

        if (historyData && Object.keys(historyData).length > 0) {
            currentPatientHistory = historyData;
            patientHistoryEntries = [];

            // Convertir el objeto de historia clínica en array para mostrar
            Object.entries(historyData).forEach(([date, entry]) => {
                patientHistoryEntries.push({
                    date: date,
                    ...entry
                });
            });

            // Ordenar por fecha (más reciente primero)
            patientHistoryEntries.sort((a, b) => new Date(b.date) - new Date(a.date));

            renderPatientHistory();
            showNotification('Historia clínica cargada correctamente', 'success');
        } else {
            showEmptyState('clinical-history',
                'No se encontró historia clínica para este paciente',
                'fas fa-file-medical');
        }

    } catch (error) {
        console.error('Error buscando historia clínica:', error);
        const errorMessage = ApiUtils.handleError(error, 'Error al buscar historia clínica');
        showSectionError('clinical-history', errorMessage);
    }
}

/**
 * Renderiza la historia clínica del paciente
 */
function renderPatientHistory() {
    const content = document.getElementById('clinical-history-content');
    const patientInfo = document.getElementById('patient-info');
    const historyList = document.getElementById('history-entries-list');

    if (!content || !patientInfo || !historyList) return;

    // Mostrar contenido
    content.style.display = 'block';

    // Información del paciente (simulada por ahora)
    patientInfo.innerHTML = `
        <div class="patient-info-grid">
            <div class="info-item">
                <strong>Cédula:</strong> ${document.getElementById('patient-id-search').value}
            </div>
            <div class="info-item">
                <strong>Total de visitas:</strong> ${patientHistoryEntries.length}
            </div>
            <div class="info-item">
                <strong>Última visita:</strong> ${formatDate(patientHistoryEntries[0]?.date)}
            </div>
        </div>
    `;

    // Entradas de historia clínica
    if (patientHistoryEntries.length > 0) {
        historyList.innerHTML = patientHistoryEntries.map(entry => `
            <div class="history-entry">
                <div class="entry-header">
                    <span class="entry-date">${formatDate(entry.date)}</span>
                    <span class="entry-type">${getEntryTypeLabel(entry.type)}</span>
                </div>
                <div class="entry-content">
                    <p><strong>Motivo:</strong> ${entry.reason || 'No especificado'}</p>
                    <p><strong>Diagnóstico:</strong> ${entry.diagnosis || 'No registrado'}</p>
                    ${entry.treatment ? `<p><strong>Tratamiento:</strong> ${entry.treatment}</p>` : ''}
                    ${entry.medications ? `<p><strong>Medicamentos:</strong> ${entry.medications}</p>` : ''}
                    ${entry.observations ? `<p><strong>Observaciones:</strong> ${entry.observations}</p>` : ''}
                </div>
            </div>
        `).join('');
    } else {
        historyList.innerHTML = '<p>No hay entradas en la historia clínica</p>';
    }
}

/**
 * Obtiene la etiqueta para el tipo de entrada
 */
function getEntryTypeLabel(type) {
    const labels = {
        'consultation': 'Consulta',
        'emergency': 'Emergencia',
        'follow-up': 'Seguimiento',
        'surgery': 'Cirugía',
        'therapy': 'Terapia'
    };

    return labels[type] || 'Consulta';
}

/**
 * Función para agregar nueva entrada a historia clínica
 */
async function addHistoryEntry(entryData) {
    const patientId = document.getElementById('patient-id-search').value.trim();
    if (!patientId) {
        showNotification('Primero busque un paciente', 'warning');
        return;
    }

    try {
        const result = await ClinicalHistoryAPI.addEntry(patientId, entryData);
        showNotification('Entrada agregada a historia clínica', 'success');

        // Recargar historia clínica
        await searchPatientHistory();

    } catch (error) {
        console.error('Error agregando entrada a historia clínica:', error);
        const errorMessage = ApiUtils.handleError(error, 'Error al agregar entrada');
        showNotification(errorMessage, 'error');
    }
}

/**
 * Función para actualizar entrada existente
 */
async function updateHistoryEntry(date, entryData) {
    const patientId = document.getElementById('patient-id-search').value.trim();
    if (!patientId) {
        showNotification('Primero busque un paciente', 'warning');
        return;
    }

    try {
        const result = await ClinicalHistoryAPI.updateEntry(patientId, date, entryData);
        showNotification('Entrada actualizada correctamente', 'success');

        // Recargar historia clínica
        await searchPatientHistory();

    } catch (error) {
        console.error('Error actualizando entrada:', error);
        const errorMessage = ApiUtils.handleError(error, 'Error al actualizar entrada');
        showNotification(errorMessage, 'error');
    }
}

/**
 * Función para imprimir historia clínica
 */
function printClinicalHistory() {
    if (patientHistoryEntries.length === 0) {
        showNotification('No hay historia clínica para imprimir', 'warning');
        return;
    }

    printContent('clinical-history-content');
}

/**
 * Función para exportar historia clínica
 */
function exportClinicalHistory() {
    if (patientHistoryEntries.length === 0) {
        showNotification('No hay historia clínica para exportar', 'warning');
        return;
    }

    const dataToExport = patientHistoryEntries.map(entry => ({
        'Fecha': formatDate(entry.date),
        'Tipo': getEntryTypeLabel(entry.type),
        'Motivo': entry.reason || '',
        'Diagnóstico': entry.diagnosis || '',
        'Tratamiento': entry.treatment || '',
        'Medicamentos': entry.medications || '',
        'Observaciones': entry.observations || ''
    }));

    exportToCSV(dataToExport, `historia_clinica_${document.getElementById('patient-id-search').value}_${new Date().toISOString().split('T')[0]}.csv`);
}

// Eventos específicos del módulo de historia clínica
document.addEventListener('DOMContentLoaded', function() {
    // Configurar búsqueda cuando se presione Enter
    const patientIdInput = document.getElementById('patient-id-search');
    if (patientIdInput) {
        patientIdInput.addEventListener('keypress', function(event) {
            if (event.key === 'Enter') {
                searchPatientHistory();
            }
        });

        // Botón de búsqueda
        const searchBtn = patientIdInput.parentNode.querySelector('button');
        if (searchBtn) {
            searchBtn.addEventListener('click', searchPatientHistory);
        }
    }

    // Agregar botones de acción adicionales si no existen
    const sectionHeader = document.querySelector('#clinical-history .section-header');
    if (sectionHeader && !sectionHeader.querySelector('.export-btn')) {
        const exportBtn = document.createElement('button');
        exportBtn.className = 'btn-secondary';
        exportBtn.innerHTML = '<i class="fas fa-download"></i> Exportar';
        exportBtn.onclick = exportClinicalHistory;
        exportBtn.title = 'Exportar historia clínica';

        const printBtn = document.createElement('button');
        printBtn.className = 'btn-secondary';
        printBtn.innerHTML = '<i class="fas fa-print"></i> Imprimir';
        printBtn.onclick = printClinicalHistory;
        printBtn.title = 'Imprimir historia clínica';

        sectionHeader.appendChild(exportBtn);
        sectionHeader.appendChild(printBtn);
    }
});

// Hacer funciones disponibles globalmente
window.searchPatientHistory = searchPatientHistory;
window.addHistoryEntry = addHistoryEntry;
window.updateHistoryEntry = updateHistoryEntry;
window.printClinicalHistory = printClinicalHistory;
window.exportClinicalHistory = exportClinicalHistory;