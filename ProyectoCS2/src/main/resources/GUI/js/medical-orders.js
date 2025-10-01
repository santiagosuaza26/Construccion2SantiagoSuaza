/**
 * Sistema de Gestión Clínica CS2 - Módulo de Órdenes Médicas
 * Funcionalidades para Médicos
 */

// Estado del módulo de órdenes médicas
let ordersData = [];
let filteredOrders = [];

/**
 * Carga los datos de órdenes médicas
 */
async function loadOrdersData() {
    if (!checkPermission('medical-orders')) {
        showSectionError('medical-orders', 'No tiene permisos para acceder a esta sección');
        return;
    }

    try {
        showSectionLoading('medical-orders');

        const orders = await OrdersAPI.getAll();
        ordersData = orders;
        filteredOrders = orders;

        renderOrdersTable();

    } catch (error) {
        console.error('Error cargando órdenes médicas:', error);
        const errorMessage = ApiUtils.handleError(error, 'Error al cargar órdenes médicas');
        showSectionError('medical-orders', errorMessage);
    }
}

/**
 * Renderiza la tabla de órdenes médicas
 */
function renderOrdersTable() {
    const tbody = document.getElementById('orders-tbody');
    if (!tbody) return;

    if (filteredOrders.length === 0) {
        tbody.innerHTML = '<tr><td colspan="6" class="text-center">No hay órdenes médicas</td></tr>';
        return;
    }

    tbody.innerHTML = filteredOrders.map(order => `
        <tr>
            <td>${order.orderNumber}</td>
            <td>${order.patientName}</td>
            <td>${getOrderTypeLabel(order.type)}</td>
            <td><span class="status-badge status-${order.status?.toLowerCase()}">${order.status || 'Pendiente'}</span></td>
            <td>${formatDate(order.createdDate)}</td>
            <td class="actions-cell">
                <button onclick="viewOrder('${order.orderNumber}')" class="btn-icon" title="Ver detalles">
                    <i class="fas fa-eye"></i>
                </button>
                <button onclick="editOrder('${order.orderNumber}')" class="btn-icon" title="Editar">
                    <i class="fas fa-edit"></i>
                </button>
                <button onclick="printOrder('${order.orderNumber}')" class="btn-icon" title="Imprimir">
                    <i class="fas fa-print"></i>
                </button>
            </td>
        </tr>
    `).join('');
}

/**
 * Obtiene el label para el tipo de orden
 */
function getOrderTypeLabel(type) {
    const labels = {
        'MEDICATION': 'Medicamentos',
        'PROCEDURE': 'Procedimientos',
        'DIAGNOSTIC': 'Ayudas Diagnósticas'
    };

    return labels[type] || type || 'No especificado';
}

/**
 * Función para mostrar modal de nueva orden médica
 */
function showOrderModal() {
    // Crear modal dinámico para órdenes médicas
    const modal = document.createElement('div');
    modal.className = 'modal';
    modal.innerHTML = `
        <div class="modal-content">
            <div class="modal-header">
                <h2>Nueva Orden Médica</h2>
                <button class="modal-close" onclick="this.closest('.modal').remove()">
                    <i class="fas fa-times"></i>
                </button>
            </div>
            <form id="order-form">
                <div class="form-grid">
                    <div class="form-group">
                        <label>Cédula del Paciente:*</label>
                        <input type="text" name="patientId" required maxlength="20">
                    </div>
                    <div class="form-group">
                        <label>Tipo de Orden:*</label>
                        <select name="type" required onchange="toggleOrderItems()">
                            <option value="">Seleccionar</option>
                            <option value="MEDICATION">Medicamentos</option>
                            <option value="PROCEDURE">Procedimientos</option>
                            <option value="DIAGNOSTIC">Ayudas Diagnósticas</option>
                        </select>
                    </div>
                </div>

                <div id="medication-items" class="order-items" style="display: none;">
                    <h4>Medicamentos</h4>
                    <div class="form-group">
                        <label>Medicamento:</label>
                        <input type="text" name="medication" placeholder="Nombre del medicamento">
                    </div>
                    <div class="form-grid">
                        <div class="form-group">
                            <label>Dosis:</label>
                            <input type="text" name="dosage" placeholder="ej: 500mg">
                        </div>
                        <div class="form-group">
                            <label>Frecuencia:</label>
                            <input type="text" name="frequency" placeholder="ej: 2 veces al día">
                        </div>
                        <div class="form-group">
                            <label>Duración:</label>
                            <input type="text" name="duration" placeholder="ej: 7 días">
                        </div>
                    </div>
                </div>

                <div id="procedure-items" class="order-items" style="display: none;">
                    <h4>Procedimientos</h4>
                    <div class="form-group">
                        <label>Procedimiento:</label>
                        <input type="text" name="procedure" placeholder="Nombre del procedimiento">
                    </div>
                    <div class="form-group">
                        <label>Indicaciones:</label>
                        <textarea name="procedureNotes" rows="3"></textarea>
                    </div>
                </div>

                <div id="diagnostic-items" class="order-items" style="display: none;">
                    <h4>Ayudas Diagnósticas</h4>
                    <div class="form-group">
                        <label>Tipo de Estudio:</label>
                        <select name="diagnosticType">
                            <option value="">Seleccionar</option>
                            <option value="LABORATORY">Laboratorio</option>
                            <option value="IMAGING">Imagenología</option>
                            <option value="CARDIOLOGY">Cardiología</option>
                            <option value="OTHER">Otro</option>
                        </select>
                    </div>
                    <div class="form-group">
                        <label>Descripción:</label>
                        <textarea name="diagnosticDescription" rows="3" placeholder="Describa el estudio solicitado"></textarea>
                    </div>
                </div>

                <div class="form-group">
                    <label>Observaciones:</label>
                    <textarea name="observations" rows="3"></textarea>
                </div>

                <div class="modal-actions">
                    <button type="button" class="btn-secondary" onclick="this.closest('.modal').remove()">Cancelar</button>
                    <button type="submit" class="btn-primary">Crear Orden</button>
                </div>
            </form>
        </div>
    `;

    document.body.appendChild(modal);
    modal.style.display = 'flex';

    // Configurar envío del formulario
    const form = modal.querySelector('#order-form');
    form.onsubmit = (e) => handleOrderSubmit(e, modal);
}

