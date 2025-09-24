package app.domain.services;

import java.time.LocalDate;
import java.util.List;

import app.domain.model.ClinicalHistoryEntry;
import app.domain.model.DiagnosticOrderItem;
import app.domain.model.MedicationOrderItem;
import app.domain.model.OrderHeader;
import app.domain.model.OrderItem;
import app.domain.model.ProcedureOrderItem;
import app.domain.port.ClinicalHistoryRepository;
import app.domain.port.DiagnosticTestRepository;
import app.domain.port.MedicationRepository;
import app.domain.port.OrderHeaderRepository;
import app.domain.port.OrderItemRepository;
import app.domain.port.ProcedureTypeRepository;
import app.domain.port.SpecialtyRepository;

public class DoctorService {
    private final OrderHeaderRepository orderHeaderRepository;
    private final OrderItemRepository orderItemRepository;
    private final ClinicalHistoryRepository clinicalHistoryRepository;
    private final MedicationRepository medicationRepository;
    private final ProcedureTypeRepository procedureTypeRepository;
    private final DiagnosticTestRepository diagnosticTestRepository;
    private final SpecialtyRepository specialtyRepository;
    private final OrderValidationService orderValidationService;

    public DoctorService(OrderHeaderRepository orderHeaderRepository,
                        OrderItemRepository orderItemRepository,
                        ClinicalHistoryRepository clinicalHistoryRepository,
                        MedicationRepository medicationRepository,
                        ProcedureTypeRepository procedureTypeRepository,
                        DiagnosticTestRepository diagnosticTestRepository,
                        SpecialtyRepository specialtyRepository,
                        OrderValidationService orderValidationService) {
        this.orderHeaderRepository = orderHeaderRepository;
        this.orderItemRepository = orderItemRepository;
        this.clinicalHistoryRepository = clinicalHistoryRepository;
        this.medicationRepository = medicationRepository;
        this.procedureTypeRepository = procedureTypeRepository;
        this.diagnosticTestRepository = diagnosticTestRepository;
        this.specialtyRepository = specialtyRepository;
        this.orderValidationService = orderValidationService;
    }

    /**
     * CREACIÓN DE ORDEN MÉDICA SEGÚN REQUERIMIENTOS:
     * - Número de orden único (máximo 6 dígitos)
     * - Cédula del paciente
     * - Cédula del médico (máximo 10 dígitos)
     * - Fecha de creación
     */
    public OrderHeader createOrderHeader(String orderNumber, String patientIdCard, String doctorIdCard, LocalDate date) {
        validateDoctorIdCard(doctorIdCard);
        
        if (orderHeaderRepository.existsOrderNumber(orderNumber)) {
            throw new IllegalArgumentException("Order number must be unique: " + orderNumber);
        }
        
        OrderHeader header = new OrderHeader(orderNumber, patientIdCard, doctorIdCard, date);
        return orderHeaderRepository.save(header);
    }

    /**
     * AGREGAR MEDICAMENTOS A LA ORDEN
     * Validaciones según documento:
     * - Verificar que el medicamento existe en inventario
     * - Números de ítem únicos
     * - Si hay varios medicamentos, todos en la misma orden
     */
    public void addMedicationItems(String orderNumber, List<MedicationOrderItem> items) {
        validateItemsNotEmpty(items, "medications");
        validateItemNumbersUnique(items.stream().map(MedicationOrderItem::getItemNumber).toList());
        
        for (MedicationOrderItem item : items) {
            // Verificar que el medicamento existe en el inventario
            medicationRepository.findById(item.getMedicationId())
                .orElseThrow(() -> new IllegalArgumentException("Medication not found in inventory: " + item.getMedicationId()));
            
            // Validar que pertenece a la orden correcta
            if (!item.getOrderNumber().equals(orderNumber)) {
                throw new IllegalArgumentException("Item order number doesn't match: " + item.getOrderNumber());
            }
            
            orderItemRepository.saveMedicationItem(item);
        }
    }

