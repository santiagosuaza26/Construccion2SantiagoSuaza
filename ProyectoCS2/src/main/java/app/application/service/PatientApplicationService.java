package app.application.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import app.application.dto.request.RegisterPatientRequest;
import app.application.dto.response.CommonResponse;
import app.application.dto.response.PatientResponse;
import app.application.mapper.PatientMapper;
import app.domain.model.Patient;
import app.domain.model.Role;
import app.domain.port.PatientRepository;
import app.domain.services.AdministrativeService;
import app.domain.services.AuthenticationService.AuthenticatedUser;

@Service
public class PatientApplicationService {
    
    private final AdministrativeService administrativeService;
    private final PatientRepository patientRepository;
    private final PatientMapper patientMapper;
    
    public PatientApplicationService(AdministrativeService administrativeService,
                                    PatientRepository patientRepository,
                                    PatientMapper patientMapper) {
        this.administrativeService = administrativeService;
        this.patientRepository = patientRepository;
        this.patientMapper = patientMapper;
    }
    
    public CommonResponse<PatientResponse> registerPatient(RegisterPatientRequest request, AuthenticatedUser currentUser) {
        try {
            if (!canRegisterPatients(currentUser)) {
                logUnauthorizedAccess(currentUser, "REGISTER_PATIENT");
                return CommonResponse.error("Access denied - Only Administrative staff can register patients", "PAT_001");
            }
            
            validateRegisterPatientRequest(request);
            
            CommonResponse<Void> uniquenessCheck = validatePatientUniqueness(request);
            if (!uniquenessCheck.isSuccess()) {
                return CommonResponse.error(uniquenessCheck.getMessage(), uniquenessCheck.getErrorCode());
            }
            
            Patient patientToCreate = patientMapper.toPatient(request);
            Patient createdPatient = administrativeService.registerPatient(patientToCreate);
            PatientResponse patientResponse = patientMapper.toResponse(createdPatient);
            
            logPatientRegistered(createdPatient, currentUser);
            
            return CommonResponse.success("Patient registered successfully", patientResponse);
            
        } catch (IllegalArgumentException e) {
            logValidationError("registerPatient", e, currentUser);
            return CommonResponse.error(e.getMessage(), "PAT_002");
        } catch (Exception e) {
            logSystemError("registerPatient", e, currentUser);
            return CommonResponse.error("Internal error registering patient", "PAT_003");
        }
    }
    
    public CommonResponse<PatientResponse> updatePatient(String patientIdCard, RegisterPatientRequest request, 
                                                        AuthenticatedUser currentUser) {
        try {
            if (!canUpdatePatients(currentUser)) {
                logUnauthorizedAccess(currentUser, "UPDATE_PATIENT");
                return CommonResponse.error("Access denied - Only Administrative staff can update patients", "PAT_004");
            }
            
            if (patientIdCard == null || patientIdCard.isBlank()) {
                return CommonResponse.error("Patient ID card is required", "PAT_005");
            }
            
            validateRegisterPatientRequest(request);
            
            Optional<Patient> existingPatientOpt = patientRepository.findByIdCard(patientIdCard);
            if (existingPatientOpt.isEmpty()) {
                return CommonResponse.error("Patient not found with ID: " + patientIdCard, "PAT_006");
            }
            
            Patient existingPatient = existingPatientOpt.get();
            
            if (!patientIdCard.equals(request.getIdCard()) || 
                !existingPatient.getCredentials().getUsername().equals(request.getUsername())) {
                CommonResponse<Void> uniquenessCheck = validatePatientUniquenessForUpdate(request, patientIdCard);
                if (!uniquenessCheck.isSuccess()) {
                    return CommonResponse.error(uniquenessCheck.getMessage(), uniquenessCheck.getErrorCode());
                }
            }
            
            Patient updatedPatientData = patientMapper.toPatient(request);
            administrativeService.removePatient(patientIdCard);
            Patient updatedPatient = administrativeService.registerPatient(updatedPatientData);
            PatientResponse patientResponse = patientMapper.toResponse(updatedPatient);
            
            logPatientUpdated(existingPatient, updatedPatient, currentUser);
            
            return CommonResponse.success("Patient updated successfully", patientResponse);
            
        } catch (IllegalArgumentException e) {
            logValidationError("updatePatient", e, currentUser);
            return CommonResponse.error(e.getMessage(), "PAT_007");
        } catch (Exception e) {
            logSystemError("updatePatient", e, currentUser);
            return CommonResponse.error("Internal error updating patient", "PAT_008");
        }
    }
    
