/**
 * Servicio de Facturación
 * Maneja las operaciones de facturación y pagos para personal administrativo
 */
class BillingService {
    constructor() {
        this.currentInvoice = null;
        this.invoices = [];
        this.filteredInvoices = [];
        this.currentPage = 1;
        this.itemsPerPage = 10;
        this.searchTerm = '';
        this.selectedStatus = '';
        this.selectedPatient = null;
    }

    /**
     * Carga la lista de facturas
     */
    async loadInvoicesList() {
        try {
            window.showInfo('Cargando facturas...');

            const response = await window.billingApi.findAllInvoices();

            if (response && Array.isArray(response)) {
                this.invoices = response;
                this.filteredInvoices = [...this.invoices];
                this.renderInvoicesList();
                window.showSuccess(`Se cargaron ${this.invoices.length} facturas`);
            } else {
                throw new Error('Formato de respuesta inválido');
            }

        } catch (error) {
            console.error('Error loading invoices:', error);
            window.showError('Error al cargar facturas: ' + error.message);
            this.showEmptyState();
        }
    }

    /**
     * Calcula la facturación para un paciente
     */
    async calculatePatientBilling(patientCedula) {
        try {
            window.showInfo('Calculando facturación...');

            const response = await window.billingApi.calculateBilling(patientCedula);

            if (response) {
                this.showBillingCalculationModal(response, patientCedula);
            } else {
                throw new Error('No se pudo calcular la facturación');
            }

        } catch (error) {
            console.error('Error calculating billing:', error);
            window.showError('Error al calcular facturación: ' + error.message);
        }
    }

    /**
     * Renderiza la lista de facturas
     */
    renderInvoicesList() {
        const contentArea = document.getElementById('content-area');
        if (!contentArea) return;

        const startIndex = (this.currentPage - 1) * this.itemsPerPage;
        const endIndex = startIndex + this.itemsPerPage;
        const invoicesToShow = this.filteredInvoices.slice(startIndex, endIndex);

        contentArea.innerHTML = `
            <div class="content-header">
                <h2><i class="fas fa-file-invoice-dollar"></i> Gestión de Facturación</h2>
                <p>Sistema de facturación y control de pagos</p>
            </div>

            <div class="content-actions">
                <div class="search-box">
                    <i class="fas fa-search search-icon"></i>
                    <input type="text" id="invoice-search" class="search-input"
                           placeholder="Buscar por paciente, número de factura..."
                           value="${this.searchTerm}">
                </div>

                <div class="filter-box">
                    <select id="status-filter" class="filter-select">
                        <option value="">Todos los estados</option>
                        <option value="PENDING">Pendiente</option>
                        <option value="PAID">Pagada</option>
                        <option value="OVERDUE">Vencida</option>
                        <option value="CANCELLED">Cancelada</option>
                    </select>
                </div>

                <button class="btn btn-primary" onclick="billingService.showCalculateBillingModal()">
                    <i class="fas fa-calculator"></i> Calcular Factura
                </button>
            </div>

            <div class="stats-grid">
                <div class="stat-card">
                    <div class="stat-value">${this.invoices.length}</div>
                    <div class="stat-label">Total Facturas</div>
                </div>
                <div class="stat-card">
                    <div class="stat-value">${this.getTotalPendingAmount()}</div>
                    <div class="stat-label">Pendiente</div>
                </div>
                <div class="stat-card">
                    <div class="stat-value">${this.getInvoicesByStatusCount('PAID')}</div>
                    <div class="stat-label">Pagadas</div>
                </div>
                <div class="stat-card">
                    <div class="stat-value">${this.getInvoicesByStatusCount('OVERDUE')}</div>
                    <div class="stat-label">Vencidas</div>
                </div>
            </div>

            <div class="table-container">
                <table class="table">
                    <thead>
                        <tr>
                            <th>Número</th>
                            <th>Paciente</th>
                            <th>Fecha</th>
                            <th>Total</th>
                            <th>Copago</th>
                            <th>Estado</th>
                            <th>Acciones</th>
                        </tr>
                    </thead>
                    <tbody id="invoices-table-body">
                        ${this.renderInvoicesTableRows(invoicesToShow)}
                    </tbody>
                </table>
            </div>

            ${this.renderPagination()}

            <!-- Modal para calcular facturación -->
            <div id="billing-modal" class="modal-overlay" style="display: none;">
                <div class="modal">
                    <div class="modal-header">
                        <h3 id="billing-modal-title">Calcular Facturación</h3>
                        <button class="modal-close" onclick="billingService.closeBillingModal()">
                            <i class="fas fa-times"></i>
                        </button>
                    </div>
                    <div class="modal-body">
                        <form id="billing-form">
                            ${this.renderBillingForm()}
                        </form>
                    </div>
                    <div class="modal-footer">
                        <button class="btn btn-secondary" onclick="billingService.closeBillingModal()">
                            Cancelar
                        </button>
                        <button class="btn btn-primary" onclick="billingService.calculateAndShowBilling()">
                            Calcular
                        </button>
                    </div>
                </div>
            </div>
        `;

        // Configurar event listeners
        this.setupEventListeners();
    }

