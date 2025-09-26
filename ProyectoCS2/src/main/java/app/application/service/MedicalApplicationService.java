package app.application.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import app.application.dto.request.CreateOrderRequest;
import app.application.dto.request.UpdateClinicalHistoryRequest;
import app.application.dto.request.VitalSignsRequest;
import app.application.dto.response.ClinicalHistoryResponse;
import app.application.dto.response.CommonResponse;
import app.application.dto.response.OrderResponse;
import app.application.mapper.OrderMapper;
import app.domain.factory.OrderFactory;
import app.domain.model.ClinicalHistoryEntry;
import app.domain.model.DiagnosticOrderItem;
import app.domain.model.MedicationOrderItem;
import app.domain.model.OrderHeader;
import app.domain.model.OrderItem;
import app.domain.model.Patient;
import app.domain.model.PatientVisit;
import app.domain.model.ProcedureOrderItem;
import app.domain.model.Role;
import app.domain.model.VitalSigns;
import app.domain.port.ClinicalHistoryRepository;
import app.domain.port.OrderHeaderRepository;
import app.domain.port.OrderItemRepository;
import app.domain.port.PatientRepository;
import app.domain.port.UserRepository;
import app.domain.services.AuthenticationService.AuthenticatedUser;
import app.domain.services.DoctorService;
import app.domain.services.NurseService;
import app.domain.services.PatientVisitService;

@Service
public class MedicalApplicationService {
    
    private final DoctorService doctorService;
    private final NurseService nurseService;
    private final PatientVisitService patientVisitService;
    private final OrderFactory orderFactory;
    private final OrderMapper orderMapper;
    private final OrderHeaderRepository orderHeaderRepository;
    private final OrderItemRepository orderItemRepository;
    private final ClinicalHistoryRepository clinicalHistoryRepository;
    private final PatientRepository patientRepository;
    private final UserRepository userRepository;
    
    public MedicalApplicationService(DoctorService doctorService,
                                    NurseService nurseService,
                                    PatientVisitService patientVisitService,
                                    OrderFactory orderFactory,
                                    OrderMapper orderMapper,
                                    OrderHeaderRepository orderHeaderRepository,
                                    OrderItemRepository orderItemRepository,
                                    ClinicalHistoryRepository clinicalHistoryRepository,
                                    PatientRepository patientRepository,
                                    UserRepository userRepository) {
        this.doctorService = doctorService;
        this.nurseService = nurseService;
        this.patientVisitService = patientVisitService;
        this.orderFactory = orderFactory;
        this.orderMapper = orderMapper;
        this.orderHeaderRepository = orderHeaderRepository;
        this.orderItemRepository = orderItemRepository;
        this.clinicalHistoryRepository = clinicalHistoryRepository;
        this.patientRepository = patientRepository;
        this.userRepository = userRepository;
    }
    