    public CommonResponse<String> removePatient(String patientIdCard, AuthenticatedUser currentUser) {
        try {
            if (!canRemovePatients(currentUser)) {
                logUnauthorizedAccess(currentUser, "REMOVE_PATIENT");
                return CommonResponse.error("Access denied - Only Administrative staff can remove patients", "PAT_009");
            }
            
            if (patientIdCard == null || patientIdCard.isBlank()) {
                return CommonResponse.error("Patient ID card is required", "PAT_010");
            }
            
            Optional<Patient> patientToRemoveOpt = patientRepository.findByIdCard(patientIdCard);
            if (patientToRemoveOpt.isEmpty()) {
                return CommonResponse.error("Patient not found with ID: " + patientIdCard, "PAT_011");
            }
            
            Patient patientToRemove = patientToRemoveOpt.get();
            
            if (!canRemoveSpecificPatient(patientToRemove, currentUser)) {
                return CommonResponse.error("Cannot remove this patient due to business restrictions", "PAT_012");
            }
            
            administrativeService.removePatient(patientIdCard);
            
            logPatientRemoved(patientToRemove, currentUser);
            
            return CommonResponse.success("Patient removed successfully: " + patientToRemove.getFullName());
            
        } catch (Exception e) {
            logSystemError("removePatient", e, currentUser);
            return CommonResponse.error("Internal error removing patient", "PAT_013");
        }
    }
    
    public CommonResponse<List<PatientResponse>> getAllPatients() {
        try {
            List<Patient> patients = patientRepository.findAll();
            List<PatientResponse> patientResponses = patientMapper.toResponseList(patients);

            return CommonResponse.success(
                String.format("Retrieved %d patients", patients.size()),
                patientResponses
            );

        } catch (Exception e) {
            logSystemError("getAllPatients", e, null);
            return CommonResponse.error("Internal error retrieving patients", "PAT_015");
        }
    }

    public CommonResponse<List<PatientResponse>> getAllPatients(AuthenticatedUser currentUser) {
        try {
            if (!canViewPatients(currentUser)) {
                logUnauthorizedAccess(currentUser, "VIEW_ALL_PATIENTS");
                return CommonResponse.error("Access denied - Cannot view patients list", "PAT_014");
            }

            List<Patient> patients = patientRepository.findAll();
            List<PatientResponse> patientResponses = patientMapper.toResponseList(patients);

            logPatientsListed(patients.size(), currentUser);

            return CommonResponse.success(
                String.format("Retrieved %d patients", patients.size()),
                patientResponses
            );

        } catch (Exception e) {
            logSystemError("getAllPatients", e, currentUser);
            return CommonResponse.error("Internal error retrieving patients", "PAT_015");
        }
    }
    
    public CommonResponse<PatientResponse> getPatientById(String patientIdCard, AuthenticatedUser currentUser) {
        try {
            if (!canViewPatients(currentUser)) {
                logUnauthorizedAccess(currentUser, "VIEW_PATIENT_BY_ID");
                return CommonResponse.error("Access denied - Cannot view patient details", "PAT_016");
            }
            
            if (patientIdCard == null || patientIdCard.isBlank()) {
                return CommonResponse.error("Patient ID card is required", "PAT_017");
            }
            
            Optional<Patient> patientOpt = patientRepository.findByIdCard(patientIdCard);
            
            if (patientOpt.isEmpty()) {
                return CommonResponse.error("Patient not found with ID: " + patientIdCard, "PAT_018");
            }
            
            PatientResponse patientResponse = patientMapper.toResponse(patientOpt.get());
            
            logPatientViewed(patientOpt.get(), currentUser);
            
            return CommonResponse.success("Patient found", patientResponse);
            
        } catch (Exception e) {
            logSystemError("getPatientById", e, currentUser);
            return CommonResponse.error("Internal error retrieving patient", "PAT_019");
        }
    }
    
