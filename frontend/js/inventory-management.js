/**
 * Servicio de Gestión de Inventario
 * Maneja las operaciones de inventario médico para soporte técnico
 */
class InventoryManagementService {
    constructor() {
        this.currentItem = null;
        this.inventoryItems = [];
        this.filteredItems = [];
        this.currentPage = 1;
        this.itemsPerPage = 10;
        this.searchTerm = '';
        this.selectedType = '';
    }

    /**
     * Carga la lista de items de inventario
     */
    async loadInventoryList() {
        try {
            window.showInfo('Cargando inventario...');

            const response = await window.inventoryApi.findAllInventoryItems();

            if (response && Array.isArray(response)) {
                this.inventoryItems = response;
                this.filteredItems = [...this.inventoryItems];
                this.renderInventoryList();
                window.showSuccess(`Se cargaron ${this.inventoryItems.length} items de inventario`);
            } else {
                throw new Error('Formato de respuesta inválido');
            }

        } catch (error) {
            console.error('Error loading inventory:', error);
            window.showError('Error al cargar inventario: ' + error.message);
            this.showEmptyState();
        }
    }

    /**
     * Renderiza la lista de inventario
     */
    renderInventoryList() {
        const contentArea = document.getElementById('content-area');
        if (!contentArea) return;

        const startIndex = (this.currentPage - 1) * this.itemsPerPage;
        const endIndex = startIndex + this.itemsPerPage;
        const itemsToShow = this.filteredItems.slice(startIndex, endIndex);

        contentArea.innerHTML = `
            <div class="content-header">
                <h2><i class="fas fa-boxes"></i> Gestión de Inventario</h2>
                <p>Control y seguimiento de inventario médico</p>
            </div>

            <div class="content-actions">
                <div class="search-box">
                    <i class="fas fa-search search-icon"></i>
                    <input type="text" id="inventory-search" class="search-input"
                           placeholder="Buscar por nombre o descripción..."
                           value="${this.searchTerm}">
                </div>

                <div class="filter-box">
                    <select id="type-filter" class="filter-select">
                        <option value="">Todos los tipos</option>
                        <option value="MEDICAMENTO">Medicamentos</option>
                        <option value="SUMINISTRO">Suministros</option>
                        <option value="EQUIPO">Equipo Médico</option>
                        <option value="MATERIAL">Material</option>
                    </select>
                </div>

                <button class="btn btn-primary" onclick="inventoryManagementService.showCreateItemModal()">
                    <i class="fas fa-plus"></i> Agregar Ítem
                </button>
            </div>

            <div class="stats-grid">
                <div class="stat-card">
                    <div class="stat-value">${this.inventoryItems.length}</div>
                    <div class="stat-label">Total Items</div>
                </div>
                <div class="stat-card">
                    <div class="stat-value">${this.getLowStockCount()}</div>
                    <div class="stat-label">Stock Bajo</div>
                </div>
                <div class="stat-card">
                    <div class="stat-value">${this.getItemsByTypeCount('MEDICAMENTO')}</div>
                    <div class="stat-label">Medicamentos</div>
                </div>
                <div class="stat-card">
                    <div class="stat-value">${this.getItemsByTypeCount('EQUIPO')}</div>
                    <div class="stat-label">Equipo</div>
                </div>
            </div>

            <div class="table-container">
                <table class="table">
                    <thead>
                        <tr>
                            <th>Nombre</th>
                            <th>Tipo</th>
                            <th>Cantidad</th>
                            <th>Stock Mínimo</th>
                            <th>Costo Unitario</th>
                            <th>Estado</th>
                            <th>Acciones</th>
                        </tr>
                    </thead>
                    <tbody id="inventory-table-body">
                        ${this.renderInventoryTableRows(itemsToShow)}
                    </tbody>
                </table>
            </div>

            ${this.renderPagination()}

            <!-- Modal para crear/editar item de inventario -->
            <div id="inventory-modal" class="modal-overlay" style="display: none;">
                <div class="modal">
                    <div class="modal-header">
                        <h3 id="inventory-modal-title">Agregar Ítem al Inventario</h3>
                        <button class="modal-close" onclick="inventoryManagementService.closeInventoryModal()">
                            <i class="fas fa-times"></i>
                        </button>
                    </div>
                    <div class="modal-body">
                        <form id="inventory-form">
                            ${this.renderInventoryForm()}
                        </form>
                    </div>
                    <div class="modal-footer">
                        <button class="btn btn-secondary" onclick="inventoryManagementService.closeInventoryModal()">
                            Cancelar
                        </button>
                        <button class="btn btn-primary" onclick="inventoryManagementService.saveInventoryItem()">
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
     * Renderiza las filas de la tabla de inventario
     */
    renderInventoryTableRows(items) {
        if (items.length === 0) {
            return `
                <tr>
                    <td colspan="7" class="text-center" style="padding: 2rem;">
                        <i class="fas fa-boxes" style="font-size: 3rem; color: var(--text-secondary); margin-bottom: 1rem;"></i>
                        <p style="color: var(--text-secondary);">No se encontraron items de inventario</p>
                    </td>
                </tr>
            `;
        }

        return items.map(item => `
            <tr class="${this.getStockStatusClass(item)}">
                <td>${item.name}</td>
                <td>
                    <span class="badge ${this.getTypeBadgeClass(item.type)}">
                        ${this.getTypeDisplayName(item.type)}
                    </span>
                </td>
                <td>
                    <span class="${this.getQuantityClass(item)}">${item.quantity}</span>
                </td>
                <td>${item.minimumStock}</td>
                <td>${this.formatCurrency(item.unitCost)}</td>
                <td>
                    <span class="badge ${this.getStatusBadgeClass(item)}">
                        ${this.getStatusDisplayName(item)}
                    </span>
                </td>
                <td>
                    <div class="btn-group">
                        <button class="btn btn-sm btn-primary"
                                onclick="inventoryManagementService.editInventoryItem('${item.id}')"
                                title="Editar">
                            <i class="fas fa-edit"></i>
                        </button>
                        <button class="btn btn-sm btn-info"
                                onclick="inventoryManagementService.adjustStock('${item.id}')"
                                title="Ajustar Stock">
                            <i class="fas fa-plus-circle"></i>
                        </button>
                        <button class="btn btn-sm btn-danger"
                                onclick="inventoryManagementService.deleteInventoryItem('${item.id}')"
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
        const totalPages = Math.ceil(this.filteredItems.length / this.itemsPerPage);

        if (totalPages <= 1) return '';

        let paginationHtml = `
            <div class="pagination">
        `;

        // Botón anterior
        if (this.currentPage > 1) {
            paginationHtml += `<button class="pagination-btn" onclick="inventoryManagementService.goToPage(${this.currentPage - 1})">
                <i class="fas fa-chevron-left"></i>
            </button>`;
        }

        // Páginas
        for (let i = 1; i <= totalPages; i++) {
            if (i === this.currentPage) {
                paginationHtml += `<button class="pagination-btn active">${i}</button>`;
            } else if (i >= this.currentPage - 2 && i <= this.currentPage + 2) {
                paginationHtml += `<button class="pagination-btn" onclick="inventoryManagementService.goToPage(${i})">${i}</button>`;
            }
        }

        // Botón siguiente
        if (this.currentPage < totalPages) {
            paginationHtml += `<button class="pagination-btn" onclick="inventoryManagementService.goToPage(${this.currentPage + 1})">
                <i class="fas fa-chevron-right"></i>
            </button>`;
        }

        paginationHtml += '</div>';
        return paginationHtml;
    }

