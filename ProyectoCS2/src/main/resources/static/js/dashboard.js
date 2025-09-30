/**
 * Sistema de Gestión Clínica CS2 - Funcionalidades del Dashboard
 */

// Estado de la aplicación
let currentSection = 'dashboard-overview';

/**
 * Muestra una sección específica del dashboard
 */
function showSection(sectionId) {
    if (!AuthManager.isAuthenticated()) {
        showLoginScreen();
        return;
    }

    // Verificar permisos para la sección
    if (!checkSectionPermission(sectionId)) {
        handlePermissionError();
        return;
    }

    // Ocultar todas las secciones
    const sections = document.querySelectorAll('.content-section');
    sections.forEach(section => {
        section.classList.remove('active');
    });

    // Mostrar la sección seleccionada
    const targetSection = document.getElementById(sectionId);
    if (targetSection) {
        targetSection.classList.add('active');
        currentSection = sectionId;

        // Actualizar navegación activa
        updateActiveNavigation(sectionId);

        // Cargar datos específicos de la sección
        loadSectionData(sectionId);
    }
}

/**
 * Verifica permisos para acceder a una sección
 */
function checkSectionPermission(sectionId) {
    if (!currentUser) return false;

    const sectionPermissions = {
        'dashboard-overview': true, // Todos pueden ver el dashboard
        'patients': ['ADMINISTRATIVE'].includes(currentUser.role),
        'appointments': ['ADMINISTRATIVE'].includes(currentUser.role),
        'billing': ['ADMINISTRATIVE'].includes(currentUser.role),
        'clinical-history': ['DOCTOR', 'NURSE'].includes(currentUser.role),
        'medical-orders': ['DOCTOR'].includes(currentUser.role),
        'vital-signs': ['DOCTOR', 'NURSE'].includes(currentUser.role),
        'users': ['HUMAN_RESOURCES'].includes(currentUser.role),
        'staff': ['HUMAN_RESOURCES'].includes(currentUser.role),
        'inventory': ['SUPPORT'].includes(currentUser.role),
        'reports': ['ADMINISTRATIVE', 'SUPPORT'].includes(currentUser.role)
    };

    return sectionPermissions[sectionId] || false;
}

/**
 * Actualiza la navegación activa
 */
function updateActiveNavigation(sectionId) {
    const navLinks = document.querySelectorAll('.nav-link');
    navLinks.forEach(link => {
        link.classList.remove('active');
    });

    // Buscar el enlace correspondiente
    const activeLink = document.querySelector(`[onclick="showSection('${sectionId}')"]`);
    if (activeLink) {
        activeLink.classList.add('active');
    }
}

/**
 * Carga datos específicos de cada sección
 */
async function loadSectionData(sectionId) {
    try {
        switch (sectionId) {
            case 'dashboard-overview':
                await loadDashboardData();
                break;
            case 'patients':
                await loadPatientsData();
                break;
            case 'clinical-history':
                // No cargar datos hasta que se busque un paciente
                break;
            case 'medical-orders':
                await loadOrdersData();
                break;
            case 'vital-signs':
                // No cargar datos hasta que se seleccione un paciente
                break;
            case 'users':
                await loadUsersData();
                break;
            case 'inventory':
                await loadInventoryData();
                break;
            case 'billing':
                await loadBillingData();
                break;
            case 'reports':
                // Los reportes se generan bajo demanda
                break;
        }
    } catch (error) {
        console.error(`Error cargando datos de sección ${sectionId}:`, error);
        ApiUtils.handleError(error, 'Error al cargar datos de la sección');
    }
}

/**
 * Función para mostrar notificaciones
 */
function showNotification(message, type = 'info') {
    return ApiUtils.showNotification(message, type);
}

/**
 * Función para formatear fechas
 */
function formatDate(dateString) {
    return ApiUtils.formatDate(dateString);
}

/**
 * Función para formatear moneda
 */
function formatCurrency(amount) {
    return ApiUtils.formatCurrency(amount);
}

/**
 * Función para validar email
 */
function isValidEmail(email) {
    return ApiUtils.isValidEmail(email);
}

/**
 * Función para validar cédula
 */
function isValidIdCard(idCard) {
    return ApiUtils.isValidIdCard(idCard);
}

