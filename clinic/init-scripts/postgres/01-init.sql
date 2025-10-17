-- ====================================================================================
-- CLINIC MANAGEMENT SYSTEM - PostgreSQL Database Initialization Script
-- ====================================================================================
-- Description: Complete database schema for clinic management system
-- Version: 2.0.0
-- Last Modified: 2025-10-16
-- ====================================================================================

-- Enable required PostgreSQL extensions
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";
CREATE EXTENSION IF NOT EXISTS "pgcrypto";

-- ====================================================================================
-- SECTION 1: CUSTOM DOMAINS AND DATA TYPES
-- ====================================================================================
-- Business-specific domain constraints for data validation

-- Email domain with improved regex validation
CREATE DOMAIN email_domain AS VARCHAR(255) CHECK (
    VALUE ~* '^[A-Za-z0-9]([A-Za-z0-9._-]*[A-Za-z0-9])?@[A-Za-z0-9]([A-Za-z0-9.-]*[A-Za-z0-9])?\.[A-Za-z]{2,}$'
);

-- Phone domain supporting international formats
CREATE DOMAIN phone_domain AS VARCHAR(20) CHECK (
    VALUE ~* '^\+?[1-9]\d{1,19}$' AND LENGTH(VALUE) >= 7
);

-- Colombian cedula domain with enhanced validation
CREATE DOMAIN cedula_domain AS VARCHAR(20) CHECK (
    LENGTH(VALUE) >= 5 AND VALUE ~* '^[A-Za-z0-9-]+$'
);

-- Currency domain for monetary values
CREATE DOMAIN currency_domain AS DECIMAL(10,2) CHECK (VALUE >= 0);

-- Percentage domain for rates and discounts
CREATE DOMAIN percentage_domain AS DECIMAL(5,2) CHECK (VALUE >= 0 AND VALUE <= 100);

-- ====================================================================================
-- SECTION 2: ENUMERATED TYPES
-- ====================================================================================
-- Define enums for better type safety and performance

CREATE TYPE user_role AS ENUM (
    'ADMIN',
    'HUMAN_RESOURCES',
    'ADMINISTRATIVE_STAFF',
    'SUPPORT_STAFF',
    'DOCTOR',
    'NURSE'
);

CREATE TYPE appointment_status AS ENUM (
    'PROGRAMADA',
    'CONFIRMADA',
    'CANCELADA',
    'COMPLETADA',
    'NO_SHOW'
);

CREATE TYPE order_status AS ENUM (
    'PENDING',
    'IN_PROGRESS',
    'COMPLETED',
    'CANCELLED'
);

CREATE TYPE order_type AS ENUM (
    'MEDICATION',
    'PROCEDURE',
    'DIAGNOSTIC_AID'
);

CREATE TYPE billing_status AS ENUM (
    'PENDING',
    'PAID',
    'OVERDUE',
    'CANCELLED',
    'PARTIALLY_PAID'
);

CREATE TYPE inventory_item_type AS ENUM (
    'MEDICATION',
    'PROCEDURE',
    'DIAGNOSTIC_AID',
    'SUPPLY',
    'EQUIPMENT'
);

-- ====================================================================================
-- SECTION 3: CORE TABLES
-- ====================================================================================

-- Users table with enhanced security and validation
CREATE TABLE IF NOT EXISTS users (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    cedula cedula_domain NOT NULL UNIQUE,
    username VARCHAR(50) NOT NULL UNIQUE CHECK (LENGTH(username) >= 3),
    password_hash VARCHAR(255) NOT NULL CHECK (LENGTH(password_hash) >= 60), -- bcrypt minimum
    full_name VARCHAR(150) NOT NULL CHECK (LENGTH(full_name) >= 2),
    birth_date DATE NOT NULL CHECK (birth_date >= '1900-01-01' AND birth_date <= CURRENT_DATE - INTERVAL '18 years'),
    address VARCHAR(200) NOT NULL CHECK (LENGTH(address) >= 5),
    phone_number phone_domain NOT NULL,
    email email_domain NOT NULL UNIQUE,
    role user_role NOT NULL DEFAULT 'SUPPORT_STAFF',
    is_active BOOLEAN NOT NULL DEFAULT true,
    last_login_at TIMESTAMP,
    login_attempts INTEGER NOT NULL DEFAULT 0,
    locked_until TIMESTAMP,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    -- Ensure at least one admin exists
    CONSTRAINT users_admin_exists CHECK (
        EXISTS (SELECT 1 FROM users WHERE role = 'ADMIN' AND is_active = true) OR
        role != 'ADMIN'
    )
);

-- Patients table with enhanced medical data management
CREATE TABLE IF NOT EXISTS patients (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    cedula cedula_domain NOT NULL UNIQUE,
    username VARCHAR(50) NOT NULL UNIQUE CHECK (LENGTH(username) >= 3),
    password_hash VARCHAR(255) NOT NULL CHECK (LENGTH(password_hash) >= 60),
    full_name VARCHAR(150) NOT NULL CHECK (LENGTH(full_name) >= 2),
    birth_date DATE NOT NULL CHECK (birth_date >= '1900-01-01' AND birth_date <= CURRENT_DATE),
    gender CHAR(1) CHECK (gender IN ('M', 'F', 'O')),
    address VARCHAR(200) NOT NULL CHECK (LENGTH(address) >= 5),
    phone_number phone_domain NOT NULL,
    email email_domain NOT NULL UNIQUE,
    emergency_contact_name VARCHAR(150) CHECK (LENGTH(emergency_contact_name) >= 2),
    emergency_contact_phone phone_domain,
    emergency_contact_relationship VARCHAR(50),
    emergency_contact_email email_domain,

    -- Insurance information
    insurance_policy_number VARCHAR(100),
    insurance_company VARCHAR(150),
    insurance_expiry_date DATE CHECK (insurance_expiry_date > CURRENT_DATE),

    -- Medical information
    blood_type VARCHAR(5) CHECK (blood_type IN ('A+', 'A-', 'B+', 'B-', 'AB+', 'AB-', 'O+', 'O-')),
    allergies TEXT,
    current_medications TEXT,
    medical_notes TEXT,

    -- Account status
    is_active BOOLEAN NOT NULL DEFAULT true,
    email_verified BOOLEAN NOT NULL DEFAULT false,
    phone_verified BOOLEAN NOT NULL DEFAULT false,

    -- Audit fields
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by UUID REFERENCES users(id),
    updated_by UUID REFERENCES users(id)
);