    /**
     * Renderiza el formulario de item de inventario
     */
    renderInventoryForm() {
        return `
            <div class="form-row">
                <div class="form-group">
                    <label for="item-name">Nombre del Ítem *</label>
                    <input type="text" id="item-name" name="name" required maxlength="100">
                </div>
                <div class="form-group">
                    <label for="item-type">Tipo *</label>
                    <select id="item-type" name="type" required>
                        <option value="">Seleccionar tipo</option>
                        <option value="MEDICAMENTO">Medicamento</option>
                        <option value="SUMINISTRO">Suministro Médico</option>
                        <option value="EQUIPO">Equipo Médico</option>
                        <option value="MATERIAL">Material</option>
                    </select>
                </div>
            </div>

            <div class="form-row">
                <div class="form-group">
                    <label for="item-description">Descripción</label>
                    <textarea id="item-description" name="description" rows="3" maxlength="200"></textarea>
                </div>
            </div>

            <div class="form-row">
                <div class="form-group">
                    <label for="item-quantity">Cantidad Actual *</label>
                    <input type="number" id="item-quantity" name="quantity" required min="0">
                </div>
                <div class="form-group">
                    <label for="item-minimum-stock">Stock Mínimo *</label>
                    <input type="number" id="item-minimum-stock" name="minimumStock" required min="0">
                </div>
            </div>

            <div class="form-row">
                <div class="form-group">
                    <label for="item-unit-cost">Costo Unitario *</label>
                    <input type="number" id="item-unit-cost" name="unitCost" required min="0" step="0.01">
                </div>
                <div class="form-group">
                    <label for="item-expiration">Fecha de Vencimiento</label>
                    <input type="date" id="item-expiration" name="expirationDate">
                </div>
            </div>

            <div class="form-row">
                <div class="form-group">
                    <label for="item-supplier">Proveedor</label>
                    <input type="text" id="item-supplier" name="supplier" maxlength="100">
                </div>
                <div class="form-group">
                    <label for="item-location">Ubicación</label>
                    <input type="text" id="item-location" name="location" maxlength="50" placeholder="Ej: Farmacia, Sala 1, etc.">
                </div>
            </div>

            <div class="form-row">
                <div class="form-group">
                    <label class="checkbox-label">
                        <input type="checkbox" id="item-controlled" name="isControlledSubstance">
                        Es sustancia controlada
                    </label>
                </div>
                <div class="form-group">
                    <label class="checkbox-label">
                        <input type="checkbox" id="item-active" name="isActive" checked>
                        Ítem activo
                    </label>
                </div>
            </div>
        `;
    }

