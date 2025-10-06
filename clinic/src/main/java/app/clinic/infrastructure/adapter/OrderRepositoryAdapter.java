package app.clinic.infrastructure.adapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import app.clinic.domain.model.DiagnosticAidOrder;
import app.clinic.domain.model.DoctorCedula;
import app.clinic.domain.model.MedicationOrder;
import app.clinic.domain.model.OrderCreationDate;
import app.clinic.domain.model.OrderNumber;
import app.clinic.domain.model.OrderStatus;
import app.clinic.domain.model.PatientCedula;
import app.clinic.domain.model.ProcedureOrder;
import app.clinic.domain.port.OrderRepository;
import app.clinic.infrastructure.entity.DiagnosticAidOrderEntity;
import app.clinic.infrastructure.entity.MedicationOrderEntity;
import app.clinic.infrastructure.entity.OrderEntity;
import app.clinic.infrastructure.entity.ProcedureOrderEntity;
import app.clinic.infrastructure.repository.OrderJpaRepository;

/**
 * Adapter that implements the OrderRepository port using JPA.
 * Converts between domain objects and JPA entities for medical orders.
 */
@Repository
public class OrderRepositoryAdapter implements OrderRepository {

    private final OrderJpaRepository orderJpaRepository;

    public OrderRepositoryAdapter(OrderJpaRepository orderJpaRepository) {
        this.orderJpaRepository = orderJpaRepository;
    }

    @Override
    public MedicationOrder saveMedicationOrder(MedicationOrder medicationOrder) {
        // Save the main order entity first
        OrderEntity orderEntity = new OrderEntity(
                medicationOrder.getOrderNumber().getValue(),
                medicationOrder.getPatientCedula().getValue(),
                medicationOrder.getDoctorCedula().getValue(),
                medicationOrder.getCreationDate().getValue().toLocalDate()
        );
        OrderEntity savedOrder = orderJpaRepository.save(orderEntity);

        // Save medication items (simplified implementation)
        // In a real implementation, you would save each medication item to MedicationOrderEntity

        return medicationOrder; // Return the original order for now
    }

    @Override
    public ProcedureOrder saveProcedureOrder(ProcedureOrder procedureOrder) {
        // Save the main order entity first
        OrderEntity orderEntity = new OrderEntity(
                procedureOrder.getOrderNumber().getValue(),
                procedureOrder.getPatientCedula().getValue(),
                procedureOrder.getDoctorCedula().getValue(),
                procedureOrder.getCreationDate().getValue().toLocalDate()
        );
        OrderEntity savedOrder = orderJpaRepository.save(orderEntity);

        // Save procedure items (simplified implementation)
        // In a real implementation, you would save each procedure item to ProcedureOrderEntity

        return procedureOrder; // Return the original order for now
    }

    @Override
    public DiagnosticAidOrder saveDiagnosticAidOrder(DiagnosticAidOrder diagnosticAidOrder) {
        // Save the main order entity first
        OrderEntity orderEntity = new OrderEntity(
                diagnosticAidOrder.getOrderNumber().getValue(),
                diagnosticAidOrder.getPatientCedula().getValue(),
                diagnosticAidOrder.getDoctorCedula().getValue(),
                diagnosticAidOrder.getCreationDate().getValue().toLocalDate()
        );
        OrderEntity savedOrder = orderJpaRepository.save(orderEntity);

        // Save diagnostic aid items (simplified implementation)
        // In a real implementation, you would save each diagnostic aid item to DiagnosticAidOrderEntity

        return diagnosticAidOrder; // Return the original order for now
    }

    @Override
    public Optional<MedicationOrder> findMedicationOrderByNumber(OrderNumber orderNumber) {
        // Find the order entity by number
        Optional<OrderEntity> orderEntity = orderJpaRepository.findByOrderNumber(orderNumber.getValue());

        if (orderEntity.isEmpty()) {
            return Optional.empty();
        }

        // For now, return a basic MedicationOrder - in a real implementation,
        // you would need to fetch the complete order with all its items
        // This is a simplified implementation
        return Optional.of(createBasicMedicationOrder(orderEntity.get()));
    }