-- Appointments table with enhanced scheduling and tracking
CREATE TABLE IF NOT EXISTS appointments (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    patient_cedula cedula_domain NOT NULL,
    doctor_cedula cedula_domain NOT NULL,
    appointment_datetime TIMESTAMP NOT NULL,
    duration_minutes INTEGER NOT NULL DEFAULT 30 CHECK (duration_minutes > 0 AND duration_minutes <= 480),
    status appointment_status NOT NULL DEFAULT 'PROGRAMADA',
    priority INTEGER NOT NULL DEFAULT 3 CHECK (priority BETWEEN 1 AND 5), -- 1=highest, 5=lowest
    appointment_type VARCHAR(50) NOT NULL DEFAULT 'GENERAL' CHECK (appointment_type IN ('GENERAL', 'FOLLOW_UP', 'EMERGENCY', 'SPECIALIST', 'PROCEDURE')),
    reason TEXT CHECK (LENGTH(reason) <= 1000),
    notes TEXT CHECK (LENGTH(notes) <= 2000),

    -- Scheduling constraints
    scheduled_by UUID REFERENCES users(id),
    confirmed_at TIMESTAMP,
    confirmed_by UUID REFERENCES users(id),
    cancelled_at TIMESTAMP,
    cancelled_by UUID REFERENCES users(id),
    cancellation_reason TEXT,

    -- Follow-up information
    follow_up_required BOOLEAN NOT NULL DEFAULT false,
    follow_up_notes TEXT,

    -- Audit fields
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by UUID REFERENCES users(id),

    -- Foreign key constraints
    FOREIGN KEY (patient_cedula) REFERENCES patients(cedula),
    FOREIGN KEY (doctor_cedula) REFERENCES users(cedula),

    -- Business logic constraints
    CONSTRAINT appointments_valid_datetime CHECK (appointment_datetime > CURRENT_TIMESTAMP),
    CONSTRAINT appointments_patient_active CHECK (
        EXISTS (SELECT 1 FROM patients WHERE cedula = patient_cedula AND is_active = true)
    ),
    CONSTRAINT appointments_doctor_active CHECK (
        EXISTS (SELECT 1 FROM users WHERE cedula = doctor_cedula AND role IN ('DOCTOR', 'NURSE') AND is_active = true)
    )
);

-- Inventory items table with enhanced categorization and stock management
CREATE TABLE IF NOT EXISTS inventory_items (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    item_code VARCHAR(50) NOT NULL UNIQUE CHECK (LENGTH(item_code) >= 3),
    name VARCHAR(200) NOT NULL CHECK (LENGTH(name) >= 2),
    type inventory_item_type NOT NULL,
    category VARCHAR(100) NOT NULL CHECK (LENGTH(category) >= 2),

    -- Cost and pricing
    unit_cost currency_domain NOT NULL CHECK (unit_cost > 0),
    sale_price currency_domain CHECK (sale_price >= unit_cost),
    discount_percentage percentage_domain DEFAULT 0,

    -- Stock management
    current_stock INTEGER NOT NULL DEFAULT 0 CHECK (current_stock >= 0),
    minimum_stock INTEGER NOT NULL DEFAULT 0 CHECK (minimum_stock >= 0),
    maximum_stock INTEGER CHECK (maximum_stock >= minimum_stock),

    -- Item details
    description TEXT CHECK (LENGTH(description) <= 2000),
    manufacturer VARCHAR(150),
    batch_number VARCHAR(100),
    expiry_date DATE CHECK (expiry_date > CURRENT_DATE),

    -- Status and flags
    is_active BOOLEAN NOT NULL DEFAULT true,
    requires_prescription BOOLEAN NOT NULL DEFAULT false,
    is_controlled_substance BOOLEAN NOT NULL DEFAULT false,

    -- Audit fields
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by UUID REFERENCES users(id),
    updated_by UUID REFERENCES users(id),

    -- Business logic constraints
    CONSTRAINT inventory_valid_stock_levels CHECK (
        current_stock <= COALESCE(maximum_stock, current_stock + 1)
    ),
    CONSTRAINT inventory_expiry_warning CHECK (
        expiry_date IS NULL OR expiry_date > CURRENT_DATE + INTERVAL '30 days'
    )
);

-- Medical orders table with comprehensive tracking
CREATE TABLE IF NOT EXISTS orders (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    order_number VARCHAR(20) NOT NULL UNIQUE CHECK (LENGTH(order_number) >= 6),
    patient_cedula cedula_domain NOT NULL,
    doctor_cedula cedula_domain NOT NULL,
    order_type order_type NOT NULL,
    status order_status NOT NULL DEFAULT 'PENDING',
    priority INTEGER NOT NULL DEFAULT 3 CHECK (priority BETWEEN 1 AND 5),

    -- Order details
    diagnosis TEXT CHECK (LENGTH(diagnosis) <= 2000),
    clinical_indication TEXT CHECK (LENGTH(clinical_indication) <= 1000),
    special_instructions TEXT CHECK (LENGTH(special_instructions) <= 1500),

    -- Processing information
    ordered_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    started_at TIMESTAMP,
    completed_at TIMESTAMP,
    cancelled_at TIMESTAMP,
    cancellation_reason TEXT,

    -- Processing staff
    processed_by UUID REFERENCES users(id),
    supervised_by UUID REFERENCES users(id),

    -- Audit fields
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by UUID REFERENCES users(id),

    -- Foreign key constraints
    FOREIGN KEY (patient_cedula) REFERENCES patients(cedula),
    FOREIGN KEY (doctor_cedula) REFERENCES users(cedula),

    -- Business logic constraints
    CONSTRAINT orders_valid_patient CHECK (
        EXISTS (SELECT 1 FROM patients WHERE cedula = patient_cedula AND is_active = true)
    ),
    CONSTRAINT orders_valid_doctor CHECK (
        EXISTS (SELECT 1 FROM users WHERE cedula = doctor_cedula AND role IN ('DOCTOR', 'NURSE') AND is_active = true)
    ),
    CONSTRAINT orders_status_timeline CHECK (
        (status IN ('COMPLETED', 'CANCELLED') AND completed_at IS NOT NULL) OR
        (status = 'CANCELLED' AND cancelled_at IS NOT NULL) OR
        (status IN ('PENDING', 'IN_PROGRESS'))
    )
);

-- Order items table with detailed itemization
CREATE TABLE IF NOT EXISTS order_items (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    order_id UUID NOT NULL,
    item_number SERIAL NOT NULL,
    item_type order_type NOT NULL,
    inventory_item_id UUID REFERENCES inventory_items(id),

    -- Item details
    name VARCHAR(200) NOT NULL CHECK (LENGTH(name) >= 2),
    generic_name VARCHAR(200),
    dosage VARCHAR(100),
    frequency VARCHAR(100),
    duration VARCHAR(100),
    quantity DECIMAL(10,3) NOT NULL DEFAULT 1 CHECK (quantity > 0),

    -- Cost calculation
    unit_cost currency_domain NOT NULL CHECK (unit_cost >= 0),
    total_cost currency_domain GENERATED ALWAYS AS (quantity * unit_cost) STORED,

    -- Status tracking
    status order_status NOT NULL DEFAULT 'PENDING',
    dispensed_at TIMESTAMP,
    dispensed_by UUID REFERENCES users(id),

    -- Additional information
    instructions TEXT CHECK (LENGTH(instructions) <= 1000),
    warnings TEXT CHECK (LENGTH(warnings) <= 500),

    -- Audit fields
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    -- Foreign key constraints
    FOREIGN KEY (order_id) REFERENCES orders(id) ON DELETE CASCADE,

    -- Business logic constraints
    CONSTRAINT order_items_valid_quantity CHECK (
        quantity <= COALESCE((
            SELECT current_stock FROM inventory_items WHERE id = inventory_item_id
        ), quantity)
    ),
    UNIQUE(order_id, item_number)
);

