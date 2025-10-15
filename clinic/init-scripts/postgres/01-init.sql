-- PostgreSQL initialization script for Clinic Management System

-- Enable UUID extension
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- Create custom domains for business constraints
CREATE DOMAIN email_domain AS VARCHAR(255) CHECK (VALUE ~* '^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,}$');
CREATE DOMAIN phone_domain AS VARCHAR(15) CHECK (VALUE ~* '^\+?[1-9]\d{1,14}$');
CREATE DOMAIN cedula_domain AS VARCHAR(20) CHECK (LENGTH(VALUE) >= 5 AND VALUE ~* '^[A-Za-z0-9-]+$');

-- Create users table
CREATE TABLE IF NOT EXISTS users (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    cedula cedula_domain NOT NULL UNIQUE,
    username VARCHAR(15) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    full_name VARCHAR(100) NOT NULL,
    birth_date DATE NOT NULL,
    address VARCHAR(30) NOT NULL,
    phone_number phone_domain NOT NULL,
    email email_domain NOT NULL,
    role VARCHAR(30) NOT NULL CHECK (role IN ('ADMIN', 'HUMAN_RESOURCES', 'ADMINISTRATIVE_STAFF', 'SUPPORT_STAFF', 'NURSE', 'DOCTOR')),
    is_active BOOLEAN NOT NULL DEFAULT true,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Create patients table
CREATE TABLE IF NOT EXISTS patients (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    cedula cedula_domain NOT NULL UNIQUE,
    username VARCHAR(15) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    full_name VARCHAR(100) NOT NULL,
    birth_date DATE NOT NULL,
    address VARCHAR(30) NOT NULL,
    phone_number phone_domain NOT NULL,
    email email_domain NOT NULL,
    emergency_contact_name VARCHAR(100),
    emergency_contact_phone phone_domain,
    emergency_contact_relationship VARCHAR(50),
    insurance_policy_number VARCHAR(50),
    insurance_company VARCHAR(100),
    insurance_expiry_date DATE,
    is_active BOOLEAN NOT NULL DEFAULT true,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Create appointments table
CREATE TABLE IF NOT EXISTS appointments (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    patient_cedula cedula_domain NOT NULL,
    doctor_cedula cedula_domain NOT NULL,
    appointment_datetime TIMESTAMP NOT NULL,
    status VARCHAR(20) NOT NULL CHECK (status IN ('PROGRAMADA', 'CONFIRMADA', 'CANCELADA', 'COMPLETADA')),
    reason TEXT,
    notes TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (patient_cedula) REFERENCES patients(cedula),
    FOREIGN KEY (doctor_cedula) REFERENCES users(cedula)
);

-- Create inventory_items table
CREATE TABLE IF NOT EXISTS inventory_items (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    name VARCHAR(100) NOT NULL UNIQUE,
    type VARCHAR(50) NOT NULL,
    cost DECIMAL(10,2) NOT NULL CHECK (cost > 0),
    is_active BOOLEAN NOT NULL DEFAULT true,
    description TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Create orders table (for medical orders)
CREATE TABLE IF NOT EXISTS orders (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    order_number VARCHAR(6) NOT NULL UNIQUE,
    patient_cedula cedula_domain NOT NULL,
    doctor_cedula cedula_domain NOT NULL,
    order_type VARCHAR(20) NOT NULL CHECK (order_type IN ('MEDICATION', 'PROCEDURE', 'DIAGNOSTIC_AID')),
    status VARCHAR(20) NOT NULL CHECK (status IN ('PENDING', 'IN_PROGRESS', 'COMPLETED', 'CANCELLED')),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (patient_cedula) REFERENCES patients(cedula),
    FOREIGN KEY (doctor_cedula) REFERENCES users(cedula)
);

-- Create order_items table (for individual items in orders)
CREATE TABLE IF NOT EXISTS order_items (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    order_id UUID NOT NULL,
    item_number INTEGER NOT NULL,
    item_type VARCHAR(20) NOT NULL CHECK (item_type IN ('MEDICATION', 'PROCEDURE', 'DIAGNOSTIC_AID')),
    name VARCHAR(100) NOT NULL,
    description TEXT,
    quantity INTEGER NOT NULL DEFAULT 1,
    unit_cost DECIMAL(10,2) NOT NULL CHECK (unit_cost >= 0),
    total_cost DECIMAL(10,2) NOT NULL CHECK (total_cost >= 0),
    FOREIGN KEY (order_id) REFERENCES orders(id) ON DELETE CASCADE,
    UNIQUE(order_id, item_number)
);

-- Create billing table
CREATE TABLE IF NOT EXISTS billing (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    patient_cedula cedula_domain NOT NULL,
    invoice_number VARCHAR(20) NOT NULL UNIQUE,
    billing_date DATE NOT NULL,
    total_amount DECIMAL(10,2) NOT NULL CHECK (total_amount >= 0),
    insurance_coverage DECIMAL(10,2) NOT NULL DEFAULT 0 CHECK (insurance_coverage >= 0),
    patient_responsibility DECIMAL(10,2) NOT NULL CHECK (patient_responsibility >= 0),
    status VARCHAR(20) NOT NULL CHECK (status IN ('PENDING', 'PAID', 'OVERDUE', 'CANCELLED')),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (patient_cedula) REFERENCES patients(cedula)
);

-- Create billing_items table (for detailed billing breakdown)
CREATE TABLE IF NOT EXISTS billing_items (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    billing_id UUID NOT NULL,
    order_id UUID NOT NULL,
    item_description TEXT NOT NULL,
    original_amount DECIMAL(10,2) NOT NULL CHECK (original_amount >= 0),
    discount_amount DECIMAL(10,2) NOT NULL DEFAULT 0 CHECK (discount_amount >= 0),
    final_amount DECIMAL(10,2) NOT NULL CHECK (final_amount >= 0),
    FOREIGN KEY (billing_id) REFERENCES billing(id) ON DELETE CASCADE,
    FOREIGN KEY (order_id) REFERENCES orders(id)
);

-- Create patient_visits table
CREATE TABLE IF NOT EXISTS patient_visits (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    patient_cedula cedula_domain NOT NULL,
    visit_datetime TIMESTAMP NOT NULL,
    reason TEXT,
    diagnosis TEXT,
    treatment TEXT,
    notes TEXT,
    is_completed BOOLEAN NOT NULL DEFAULT false,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (patient_cedula) REFERENCES patients(cedula)
);

-- Create vital_signs table
CREATE TABLE IF NOT EXISTS vital_signs (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    patient_visit_id UUID NOT NULL,
    blood_pressure_systolic INTEGER CHECK (blood_pressure_systolic BETWEEN 70 AND 250),
    blood_pressure_diastolic INTEGER CHECK (blood_pressure_diastolic BETWEEN 40 AND 150),
    temperature DECIMAL(4,2) CHECK (temperature BETWEEN 35.0 AND 45.0),
    pulse INTEGER CHECK (pulse BETWEEN 30 AND 250),
    oxygen_level INTEGER CHECK (oxygen_level BETWEEN 70 AND 100),
    recorded_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (patient_visit_id) REFERENCES patient_visits(id) ON DELETE CASCADE
);

-- Create indexes for better performance
CREATE INDEX IF NOT EXISTS idx_patients_cedula ON patients(cedula);
CREATE INDEX IF NOT EXISTS idx_patients_username ON patients(username);
CREATE INDEX IF NOT EXISTS idx_users_cedula ON users(cedula);
CREATE INDEX IF NOT EXISTS idx_users_username ON users(username);
CREATE INDEX IF NOT EXISTS idx_users_role ON users(role);
CREATE INDEX IF NOT EXISTS idx_appointments_patient ON appointments(patient_cedula);
CREATE INDEX IF NOT EXISTS idx_appointments_doctor ON appointments(doctor_cedula);
CREATE INDEX IF NOT EXISTS idx_appointments_datetime ON appointments(appointment_datetime);
CREATE INDEX IF NOT EXISTS idx_orders_patient ON orders(patient_cedula);
CREATE INDEX IF NOT EXISTS idx_orders_doctor ON orders(doctor_cedula);
CREATE INDEX IF NOT EXISTS idx_orders_type ON orders(order_type);
CREATE INDEX IF NOT EXISTS idx_billing_patient ON billing(patient_cedula);
CREATE INDEX IF NOT EXISTS idx_billing_date ON billing(billing_date);
CREATE INDEX IF NOT EXISTS idx_patient_visits_patient ON patient_visits(patient_cedula);
CREATE INDEX IF NOT EXISTS idx_patient_visits_datetime ON patient_visits(visit_datetime);

-- Create triggers for updated_at timestamps
CREATE OR REPLACE FUNCTION update_updated_at_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ language 'plpgsql';

CREATE TRIGGER update_users_updated_at BEFORE UPDATE ON users FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();
CREATE TRIGGER update_patients_updated_at BEFORE UPDATE ON patients FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();
CREATE TRIGGER update_appointments_updated_at BEFORE UPDATE ON appointments FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();
CREATE TRIGGER update_orders_updated_at BEFORE UPDATE ON orders FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();
CREATE TRIGGER update_billing_updated_at BEFORE UPDATE ON billing FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();
CREATE TRIGGER update_patient_visits_updated_at BEFORE UPDATE ON patient_visits FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

-- Insert default admin user
INSERT INTO users (cedula, username, password_hash, full_name, birth_date, address, phone_number, email, role, is_active)
VALUES (
    'ADMIN-001',
    'admin',
    '$2a$10$N9qo8uLOickgx2ZMRZoMye1jEftwEfXQqK.qjK7qZ0PqKJL.0jOy', -- 'admin123' hashed
    'System Administrator',
    '1980-01-01',
    'Admin Address',
    '+1234567890',
    'admin@clinic.com',
    'ADMIN',
    true
) ON CONFLICT (username) DO NOTHING;

-- Insert default HR user for testing
INSERT INTO users (cedula, username, password_hash, full_name, birth_date, address, phone_number, email, role, is_active)
VALUES (
    'HR-001',
    'rrhh',
    '$2a$10$N9qo8uLOickgx2ZMRZoMye1jEftwEfXQqK.qjK7qZ0PqKJL.0jOy', -- 'rrhh123' hashed
    'Recursos Humanos',
    '1985-03-15',
    'Calle 123 #45-67',
    '+573001234567',
    'rrhh@clinic.com',
    'HUMAN_RESOURCES',
    true
) ON CONFLICT (username) DO NOTHING;

-- Insert sample data for testing
INSERT INTO patients (cedula, username, password_hash, full_name, birth_date, address, phone_number, email, emergency_contact_name, emergency_contact_phone, insurance_policy_number, insurance_company)
VALUES (
    'CC-12345678',
    'patient1',
    '$2a$10$N9qo8uLOickgx2ZMRZoMye1jEftwEfXQqK.qjK7qZ0PqKJL.0jOy', -- 'patient123' hashed
    'Juan Pérez García',
    '1985-05-15',
    'Calle 123 #45-67',
    '+573001234567',
    'juan.perez@email.com',
    'María García',
    '+573001111111',
    'EPS-001',
    'Sura EPS'
) ON CONFLICT (username) DO NOTHING;

-- Insert sample inventory items
INSERT INTO inventory_items (name, type, cost, description)
VALUES
    ('Paracetamol 500mg', 'MEDICATION', 2500.00, 'Analgésico y antipirético'),
    ('Ibuprofeno 400mg', 'MEDICATION', 3000.00, 'Antiinflamatorio no esteroideo'),
    ('Amoxicilina 500mg', 'MEDICATION', 4500.00, 'Antibiótico de amplio espectro'),
    ('Hemograma completo', 'DIAGNOSTIC_AID', 45000.00, 'Análisis de sangre completo'),
    ('Radiografía de tórax', 'DIAGNOSTIC_AID', 80000.00, 'Imagen diagnóstica'),
    ('Resonancia magnética', 'DIAGNOSTIC_AID', 350000.00, 'Imagen avanzada de diagnóstico'),
    ('Consulta médica general', 'PROCEDURE', 60000.00, 'Consulta con médico general'),
    ('Consulta médica especializada', 'PROCEDURE', 80000.00, 'Consulta con especialista'),
    ('Curación de herida', 'PROCEDURE', 25000.00, 'Procedimiento de limpieza y cuidado de heridas'),
    ('Inyección intramuscular', 'PROCEDURE', 15000.00, 'Administración de medicamentos vía IM')
ON CONFLICT (name) DO NOTHING;

-- Insert additional test users for comprehensive testing
INSERT INTO users (cedula, username, password_hash, full_name, birth_date, address, phone_number, email, role, is_active)
VALUES
    ('HR-002', 'rrhh2', '$2a$10$N9qo8uLOickgx2ZMRZoMye1jEftwEfXQqK.qjK7qZ0PqKJL.0jOy', 'Lucía Fernández', '1979-05-03', 'Carrera 78 #90-12', '+573151112223', 'lucia.fernandez@clinica.com', 'HUMAN_RESOURCES', true),
    ('DOC-002', 'drgarcia', '$2a$10$N9qo8uLOickgx2ZMRZoMye1jEftwEfXQqK.qjK7qZ0PqKJL.0jOy', 'Dra. Carmen García', '1980-12-25', 'Calle 56 #78-90', '+573113344556', 'carmen.garcia@clinica.com', 'DOCTOR', true),
    ('NURSE-002', 'enfmorales', '$2a$10$N9qo8uLOickgx2ZMRZoMye1jEftwEfXQqK.qjK7qZ0PqKJL.0jOy', 'Enf. Pedro Morales', '1988-04-08', 'Carrera 23 #45-67', '+573127788990', 'pedro.morales@clinica.com', 'NURSE', true),
    ('ADMIN-002', 'admin2', '$2a$10$N9qo8uLOickgx2ZMRZoMye1jEftwEfXQqK.qjK7qZ0PqKJL.0jOy', 'Sofía Herrera', '1983-09-30', 'Avenida 12 #34-56', '+573132233445', 'sofia.herrera@clinica.com', 'ADMINISTRATIVE_STAFF', true),
    ('SUPPORT-002', 'sop2', '$2a$10$N9qo8uLOickgx2ZMRZoMye1jEftwEfXQqK.qjK7qZ0PqKJL.0jOy', 'Miguel Torres', '1986-07-14', 'Calle 89 #01-23', '+573146677889', 'miguel.torres@clinica.com', 'SUPPORT_STAFF', false)
ON CONFLICT (username) DO NOTHING;

-- Insert sample patients for administrative testing
INSERT INTO patients (cedula, username, password_hash, full_name, birth_date, address, phone_number, email, emergency_contact_name, emergency_contact_phone, insurance_policy_number, insurance_company)
VALUES
    ('CC-10000001', 'paciente001', '$2a$10$N9qo8uLOickgx2ZMRZoMye1jEftwEfXQqK.qjK7qZ0PqKJL.0jOy', 'María Fernanda Castro', '1980-05-15', 'Calle 100 #20-30', '+573001000001', 'maria.castro@email.com', 'Carlos Castro', '+573101000001', 'POL001234567', 'Seguros Bolívar'),
    ('CC-20000002', 'paciente002', '$2a$10$N9qo8uLOickgx2ZMRZoMye1jEftwEfXQqK.qjK7qZ0PqKJL.0jOy', 'Roberto Jiménez', '1975-08-22', 'Carrera 50 #75-20', '+573102000002', 'roberto.jimenez@email.com', 'Carmen Jiménez', '+573202000002', 'POL002345678', 'Sura EPS'),
    ('CC-30000003', 'paciente003', '$2a$10$N9qo8uLOickgx2ZMRZoMye1jEftwEfXQqK.qjK7qZ0PqKJL.0jOy', 'Ana María González', '1992-03-10', 'Calle 25 #40-15', '+573123000003', 'ana.gonzalez@email.com', 'José González', '+573113000003', NULL, NULL)
ON CONFLICT (username) DO NOTHING;

COMMIT;