    /**
     * Configura los event listeners
     */
    setupEventListeners() {
        // Búsqueda
        const searchInput = document.getElementById('inventory-search');
        if (searchInput) {
            searchInput.addEventListener('input', window.debounce((e) => {
                this.searchTerm = e.target.value;
                this.filterInventory();
            }, 300));
        }

        // Filtro por tipo
        const typeFilter = document.getElementById('type-filter');
        if (typeFilter) {
            typeFilter.addEventListener('change', (e) => {
                this.selectedType = e.target.value;
                this.filterInventory();
            });
        }

        // Validación del formulario
        const inventoryForm = document.getElementById('inventory-form');
        if (inventoryForm) {
            inventoryForm.addEventListener('submit', (e) => {
                e.preventDefault();
                this.saveInventoryItem();
            });
        }
    }

    /**
     * Filtra el inventario según término de búsqueda y tipo
     */
    filterInventory() {
        let filtered = [...this.inventoryItems];

        // Filtrar por término de búsqueda
        if (this.searchTerm) {
            const term = this.searchTerm.toLowerCase();
            filtered = filtered.filter(item =>
                item.name.toLowerCase().includes(term) ||
                (item.description && item.description.toLowerCase().includes(term))
            );
        }

        // Filtrar por tipo
        if (this.selectedType) {
            filtered = filtered.filter(item => item.type === this.selectedType);
        }

        this.filteredItems = filtered;
        this.currentPage = 1;
        this.renderInventoryList();
    }

    /**
     * Muestra el modal para crear item de inventario
     */
    showCreateItemModal() {
        this.currentItem = null;
        this.showInventoryModal();
    }

    /**
     * Muestra el modal para editar item de inventario
     */
    async editInventoryItem(itemId) {
        try {
            window.showInfo('Cargando item de inventario...');

            const item = await window.inventoryApi.findInventoryItemById(itemId);

            if (item) {
                this.currentItem = item;
                this.showInventoryModal();
                this.populateInventoryForm(item);
                window.showSuccess('Item cargado para edición');
            } else {
                throw new Error('Item de inventario no encontrado');
            }

        } catch (error) {
            console.error('Error loading inventory item:', error);
            window.showError('Error al cargar item: ' + error.message);
        }
    }

