/**
 * Sistema de Gestión Clínica CS2 - Módulo de Facturación
 * Funcionalidades para Administrativo
 */

// Estado del módulo de facturación
let billingData = [];
let filteredBills = [];

/**
 * Carga los datos de facturación
 */
async function loadBillingData() {
    if (!checkPermission('billing')) {
        showSectionError('billing', 'No tiene permisos para acceder a esta sección');
        return;
    }

    try {
        showSectionLoading('billing');

        const bills = await BillingAPI.getAll();
        billingData = bills;
        filteredBills = bills;

        renderBillingTable();

    } catch (error) {
        console.error('Error cargando facturación:', error);
        const errorMessage = ApiUtils.handleError(error, 'Error al cargar facturación');
        showSectionError('billing', errorMessage);
    }
}

/**
 * Renderiza la tabla de facturación
 */
function renderBillingTable() {
    const tbody = document.getElementById('billing-tbody');
    if (!tbody) return;

    if (filteredBills.length === 0) {
        tbody.innerHTML = '<tr><td colspan="6" class="text-center">No hay facturas registradas</td></tr>';
        return;
    }

    tbody.innerHTML = filteredBills.map(bill => `
        <tr>
            <td>${bill.invoiceNumber}</td>
            <td>${bill.patientName}</td>
            <td>${formatDate(bill.issueDate)}</td>
            <td>${formatCurrency(bill.totalAmount)}</td>
            <td><span class="status-badge status-${bill.status?.toLowerCase()}">${bill.status || 'Pendiente'}</span></td>
            <td class="actions-cell">
                <button onclick="viewBill('${bill.invoiceNumber}')" class="btn-icon" title="Ver detalles">
                    <i class="fas fa-eye"></i>
                </button>
                <button onclick="printBill('${bill.invoiceNumber}')" class="btn-icon" title="Imprimir">
                    <i class="fas fa-print"></i>
                </button>
                <button onclick="downloadBill('${bill.invoiceNumber}')" class="btn-icon" title="Descargar PDF">
                    <i class="fas fa-download"></i>
                </button>
            </td>
        </tr>
    `).join('');
}

/**
 * Función para mostrar modal de nueva factura
 */
function showBillingModal() {
    // Crear modal dinámico para facturación
    const modal = document.createElement('div');
    modal.className = 'modal';
    modal.innerHTML = `
        <div class="modal-content">
            <div class="modal-header">
                <h2>Nueva Factura</h2>
                <button class="modal-close" onclick="this.closest('.modal').remove()">
                    <i class="fas fa-times"></i>
                </button>
            </div>
            <form id="billing-form">
                <div class="form-grid">
                    <div class="form-group">
                        <label>Cédula del Paciente:*</label>
                        <input type="text" name="patientId" required maxlength="20">
                    </div>
                    <div class="form-group">
                        <label>ID del Médico Tratante:*</label>
                        <input type="text" name="doctorId" required maxlength="15">
                    </div>
                </div>

                <div class="form-section">
                    <h3>Detalles de la Factura</h3>
                    <div class="form-grid">
                        <div class="form-group">
                            <label>Compañía Aseguradora:</label>
                            <input type="text" name="insuranceCompany">
                        </div>
                        <div class="form-group">
                            <label>Número de Póliza:</label>
                            <input type="text" name="policyNumber">
                        </div>
                    </div>
                </div>

                <div class="form-section">
                    <h3>Conceptos</h3>
                    <div id="billing-items">
                        <div class="billing-item">
                            <div class="form-grid">
                                <div class="form-group">
                                    <label>Descripción:</label>
                                    <input type="text" name="description[]" placeholder="Descripción del concepto">
                                </div>
                                <div class="form-group">
                                    <label>Valor:</label>
                                    <input type="number" name="amount[]" min="0" step="0.01" placeholder="0.00">
                                </div>
                            </div>
                        </div>
                    </div>
                    <button type="button" class="btn-secondary" onclick="addBillingItem()">
                        <i class="fas fa-plus"></i> Agregar Concepto
                    </button>
                </div>

                <div class="form-section">
                    <h3>Resumen</h3>
                    <div class="billing-summary">
                        <div class="summary-item">
                            <span>Subtotal:</span>
                            <span id="subtotal">$0.00</span>
                        </div>
                        <div class="summary-item">
                            <span>Descuento:</span>
                            <span id="discount">$0.00</span>
                        </div>
                        <div class="summary-item total">
                            <span>Total:</span>
                            <span id="total">$0.00</span>
                        </div>
                    </div>
                </div>

                <div class="modal-actions">
                    <button type="button" class="btn-secondary" onclick="this.closest('.modal').remove()">Cancelar</button>
                    <button type="submit" class="btn-primary">Generar Factura</button>
                </div>
            </form>
        </div>
    `;

    document.body.appendChild(modal);
    modal.style.display = 'flex';

    // Configurar envío del formulario
    const form = modal.querySelector('#billing-form');
    form.onsubmit = (e) => handleBillingSubmit(e, modal);

    // Configurar cálculo automático
    setupBillingCalculations(form);
}

/**
 * Agrega un nuevo item a la factura
 */