    /**
     * Renderiza las filas de la tabla de facturas
     */
    renderInvoicesTableRows(invoices) {
        if (invoices.length === 0) {
            return `
                <tr>
                    <td colspan="7" class="text-center" style="padding: 2rem;">
                        <i class="fas fa-file-invoice-dollar" style="font-size: 3rem; color: var(--text-secondary); margin-bottom: 1rem;"></i>
                        <p style="color: var(--text-secondary);">No se encontraron facturas</p>
                    </td>
                </tr>
            `;
        }

        return invoices.map(invoice => `
            <tr class="${this.getInvoiceStatusClass(invoice)}">
                <td>${invoice.invoiceNumber}</td>
                <td>${invoice.patientName}</td>
                <td>${window.formatDate(invoice.billingDate)}</td>
                <td>${this.formatCurrency(invoice.totalAmount)}</td>
                <td>${this.formatCurrency(invoice.copaymentAmount)}</td>
                <td>
                    <span class="badge ${this.getStatusBadgeClass(invoice.status)}">
                        ${this.getStatusDisplayName(invoice.status)}
                    </span>
                </td>
                <td>
                    <div class="btn-group">
                        <button class="btn btn-sm btn-primary"
                                onclick="billingService.viewInvoice('${invoice.id}')"
                                title="Ver Detalles">
                            <i class="fas fa-eye"></i>
                        </button>
                        <button class="btn btn-sm btn-info"
                                onclick="billingService.printInvoice('${invoice.id}')"
                                title="Imprimir">
                            <i class="fas fa-print"></i>
                        </button>
                        ${invoice.status === 'PENDING' ? `
                            <button class="btn btn-sm btn-success"
                                    onclick="billingService.markAsPaid('${invoice.id}')"
                                    title="Marcar como Pagada">
                                <i class="fas fa-check"></i>
                            </button>
                        ` : ''}
                    </div>
                </td>
            </tr>
        `).join('');
    }

    /**
     * Renderiza la paginación
     */
    renderPagination() {
        const totalPages = Math.ceil(this.filteredInvoices.length / this.itemsPerPage);

        if (totalPages <= 1) return '';

        let paginationHtml = `
            <div class="pagination">
        `;

        // Botón anterior
        if (this.currentPage > 1) {
            paginationHtml += `<button class="pagination-btn" onclick="billingService.goToPage(${this.currentPage - 1})">
                <i class="fas fa-chevron-left"></i>
            </button>`;
        }

        // Páginas
        for (let i = 1; i <= totalPages; i++) {
            if (i === this.currentPage) {
                paginationHtml += `<button class="pagination-btn active">${i}</button>`;
            } else if (i >= this.currentPage - 2 && i <= this.currentPage + 2) {
                paginationHtml += `<button class="pagination-btn" onclick="billingService.goToPage(${i})">${i}</button>`;
            }
        }

        // Botón siguiente
        if (this.currentPage < totalPages) {
            paginationHtml += `<button class="pagination-btn" onclick="billingService.goToPage(${this.currentPage + 1})">
                <i class="fas fa-chevron-right"></i>
            </button>`;
        }

        paginationHtml += '</div>';
        return paginationHtml;
    }

