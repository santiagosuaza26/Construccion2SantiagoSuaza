package app.application.mapper;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import app.application.dto.request.CreateOrderRequest;
import app.application.dto.request.UpdateClinicalHistoryRequest;
import app.application.dto.response.ClinicalHistoryResponse;
import app.application.dto.response.OrderResponse;
import app.domain.model.ClinicalHistoryEntry;
import app.domain.model.DiagnosticOrderItem;
import app.domain.model.MedicationOrderItem;
import app.domain.model.OrderHeader;
import app.domain.model.OrderItem;
import app.domain.model.OrderItemType;
import app.domain.model.ProcedureOrderItem;
import app.domain.model.VitalSigns;

/**
 * OrderMapper - Mapper profesional para órdenes médicas y historia clínica
 * 
 * RESPONSABILIDADES:
 * - Convertir CreateOrderRequest → OrderHeader + OrderItems + ClinicalHistoryEntry
 * - Convertir órdenes y items → OrderResponse
 * - Manejar diferentes tipos de items (medications, procedures, diagnostics)
 * - Validar reglas de negocio médicas
 * - Convertir signos vitales y historia clínica
 * 
 * REGLAS MÉDICAS IMPLEMENTADAS:
 * - Ayudas diagnósticas NO se mezclan con medicamentos/procedimientos
 * - Items únicos por orden con numeración secuencial
 * - Especialistas requeridos cuando sea necesario
 * - Órdenes únicas con máximo 6 dígitos
 * - Historia clínica estructurada por fechas
 */
@Component
public class OrderMapper {
    
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    
    /**
     * Convierte CreateOrderRequest → OrderHeader (Domain Model)
     * 
     * TRANSFORMACIONES:
     * - String date → LocalDate
     * - Validaciones de cédulas médico/paciente
     * - Generación de número de orden único
     * 
     * @param request DTO con información de la orden
     * @param generatedOrderNumber Número único generado por OrderFactory
     * @return OrderHeader domain model
     */
    public OrderHeader toOrderHeader(CreateOrderRequest request, String generatedOrderNumber) {
        if (request == null) {
            throw new IllegalArgumentException("CreateOrderRequest cannot be null");
        }
        
        if (generatedOrderNumber == null || generatedOrderNumber.isBlank()) {
            throw new IllegalArgumentException("Generated order number cannot be null");
        }
        
        try {
            // Validar y parsear fecha
            LocalDate orderDate = parseOrderDate(request.getOrderDate());
            
            // Validar cédulas
            validateDoctorIdCard(request.getDoctorIdCard());
            validatePatientIdCard(request.getPatientIdCard());
            
            return new OrderHeader(
                generatedOrderNumber,
                request.getPatientIdCard().trim(),
                request.getDoctorIdCard().trim(),
                orderDate
            );
            
        } catch (Exception e) {
            throw new IllegalArgumentException("Error creating OrderHeader: " + e.getMessage(), e);
        }
    }
    
    /**
     * Convierte items del request → Lista de MedicationOrderItems
     * 
     * VALIDACIONES:
     * - Items secuenciales empezando en 1
     * - Medicamentos válidos en inventario
     * - Costos positivos
     */
    public List<MedicationOrderItem> toMedicationOrderItems(String orderNumber, 
                                                            List<CreateOrderRequest.MedicationItemRequest> itemRequests) {
        if (itemRequests == null || itemRequests.isEmpty()) {
            return new ArrayList<>();
        }
        
        List<MedicationOrderItem> items = new ArrayList<>();
        
        for (int i = 0; i < itemRequests.size(); i++) {
            CreateOrderRequest.MedicationItemRequest itemRequest = itemRequests.get(i);
            int itemNumber = i + 1; // Items empiezan en 1
            
            validateMedicationItemRequest(itemRequest, itemNumber);
            
            MedicationOrderItem item = new MedicationOrderItem(
                orderNumber,
                itemNumber,
                itemRequest.getMedicationId().trim(),
                itemRequest.getMedicationName().trim(),
                itemRequest.getDosage() != null ? itemRequest.getDosage().trim() : null,
                itemRequest.getTreatmentDuration() != null ? itemRequest.getTreatmentDuration().trim() : null,
                itemRequest.getCost() != null ? itemRequest.getCost() : 0L
            );
            
            items.add(item);
        }
        
        return items;
    }
    
