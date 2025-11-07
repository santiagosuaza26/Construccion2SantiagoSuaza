-- V1__Create_tables.sql
-- Script de migración inicial para la base de datos de la clínica
-- Compatible con H2 Database

-- Tabla de usuarios
CREATE TABLE users (
    id VARCHAR(20) PRIMARY KEY,
    full_name VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    phone VARCHAR(15) NOT NULL,
    date_of_birth VARCHAR(10) NOT NULL,
    address VARCHAR(30) NOT NULL,
    role VARCHAR(50) NOT NULL CHECK (role IN ('MEDICO', 'ENFERMERA', 'PERSONAL_ADMINISTRATIVO', 'RECURSOS_HUMANOS', 'SOPORTE_DE_INFORMACION')),
    username VARCHAR(15) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL
);

-- Tabla de pacientes
CREATE TABLE patients (
    identification_number VARCHAR(20) PRIMARY KEY,
    full_name VARCHAR(100) NOT NULL,
    date_of_birth VARCHAR(10) NOT NULL,
    gender VARCHAR(10) NOT NULL CHECK (gender IN ('masculino', 'femenino', 'otro')),
    address VARCHAR(100) NOT NULL,
    phone VARCHAR(15) NOT NULL,
    email VARCHAR(100),
    emergency_contact_name VARCHAR(100),
    emergency_contact_relation VARCHAR(50),
    emergency_contact_phone VARCHAR(15),
    insurance_company_name VARCHAR(100),
    insurance_policy_number VARCHAR(50),
    insurance_active BOOLEAN NOT NULL DEFAULT FALSE,
    insurance_validity_date VARCHAR(10),
    annual_copay_total DECIMAL(10,2) DEFAULT 0.00
);

