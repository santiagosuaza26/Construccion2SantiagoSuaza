package app.application.mapper;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import app.application.dto.request.RegisterPatientRequest;
import app.application.dto.response.PatientResponse;
import app.domain.model.Credentials;
import app.domain.model.EmergencyContact;
import app.domain.model.InsurancePolicy;
import app.domain.model.Patient;

/**
 * PatientMapper - Professional mapper for Patient ↔ DTO conversions
 *
 * RESPONSIBILITIES:
 * - Convert RegisterPatientRequest → Patient (Domain)
 * - Convert Patient (Domain) → PatientResponse
 * - Handle complex structures (EmergencyContact, InsurancePolicy)
 * - Validate and calculate derived information (age, insurance status)
 * - Apply specific patient business rules
 *
 * IMPLEMENTED RULES:
 * - Emergency contact REQUIRED (minimum and maximum one)
 * - Medical insurance OPTIONAL (only one policy)
 * - Phones exactly 10 digits for patients
 * - Maximum age 150 years
 * - Validation of allowed genders
 */
@Component
public class PatientMapper {
    
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final List<String> ALLOWED_GENDERS = List.of("masculino", "femenino", "otro"); // Allowed genders in Spanish as per business requirements
    
    /**
     * Convierte RegisterPatientRequest → Patient (Domain Model)
     * 
     * TRANSFORMACIONES COMPLEJAS:
     * - String dates → LocalDate con validaciones
     * - Crear EmergencyContact obligatorio
     * - Crear InsurancePolicy opcional
     * - Validar género permitido
     * - Crear Credentials seguras
     * 
     * @param request DTO con todos los datos del paciente
     * @return Patient domain model completo
     * @throws IllegalArgumentException si hay datos inválidos
     */
    public Patient toPatient(RegisterPatientRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("RegisterPatientRequest cannot be null");
        }
        