    /**
     * AGREGAR PROCEDIMIENTOS A LA ORDEN
     * Validaciones según documento:
     * - Verificar que el procedimiento existe en inventario
     * - Si requiere especialista, debe especificar la especialidad
     * - Números de ítem únicos
     */
    public void addProcedureItems(String orderNumber, List<ProcedureOrderItem> items) {
        validateItemsNotEmpty(items, "procedures");
        validateItemNumbersUnique(items.stream().map(ProcedureOrderItem::getItemNumber).toList());
        
        for (ProcedureOrderItem item : items) {
            // Verificar que el procedimiento existe en el inventario
            procedureTypeRepository.findById(item.getProcedureId())
                .orElseThrow(() -> new IllegalArgumentException("Procedure not found in inventory: " + item.getProcedureId()));
            
            // Validar especialista si es requerido
            if (item.isSpecialistRequired()) {
                validateSpecialtyExists(item.getSpecialtyId());
            }
            
            // Validar que pertenece a la orden correcta
            if (!item.getOrderNumber().equals(orderNumber)) {
                throw new IllegalArgumentException("Item order number doesn't match: " + item.getOrderNumber());
            }
            
            orderItemRepository.saveProcedureItem(item);
        }
    }

    /**
     * AGREGAR AYUDAS DIAGNÓSTICAS A LA ORDEN
     * Validaciones según documento:
     * - Verificar que la ayuda diagnóstica existe en inventario
     * - Si requiere especialista, debe especificar la especialidad
     * - No puede mezclarse con medicamentos o procedimientos
     */
    public void addDiagnosticItems(String orderNumber, List<DiagnosticOrderItem> items) {
        validateItemsNotEmpty(items, "diagnostic aids");
        validateItemNumbersUnique(items.stream().map(DiagnosticOrderItem::getItemNumber).toList());
        
        // Verificar que no hay medicamentos o procedimientos en esta orden
        List<OrderItem> existingItems = orderItemRepository.findByOrderNumber(orderNumber);
        boolean hasMedicationOrProcedure = existingItems.stream()
            .anyMatch(item -> item instanceof MedicationOrderItem || item instanceof ProcedureOrderItem);
            
        if (hasMedicationOrProcedure) {
            throw new IllegalArgumentException(
                "Cannot add diagnostic aids to order that already contains medications or procedures");
        }
        
        for (DiagnosticOrderItem item : items) {
            // Verificar que la ayuda diagnóstica existe en el inventario
            diagnosticTestRepository.findById(item.getDiagnosticId())
                .orElseThrow(() -> new IllegalArgumentException("Diagnostic test not found in inventory: " + item.getDiagnosticId()));
            
            // Validar especialista si es requerido
            if (item.isSpecialistRequired()) {
                validateSpecialtyExists(item.getSpecialtyId());
            }
            
            // Validar que pertenece a la orden correcta
            if (!item.getOrderNumber().equals(orderNumber)) {
                throw new IllegalArgumentException("Item order number doesn't match: " + item.getOrderNumber());
            }
            
            orderItemRepository.saveDiagnosticItem(item);
        }
    }