-- Tabla de citas
CREATE TABLE appointments (
    id VARCHAR(50) PRIMARY KEY,
    patient_id VARCHAR(20) NOT NULL,
    admin_id VARCHAR(20),
    doctor_id VARCHAR(20),
    appointment_date TIMESTAMP NOT NULL,
    reason TEXT,
    status VARCHAR(20) NOT NULL DEFAULT 'scheduled' CHECK (status IN ('scheduled', 'completed', 'cancelled')),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Tabla de medicamentos
CREATE TABLE medications (
    id VARCHAR(20) PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    cost DECIMAL(10,2) NOT NULL,
    requires_specialist BOOLEAN NOT NULL DEFAULT FALSE,
    specialist_type VARCHAR(50)
);

-- Tabla de procedimientos
CREATE TABLE procedures (
    id VARCHAR(20) PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    cost DECIMAL(10,2) NOT NULL,
    requires_specialist BOOLEAN NOT NULL DEFAULT FALSE,
    specialist_type VARCHAR(50)
);

-- Tabla de ayudas diagnósticas
CREATE TABLE diagnostic_aids (
    id VARCHAR(20) PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    cost DECIMAL(10,2) NOT NULL,
    requires_specialist BOOLEAN NOT NULL DEFAULT FALSE,
    specialist_type VARCHAR(50)
);

-- Tabla de órdenes
CREATE TABLE orders (
    order_number VARCHAR(10) PRIMARY KEY,
    patient_identification_number VARCHAR(20) NOT NULL,
    doctor_identification_number VARCHAR(20) NOT NULL,
    date DATE NOT NULL,
    diagnosis TEXT
);

-- Tabla de órdenes de medicamentos
CREATE TABLE medication_orders (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    order_number VARCHAR(10) NOT NULL,
    item INTEGER NOT NULL,
    medication_id VARCHAR(20) NOT NULL,
    dosage VARCHAR(50) NOT NULL,
    duration VARCHAR(50) NOT NULL,
    cost DECIMAL(10,2) NOT NULL,
    UNIQUE(order_number, item)
);

-- Tabla de órdenes de procedimientos
CREATE TABLE procedure_orders (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    order_number VARCHAR(10) NOT NULL,
    item INTEGER NOT NULL,
    procedure_id VARCHAR(20) NOT NULL,
    quantity VARCHAR(50) NOT NULL,
    frequency VARCHAR(50) NOT NULL,
    requires_specialist BOOLEAN NOT NULL DEFAULT FALSE,
    specialist_id VARCHAR(20),
    cost DECIMAL(10,2) NOT NULL,
    UNIQUE(order_number, item)
);

-- Tabla de órdenes de ayudas diagnósticas
CREATE TABLE diagnostic_aid_orders (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    order_number VARCHAR(10) NOT NULL,
    item INTEGER NOT NULL,
    diagnostic_aid_id VARCHAR(20) NOT NULL,
    quantity VARCHAR(50) NOT NULL,
    requires_specialist BOOLEAN NOT NULL DEFAULT FALSE,
    specialist_id VARCHAR(20),
    cost DECIMAL(10,2) NOT NULL,
    UNIQUE(order_number, item)
);

-- Tabla de signos vitales
CREATE TABLE vital_signs (
    patient_identification_number VARCHAR(20) NOT NULL,
    date_time TIMESTAMP NOT NULL,
    blood_pressure VARCHAR(20),
    temperature DECIMAL(4,1),
    pulse INTEGER,
    oxygen_level INTEGER,
    observations TEXT,
    PRIMARY KEY (patient_identification_number, date_time)
);

-- Tabla de facturación
CREATE TABLE billings (
    order_number VARCHAR(10) PRIMARY KEY,
    patient_name VARCHAR(100) NOT NULL,
    age INTEGER NOT NULL,
    identification_number VARCHAR(20) NOT NULL,
    doctor_name VARCHAR(100) NOT NULL,
    company VARCHAR(100),
    policy_number VARCHAR(50),
    validity_days INTEGER,
    validity_date DATE,
    total_cost DECIMAL(10,2) NOT NULL,
    copay DECIMAL(10,2) DEFAULT 0.00,
    insurance_coverage DECIMAL(10,2) DEFAULT 0.00,
    applied_medications TEXT,
    applied_procedures TEXT,
    applied_diagnostic_aids TEXT,
    generated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    generated_by VARCHAR(20) NOT NULL
);

-- Tabla de tickets de soporte
CREATE TABLE support_tickets (
    id VARCHAR(50) PRIMARY KEY,
    user_id VARCHAR(20) NOT NULL,
    issue_description TEXT NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    status VARCHAR(20) NOT NULL DEFAULT 'open' CHECK (status IN ('open', 'in_progress', 'resolved', 'closed')),
    assigned_to VARCHAR(20),
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Índices para mejorar rendimiento
CREATE INDEX idx_appointments_patient_id ON appointments(patient_id);
CREATE INDEX idx_appointments_doctor_id ON appointments(doctor_id);
CREATE INDEX idx_appointments_date ON appointments(appointment_date);
CREATE INDEX idx_orders_patient_id ON orders(patient_identification_number);
CREATE INDEX idx_orders_doctor_id ON orders(doctor_identification_number);
CREATE INDEX idx_orders_date ON orders(date);
CREATE INDEX idx_medication_orders_order_number ON medication_orders(order_number);
CREATE INDEX idx_procedure_orders_order_number ON procedure_orders(order_number);
CREATE INDEX idx_diagnostic_aid_orders_order_number ON diagnostic_aid_orders(order_number);
CREATE INDEX idx_vital_signs_patient_id ON vital_signs(patient_identification_number);
CREATE INDEX idx_billings_patient_id ON billings(identification_number);
CREATE INDEX idx_support_tickets_user_id ON support_tickets(user_id);
CREATE INDEX idx_support_tickets_assigned_to ON support_tickets(assigned_to);
CREATE INDEX idx_support_tickets_status ON support_tickets(status);

-- Foreign Key Constraints
ALTER TABLE appointments ADD CONSTRAINT fk_appointments_patient_id FOREIGN KEY (patient_id) REFERENCES patients(identification_number) ON DELETE CASCADE;
ALTER TABLE appointments ADD CONSTRAINT fk_appointments_admin_id FOREIGN KEY (admin_id) REFERENCES users(id) ON DELETE SET NULL;
ALTER TABLE appointments ADD CONSTRAINT fk_appointments_doctor_id FOREIGN KEY (doctor_id) REFERENCES users(id) ON DELETE SET NULL;

ALTER TABLE orders ADD CONSTRAINT fk_orders_patient_id FOREIGN KEY (patient_identification_number) REFERENCES patients(identification_number) ON DELETE CASCADE;
ALTER TABLE orders ADD CONSTRAINT fk_orders_doctor_id FOREIGN KEY (doctor_identification_number) REFERENCES users(id) ON DELETE CASCADE;

ALTER TABLE medication_orders ADD CONSTRAINT fk_medication_orders_order_number FOREIGN KEY (order_number) REFERENCES orders(order_number) ON DELETE CASCADE;
ALTER TABLE medication_orders ADD CONSTRAINT fk_medication_orders_medication_id FOREIGN KEY (medication_id) REFERENCES medications(id) ON DELETE CASCADE;

ALTER TABLE procedure_orders ADD CONSTRAINT fk_procedure_orders_order_number FOREIGN KEY (order_number) REFERENCES orders(order_number) ON DELETE CASCADE;
ALTER TABLE procedure_orders ADD CONSTRAINT fk_procedure_orders_procedure_id FOREIGN KEY (procedure_id) REFERENCES procedures(id) ON DELETE CASCADE;
ALTER TABLE procedure_orders ADD CONSTRAINT fk_procedure_orders_specialist_id FOREIGN KEY (specialist_id) REFERENCES users(id) ON DELETE SET NULL;

ALTER TABLE diagnostic_aid_orders ADD CONSTRAINT fk_diagnostic_aid_orders_order_number FOREIGN KEY (order_number) REFERENCES orders(order_number) ON DELETE CASCADE;
ALTER TABLE diagnostic_aid_orders ADD CONSTRAINT fk_diagnostic_aid_orders_diagnostic_aid_id FOREIGN KEY (diagnostic_aid_id) REFERENCES diagnostic_aids(id) ON DELETE CASCADE;
ALTER TABLE diagnostic_aid_orders ADD CONSTRAINT fk_diagnostic_aid_orders_specialist_id FOREIGN KEY (specialist_id) REFERENCES users(id) ON DELETE SET NULL;

ALTER TABLE vital_signs ADD CONSTRAINT fk_vital_signs_patient_id FOREIGN KEY (patient_identification_number) REFERENCES patients(identification_number) ON DELETE CASCADE;

ALTER TABLE billings ADD CONSTRAINT fk_billings_order_number FOREIGN KEY (order_number) REFERENCES orders(order_number) ON DELETE CASCADE;
ALTER TABLE billings ADD CONSTRAINT fk_billings_generated_by FOREIGN KEY (generated_by) REFERENCES users(id) ON DELETE SET NULL;

ALTER TABLE support_tickets ADD CONSTRAINT fk_support_tickets_user_id FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE;
ALTER TABLE support_tickets ADD CONSTRAINT fk_support_tickets_assigned_to FOREIGN KEY (assigned_to) REFERENCES users(id) ON DELETE SET NULL;