-- Billing table with comprehensive financial tracking
CREATE TABLE IF NOT EXISTS billing (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    patient_cedula cedula_domain NOT NULL,
    invoice_number VARCHAR(50) NOT NULL UNIQUE CHECK (LENGTH(invoice_number) >= 8),
    billing_date DATE NOT NULL DEFAULT CURRENT_DATE,
    due_date DATE NOT NULL DEFAULT (CURRENT_DATE + INTERVAL '30 days'),

    -- Financial amounts
    subtotal_amount currency_domain NOT NULL DEFAULT 0,
    tax_percentage percentage_domain NOT NULL DEFAULT 0,
    tax_amount currency_domain GENERATED ALWAYS AS (subtotal_amount * tax_percentage / 100) STORED,
    discount_amount currency_domain NOT NULL DEFAULT 0,
    total_amount currency_domain GENERATED ALWAYS AS (subtotal_amount + tax_amount - discount_amount) STORED,

    -- Insurance information
    insurance_coverage currency_domain NOT NULL DEFAULT 0,
    copayment_amount currency_domain NOT NULL DEFAULT 0,
    patient_responsibility currency_domain GENERATED ALWAYS AS (total_amount - insurance_coverage) STORED,

    -- Payment tracking
    paid_amount currency_domain NOT NULL DEFAULT 0,
    pending_amount currency_domain GENERATED ALWAYS AS (total_amount - paid_amount) STORED,
    status billing_status NOT NULL DEFAULT 'PENDING',

    -- Payment information
    payment_method VARCHAR(50),
    payment_reference VARCHAR(100),
    paid_at TIMESTAMP,

    -- Audit fields
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by UUID REFERENCES users(id),

    -- Foreign key constraints
    FOREIGN KEY (patient_cedula) REFERENCES patients(cedula),

    -- Business logic constraints
    CONSTRAINT billing_valid_amounts CHECK (
        paid_amount <= total_amount AND
        insurance_coverage <= total_amount AND
        copayment_amount >= 0
    ),
    CONSTRAINT billing_due_date CHECK (due_date >= billing_date)
);

-- Billing items table for detailed breakdown
CREATE TABLE IF NOT EXISTS billing_items (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    billing_id UUID NOT NULL,
    order_id UUID,
    order_item_id UUID,

    -- Item identification
    item_description TEXT NOT NULL CHECK (LENGTH(item_description) >= 10),
    item_code VARCHAR(50),
    quantity DECIMAL(10,3) NOT NULL DEFAULT 1,

    -- Pricing
    unit_price currency_domain NOT NULL,
    total_price currency_domain GENERATED ALWAYS AS (quantity * unit_price) STORED,
    discount_amount currency_domain NOT NULL DEFAULT 0,
    final_amount currency_domain GENERATED ALWAYS AS (total_price - discount_amount) STORED,

    -- Insurance coverage for this item
    insurance_coverage_item currency_domain NOT NULL DEFAULT 0,

    -- Audit fields
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    -- Foreign key constraints
    FOREIGN KEY (billing_id) REFERENCES billing(id) ON DELETE CASCADE,
    FOREIGN KEY (order_id) REFERENCES orders(id),
    FOREIGN KEY (order_item_id) REFERENCES order_items(id),

    -- Business logic constraints
    CONSTRAINT billing_items_valid_amounts CHECK (
        discount_amount <= total_price AND
        insurance_coverage_item <= total_price
    )
);

-- Patient visits table with comprehensive medical records
CREATE TABLE IF NOT EXISTS patient_visits (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    patient_cedula cedula_domain NOT NULL,
    visit_datetime TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    visit_type VARCHAR(50) NOT NULL DEFAULT 'CONSULTATION' CHECK (visit_type IN ('CONSULTATION', 'FOLLOW_UP', 'EMERGENCY', 'PROCEDURE', 'CHECKUP')),

    -- Medical staff
    doctor_cedula cedula_domain NOT NULL,
    nurse_cedula cedula_domain,

    -- Visit details
    chief_complaint TEXT CHECK (LENGTH(chief_complaint) <= 1000),
    history_of_present_illness TEXT CHECK (LENGTH(history_of_present_illness) <= 2000),
    physical_examination TEXT CHECK (LENGTH(physical_examination) <= 2000),
    diagnosis TEXT CHECK (LENGTH(diagnosis) <= 1500),
    treatment_plan TEXT CHECK (LENGTH(treatment_plan) <= 2000),
    recommendations TEXT CHECK (LENGTH(recommendations) <= 1500),

    -- Visit status
    status VARCHAR(20) NOT NULL DEFAULT 'IN_PROGRESS' CHECK (status IN ('IN_PROGRESS', 'COMPLETED', 'CANCELLED')),
    is_completed BOOLEAN NOT NULL DEFAULT false,

    -- Follow-up
    follow_up_required BOOLEAN NOT NULL DEFAULT false,
    follow_up_date DATE,
    follow_up_notes TEXT,

    -- Audit fields
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by UUID REFERENCES users(id),
    completed_by UUID REFERENCES users(id),

    -- Foreign key constraints
    FOREIGN KEY (patient_cedula) REFERENCES patients(cedula),
    FOREIGN KEY (doctor_cedula) REFERENCES users(cedula),
    FOREIGN KEY (nurse_cedula) REFERENCES users(cedula),

    -- Business logic constraints
    CONSTRAINT patient_visits_valid_staff CHECK (
        EXISTS (SELECT 1 FROM users WHERE cedula = doctor_cedula AND role IN ('DOCTOR', 'NURSE') AND is_active = true)
    ),
    CONSTRAINT patient_visits_follow_up_date CHECK (
        follow_up_required = false OR follow_up_date > visit_datetime::date
    )
);

-- Vital signs table with enhanced medical validation
CREATE TABLE IF NOT EXISTS vital_signs (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    patient_visit_id UUID NOT NULL,

    -- Blood pressure
    blood_pressure_systolic SMALLINT CHECK (blood_pressure_systolic BETWEEN 70 AND 250),
    blood_pressure_diastolic SMALLINT CHECK (blood_pressure_diastolic BETWEEN 40 AND 150),

    -- Temperature with multiple units support
    temperature_celsius DECIMAL(4,2) CHECK (temperature_celsius BETWEEN 35.0 AND 45.0),
    temperature_fahrenheit DECIMAL(4,2) GENERATED ALWAYS AS ((temperature_celsius * 9/5) + 32) STORED,

    -- Heart and respiratory
    pulse SMALLINT CHECK (pulse BETWEEN 30 AND 250),
    respiratory_rate SMALLINT CHECK (respiratory_rate BETWEEN 10 AND 40),
    oxygen_saturation SMALLINT CHECK (oxygen_saturation BETWEEN 70 AND 100),

    -- Additional vital signs
    weight_kg DECIMAL(5,2) CHECK (weight_kg > 0 AND weight_kg < 1000),
    height_cm DECIMAL(5,2) CHECK (height_cm > 0 AND height_cm < 300),
    bmi DECIMAL(4,2) GENERATED ALWAYS AS (
        CASE
            WHEN weight_kg IS NOT NULL AND height_cm IS NOT NULL
            THEN weight_kg / ((height_cm / 100) * (height_cm / 100))
            ELSE NULL
        END
    ) STORED,

    -- Pain and consciousness
    pain_level SMALLINT CHECK (pain_level BETWEEN 0 AND 10),
    consciousness_level VARCHAR(20) CHECK (consciousness_level IN ('ALERT', 'CONFUSED', 'SOMNOLENT', 'STUPOR', 'COMA')),

    -- Recording information
    recorded_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    recorded_by UUID NOT NULL REFERENCES users(id),
    notes TEXT CHECK (LENGTH(notes) <= 500),

    -- Foreign key constraints
    FOREIGN KEY (patient_visit_id) REFERENCES patient_visits(id) ON DELETE CASCADE,

    -- Business logic constraints
    CONSTRAINT vital_signs_recorded_by_nurse CHECK (
        EXISTS (SELECT 1 FROM users WHERE id = recorded_by AND role IN ('NURSE', 'DOCTOR') AND is_active = true)
    ),
    CONSTRAINT vital_signs_bmi_calculation CHECK (
        (bmi IS NULL) OR (bmi >= 10 AND bmi <= 50)
    )
);