    @Override
    public Optional<ProcedureOrder> findProcedureOrderByNumber(OrderNumber orderNumber) {
        // Find the order entity by number
        Optional<OrderEntity> orderEntity = orderJpaRepository.findByOrderNumber(orderNumber.getValue());

        if (orderEntity.isEmpty()) {
            return Optional.empty();
        }

        // For now, return a basic ProcedureOrder - in a real implementation,
        // you would need to fetch the complete order with all its items
        return Optional.of(createBasicProcedureOrder(orderEntity.get()));
    }

    @Override
    public Optional<DiagnosticAidOrder> findDiagnosticAidOrderByNumber(OrderNumber orderNumber) {
        // Find the order entity by number
        Optional<OrderEntity> orderEntity = orderJpaRepository.findByOrderNumber(orderNumber.getValue());

        if (orderEntity.isEmpty()) {
            return Optional.empty();
        }

        // For now, return a basic DiagnosticAidOrder - in a real implementation,
        // you would need to fetch the complete order with all its items
        return Optional.of(createBasicDiagnosticAidOrder(orderEntity.get()));
    }

    @Override
    public List<MedicationOrder> findMedicationOrdersByPatient(PatientCedula patientCedula) {
        // Find orders by patient cedula and return as medication orders
        // In a real implementation, you would need to determine the order type
        return orderJpaRepository.findByPatientCedula(patientCedula.getValue())
                .stream()
                .map(this::createBasicMedicationOrder)
                .collect(java.util.stream.Collectors.toList());
    }

    @Override
    public List<ProcedureOrder> findProcedureOrdersByPatient(PatientCedula patientCedula) {
        // Find orders by patient cedula and return as procedure orders
        // In a real implementation, you would need to determine the order type
        return orderJpaRepository.findByPatientCedula(patientCedula.getValue())
                .stream()
                .map(this::createBasicProcedureOrder)
                .collect(java.util.stream.Collectors.toList());
    }

    @Override
    public List<DiagnosticAidOrder> findDiagnosticAidOrdersByPatient(PatientCedula patientCedula) {
        // Find orders by patient cedula and return as diagnostic aid orders
        // In a real implementation, you would need to determine the order type
        return orderJpaRepository.findByPatientCedula(patientCedula.getValue())
                .stream()
                .map(this::createBasicDiagnosticAidOrder)
                .collect(java.util.stream.Collectors.toList());
    }

    @Override
    public List<Object> findAllOrdersByNumber(OrderNumber orderNumber) {
        return orderJpaRepository.findByOrderNumber(orderNumber.getValue())
                .map(order -> List.of(toOrderEntity(order)))
                .orElse(List.of());
    }

    @Override
    public boolean existsByOrderNumber(OrderNumber orderNumber) {
        return orderJpaRepository.existsByOrderNumber(orderNumber.getValue());
    }

    @Override
    public void deleteByOrderNumber(OrderNumber orderNumber) {
        orderJpaRepository.findByOrderNumber(orderNumber.getValue())
                .ifPresent(orderJpaRepository::delete);
    }

    @Override
    public long count() {
        return orderJpaRepository.count();
    }

    @Override
    public long countByPatient(PatientCedula patientCedula) {
        return orderJpaRepository.countByPatientCedula(patientCedula.getValue());
    }

    // Métodos auxiliares de conversión

    private Object toOrderEntity(OrderEntity entity) {
        // Convertir entidad a objeto de dominio según sea necesario
        return entity;
    }

    /**
     * Creates a basic MedicationOrder from OrderEntity.
     * In a real implementation, this would need to fetch the complete order with all medication items.
     */
    private MedicationOrder createBasicMedicationOrder(OrderEntity orderEntity) {
        try {
            // Create basic domain objects - in a real implementation, you would need to
            // fetch the complete order with all its medication items from the database
            OrderNumber orderNumber = OrderNumber.of(orderEntity.getOrderNumber());
            PatientCedula patientCedula = PatientCedula.of(orderEntity.getPatientCedula());
            DoctorCedula doctorCedula = DoctorCedula.of(orderEntity.getDoctorCedula());
            OrderCreationDate creationDate = OrderCreationDate.of(orderEntity.getCreationDate().atStartOfDay());

            // For now, create an empty medication order - in a real implementation,
            // you would need to fetch the medication items from MedicationOrderEntity
            List<app.clinic.domain.model.MedicationItem> medications = new ArrayList<>();
            return MedicationOrder.of(orderNumber, patientCedula, doctorCedula, creationDate,
                                    OrderStatus.CREADA, medications);
        } catch (Exception e) {
            throw new RuntimeException("Error creating MedicationOrder from entity: " + e.getMessage(), e);
        }
    }