    /**
     * CREAR ORDEN COMPLETA CON VALIDACIONES
     * Aplica todas las reglas del documento de una vez
     */
    public OrderHeader createCompleteOrder(String orderNumber, String patientIdCard, String doctorIdCard, 
                                            LocalDate date, List<OrderItem> allItems) {
        
        // Validar todas las reglas de orden antes de guardar
        orderValidationService.validateOrderRules(allItems);
        orderValidationService.validateSpecialtyRequirements(allItems);
        
        // Crear el header
        OrderHeader header = createOrderHeader(orderNumber, patientIdCard, doctorIdCard, date);
        
        // Separar y guardar los items por tipo
        List<MedicationOrderItem> medications = allItems.stream()
            .filter(item -> item instanceof MedicationOrderItem)
            .map(item -> (MedicationOrderItem) item)
            .toList();
            
        List<ProcedureOrderItem> procedures = allItems.stream()
            .filter(item -> item instanceof ProcedureOrderItem)
            .map(item -> (ProcedureOrderItem) item)
            .toList();
            
        List<DiagnosticOrderItem> diagnostics = allItems.stream()
            .filter(item -> item instanceof DiagnosticOrderItem)
            .map(item -> (DiagnosticOrderItem) item)
            .toList();
        
        // Guardar cada tipo si tiene items
        if (!medications.isEmpty()) {
            addMedicationItems(orderNumber, medications);
        }
        
        if (!procedures.isEmpty()) {
            addProcedureItems(orderNumber, procedures);
        }
        
        if (!diagnostics.isEmpty()) {
            addDiagnosticItems(orderNumber, diagnostics);
        }
        
        return header;
    }

    /**
     * REGISTRO EN HISTORIA CLÍNICA
     * Según documento: estructura no estructurada (NoSQL)
     * - Fecha como clave
     * - Cédula del paciente como clave principal
     * - Información médica completa
     */
    public void appendClinicalHistory(String patientIdCard, ClinicalHistoryEntry entry) {
        validateClinicalHistoryEntry(entry);
        clinicalHistoryRepository.saveEntry(patientIdCard, entry);
    }

    /**
     * CREAR NUEVA CONSULTA CON DIAGNÓSTICO
     * Para cuando se ven resultados de ayuda diagnóstica y se crea nuevo registro
     */
    public void createFollowUpConsultation(String patientIdCard, String doctorIdCard, 
                                            String diagnosis, List<String> relatedOrderNumbers) {
        
        ClinicalHistoryEntry followUp = new ClinicalHistoryEntry(
            LocalDate.now(),
            doctorIdCard,
            "Seguimiento por resultados de ayuda diagnóstica",
            "Resultados de exámenes revisados",
            diagnosis,
            null, // Sin signos vitales en seguimiento
            relatedOrderNumbers
        );
        
        appendClinicalHistory(patientIdCard, followUp);
    }

    // Métodos de validación privados
    
    private void validateDoctorIdCard(String doctorIdCard) {
        if (doctorIdCard == null || !doctorIdCard.matches("\\d{1,10}")) {
            throw new IllegalArgumentException("Doctor ID card must be numeric with maximum 10 digits");
        }
    }
    
    private void validateItemsNotEmpty(List<?> items, String itemType) {
        if (items == null || items.isEmpty()) {
            throw new IllegalArgumentException("Cannot add empty " + itemType + " list to order");
        }
    }
    
    private void validateItemNumbersUnique(List<Integer> itemNumbers) {
        long distinctCount = itemNumbers.stream().distinct().count();
        if (distinctCount != itemNumbers.size()) {
            throw new IllegalArgumentException("Item numbers must be unique within the same order");
        }
    }
    
    private void validateSpecialtyExists(String specialtyId) {
        if (specialtyId == null || specialtyId.isBlank()) {
            throw new IllegalArgumentException("Specialty ID required when specialist is needed");
        }
        
        specialtyRepository.findById(specialtyId)
            .orElseThrow(() -> new IllegalArgumentException("Specialty not found: " + specialtyId));
    }
    
    private void validateClinicalHistoryEntry(ClinicalHistoryEntry entry) {
        if (entry == null) {
            throw new IllegalArgumentException("Clinical history entry cannot be null");
        }
        
        if (entry.getDoctorIdCard() == null || !entry.getDoctorIdCard().matches("\\d{1,10}")) {
            throw new IllegalArgumentException("Invalid doctor ID card in clinical history entry");
        }
        
        if (entry.getDate() == null) {
            throw new IllegalArgumentException("Date required in clinical history entry");
        }
    }
}