function addBillingItem() {
    const itemsContainer = document.getElementById('billing-items');
    if (!itemsContainer) return;

    const itemDiv = document.createElement('div');
    itemDiv.className = 'billing-item';
    itemDiv.innerHTML = `
        <div class="form-grid">
            <div class="form-group">
                <label>Descripción:</label>
                <input type="text" name="description[]" placeholder="Descripción del concepto">
            </div>
            <div class="form-group">
                <label>Valor:</label>
                <input type="number" name="amount[]" min="0" step="0.01" placeholder="0.00">
            </div>
        </div>
        <button type="button" class="btn-danger btn-small" onclick="this.parentNode.remove()">
            <i class="fas fa-trash"></i> Eliminar
        </button>
    `;

    itemsContainer.appendChild(itemDiv);
}

/**
 * Configura cálculos automáticos de la factura
 */
function setupBillingCalculations(form) {
    const amountInputs = form.querySelectorAll('input[name="amount[]"]');

    amountInputs.forEach(input => {
        input.addEventListener('input', calculateBillingTotal);
    });
}

/**
 * Calcula el total de la factura
 */
function calculateBillingTotal() {
    const amountInputs = document.querySelectorAll('#billing-form input[name="amount[]"]');
    let subtotal = 0;

    amountInputs.forEach(input => {
        const value = parseFloat(input.value) || 0;
        subtotal += value;
    });

    // Calcular descuento (simulado)
    const discount = subtotal * 0.1; // 10% de descuento si aplica
    const total = subtotal - discount;

    // Actualizar display
    document.getElementById('subtotal').textContent = formatCurrency(subtotal);
    document.getElementById('discount').textContent = formatCurrency(discount);
    document.getElementById('total').textContent = formatCurrency(total);
}

/**
 * Maneja el envío del formulario de facturación
 */
async function handleBillingSubmit(event, modal) {
    event.preventDefault();

    const form = document.getElementById('billing-form');
    const formData = new FormData(form);

    // Recopilar datos de conceptos
    const descriptions = formData.getAll('description[]');
    const amounts = formData.getAll('amount[]');

    const concepts = [];
    for (let i = 0; i < descriptions.length; i++) {
        if (descriptions[i].trim() && amounts[i]) {
            concepts.push({
                description: descriptions[i].trim(),
                amount: parseFloat(amounts[i])
            });
        }
    }

    const billingData = {
        patientId: formData.get('patientId').trim(),
        doctorId: formData.get('doctorId').trim(),
        insuranceCompany: formData.get('insuranceCompany').trim(),
        policyNumber: formData.get('policyNumber').trim(),
        items: concepts
    };

    // Validar datos
    const validationErrors = validateBillingData(billingData);
    if (validationErrors.length > 0) {
        showValidationErrors(validationErrors);
        return;
    }

    try {
        const result = await BillingAPI.generateInvoice(billingData);
        showNotification('Factura generada correctamente', 'success');

        modal.remove();
        await loadBillingData(); // Recargar lista

    } catch (error) {
        console.error('Error generando factura:', error);
        const errorMessage = ApiUtils.handleError(error, 'Error al generar factura');
        showNotification(errorMessage, 'error');
    }
}

/**
 * Valida datos de facturación
 */
function validateBillingData(billingData) {
    const errors = [];

    if (!billingData.patientId) {
        errors.push('La cédula del paciente es requerida');
    } else if (!isValidIdCard(billingData.patientId)) {
        errors.push('La cédula del paciente debe contener solo números');
    }

    if (!billingData.doctorId) {
        errors.push('El ID del médico tratante es requerido');
    }

    if (billingData.items.length === 0) {
        errors.push('Debe agregar al menos un concepto');
    }

    return errors;
}

/**
 * Función para ver detalles de factura
 */
function viewBill(invoiceNumber) {
    const bill = billingData.find(b => b.invoiceNumber === invoiceNumber);
    if (!bill) return;

    let details = `
        <div class="bill-details">
            <h3>Factura #${bill.invoiceNumber}</h3>
            <div class="details-grid">
                <div class="detail-item">
                    <strong>Paciente:</strong> ${bill.patientName}
                </div>
                <div class="detail-item">
                    <strong>Fecha:</strong> ${formatDate(bill.issueDate)}
                </div>
                <div class="detail-item">
                    <strong>Total:</strong> ${formatCurrency(bill.totalAmount)}
                </div>
                <div class="detail-item">
                    <strong>Estado:</strong> <span class="status-badge status-${bill.status?.toLowerCase()}">${bill.status || 'Pendiente'}</span>
                </div>
            </div>
        </div>
    `;

    showNotification(details, 'info');
}

/**
 * Función para imprimir factura
 */
function printBill(invoiceNumber) {
    showNotification('Función de impresión pendiente de implementar', 'info');
}

/**
 * Función para descargar factura en PDF
 */
function downloadBill(invoiceNumber) {
    showNotification('Función de descarga PDF pendiente de implementar', 'info');
}

/**
 * Función para exportar facturación
 */
function exportBilling() {
    if (filteredBills.length === 0) {
        showNotification('No hay facturas para exportar', 'warning');
        return;
    }

    const dataToExport = filteredBills.map(bill => ({
        'Número': bill.invoiceNumber,
        'Paciente': bill.patientName,
        'Fecha': formatDate(bill.issueDate),
        'Total': bill.totalAmount,
        'Estado': bill.status || 'Pendiente'
    }));

    exportToCSV(dataToExport, `facturacion_${new Date().toISOString().split('T')[0]}.csv`);
}

// Hacer funciones disponibles globalmente
window.loadBillingData = loadBillingData;
window.showBillingModal = showBillingModal;
window.viewBill = viewBill;
window.printBill = printBill;
window.downloadBill = downloadBill;
window.exportBilling = exportBilling;