    /**
     * Creates a basic ProcedureOrder from OrderEntity.
     * In a real implementation, this would need to fetch the complete order with all procedure items.
     */
    private ProcedureOrder createBasicProcedureOrder(OrderEntity orderEntity) {
        try {
            OrderNumber orderNumber = OrderNumber.of(orderEntity.getOrderNumber());
            PatientCedula patientCedula = PatientCedula.of(orderEntity.getPatientCedula());
            DoctorCedula doctorCedula = DoctorCedula.of(orderEntity.getDoctorCedula());
            OrderCreationDate creationDate = OrderCreationDate.of(orderEntity.getCreationDate().atStartOfDay());

            // For now, create an empty procedure order - in a real implementation,
            // you would need to fetch the procedure items from ProcedureOrderEntity
            List<app.clinic.domain.model.ProcedureItem> procedures = new ArrayList<>();
            return ProcedureOrder.of(orderNumber, patientCedula, doctorCedula, creationDate,
                                   OrderStatus.CREADA, procedures);
        } catch (Exception e) {
            throw new RuntimeException("Error creating ProcedureOrder from entity: " + e.getMessage(), e);
        }
    }

    /**
     * Creates a basic DiagnosticAidOrder from OrderEntity.
     * In a real implementation, this would need to fetch the complete order with all diagnostic aid items.
     */
    private DiagnosticAidOrder createBasicDiagnosticAidOrder(OrderEntity orderEntity) {
        try {
            OrderNumber orderNumber = OrderNumber.of(orderEntity.getOrderNumber());
            PatientCedula patientCedula = PatientCedula.of(orderEntity.getPatientCedula());
            DoctorCedula doctorCedula = DoctorCedula.of(orderEntity.getDoctorCedula());
            OrderCreationDate creationDate = OrderCreationDate.of(orderEntity.getCreationDate().atStartOfDay());

            // For now, create an empty diagnostic aid order - in a real implementation,
            // you would need to fetch the diagnostic aid items from DiagnosticAidOrderEntity
            List<app.clinic.domain.model.DiagnosticAidItem> diagnosticAids = new ArrayList<>();
            return DiagnosticAidOrder.of(orderNumber, patientCedula, doctorCedula, creationDate,
                                       OrderStatus.CREADA, diagnosticAids);
        } catch (Exception e) {
            throw new RuntimeException("Error creating DiagnosticAidOrder from entity: " + e.getMessage(), e);
        }
    }

    private MedicationOrder toMedicationOrderDomain(MedicationOrderEntity entity) {
        // Convertir MedicationOrderEntity a MedicationOrder del dominio
        // Esta implementación depende de la estructura específica del modelo de dominio
        throw new UnsupportedOperationException("Not implemented yet");
    }

    private ProcedureOrder toProcedureOrderDomain(ProcedureOrderEntity entity) {
        // Convertir ProcedureOrderEntity a ProcedureOrder del dominio
        // Esta implementación depende de la estructura específica del modelo de dominio
        throw new UnsupportedOperationException("Not implemented yet");
    }

    private DiagnosticAidOrder toDiagnosticAidOrderDomain(DiagnosticAidOrderEntity entity) {
        // Convertir DiagnosticAidOrderEntity a DiagnosticAidOrder del dominio
        // Esta implementación depende de la estructura específica del modelo de dominio
        throw new UnsupportedOperationException("Not implemented yet");
    }

    // Implementación de métodos nuevos agregados al repositorio

    @Override
    public List<MedicationOrder> findMedicationOrdersByDoctor(DoctorCedula doctorCedula) {
        // Find orders by doctor cedula and return as medication orders
        return orderJpaRepository.findByDoctorCedula(doctorCedula.getValue())
                .stream()
                .map(this::createBasicMedicationOrder)
                .collect(java.util.stream.Collectors.toList());
    }

    @Override
    public List<ProcedureOrder> findProcedureOrdersByDoctor(DoctorCedula doctorCedula) {
        // Find orders by doctor cedula and return as procedure orders
        return orderJpaRepository.findByDoctorCedula(doctorCedula.getValue())
                .stream()
                .map(this::createBasicProcedureOrder)
                .collect(java.util.stream.Collectors.toList());
    }

