/**
 * Servicio de almacenamiento local para persistencia de datos
 * Maneja el almacenamiento y recuperación de datos en localStorage
 */
class LocalStorageService {
    constructor() {
        this.storageKeys = {
            USERS: 'clinic_users',
            PATIENTS: 'clinic_patients',
            MEDICAL_RECORDS: 'clinic_medical_records',
            APPOINTMENTS: 'clinic_appointments',
            INVENTORY: 'clinic_inventory',
            BILLING: 'clinic_billing',
            ORDERS: 'clinic_orders',
            VITAL_SIGNS: 'clinic_vital_signs',
            SETTINGS: 'clinic_settings'
        };

        this.initializeDefaultData();
    }

    /**
     * Inicializa los datos por defecto si no existen
     */
    initializeDefaultData() {
        // Usuario HR por defecto
        if (!this.getUsers() || this.getUsers().length === 0) {
            const defaultHRUser = {
                id: 'hr_default',
                cedula: '1234567890',
                username: 'admin_hr',
                password: 'password', // En producción esto debería estar hasheado
                fullName: 'Admin Recursos Humanos',
                email: 'admin@clinic.com',
                phoneNumber: '3001234567',
                birthDate: '01/01/1980',
                address: 'Calle 123 #45-67',
                role: 'HUMAN_RESOURCES',
                active: true,
                createdAt: new Date().toISOString(),
                permissions: ['manage-users', 'register-patients', 'view-patients', 'manage-medical-records', 'manage-patient-visits', 'manage-billing', 'manage-inventory', 'manage-appointments']
            };

            this.saveUsers([defaultHRUser]);
        }

        // Configuración inicial
        if (!this.getSettings()) {
            const defaultSettings = {
                clinicName: 'Clínica Médica',
                version: '1.0.0',
                lastBackup: null,
                theme: 'light',
                language: 'es',
                autoSave: true,
                notifications: true
            };

            this.saveSettings(defaultSettings);
        }
    }

    // ===== MÉTODOS PARA USUARIOS =====

    /**
     * Obtiene todos los usuarios
     */
    getUsers() {
        return this.getData(this.storageKeys.USERS, []);
    }

    /**
     * Guarda todos los usuarios
     */
    saveUsers(users) {
        this.saveData(this.storageKeys.USERS, users);
    }

    /**
     * Obtiene un usuario por ID
     */
    getUserById(id) {
        const users = this.getUsers();
        return users.find(user => user.id === id);
    }

    /**
     * Obtiene un usuario por username
     */
    getUserByUsername(username) {
        const users = this.getUsers();
        return users.find(user => user.username === username);
    }

    /**
     * Agrega o actualiza un usuario
     */
    saveUser(user) {
        const users = this.getUsers();
        const existingIndex = users.findIndex(u => u.id === user.id);

        if (existingIndex >= 0) {
            users[existingIndex] = { ...users[existingIndex], ...user, updatedAt: new Date().toISOString() };
        } else {
            user.id = user.id || this.generateId();
            user.createdAt = user.createdAt || new Date().toISOString();
            users.push(user);
        }

        this.saveUsers(users);
        return user;
    }

    /**
     * Elimina un usuario
     */
    deleteUser(id) {
        const users = this.getUsers();
        const filteredUsers = users.filter(user => user.id !== id);
        this.saveUsers(filteredUsers);
    }

    // ===== MÉTODOS PARA PACIENTES =====

    /**
     * Obtiene todos los pacientes
     */
    getPatients() {
        return this.getData(this.storageKeys.PATIENTS, []);
    }

    /**
     * Guarda todos los pacientes
     */
    savePatients(patients) {
        this.saveData(this.storageKeys.PATIENTS, patients);
    }

    /**
     * Obtiene un paciente por ID
     */
    getPatientById(id) {
        const patients = this.getPatients();
        return patients.find(patient => patient.id === id);
    }

    /**
     * Agrega o actualiza un paciente
     */
    savePatient(patient) {
        const patients = this.getPatients();
        const existingIndex = patients.findIndex(p => p.id === patient.id);

        if (existingIndex >= 0) {
            patients[existingIndex] = { ...patients[existingIndex], ...patient, updatedAt: new Date().toISOString() };
        } else {
            patient.id = patient.id || this.generateId();
            patient.createdAt = patient.createdAt || new Date().toISOString();
            patients.push(patient);
        }

        this.savePatients(patients);
        return patient;
    }

