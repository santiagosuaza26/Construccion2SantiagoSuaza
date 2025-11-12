-- V2__Insert_initial_data.sql
-- Script para insertar datos iniciales en la base de datos de la clínica

-- Insertar usuarios de ejemplo (contraseñas en texto plano para desarrollo)
-- NOTA: Estas son contraseñas de ejemplo. En producción, usar contraseñas seguras y hasheadas.
INSERT INTO users (id, full_name, email, phone, date_of_birth, address, role, username, password_hash) VALUES
('1234567890', 'Dr. Juan Pérez', 'juan.perez@clinica.com', '3001234567', '15/05/1980', 'Calle 123 #45-67', 'MEDICO', 'jperez', 'Password123!'),
('0987654321', 'María González', 'maria.gonzalez@clinica.com', '3019876543', '20/03/1985', 'Carrera 89 #12-34', 'ENFERMERA', 'mgonzalez', 'Password123!'),
('1122334455', 'Carlos Rodríguez', 'carlos.rodriguez@clinica.com', '3025556666', '10/08/1975', 'Avenida 56 #78-90', 'PERSONAL_ADMINISTRATIVO', 'crodriguez', 'Password123!'),
('5566778899', 'Ana López', 'ana.lopez@clinica.com', '3034445555', '25/12/1990', 'Diagonal 34 #56-78', 'RECURSOS_HUMANOS', 'alopez', 'Password123!'),
('9988776655', 'Pedro Martínez', 'pedro.martinez@clinica.com', '3043332222', '05/11/1982', 'Transversal 67 #89-01', 'SOPORTE_DE_INFORMACION', 'pmartinez', 'Password123!');

-- Insertar pacientes de ejemplo
INSERT INTO patients (identification_number, full_name, date_of_birth, gender, address, phone, email, emergency_contact_name, emergency_contact_relation, emergency_contact_phone, insurance_company_name, insurance_policy_number, insurance_active, insurance_validity_date, annual_copay_total) VALUES
('1111111111', 'Roberto Silva', '15/07/1985', 'masculino', 'Calle 45 #67-89', '3101234567', 'roberto.silva@clinica.com', 'María Silva', 'Esposa', '3117654321', 'Seguros Salud Plus', 'POL001234', true, '31/12/2025', 0.00),
('2222222222', 'Carmen Torres', '22/09/1992', 'femenino', 'Carrera 78 #90-12', '3209876543', 'carmen.torres@clinica.com', 'José Torres', 'Hermano', '3216543210', 'MediCare Colombia', 'POL005678', true, '30/06/2025', 50000.00),
('3333333333', 'Luis Ramírez', '08/03/1978', 'masculino', 'Avenida 23 #45-67', '3305554444', 'luis.ramirez@clinica.com', 'Ana Ramírez', 'Hija', '3312223333', 'Sin Seguro', 'N/A', false, NULL, 0.00);

-- Insertar medicamentos de ejemplo
INSERT INTO medications (id, name, cost, requires_specialist, specialist_type) VALUES
('MED001', 'Paracetamol 500mg', 2500.00, false, NULL),
('MED002', 'Ibuprofeno 400mg', 1800.00, false, NULL),
('MED003', 'Amoxicilina 500mg', 4500.00, true, 'Infectólogo'),
('MED004', 'Omeprazol 20mg', 3200.00, false, NULL),
('MED005', 'Insulina Humana', 15000.00, true, 'Endocrinólogo');

-- Insertar procedimientos de ejemplo
INSERT INTO procedures (id, name, cost, requires_specialist, specialist_type) VALUES
('PROC001', 'Consulta General', 50000.00, false, NULL),
('PROC002', 'Electrocardiograma', 80000.00, true, 'Cardiólogo'),
('PROC003', 'Radiografía de Tórax', 60000.00, true, 'Radiólogo'),
('PROC004', 'Ecografía Abdominal', 120000.00, true, 'Radiólogo'),
('PROC005', 'Hospitalización', 500000.00, true, 'Médico General');

-- Insertar ayudas diagnósticas de ejemplo
INSERT INTO diagnostic_aids (id, name, cost, requires_specialist, specialist_type) VALUES
('DIAG001', 'Análisis de Sangre Completo', 35000.00, false, NULL),
('DIAG002', 'Tomografía Computarizada', 250000.00, true, 'Radiólogo'),
('DIAG003', 'Resonancia Magnética', 400000.00, true, 'Radiólogo'),
('DIAG004', 'Endoscopía Digestiva', 180000.00, true, 'Gastroenterólogo'),
('DIAG005', 'Ecocardiograma', 150000.00, true, 'Cardiólogo');

-- Insertar citas de ejemplo
INSERT INTO appointments (id, patient_id, admin_id, doctor_id, appointment_date, reason, status) VALUES
('APPT001', '1111111111', '1122334455', '1234567890', '2025-11-10 09:00:00', 'Consulta de rutina', 'scheduled'),
('APPT002', '2222222222', '1122334455', '1234567890', '2025-11-12 14:30:00', 'Dolor abdominal', 'scheduled'),
('APPT003', '3333333333', '1122334455', '1234567890', '2025-11-15 11:00:00', 'Chequeo general', 'scheduled');

-- Insertar signos vitales de ejemplo
INSERT INTO vital_signs (patient_identification_number, date_time, blood_pressure, temperature, pulse, oxygen_level, observations) VALUES
('1111111111', '2025-11-01 08:30:00', '120/80', 36.5, 72, 98, 'Paciente estable'),
('2222222222', '2025-11-02 10:15:00', '130/85', 37.2, 78, 97, 'Temperatura ligeramente elevada'),
('3333333333', '2025-11-03 14:45:00', '115/75', 36.8, 68, 99, 'Signos normales');

-- Insertar tickets de soporte de ejemplo
INSERT INTO support_tickets (id, user_id, issue_description, status, assigned_to) VALUES
('TICKET001', '1234567890', 'Problemas con el acceso al sistema de registros médicos', 'open', '9988776655'),
('TICKET002', '0987654321', 'Error al registrar signos vitales del paciente', 'in_progress', '9988776655'),
('TICKET003', '1122334455', 'Sistema de facturación no genera reportes correctamente', 'resolved', '9988776655');