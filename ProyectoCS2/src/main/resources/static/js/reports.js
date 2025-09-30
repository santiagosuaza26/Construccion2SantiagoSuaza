/**
 * Sistema de Gestión Clínica CS2 - Módulo de Reportes
 * Funcionalidades para Administrativo y Soporte de Información
 */

// Estado del módulo de reportes
let currentReport = null;

/**
 * Genera reporte de pacientes
 */
async function generatePatientReport() {
    try {
        showReportLoading('Generando reporte de pacientes...');

        const filters = {
            status: document.getElementById('patient-filter')?.value || '',
            dateFrom: document.getElementById('report-date-from')?.value || '',
            dateTo: document.getElementById('report-date-to')?.value || ''
        };

        const reportData = await ReportsAPI.generatePatientReport(filters);

        renderPatientReport(reportData);

    } catch (error) {
        console.error('Error generando reporte de pacientes:', error);
        const errorMessage = ApiUtils.handleError(error, 'Error al generar reporte de pacientes');
        showReportError(errorMessage);
    }
}

/**
 * Genera reporte financiero
 */
async function generateFinancialReport() {
    try {
        showReportLoading('Generando reporte financiero...');

        const filters = {
            dateFrom: document.getElementById('financial-date-from')?.value || '',
            dateTo: document.getElementById('financial-date-to')?.value || '',
            status: document.getElementById('financial-status-filter')?.value || ''
        };

        const reportData = await ReportsAPI.generateFinancialReport(filters);

        renderFinancialReport(reportData);

    } catch (error) {
        console.error('Error generando reporte financiero:', error);
        const errorMessage = ApiUtils.handleError(error, 'Error al generar reporte financiero');
        showReportError(errorMessage);
    }
}

/**
 * Genera reporte de órdenes médicas
 */
async function generateOrdersReport() {
    try {
        showReportLoading('Generando reporte de órdenes médicas...');

        const filters = {
            dateFrom: document.getElementById('orders-date-from')?.value || '',
            dateTo: document.getElementById('orders-date-to')?.value || '',
            type: document.getElementById('orders-type-filter')?.value || '',
            status: document.getElementById('orders-status-filter')?.value || ''
        };

        const reportData = await ReportsAPI.generateOrdersReport(filters);

        renderOrdersReport(reportData);

    } catch (error) {
        console.error('Error generando reporte de órdenes:', error);
        const errorMessage = ApiUtils.handleError(error, 'Error al generar reporte de órdenes');
        showReportError(errorMessage);
    }
}

/**
 * Muestra pantalla de carga para reportes
 */
function showReportLoading(message) {
    const reportsContent = document.querySelector('#reports .reports-grid');
    if (reportsContent) {
        reportsContent.innerHTML = `
            <div class="report-loading" style="grid-column: 1 / -1; text-align: center; padding: 3rem;">
                <i class="fas fa-spinner fa-spin" style="font-size: 2rem; color: var(--primary-color);"></i>
                <p style="margin-top: 1rem; color: var(--text-secondary);">${message}</p>
            </div>
        `;
    }
}

/**
 * Muestra error en reportes
 */
function showReportError(message) {
    const reportsContent = document.querySelector('#reports .reports-grid');
    if (reportsContent) {
        reportsContent.innerHTML = `
            <div class="report-error" style="grid-column: 1 / -1; text-align: center; padding: 3rem;">
                <i class="fas fa-exclamation-triangle" style="font-size: 2rem; color: var(--danger-color);"></i>
                <p style="margin-top: 1rem; color: var(--text-secondary);">${message}</p>
                <button onclick="retryLastReport()" class="btn-secondary" style="margin-top: 1rem;">
                    <i class="fas fa-redo"></i> Reintentar
                </button>
            </div>
        `;
    }
}

/**
 * Renderiza reporte de pacientes
 */