    public CommonResponse<OrderResponse> createMedicalOrder(CreateOrderRequest request, AuthenticatedUser currentUser) {
        try {
            if (!canCreateOrders(currentUser)) {
                logUnauthorizedAccess(currentUser, "CREATE_MEDICAL_ORDER");
                return CommonResponse.error("Access denied - Only doctors can create medical orders", "MED_001");
            }
            
            validateCreateOrderRequest(request);
            orderMapper.validateCreateOrderRequest(request);
            
            String generatedOrderNumber = orderFactory.generateUniqueOrderNumber();
            OrderHeader orderHeader = orderMapper.toOrderHeader(request, generatedOrderNumber);
            OrderHeader createdHeader = doctorService.createOrderHeader(
                orderHeader.getOrderNumber(),
                orderHeader.getPatientIdCard(),
                orderHeader.getDoctorIdCard(),
                orderHeader.getCreationDate()
            );
            
            List<OrderItem> allItems = new ArrayList<>();
            
            if (request.hasMedications()) {
                List<MedicationOrderItem> medicationItems = orderMapper.toMedicationOrderItems(
                    generatedOrderNumber, request.getMedications()
                );
                doctorService.addMedicationItems(generatedOrderNumber, medicationItems);
                allItems.addAll(medicationItems);
            }
            
            if (request.hasProcedures()) {
                List<ProcedureOrderItem> procedureItems = orderMapper.toProcedureOrderItems(
                    generatedOrderNumber, request.getProcedures()
                );
                doctorService.addProcedureItems(generatedOrderNumber, procedureItems);
                allItems.addAll(procedureItems);
            }
            
            if (request.hasDiagnostics()) {
                List<DiagnosticOrderItem> diagnosticItems = orderMapper.toDiagnosticOrderItems(
                    generatedOrderNumber, request.getDiagnostics()
                );
                doctorService.addDiagnosticItems(generatedOrderNumber, diagnosticItems);
                allItems.addAll(diagnosticItems);
            }
            
            ClinicalHistoryEntry historyEntry = orderMapper.toClinicalHistoryEntry(
                request, List.of(generatedOrderNumber)
            );
            doctorService.appendClinicalHistory(request.getPatientIdCard(), historyEntry);
            
            String patientName = getPatientName(request.getPatientIdCard());
            String doctorName = getDoctorName(request.getDoctorIdCard());
            
            OrderResponse orderResponse = orderMapper.toOrderResponse(
                createdHeader, allItems, patientName, doctorName
            );
            
            logMedicalOrderCreated(createdHeader, allItems.size(), currentUser);
            
            return CommonResponse.success("Medical order created successfully", orderResponse);
            
        } catch (IllegalArgumentException e) {
            logValidationError("createMedicalOrder", e, currentUser);
            return CommonResponse.error(e.getMessage(), "MED_002");
        } catch (Exception e) {
            logSystemError("createMedicalOrder", e, currentUser);
            return CommonResponse.error("Internal error creating medical order", "MED_003");
        }
    }
    
    public CommonResponse<String> updateClinicalHistory(UpdateClinicalHistoryRequest request, AuthenticatedUser currentUser) {
        try {
            if (!canUpdateClinicalHistory(currentUser)) {
                logUnauthorizedAccess(currentUser, "UPDATE_CLINICAL_HISTORY");
                return CommonResponse.error("Access denied - Only doctors can update clinical history", "MED_004");
            }
            
            validateUpdateClinicalHistoryRequest(request);
            
            ClinicalHistoryEntry historyEntry = orderMapper.toClinicalHistoryEntry(request);
            doctorService.appendClinicalHistory(request.getPatientIdCard(), historyEntry);
            
            logClinicalHistoryUpdated(request.getPatientIdCard(), currentUser);
            
            return CommonResponse.success("Clinical history updated successfully");
            
        } catch (IllegalArgumentException e) {
            logValidationError("updateClinicalHistory", e, currentUser);
            return CommonResponse.error(e.getMessage(), "MED_005");
        } catch (Exception e) {
            logSystemError("updateClinicalHistory", e, currentUser);
            return CommonResponse.error("Internal error updating clinical history", "MED_006");
        }
    }
    
