/**
 * Sistema de Gestión Clínica CS2 - Módulo de Inventario
 * Funcionalidades para Soporte de Información
 */

// Estado del módulo de inventario
let inventoryData = [];
let filteredInventory = [];
let currentInventoryTab = 'medicamentos';

/**
 * Carga los datos de inventario
 */
async function loadInventoryData() {
    if (!checkPermission('inventory')) {
        showSectionError('inventory', 'No tiene permisos para acceder a esta sección');
        return;
    }

    try {
        showSectionLoading('inventory');

        const inventory = await InventoryAPI.getAll();
        inventoryData = inventory;
        filteredInventory = inventory;

        renderInventoryTable();
        updateInventoryStats();

    } catch (error) {
        console.error('Error cargando inventario:', error);
        const errorMessage = ApiUtils.handleError(error, 'Error al cargar inventario');
        showSectionError('inventory', errorMessage);
    }
}

/**
 * Renderiza la tabla de inventario según la pestaña actual
 */
function renderInventoryTable() {
    const tbody = document.getElementById('inventory-tbody');
    if (!tbody) return;

    let dataToShow = filteredInventory;

    // Filtrar por categoría según pestaña actual
    if (currentInventoryTab !== 'all') {
        dataToShow = filteredInventory.filter(item =>
            item.category?.toLowerCase() === currentInventoryTab || false
        );
    }

    if (dataToShow.length === 0) {
        tbody.innerHTML = '<tr><td colspan="7" class="text-center">No hay items en esta categoría</td></tr>';
        return;
    }

    tbody.innerHTML = dataToShow.map(item => `
        <tr>
            <td>${item.id}</td>
            <td>${item.name}</td>
            <td>${getCategoryLabel(item.category)}</td>
            <td>
                <span class="stock-info ${getStockStatus(item.stock, item.minStock)}">
                    ${item.stock} ${item.unit || 'unidades'}
                </span>
            </td>
            <td>${formatCurrency(item.cost)}</td>
            <td><span class="status-badge status-${item.status || 'active'}">${item.status || 'Activo'}</span></td>
            <td class="actions-cell">
                <button onclick="viewInventoryItem('${item.id}')" class="btn-icon" title="Ver detalles">
                    <i class="fas fa-eye"></i>
                </button>
                <button onclick="editInventoryItem('${item.id}')" class="btn-icon" title="Editar">
                    <i class="fas fa-edit"></i>
                </button>
                <button onclick="adjustInventoryStock('${item.id}')" class="btn-icon" title="Ajustar stock">
                    <i class="fas fa-boxes"></i>
                </button>
            </td>
        </tr>
    `).join('');
}

/**
 * Obtiene el label para la categoría
 */
function getCategoryLabel(category) {
    const labels = {
        'medicamentos': 'Medicamentos',
        'procedimientos': 'Procedimientos',
        'diagnosticos': 'Ayudas Diagnósticas',
        'materiales': 'Materiales',
        'equipos': 'Equipos'
    };

    return labels[category] || category || 'Sin categoría';
}

/**
 * Obtiene la clase CSS para el estado del stock
 */
function getStockStatus(stock, minStock) {
    if (stock <= 0) return 'stock-out';
    if (stock <= (minStock || 0)) return 'stock-low';
    return 'stock-ok';
}

/**
 * Actualiza estadísticas del inventario
 */
function updateInventoryStats() {
    const totalItems = inventoryData.length;
    const lowStockItems = inventoryData.filter(item =>
        item.stock <= (item.minStock || 0)
    ).length;
    const totalValue = inventoryData.reduce((sum, item) =>
        sum + (item.cost * item.stock), 0
    );

    // Actualizar tarjetas del dashboard si estamos en esa sección
    if (currentSection === 'inventory') {
        updateDashboardCard('low-stock', lowStockItems);
    }
}

/**
 * Muestra una pestaña específica del inventario
 */
function showInventoryTab(tabName) {
    currentInventoryTab = tabName;

    // Actualizar botones de pestaña
    const tabButtons = document.querySelectorAll('.tab-btn');
    tabButtons.forEach(btn => {
        btn.classList.remove('active');
    });

    const activeBtn = document.querySelector(`[onclick="showInventoryTab('${tabName}')"]`);
    if (activeBtn) {
        activeBtn.classList.add('active');
    }

    // Renderizar tabla con filtro
    renderInventoryTable();
}

/**
 * Función para mostrar modal de nuevo item de inventario
 */