    public CommonResponse<PatientResponse> getPatientByUsername(String username, AuthenticatedUser currentUser) {
        try {
            if (!canViewPatients(currentUser)) {
                logUnauthorizedAccess(currentUser, "VIEW_PATIENT_BY_USERNAME");
                return CommonResponse.error("Access denied - Cannot view patient details", "PAT_020");
            }
            
            if (username == null || username.isBlank()) {
                return CommonResponse.error("Username is required", "PAT_021");
            }
            
            Optional<Patient> patientOpt = patientRepository.findByUsername(username);
            
            if (patientOpt.isEmpty()) {
                return CommonResponse.error("Patient not found with username: " + username, "PAT_022");
            }
            
            PatientResponse patientResponse = patientMapper.toResponse(patientOpt.get());
            
            logPatientViewed(patientOpt.get(), currentUser);
            
            return CommonResponse.success("Patient found", patientResponse);
            
        } catch (Exception e) {
            logSystemError("getPatientByUsername", e, currentUser);
            return CommonResponse.error("Internal error retrieving patient", "PAT_023");
        }
    }
    
    public CommonResponse<PatientResponse> getOwnPatientInfo(AuthenticatedUser currentUser) {
        try {
            if (currentUser.getRole() != Role.PATIENT) {
                return CommonResponse.error("Only patients can access their own information", "PAT_024");
            }
            
            Optional<Patient> patientOpt = patientRepository.findByIdCard(currentUser.getIdCard());
            
            if (patientOpt.isEmpty()) {
                return CommonResponse.error("Patient information not found", "PAT_025");
            }
            
            PatientResponse patientResponse = patientMapper.toResponse(patientOpt.get());
            
            logOwnPatientInfoViewed(patientOpt.get());
            
            return CommonResponse.success("Patient information retrieved", patientResponse);
            
        } catch (Exception e) {
            logSystemError("getOwnPatientInfo", e, currentUser);
            return CommonResponse.error("Internal error retrieving patient information", "PAT_026");
        }
    }
    
    // =============================================================================
    // MÉTODOS PRIVADOS DE VALIDACIÓN
    // =============================================================================
    
    private void validateRegisterPatientRequest(RegisterPatientRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("Patient registration request cannot be null");
        }
        