    public CommonResponse<ClinicalHistoryResponse> getClinicalHistory(String patientIdCard, AuthenticatedUser currentUser) {
        try {
            if (!canAccessClinicalHistory(currentUser)) {
                logUnauthorizedAccess(currentUser, "VIEW_CLINICAL_HISTORY");
                return CommonResponse.error("Access denied - Cannot access clinical history", "MED_007");
            }
            
            if (patientIdCard == null || patientIdCard.isBlank()) {
                return CommonResponse.error("Patient ID card is required", "MED_008");
            }
            
            if (currentUser.getRole() == Role.PATIENT && !currentUser.getIdCard().equals(patientIdCard)) {
                return CommonResponse.error("Patients can only access their own clinical history", "MED_009");
            }
            
            List<ClinicalHistoryEntry> entries = clinicalHistoryRepository.findByPatient(patientIdCard);
            
            String patientName = getPatientName(patientIdCard);
            Patient patient = patientRepository.findByIdCard(patientIdCard).orElse(null);
            int patientAge = patient != null ? java.time.Period.between(patient.getBirthDate(), LocalDate.now()).getYears() : 0;
            String patientGender = patient != null ? patient.getGender() : "Unknown";
            
            ClinicalHistoryResponse historyResponse = orderMapper.toClinicalHistoryResponse(
                patientIdCard, patientName, patientAge, patientGender, entries
            );
            
            logClinicalHistoryViewed(patientIdCard, currentUser);
            
            return CommonResponse.success("Clinical history retrieved", historyResponse);
            
        } catch (Exception e) {
            logSystemError("getClinicalHistory", e, currentUser);
            return CommonResponse.error("Internal error retrieving clinical history", "MED_010");
        }
    }
    
    public CommonResponse<String> recordVitalSigns(VitalSignsRequest request, AuthenticatedUser currentUser) {
        try {
            if (!canRecordVitalSigns(currentUser)) {
                logUnauthorizedAccess(currentUser, "RECORD_VITAL_SIGNS");
                return CommonResponse.error("Access denied - Only nurses and doctors can record vital signs", "MED_011");
            }
            
            validateVitalSignsRequest(request);
            
            VitalSigns vitalSigns = new VitalSigns(
                request.getBloodPressure(),
                request.getTemperature(),
                request.getPulse(),
                request.getOxygenLevel()
            );
            
            nurseService.recordVitalSigns(
                request.getPatientIdCard(),
                request.getNurseIdCard(),
                vitalSigns
            );
            
            if (request.hasRelatedOrder()) {
                PatientVisit visit = patientVisitService.recordVisit(
                    request.getPatientIdCard(),
                    request.getNurseIdCard(),
                    currentUser.getRole(),
                    request.getNotes(),
                    vitalSigns,
                    request.getRelatedOrderNumber()
                );
                
                logPatientVisitRecorded(visit, currentUser);
            }
            
            logVitalSignsRecorded(request.getPatientIdCard(), currentUser);
            
            return CommonResponse.success("Vital signs recorded successfully");
            
        } catch (IllegalArgumentException e) {
            logValidationError("recordVitalSigns", e, currentUser);
            return CommonResponse.error(e.getMessage(), "MED_012");
        } catch (Exception e) {
            logSystemError("recordVitalSigns", e, currentUser);
            return CommonResponse.error("Internal error recording vital signs", "MED_013");
        }
    }
    
    public CommonResponse<OrderResponse> getOrderById(String orderNumber, AuthenticatedUser currentUser) {
        try {
            if (!canViewOrders(currentUser)) {
                logUnauthorizedAccess(currentUser, "VIEW_ORDER");
                return CommonResponse.error("Access denied - Cannot view medical orders", "MED_014");
            }
            
            if (orderNumber == null || orderNumber.isBlank()) {
                return CommonResponse.error("Order number is required", "MED_015");
            }
            
            OrderHeader orderHeader = orderHeaderRepository.findByNumber(orderNumber)
                .orElseThrow(() -> new IllegalArgumentException("Order not found: " + orderNumber));
            
            if (currentUser.getRole() == Role.PATIENT && !currentUser.getIdCard().equals(orderHeader.getPatientIdCard())) {
                return CommonResponse.error("Patients can only view their own orders", "MED_016");
            }
            
            List<OrderItem> orderItems = orderItemRepository.findByOrderNumber(orderNumber);
            
            String patientName = getPatientName(orderHeader.getPatientIdCard());
            String doctorName = getDoctorName(orderHeader.getDoctorIdCard());
            
            OrderResponse orderResponse = orderMapper.toOrderResponse(
                orderHeader, orderItems, patientName, doctorName
            );
            
            logOrderViewed(orderNumber, currentUser);
            
            return CommonResponse.success("Order retrieved successfully", orderResponse);
            
        } catch (IllegalArgumentException e) {
            return CommonResponse.error(e.getMessage(), "MED_017");
        } catch (Exception e) {
            logSystemError("getOrderById", e, currentUser);
            return CommonResponse.error("Internal error retrieving order", "MED_018");
        }
    }
    