-- ====================================================================================
-- SECTION 4: PERFORMANCE OPTIMIZATION INDEXES
-- ====================================================================================
-- Strategic indexes for optimal query performance

-- Users table indexes
CREATE INDEX IF NOT EXISTS idx_users_cedula ON users(cedula);
CREATE INDEX IF NOT EXISTS idx_users_username ON users(username);
CREATE INDEX IF NOT EXISTS idx_users_email ON users(email);
CREATE INDEX IF NOT EXISTS idx_users_role ON users(role);
CREATE INDEX IF NOT EXISTS idx_users_active_role ON users(role, is_active) WHERE is_active = true;
CREATE INDEX IF NOT EXISTS idx_users_login ON users(last_login_at DESC) WHERE last_login_at IS NOT NULL;

-- Patients table indexes
CREATE INDEX IF NOT EXISTS idx_patients_cedula ON patients(cedula);
CREATE INDEX IF NOT EXISTS idx_patients_username ON patients(username);
CREATE INDEX IF NOT EXISTS idx_patients_email ON patients(email);
CREATE INDEX IF NOT EXISTS idx_patients_active ON patients(is_active) WHERE is_active = true;
CREATE INDEX IF NOT EXISTS idx_patients_insurance ON patients(insurance_company) WHERE insurance_company IS NOT NULL;
CREATE INDEX IF NOT EXISTS idx_patients_blood_type ON patients(blood_type) WHERE blood_type IS NOT NULL;

-- Appointments table indexes
CREATE INDEX IF NOT EXISTS idx_appointments_patient ON appointments(patient_cedula);
CREATE INDEX IF NOT EXISTS idx_appointments_doctor ON appointments(doctor_cedula);
CREATE INDEX IF NOT EXISTS idx_appointments_datetime ON appointments(appointment_datetime);
CREATE INDEX IF NOT EXISTS idx_appointments_status ON appointments(status);
CREATE INDEX IF NOT EXISTS idx_appointments_doctor_datetime ON appointments(doctor_cedula, appointment_datetime);
CREATE INDEX IF NOT EXISTS idx_appointments_patient_datetime ON appointments(patient_cedula, appointment_datetime);
CREATE INDEX IF NOT EXISTS idx_appointments_upcoming ON appointments(appointment_datetime, status) WHERE appointment_datetime > CURRENT_TIMESTAMP;
CREATE INDEX IF NOT EXISTS idx_appointments_confirmed ON appointments(confirmed_at DESC) WHERE status = 'CONFIRMADA';

-- Inventory items indexes
CREATE INDEX IF NOT EXISTS idx_inventory_code ON inventory_items(item_code);
CREATE INDEX IF NOT EXISTS idx_inventory_name ON inventory_items(name);
CREATE INDEX IF NOT EXISTS idx_inventory_type ON inventory_items(type);
CREATE INDEX IF NOT EXISTS idx_inventory_category ON inventory_items(category);
CREATE INDEX IF NOT EXISTS idx_inventory_active ON inventory_items(is_active) WHERE is_active = true;
CREATE INDEX IF NOT EXISTS idx_inventory_stock ON inventory_items(current_stock) WHERE current_stock <= minimum_stock;
CREATE INDEX IF NOT EXISTS idx_inventory_expiry ON inventory_items(expiry_date) WHERE expiry_date IS NOT NULL;

-- Orders table indexes
CREATE INDEX IF NOT EXISTS idx_orders_number ON orders(order_number);
CREATE INDEX IF NOT EXISTS idx_orders_patient ON orders(patient_cedula);
CREATE INDEX IF NOT EXISTS idx_orders_doctor ON orders(doctor_cedula);
CREATE INDEX IF NOT EXISTS idx_orders_type ON orders(order_type);
CREATE INDEX IF NOT EXISTS idx_orders_status ON orders(status);
CREATE INDEX IF NOT EXISTS idx_orders_priority ON orders(priority, created_at DESC);
CREATE INDEX IF NOT EXISTS idx_orders_pending ON orders(created_at) WHERE status IN ('PENDING', 'IN_PROGRESS');
CREATE INDEX IF NOT EXISTS idx_orders_patient_status ON orders(patient_cedula, status);

-- Order items indexes
CREATE INDEX IF NOT EXISTS idx_order_items_order ON order_items(order_id);
CREATE INDEX IF NOT EXISTS idx_order_items_inventory ON order_items(inventory_item_id) WHERE inventory_item_id IS NOT NULL;
CREATE INDEX IF NOT EXISTS idx_order_items_status ON order_items(status);
CREATE INDEX IF NOT EXISTS idx_order_items_dispensed ON order_items(dispensed_at DESC) WHERE status = 'COMPLETED';

-- Billing table indexes
CREATE INDEX IF NOT EXISTS idx_billing_patient ON billing(patient_cedula);
CREATE INDEX IF NOT EXISTS idx_billing_invoice ON billing(invoice_number);
CREATE INDEX IF NOT EXISTS idx_billing_date ON billing(billing_date);
CREATE INDEX IF NOT EXISTS idx_billing_due ON billing(due_date);
CREATE INDEX IF NOT EXISTS idx_billing_status ON billing(status);
CREATE INDEX IF NOT EXISTS idx_billing_pending ON billing(due_date) WHERE status IN ('PENDING', 'OVERDUE');
CREATE INDEX IF NOT EXISTS idx_billing_amount ON billing(total_amount DESC) WHERE status = 'PAID';

-- Billing items indexes
CREATE INDEX IF NOT EXISTS idx_billing_items_billing ON billing_items(billing_id);
CREATE INDEX IF NOT EXISTS idx_billing_items_order ON billing_items(order_id) WHERE order_id IS NOT NULL;
CREATE INDEX IF NOT EXISTS idx_billing_items_code ON billing_items(item_code) WHERE item_code IS NOT NULL;

-- Patient visits indexes
CREATE INDEX IF NOT EXISTS idx_patient_visits_patient ON patient_visits(patient_cedula);
CREATE INDEX IF NOT EXISTS idx_patient_visits_doctor ON patient_visits(doctor_cedula);
CREATE INDEX IF NOT EXISTS idx_patient_visits_datetime ON patient_visits(visit_datetime DESC);
CREATE INDEX IF NOT EXISTS idx_patient_visits_type ON patient_visits(visit_type);
CREATE INDEX IF NOT EXISTS idx_patient_visits_status ON patient_visits(status);
CREATE INDEX IF NOT EXISTS idx_patient_visits_completed ON patient_visits(completed_by) WHERE is_completed = true;
CREATE INDEX IF NOT EXISTS idx_patient_visits_follow_up ON patient_visits(follow_up_date) WHERE follow_up_required = true;

