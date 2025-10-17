package app.clinic.application.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import app.clinic.application.dto.patient.CreatePatientDTO;
import app.clinic.application.dto.patient.PatientDTO;
import app.clinic.application.dto.user.CreateUserDTO;
import app.clinic.application.dto.user.UserDTO;
import app.clinic.application.usecase.CreateUserUseCase;
import app.clinic.application.usecase.RegisterPatientUseCase;

/**
 * Servicio de coordinación principal de la aplicación.
 * Proporciona una interfaz unificada para todos los casos de uso del sistema.
 * Sigue los principios de arquitectura limpia coordinando entre capas.
 */
@Service
public class ClinicApplicationService {

    private final CreateUserUseCase createUserUseCase;
    private final RegisterPatientUseCase registerPatientUseCase;
    private final UserApplicationService userApplicationService;
    private final PatientApplicationService patientApplicationService;

    public ClinicApplicationService(
            CreateUserUseCase createUserUseCase,
            RegisterPatientUseCase registerPatientUseCase,
            UserApplicationService userApplicationService,
            PatientApplicationService patientApplicationService) {
        this.createUserUseCase = createUserUseCase;
        this.registerPatientUseCase = registerPatientUseCase;
        this.userApplicationService = userApplicationService;
        this.patientApplicationService = patientApplicationService;
    }

    // === GESTIÓN DE USUARIOS ===

    /**
     * Crea un nuevo usuario en el sistema.
     */
    public UserDTO createUser(CreateUserDTO request) {
        return createUserUseCase.execute(request);
    }

    /**
     * Actualiza un usuario existente.
     */
    public UserDTO updateUser(String userId, Object updateRequest) {
        return userApplicationService.updateUser((app.clinic.application.dto.user.UpdateUserDTO) updateRequest);
    }

    /**
     * Busca un usuario por cédula.
     */
    public Optional<UserDTO> findUserByCedula(String cedula) {
        return userApplicationService.findUserByCedula(cedula);
    }

    /**
     * Obtiene todos los usuarios activos.
     */
    public List<UserDTO> getAllActiveUsers() {
        return userApplicationService.findAllActiveUsers();
    }

    /**
     * Obtiene todos los usuarios por rol.
     */
    public List<UserDTO> getUsersByRole(String role) {
        return userApplicationService.findUsersByRole(role);
    }

    /**
     * Activa un usuario.
     */
    public UserDTO activateUser(String cedula) {
        return userApplicationService.activateUser(cedula);
    }

    /**
     * Desactiva un usuario.
     */
    public UserDTO deactivateUser(String cedula) {
        return userApplicationService.deactivateUser(cedula);
    }

    // === GESTIÓN DE PACIENTES ===

    /**
     * Registra un nuevo paciente.
     */
    public PatientDTO registerPatient(CreatePatientDTO request) {
        return registerPatientUseCase.execute(request);
    }

    /**
     * Actualiza un paciente existente.
     */
    public PatientDTO updatePatient(String patientId, Object updateRequest) {
        return patientApplicationService.updatePatient((app.clinic.application.dto.patient.UpdatePatientDTO) updateRequest);
    }

    /**
     * Busca un paciente por cédula.
     */
    public Optional<PatientDTO> findPatientByCedula(String cedula) {
        return patientApplicationService.findPatientByCedula(cedula);
    }

    /**
     * Obtiene todos los pacientes.
     */
    public List<PatientDTO> getAllPatients() {
        return patientApplicationService.findAllPatients();
    }

    /**
     * Verifica si un paciente tiene seguro médico activo.
     */
    public boolean patientHasActiveInsurance(String cedula) {
        return patientApplicationService.hasActiveInsurance(cedula);
    }

    /**
     * Obtiene la edad de un paciente.
     */
    public int getPatientAge(String cedula) {
        return patientApplicationService.getPatientAge(cedula);
    }

    // === PERMISOS Y VALIDACIONES ===

    /**
     * Verifica si un usuario puede ver información de pacientes.
     */
    public boolean canUserViewPatientInfo(String userCedula) {
        return userApplicationService.canViewPatientInfo(userCedula);
    }

    /**
     * Verifica si un usuario puede gestionar otros usuarios.
     */
    public boolean canUserManageUsers(String userCedula) {
        return userApplicationService.canManageUsers(userCedula);
    }

    /**
     * Verifica si un usuario puede registrar pacientes.
     */
    public boolean canUserRegisterPatients(String userCedula) {
        return userApplicationService.canRegisterPatients(userCedula);
    }

    // === OPERACIONES DE SISTEMA ===

    /**
     * Obtiene estadísticas generales del sistema.
     */
    public SystemStatistics getSystemStatistics() {
        List<UserDTO> allUsers = userApplicationService.findAllUsers();
        List<PatientDTO> allPatients = patientApplicationService.findAllPatients();

        return new SystemStatistics(
            allUsers.size(),
            allPatients.size(),
            (int) allUsers.stream().filter(UserDTO::isActive).count(),
            (int) allPatients.stream().filter(p -> patientHasActiveInsurance(p.getCedula())).count()
        );
    }

    /**
     * Clase interna para estadísticas del sistema.
     */
    public static class SystemStatistics {
        private final int totalUsers;
        private final int totalPatients;
        private final int activeUsers;
        private final int patientsWithInsurance;

        public SystemStatistics(int totalUsers, int totalPatients, int activeUsers, int patientsWithInsurance) {
            this.totalUsers = totalUsers;
            this.totalPatients = totalPatients;
            this.activeUsers = activeUsers;
            this.patientsWithInsurance = patientsWithInsurance;
        }

        // Getters
        public int getTotalUsers() { return totalUsers; }
        public int getTotalPatients() { return totalPatients; }
        public int getActiveUsers() { return activeUsers; }
        public int getPatientsWithInsurance() { return patientsWithInsurance; }
    }
}