    public CommonResponse<String> processFollowUpConsultation(String patientIdCard, String doctorIdCard, 
                                                                String diagnosis, List<String> relatedOrderNumbers,
                                                                AuthenticatedUser currentUser) {
        try {
            if (!canCreateOrders(currentUser)) {
                logUnauthorizedAccess(currentUser, "PROCESS_FOLLOWUP");
                return CommonResponse.error("Access denied - Only doctors can process follow-up consultations", "MED_019");
            }
            
            if (patientIdCard == null || patientIdCard.isBlank()) {
                return CommonResponse.error("Patient ID card is required", "MED_020");
            }
            
            if (diagnosis == null || diagnosis.isBlank()) {
                return CommonResponse.error("Diagnosis is required for follow-up consultation", "MED_021");
            }
            
            doctorService.createFollowUpConsultation(patientIdCard, doctorIdCard, diagnosis, relatedOrderNumbers);
            
            logFollowUpProcessed(patientIdCard, currentUser);
            
            return CommonResponse.success("Follow-up consultation processed successfully");
            
        } catch (Exception e) {
            logSystemError("processFollowUpConsultation", e, currentUser);
            return CommonResponse.error("Internal error processing follow-up consultation", "MED_022");
        }
    }
    
    // =============================================================================
    // MÉTODOS PRIVADOS DE VALIDACIÓN
    // =============================================================================
    
    private void validateCreateOrderRequest(CreateOrderRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("Create order request cannot be null");
        }
        
        if (request.hasMultipleItemTypes()) {
            throw new IllegalArgumentException("Cannot mix diagnostic aids with medications or procedures");
        }
        