    /**
     * Muestra el modal de inventario
     */
    showInventoryModal() {
        const modal = document.getElementById('inventory-modal');
        if (modal) {
            modal.style.display = 'flex';
            setTimeout(() => {
                modal.classList.add('show');
            }, 10);
        }
    }

    /**
     * Cierra el modal de inventario
     */
    closeInventoryModal() {
        const modal = document.getElementById('inventory-modal');
        if (modal) {
            modal.classList.remove('show');
            setTimeout(() => {
                modal.style.display = 'none';
            }, 300);
        }
        this.currentItem = null;
    }

    /**
     * Llena el formulario con datos del item de inventario
     */
    populateInventoryForm(item) {
        document.getElementById('inventory-modal-title').textContent = 'Editar Ítem de Inventario';

        document.getElementById('item-name').value = item.name;
        document.getElementById('item-type').value = item.type;
        document.getElementById('item-description').value = item.description || '';
        document.getElementById('item-quantity').value = item.quantity;
        document.getElementById('item-minimum-stock').value = item.minimumStock;
        document.getElementById('item-unit-cost').value = item.unitCost;
        document.getElementById('item-supplier').value = item.supplier || '';
        document.getElementById('item-location').value = item.location || '';
        document.getElementById('item-controlled').checked = item.isControlledSubstance || false;
        document.getElementById('item-active').checked = item.isActive !== false;

        if (item.expirationDate) {
            const expirationDate = new Date(item.expirationDate);
            document.getElementById('item-expiration').value = expirationDate.toISOString().split('T')[0];
        }
    }

    /**
     * Guarda el item de inventario (crear o actualizar)
     */
    async saveInventoryItem() {
        const form = document.getElementById('inventory-form');
        if (!form) return;

        // Validar formulario
        if (!this.validateInventoryForm()) {
            return;
        }

        const itemData = this.getInventoryFormData();

        try {
            window.showInfo(this.currentItem ? 'Actualizando item...' : 'Agregando item...');

            let result;
            if (this.currentItem) {
                result = await window.inventoryApi.updateInventoryItem(this.currentItem.id, itemData);
            } else {
                result = await window.inventoryApi.createInventoryItem(itemData);
            }

            if (result) {
                window.showSuccess(`Item de inventario ${this.currentItem ? 'actualizado' : 'agregado'} exitosamente`);
                this.closeInventoryModal();
                this.loadInventoryList(); // Recargar lista
            }

        } catch (error) {
            console.error('Error saving inventory item:', error);
            window.showError('Error al guardar item: ' + error.message);
        }
    }

    /**
     * Valida el formulario de inventario
     */
    validateInventoryForm() {
        const name = document.getElementById('item-name').value.trim();
        const type = document.getElementById('item-type').value;
        const quantity = document.getElementById('item-quantity').value;
        const minimumStock = document.getElementById('item-minimum-stock').value;
        const unitCost = document.getElementById('item-unit-cost').value;

        if (!name) {
            window.showError('Nombre del ítem es requerido');
            return false;
        }

        if (!type) {
            window.showError('Tipo es requerido');
            return false;
        }

        if (quantity === '' || quantity < 0) {
            window.showError('Cantidad debe ser un número positivo');
            return false;
        }

        if (minimumStock === '' || minimumStock < 0) {
            window.showError('Stock mínimo debe ser un número positivo');
            return false;
        }

        if (unitCost === '' || unitCost < 0) {
            window.showError('Costo unitario debe ser un número positivo');
            return false;
        }

        return true;
    }