function renderPatientReport(data) {
    const reportsContent = document.querySelector('#reports .reports-grid');
    if (!reportsContent) return;

    const totalPatients = data.total || 0;
    const activePatients = data.active || 0;
    const newPatients = data.newThisMonth || 0;

    reportsContent.innerHTML = `
        <div class="report-result" style="grid-column: 1 / -1;">
            <div class="report-header">
                <h3>Reporte de Pacientes</h3>
                <div class="report-actions">
                    <button onclick="exportPatientReport()" class="btn-secondary">
                        <i class="fas fa-download"></i> Exportar
                    </button>
                    <button onclick="printPatientReport()" class="btn-secondary">
                        <i class="fas fa-print"></i> Imprimir
                    </button>
                </div>
            </div>

            <div class="report-summary">
                <div class="summary-card">
                    <div class="summary-icon">
                        <i class="fas fa-users"></i>
                    </div>
                    <div class="summary-content">
                        <h4>Total Pacientes</h4>
                        <p class="summary-number">${totalPatients}</p>
                    </div>
                </div>

                <div class="summary-card">
                    <div class="summary-icon">
                        <i class="fas fa-user-check"></i>
                    </div>
                    <div class="summary-content">
                        <h4>Pacientes Activos</h4>
                        <p class="summary-number">${activePatients}</p>
                    </div>
                </div>

                <div class="summary-card">
                    <div class="summary-icon">
                        <i class="fas fa-user-plus"></i>
                    </div>
                    <div class="summary-content">
                        <h4>Nuevos este Mes</h4>
                        <p class="summary-number">${newPatients}</p>
                    </div>
                </div>
            </div>

            <div class="report-details">
                <h4>Detalles del Reporte</h4>
                <div class="data-table">
                    <table>
                        <thead>
                            <tr>
                                <th>Cédula</th>
                                <th>Nombre</th>
                                <th>Estado</th>
                                <th>Fecha Registro</th>
                                <th>Última Visita</th>
                            </tr>
                        </thead>
                        <tbody id="patient-report-tbody">
                            ${generatePatientReportRows(data.patients || [])}
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    `;
}

/**
 * Genera filas para reporte de pacientes
 */
function generatePatientReportRows(patients) {
    if (!patients || patients.length === 0) {
        return '<tr><td colspan="5" class="text-center">No hay datos para mostrar</td></tr>';
    }

    return patients.map(patient => `
        <tr>
            <td>${patient.idCard}</td>
            <td>${patient.name}</td>
            <td><span class="status-badge status-${patient.status || 'active'}">${patient.status || 'Activo'}</span></td>
            <td>${formatDate(patient.registrationDate)}</td>
            <td>${formatDate(patient.lastVisit)}</td>
        </tr>
    `).join('');
}

/**
 * Renderiza reporte financiero
 */
function renderFinancialReport(data) {
    const reportsContent = document.querySelector('#reports .reports-grid');
    if (!reportsContent) return;

    const totalInvoiced = data.totalInvoiced || 0;
    const totalPaid = data.totalPaid || 0;
    const pendingAmount = data.pendingAmount || 0;

    reportsContent.innerHTML = `
        <div class="report-result" style="grid-column: 1 / -1;">
            <div class="report-header">
                <h3>Reporte Financiero</h3>
                <div class="report-actions">
                    <button onclick="exportFinancialReport()" class="btn-secondary">
                        <i class="fas fa-download"></i> Exportar
                    </button>
                    <button onclick="printFinancialReport()" class="btn-secondary">
                        <i class="fas fa-print"></i> Imprimir
                    </button>
                </div>
            </div>

            <div class="report-summary">
                <div class="summary-card">
                    <div class="summary-icon">
                        <i class="fas fa-file-invoice-dollar"></i>
                    </div>
                    <div class="summary-content">
                        <h4>Total Facturado</h4>
                        <p class="summary-number">${formatCurrency(totalInvoiced)}</p>
                    </div>
                </div>

                <div class="summary-card">
                    <div class="summary-icon">
                        <i class="fas fa-check-circle"></i>
                    </div>
                    <div class="summary-content">
                        <h4>Total Cobrado</h4>
                        <p class="summary-number">${formatCurrency(totalPaid)}</p>
                    </div>
                </div>

                <div class="summary-card">
                    <div class="summary-icon">
                        <i class="fas fa-clock"></i>
                    </div>
                    <div class="summary-content">
                        <h4>Pendiente de Cobro</h4>
                        <p class="summary-number">${formatCurrency(pendingAmount)}</p>
                    </div>
                </div>
            </div>

            <div class="report-details">
                <h4>Facturas del Período</h4>
                <div class="data-table">
                    <table>
                        <thead>
                            <tr>
                                <th>Número</th>
                                <th>Paciente</th>
                                <th>Fecha</th>
                                <th>Total</th>
                                <th>Estado</th>
                            </tr>
                        </thead>
                        <tbody id="financial-report-tbody">
                            ${generateFinancialReportRows(data.invoices || [])}
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    `;
}

/**
 * Genera filas para reporte financiero
 */
function generateFinancialReportRows(invoices) {
    if (!invoices || invoices.length === 0) {
        return '<tr><td colspan="5" class="text-center">No hay datos para mostrar</td></tr>';
    }

    return invoices.map(invoice => `
        <tr>
            <td>${invoice.invoiceNumber}</td>
            <td>${invoice.patientName}</td>
            <td>${formatDate(invoice.issueDate)}</td>
            <td>${formatCurrency(invoice.totalAmount)}</td>
            <td><span class="status-badge status-${invoice.status?.toLowerCase()}">${invoice.status || 'Pendiente'}</span></td>
        </tr>
    `).join('');
}