    /**
     * Elimina un paciente
     */
    deletePatient(id) {
        const patients = this.getPatients();
        const filteredPatients = patients.filter(patient => patient.id !== id);
        this.savePatients(filteredPatients);
    }

    // ===== MÉTODOS PARA HISTORIAS CLÍNICAS =====

    /**
     * Obtiene todas las historias clínicas
     */
    getMedicalRecords() {
        return this.getData(this.storageKeys.MEDICAL_RECORDS, []);
    }

    /**
     * Guarda todas las historias clínicas
     */
    saveMedicalRecords(records) {
        this.saveData(this.storageKeys.MEDICAL_RECORDS, records);
    }

    /**
     * Obtiene historias clínicas por paciente
     */
    getMedicalRecordsByPatient(patientId) {
        const records = this.getMedicalRecords();
        return records.filter(record => record.patientId === patientId);
    }

    /**
     * Agrega o actualiza una historia clínica
     */
    saveMedicalRecord(record) {
        const records = this.getMedicalRecords();
        const existingIndex = records.findIndex(r => r.id === record.id);

        if (existingIndex >= 0) {
            records[existingIndex] = { ...records[existingIndex], ...record, updatedAt: new Date().toISOString() };
        } else {
            record.id = record.id || this.generateId();
            record.createdAt = record.createdAt || new Date().toISOString();
            records.push(record);
        }

        this.saveMedicalRecords(records);
        return record;
    }

    // ===== MÉTODOS PARA CITAS =====

    /**
     * Obtiene todas las citas
     */
    getAppointments() {
        return this.getData(this.storageKeys.APPOINTMENTS, []);
    }

    /**
     * Guarda todas las citas
     */
    saveAppointments(appointments) {
        this.saveData(this.storageKeys.APPOINTMENTS, appointments);
    }

    /**
     * Agrega o actualiza una cita
     */
    saveAppointment(appointment) {
        const appointments = this.getAppointments();
        const existingIndex = appointments.findIndex(a => a.id === appointment.id);

        if (existingIndex >= 0) {
            appointments[existingIndex] = { ...appointments[existingIndex], ...appointment, updatedAt: new Date().toISOString() };
        } else {
            appointment.id = appointment.id || this.generateId();
            appointment.createdAt = appointment.createdAt || new Date().toISOString();
            appointments.push(appointment);
        }

        this.saveAppointments(appointments);
        return appointment;
    }

    // ===== MÉTODOS PARA INVENTARIO =====

    /**
     * Obtiene todo el inventario
     */
    getInventory() {
        return this.getData(this.storageKeys.INVENTORY, []);
    }

    /**
     * Guarda todo el inventario
     */
    saveInventory(inventory) {
        this.saveData(this.storageKeys.INVENTORY, inventory);
    }

    /**
     * Agrega o actualiza un item del inventario
     */
    saveInventoryItem(item) {
        const inventory = this.getInventory();
        const existingIndex = inventory.findIndex(i => i.id === item.id);

        if (existingIndex >= 0) {
            inventory[existingIndex] = { ...inventory[existingIndex], ...item, updatedAt: new Date().toISOString() };
        } else {
            item.id = item.id || this.generateId();
            item.createdAt = item.createdAt || new Date().toISOString();
            inventory.push(item);
        }

        this.saveInventory(inventory);
        return item;
    }

    // ===== MÉTODOS PARA FACTURACIÓN =====

    /**
     * Obtiene todas las facturas
     */
    getBilling() {
        return this.getData(this.storageKeys.BILLING, []);
    }

    /**
     * Guarda todas las facturas
     */
    saveBilling(billing) {
        this.saveData(this.storageKeys.BILLING, billing);
    }

    /**
     * Agrega o actualiza una factura
     */
    saveBillingItem(item) {
        const billing = this.getBilling();
        const existingIndex = billing.findIndex(b => b.id === item.id);

        if (existingIndex >= 0) {
            billing[existingIndex] = { ...billing[existingIndex], ...item, updatedAt: new Date().toISOString() };
        } else {
            item.id = item.id || this.generateId();
            item.createdAt = item.createdAt || new Date().toISOString();
            billing.push(item);
        }

        this.saveBilling(billing);
        return item;
    }

    // ===== MÉTODOS PARA ÓRDENES =====

    /**
     * Obtiene todas las órdenes
     */
    getOrders() {
        return this.getData(this.storageKeys.ORDERS, []);
    }

    /**
     * Guarda todas las órdenes
     */
    saveOrders(orders) {
        this.saveData(this.storageKeys.ORDERS, orders);
    }