    /**
     * Obtiene los datos del formulario de inventario
     */
    getInventoryFormData() {
        const expirationDate = document.getElementById('item-expiration').value;
        const isControlled = document.getElementById('item-controlled').checked;

        return {
            name: document.getElementById('item-name').value.trim(),
            type: document.getElementById('item-type').value,
            description: document.getElementById('item-description').value.trim(),
            quantity: parseInt(document.getElementById('item-quantity').value),
            minimumStock: parseInt(document.getElementById('item-minimum-stock').value),
            unitCost: parseFloat(document.getElementById('item-unit-cost').value),
            supplier: document.getElementById('item-supplier').value.trim(),
            location: document.getElementById('item-location').value.trim(),
            expirationDate: expirationDate || null,
            isControlledSubstance: isControlled,
            isActive: document.getElementById('item-active').checked
        };
    }

    /**
     * Muestra modal para ajustar stock
     */
    async adjustStock(itemId) {
        try {
            const item = this.inventoryItems.find(i => i.id === itemId);
            if (!item) {
                throw new Error('Item no encontrado');
            }

            const modal = document.createElement('div');
            modal.className = 'modal-overlay';
            modal.innerHTML = `
                <div class="modal">
                    <div class="modal-header">
                        <h3>Ajustar Stock - ${item.name}</h3>
                        <button class="modal-close" onclick="this.closest('.modal-overlay').remove()">
                            <i class="fas fa-times"></i>
                        </button>
                    </div>
                    <div class="modal-body">
                        <div class="stock-adjustment">
                            <p><strong>Stock Actual:</strong> ${item.quantity}</p>
                            <p><strong>Stock Mínimo:</strong> ${item.minimumStock}</p>

                            <div class="form-group">
                                <label for="adjustment-type">Tipo de Ajuste</label>
                                <select id="adjustment-type">
                                    <option value="add">Agregar</option>
                                    <option value="subtract">Restar</option>
                                    <option value="set">Establecer</option>
                                </select>
                            </div>

                            <div class="form-group">
                                <label for="adjustment-quantity">Cantidad</label>
                                <input type="number" id="adjustment-quantity" min="0" value="1">
                            </div>

                            <div class="form-group">
                                <label for="adjustment-reason">Motivo</label>
                                <textarea id="adjustment-reason" rows="3" placeholder="Motivo del ajuste..."></textarea>
                            </div>
                        </div>
                    </div>
                    <div class="modal-footer">
                        <button class="btn btn-secondary" onclick="this.closest('.modal-overlay').remove()">
                            Cancelar
                        </button>
                        <button class="btn btn-primary" onclick="inventoryManagementService.confirmStockAdjustment('${itemId}')">
                            Confirmar Ajuste
                        </button>
                    </div>
                </div>
            `;

            document.body.appendChild(modal);

            setTimeout(() => {
                modal.classList.add('show');
            }, 10);

        } catch (error) {
            console.error('Error showing stock adjustment modal:', error);
            window.showError('Error al mostrar ajuste de stock: ' + error.message);
        }
    }

    /**
     * Confirma el ajuste de stock
     */
    async confirmStockAdjustment(itemId) {
        const adjustmentType = document.getElementById('adjustment-type').value;
        const quantity = parseInt(document.getElementById('adjustment-quantity').value);
        const reason = document.getElementById('adjustment-reason').value.trim();

        if (!quantity || quantity <= 0) {
            window.showError('Cantidad debe ser un número positivo');
            return;
        }

        if (!reason) {
            window.showError('Debe especificar el motivo del ajuste');
            return;
        }

        try {
            const item = this.inventoryItems.find(i => i.id === itemId);
            if (!item) {
                throw new Error('Item no encontrado');
            }

            let newQuantity;
            switch (adjustmentType) {
                case 'add':
                    newQuantity = item.quantity + quantity;
                    break;
                case 'subtract':
                    newQuantity = Math.max(0, item.quantity - quantity);
                    break;
                case 'set':
                    newQuantity = quantity;
                    break;
                default:
                    throw new Error('Tipo de ajuste inválido');
            }

            window.showInfo('Ajustando stock...');

            const result = await window.inventoryApi.updateInventoryItem(itemId, {
                ...item,
                quantity: newQuantity,
                lastAdjustmentReason: reason,
                lastAdjustmentDate: new Date().toISOString()
            });

            if (result) {
                window.showSuccess('Stock ajustado exitosamente');
                document.querySelector('.modal-overlay').remove();
                this.loadInventoryList(); // Recargar lista
            }

        } catch (error) {
            console.error('Error adjusting stock:', error);
            window.showError('Error al ajustar stock: ' + error.message);
        }
    }