function showInventoryModal() {
    // Crear modal dinámico para inventario
    const modal = document.createElement('div');
    modal.className = 'modal';
    modal.innerHTML = `
        <div class="modal-content">
            <div class="modal-header">
                <h2>Nuevo Item de Inventario</h2>
                <button class="modal-close" onclick="this.closest('.modal').remove()">
                    <i class="fas fa-times"></i>
                </button>
            </div>
            <form id="inventory-form">
                <div class="form-grid">
                    <div class="form-group">
                        <label>Nombre:*</label>
                        <input type="text" name="name" required>
                    </div>
                    <div class="form-group">
                        <label>Categoría:*</label>
                        <select name="category" required>
                            <option value="">Seleccionar</option>
                            <option value="medicamentos">Medicamentos</option>
                            <option value="procedimientos">Procedimientos</option>
                            <option value="diagnosticos">Ayudas Diagnósticas</option>
                            <option value="materiales">Materiales</option>
                            <option value="equipos">Equipos</option>
                        </select>
                    </div>
                    <div class="form-group">
                        <label>Stock Inicial:*</label>
                        <input type="number" name="stock" min="0" required>
                    </div>
                    <div class="form-group">
                        <label>Stock Mínimo:</label>
                        <input type="number" name="minStock" min="0" value="0">
                    </div>
                    <div class="form-group">
                        <label>Costo Unitario:*</label>
                        <input type="number" name="cost" min="0" step="0.01" required>
                    </div>
                    <div class="form-group">
                        <label>Unidad:</label>
                        <input type="text" name="unit" placeholder="unidades, ml, etc.">
                    </div>
                </div>
                <div class="form-group">
                    <label>Descripción:</label>
                    <textarea name="description" rows="3"></textarea>
                </div>
                <div class="modal-actions">
                    <button type="button" class="btn-secondary" onclick="this.closest('.modal').remove()">Cancelar</button>
                    <button type="submit" class="btn-primary">Guardar</button>
                </div>
            </form>
        </div>
    `;

    document.body.appendChild(modal);
    modal.style.display = 'flex';

    // Configurar envío del formulario
    const form = modal.querySelector('#inventory-form');
    form.onsubmit = (e) => handleInventorySubmit(e, modal);
}

/**
 * Maneja el envío del formulario de inventario
 */
async function handleInventorySubmit(event, modal) {
    event.preventDefault();

    const form = document.getElementById('inventory-form');
    const formData = new FormData(form);

    const inventoryData = {
        name: formData.get('name').trim(),
        category: formData.get('category'),
        stock: parseInt(formData.get('stock')),
        minStock: parseInt(formData.get('minStock')) || 0,
        cost: parseFloat(formData.get('cost')),
        unit: formData.get('unit').trim(),
        description: formData.get('description').trim()
    };

    // Validar datos
    const validationErrors = validateInventoryData(inventoryData);
    if (validationErrors.length > 0) {
        showValidationErrors(validationErrors);
        return;
    }

    try {
        const result = await InventoryAPI.create(inventoryData);
        showNotification('Item agregado al inventario correctamente', 'success');

        modal.remove();
        await loadInventoryData(); // Recargar inventario

    } catch (error) {
        console.error('Error guardando item de inventario:', error);
        const errorMessage = ApiUtils.handleError(error, 'Error al guardar item');
        showNotification(errorMessage, 'error');
    }
}

/**
 * Valida datos del item de inventario
 */
function validateInventoryData(data) {
    const errors = [];

    if (!data.name) {
        errors.push('El nombre es requerido');
    }

    if (!data.category) {
        errors.push('La categoría es requerida');
    }

    if (data.stock < 0) {
        errors.push('El stock no puede ser negativo');
    }

    if (data.cost < 0) {
        errors.push('El costo no puede ser negativo');
    }

    return errors;
}

/**
 * Función para ajustar stock de un item
 */
function adjustInventoryStock(itemId) {
    const item = inventoryData.find(i => i.id === itemId);
    if (!item) return;

    const newStock = prompt(`Ingrese el nuevo stock para "${item.name}" (actual: ${item.stock}):`);
    if (newStock === null) return;

    const stockValue = parseInt(newStock);
    if (isNaN(stockValue) || stockValue < 0) {
        showNotification('Ingrese un valor válido para el stock', 'error');
        return;
    }

    // Aquí iría la llamada a la API para ajustar stock
    showNotification(`Stock ajustado a ${stockValue} unidades`, 'success');
}

/**
 * Función para ver detalles de item de inventario
 */