        patientMapper.validateRegisterPatientRequest(request);
    }
    
    private CommonResponse<Void> validatePatientUniqueness(RegisterPatientRequest request) {
        if (patientRepository.existsByIdCard(request.getIdCard())) {
            return CommonResponse.error("Patient with ID card " + request.getIdCard() + " already exists", "PAT_027");
        }
        
        if (patientRepository.existsByUsername(request.getUsername())) {
            return CommonResponse.error("Username " + request.getUsername() + " already exists", "PAT_028");
        }
        
        return CommonResponse.success(null);
    }
    
    private CommonResponse<Void> validatePatientUniquenessForUpdate(RegisterPatientRequest request, String currentIdCard) {
        Optional<Patient> patientWithSameId = patientRepository.findByIdCard(request.getIdCard());
        if (patientWithSameId.isPresent() && !patientWithSameId.get().getIdCard().equals(currentIdCard)) {
            return CommonResponse.error("Patient with ID card " + request.getIdCard() + " already exists", "PAT_029");
        }
        
        Optional<Patient> patientWithSameUsername = patientRepository.findByUsername(request.getUsername());
        if (patientWithSameUsername.isPresent() && !patientWithSameUsername.get().getIdCard().equals(currentIdCard)) {
            return CommonResponse.error("Username " + request.getUsername() + " already exists", "PAT_030");
        }
        
        return CommonResponse.success(null);
    }
    
    // =============================================================================
    // MÉTODOS PRIVADOS DE AUTORIZACIÓN
    // =============================================================================
    
    private boolean canRegisterPatients(AuthenticatedUser user) {
        return user != null && user.getRole() == Role.ADMINISTRATIVE && user.canRegisterPatients();
    }
    
    private boolean canUpdatePatients(AuthenticatedUser user) {
        return user != null && user.getRole() == Role.ADMINISTRATIVE;
    }
    
    private boolean canRemovePatients(AuthenticatedUser user) {
        return user != null && user.getRole() == Role.ADMINISTRATIVE;
    }
    
    private boolean canViewPatients(AuthenticatedUser user) {
        return user != null && user.canAccessPatientData();
    }
    
    private boolean canRemoveSpecificPatient(Patient patientToRemove, AuthenticatedUser currentUser) {
        // No permitir eliminar pacientes con facturas pendientes, órdenes activas, etc.
        return true;
    }
    
    // =============================================================================
    // MÉTODOS DE LOGGING Y AUDITORÍA
    // =============================================================================
    
    private void logPatientRegistered(Patient createdPatient, AuthenticatedUser currentUser) {
        System.out.printf("PATIENT REGISTERED: %s registered patient %s (%s) at %s%n",
            currentUser.getFullName(), createdPatient.getFullName(), createdPatient.getIdCard(),
            java.time.LocalDateTime.now());
    }
    
    private void logPatientUpdated(Patient oldPatient, Patient updatedPatient, AuthenticatedUser currentUser) {
        System.out.printf("PATIENT UPDATED: %s updated patient %s (%s) at %s%n",
            currentUser.getFullName(), updatedPatient.getFullName(), updatedPatient.getIdCard(),
            java.time.LocalDateTime.now());
    }
    
    private void logPatientRemoved(Patient removedPatient, AuthenticatedUser currentUser) {
        System.out.printf("PATIENT REMOVED: %s removed patient %s (%s) at %s%n",
            currentUser.getFullName(), removedPatient.getFullName(), removedPatient.getIdCard(),
            java.time.LocalDateTime.now());
    }
    
    private void logPatientsListed(int count, AuthenticatedUser currentUser) {
        System.out.printf("PATIENTS LISTED: %s retrieved %d patients at %s%n",
            currentUser.getFullName(), count, java.time.LocalDateTime.now());
    }
    
    private void logPatientViewed(Patient viewedPatient, AuthenticatedUser currentUser) {
        System.out.printf("PATIENT VIEWED: %s viewed patient %s (%s) at %s%n",
            currentUser.getFullName(), viewedPatient.getFullName(), viewedPatient.getIdCard(),
            java.time.LocalDateTime.now());
    }
    
    private void logOwnPatientInfoViewed(Patient patient) {
        System.out.printf("OWN PATIENT INFO VIEWED: Patient %s (%s) viewed own information at %s%n",
            patient.getFullName(), patient.getIdCard(), java.time.LocalDateTime.now());
    }
    
    private void logUnauthorizedAccess(AuthenticatedUser user, String operation) {
        System.err.printf("UNAUTHORIZED ACCESS: User %s (%s) role %s attempted %s at %s%n",
            user.getFullName(), user.getIdCard(), user.getRole(), operation,
            java.time.LocalDateTime.now());
    }
    
    private void logValidationError(String operation, Exception e, AuthenticatedUser user) {
        System.err.printf("VALIDATION ERROR in %s by %s: %s at %s%n",
            operation, user.getFullName(), e.getMessage(), java.time.LocalDateTime.now());
    }
    
    private void logSystemError(String operation, Exception e, AuthenticatedUser user) {
        System.err.printf("SYSTEM ERROR in %s by %s: %s at %s%n",
            operation, user.getFullName(), e.getMessage(), java.time.LocalDateTime.now());
    }
}