-- Vital signs indexes
CREATE INDEX IF NOT EXISTS idx_vital_signs_visit ON vital_signs(patient_visit_id);
CREATE INDEX IF NOT EXISTS idx_vital_signs_recorded ON vital_signs(recorded_at DESC);
CREATE INDEX IF NOT EXISTS idx_vital_signs_recorded_by ON vital_signs(recorded_by);
CREATE INDEX IF NOT EXISTS idx_vital_signs_bmi ON vital_signs(bmi) WHERE bmi IS NOT NULL;
CREATE INDEX IF NOT EXISTS idx_vital_signs_abnormal ON vital_signs(recorded_at DESC) WHERE
    (blood_pressure_systolic > 140 OR blood_pressure_diastolic > 90 OR
     temperature_celsius > 37.5 OR pulse > 100 OR oxygen_saturation < 95);

-- ====================================================================================
-- SECTION 5: UTILITY FUNCTIONS AND VIEWS
-- ====================================================================================
-- Helper functions for common operations

-- Function to calculate patient age
CREATE OR REPLACE FUNCTION calculate_age(birth_date DATE)
RETURNS INTEGER AS $$
BEGIN
    RETURN EXTRACT(YEAR FROM AGE(CURRENT_DATE, birth_date));
END;
$$ LANGUAGE plpgsql IMMUTABLE;

-- Function to check if patient is of legal age
CREATE OR REPLACE FUNCTION is_patient_of_legal_age(birth_date DATE)
RETURNS BOOLEAN AS $$
BEGIN
    RETURN calculate_age(birth_date) >= 18;
END;
$$ LANGUAGE plpgsql IMMUTABLE;

-- Function to get next appointment for a patient
CREATE OR REPLACE FUNCTION get_next_appointment(patient_cedula_param cedula_domain)
RETURNS TIMESTAMP AS $$
DECLARE
    next_appointment TIMESTAMP;
BEGIN
    SELECT appointment_datetime INTO next_appointment
    FROM appointments
    WHERE patient_cedula = patient_cedula_param
      AND status IN ('PROGRAMADA', 'CONFIRMADA')
      AND appointment_datetime > CURRENT_TIMESTAMP
    ORDER BY appointment_datetime
    LIMIT 1;

    RETURN next_appointment;
END;
$$ LANGUAGE plpgsql STABLE;

-- Function to calculate billing totals for a patient
CREATE OR REPLACE FUNCTION get_patient_billing_summary(
    patient_cedula_param cedula_domain,
    start_date DATE DEFAULT NULL,
    end_date DATE DEFAULT NULL
)
RETURNS TABLE (
    total_billed currency_domain,
    total_paid currency_domain,
    total_pending currency_domain,
    insurance_coverage currency_domain
) AS $$
BEGIN
    RETURN QUERY
    SELECT
        COALESCE(SUM(b.total_amount), 0) as total_billed,
        COALESCE(SUM(b.paid_amount), 0) as total_paid,
        COALESCE(SUM(b.pending_amount), 0) as total_pending,
        COALESCE(SUM(b.insurance_coverage), 0) as insurance_coverage
    FROM billing b
    WHERE b.patient_cedula = patient_cedula_param
      AND (start_date IS NULL OR b.billing_date >= start_date)
      AND (end_date IS NULL OR b.billing_date <= end_date);
END;
$$ LANGUAGE plpgsql STABLE;

-- Function to check inventory stock levels
CREATE OR REPLACE FUNCTION check_inventory_alerts()
RETURNS TABLE (
    item_id UUID,
    item_name VARCHAR(200),
    current_stock INTEGER,
    minimum_stock INTEGER,
    alert_level VARCHAR(20)
) AS $$
BEGIN
    RETURN QUERY
    SELECT
        ii.id,
        ii.name,
        ii.current_stock,
        ii.minimum_stock,
        CASE
            WHEN ii.current_stock = 0 THEN 'OUT_OF_STOCK'
            WHEN ii.current_stock <= ii.minimum_stock * 0.5 THEN 'CRITICAL'
            WHEN ii.current_stock <= ii.minimum_stock THEN 'LOW'
            ELSE 'NORMAL'
        END as alert_level
    FROM inventory_items ii
    WHERE ii.is_active = true
      AND (ii.current_stock <= ii.minimum_stock OR ii.expiry_date <= CURRENT_DATE + INTERVAL '30 days');
END;
$$ LANGUAGE plpgsql STABLE;

-- View for active appointments today
CREATE OR REPLACE VIEW active_appointments_today AS
SELECT
    a.id,
    a.patient_cedula,
    p.full_name as patient_name,
    a.doctor_cedula,
    u.full_name as doctor_name,
    a.appointment_datetime,
    a.status,
    a.appointment_type,
    a.reason
FROM appointments a
JOIN patients p ON a.patient_cedula = p.cedula
JOIN users u ON a.doctor_cedula = u.cedula
WHERE DATE(a.appointment_datetime) = CURRENT_DATE
  AND a.status IN ('PROGRAMADA', 'CONFIRMADA')
ORDER BY a.appointment_datetime;

-- View for pending orders requiring attention
CREATE OR REPLACE VIEW pending_orders_summary AS
SELECT
    o.id,
    o.order_number,
    o.patient_cedula,
    p.full_name as patient_name,
    o.doctor_cedula,
    u.full_name as doctor_name,
    o.order_type,
    o.status,
    o.priority,
    o.created_at,
    COUNT(oi.id) as item_count,
    SUM(oi.total_cost) as total_cost
FROM orders o
JOIN patients p ON o.patient_cedula = p.cedula
JOIN users u ON o.doctor_cedula = u.cedula
LEFT JOIN order_items oi ON o.id = oi.order_id
WHERE o.status IN ('PENDING', 'IN_PROGRESS')
GROUP BY o.id, o.order_number, o.patient_cedula, p.full_name, o.doctor_cedula, u.full_name,
         o.order_type, o.status, o.priority, o.created_at
ORDER BY o.priority, o.created_at;

-- View for overdue payments
CREATE OR REPLACE VIEW overdue_payments AS
SELECT
    b.id,
    b.patient_cedula,
    p.full_name as patient_name,
    b.invoice_number,
    b.total_amount,
    b.paid_amount,
    b.pending_amount,
    b.due_date,
    (CURRENT_DATE - b.due_date) as days_overdue,
    b.insurance_company
FROM billing b
JOIN patients p ON b.patient_cedula = p.cedula
WHERE b.status IN ('PENDING', 'OVERDUE')
  AND b.due_date < CURRENT_DATE
ORDER BY days_overdue DESC, b.pending_amount DESC;

-- ====================================================================================
-- SECTION 6: SECURITY AND ACCESS CONTROL
-- ====================================================================================
-- Row Level Security (RLS) policies for data protection

-- Enable RLS on sensitive tables
ALTER TABLE patients ENABLE ROW LEVEL SECURITY;
ALTER TABLE appointments ENABLE ROW LEVEL SECURITY;
ALTER TABLE orders ENABLE ROW LEVEL SECURITY;
ALTER TABLE order_items ENABLE ROW LEVEL SECURITY;
ALTER TABLE billing ENABLE ROW LEVEL SECURITY;
ALTER TABLE billing_items ENABLE ROW LEVEL SECURITY;
ALTER TABLE patient_visits ENABLE ROW LEVEL SECURITY;
ALTER TABLE vital_signs ENABLE ROW LEVEL SECURITY;