/**
 * Función para validar teléfono
 */
function isValidPhone(phone) {
    return ApiUtils.isValidPhone(phone);
}

/**
 * Función para mostrar pantalla de carga en una sección
 */
function showSectionLoading(sectionId) {
    const section = document.getElementById(sectionId);
    if (section) {
        section.innerHTML = `
            <div class="section-loading">
                <i class="fas fa-spinner fa-spin"></i>
                <p>Cargando...</p>
            </div>
        `;
    }
}

/**
 * Función para mostrar errores en una sección
 */
function showSectionError(sectionId, message) {
    const section = document.getElementById(sectionId);
    if (section) {
        section.innerHTML = `
            <div class="section-error">
                <i class="fas fa-exclamation-triangle"></i>
                <p>${message}</p>
                <button onclick="loadSectionData('${sectionId}')" class="btn-secondary">
                    <i class="fas fa-redo"></i>
                    Reintentar
                </button>
            </div>
        `;
    }
}

/**
 * Función para mostrar mensajes vacíos
 */
function showEmptyState(sectionId, message, icon = 'fas fa-inbox') {
    const section = document.getElementById(sectionId);
    if (section) {
        section.innerHTML = `
            <div class="empty-state">
                <i class="${icon}"></i>
                <h3>No hay datos</h3>
                <p>${message}</p>
            </div>
        `;
    }
}

/**
 * Función para crear elementos de tabla dinámicamente
 */
function createTableRow(data, columns) {
    const row = document.createElement('tr');

    columns.forEach(column => {
        const cell = document.createElement('td');

        if (column.type === 'date') {
            cell.textContent = formatDate(data[column.key]);
        } else if (column.type === 'currency') {
            cell.textContent = formatCurrency(data[column.key]);
        } else if (column.type === 'status') {
            cell.innerHTML = `<span class="status-badge status-${data[column.key].toLowerCase()}">${data[column.key]}</span>`;
        } else if (column.type === 'actions') {
            cell.innerHTML = column.render(data);
        } else {
            cell.textContent = data[column.key] || '';
        }

        row.appendChild(cell);
    });

    return row;
}

/**
 * Función para actualizar el título de la página
 */
function updatePageTitle(title) {
    document.title = `${title} - Sistema Clínico CS2`;
}

/**
 * Función para exportar datos a CSV
 */
function exportToCSV(data, filename) {
    if (!data || data.length === 0) {
        showNotification('No hay datos para exportar', 'warning');
        return;
    }

    const headers = Object.keys(data[0]);
    const csvContent = [
        headers.join(','),
        ...data.map(row =>
            headers.map(header => {
                const value = row[header] || '';
                // Escapar comillas y valores con comas
                return typeof value === 'string' && (value.includes(',') || value.includes('"'))
                    ? `"${value.replace(/"/g, '""')}"`
                    : value;
            }).join(',')
        )
    ].join('\n');

    const blob = new Blob([csvContent], { type: 'text/csv;charset=utf-8;' });
    const link = document.createElement('a');
    const url = URL.createObjectURL(blob);

    link.setAttribute('href', url);
    link.setAttribute('download', filename);
    link.style.visibility = 'hidden';

    document.body.appendChild(link);
    link.click();
    document.body.removeChild(link);

    showNotification('Datos exportados correctamente', 'success');
}

/**
 * Función para imprimir contenido
 */
function printContent(elementId) {
    const element = document.getElementById(elementId);
    if (!element) {
        showNotification('Elemento no encontrado para imprimir', 'error');
        return;
    }

    const printWindow = window.open('', '_blank');
    printWindow.document.write(`
        <!DOCTYPE html>
        <html>
        <head>
            <title>Imprimir - Sistema Clínico CS2</title>
            <link href="css/styles.css" rel="stylesheet">
            <style>
                @media print {
                    body { margin: 0; }
                    .no-print { display: none; }
                    .page-break { page-break-before: always; }
                }
            </style>
        </head>
        <body>
            ${element.innerHTML}
        </body>
        </html>
    `);
    printWindow.document.close();
    printWindow.print();
}

/**
 * Función para refrescar datos actuales
 */