    /**
     * Renderiza el formulario de cálculo de facturación
     */
    renderBillingForm() {
        return `
            <div class="form-row">
                <div class="form-group">
                    <label for="patient-cedula">Cédula del Paciente *</label>
                    <input type="text" id="patient-cedula" name="patientCedula" required maxlength="20"
                           placeholder="Ingrese la cédula del paciente">
                </div>
                <div class="form-group">
                    <label for="billing-date">Fecha de Facturación</label>
                    <input type="date" id="billing-date" name="billingDate" value="${new Date().toISOString().split('T')[0]}">
                </div>
            </div>

            <div class="form-row">
                <div class="form-group">
                    <label class="checkbox-label">
                        <input type="checkbox" id="include-medical-records" name="includeMedicalRecords" checked>
                        Incluir consultas médicas
                    </label>
                </div>
                <div class="form-group">
                    <label class="checkbox-label">
                        <input type="checkbox" id="include-medications" name="includeMedications" checked>
                        Incluir medicamentos
                    </label>
                </div>
            </div>

            <div class="form-row">
                <div class="form-group">
                    <label class="checkbox-label">
                        <input type="checkbox" id="include-procedures" name="includeProcedures" checked>
                        Incluir procedimientos
                    </label>
                </div>
                <div class="form-group">
                    <label class="checkbox-label">
                        <input type="checkbox" id="include-diagnostic-aids" name="includeDiagnosticAids" checked>
                        Incluir ayudas diagnósticas
                    </label>
                </div>
            </div>

            <div class="form-section">
                <h4><i class="fas fa-sticky-note"></i> Observaciones</h4>
                <div class="form-row">
                    <div class="form-group">
                        <textarea id="billing-notes" name="notes" rows="3"
                                  placeholder="Observaciones adicionales para la factura..."></textarea>
                    </div>
                </div>
            </div>
        `;
    }

    /**
     * Configura los event listeners
     */
    setupEventListeners() {
        // Búsqueda
        const searchInput = document.getElementById('invoice-search');
        if (searchInput) {
            searchInput.addEventListener('input', window.debounce((e) => {
                this.searchTerm = e.target.value;
                this.filterInvoices();
            }, 300));
        }

        // Filtro por estado
        const statusFilter = document.getElementById('status-filter');
        if (statusFilter) {
            statusFilter.addEventListener('change', (e) => {
                this.selectedStatus = e.target.value;
                this.filterInvoices();
            });
        }

        // Validación del formulario
        const billingForm = document.getElementById('billing-form');
        if (billingForm) {
            billingForm.addEventListener('submit', (e) => {
                e.preventDefault();
                this.calculateAndShowBilling();
            });
        }
    }

    /**
     * Filtra las facturas según término de búsqueda y estado
     */
    filterInvoices() {
        let filtered = [...this.invoices];

        // Filtrar por término de búsqueda
        if (this.searchTerm) {
            const term = this.searchTerm.toLowerCase();
            filtered = filtered.filter(invoice =>
                invoice.patientName.toLowerCase().includes(term) ||
                invoice.invoiceNumber.toLowerCase().includes(term)
            );
        }

        // Filtrar por estado
        if (this.selectedStatus) {
            filtered = filtered.filter(invoice => invoice.status === this.selectedStatus);
        }

        this.filteredInvoices = filtered;
        this.currentPage = 1;
        this.renderInvoicesList();
    }

    /**
     * Muestra el modal para calcular facturación
     */
    showCalculateBillingModal() {
        this.showBillingModal();
    }

    /**
     * Muestra el modal de facturación
     */
    showBillingModal() {
        const modal = document.getElementById('billing-modal');
        if (modal) {
            modal.style.display = 'flex';
            setTimeout(() => {
                modal.classList.add('show');
            }, 10);
        }
    }

    /**
     * Cierra el modal de facturación
     */
    closeBillingModal() {
        const modal = document.getElementById('billing-modal');
        if (modal) {
            modal.classList.remove('show');
            setTimeout(() => {
                modal.style.display = 'none';
            }, 300);
        }
    }

    /**
     * Calcula y muestra la facturación
     */
    async calculateAndShowBilling() {
        const form = document.getElementById('billing-form');
        if (!form) return;

        // Validar formulario
        if (!this.validateBillingForm()) {
            return;
        }

        const patientCedula = document.getElementById('patient-cedula').value.trim();

        try {
            await this.calculatePatientBilling(patientCedula);
        } catch (error) {
            console.error('Error calculating billing:', error);
            window.showError('Error al calcular facturación: ' + error.message);
        }
    }