    /**
     * Agrega o actualiza una orden
     */
    saveOrder(order) {
        const orders = this.getOrders();
        const existingIndex = orders.findIndex(o => o.id === order.id);

        if (existingIndex >= 0) {
            orders[existingIndex] = { ...orders[existingIndex], ...order, updatedAt: new Date().toISOString() };
        } else {
            order.id = order.id || this.generateId();
            order.createdAt = order.createdAt || new Date().toISOString();
            orders.push(order);
        }

        this.saveOrders(orders);
        return order;
    }

    // ===== MÉTODOS PARA SIGNOS VITALES =====

    /**
     * Obtiene todos los signos vitales
     */
    getVitalSigns() {
        return this.getData(this.storageKeys.VITAL_SIGNS, []);
    }

    /**
     * Guarda todos los signos vitales
     */
    saveVitalSigns(vitalSigns) {
        this.saveData(this.storageKeys.VITAL_SIGNS, vitalSigns);
    }

    /**
     * Obtiene signos vitales por paciente
     */
    getVitalSignsByPatient(patientId) {
        const vitalSigns = this.getVitalSigns();
        return vitalSigns.filter(vs => vs.patientId === patientId);
    }

    /**
     * Agrega o actualiza signos vitales
     */
    saveVitalSignsRecord(record) {
        const vitalSigns = this.getVitalSigns();
        const existingIndex = vitalSigns.findIndex(vs => vs.id === record.id);

        if (existingIndex >= 0) {
            vitalSigns[existingIndex] = { ...vitalSigns[existingIndex], ...record, updatedAt: new Date().toISOString() };
        } else {
            record.id = record.id || this.generateId();
            record.createdAt = record.createdAt || new Date().toISOString();
            vitalSigns.push(record);
        }

        this.saveVitalSigns(vitalSigns);
        return record;
    }

    // ===== MÉTODOS PARA CONFIGURACIÓN =====

    /**
     * Obtiene la configuración
     */
    getSettings() {
        return this.getData(this.storageKeys.SETTINGS, null);
    }

    /**
     * Guarda la configuración
     */
    saveSettings(settings) {
        this.saveData(this.storageKeys.SETTINGS, settings);
    }

    /**
     * Actualiza una configuración específica
     */
    updateSetting(key, value) {
        const settings = this.getSettings() || {};
        settings[key] = value;
        settings.updatedAt = new Date().toISOString();
        this.saveSettings(settings);
    }

    // ===== MÉTODOS UTILITARIOS =====

    /**
     * Genera un ID único
     */
    generateId() {
        return 'id_' + Date.now() + '_' + Math.random().toString(36).substr(2, 9);
    }

    /**
     * Obtiene datos del localStorage
     */
    getData(key, defaultValue = null) {
        try {
            const data = localStorage.getItem(key);
            return data ? JSON.parse(data) : defaultValue;
        } catch (error) {
            console.error(`Error reading from localStorage for key ${key}:`, error);
            return defaultValue;
        }
    }

    /**
     * Guarda datos en localStorage
     */
    saveData(key, data) {
        try {
            localStorage.setItem(key, JSON.stringify(data));
        } catch (error) {
            console.error(`Error saving to localStorage for key ${key}:`, error);
            throw new Error('Error al guardar datos localmente');
        }
    }

    /**
     * Limpia todos los datos
     */
    clearAllData() {
        Object.values(this.storageKeys).forEach(key => {
            localStorage.removeItem(key);
        });
        this.initializeDefaultData();
    }

    /**
     * Exporta todos los datos
     */
    exportData() {
        const data = {};
        Object.keys(this.storageKeys).forEach(key => {
            data[key] = this.getData(this.storageKeys[key], null);
        });
        return data;
    }

    /**
     * Importa datos
     */
    importData(data) {
        Object.keys(data).forEach(key => {
            if (this.storageKeys[key]) {
                this.saveData(this.storageKeys[key], data[key]);
            }
        });
    }

    /**
     * Obtiene estadísticas del almacenamiento
     */
    getStorageStats() {
        const stats = {};
        Object.keys(this.storageKeys).forEach(key => {
            const data = this.getData(this.storageKeys[key], null);
            stats[key] = {
                count: Array.isArray(data) ? data.length : (data ? 1 : 0),
                size: JSON.stringify(data || '').length
            };
        });
        return stats;
    }
}

// Crear instancia global del servicio de almacenamiento local
window.localStorageService = new LocalStorageService();