function refreshCurrentSection() {
    loadSectionData(currentSection);
    showNotification('Datos actualizados', 'success');
}

/**
 * Función para mostrar ayuda contextual
 */
function showContextHelp(sectionId) {
    const helpContent = {
        'dashboard-overview': 'Panel principal con estadísticas generales del sistema',
        'patients': 'Gestión completa de pacientes: registro, búsqueda, edición y eliminación',
        'clinical-history': 'Consulta y gestión de historias clínicas de pacientes',
        'medical-orders': 'Creación y seguimiento de órdenes médicas',
        'vital-signs': 'Registro y consulta de signos vitales de pacientes',
        'users': 'Administración de usuarios del sistema',
        'inventory': 'Control de inventario médico y suministros',
        'billing': 'Gestión de facturación y pagos',
        'reports': 'Generación de reportes y análisis de datos'
    };

    const message = helpContent[sectionId] || 'Ayuda no disponible para esta sección';
    showNotification(message, 'info');
}

/**
 * Función para validar formularios
 */
function validateForm(form, rules) {
    const errors = [];

    Object.keys(rules).forEach(fieldName => {
        const field = form.querySelector(`[name="${fieldName}"]`) || form.querySelector(`#${fieldName}`);
        const value = field ? field.value.trim() : '';
        const fieldRules = rules[fieldName];

        // Validar requerido
        if (fieldRules.required && !value) {
            errors.push(`${fieldRules.label || fieldName} es requerido`);
            return;
        }

        // Validar mínimo
        if (fieldRules.minLength && value.length < fieldRules.minLength) {
            errors.push(`${fieldRules.label || fieldName} debe tener al menos ${fieldRules.minLength} caracteres`);
        }

        // Validar máximo
        if (fieldRules.maxLength && value.length > fieldRules.maxLength) {
            errors.push(`${fieldRules.label || fieldName} no puede tener más de ${fieldRules.maxLength} caracteres`);
        }

        // Validar patrón
        if (fieldRules.pattern && !fieldRules.pattern.test(value)) {
            errors.push(`${fieldRules.label || fieldName} tiene un formato inválido`);
        }

        // Validar email
        if (fieldRules.email && !isValidEmail(value)) {
            errors.push(`${fieldRules.label || fieldName} debe ser un email válido`);
        }

        // Validar cédula
        if (fieldRules.idCard && !isValidIdCard(value)) {
            errors.push(`${fieldRules.label || fieldName} debe ser una cédula válida`);
        }

        // Validar teléfono
        if (fieldRules.phone && !isValidPhone(value)) {
            errors.push(`${fieldRules.label || fieldName} debe ser un teléfono válido`);
        }
    });

    return errors;
}

/**
 * Función para mostrar errores de validación
 */
function showValidationErrors(errors) {
    if (errors.length === 0) return;

    const errorMessage = errors.join('<br>');
    showNotification(errorMessage, 'error');
}

/**
 * Función para limpiar formularios
 */
function clearForm(form) {
    const inputs = form.querySelectorAll('input, select, textarea');
    inputs.forEach(input => {
        input.value = '';
    });

    // Limpiar errores
    const errorElements = form.querySelectorAll('.field-error');
    errorElements.forEach(element => {
        element.style.display = 'none';
    });
}

/**
 * Función para confirmar acciones destructivas
 */
function confirmAction(message, callback) {
    if (confirm(message)) {
        callback();
    }
}

/**
 * Función para debounce de búsquedas
 */
function debounce(func, wait) {
    let timeout;
    return function executedFunction(...args) {
        const later = () => {
            clearTimeout(timeout);
            func(...args);
        };
        clearTimeout(timeout);
        timeout = setTimeout(later, wait);
    };
}

/**
 * Función para throttling de eventos
 */
function throttle(func, limit) {
    let inThrottle;
    return function() {
        const args = arguments;
        const context = this;
        if (!inThrottle) {
            func.apply(context, args);
            inThrottle = true;
            setTimeout(() => inThrottle = false, limit);
        }
    }
}

/**
 * Función para manejar cambios de tamaño de ventana
 */