    /**
     * Convierte items del request → Lista de ProcedureOrderItems
     */
    public List<ProcedureOrderItem> toProcedureOrderItems(String orderNumber,
                                                        List<CreateOrderRequest.ProcedureItemRequest> itemRequests) {
        if (itemRequests == null || itemRequests.isEmpty()) {
            return new ArrayList<>();
        }
        
        List<ProcedureOrderItem> items = new ArrayList<>();
        
        for (int i = 0; i < itemRequests.size(); i++) {
            CreateOrderRequest.ProcedureItemRequest itemRequest = itemRequests.get(i);
            int itemNumber = i + 1;
            
            validateProcedureItemRequest(itemRequest, itemNumber);
            
            ProcedureOrderItem item = new ProcedureOrderItem(
                orderNumber,
                itemNumber,
                itemRequest.getProcedureId().trim(),
                itemRequest.getProcedureName().trim(),
                itemRequest.getQuantity() != null ? itemRequest.getQuantity() : 1,
                itemRequest.getFrequency() != null ? itemRequest.getFrequency().trim() : null,
                itemRequest.getSpecialistRequired() != null ? itemRequest.getSpecialistRequired() : false,
                itemRequest.getSpecialtyId() != null ? itemRequest.getSpecialtyId().trim() : null,
                itemRequest.getCost() != null ? itemRequest.getCost() : 0L
            );
            
            items.add(item);
        }
        
        return items;
    }
    
    /**
     * Convierte items del request → Lista de DiagnosticOrderItems
     */
    public List<DiagnosticOrderItem> toDiagnosticOrderItems(String orderNumber,
                                                            List<CreateOrderRequest.DiagnosticItemRequest> itemRequests) {
        if (itemRequests == null || itemRequests.isEmpty()) {
            return new ArrayList<>();
        }
        
        List<DiagnosticOrderItem> items = new ArrayList<>();
        
        for (int i = 0; i < itemRequests.size(); i++) {
            CreateOrderRequest.DiagnosticItemRequest itemRequest = itemRequests.get(i);
            int itemNumber = i + 1;
            
            validateDiagnosticItemRequest(itemRequest, itemNumber);
            
            DiagnosticOrderItem item = new DiagnosticOrderItem(
                orderNumber,
                itemNumber,
                itemRequest.getDiagnosticId().trim(),
                itemRequest.getDiagnosticName().trim(),
                itemRequest.getQuantity() != null ? itemRequest.getQuantity() : 1,
                itemRequest.getSpecialistRequired() != null ? itemRequest.getSpecialistRequired() : false,
                itemRequest.getSpecialtyId() != null ? itemRequest.getSpecialtyId().trim() : null,
                itemRequest.getCost() != null ? itemRequest.getCost() : 0L
            );
            
            items.add(item);
        }
        
        return items;
    }
    
    /**
     * Convierte signos vitales del request → VitalSigns domain model
     */
    public VitalSigns toVitalSigns(CreateOrderRequest.VitalSignsRequest vitalSignsRequest) {
        if (vitalSignsRequest == null) {
            return null;
        }
        
        try {
            return new VitalSigns(
                vitalSignsRequest.getBloodPressure() != null ? vitalSignsRequest.getBloodPressure().trim() : null,
                vitalSignsRequest.getTemperature() != null ? vitalSignsRequest.getTemperature() : 0.0,
                vitalSignsRequest.getPulse() != null ? vitalSignsRequest.getPulse() : 0,
                vitalSignsRequest.getOxygenLevel() != null ? vitalSignsRequest.getOxygenLevel() : 0
            );
        } catch (Exception e) {
            throw new IllegalArgumentException("Error creating VitalSigns: " + e.getMessage(), e);
        }
    }
    
    /**
     * Convierte request → ClinicalHistoryEntry para historia clínica
     */
    public ClinicalHistoryEntry toClinicalHistoryEntry(CreateOrderRequest request, List<String> orderNumbers) {
        if (request == null) {
            throw new IllegalArgumentException("CreateOrderRequest cannot be null");
        }
        
        try {
            LocalDate entryDate = parseOrderDate(request.getOrderDate());
            VitalSigns vitalSigns = toVitalSigns(request.getVitalSigns());
            
            return new ClinicalHistoryEntry(
                entryDate,
                request.getDoctorIdCard().trim(),
                request.getReason() != null ? request.getReason().trim() : "",
                request.getSymptoms() != null ? request.getSymptoms().trim() : "",
                request.getDiagnosis() != null ? request.getDiagnosis().trim() : "",
                vitalSigns,
                orderNumbers != null ? orderNumbers : new ArrayList<>()
            );
            
        } catch (Exception e) {
            throw new IllegalArgumentException("Error creating ClinicalHistoryEntry: " + e.getMessage(), e);
        }
    }
    
