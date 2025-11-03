package app.clinic.domain.service;

import java.time.LocalDate;
import java.util.List;

import app.clinic.domain.model.entities.DiagnosticAidOrder;
import app.clinic.domain.model.entities.MedicationOrder;
import app.clinic.domain.model.entities.Order;
import app.clinic.domain.model.entities.ProcedureOrder;
import app.clinic.domain.model.entities.User;
import app.clinic.domain.model.valueobject.Id;
import app.clinic.domain.model.valueobject.OrderNumber;
import app.clinic.domain.repository.InventoryRepository;
import app.clinic.domain.repository.OrderRepository;
import app.clinic.domain.repository.PatientRepository;
import app.clinic.domain.repository.UserRepository;

public class OrderService {
    private final OrderRepository orderRepository;
    private final PatientRepository patientRepository;
    private final InventoryRepository inventoryRepository;
    private final UserRepository userRepository;
    private final RoleBasedAccessService roleBasedAccessService;

    public OrderService(OrderRepository orderRepository, PatientRepository patientRepository, InventoryRepository inventoryRepository, UserRepository userRepository, RoleBasedAccessService roleBasedAccessService) {
        this.orderRepository = orderRepository;
        this.patientRepository = patientRepository;
        this.inventoryRepository = inventoryRepository;
        this.userRepository = userRepository;
        this.roleBasedAccessService = roleBasedAccessService;
    }

    public Order createDiagnosticAidOrder(String patientId, String doctorId, List<DiagnosticAidOrder> diagnosticAids) {
        Id patientIdObj = new Id(patientId);
        if (!patientRepository.existsByIdentificationNumber(patientIdObj)) {
            throw new IllegalArgumentException("Patient not found");
        }
        OrderNumber orderNumber = generateUniqueOrderNumber();
        Order order = new Order(orderNumber, patientId, doctorId, LocalDate.now());
        for (DiagnosticAidOrder aid : diagnosticAids) {
            if (!inventoryRepository.existsDiagnosticAidById(aid.getDiagnosticAidId())) {
                throw new IllegalArgumentException("Diagnostic aid not found in inventory");
            }
            order.addDiagnosticAid(aid);
        }
        orderRepository.save(order);
        return order;
    }

    public Order createPostDiagnosticOrder(String patientId, String doctorId, String diagnosis, List<MedicationOrder> medications, List<ProcedureOrder> procedures) {
        // Validate that patient has diagnostic aid results before prescribing
        validateDiagnosticResultsAvailable(patientId);

        Id patientIdObj = new Id(patientId);
        if (!patientRepository.existsByIdentificationNumber(patientIdObj)) {
            throw new IllegalArgumentException("Patient not found");
        }

        // Validar que el médico tenga cédula de máximo 10 dígitos
        if (doctorId.length() > 10) {
            throw new IllegalArgumentException("Doctor ID must be maximum 10 digits");
        }

        OrderNumber orderNumber = generateUniqueOrderNumber();
        Order order = new Order(orderNumber, patientId, doctorId, LocalDate.now(), diagnosis);
        for (MedicationOrder med : medications) {
            if (!inventoryRepository.existsMedicationById(med.getMedicationId())) {
                throw new IllegalArgumentException("Medication not found in inventory");
            }
            order.addMedication(med);
        }
        for (ProcedureOrder proc : procedures) {
            if (!inventoryRepository.existsProcedureById(proc.getProcedureId())) {
                throw new IllegalArgumentException("Procedure not found in inventory");
            }
            order.addProcedure(proc);
        }
        orderRepository.save(order);
        return order;
    }

    public Order createMedicationOrder(String patientId, String doctorId, List<MedicationOrder> medications) {
        validateDoctorRole(doctorId);
        Id patientIdObj = new Id(patientId);
        if (!patientRepository.existsByIdentificationNumber(patientIdObj)) {
            throw new IllegalArgumentException("Patient not found");
        }
        OrderNumber orderNumber = generateUniqueOrderNumber();
        Order order = new Order(orderNumber, patientId, doctorId, LocalDate.now());
        for (MedicationOrder med : medications) {
            if (!inventoryRepository.existsMedicationById(med.getMedicationId())) {
                throw new IllegalArgumentException("Medication not found in inventory");
            }
            order.addMedication(med);
        }
        orderRepository.save(order);
        return order;
    }

    public Order createProcedureOrder(String patientId, String doctorId, List<ProcedureOrder> procedures) {
        validateDoctorRole(doctorId);
        Id patientIdObj = new Id(patientId);
        if (!patientRepository.existsByIdentificationNumber(patientIdObj)) {
            throw new IllegalArgumentException("Patient not found");
        }
        OrderNumber orderNumber = generateUniqueOrderNumber();
        Order order = new Order(orderNumber, patientId, doctorId, LocalDate.now());
        for (ProcedureOrder proc : procedures) {
            if (!inventoryRepository.existsProcedureById(proc.getProcedureId())) {
                throw new IllegalArgumentException("Procedure not found in inventory");
            }
            order.addProcedure(proc);
        }
        orderRepository.save(order);
        return order;
    }

    public Order createStandaloneDiagnosticAidOrder(String patientId, String doctorId, List<DiagnosticAidOrder> diagnosticAids) {
        validateDoctorRole(doctorId);
        Id patientIdObj = new Id(patientId);
        if (!patientRepository.existsByIdentificationNumber(patientIdObj)) {
            throw new IllegalArgumentException("Patient not found");
        }
        OrderNumber orderNumber = generateUniqueOrderNumber();
        Order order = new Order(orderNumber, patientId, doctorId, LocalDate.now());
        for (DiagnosticAidOrder aid : diagnosticAids) {
            if (!inventoryRepository.existsDiagnosticAidById(aid.getDiagnosticAidId())) {
                throw new IllegalArgumentException("Diagnostic aid not found in inventory");
            }
            order.addDiagnosticAid(aid);
        }
        orderRepository.save(order);
        return order;
    }

    private void validateDoctorRole(String doctorId) {
        Id doctorIdObj = new Id(doctorId);
        User doctor = userRepository.findByIdentificationNumber(doctorIdObj).orElseThrow(() -> new IllegalArgumentException("Doctor not found"));
        roleBasedAccessService.checkAccess(doctor.getRole(), "order");
    }

    private void validateDiagnosticResultsAvailable(String patientId) {
        // Check if patient has any diagnostic aid orders
        // This is a simplified check - in practice, you'd check for actual results
        // For now, we assume that if there are diagnostic aid orders, they have results
        // In a real implementation, you'd check the medical record for completed diagnostic results
        List<Order> patientOrders = orderRepository.findByPatientIdentificationNumber(patientId);

        boolean hasDiagnosticAids = patientOrders.stream()
            .anyMatch(order -> order.isDiagnosticAidOnly());

        if (!hasDiagnosticAids) {
            throw new IllegalArgumentException("Cannot prescribe medication or procedures without diagnostic aid results");
        }
    }

    private OrderNumber generateUniqueOrderNumber() {
        int attempt = 1;
        while (attempt <= 999999) {
            String orderNumberStr = String.format("%06d", attempt);
            OrderNumber orderNumber = new OrderNumber(orderNumberStr);
            if (!orderRepository.existsByOrderNumber(orderNumber)) {
                return orderNumber;
            }
            attempt++;
        }
        throw new RuntimeException("No se pudo generar un número de orden único.");
    }

    public List<Order> findOrdersByPatientId(String patientId) {
        return orderRepository.findByPatientIdentificationNumber(patientId);
    }
}