        if (!request.hasMedications() && !request.hasProcedures() && !request.hasDiagnostics()) {
            throw new IllegalArgumentException("Order must contain at least one item");
        }
    }
    
    private void validateUpdateClinicalHistoryRequest(UpdateClinicalHistoryRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("Clinical history update request cannot be null");
        }
        
        if (request.getPatientIdCard() == null || request.getPatientIdCard().isBlank()) {
            throw new IllegalArgumentException("Patient ID card is required");
        }
        
        if (request.getDoctorIdCard() == null || request.getDoctorIdCard().isBlank()) {
            throw new IllegalArgumentException("Doctor ID card is required");
        }
        
        if (request.getReason() == null || request.getReason().isBlank()) {
            throw new IllegalArgumentException("Reason for consultation is required");
        }
    }
    
    private void validateVitalSignsRequest(VitalSignsRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("Vital signs request cannot be null");
        }
        
        if (request.getPatientIdCard() == null || request.getPatientIdCard().isBlank()) {
            throw new IllegalArgumentException("Patient ID card is required");
        }
        
        if (request.getNurseIdCard() == null || request.getNurseIdCard().isBlank()) {
            throw new IllegalArgumentException("Staff ID card is required");
        }
        
        if (request.getTemperature() != null && (request.getTemperature() < 30 || request.getTemperature() > 45)) {
            throw new IllegalArgumentException("Invalid temperature range");
        }
        
        if (request.getPulse() != null && (request.getPulse() < 30 || request.getPulse() > 250)) {
            throw new IllegalArgumentException("Invalid pulse range");
        }
        
        if (request.getOxygenLevel() != null && (request.getOxygenLevel() < 0 || request.getOxygenLevel() > 100)) {
            throw new IllegalArgumentException("Invalid oxygen level range");
        }
    }
    
    // =============================================================================
    // MÉTODOS PRIVADOS DE AUTORIZACIÓN
    // =============================================================================
    
    private boolean canCreateOrders(AuthenticatedUser user) {
        return user != null && user.getRole() == Role.DOCTOR && user.canCreateOrders();
    }
    
    private boolean canUpdateClinicalHistory(AuthenticatedUser user) {
        return user != null && user.getRole() == Role.DOCTOR;
    }
    
    private boolean canAccessClinicalHistory(AuthenticatedUser user) {
        return user != null && (user.canAccessClinicalHistory() || user.getRole() == Role.PATIENT);
    }
    
    private boolean canRecordVitalSigns(AuthenticatedUser user) {
        return user != null && user.canRecordVitalSigns();
    }
    
    private boolean canViewOrders(AuthenticatedUser user) {
        return user != null && (user.getRole() == Role.DOCTOR || user.getRole() == Role.NURSE || 
                                user.getRole() == Role.ADMINISTRATIVE || user.getRole() == Role.PATIENT);
    }
    
    // =============================================================================
    // MÉTODOS PRIVADOS DE UTILIDAD
    // =============================================================================
    
    private String getPatientName(String patientIdCard) {
        return patientRepository.findByIdCard(patientIdCard)
            .map(Patient::getFullName)
            .orElse("Unknown Patient");
    }
    
    private String getDoctorName(String doctorIdCard) {
        return userRepository.findByIdCard(doctorIdCard)
            .map(user -> "Dr. " + user.getFullName())
            .orElse("Unknown Doctor");
    }
    
    // =============================================================================
    // MÉTODOS DE LOGGING Y AUDITORÍA
    // =============================================================================
    
    private void logMedicalOrderCreated(OrderHeader header, int itemCount, AuthenticatedUser currentUser) {
        System.out.printf("MEDICAL ORDER CREATED: Dr. %s created order %s with %d items for patient %s at %s%n",
            currentUser.getFullName(), header.getOrderNumber(), itemCount, header.getPatientIdCard(),
            java.time.LocalDateTime.now());
    }
    
    private void logClinicalHistoryUpdated(String patientIdCard, AuthenticatedUser currentUser) {
        System.out.printf("CLINICAL HISTORY UPDATED: Dr. %s updated clinical history for patient %s at %s%n",
            currentUser.getFullName(), patientIdCard, java.time.LocalDateTime.now());
    }
    
    private void logClinicalHistoryViewed(String patientIdCard, AuthenticatedUser currentUser) {
        System.out.printf("CLINICAL HISTORY VIEWED: %s (%s) viewed clinical history for patient %s at %s%n",
            currentUser.getFullName(), currentUser.getRole(), patientIdCard, java.time.LocalDateTime.now());
    }
    
    private void logVitalSignsRecorded(String patientIdCard, AuthenticatedUser currentUser) {
        System.out.printf("VITAL SIGNS RECORDED: %s recorded vital signs for patient %s at %s%n",
            currentUser.getFullName(), patientIdCard, java.time.LocalDateTime.now());
    }
    
    private void logPatientVisitRecorded(PatientVisit visit, AuthenticatedUser currentUser) {
        System.out.printf("PATIENT VISIT RECORDED: %s recorded visit %s for patient %s at %s%n",
            currentUser.getFullName(), visit.getVisitId(), visit.getPatientIdCard(),
            java.time.LocalDateTime.now());
    }
    
    private void logOrderViewed(String orderNumber, AuthenticatedUser currentUser) {
        System.out.printf("ORDER VIEWED: %s (%s) viewed order %s at %s%n",
            currentUser.getFullName(), currentUser.getRole(), orderNumber, java.time.LocalDateTime.now());
    }
    
    private void logFollowUpProcessed(String patientIdCard, AuthenticatedUser currentUser) {
        System.out.printf("FOLLOWUP PROCESSED: Dr. %s processed follow-up for patient %s at %s%n",
            currentUser.getFullName(), patientIdCard, java.time.LocalDateTime.now());
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