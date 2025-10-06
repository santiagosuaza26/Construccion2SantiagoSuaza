/**
 * Componentes reutilizables de UI
 * Proporciona componentes comunes para toda la aplicación
 */

// Crear espacio de nombres para componentes
window.UI = window.UI || {};

/**
 * Componente Modal
 */
UI.Modal = class {
    constructor(options = {}) {
        this.title = options.title || 'Modal';
        this.content = options.content || '';
        this.size = options.size || 'medium'; // small, medium, large
        this.closable = options.closable !== false;
        this.id = options.id || `modal-${Date.now()}`;
    }

    /**
     * Muestra el modal
     */
    show() {
        const modal = document.createElement('div');
        modal.className = 'modal-overlay';
        modal.id = this.id;
        modal.innerHTML = `
            <div class="modal ${this.size}">
                ${this.closable ? `
                    <div class="modal-header">
                        <h3>${this.title}</h3>
                        <button class="modal-close" onclick="UI.Modal.hide('${this.id}')">
                            <i class="fas fa-times"></i>
                        </button>
                    </div>
                ` : ''}
                <div class="modal-body">
                    ${this.content}
                </div>
            </div>
        `;

        document.body.appendChild(modal);

        setTimeout(() => {
            modal.classList.add('show');
        }, 10);

        return modal;
    }

    /**
     * Oculta el modal
     */
    static hide(modalId) {
        const modal = document.getElementById(modalId);
        if (modal) {
            modal.classList.remove('show');
            setTimeout(() => {
                modal.remove();
            }, 300);
        }
    }

    /**
     * Actualiza el contenido del modal
     */
    updateContent(newContent) {
        const modal = document.getElementById(this.id);
        if (modal) {
            const body = modal.querySelector('.modal-body');
            if (body) {
                body.innerHTML = newContent;
            }
        }
    }
};

/**
 * Componente Tabla
 */