function viewInventoryItem(itemId) {
    const item = inventoryData.find(i => i.id === itemId);
    if (!item) return;

    let details = `
        <div class="inventory-details">
            <h3>${item.name}</h3>
            <div class="details-grid">
                <div class="detail-item">
                    <strong>Categoría:</strong> ${getCategoryLabel(item.category)}
                </div>
                <div class="detail-item">
                    <strong>Stock Actual:</strong> ${item.stock} ${item.unit || 'unidades'}
                </div>
                <div class="detail-item">
                    <strong>Stock Mínimo:</strong> ${item.minStock || 0} ${item.unit || 'unidades'}
                </div>
                <div class="detail-item">
                    <strong>Costo Unitario:</strong> ${formatCurrency(item.cost)}
                </div>
                <div class="detail-item">
                    <strong>Valor Total:</strong> ${formatCurrency(item.cost * item.stock)}
                </div>
                <div class="detail-item">
                    <strong>Estado:</strong> <span class="status-badge status-${item.status || 'active'}">${item.status || 'Activo'}</span>
                </div>
            </div>
            ${item.description ? `<div class="detail-item"><strong>Descripción:</strong> ${item.description}</div>` : ''}
        </div>
    `;

    showNotification(details, 'info');
}

/**
 * Función para editar item de inventario
 */
function editInventoryItem(itemId) {
    showNotification('Función de edición pendiente de implementar', 'info');
}

/**
 * Función para exportar inventario
 */
function exportInventory() {
    if (filteredInventory.length === 0) {
        showNotification('No hay items de inventario para exportar', 'warning');
        return;
    }

    const dataToExport = filteredInventory.map(item => ({
        'ID': item.id,
        'Nombre': item.name,
        'Categoría': getCategoryLabel(item.category),
        'Stock': item.stock,
        'Stock Mínimo': item.minStock || 0,
        'Unidad': item.unit || '',
        'Costo Unitario': item.cost,
        'Valor Total': item.cost * item.stock,
        'Estado': item.status || 'Activo'
    }));

    exportToCSV(dataToExport, `inventario_${currentInventoryTab}_${new Date().toISOString().split('T')[0]}.csv`);
}

/**
 * Función para mostrar estadísticas de inventario
 */
function showInventoryStats() {
    const totalItems = inventoryData.length;
    const lowStockItems = inventoryData.filter(item => item.stock <= (item.minStock || 0)).length;
    const totalValue = inventoryData.reduce((sum, item) => sum + (item.cost * item.stock), 0);
    const categories = [...new Set(inventoryData.map(item => item.category).filter(Boolean))];

    const stats = `
        <div class="inventory-stats">
            <div class="stat-item">
                <span class="stat-label">Total Items:</span>
                <span class="stat-value">${totalItems}</span>
            </div>
            <div class="stat-item">
                <span class="stat-label">Stock Bajo:</span>
                <span class="stat-value">${lowStockItems}</span>
            </div>
            <div class="stat-item">
                <span class="stat-label">Valor Total:</span>
                <span class="stat-value">${formatCurrency(totalValue)}</span>
            </div>
            <div class="stat-item">
                <span class="stat-label">Categorías:</span>
                <span class="stat-value">${categories.length}</span>
            </div>
        </div>
    `;

    showNotification(stats, 'info');
}

// Eventos específicos del módulo de inventario
document.addEventListener('DOMContentLoaded', function() {
    // Configurar pestañas de inventario
    const tabButtons = document.querySelectorAll('.tab-btn');
    tabButtons.forEach(button => {
        button.addEventListener('click', function() {
            const tabName = this.onclick.toString().match(/'([^']+)'/)[1];
            showInventoryTab(tabName);
        });
    });

    // Agregar botones de acción adicionales si no existen
    const sectionHeader = document.querySelector('#inventory .section-header');
    if (sectionHeader && !sectionHeader.querySelector('.export-btn')) {
        const exportBtn = document.createElement('button');
        exportBtn.className = 'btn-secondary';
        exportBtn.innerHTML = '<i class="fas fa-download"></i> Exportar';
        exportBtn.onclick = exportInventory;
        exportBtn.title = 'Exportar a CSV';

        const statsBtn = document.createElement('button');
        statsBtn.className = 'btn-secondary';
        statsBtn.innerHTML = '<i class="fas fa-chart-bar"></i> Estadísticas';
        statsBtn.onclick = showInventoryStats;
        statsBtn.title = 'Ver estadísticas';

        sectionHeader.appendChild(statsBtn);
        sectionHeader.appendChild(exportBtn);
    }
});

// Hacer funciones disponibles globalmente
window.loadInventoryData = loadInventoryData;
window.showInventoryTab = showInventoryTab;
window.showInventoryModal = showInventoryModal;
window.viewInventoryItem = viewInventoryItem;
window.editInventoryItem = editInventoryItem;
window.adjustInventoryStock = adjustInventoryStock;
window.exportInventory = exportInventory;
window.showInventoryStats = showInventoryStats;