-- Policy for patients: Users can only see patients they're assigned to or their own data
CREATE POLICY patients_access_policy ON patients
    FOR ALL USING (
        -- Admin and HR can see all patients
        EXISTS (SELECT 1 FROM users WHERE id = auth.uid() AND role IN ('ADMIN', 'HUMAN_RESOURCES') AND is_active = true) OR
        -- Doctors and nurses can see patients assigned to them
        EXISTS (SELECT 1 FROM appointments a WHERE a.patient_cedula = patients.cedula AND a.doctor_cedula = auth.uid() AND a.appointment_datetime >= CURRENT_DATE - INTERVAL '30 days') OR
        -- Patients can see their own data
        cedula = (SELECT cedula FROM users WHERE id = auth.uid())
    );

-- Policy for appointments: Users can only access relevant appointments
CREATE POLICY appointments_access_policy ON appointments
    FOR ALL USING (
        -- Admin and HR can see all appointments
        EXISTS (SELECT 1 FROM users WHERE id = auth.uid() AND role IN ('ADMIN', 'HUMAN_RESOURCES') AND is_active = true) OR
        -- Doctors can see their own appointments
        doctor_cedula = (SELECT cedula FROM users WHERE id = auth.uid()) OR
        -- Patients can see their own appointments
        patient_cedula = (SELECT cedula FROM users WHERE id = auth.uid())
    );

-- Policy for medical orders
CREATE POLICY orders_access_policy ON orders
    FOR ALL USING (
        -- Admin and HR can see all orders
        EXISTS (SELECT 1 FROM users WHERE id = auth.uid() AND role IN ('ADMIN', 'HUMAN_RESOURCES') AND is_active = true) OR
        -- Doctors can see orders they created
        doctor_cedula = (SELECT cedula FROM users WHERE id = auth.uid()) OR
        -- Patients can see their own orders
        patient_cedula = (SELECT cedula FROM users WHERE id = auth.uid())
    );

-- Policy for billing information
CREATE POLICY billing_access_policy ON billing
    FOR ALL USING (
        -- Admin and administrative staff can see all billing
        EXISTS (SELECT 1 FROM users WHERE id = auth.uid() AND role IN ('ADMIN', 'ADMINISTRATIVE_STAFF') AND is_active = true) OR
        -- Patients can see their own billing
        patient_cedula = (SELECT cedula FROM users WHERE id = auth.uid())
    );

-- Policy for patient visits and vital signs
CREATE POLICY patient_visits_access_policy ON patient_visits
    FOR ALL USING (
        -- Admin and HR can see all visits
        EXISTS (SELECT 1 FROM users WHERE id = auth.uid() AND role IN ('ADMIN', 'HUMAN_RESOURCES') AND is_active = true) OR
        -- Medical staff can see visits they're involved in
        doctor_cedula = (SELECT cedula FROM users WHERE id = auth.uid()) OR
        nurse_cedula = (SELECT cedula FROM users WHERE id = auth.uid()) OR
        -- Patients can see their own visits
        patient_cedula = (SELECT cedula FROM users WHERE id = auth.uid())
    );

-- ====================================================================================
-- SECTION 7: ADVANCED TRIGGERS AND FUNCTIONS
-- ====================================================================================
-- Enhanced trigger functions for data integrity and automation

-- Improved timestamp update function with user tracking
CREATE OR REPLACE FUNCTION update_updated_at_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;

    -- Track who made the update if user context is available
    IF auth.uid() IS NOT NULL THEN
        NEW.updated_by = auth.uid();
    END IF;

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Function to validate appointment scheduling
CREATE OR REPLACE FUNCTION validate_appointment_scheduling()
RETURNS TRIGGER AS $$
DECLARE
    conflicting_appointment TIMESTAMP;
    doctor_workload INTEGER;
BEGIN
    -- Check for conflicting appointments
    SELECT appointment_datetime INTO conflicting_appointment
    FROM appointments
    WHERE doctor_cedula = NEW.doctor_cedula
      AND appointment_datetime = NEW.appointment_datetime
      AND status IN ('PROGRAMADA', 'CONFIRMADA')
      AND id != COALESCE(NEW.id, gen_random_uuid());

    IF conflicting_appointment IS NOT NULL THEN
        RAISE EXCEPTION 'Doctor already has an appointment scheduled at %', conflicting_appointment;
    END IF;

    -- Check doctor's daily workload (max 8 appointments per day)
    SELECT COUNT(*) INTO doctor_workload
    FROM appointments
    WHERE doctor_cedula = NEW.doctor_cedula
      AND DATE(appointment_datetime) = DATE(NEW.appointment_datetime)
      AND status IN ('PROGRAMADA', 'CONFIRMADA');

    IF doctor_workload >= 8 THEN
        RAISE EXCEPTION 'Doctor has reached maximum daily appointment limit';
    END IF;

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Function to update inventory stock when order items are dispensed
CREATE OR REPLACE FUNCTION update_inventory_stock()
RETURNS TRIGGER AS $$
BEGIN
    -- Only update stock when item is marked as completed
    IF NEW.status = 'COMPLETED' AND (OLD.status != 'COMPLETED' OR OLD.status IS NULL) THEN
        -- Reduce inventory stock
        UPDATE inventory_items
        SET current_stock = current_stock - NEW.quantity,
            updated_at = CURRENT_TIMESTAMP
        WHERE id = NEW.inventory_item_id;

        -- Check if stock went below minimum
        IF EXISTS (
            SELECT 1 FROM inventory_items
            WHERE id = NEW.inventory_item_id
              AND current_stock <= minimum_stock
        ) THEN
            -- Log low stock alert (you could also send notifications here)
            RAISE NOTICE 'Inventory item % is running low on stock', NEW.name;
        END IF;
    END IF;

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Function to calculate billing totals automatically
CREATE OR REPLACE FUNCTION calculate_billing_totals()
RETURNS TRIGGER AS $$
DECLARE
    order_total currency_domain;
BEGIN
    -- Calculate subtotal from order items
    SELECT COALESCE(SUM(final_amount), 0) INTO order_total
    FROM billing_items
    WHERE billing_id = NEW.billing_id;

    -- Update billing totals
    UPDATE billing
    SET subtotal_amount = order_total,
        updated_at = CURRENT_TIMESTAMP
    WHERE id = NEW.billing_id;

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Function to check for overdue payments
CREATE OR REPLACE FUNCTION check_overdue_billing()
RETURNS TRIGGER AS $$
BEGIN
    -- Mark as overdue if past due date and still pending
    IF NEW.due_date < CURRENT_DATE AND NEW.status IN ('PENDING', 'PARTIALLY_PAID') THEN
        NEW.status = 'OVERDUE';
    END IF;

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Create triggers for all tables
CREATE TRIGGER update_users_updated_at BEFORE UPDATE ON users FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();
CREATE TRIGGER update_patients_updated_at BEFORE UPDATE ON patients FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();
CREATE TRIGGER update_appointments_updated_at BEFORE UPDATE ON appointments FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();
CREATE TRIGGER update_orders_updated_at BEFORE UPDATE ON orders FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();
CREATE TRIGGER update_billing_updated_at BEFORE UPDATE ON billing FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();
CREATE TRIGGER update_patient_visits_updated_at BEFORE UPDATE ON patient_visits FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();
CREATE TRIGGER update_inventory_items_updated_at BEFORE UPDATE ON inventory_items FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

-- Business logic triggers
CREATE TRIGGER validate_appointment_scheduling_trigger
    BEFORE INSERT OR UPDATE ON appointments
    FOR EACH ROW EXECUTE FUNCTION validate_appointment_scheduling();