/**
 * Renderiza reporte de órdenes médicas
 */
function renderOrdersReport(data) {
    const reportsContent = document.querySelector('#reports .reports-grid');
    if (!reportsContent) return;

    const totalOrders = data.total || 0;
    const completedOrders = data.completed || 0;
    const pendingOrders = data.pending || 0;

    reportsContent.innerHTML = `
        <div class="report-result" style="grid-column: 1 / -1;">
            <div class="report-header">
                <h3>Reporte de Órdenes Médicas</h3>
                <div class="report-actions">
                    <button onclick="exportOrdersReport()" class="btn-secondary">
                        <i class="fas fa-download"></i> Exportar
                    </button>
                    <button onclick="printOrdersReport()" class="btn-secondary">
                        <i class="fas fa-print"></i> Imprimir
                    </button>
                </div>
            </div>

            <div class="report-summary">
                <div class="summary-card">
                    <div class="summary-icon">
                        <i class="fas fa-prescription"></i>
                    </div>
                    <div class="summary-content">
                        <h4>Total Órdenes</h4>
                        <p class="summary-number">${totalOrders}</p>
                    </div>
                </div>

                <div class="summary-card">
                    <div class="summary-icon">
                        <i class="fas fa-check-circle"></i>
                    </div>
                    <div class="summary-content">
                        <h4>Completadas</h4>
                        <p class="summary-number">${completedOrders}</p>
                    </div>
                </div>

                <div class="summary-card">
                    <div class="summary-icon">
                        <i class="fas fa-clock"></i>
                    </div>
                    <div class="summary-content">
                        <h4>Pendientes</h4>
                        <p class="summary-number">${pendingOrders}</p>
                    </div>
                </div>
            </div>

            <div class="report-details">
                <h4>Órdenes del Período</h4>
                <div class="data-table">
                    <table>
                        <thead>
                            <tr>
                                <th>Número</th>
                                <th>Paciente</th>
                                <th>Tipo</th>
                                <th>Estado</th>
                                <th>Fecha</th>
                            </tr>
                        </thead>
                        <tbody id="orders-report-tbody">
                            ${generateOrdersReportRows(data.orders || [])}
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    `;
}

/**
 * Genera filas para reporte de órdenes
 */
function generateOrdersReportRows(orders) {
    if (!orders || orders.length === 0) {
        return '<tr><td colspan="5" class="text-center">No hay datos para mostrar</td></tr>';
    }

    return orders.map(order => `
        <tr>
            <td>${order.orderNumber}</td>
            <td>${order.patientName}</td>
            <td>${getOrderTypeLabel(order.type)}</td>
            <td><span class="status-badge status-${order.status?.toLowerCase()}">${order.status || 'Pendiente'}</span></td>
            <td>${formatDate(order.createdDate)}</td>
        </tr>
    `).join('');
}

/**
 * Función para exportar reporte de pacientes
 */
function exportPatientReport() {
    showNotification('Exportando reporte de pacientes...', 'info');
    // Implementar exportación específica del reporte actual
}

/**
 * Función para imprimir reporte de pacientes
 */
function printPatientReport() {
    printContent('reports');
}

/**
 * Función para exportar reporte financiero
 */
function exportFinancialReport() {
    showNotification('Exportando reporte financiero...', 'info');
    // Implementar exportación específica del reporte actual
}

/**
 * Función para imprimir reporte financiero
 */
function printFinancialReport() {
    printContent('reports');
}

/**
 * Función para exportar reporte de órdenes
 */
function exportOrdersReport() {
    showNotification('Exportando reporte de órdenes médicas...', 'info');
    // Implementar exportación específica del reporte actual
}

/**
 * Función para imprimir reporte de órdenes
 */
function printOrdersReport() {
    printContent('reports');
}

/**
 * Función para reintentar último reporte
 */
function retryLastReport() {
    if (currentReport) {
        switch (currentReport.type) {
            case 'patients':
                generatePatientReport();
                break;
            case 'financial':
                generateFinancialReport();
                break;
            case 'orders':
                generateOrdersReport();
                break;
        }
    }
}

// Hacer funciones disponibles globalmente
window.generatePatientReport = generatePatientReport;
window.generateFinancialReport = generateFinancialReport;
window.generateOrdersReport = generateOrdersReport;
window.exportPatientReport = exportPatientReport;
window.printPatientReport = printPatientReport;
window.exportFinancialReport = exportFinancialReport;
window.printFinancialReport = printFinancialReport;
window.exportOrdersReport = exportOrdersReport;
window.printOrdersReport = printOrdersReport;
window.retryLastReport = retryLastReport;