    @Override
    public List<DiagnosticAidOrder> findDiagnosticAidOrdersByDoctor(DoctorCedula doctorCedula) {
        // Find orders by doctor cedula and return as diagnostic aid orders
        return orderJpaRepository.findByDoctorCedula(doctorCedula.getValue())
                .stream()
                .map(this::createBasicDiagnosticAidOrder)
                .collect(java.util.stream.Collectors.toList());
    }

    @Override
    public List<MedicationOrder> findMedicationOrdersByDateRange(OrderCreationDate startDate, OrderCreationDate endDate) {
        // Find orders by date range and return as medication orders
        return orderJpaRepository.findByCreationDateBetween(startDate.getValue().toLocalDate(), endDate.getValue().toLocalDate())
                .stream()
                .map(this::createBasicMedicationOrder)
                .collect(java.util.stream.Collectors.toList());
    }

    @Override
    public List<ProcedureOrder> findProcedureOrdersByDateRange(OrderCreationDate startDate, OrderCreationDate endDate) {
        // Find orders by date range and return as procedure orders
        return orderJpaRepository.findByCreationDateBetween(startDate.getValue().toLocalDate(), endDate.getValue().toLocalDate())
                .stream()
                .map(this::createBasicProcedureOrder)
                .collect(java.util.stream.Collectors.toList());
    }

    @Override
    public List<DiagnosticAidOrder> findDiagnosticAidOrdersByDateRange(OrderCreationDate startDate, OrderCreationDate endDate) {
        // Find orders by date range and return as diagnostic aid orders
        return orderJpaRepository.findByCreationDateBetween(startDate.getValue().toLocalDate(), endDate.getValue().toLocalDate())
                .stream()
                .map(this::createBasicDiagnosticAidOrder)
                .collect(java.util.stream.Collectors.toList());
    }

    @Override
    public MedicationOrder updateMedicationOrder(MedicationOrder medicationOrder) {
        // Update the main order entity
        OrderEntity orderEntity = new OrderEntity(
                medicationOrder.getOrderNumber().getValue(),
                medicationOrder.getPatientCedula().getValue(),
                medicationOrder.getDoctorCedula().getValue(),
                medicationOrder.getCreationDate().getValue().toLocalDate()
        );
        OrderEntity savedOrder = orderJpaRepository.save(orderEntity);

        // In a real implementation, you would also update the medication items
        // For now, just return the original order
        return medicationOrder;
    }

    @Override
    public ProcedureOrder updateProcedureOrder(ProcedureOrder procedureOrder) {
        // Update the main order entity
        OrderEntity orderEntity = new OrderEntity(
                procedureOrder.getOrderNumber().getValue(),
                procedureOrder.getPatientCedula().getValue(),
                procedureOrder.getDoctorCedula().getValue(),
                procedureOrder.getCreationDate().getValue().toLocalDate()
        );
        OrderEntity savedOrder = orderJpaRepository.save(orderEntity);

        // In a real implementation, you would also update the procedure items
        return procedureOrder;
    }

    @Override
    public DiagnosticAidOrder updateDiagnosticAidOrder(DiagnosticAidOrder diagnosticAidOrder) {
        // Update the main order entity
        OrderEntity orderEntity = new OrderEntity(
                diagnosticAidOrder.getOrderNumber().getValue(),
                diagnosticAidOrder.getPatientCedula().getValue(),
                diagnosticAidOrder.getDoctorCedula().getValue(),
                diagnosticAidOrder.getCreationDate().getValue().toLocalDate()
        );
        OrderEntity savedOrder = orderJpaRepository.save(orderEntity);

        // In a real implementation, you would also update the diagnostic aid items
        return diagnosticAidOrder;
    }

    @Override
    public void deleteMedicationOrderByNumber(OrderNumber orderNumber) {
        // Implementación básica - en una implementación real eliminaría de la base de datos
        deleteByOrderNumber(orderNumber);
    }

    @Override
    public void deleteProcedureOrderByNumber(OrderNumber orderNumber) {
        // Implementación básica - en una implementación real eliminaría de la base de datos
        deleteByOrderNumber(orderNumber);
    }

    @Override
    public void deleteDiagnosticAidOrderByNumber(OrderNumber orderNumber) {
        // Implementación básica - en una implementación real eliminaría de la base de datos
        deleteByOrderNumber(orderNumber);
    }
}