    /**
     * Elimina un item de inventario
     */
    async deleteInventoryItem(itemId) {
        if (!confirm('¿Está seguro de que desea eliminar este item del inventario? Esta acción no se puede deshacer.')) {
            return;
        }

        try {
            window.showInfo('Eliminando item...');

            await window.inventoryApi.deleteInventoryItem(itemId);

            window.showSuccess('Item eliminado exitosamente');
            this.loadInventoryList(); // Recargar lista

        } catch (error) {
            console.error('Error deleting inventory item:', error);
            window.showError('Error al eliminar item: ' + error.message);
        }
    }

    /**
     * Cambia de página
     */
    goToPage(page) {
        this.currentPage = page;
        this.renderInventoryList();
    }

    /**
     * Obtiene el conteo de items con stock bajo
     */
    getLowStockCount() {
        return this.inventoryItems.filter(item => item.quantity <= item.minimumStock).length;
    }

    /**
     * Obtiene el conteo de items por tipo
     */
    getItemsByTypeCount(type) {
        return this.inventoryItems.filter(item => item.type === type).length;
    }

    /**
     * Obtiene la clase CSS para el estado de stock
     */
    getStockStatusClass(item) {
        if (item.quantity <= item.minimumStock) {
            return 'table-row-warning';
        } else if (item.quantity === 0) {
            return 'table-row-error';
        }
        return '';
    }

    /**
     * Obtiene la clase CSS para la cantidad
     */
    getQuantityClass(item) {
        if (item.quantity <= item.minimumStock) {
            return 'quantity-low';
        } else if (item.quantity === 0) {
            return 'quantity-zero';
        }
        return 'quantity-normal';
    }

    /**
     * Obtiene la clase CSS para el badge de tipo
     */
    getTypeBadgeClass(type) {
        const typeClasses = {
            'MEDICAMENTO': 'success',
            'SUMINISTRO': 'primary',
            'EQUIPO': 'info',
            'MATERIAL': 'warning'
        };

        return typeClasses[type] || 'secondary';
    }

    /**
     * Obtiene el nombre para mostrar del tipo
     */
    getTypeDisplayName(type) {
        const typeNames = {
            'MEDICAMENTO': 'Medicamento',
            'SUMINISTRO': 'Suministro',
            'EQUIPO': 'Equipo',
            'MATERIAL': 'Material'
        };

        return typeNames[type] || type;
    }

    /**
     * Obtiene la clase CSS para el badge de estado
     */
    getStatusBadgeClass(item) {
        if (item.quantity === 0) {
            return 'badge-error';
        } else if (item.quantity <= item.minimumStock) {
            return 'badge-warning';
        }
        return 'badge-success';
    }

    /**
     * Obtiene el nombre para mostrar del estado
     */
    getStatusDisplayName(item) {
        if (item.quantity === 0) {
            return 'Agotado';
        } else if (item.quantity <= item.minimumStock) {
            return 'Stock Bajo';
        }
        return 'Disponible';
    }

    /**
     * Formatea moneda
     */
    formatCurrency(amount) {
        return new Intl.NumberFormat('es-CO', {
            style: 'currency',
            currency: 'COP'
        }).format(amount);
    }

    /**
     * Muestra estado vacío
     */
    showEmptyState() {
        const contentArea = document.getElementById('content-area');
        if (contentArea) {
            contentArea.innerHTML = `
                <div class="empty-state">
                    <i class="fas fa-boxes"></i>
                    <h3>No se pudieron cargar los items de inventario</h3>
                    <p>Verifica tu conexión a internet e intenta nuevamente.</p>
                    <button class="btn btn-primary" onclick="inventoryManagementService.loadInventoryList()">
                        <i class="fas fa-refresh"></i> Reintentar
                    </button>
                </div>
            `;
        }
    }
}

// Crear instancia global del servicio de gestión de inventario
window.inventoryManagementService = new InventoryManagementService();