CREATE TRIGGER update_inventory_on_dispense
    AFTER UPDATE ON order_items
    FOR EACH ROW EXECUTE FUNCTION update_inventory_stock();

CREATE TRIGGER calculate_billing_totals_trigger
    AFTER INSERT OR UPDATE OR DELETE ON billing_items
    FOR EACH ROW EXECUTE FUNCTION calculate_billing_totals();

CREATE TRIGGER check_billing_overdue_trigger
    BEFORE UPDATE ON billing
    FOR EACH ROW EXECUTE FUNCTION check_overdue_billing();

-- ====================================================================================
-- SECTION 8: DATA INITIALIZATION AND SAMPLE DATA
-- ====================================================================================
-- Production-ready sample data with proper relationships

-- Function to initialize sample data safely
CREATE OR REPLACE FUNCTION initialize_sample_data()
RETURNS TEXT AS $$
DECLARE
    admin_count INTEGER;
    sample_data_version INTEGER := 1;
BEGIN
    -- Check if sample data already exists
    SELECT COUNT(*) INTO admin_count FROM users WHERE role = 'ADMIN';

    -- Only insert if no admin exists (fresh database)
    IF admin_count = 0 THEN
        RAISE NOTICE 'Initializing sample data for clinic management system...';

        -- Insert system administrator
        INSERT INTO users (cedula, username, password_hash, full_name, birth_date, address, phone_number, email, role, is_active)
        VALUES (
            'ADMIN-001',
            'admin',
            '$2a$10$N9qo8uLOickgx2ZMRZoMye1jEftwEfXQqK.qjK7qZ0PqKJL.0jOy', -- 'admin123' hashed with bcrypt
            'System Administrator',
            '1980-01-01',
            'Centro Administrativo, Oficina 101',
            '+573001234567',
            'admin@clinic.com',
            'ADMIN',
            true
        );

        -- Insert HR manager
        INSERT INTO users (cedula, username, password_hash, full_name, birth_date, address, phone_number, email, role, is_active)
        VALUES (
            'HR-001',
            'rrhh',
            '$2a$10$N9qo8uLOickgx2ZMRZoMye1jEftwEfXQqK.qjK7qZ0PqKJL.0jOy', -- 'rrhh123' hashed
            'Directora de Recursos Humanos',
            '1985-03-15',
            'Carrera 15 #127-45, Oficina 502',
            '+573151112223',
            'rrhh@clinic.com',
            'HUMAN_RESOURCES',
            true
        );

        -- Insert medical staff
        INSERT INTO users (cedula, username, password_hash, full_name, birth_date, address, phone_number, email, role, is_active)
        VALUES
        (
            'DOC-001', 'drgarcia', '$2a$10$N9qo8uLOickgx2ZMRZoMye1jEftwEfXQqK.qjK7qZ0PqKJL.0jOy',
            'Dr. Carlos García López', '1978-12-25', 'Calle 56 #78-90, Apto 12B', '+573113344556',
            'carlos.garcia@clinic.com', 'DOCTOR', true
        ),
        (
            'NURSE-001', 'enfmorales', '$2a$10$N9qo8uLOickgx2ZMRZoMye1jEftwEfXQqK.qjK7qZ0PqKJL.0jOy',
            'Enf. María Morales Sánchez', '1988-04-08', 'Carrera 23 #45-67, Apto 8A', '+573127788990',
            'maria.morales@clinic.com', 'NURSE', true
        ),
        (
            'ADMIN-002', 'admin2', '$2a$10$N9qo8uLOickgx2ZMRZoMye1jEftwEfXQqK.qjK7qZ0PqKJL.0jOy',
            'Sofía Herrera Martínez', '1983-09-30', 'Avenida 12 #34-56, Oficina 201', '+573132233445',
            'sofia.herrera@clinic.com', 'ADMINISTRATIVE_STAFF', true
        );

        -- Insert sample patients
        INSERT INTO patients (cedula, username, password_hash, full_name, birth_date, gender, address, phone_number, email,
                            emergency_contact_name, emergency_contact_phone, emergency_contact_relationship,
                            blood_type, allergies, insurance_policy_number, insurance_company)
        VALUES
        (
            'CC-12345678', 'patient1', '$2a$10$N9qo8uLOickgx2ZMRZoMye1jEftwEfXQqK.qjK7qZ0PqKJL.0jOy',
            'Juan Pérez García', '1985-05-15', 'M', 'Calle 123 #45-67, Apto 89',
            '+573001234567', 'juan.perez@email.com',
            'María García de Pérez', '+573001111111', 'Esposa',
            'O+', 'Ninguna conocida', 'EPS-001', 'Sura EPS'
        ),
        (
            'CC-87654321', 'patient2', '$2a$10$N9qo8uLOickgx2ZMRZoMye1jEftwEfXQqK.qjK7qZ0PqKJL.0jOy',
            'María Fernanda Castro', '1980-05-15', 'F', 'Calle 100 #20-30, Torre 5 Apto 1201',
            '+573001000001', 'maria.castro@email.com',
            'Carlos Castro', '+573101000001', 'Hermano',
            'A+', 'Penicilina', 'POL001234567', 'Seguros Bolívar'
        ),
        (
            'CC-11223344', 'patient3', '$2a$10$N9qo8uLOickgx2ZMRZoMye1jEftwEfXQqK.qjK7qZ0PqKJL.0jOy',
            'Roberto Jiménez', '1975-08-22', 'M', 'Carrera 50 #75-20, Casa 34',
            '+573102000002', 'roberto.jimenez@email.com',
            'Carmen Jiménez', '+573202000002', 'Esposa',
            'B+', 'Mariscos', 'POL002345678', 'Sura EPS'
        );

        -- Insert inventory items with proper categorization
        INSERT INTO inventory_items (item_code, name, type, category, unit_cost, sale_price, current_stock, minimum_stock,
                                   description, manufacturer, requires_prescription)
        VALUES
        (
            'MED-PAR-500', 'Paracetamol 500mg', 'MEDICATION', 'Analgésicos', 2500.00, 2800.00, 100, 20,
            'Analgésico y antipirético de acción rápida', 'Laboratorios PharmaGen', false
        ),
        (
            'MED-IBU-400', 'Ibuprofeno 400mg', 'MEDICATION', 'Antiinflamatorios', 3000.00, 3500.00, 80, 15,
            'Antiinflamatorio no esteroideo de amplio espectro', 'Medicamentos del Valle', false
        ),
        (
            'MED-AMO-500', 'Amoxicilina 500mg', 'MEDICATION', 'Antibióticos', 4500.00, 5200.00, 50, 10,
            'Antibiótico de amplio espectro para infecciones bacterianas', 'BioFarma Ltda.', true
        ),
        (
            'PROC-HEM-COMP', 'Hemograma completo', 'PROCEDURE', 'Análisis Clínicos', 45000.00, 55000.00, 0, 0,
            'Análisis completo de sangre con conteo celular', 'Laboratorio Clínico Central', false
        ),
        (
            'PROC-RX-TORAX', 'Radiografía de tórax', 'PROCEDURE', 'Imagenología', 80000.00, 95000.00, 0, 0,
            'Imagen diagnóstica de tórax PA y lateral', 'Centro Radiológico del Norte', false
        ),
        (
            'PROC-RM-ABD', 'Resonancia magnética abdominal', 'PROCEDURE', 'Imagenología Avanzada', 350000.00, 420000.00, 0, 0,
            'Imagen avanzada de diagnóstico por resonancia magnética', 'Centro de Diagnóstico Avanzado', false
        );

        -- Insert sample appointments
        INSERT INTO appointments (patient_cedula, doctor_cedula, appointment_datetime, duration_minutes, appointment_type,
                               reason, scheduled_by)
        VALUES
        (
            'CC-12345678', 'DOC-001', CURRENT_TIMESTAMP + INTERVAL '2 days', 30, 'GENERAL',
            'Consulta de seguimiento por hipertensión', (SELECT id FROM users WHERE username = 'admin')
        ),
        (
            'CC-87654321', 'DOC-001', CURRENT_TIMESTAMP + INTERVAL '3 days', 45, 'SPECIALIST',
            'Evaluación de dolor abdominal crónico', (SELECT id FROM users WHERE username = 'admin')
        );

        -- Insert sample orders
        INSERT INTO orders (order_number, patient_cedula, doctor_cedula, order_type, priority, diagnosis, created_by)
        VALUES
        (
            'ORD-2025001', 'CC-12345678', 'DOC-001', 'MEDICATION', 3,
            'Hipertensión arterial controlada', (SELECT id FROM users WHERE cedula = 'DOC-001')
        );

        -- Insert order items
        INSERT INTO order_items (order_id, item_type, inventory_item_id, name, quantity, unit_cost, instructions)
        SELECT
            o.id, 'MEDICATION', ii.id, ii.name, 1, ii.unit_cost,
            'Tomar una tableta cada 8 horas por 7 días'
        FROM orders o
        JOIN inventory_items ii ON ii.item_code = 'MED-PAR-500'
        WHERE o.order_number = 'ORD-2025001';

        RAISE NOTICE 'Sample data initialized successfully. Version: %', sample_data_version;
        RETURN 'Sample data initialized successfully';
    ELSE
        RAISE NOTICE 'Sample data already exists. Skipping initialization.';
        RETURN 'Sample data already exists';
    END IF;
