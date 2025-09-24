package app.domain.services;

import java.util.Optional;

import app.domain.model.Credentials;
import app.domain.model.Patient;
import app.domain.model.Role;
import app.domain.model.User;
import app.domain.port.PatientRepository;
import app.domain.port.UserRepository;

public class AuthenticationService {
    private final UserRepository userRepository;
    private final PatientRepository patientRepository;
    
    public AuthenticationService(UserRepository userRepository, PatientRepository patientRepository) {
        this.userRepository = userRepository;
        this.patientRepository = patientRepository;
    }
    
    public AuthenticatedUser authenticate(String username, String password) {
        // Primero buscar en usuarios del sistema
        Optional<User> user = userRepository.findByUsername(username);
        if (user.isPresent() && verifyPassword(user.get().getCredentials(), password)) {
            return new AuthenticatedUser(
                user.get().getIdCard(),
                user.get().getFullName(),
                user.get().getRole(),
                true
            );
        }
        
        // Si no se encuentra, buscar en pacientes
        Optional<Patient> patient = patientRepository.findByUsername(username);
        if (patient.isPresent() && verifyPassword(patient.get().getCredentials(), password)) {
            return new AuthenticatedUser(
                patient.get().getIdCard(),
                patient.get().getFullName(),
                Role.PATIENT, // Rol implícito para pacientes
                false
            );
        }
        
        throw new SecurityException("Invalid credentials");
    }
    
    private boolean verifyPassword(Credentials credentials, String password) {
        // En una implementación real, aquí se verificaría el hash del password
        return credentials.getPassword().equals(password);
    }
    
    public static class AuthenticatedUser {
        private final String idCard;
        private final String fullName;
        private final Role role;
        private final boolean isStaff;
        
        public AuthenticatedUser(String idCard, String fullName, Role role, boolean isStaff) {
            this.idCard = idCard;
            this.fullName = fullName;
            this.role = role;
            this.isStaff = isStaff;
        }
        
        public String getIdCard() { return idCard; }
        public String getFullName() { return fullName; }
        public Role getRole() { return role; }
        public boolean isStaff() { return isStaff; }
        
        // RESTRICCIONES ESPECÍFICAS SEGÚN DOCUMENTO
        
        /**
         * Recursos Humanos NO DEBE poder visualizar información de pacientes
         */
        public boolean canAccessPatientData() {
            return role != Role.HUMAN_RESOURCES;
        }
        
        /**
         * Recursos Humanos NO DEBE poder visualizar información de medicamentos
         */
        public boolean canAccessMedicationData() {
            return role != Role.HUMAN_RESOURCES;
        }
        
        /**
         * Recursos Humanos NO DEBE poder visualizar información de procedimientos
         */
        public boolean canAccessProcedureData() {
            return role != Role.HUMAN_RESOURCES;
        }
        
        /**
         * Solo Recursos Humanos puede crear y eliminar usuarios
         */
        public boolean canCreateUsers() {
            return role == Role.HUMAN_RESOURCES;
        }
        
        /**
         * Solo Personal Administrativo puede registrar pacientes
         */
        public boolean canRegisterPatients() {
            return role == Role.ADMINISTRATIVE;
        }
        
        /**
         * Solo Personal de Soporte puede manejar inventarios
         */
        public boolean canManageInventory() {
            return role == Role.SUPPORT;
        }
        
        /**
         * Enfermeras y Médicos pueden registrar signos vitales
         */
        public boolean canRecordVitalSigns() {
            return role == Role.NURSE || role == Role.DOCTOR;
        }
        
        /**
         * Solo Médicos pueden crear órdenes médicas
         */
        public boolean canCreateOrders() {
            return role == Role.DOCTOR;
        }
        
        /**
         * Médicos y Enfermeras pueden acceder a historia clínica
         */
        public boolean canAccessClinicalHistory() {
            return role == Role.DOCTOR || role == Role.NURSE;
        }
        
        /**
         * Personal Administrativo puede generar facturas
         */
        public boolean canGenerateInvoices() {
            return role == Role.ADMINISTRATIVE;
        }
        
        /**
         * Personal Administrativo maneja programación de citas y facturación
         */
        public boolean canScheduleAppointments() {
            return role == Role.ADMINISTRATIVE;
        }
        
        /**
         * Solo el mismo paciente puede ver su propia información (para sistema de pacientes)
         */
        public boolean canAccessOwnPatientData(String patientIdCard) {
            return (role == Role.PATIENT && this.idCard.equals(patientIdCard)) || 
                    canAccessPatientData();
        }
    }
}