        try {
            // 1. Convertir y validar fecha de nacimiento
            LocalDate birthDate = parseAndValidateBirthDate(request.getBirthDate());
            
            // 2. Validar y normalizar género
            String normalizedGender = validateAndNormalizeGender(request.getGender());
            
            // 3. Crear credenciales seguras
            Credentials credentials = createSecureCredentials(request.getUsername(), request.getPassword());
            
            // 4. Crear contacto de emergencia (OBLIGATORIO)
            EmergencyContact emergencyContact = createEmergencyContact(request);
            
            // 5. Crear póliza de seguro (OPCIONAL)
            InsurancePolicy insurancePolicy = createInsurancePolicy(request);
            
            // 6. Procesar dirección opcional
            String processedAddress = processOptionalAddress(request.getAddress());
            
            // 7. Crear el paciente con todas las validaciones
            Patient patient = new Patient(
                request.getIdCard().trim(),
                request.getFullName().trim(),
                birthDate,
                normalizedGender,
                processedAddress,
                request.getPhone().trim(), // Ya validado como exactamente 10 dígitos
                request.getEmail().toLowerCase().trim(),
                credentials,
                emergencyContact,
                insurancePolicy
            );
            
            return patient;
            
        } catch (Exception e) {
            throw new IllegalArgumentException("Error converting RegisterPatientRequest to Patient: " + e.getMessage(), e);
        }
    }
    
    /**
     * Convierte Patient (Domain) → PatientResponse (DTO)
     * 
     * TRANSFORMACIONES:
     * - LocalDate → String (DD/MM/YYYY)
     * - Calcular edad actual
     * - Crear objetos internos para EmergencyContact e Insurance
     * - Formatear información del seguro con días restantes
     * - Ocultar información sensible
     * 
     * @param patient Domain model a convertir
     * @return PatientResponse DTO completo para el cliente
     */
    public PatientResponse toResponse(Patient patient) {
        if (patient == null) {
            throw new IllegalArgumentException("Patient cannot be null");
        }
        
        try {
            PatientResponse response = new PatientResponse();
            
            // Información básica
            response.setIdCard(patient.getIdCard());
            response.setFullName(patient.getFullName());
            response.setEmail(patient.getEmail());
            response.setPhone(patient.getPhone());
            response.setGender(patient.getGender());
            response.setAddress(patient.getAddress());
            
            // Convertir fecha y calcular edad
            if (patient.getBirthDate() != null) {
                response.setBirthDate(patient.getBirthDate().format(DATE_FORMATTER));
                response.setAge(Period.between(patient.getBirthDate(), LocalDate.now()).getYears());
            }
            
            // Credenciales (solo username, NO password)
            if (patient.getCredentials() != null) {
                response.setUsername(patient.getCredentials().getUsername());
            }
            
            // Convertir contacto de emergencia
            response.setEmergencyContact(createEmergencyContactInfo(patient.getEmergencyContact()));
            
            // Convertir información del seguro
            response.setInsurancePolicy(createInsurancePolicyInfo(patient.getInsurancePolicy()));
            
            // Fecha de registro (simulada - en implementación real vendría de auditoría)
            response.setRegistrationDate(LocalDate.now().format(DATE_FORMATTER));
            
            return response;
            
        } catch (Exception e) {
            throw new IllegalArgumentException("Error converting Patient to PatientResponse: " + e.getMessage(), e);
        }
    }
    
    /**
     * Convierte lista de Patients → lista de PatientResponses
     * Optimizado para grandes volúmenes
     */
    public List<PatientResponse> toResponseList(List<Patient> patients) {
        if (patients == null) {
            throw new IllegalArgumentException("Patient list cannot be null");
        }
        
        return patients.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }
    
    /**
     * Crear PatientResponse básico (solo información esencial)
     * Útil para listas donde no se necesita información completa
     */
    public PatientResponse toBasicResponse(Patient patient) {
        if (patient == null) {
            throw new IllegalArgumentException("Patient cannot be null");
        }
        
        PatientResponse response = new PatientResponse(
            patient.getIdCard(),
            patient.getFullName(),
            patient.getEmail(),
            patient.getPhone()
        );
        
        // Agregar edad si está disponible
        if (patient.getBirthDate() != null) {
            response.setAge(Period.between(patient.getBirthDate(), LocalDate.now()).getYears());
        }
        
        return response;
    }
    
    // =============================================================================
    // MÉTODOS PRIVADOS DE UTILIDAD PARA CONVERSIÓN REQUEST → DOMAIN
    // =============================================================================
    
    /**
     * Parsear y validar fecha de nacimiento con reglas específicas para pacientes
     */
    private LocalDate parseAndValidateBirthDate(String dateString) {
        if (dateString == null || dateString.isBlank()) {
            throw new IllegalArgumentException("Patient birth date is required");
        }
        
        try {
            LocalDate birthDate = LocalDate.parse(dateString, DATE_FORMATTER);
            
            // Validaciones específicas para pacientes
            if (birthDate.isAfter(LocalDate.now())) {
                throw new IllegalArgumentException("Birth date cannot be in the future");
            }
            
            int age = Period.between(birthDate, LocalDate.now()).getYears();
            if (age > 150) {
                throw new IllegalArgumentException("Patient age cannot exceed 150 years");
            }
            
            if (age < 0) {
                throw new IllegalArgumentException("Invalid birth date calculation");
            }
            
            return birthDate;
            
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid birth date format. Use DD/MM/YYYY", e);
        }
    }
    
    /**
     * Validar y normalizar género según reglas del documento
     */
    private String validateAndNormalizeGender(String gender) {
        if (gender == null || gender.isBlank()) {
            throw new IllegalArgumentException("Patient gender is required");
        }
        
        String normalizedGender = gender.toLowerCase().trim();
        
        if (!ALLOWED_GENDERS.contains(normalizedGender)) {
            throw new IllegalArgumentException("Gender must be: masculino, femenino, or otro");
        }
        
        return normalizedGender;
    }
    
    /**
     * Crear credenciales con validaciones específicas para pacientes
     */
    private Credentials createSecureCredentials(String username, String password) {
        if (username == null || username.isBlank()) {
            throw new IllegalArgumentException("Patient username is required");
        }
        
        if (password == null || password.isBlank()) {
            throw new IllegalArgumentException("Patient password is required");
        }
        
        try {
            return new Credentials(username.trim(), password);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid patient credentials: " + e.getMessage(), e);
        }
    }
    
    /**
     * Crear contacto de emergencia OBLIGATORIO
     * Según documento: mínimo y máximo un solo contacto
     */
    private EmergencyContact createEmergencyContact(RegisterPatientRequest request) {
        // Validar que todos los campos de emergencia estén presentes
        if (request.getEmergencyFirstName() == null || request.getEmergencyFirstName().isBlank()) {
            throw new IllegalArgumentException("Emergency contact first name is required");
        }
        
        if (request.getEmergencyLastName() == null || request.getEmergencyLastName().isBlank()) {
            throw new IllegalArgumentException("Emergency contact last name is required");
        }
        
        if (request.getEmergencyRelationship() == null || request.getEmergencyRelationship().isBlank()) {
            throw new IllegalArgumentException("Emergency contact relationship is required");
        }
        
        if (request.getEmergencyPhone() == null || !request.getEmergencyPhone().matches("\\d{10}")) {
            throw new IllegalArgumentException("Emergency contact phone must have exactly 10 digits");
        }
        
        try {
            return new EmergencyContact(
                request.getEmergencyFirstName().trim(),
                request.getEmergencyLastName().trim(),
                request.getEmergencyRelationship().trim(),
                request.getEmergencyPhone().trim()
            );
        } catch (Exception e) {
            throw new IllegalArgumentException("Error creating emergency contact: " + e.getMessage(), e);
        }
    }
    
    /**
     * Crear póliza de seguro OPCIONAL
     * Según documento: solo una póliza
     */
    private InsurancePolicy createInsurancePolicy(RegisterPatientRequest request) {
        // Si no hay información de seguro, retornar null
        if (!request.hasInsurance()) {
            return null;
        }
        
        // Si tiene información parcial, validar que esté completa
        if (request.getInsuranceCompany() == null || request.getInsuranceCompany().isBlank()) {
            throw new IllegalArgumentException("Insurance company name is required when providing insurance info");
        }
        
        if (request.getPolicyNumber() == null || request.getPolicyNumber().isBlank()) {
            throw new IllegalArgumentException("Policy number is required when providing insurance info");
        }
        
        if (request.getPolicyActive() == null) {
            throw new IllegalArgumentException("Policy status must be specified");
        }
        
        // Parsear fecha de vencimiento si está presente
        LocalDate endDate = null;
        if (request.getPolicyEndDate() != null && !request.getPolicyEndDate().isBlank()) {
            try {
                endDate = LocalDate.parse(request.getPolicyEndDate(), DATE_FORMATTER);
                
                // Validar que la fecha de vencimiento sea lógica
                if (endDate.isBefore(LocalDate.now().minusYears(1))) {
                    throw new IllegalArgumentException("Policy end date seems to old");
                }
                
            } catch (DateTimeParseException e) {
                throw new IllegalArgumentException("Invalid policy end date format. Use DD/MM/YYYY", e);
            }
        }
        
        try {
            return new InsurancePolicy(
                request.getInsuranceCompany().trim(),
                request.getPolicyNumber().trim(),
                request.getPolicyActive(),
                endDate
            );
        } catch (Exception e) {
            throw new IllegalArgumentException("Error creating insurance policy: " + e.getMessage(), e);
        }
    }
    
    /**
     * Procesar dirección opcional con validaciones
     */
    private String processOptionalAddress(String address) {
        if (address == null || address.isBlank()) {
            return null;
        }
        
        String trimmedAddress = address.trim();
        
        if (trimmedAddress.length() > 30) {
            throw new IllegalArgumentException("Patient address must be maximum 30 characters");
        }
        
        return trimmedAddress;
    }
    
    // =============================================================================
    // MÉTODOS PRIVADOS DE UTILIDAD PARA CONVERSIÓN DOMAIN → RESPONSE
    // =============================================================================
    
    /**
     * Crear información del contacto de emergencia para el response
     */
    private PatientResponse.EmergencyContactInfo createEmergencyContactInfo(EmergencyContact contact) {
        if (contact == null) {
            return null;
        }
        
        return new PatientResponse.EmergencyContactInfo(
            contact.getFirstName() + " " + contact.getLastName(),
            contact.getRelationship(),
            contact.getPhone()
        );
    }
    
    /**
     * Crear información de la póliza de seguro con cálculos de días restantes
     */
    private PatientResponse.InsurancePolicyInfo createInsurancePolicyInfo(InsurancePolicy policy) {
        if (policy == null) {
            return null;
        }
        
        // Calcular días restantes
        int remainingDays = 0;
        String endDateString = null;
        
        if (policy.getEndDate() != null) {
            remainingDays = (int) policy.remainingDaysFrom(LocalDate.now());
            endDateString = policy.getEndDate().format(DATE_FORMATTER);
        }
        
        return new PatientResponse.InsurancePolicyInfo(
            policy.getCompany(),
            policy.getPolicyNumber(),
            policy.isActive(),
            endDateString,
            remainingDays
        );
    }
    
    // =============================================================================
    // MÉTODOS DE UTILIDAD PARA VALIDACIONES Y TESTING
    // =============================================================================
    
    /**
     * Validar RegisterPatientRequest con reglas adicionales de negocio
     */
    public void validateRegisterPatientRequest(RegisterPatientRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("RegisterPatientRequest cannot be null");
        }
        
        // Validar que nombre tenga al menos nombres y apellidos
        if (request.getFullName() != null) {
            String[] nameParts = request.getFullName().trim().split("\\s+");
            if (nameParts.length < 2) {
                throw new IllegalArgumentException("Patient must provide at least first name and last name");
            }
        }
        
        // Validar coherencia en información de seguro
        if (request.hasInsurance()) {
            if (request.getPolicyActive() && 
                (request.getPolicyEndDate() == null || request.getPolicyEndDate().isBlank())) {
                throw new IllegalArgumentException("Active insurance policies must have an end date");
            }
        }
        
        // Validar que el contacto de emergencia no sea el mismo paciente
        if (request.getEmergencyPhone() != null && request.getEmergencyPhone().equals(request.getPhone())) {
            // Warning: mismo teléfono, pero puede ser válido en algunos casos
            // En implementación real, esto podría ser solo un warning
        }
    }
    
    /**
     * Crear un Patient de prueba para testing
     */
    public Patient createTestPatient(String idCard, String name, boolean withInsurance) {
        RegisterPatientRequest testRequest = new RegisterPatientRequest(
            idCard, // idCard
            name, // name
            "15/05/1990", // birthDate
            "masculino", // gender
            "1234567890", // phone
            name.toLowerCase().replace(" ", ".") + "@test.com", // email
            name.toLowerCase().replace(" ", "") + "123", // username
            "TestPass123!", // password
            "Calle Falsa 123", // dirección ficticia
            "Ciudad Test", // ciudad ficticia
            withInsurance ? "POL123456" : null, // número de póliza ficticio o null
            withInsurance ? "2025-12-31" : null // fecha de fin de póliza ficticia o null
        );
        
        // Agregar contacto de emergencia obligatorio
        testRequest.setEmergencyFirstName("Juan");
        testRequest.setEmergencyLastName("Pérez");
        testRequest.setEmergencyRelationship("Hermano");
        testRequest.setEmergencyPhone("0987654321");
        
        // Agregar seguro si se solicita
        if (withInsurance) {
            testRequest.setInsuranceCompany("Seguros Test SA");
            testRequest.setPolicyNumber("POL123456");
            testRequest.setPolicyActive(true);
            testRequest.setPolicyEndDate("31/12/2025");
        }
        
        return toPatient(testRequest);
    }
    
    /**
     * Método para debugging
     */
    public String patientToDebugString(Patient patient) {
        if (patient == null) return "Patient[null]";
        
        return String.format("Patient[id=%s, name=%s, age=%d, hasInsurance=%s, emergencyContact=%s]",
            patient.getIdCard(),
            patient.getFullName(),
            Period.between(patient.getBirthDate(), LocalDate.now()).getYears(),
            patient.getInsurancePolicy() != null,
            patient.getEmergencyContact() != null ? 
                patient.getEmergencyContact().getFirstName() + " " + patient.getEmergencyContact().getLastName() : 
                "None"
        );
    }

    /**
     * Convierte lista de Patients → lista de PatientResponses
     * Método adicional con nombre diferente para evitar conflictos
     */
    public List<PatientResponse> toPatientResponseListFromPatients(List<Patient> patients) {
        if (patients == null) {
            throw new IllegalArgumentException("Patient list cannot be null");
        }

        return patients.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }
}