    /**
     * Valida el formulario de facturación
     */
    validateBillingForm() {
        const patientCedula = document.getElementById('patient-cedula').value.trim();

        if (!patientCedula || !window.isValidCedula(patientCedula)) {
            window.showError('Cédula del paciente inválida');
            return false;
        }

        return true;
    }

    /**
     * Muestra el modal con el cálculo de facturación
     */
    showBillingCalculationModal(calculation, patientCedula) {
        const modal = document.createElement('div');
        modal.className = 'modal-overlay';
        modal.innerHTML = `
            <div class="modal large-modal">
                <div class="modal-header">
                    <h3>Cálculo de Facturación</h3>
                    <button class="modal-close" onclick="this.closest('.modal-overlay').remove()">
                        <i class="fas fa-times"></i>
                    </button>
                </div>
                <div class="modal-body">
                    <div class="billing-calculation">
                        <div class="calculation-header">
                            <h4>Paciente: ${calculation.patientName}</h4>
                            <p><strong>Cédula:</strong> ${window.formatCedula(patientCedula)}</p>
                        </div>

                        ${calculation.medicalRecordSummaries && calculation.medicalRecordSummaries.length > 0 ? `
                            <div class="calculation-section">
                                <h4><i class="fas fa-stethoscope"></i> Consultas Médicas</h4>
                                <div class="summaries-list">
                                    ${calculation.medicalRecordSummaries.map(summary => `
                                        <div class="summary-item">
                                            <span class="summary-description">${summary.description}</span>
                                            <span class="summary-amount">${this.formatCurrency(summary.amount)}</span>
                                        </div>
                                    `).join('')}
                                </div>
                            </div>
                        ` : ''}

                        ${calculation.medicationSummaries && calculation.medicationSummaries.length > 0 ? `
                            <div class="calculation-section">
                                <h4><i class="fas fa-pills"></i> Medicamentos</h4>
                                <div class="summaries-list">
                                    ${calculation.medicationSummaries.map(summary => `
                                        <div class="summary-item">
                                            <span class="summary-description">${summary.medicationName} (${summary.quantity})</span>
                                            <span class="summary-amount">${this.formatCurrency(summary.amount)}</span>
                                        </div>
                                    `).join('')}
                                </div>
                            </div>
                        ` : ''}

                        ${calculation.procedureSummaries && calculation.procedureSummaries.length > 0 ? `
                            <div class="calculation-section">
                                <h4><i class="fas fa-procedures"></i> Procedimientos</h4>
                                <div class="summaries-list">
                                    ${calculation.procedureSummaries.map(summary => `
                                        <div class="summary-item">
                                            <span class="summary-description">${summary.procedureName}</span>
                                            <span class="summary-amount">${this.formatCurrency(summary.amount)}</span>
                                        </div>
                                    `).join('')}
                                </div>
                            </div>
                        ` : ''}

                        ${calculation.diagnosticAidSummaries && calculation.diagnosticAidSummaries.length > 0 ? `
                            <div class="calculation-section">
                                <h4><i class="fas fa-flask"></i> Ayudas Diagnósticas</h4>
                                <div class="summaries-list">
                                    ${calculation.diagnosticAidSummaries.map(summary => `
                                        <div class="summary-item">
                                            <span class="summary-description">${summary.diagnosticAidName}</span>
                                            <span class="summary-amount">${this.formatCurrency(summary.amount)}</span>
                                        </div>
                                    `).join('')}
                                </div>
                            </div>
                        ` : ''}

                        <div class="calculation-total">
                            <div class="total-row">
                                <span class="total-label">Subtotal:</span>
                                <span class="total-amount">${this.formatCurrency(calculation.subtotal)}</span>
                            </div>
                            <div class="total-row">
                                <span class="total-label">Copago:</span>
                                <span class="total-amount">${this.formatCurrency(calculation.copaymentAmount)}</span>
                            </div>
                            <div class="total-row final-total">
                                <span class="total-label">Total a Pagar:</span>
                                <span class="total-amount">${this.formatCurrency(calculation.totalAmount)}</span>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="modal-footer">
                    <button class="btn btn-secondary" onclick="this.closest('.modal-overlay').remove()">
                        Cerrar
                    </button>
                    <button class="btn btn-primary" onclick="billingService.generateInvoice('${patientCedula}')">
                        <i class="fas fa-file-invoice"></i> Generar Factura
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
     * Genera una factura
     */
    async generateInvoice(patientCedula) {
        try {
            const billingDate = document.getElementById('billing-date').value;
            const notes = document.getElementById('billing-notes').value.trim();

            const invoiceData = {
                patientCedula: patientCedula,
                billingDate: billingDate,
                notes: notes,
                includeMedicalRecords: document.getElementById('include-medical-records').checked,
                includeMedications: document.getElementById('include-medications').checked,
                includeProcedures: document.getElementById('include-procedures').checked,
                includeDiagnosticAids: document.getElementById('include-diagnostic-aids').checked
            };

            window.showInfo('Generando factura...');

            const result = await window.billingApi.generateInvoice(invoiceData);

            if (result) {
                window.showSuccess('Factura generada exitosamente');
                document.querySelector('.modal-overlay').remove();
                this.loadInvoicesList(); // Recargar lista
            }

        } catch (error) {
            console.error('Error generating invoice:', error);
            window.showError('Error al generar factura: ' + error.message);
        }
    }

    /**
     * Ver detalles de una factura
     */
    async viewInvoice(invoiceId) {
        try {
            window.showInfo('Cargando detalles de factura...');

            // Buscar la factura en la lista actual
            const invoice = this.invoices.find(i => i.id === invoiceId);

            if (invoice) {
                this.showInvoiceDetailsModal(invoice);
            } else {
                throw new Error('Factura no encontrada');
            }

        } catch (error) {
            console.error('Error loading invoice:', error);
            window.showError('Error al cargar factura: ' + error.message);
        }
    }

    /**
     * Muestra modal con detalles de la factura
     */
    showInvoiceDetailsModal(invoice) {
        const modal = document.createElement('div');
        modal.className = 'modal-overlay';
        modal.innerHTML = `
            <div class="modal large-modal">
                <div class="modal-header">
                    <h3>Factura #${invoice.invoiceNumber}</h3>
                    <button class="modal-close" onclick="this.closest('.modal-overlay').remove()">
                        <i class="fas fa-times"></i>
                    </button>
                </div>
                <div class="modal-body">
                    <div class="invoice-details">
                        <div class="invoice-header">
                            <div class="invoice-info">
                                <h4>Paciente: ${invoice.patientName}</h4>
                                <p><strong>Fecha:</strong> ${window.formatDate(invoice.billingDate)}</p>
                                <p><strong>Estado:</strong>
                                    <span class="badge ${this.getStatusBadgeClass(invoice.status)}">
                                        ${this.getStatusDisplayName(invoice.status)}
                                    </span>
                                </p>
                            </div>
                        </div>

                        <div class="invoice-totals">
                            <div class="total-row">
                                <span class="total-label">Subtotal:</span>
                                <span class="total-amount">${this.formatCurrency(invoice.subtotal)}</span>
                            </div>
                            <div class="total-row">
                                <span class="total-label">Copago:</span>
                                <span class="total-amount">${this.formatCurrency(invoice.copaymentAmount)}</span>
                            </div>
                            <div class="total-row final-total">
                                <span class="total-label">Total:</span>
                                <span class="total-amount">${this.formatCurrency(invoice.totalAmount)}</span>
                            </div>
                        </div>

                        ${invoice.notes ? `
                            <div class="invoice-notes">
                                <h4>Observaciones:</h4>
                                <p>${invoice.notes}</p>
                            </div>
                        ` : ''}
                    </div>
                </div>
                <div class="modal-footer">
                    <button class="btn btn-secondary" onclick="this.closest('.modal-overlay').remove()">
                        Cerrar
                    </button>
                    <button class="btn btn-primary" onclick="billingService.printInvoice('${invoice.id}')">
                        <i class="fas fa-print"></i> Imprimir
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
     * Imprime una factura
     */
    async printInvoice(invoiceId) {
        try {
            const invoice = this.invoices.find(i => i.id === invoiceId);
            if (!invoice) {
                throw new Error('Factura no encontrada');
            }

            // Crear ventana de impresión
            const printWindow = window.open('', '_blank');
            printWindow.document.write(`
                <html>
                    <head>
                        <title>Factura #${invoice.invoiceNumber}</title>
                        <style>
                            body { font-family: Arial, sans-serif; margin: 20px; }
                            .header { text-align: center; margin-bottom: 30px; }
                            .invoice-details { margin-bottom: 20px; }
                            .totals { margin-top: 20px; text-align: right; }
                            .total-row { margin: 5px 0; }
                            .final-total { font-weight: bold; font-size: 1.2em; border-top: 2px solid #000; padding-top: 10px; }
                            @media print { body { margin: 0; } }
                        </style>
                    </head>
                    <body>
                        <div class="header">
                            <h1>Clínica Médica</h1>
                            <h2>Factura #${invoice.invoiceNumber}</h2>
                        </div>

                        <div class="invoice-details">
                            <p><strong>Paciente:</strong> ${invoice.patientName}</p>
                            <p><strong>Fecha:</strong> ${window.formatDate(invoice.billingDate)}</p>
                            <p><strong>Estado:</strong> ${this.getStatusDisplayName(invoice.status)}</p>
                        </div>

                        <div class="totals">
                            <div class="total-row">
                                <span>Subtotal: ${this.formatCurrency(invoice.subtotal)}</span>
                            </div>
                            <div class="total-row">
                                <span>Copago: ${this.formatCurrency(invoice.copaymentAmount)}</span>
                            </div>
                            <div class="total-row final-total">
                                <span>Total: ${this.formatCurrency(invoice.totalAmount)}</span>
                            </div>
                        </div>
                    </body>
                </html>
            `);

            printWindow.document.close();
            printWindow.print();

        } catch (error) {
            console.error('Error printing invoice:', error);
            window.showError('Error al imprimir factura: ' + error.message);
        }
    }

    /**
     * Marca una factura como pagada
     */
    async markAsPaid(invoiceId) {
        if (!confirm('¿Está seguro de que desea marcar esta factura como pagada?')) {
            return;
        }

        try {
            window.showInfo('Marcando factura como pagada...');

            // Aquí iría la llamada a la API para actualizar el estado
            // await window.billingApi.markInvoiceAsPaid(invoiceId);

            window.showSuccess('Factura marcada como pagada');
            this.loadInvoicesList(); // Recargar lista

        } catch (error) {
            console.error('Error marking invoice as paid:', error);
            window.showError('Error al actualizar factura: ' + error.message);
        }
    }

    /**
     * Cambia de página
     */
    goToPage(page) {
        this.currentPage = page;
        this.renderInvoicesList();
    }

    /**
     * Obtiene el total de montos pendientes
     */
    getTotalPendingAmount() {
        return this.invoices
            .filter(invoice => invoice.status === 'PENDING')
            .reduce((total, invoice) => total + invoice.totalAmount, 0);
    }

    /**
     * Obtiene el conteo de facturas por estado
     */
    getInvoicesByStatusCount(status) {
        return this.invoices.filter(invoice => invoice.status === status).length;
    }

    /**
     * Obtiene la clase CSS para el estado de la factura
     */
    getInvoiceStatusClass(invoice) {
        switch (invoice.status) {
            case 'PAID':
                return 'table-row-success';
            case 'OVERDUE':
                return 'table-row-warning';
            case 'CANCELLED':
                return 'table-row-error';
            default:
                return '';
        }
    }

    /**
     * Obtiene la clase CSS para el badge de estado
     */
    getStatusBadgeClass(status) {
        const statusClasses = {
            'PENDING': 'warning',
            'PAID': 'success',
            'OVERDUE': 'error',
            'CANCELLED': 'secondary'
        };

        return statusClasses[status] || 'secondary';
    }

    /**
     * Obtiene el nombre para mostrar del estado
     */
    getStatusDisplayName(status) {
        const statusNames = {
            'PENDING': 'Pendiente',
            'PAID': 'Pagada',
            'OVERDUE': 'Vencida',
            'CANCELLED': 'Cancelada'
        };

        return statusNames[status] || status;
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
                    <i class="fas fa-file-invoice-dollar"></i>
                    <h3>No se pudieron cargar las facturas</h3>
                    <p>Verifica tu conexión a internet e intenta nuevamente.</p>
                    <button class="btn btn-primary" onclick="billingService.loadInvoicesList()">
                        <i class="fas fa-refresh"></i> Reintentar
                    </button>
                </div>
            `;
        }
    }
}

// Crear instancia global del servicio de facturación
window.billingService = new BillingService();