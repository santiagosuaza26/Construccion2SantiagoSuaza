-- Sample data for development and testing

-- Insert sample users
INSERT INTO users (id, username, password_hash, full_name, email, phone, date_of_birth, address, role, created_at, updated_at) VALUES
('1234567890', 'admin_hr', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', 'Admin Recursos Humanos', 'admin@clinic.com', '3001234567', '1980-01-01', 'Calle 123 #45-67', 'RECURSOS_HUMANOS', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('1234567891', 'admin_staff', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', 'Admin Personal', 'admin_staff@clinic.com', '3001234568', '1982-02-02', 'Carrera 45 #12-34', 'PERSONAL_ADMINISTRATIVO', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('1234567892', 'soporte_it', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', 'Soporte IT', 'soporte@clinic.com', '3001234569', '1985-03-03', 'Avenida 67 #89-01', 'SOPORTE_DE_INFORMACION', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('1234567893', 'nurse_ana', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', 'Ana López', 'ana.nurse@clinic.com', '3001234570', '1990-04-04', 'Transversal 23 #56-78', 'ENFERMERA', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('1234567894', 'dr_perez', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', 'Dr. Carlos Pérez', 'carlos.doctor@clinic.com', '3001234571', 'Diagonal 34 #67-89', 'MEDICO', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Insert sample patients
INSERT INTO patients (id, full_name, date_of_birth, gender, address, phone, email, emergency_name, emergency_relation, emergency_phone, insurance_company, insurance_policy, insurance_active, insurance_validity_date) VALUES
('1098765432', 'María González', '1985-05-15', 'FEMENINO', 'Calle 45 #67-89', '3012345678', 'maria@email.com', 'Juan González', 'Esposo', '3012345679', 'Salud Total', 'POL001234', true, '2025-12-31'),
('1098765433', 'Pedro Ramírez', '1978-08-22', 'MASCULINO', 'Carrera 12 #34-56', '3012345680', 'pedro@email.com', 'Ana Ramírez', 'Esposa', '3012345681', 'Nueva EPS', 'POL005678', true, '2025-10-15'),
('1098765434', 'Laura Martínez', '1992-11-30', 'FEMENINO', 'Avenida 78 #90-12', '3012345682', 'laura@email.com', 'Carlos Martínez', 'Padre', '3012345683', NULL, NULL, false, NULL);

-- Insert sample medications
INSERT INTO medications (id, name, cost, requires_specialist, specialist_type, stock_quantity) VALUES
('MED001', 'Paracetamol 500mg', 2500.00, false, NULL, 100),
('MED002', 'Amoxicilina 500mg', 4500.00, true, 'Infectólogo', 50),
('MED003', 'Ibuprofeno 400mg', 1800.00, false, NULL, 80),
('MED004', 'Omeprazol 20mg', 3200.00, true, 'Gastroenterólogo', 60);

-- Insert sample procedures
INSERT INTO procedures (id, name, cost, requires_specialist, specialist_type) VALUES
('PROC001', 'Consulta General', 35000.00, false, NULL),
('PROC002', 'Ecografía Abdominal', 85000.00, true, 'Radiólogo', 30),
('PROC003', 'Electrocardiograma', 45000.00, true, 'Cardiólogo', 20),
('PROC004', 'Hospitalización 24h', 150000.00, true, 'Médico General', 10);

-- Insert sample diagnostic aids
INSERT INTO diagnostic_aids (id, name, cost, requires_specialist, specialist_type) VALUES
('DIAG001', 'Rayos X Tórax', 25000.00, true, 'Radiólogo'),
('DIAG002', 'Laboratorio Sangre Completo', 35000.00, true, 'Patólogo'),
('DIAG003', 'Mamografía', 65000.00, true, 'Radiólogo'),
('DIAG004', 'Resonancia Magnética', 180000.00, true, 'Radiólogo');

-- Insert sample appointments
INSERT INTO appointments (id, patient_id, admin_id, doctor_id, appointment_date, reason, status) VALUES
('APPT001', '1098765432', '1234567891', '1234567894', '2024-01-15 10:00:00', 'Consulta de rutina', 'COMPLETED'),
('APPT002', '1098765433', '1234567891', '1234567894', '2024-01-16 14:30:00', 'Dolor abdominal', 'SCHEDULED'),
('APPT003', '1098765434', '1234567891', '1234567894', '2024-01-17 09:15:00', 'Chequeo anual', 'SCHEDULED');

-- Insert sample orders
INSERT INTO orders (order_number, patient_id, doctor_id, order_type, status, total_cost) VALUES
('123456', '1098765432', '1234567894', 'MEDICATION', 'COMPLETED', 7000.00),
('123457', '1098765433', '1234567894', 'PROCEDURE', 'PENDING', 35000.00),
('123458', '1098765434', '1234567894', 'DIAGNOSTIC_AID', 'COMPLETED', 25000.00);

-- Insert sample medication orders
INSERT INTO medication_orders (order_number, medication_id, dosage, duration, item_number) VALUES
('123456', 'MED001', '500mg cada 8 horas', '5 días', 1),
('123456', 'MED003', '400mg cada 12 horas', '3 días', 2);

-- Insert sample procedure orders
INSERT INTO procedure_orders (order_number, procedure_id, details, item_number) VALUES
('123457', 'PROC001', 'Consulta por dolor abdominal', 1);

-- Insert sample diagnostic aid orders
INSERT INTO diagnostic_aid_orders (order_number, diagnostic_aid_id, details, item_number) VALUES
('123458', 'DIAG001', 'Rayos X de tórax por tos persistente', 1);

-- Insert sample vital signs
INSERT INTO vital_signs (patient_id, nurse_id, blood_pressure, temperature, pulse, oxygen_level, observations) VALUES
('1098765432', '1234567893', '120/80', 36.5, 72, 98, 'Signos vitales normales'),
('1098765433', '1234567893', '130/85', 37.2, 78, 96, 'Temperatura elevada, posible infección');

-- Insert sample medical records
INSERT INTO medical_records (patient_id, doctor_id, consultation_date, reason, symptoms, diagnosis, prescriptions, procedures, diagnostic_aids) VALUES
('1098765432', '1234567894', '2024-01-15', 'Chequeo rutinario', 'Ninguno', 'Paciente en buen estado general', 'Paracetamol 500mg - 500mg cada 8 horas por 5 días', 'Consulta General', NULL),
('1098765433', '1234567894', '2024-01-16', 'Dolor abdominal', 'Dolor en cuadrante inferior derecho, náuseas', 'Posible apendicitis', 'Analgésicos', 'Consulta General', 'Rayos X Abdominal'),
('1098765434', '1234567894', '2024-01-17', 'Tos persistente', 'Tos seca por más de 2 semanas', 'Bronquitis aguda', 'Antitusivos', 'Consulta General', 'Rayos X Tórax');

-- Insert sample billing
INSERT INTO billing (patient_id, order_number, doctor_name, total_cost, copay_amount, insurance_covered, applied_medications, applied_procedures, applied_diagnostic_aids, status) VALUES
('1098765432', '123456', 'Dr. Carlos Pérez', 7000.00, 50000.00, true, 'Paracetamol 500mg, Ibuprofeno 400mg', NULL, NULL, 'PAID'),
('1098765433', '123457', 'Dr. Carlos Pérez', 35000.00, 50000.00, true, NULL, 'Consulta General', NULL, 'PENDING'),
('1098765434', '123458', 'Dr. Carlos Pérez', 25000.00, 50000.00, false, NULL, NULL, 'Rayos X Tórax', 'PENDING');