/**
 * Muestra/oculta items según el tipo de orden
 */
function toggleOrderItems() {
    const typeSelect = document.querySelector('#order-form select[name="type"]');
    const type = typeSelect ? typeSelect.value : '';

    // Ocultar todos los items primero
    const itemSections = document.querySelectorAll('.order-items');
    itemSections.forEach(section => {
        section.style.display = 'none';
    });

    // Mostrar sección correspondiente
    if (type) {
        const targetSection = document.getElementById(`${type.toLowerCase()}-items`);
        if (targetSection) {
            targetSection.style.display = 'block';
        }
    }
}

/**
 * Maneja el envío del formulario de orden médica
 */
async function handleOrderSubmit(event, modal) {
    event.preventDefault();

    const form = document.getElementById('order-form');
    const formData = new FormData(form);

    const orderData = {
        patientId: formData.get('patientId').trim(),
        type: formData.get('type'),
        observations: formData.get('observations').trim()
    };

    // Agregar datos específicos según el tipo
    if (orderData.type === 'MEDICATION') {
        orderData.medication = {
            name: formData.get('medication'),
            dosage: formData.get('dosage'),
            frequency: formData.get('frequency'),
            duration: formData.get('duration')
        };
    } else if (orderData.type === 'PROCEDURE') {
        orderData.procedure = {
            name: formData.get('procedure'),
            notes: formData.get('procedureNotes')
        };
    } else if (orderData.type === 'DIAGNOSTIC') {
        orderData.diagnostic = {
            type: formData.get('diagnosticType'),
            description: formData.get('diagnosticDescription')
        };
    }

    // Validar datos
    const validationErrors = validateOrderData(orderData);
    if (validationErrors.length > 0) {
        showValidationErrors(validationErrors);
        return;
    }

    try {
        const result = await OrdersAPI.create(orderData);
        showNotification('Orden médica creada correctamente', 'success');

        modal.remove();
        await loadOrdersData(); // Recargar lista

    } catch (error) {
        console.error('Error creando orden médica:', error);
        const errorMessage = ApiUtils.handleError(error, 'Error al crear orden médica');
        showNotification(errorMessage, 'error');
    }
}

/**
 * Valida datos de la orden médica
 */
function validateOrderData(orderData) {
    const errors = [];

    if (!orderData.patientId) {
        errors.push('La cédula del paciente es requerida');
    } else if (!isValidIdCard(orderData.patientId)) {
        errors.push('La cédula del paciente debe contener solo números');
    }

    if (!orderData.type) {
        errors.push('El tipo de orden es requerido');
    }

    return errors;
}

/**
 * Función para ver detalles de orden médica
 */
function viewOrder(orderNumber) {
    const order = ordersData.find(o => o.orderNumber === orderNumber);
    if (!order) return;

    let details = `
        <div class="order-details">
            <h3>Orden Médica #${order.orderNumber}</h3>
            <div class="details-grid">
                <div class="detail-item">
                    <strong>Paciente:</strong> ${order.patientName}
                </div>
                <div class="detail-item">
                    <strong>Tipo:</strong> ${getOrderTypeLabel(order.type)}
                </div>
                <div class="detail-item">
                    <strong>Estado:</strong> <span class="status-badge status-${order.status?.toLowerCase()}">${order.status || 'Pendiente'}</span>
                </div>
                <div class="detail-item">
                    <strong>Fecha:</strong> ${formatDate(order.createdDate)}
                </div>
            </div>
            ${order.observations ? `<div class="detail-item"><strong>Observaciones:</strong> ${order.observations}</div>` : ''}
        </div>
    `;

    showNotification(details, 'info');
}

/**
 * Función para editar orden médica
 */
function editOrder(orderNumber) {
    showNotification('Función de edición pendiente de implementar', 'info');
}

/**
 * Función para imprimir orden médica
 */
function printOrder(orderNumber) {
    showNotification('Función de impresión pendiente de implementar', 'info');
}

/**
 * Función para exportar órdenes médicas
 */
function exportOrders() {
    if (filteredOrders.length === 0) {
        showNotification('No hay órdenes médicas para exportar', 'warning');
        return;
    }

    const dataToExport = filteredOrders.map(order => ({
        'Número': order.orderNumber,
        'Paciente': order.patientName,
        'Tipo': getOrderTypeLabel(order.type),
        'Estado': order.status || 'Pendiente',
        'Fecha': formatDate(order.createdDate),
        'Observaciones': order.observations || ''
    }));

    exportToCSV(dataToExport, `ordenes_medicas_${new Date().toISOString().split('T')[0]}.csv`);
}

// Hacer funciones disponibles globalmente
window.loadOrdersData = loadOrdersData;
window.showOrderModal = showOrderModal;
window.viewOrder = viewOrder;
window.editOrder = editOrder;
window.printOrder = printOrder;
window.exportOrders = exportOrders;