package app.clinic.domain.service;

import java.time.LocalDate;
import java.util.List;

import app.clinic.domain.model.entities.DiagnosticAidOrder;
import app.clinic.domain.model.entities.MedicalRecord;
import app.clinic.domain.model.entities.MedicationOrder;
import app.clinic.domain.model.entities.Order;
import app.clinic.domain.model.entities.ProcedureOrder;
import app.clinic.domain.model.valueobject.OrderNumber;
import app.clinic.domain.repository.MedicalRecordRepository;
import app.clinic.domain.repository.OrderRepository;

public class OrderManagementService {
    private final OrderRepository orderRepository;
    private final MedicalRecordRepository medicalRecordRepository;
    private final RoleBasedAccessService roleBasedAccessService;

    public OrderManagementService(OrderRepository orderRepository, MedicalRecordRepository medicalRecordRepository, RoleBasedAccessService roleBasedAccessService) {
        this.orderRepository = orderRepository;
        this.medicalRecordRepository = medicalRecordRepository;
        this.roleBasedAccessService = roleBasedAccessService;
    }

    public Order createDiagnosticAidOrder(String patientId, String doctorId, List<DiagnosticAidOrder> diagnosticAids, app.clinic.domain.model.valueobject.Role userRole) {
        // Validar que solo médicos puedan crear órdenes de ayuda diagnóstica
        roleBasedAccessService.validateOrderCreation(userRole, "diagnosticAid");

        OrderNumber orderNumber = generateUniqueOrderNumber();

        Order order = new Order(orderNumber, patientId, doctorId, LocalDate.now());

        for (DiagnosticAidOrder aid : diagnosticAids) {
            order.addDiagnosticAid(aid);
        }

        orderRepository.save(order);
        updateMedicalRecord(patientId, order);

        return order;
    }

    public Order createPostDiagnosticOrder(String patientId, String doctorId, String diagnosis,
                                         List<MedicationOrder> medications, List<ProcedureOrder> procedures, app.clinic.domain.model.valueobject.Role userRole) {
        // Validar que solo médicos puedan crear órdenes post-diagnóstico
        roleBasedAccessService.validateOrderCreation(userRole, "postDiagnostic");

        OrderNumber orderNumber = generateUniqueOrderNumber();

        Order order = new Order(orderNumber, patientId, doctorId, LocalDate.now(), diagnosis);

        for (MedicationOrder med : medications) {
            order.addMedication(med);
        }

        for (ProcedureOrder proc : procedures) {
            order.addProcedure(proc);
        }

        orderRepository.save(order);
        updateMedicalRecordWithDiagnosis(patientId, order, diagnosis);

        return order;
    }

    private void updateMedicalRecord(String patientId, Order order) {
        MedicalRecord record = medicalRecordRepository.findByPatientIdentificationNumber(patientId)
                .orElse(new MedicalRecord(patientId));

        record.addOrderToRecord(order.getDate(), order.getOrderNumber().getValue(), order.getDiagnosis());

        // Add individual orders to record
        for (DiagnosticAidOrder aid : order.getDiagnosticAids()) {
            record.addDiagnosticAidToRecord(order.getDate(), aid.getOrderNumber().getValue(),
                    aid.getDiagnosticAidId().getValue(), aid.getQuantity(),
                    aid.isRequiresSpecialist(), aid.getSpecialistId() != null ? aid.getSpecialistId().getValue() : null);
        }

        medicalRecordRepository.save(record);
    }

    private void updateMedicalRecordWithDiagnosis(String patientId, Order order, String diagnosis) {
        MedicalRecord record = medicalRecordRepository.findByPatientIdentificationNumber(patientId)
                .orElse(new MedicalRecord(patientId));

        record.addRecord(order.getDate(), order.getDoctorIdentificationNumber(),
                "Consulta post-diagnóstico", "", diagnosis);

        record.addOrderToRecord(order.getDate(), order.getOrderNumber().getValue(), diagnosis);

        // Add medications and procedures
        for (MedicationOrder med : order.getMedications()) {
            record.addMedicationToRecord(order.getDate(), med.getOrderNumber().getValue(),
                    med.getMedicationId().getValue(), med.getDosage(), med.getDuration());
        }

        for (ProcedureOrder proc : order.getProcedures()) {
            record.addProcedureToRecord(order.getDate(), proc.getOrderNumber().getValue(),
                    proc.getProcedureId().getValue(), proc.getQuantity(), proc.getFrequency(),
                    proc.isRequiresSpecialist(), proc.getSpecialistId() != null ? proc.getSpecialistId().getValue() : null);
        }

        medicalRecordRepository.save(record);
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
}