    /**
     * Convierte UpdateClinicalHistoryRequest → ClinicalHistoryEntry
     */
    public ClinicalHistoryEntry toClinicalHistoryEntry(UpdateClinicalHistoryRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("UpdateClinicalHistoryRequest cannot be null");
        }
        
        try {
            LocalDate entryDate = parseOrderDate(request.getEntryDate());
            VitalSigns vitalSigns = null;
            
            if (request.getVitalSigns() != null) {
                UpdateClinicalHistoryRequest.VitalSignsData vsData = request.getVitalSigns();
                vitalSigns = new VitalSigns(
                    vsData.getBloodPressure(),
                    vsData.getTemperature() != null ? vsData.getTemperature() : 0.0,
                    vsData.getPulse() != null ? vsData.getPulse() : 0,
                    vsData.getOxygenLevel() != null ? vsData.getOxygenLevel() : 0
                );
            }
            
            return new ClinicalHistoryEntry(
                entryDate,
                request.getDoctorIdCard().trim(),
                request.getReason() != null ? request.getReason().trim() : "",
                request.getSymptoms() != null ? request.getSymptoms().trim() : "",
                request.getDiagnosis() != null ? request.getDiagnosis().trim() : "",
                vitalSigns,
                request.getRelatedOrderNumbers() != null ? request.getRelatedOrderNumbers() : new ArrayList<>()
            );
            
        } catch (Exception e) {
            throw new IllegalArgumentException("Error creating ClinicalHistoryEntry from update request: " + e.getMessage(), e);
        }
    }
    
    /**
     * Convierte OrderHeader + OrderItems → OrderResponse completo
     */
    public OrderResponse toOrderResponse(OrderHeader header, List<OrderItem> items, 
                                        String patientName, String doctorName) {
        if (header == null) {
            throw new IllegalArgumentException("OrderHeader cannot be null");
        }
        
        try {
            OrderResponse response = new OrderResponse();
            
            // Información básica de la orden
            response.setOrderNumber(header.getOrderNumber());
            response.setPatientIdCard(header.getPatientIdCard());
            response.setPatientName(patientName != null ? patientName : "Unknown Patient");
            response.setDoctorIdCard(header.getDoctorIdCard());
            response.setDoctorName(doctorName != null ? doctorName : "Unknown Doctor");
            response.setCreationDate(header.getCreationDate().format(DATE_FORMATTER));
            
            // Procesar items y determinar tipo de orden
            if (items != null && !items.isEmpty()) {
                List<OrderResponse.OrderItemInfo> itemInfos = createOrderItemInfoList(items);
                response.setItems(itemInfos);
                response.setItemCount(items.size());
                response.setOrderType(determineOrderType(items));
                response.setTotalCost(calculateTotalCost(items));
            } else {
                response.setItems(new ArrayList<>());
                response.setItemCount(0);
                response.setOrderType("EMPTY");
                response.setTotalCost(0L);
            }
            
            // Estado por defecto
            response.setStatus("CREATED");
            
            return response;
            
        } catch (Exception e) {
            throw new IllegalArgumentException("Error creating OrderResponse: " + e.getMessage(), e);
        }
    }
    
    /**
     * Convierte lista de ClinicalHistoryEntry → ClinicalHistoryResponse
     */
    public ClinicalHistoryResponse toClinicalHistoryResponse(String patientIdCard, String patientName, 
                                                            int patientAge, String patientGender,
                                                            List<ClinicalHistoryEntry> entries) {
        if (entries == null) {
            entries = new ArrayList<>();
        }
        
        try {
            // Crear entradas de información
            List<ClinicalHistoryResponse.ClinicalHistoryEntryInfo> entryInfos = entries.stream()
                .map(this::toClinicalHistoryEntryInfo)
                .collect(Collectors.toList());
            
            // Calcular estadísticas
            int totalEntries = entries.size();
            String firstConsultation = entries.stream()
                .map(entry -> entry.getDate())
                .min(LocalDate::compareTo)
                .map(date -> date.format(DATE_FORMATTER))
                .orElse(null);
            
            String lastConsultation = entries.stream()
                .map(entry -> entry.getDate())
                .max(LocalDate::compareTo)
                .map(date -> date.format(DATE_FORMATTER))
                .orElse(null);
            
            int consultationsThisYear = (int) entries.stream()
                .filter(entry -> entry.getDate().getYear() == LocalDate.now().getYear())
                .count();
            
            return new ClinicalHistoryResponse(
                patientIdCard, patientName, patientAge, patientGender,
                entryInfos, totalEntries, firstConsultation, lastConsultation, consultationsThisYear
            );
            
        } catch (Exception e) {
            throw new IllegalArgumentException("Error creating ClinicalHistoryResponse: " + e.getMessage(), e);
        }
    }
    
    // =============================================================================
    // MÉTODOS PRIVADOS DE UTILIDAD
    // =============================================================================
    
    private LocalDate parseOrderDate(String dateString) {
        if (dateString == null || dateString.isBlank()) {
            throw new IllegalArgumentException("Order date is required");
        }
        
        try {
            LocalDate date = LocalDate.parse(dateString, DATE_FORMATTER);
            
            // Validar que la fecha no sea muy antigua ni futura
            if (date.isAfter(LocalDate.now().plusDays(1))) {
                throw new IllegalArgumentException("Order date cannot be in the future");
            }
            
            if (date.isBefore(LocalDate.now().minusYears(1))) {
                throw new IllegalArgumentException("Order date cannot be more than 1 year old");
            }
            
            return date;
            
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid order date format. Use DD/MM/YYYY", e);
        }
    }
    
    private void validateDoctorIdCard(String doctorIdCard) {
        if (doctorIdCard == null || !doctorIdCard.matches("\\d{1,10}")) {
            throw new IllegalArgumentException("Doctor ID card must be numeric with maximum 10 digits");
        }
    }
    
    private void validatePatientIdCard(String patientIdCard) {
        if (patientIdCard == null || !patientIdCard.matches("\\d+")) {
            throw new IllegalArgumentException("Patient ID card must contain only numbers");
        }
    }
    
    private void validateMedicationItemRequest(CreateOrderRequest.MedicationItemRequest itemRequest, int itemNumber) {
        if (itemRequest == null) {
            throw new IllegalArgumentException("Medication item " + itemNumber + " cannot be null");
        }
        
        if (itemRequest.getMedicationId() == null || itemRequest.getMedicationId().isBlank()) {
            throw new IllegalArgumentException("Medication ID is required for item " + itemNumber);
        }
        
        if (itemRequest.getMedicationName() == null || itemRequest.getMedicationName().isBlank()) {
            throw new IllegalArgumentException("Medication name is required for item " + itemNumber);
        }
        
        if (itemRequest.getCost() != null && itemRequest.getCost() < 0) {
            throw new IllegalArgumentException("Medication cost cannot be negative for item " + itemNumber);
        }
    }
    
    private void validateProcedureItemRequest(CreateOrderRequest.ProcedureItemRequest itemRequest, int itemNumber) {
        if (itemRequest == null) {
            throw new IllegalArgumentException("Procedure item " + itemNumber + " cannot be null");
        }
        
        if (itemRequest.getProcedureId() == null || itemRequest.getProcedureId().isBlank()) {
            throw new IllegalArgumentException("Procedure ID is required for item " + itemNumber);
        }
        
        if (itemRequest.getProcedureName() == null || itemRequest.getProcedureName().isBlank()) {
            throw new IllegalArgumentException("Procedure name is required for item " + itemNumber);
        }
        
        if (itemRequest.getQuantity() != null && itemRequest.getQuantity() < 1) {
            throw new IllegalArgumentException("Procedure quantity must be at least 1 for item " + itemNumber);
        }
        
        if (itemRequest.getCost() != null && itemRequest.getCost() < 0) {
            throw new IllegalArgumentException("Procedure cost cannot be negative for item " + itemNumber);
        }
        
        // Validar especialista si es requerido
        if (itemRequest.getSpecialistRequired() != null && itemRequest.getSpecialistRequired()) {
            if (itemRequest.getSpecialtyId() == null || itemRequest.getSpecialtyId().isBlank()) {
                throw new IllegalArgumentException("Specialty ID is required when specialist is needed for item " + itemNumber);
            }
        }
    }
    
    private void validateDiagnosticItemRequest(CreateOrderRequest.DiagnosticItemRequest itemRequest, int itemNumber) {
        if (itemRequest == null) {
            throw new IllegalArgumentException("Diagnostic item " + itemNumber + " cannot be null");
        }
        
        if (itemRequest.getDiagnosticId() == null || itemRequest.getDiagnosticId().isBlank()) {
            throw new IllegalArgumentException("Diagnostic ID is required for item " + itemNumber);
        }
        
        if (itemRequest.getDiagnosticName() == null || itemRequest.getDiagnosticName().isBlank()) {
            throw new IllegalArgumentException("Diagnostic name is required for item " + itemNumber);
        }
        
        if (itemRequest.getQuantity() != null && itemRequest.getQuantity() < 1) {
            throw new IllegalArgumentException("Diagnostic quantity must be at least 1 for item " + itemNumber);
        }
        
        if (itemRequest.getCost() != null && itemRequest.getCost() < 0) {
            throw new IllegalArgumentException("Diagnostic cost cannot be negative for item " + itemNumber);
        }
        
        // Validar especialista si es requerido
        if (itemRequest.getSpecialistRequired() != null && itemRequest.getSpecialistRequired()) {
            if (itemRequest.getSpecialtyId() == null || itemRequest.getSpecialtyId().isBlank()) {
                throw new IllegalArgumentException("Specialty ID is required when specialist is needed for item " + itemNumber);
            }
        }
    }
    
    private List<OrderResponse.OrderItemInfo> createOrderItemInfoList(List<OrderItem> items) {
        return items.stream()
            .map(this::toOrderItemInfo)
            .collect(Collectors.toList());
    }
    
    private OrderResponse.OrderItemInfo toOrderItemInfo(OrderItem item) {
        OrderResponse.OrderItemInfo info = new OrderResponse.OrderItemInfo();
        info.setItemNumber(item.getItemNumber());
        info.setItemType(item.getType().name());
        
        if (item instanceof MedicationOrderItem) {
            MedicationOrderItem med = (MedicationOrderItem) item;
            info.setItemId(med.getMedicationId());
            info.setItemName(med.getMedicationName());
            info.setDosage(med.getDosage());
            info.setDuration(med.getTreatmentDuration());
            info.setCost(med.getCost());
        } else if (item instanceof ProcedureOrderItem) {
            ProcedureOrderItem proc = (ProcedureOrderItem) item;
            info.setItemId(proc.getProcedureId());
            info.setItemName(proc.getProcedureName());
            info.setQuantity(proc.getQuantity());
            info.setFrequency(proc.getFrequency());
            info.setSpecialistRequired(proc.isSpecialistRequired());
            info.setSpecialtyId(proc.getSpecialtyId());
            info.setCost(proc.getCost());
        } else if (item instanceof DiagnosticOrderItem) {
            DiagnosticOrderItem diag = (DiagnosticOrderItem) item;
            info.setItemId(diag.getDiagnosticId());
            info.setItemName(diag.getDiagnosticName());
            info.setQuantity(diag.getQuantity());
            info.setSpecialistRequired(diag.isSpecialistRequired());
            info.setSpecialtyId(diag.getSpecialtyId());
            info.setCost(diag.getCost());
        }
        
        return info;
    }
    
    private String determineOrderType(List<OrderItem> items) {
        if (items.isEmpty()) return "EMPTY";
        
        OrderItemType firstType = items.get(0).getType();
        return firstType.name();
    }
    
    private long calculateTotalCost(List<OrderItem> items) {
        return items.stream()
            .mapToLong(item -> {
                if (item instanceof MedicationOrderItem) {
                    return ((MedicationOrderItem) item).getCost();
                } else if (item instanceof ProcedureOrderItem) {
                    return ((ProcedureOrderItem) item).getCost();
                } else if (item instanceof DiagnosticOrderItem) {
                    return ((DiagnosticOrderItem) item).getCost();
                }
                return 0L;
            })
            .sum();
    }
    
    private ClinicalHistoryResponse.ClinicalHistoryEntryInfo toClinicalHistoryEntryInfo(ClinicalHistoryEntry entry) {
        ClinicalHistoryResponse.ClinicalHistoryEntryInfo info = new ClinicalHistoryResponse.ClinicalHistoryEntryInfo();
        
        info.setEntryDate(entry.getDate().format(DATE_FORMATTER));
        info.setDoctorIdCard(entry.getDoctorIdCard());
        info.setDoctorName("Dr. " + entry.getDoctorIdCard()); // En implementación real sería lookup
        info.setReason(entry.getReason());
        info.setSymptoms(entry.getSymptoms());
        info.setDiagnosis(entry.getDiagnosis());
        info.setRelatedOrderNumbers(entry.getRelatedOrderNumbers());
        
        // Convertir signos vitales si existen
        if (entry.getVitalSigns() != null) {
            VitalSigns vs = entry.getVitalSigns();
            ClinicalHistoryResponse.VitalSignsInfo vsInfo = new ClinicalHistoryResponse.VitalSignsInfo(
                vs.getBloodPressure(),
                vs.getTemperature(),
                vs.getPulse(),
                vs.getOxygenLevel(),
                "Nurse/Doctor", // En implementación real sería lookup
                entry.getDate().format(DATE_FORMATTER)
            );
            info.setVitalSigns(vsInfo);
        }
        
        // Determinar tipo de entrada basado en el contenido
        if (entry.getDiagnosis() != null && !entry.getDiagnosis().isBlank()) {
            if (entry.getRelatedOrderNumbers() != null && !entry.getRelatedOrderNumbers().isEmpty()) {
                info.setEntryType("CONSULTATION");
            } else {
                info.setEntryType("FOLLOW_UP");
            }
        } else {
            info.setEntryType("CONSULTATION");
        }
        
        return info;
    }
    
    // =============================================================================
    // MÉTODOS DE UTILIDAD PARA VALIDACIONES Y TESTING
    // =============================================================================
    
    /**
     * Validar CreateOrderRequest con reglas médicas completas
     */
    public void validateCreateOrderRequest(CreateOrderRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("CreateOrderRequest cannot be null");
        }
        
        // Validar que no se mezclen tipos de items (regla médica crítica)
        if (request.hasMultipleItemTypes()) {
            throw new IllegalArgumentException("Cannot mix diagnostic aids with medications or procedures in same order");
        }
        
        // Validar que tenga al menos un tipo de item
        if (!request.hasMedications() && !request.hasProcedures() && !request.hasDiagnostics()) {
            throw new IllegalArgumentException("Order must contain at least one item (medication, procedure, or diagnostic)");
        }
        
        // Validar información clínica mínima
        if (request.getReason() == null || request.getReason().isBlank()) {
            throw new IllegalArgumentException("Reason for consultation is required");
        }
    }
    
    /**
     * Crear orden de prueba para testing
     */
    public CreateOrderRequest createTestOrderRequest(String patientIdCard, String doctorIdCard, String orderType) {
        CreateOrderRequest request = new CreateOrderRequest(
            patientIdCard,
            doctorIdCard,
            LocalDate.now().format(DATE_FORMATTER),
            "Test consultation",
            "Test symptoms",
            "Test diagnosis"
        );
        
        // Agregar items según el tipo solicitado
        if ("MEDICATION".equals(orderType)) {
            CreateOrderRequest.MedicationItemRequest medItem = new CreateOrderRequest.MedicationItemRequest(
                "MED001", "Test Medication", "1 tablet", "7 days", 15000L
            );
            request.setMedications(List.of(medItem));
        } else if ("PROCEDURE".equals(orderType)) {
            CreateOrderRequest.ProcedureItemRequest procItem = new CreateOrderRequest.ProcedureItemRequest(
                "PROC001", "Test Procedure", 1, "Once", false, null, 50000L
            );
            request.setProcedures(List.of(procItem));
        } else if ("DIAGNOSTIC".equals(orderType)) {
            CreateOrderRequest.DiagnosticItemRequest diagItem = new CreateOrderRequest.DiagnosticItemRequest(
                "DIAG001", "Test Diagnostic", 1, false, null, 25000L
            );
            request.setDiagnostics(List.of(diagItem));
        }
        
        return request;
    }
}