UI.Table = class {
    constructor(options = {}) {
        this.columns = options.columns || [];
        this.data = options.data || [];
        this.actions = options.actions || [];
        this.searchable = options.searchable !== false;
        this.pagination = options.pagination !== false;
        this.itemsPerPage = options.itemsPerPage || 10;
        this.currentPage = 1;
    }

    /**
     * Renderiza la tabla
     */
    render() {
        const filteredData = this.searchable ? this.filterData() : this.data;
        const paginatedData = this.pagination ? this.paginateData(filteredData) : filteredData;

        return `
            <div class="table-container">
                ${this.searchable ? this.renderSearch() : ''}
                <table class="table">
                    <thead>
                        <tr>
                            ${this.columns.map(col => `<th>${col.header}</th>`).join('')}
                            ${this.actions.length > 0 ? '<th>Acciones</th>' : ''}
                        </tr>
                    </thead>
                    <tbody>
                        ${this.renderRows(paginatedData)}
                    </tbody>
                </table>
                ${this.pagination ? this.renderPagination(filteredData.length) : ''}
            </div>
        `;
    }

    /**
     * Renderiza las filas de la tabla
     */
    renderRows(data) {
        if (data.length === 0) {
            return `
                <tr>
                    <td colspan="${this.columns.length + (this.actions.length > 0 ? 1 : 0)}" class="text-center" style="padding: 2rem;">
                        <i class="fas fa-table" style="font-size: 3rem; color: var(--text-secondary); margin-bottom: 1rem;"></i>
                        <p style="color: var(--text-secondary);">No se encontraron datos</p>
                    </td>
                </tr>
            `;
        }

        return data.map(row => `
            <tr>
                ${this.columns.map(col => `<td>${this.formatCell(row[col.key], col)}</td>`).join('')}
                ${this.actions.length > 0 ? `
                    <td>
                        <div class="btn-group">
                            ${this.actions.map(action => `
                                <button class="btn btn-sm ${action.class || 'btn-primary'}"
                                        onclick="${action.handler}('${row.id || row.cedula}')"
                                        title="${action.title}">
                                    <i class="fas ${action.icon}"></i>
                                </button>
                            `).join('')}
                        </div>
                    </td>
                ` : ''}
            </tr>
        `).join('');
    }

    /**
     * Formatea el contenido de una celda
     */
    formatCell(value, column) {
        if (column.formatter) {
            return column.formatter(value);
        }

        if (column.type === 'badge') {
            return `<span class="badge ${column.badgeClass ? column.badgeClass(value) : 'badge-primary'}">${value}</span>`;
        }

        if (column.type === 'date') {
            return value ? window.formatDate(value) : '';
        }

        if (column.type === 'currency') {
            return value ? this.formatCurrency(value) : '';
        }

        return value || '';
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
     * Renderiza la búsqueda
     */
    renderSearch() {
        return `
            <div class="search-box" style="margin-bottom: 1rem;">
                <i class="fas fa-search search-icon"></i>
                <input type="text" id="table-search-${this.id}" class="search-input"
                       placeholder="Buscar en la tabla...">
            </div>
        `;
    }

    /**
     * Renderiza la paginación
     */
    renderPagination(totalItems) {
        const totalPages = Math.ceil(totalItems / this.itemsPerPage);

        if (totalPages <= 1) return '';

        let paginationHtml = `
            <div class="pagination">
        `;

        // Botón anterior
        if (this.currentPage > 1) {
            paginationHtml += `<button class="pagination-btn" onclick="this.closest('.table-container').querySelector('.table-component').goToPage(${this.currentPage - 1})">
                <i class="fas fa-chevron-left"></i>
            </button>`;
        }

        // Páginas
        for (let i = 1; i <= totalPages; i++) {
            if (i === this.currentPage) {
                paginationHtml += `<button class="pagination-btn active">${i}</button>`;
            } else if (i >= this.currentPage - 2 && i <= this.currentPage + 2) {
                paginationHtml += `<button class="pagination-btn" onclick="this.closest('.table-container').querySelector('.table-component').goToPage(${i})">${i}</button>`;
            }
        }

        // Botón siguiente
        if (this.currentPage < totalPages) {
            paginationHtml += `<button class="pagination-btn" onclick="this.closest('.table-container').querySelector('.table-component').goToPage(${this.currentPage + 1})">
                <i class="fas fa-chevron-right"></i>
            </button>`;
        }

        paginationHtml += '</div>';
        return paginationHtml;
    }

    /**
     * Filtra los datos
     */
    filterData() {
        const searchInput = document.getElementById(`table-search-${this.id}`);
        if (!searchInput || !this.data) return this.data;

        const term = searchInput.value.toLowerCase();
        if (!term) return this.data;

        return this.data.filter(row =>
            this.columns.some(col =>
                String(row[col.key]).toLowerCase().includes(term)
            )
        );
    }

    /**
     * Pagina los datos
     */
    paginateData(data) {
        const startIndex = (this.currentPage - 1) * this.itemsPerPage;
        const endIndex = startIndex + this.itemsPerPage;
        return data.slice(startIndex, endIndex);
    }

    /**
     * Cambia de página
     */
    goToPage(page) {
        this.currentPage = page;
        this.refresh();
    }

    /**
     * Actualiza la tabla
     */
    refresh() {
        const container = document.querySelector(`[data-table-id="${this.id}"]`);
        if (container) {
            container.innerHTML = this.render();
            this.setupEventListeners();
        }
    }

    /**
     * Configura los event listeners
     */
    setupEventListeners() {
        const searchInput = document.getElementById(`table-search-${this.id}`);
        if (searchInput) {
            searchInput.addEventListener('input', window.debounce(() => {
                this.currentPage = 1;
                this.refresh();
            }, 300));
        }
    }
};

/**
 * Componente Formulario
 */
UI.Form = class {
    constructor(options = {}) {
        this.fields = options.fields || [];
        this.data = options.data || {};
        this.layout = options.layout || 'vertical'; // vertical, horizontal, grid
        this.id = options.id || `form-${Date.now()}`;
    }

    /**
     * Renderiza el formulario
     */
    render() {
        return `
            <form id="${this.id}" class="form ${this.layout}">
                ${this.renderFields()}
            </form>
        `;
    }

    /**
     * Renderiza los campos del formulario
     */
    renderFields() {
        return this.fields.map(field => this.renderField(field)).join('');
    }

    /**
     * Renderiza un campo individual
     */
    renderField(field) {
        const value = this.data[field.name] || field.value || '';
        const required = field.required ? 'required' : '';
        const error = field.error || '';

        return `
            <div class="form-group ${field.class || ''}">
                ${field.label ? `<label for="${field.name}">${field.label} ${required ? '*' : ''}</label>` : ''}

                ${this.renderFieldInput(field, value)}

                ${error ? `<div class="form-error">${error}</div>` : ''}
                ${field.help ? `<div class="form-help">${field.help}</div>` : ''}
            </div>
        `;
    }

    /**
     * Renderiza el input según el tipo
     */
    renderFieldInput(field, value) {
        switch (field.type) {
            case 'select':
                return `
                    <select id="${field.name}" name="${field.name}" ${field.required ? 'required' : ''}>
                        ${field.options.map(option => `
                            <option value="${option.value}" ${value === option.value ? 'selected' : ''}>
                                ${option.label}
                            </option>
                        `).join('')}
                    </select>
                `;

            case 'textarea':
                return `
                    <textarea id="${field.name}" name="${field.name}"
                              rows="${field.rows || 3}"
                              maxlength="${field.maxlength || ''}"
                              placeholder="${field.placeholder || ''}">${value}</textarea>
                `;

            case 'checkbox':
                return `
                    <label class="checkbox-label">
                        <input type="checkbox" id="${field.name}" name="${field.name}" ${value ? 'checked' : ''}>
                        ${field.label || ''}
                    </label>
                `;

            case 'radio':
                return field.options.map(option => `
                    <label class="radio-label">
                        <input type="radio" name="${field.name}" value="${option.value}" ${value === option.value ? 'checked' : ''}>
                        ${option.label}
                    </label>
                `).join('<br>');

            default:
                return `
                    <input type="${field.type || 'text'}"
                           id="${field.name}"
                           name="${field.name}"
                           value="${value}"
                           ${field.required ? 'required' : ''}
                           maxlength="${field.maxlength || ''}"
                           placeholder="${field.placeholder || ''}"
                           min="${field.min || ''}"
                           max="${field.max || ''}"
                           step="${field.step || ''}">
                `;
        }
    }

    /**
     * Obtiene los datos del formulario
     */
    getData() {
        const form = document.getElementById(this.id);
        if (!form) return {};

        const formData = new FormData(form);
        const data = {};

        for (let [key, value] of formData.entries()) {
            data[key] = value;
        }

        return data;
    }

    /**
     * Establece los datos del formulario
     */
    setData(data) {
        this.data = { ...this.data, ...data };
        this.refresh();
    }

    /**
     * Valida el formulario
     */
    validate() {
        const form = document.getElementById(this.id);
        if (!form) return false;

        let isValid = true;
        const errors = {};

        this.fields.forEach(field => {
            if (field.required) {
                const element = form.querySelector(`[name="${field.name}"]`);
                if (element && !element.value.trim()) {
                    errors[field.name] = `${field.label || field.name} es requerido`;
                    isValid = false;
                }
            }

            // Validaciones personalizadas
            if (field.validation) {
                const element = form.querySelector(`[name="${field.name}"]`);
                if (element) {
                    const validation = field.validation(element.value);
                    if (validation !== true) {
                        errors[field.name] = validation;
                        isValid = false;
                    }
                }
            }
        });

        this.showErrors(errors);
        return isValid;
    }

    /**
     * Muestra errores de validación
     */
    showErrors(errors) {
        this.fields.forEach(field => {
            const errorElement = document.querySelector(`#${this.id} [name="${field.name}"]`).closest('.form-group').querySelector('.form-error');
            if (errorElement) {
                errorElement.textContent = errors[field.name] || '';
            }
        });
    }

    /**
     * Actualiza el formulario
     */
    refresh() {
        const container = document.querySelector(`[data-form-id="${this.id}"]`);
        if (container) {
            container.innerHTML = this.render();
        }
    }
};

/**
 * Componente Estadísticas
 */
UI.Stats = class {
    constructor(options = {}) {
        this.stats = options.stats || [];
        this.columns = options.columns || 4;
    }

    /**
     * Renderiza las estadísticas
     */
    render() {
        return `
            <div class="stats-grid" style="grid-template-columns: repeat(${this.columns}, 1fr);">
                ${this.stats.map(stat => `
                    <div class="stat-card ${stat.class || ''}">
                        <div class="stat-value">${stat.value}</div>
                        <div class="stat-label">${stat.label}</div>
                        ${stat.action ? `
                            <div class="stat-action">
                                <button class="btn btn-sm ${stat.action.class || 'btn-primary'}"
                                        onclick="${stat.action.handler}">
                                    ${stat.action.text}
                                </button>
                            </div>
                        ` : ''}
                    </div>
                `).join('')}
            </div>
        `;
    }
};

/**
 * Componente Notificación mejorada
 */
UI.Notification = class {
    constructor() {
        this.container = null;
        this.notifications = [];
        this.createContainer();
    }

    /**
     * Crea el contenedor de notificaciones
     */
    createContainer() {
        this.container = document.createElement('div');
        this.container.className = 'notification-container-improved';
        document.body.appendChild(this.container);
    }

    /**
     * Muestra una notificación
     */
    show(message, type = 'info', options = {}) {
        const notification = {
            id: Date.now(),
            message,
            type,
            duration: options.duration || 5000,
            persistent: options.persistent || false,
            actions: options.actions || []
        };

        this.notifications.push(notification);
        this.renderNotification(notification);

        if (!notification.persistent) {
            setTimeout(() => {
                this.hide(notification.id);
            }, notification.duration);
        }

        return notification.id;
    }

    /**
     * Renderiza una notificación
     */
    renderNotification(notification) {
        const notificationElement = document.createElement('div');
        notificationElement.className = `notification notification-${notification.type}`;
        notificationElement.id = `notification-${notification.id}`;
        notificationElement.innerHTML = `
            <div class="notification-content">
                <i class="fas ${this.getIconClass(notification.type)}"></i>
                <span class="notification-message">${notification.message}</span>
            </div>
            ${notification.actions.length > 0 ? `
                <div class="notification-actions">
                    ${notification.actions.map(action => `
                        <button class="btn btn-sm ${action.class || 'btn-primary'}"
                                onclick="${action.handler}">
                            ${action.text}
                        </button>
                    `).join('')}
                </div>
            ` : ''}
            <button class="notification-close" onclick="window.uiNotification.hide(${notification.id})">
                <i class="fas fa-times"></i>
            </button>
        `;

        this.container.appendChild(notificationElement);

        setTimeout(() => {
            notificationElement.classList.add('show');
        }, 10);
    }

    /**
     * Oculta una notificación
     */
    hide(notificationId) {
        const notification = document.getElementById(`notification-${notificationId}`);
        if (notification) {
            notification.classList.remove('show');
            setTimeout(() => {
                notification.remove();
                this.notifications = this.notifications.filter(n => n.id !== notificationId);
            }, 300);
        }
    }

    /**
     * Obtiene la clase del ícono según el tipo
     */
    getIconClass(type) {
        const icons = {
            'success': 'fa-check-circle',
            'error': 'fa-exclamation-circle',
            'warning': 'fa-exclamation-triangle',
            'info': 'fa-info-circle'
        };

        return icons[type] || icons.info;
    }

    /**
     * Muestra notificación de éxito
     */
    success(message, options = {}) {
        return this.show(message, 'success', { duration: 5000, ...options });
    }

    /**
     * Muestra notificación de error
     */
    error(message, options = {}) {
        return this.show(message, 'error', { duration: 7000, ...options });
    }

    /**
     * Muestra notificación de advertencia
     */
    warning(message, options = {}) {
        return this.show(message, 'warning', { duration: 6000, ...options });
    }

    /**
     * Muestra notificación de información
     */
    info(message, options = {}) {
        return this.show(message, 'info', { duration: 5000, ...options });
    }
};

/**
 * Inicializar componentes globales
 */
document.addEventListener('DOMContentLoaded', function() {
    // Crear instancia global de notificaciones mejorada
    window.uiNotification = new UI.Notification();

    // Funciones de conveniencia globales
    window.showSuccess = function(message, options = {}) {
        return window.uiNotification.success(message, options);
    };

    window.showError = function(message, options = {}) {
        return window.uiNotification.error(message, options);
    };

    window.showWarning = function(message, options = {}) {
        return window.uiNotification.warning(message, options);
    };

    window.showInfo = function(message, options = {}) {
        return window.uiNotification.info(message, options);
    };
});

console.log('🧩 Componentes UI cargados exitosamente');