END;
$$ LANGUAGE plpgsql;

-- Execute sample data initialization
SELECT initialize_sample_data();

-- ====================================================================================
-- SECTION 9: DATA INTEGRITY VERIFICATION
-- ====================================================================================
-- Validation queries to ensure data consistency

-- Function to verify referential integrity
CREATE OR REPLACE FUNCTION verify_data_integrity()
RETURNS TABLE (
    check_name VARCHAR(100),
    status VARCHAR(20),
    details TEXT
) AS $$
BEGIN
    -- Check 1: All appointments reference valid patients and doctors
    RETURN QUERY
    SELECT
        'Appointments Reference Check'::VARCHAR(100) as check_name,
        CASE
            WHEN COUNT(*) = 0 THEN 'PASS'::VARCHAR(20)
            ELSE 'FAIL'::VARCHAR(20)
        END as status,
        'Found ' || COUNT(*)::TEXT || ' appointments with invalid references' as details
    FROM appointments a
    WHERE NOT EXISTS (SELECT 1 FROM patients p WHERE p.cedula = a.patient_cedula AND p.is_active = true)
       OR NOT EXISTS (SELECT 1 FROM users u WHERE u.cedula = a.doctor_cedula AND u.role IN ('DOCTOR', 'NURSE') AND u.is_active = true);

    -- Check 2: All orders reference valid patients and doctors
    RETURN QUERY
    SELECT
        'Orders Reference Check'::VARCHAR(100) as check_name,
        CASE
            WHEN COUNT(*) = 0 THEN 'PASS'::VARCHAR(20)
            ELSE 'FAIL'::VARCHAR(20)
        END as status,
        'Found ' || COUNT(*)::TEXT || ' orders with invalid references' as details
    FROM orders o
    WHERE NOT EXISTS (SELECT 1 FROM patients p WHERE p.cedula = o.patient_cedula AND p.is_active = true)
       OR NOT EXISTS (SELECT 1 FROM users u WHERE u.cedula = o.doctor_cedula AND u.role IN ('DOCTOR', 'NURSE') AND u.is_active = true);

    -- Check 3: Inventory stock levels are valid
    RETURN QUERY
    SELECT
        'Inventory Stock Check'::VARCHAR(100) as check_name,
        CASE
            WHEN COUNT(*) = 0 THEN 'PASS'::VARCHAR(20)
            ELSE 'FAIL'::VARCHAR(20)
        END as status,
        'Found ' || COUNT(*)::TEXT || ' items with invalid stock levels' as details
    FROM inventory_items
    WHERE current_stock < 0 OR current_stock > COALESCE(maximum_stock, current_stock);

    -- Check 4: Billing totals match billing items
    RETURN QUERY
    SELECT
        'Billing Totals Check'::VARCHAR(100) as check_name,
        CASE
            WHEN COUNT(*) = 0 THEN 'PASS'::VARCHAR(20)
            ELSE 'FAIL'::VARCHAR(20)
        END as status,
        'Found ' || COUNT(*)::TEXT || ' billing records with mismatched totals' as details
    FROM billing b
    WHERE ABS(b.total_amount - COALESCE((
        SELECT SUM(bi.final_amount)
        FROM billing_items bi
        WHERE bi.billing_id = b.id
    ), 0)) > 0.01;

    -- Check 5: At least one active admin exists
    RETURN QUERY
    SELECT
        'Admin User Check'::VARCHAR(100) as check_name,
        CASE
            WHEN COUNT(*) > 0 THEN 'PASS'::VARCHAR(20)
            ELSE 'FAIL'::VARCHAR(20)
        END as status,
        CASE
            WHEN COUNT(*) > 0 THEN COUNT(*)::TEXT || ' active admin users found'
            ELSE 'No active admin users found'
        END as details
    FROM users
    WHERE role = 'ADMIN' AND is_active = true;

END;
$$ LANGUAGE plpgsql;

-- Run integrity verification
SELECT * FROM verify_data_integrity();

-- ====================================================================================
-- SECTION 10: PERFORMANCE MONITORING SETUP
-- ====================================================================================
-- Create a view for monitoring system performance

CREATE OR REPLACE VIEW system_performance_metrics AS
SELECT
    'active_users' as metric_name,
    COUNT(*)::TEXT as metric_value,
    'Users currently active in the system' as description
FROM users WHERE is_active = true

UNION ALL

SELECT
    'active_patients' as metric_name,
    COUNT(*)::TEXT as metric_value,
    'Patients registered and active' as description
FROM patients WHERE is_active = true

UNION ALL

SELECT
    'today_appointments' as metric_name,
    COUNT(*)::TEXT as metric_value,
    'Appointments scheduled for today' as description
FROM appointments
WHERE DATE(appointment_datetime) = CURRENT_DATE
  AND status IN ('PROGRAMADA', 'CONFIRMADA')

UNION ALL

SELECT
    'pending_orders' as metric_name,
    COUNT(*)::TEXT as metric_value,
    'Medical orders awaiting completion' as description
FROM orders WHERE status IN ('PENDING', 'IN_PROGRESS')

UNION ALL

SELECT
    'low_stock_items' as metric_name,
    COUNT(*)::TEXT as metric_value,
    'Inventory items below minimum stock level' as description
FROM inventory_items
WHERE is_active = true AND current_stock <= minimum_stock;

-- Display final setup summary
SELECT
    'Database setup completed successfully!' as status,
    'Clinic Management System v2.0' as version,
    CURRENT_TIMESTAMP as completed_at;

COMMIT;