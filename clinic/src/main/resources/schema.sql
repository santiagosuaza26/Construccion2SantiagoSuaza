-- Schema initialization for Clinic Management System (H2 Compatible)

-- Users table
CREATE TABLE users (
    id VARCHAR(20) PRIMARY KEY,
    username VARCHAR(15) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    full_name VARCHAR(255) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    phone VARCHAR(10) NOT NULL,
    date_of_birth DATE NOT NULL,
    address VARCHAR(30) NOT NULL,
    role VARCHAR(50) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Patients table
CREATE TABLE patients (
    id VARCHAR(20) PRIMARY KEY,
    full_name VARCHAR(255) NOT NULL,
    date_of_birth DATE NOT NULL,
    gender VARCHAR(10) NOT NULL,
    address VARCHAR(30) NOT NULL,
    phone VARCHAR(10) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    emergency_name VARCHAR(255) NOT NULL,
    emergency_relation VARCHAR(100) NOT NULL,
    emergency_phone VARCHAR(10) NOT NULL,
    insurance_company VARCHAR(255),
    insurance_policy VARCHAR(100),
    insurance_active BOOLEAN DEFAULT FALSE,
    insurance_validity_date DATE,
    annual_copay_total DECIMAL(10,2) DEFAULT 0.0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Appointments table
CREATE TABLE appointments (
    id VARCHAR(20) PRIMARY KEY,
    patient_id VARCHAR(20) NOT NULL,
    admin_id VARCHAR(20) NOT NULL,
    doctor_id VARCHAR(20) NOT NULL,
    appointment_date TIMESTAMP NOT NULL,
    reason CLOB,
    status VARCHAR(20) DEFAULT 'SCHEDULED',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (patient_id) REFERENCES patients(id),
    FOREIGN KEY (admin_id) REFERENCES users(id),
    FOREIGN KEY (doctor_id) REFERENCES users(id)
);

-- Medications table
CREATE TABLE medications (
    id VARCHAR(20) PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    cost DECIMAL(10,2) NOT NULL,
    requires_specialist BOOLEAN DEFAULT FALSE,
    specialist_type VARCHAR(100),
    stock_quantity INTEGER DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Procedures table
CREATE TABLE procedures (
    id VARCHAR(20) PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    cost DECIMAL(10,2) NOT NULL,
    requires_specialist BOOLEAN DEFAULT FALSE,
    specialist_type VARCHAR(100),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Diagnostic aids table
CREATE TABLE diagnostic_aids (
    id VARCHAR(20) PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    cost DECIMAL(10,2) NOT NULL,
    requires_specialist BOOLEAN DEFAULT FALSE,
    specialist_type VARCHAR(100),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Orders table
CREATE TABLE orders (
    order_number VARCHAR(6) PRIMARY KEY,
    patient_id VARCHAR(20) NOT NULL,
    doctor_id VARCHAR(20) NOT NULL,
    order_type VARCHAR(20) NOT NULL, -- MEDICATION, PROCEDURE, DIAGNOSTIC_AID
    status VARCHAR(20) DEFAULT 'PENDING',
    total_cost DECIMAL(10,2) DEFAULT 0.0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (patient_id) REFERENCES patients(id),
    FOREIGN KEY (doctor_id) REFERENCES users(id)
);

-- Medication orders
CREATE TABLE medication_orders (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    order_number VARCHAR(6) NOT NULL,
    medication_id VARCHAR(20) NOT NULL,
    dosage VARCHAR(100) NOT NULL,
    duration VARCHAR(100) NOT NULL,
    item_number INTEGER NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (order_number) REFERENCES orders(order_number),
    FOREIGN KEY (medication_id) REFERENCES medications(id)
);

-- Procedure orders
CREATE TABLE procedure_orders (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    order_number VARCHAR(6) NOT NULL,
    procedure_id VARCHAR(20) NOT NULL,
    details CLOB,
    item_number INTEGER NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (order_number) REFERENCES orders(order_number),
    FOREIGN KEY (procedure_id) REFERENCES procedures(id)
);

-- Diagnostic aid orders
CREATE TABLE diagnostic_aid_orders (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    order_number VARCHAR(6) NOT NULL,
    diagnostic_aid_id VARCHAR(20) NOT NULL,
    details CLOB,
    item_number INTEGER NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (order_number) REFERENCES orders(order_number),
    FOREIGN KEY (diagnostic_aid_id) REFERENCES diagnostic_aids(id)
);

-- Medical records table (simplified for H2 - no MongoDB)
CREATE TABLE medical_records (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    patient_id VARCHAR(20) NOT NULL,
    doctor_id VARCHAR(20) NOT NULL,
    consultation_date DATE NOT NULL,
    reason CLOB,
    symptoms CLOB,
    diagnosis CLOB,
    prescriptions CLOB,
    procedures CLOB,
    diagnostic_aids CLOB,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (patient_id) REFERENCES patients(id),
    FOREIGN KEY (doctor_id) REFERENCES users(id)
);

-- Vital signs table
CREATE TABLE vital_signs (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    patient_id VARCHAR(20) NOT NULL,
    nurse_id VARCHAR(20) NOT NULL,
    blood_pressure VARCHAR(20),
    temperature DECIMAL(4,1),
    pulse INTEGER,
    oxygen_level INTEGER,
    observations CLOB,
    recorded_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (patient_id) REFERENCES patients(id),
    FOREIGN KEY (nurse_id) REFERENCES users(id)
);

-- Billing table
CREATE TABLE billing (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    patient_id VARCHAR(20) NOT NULL,
    order_number VARCHAR(6),
    doctor_name VARCHAR(255) NOT NULL,
    total_cost DECIMAL(10,2) NOT NULL,
    copay_amount DECIMAL(10,2) DEFAULT 0.0,
    insurance_covered BOOLEAN DEFAULT FALSE,
    applied_medications CLOB,
    applied_procedures CLOB,
    applied_diagnostic_aids CLOB,
    billing_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    status VARCHAR(20) DEFAULT 'PENDING',
    FOREIGN KEY (patient_id) REFERENCES patients(id),
    FOREIGN KEY (order_number) REFERENCES orders(order_number)
);

-- Indexes for better performance
CREATE INDEX idx_patients_email ON patients(email);
CREATE INDEX idx_users_username ON users(username);
CREATE INDEX idx_users_email ON users(email);
CREATE INDEX idx_appointments_patient_id ON appointments(patient_id);
CREATE INDEX idx_appointments_doctor_id ON appointments(doctor_id);
CREATE INDEX idx_orders_patient_id ON orders(patient_id);
CREATE INDEX idx_orders_doctor_id ON orders(doctor_id);
CREATE INDEX idx_vital_signs_patient_id ON vital_signs(patient_id);
CREATE INDEX idx_billing_patient_id ON billing(patient_id);
CREATE INDEX idx_billing_order_number ON billing(order_number);
CREATE INDEX idx_medical_records_patient_id ON medical_records(patient_id);
CREATE INDEX idx_medical_records_consultation_date ON medical_records(consultation_date);