function handleWindowResize() {
    // Ajustar sidebar en móviles
    const sidebar = document.getElementById('sidebar');
    const overlay = document.querySelector('.sidebar-overlay');

    if (window.innerWidth > 768) {
        if (sidebar) {
            sidebar.classList.remove('show');
        }
        if (overlay) {
            overlay.classList.remove('show');
        }
    }
}

// Eventos de ventana
window.addEventListener('resize', throttle(handleWindowResize, 250));

// Función para inicializar tooltips (si se implementan)
function initializeTooltips() {
    const tooltipElements = document.querySelectorAll('[data-tooltip]');
    tooltipElements.forEach(element => {
        element.addEventListener('mouseenter', function() {
            showTooltip(this, this.getAttribute('data-tooltip'));
        });

        element.addEventListener('mouseleave', function() {
            hideTooltip();
        });
    });
}

/**
 * Función para mostrar tooltip
 */
function showTooltip(element, message) {
    const tooltip = document.createElement('div');
    tooltip.className = 'tooltip';
    tooltip.textContent = message;

    document.body.appendChild(tooltip);

    const rect = element.getBoundingClientRect();
    tooltip.style.left = `${rect.left + rect.width / 2}px`;
    tooltip.style.top = `${rect.top - tooltip.offsetHeight - 10}px`;

    // Ajustar posición si se sale de la pantalla
    if (parseFloat(tooltip.style.left) + tooltip.offsetWidth > window.innerWidth) {
        tooltip.style.left = `${window.innerWidth - tooltip.offsetWidth - 10}px`;
    }

    setTimeout(() => {
        tooltip.style.opacity = '1';
        tooltip.style.transform = 'translateY(0)';
    }, 10);
}

/**
 * Función para ocultar tooltip
 */
function hideTooltip() {
    const tooltip = document.querySelector('.tooltip');
    if (tooltip) {
        tooltip.style.opacity = '0';
        tooltip.style.transform = 'translateY(10px)';
        setTimeout(() => {
            if (tooltip.parentNode) {
                tooltip.parentNode.removeChild(tooltip);
            }
        }, 300);
    }
}

// Inicializar funcionalidades cuando el DOM esté listo
document.addEventListener('DOMContentLoaded', function() {
    // Inicializar tooltips si existen elementos con data-tooltip
    initializeTooltips();

    // Configurar búsqueda global
    const globalSearch = document.querySelector('.search-box input');
    if (globalSearch) {
        const debouncedSearch = debounce(function(value) {
            performGlobalSearch(value);
        }, 300);

        globalSearch.addEventListener('input', function() {
            debouncedSearch(this.value);
        });
    }
});

/**
 * Función para realizar búsqueda global
 */
function performGlobalSearch(query) {
    if (!query || query.length < 2) return;

    console.log('Búsqueda global:', query);

    // Implementar búsqueda según la sección actual
    switch (currentSection) {
        case 'patients':
            searchPatients(query);
            break;
        case 'inventory':
            searchInventory(query);
            break;
        default:
            // Búsqueda general en múltiples módulos
            searchAcrossModules(query);
    }
}

/**
 * Función para buscar en múltiples módulos
 */
async function searchAcrossModules(query) {
    try {
        const results = [];

        // Buscar en pacientes
        if (checkPermission('patients')) {
            const patients = await PatientAPI.getAll();
            const patientMatches = patients.filter(patient =>
                patient.name.toLowerCase().includes(query.toLowerCase()) ||
                patient.idCard.includes(query)
            );
            results.push(...patientMatches.map(p => ({ type: 'Paciente', data: p })));
        }

        // Buscar en órdenes médicas
        if (checkPermission('medical-orders')) {
            const orders = await OrdersAPI.getAll();
            const orderMatches = orders.filter(order =>
                order.patientName.toLowerCase().includes(query.toLowerCase()) ||
                order.orderNumber.includes(query)
            );
            results.push(...orderMatches.map(o => ({ type: 'Orden Médica', data: o })));
        }

        // Mostrar resultados en una notificación o modal
        if (results.length > 0) {
            showNotification(`Encontrados ${results.length} resultados para "${query}"`, 'info');
        } else {
            showNotification(`No se encontraron resultados para "${query}"`, 'info');
        }

    } catch (error) {
        console.error('Error en búsqueda global:', error);
    }
}