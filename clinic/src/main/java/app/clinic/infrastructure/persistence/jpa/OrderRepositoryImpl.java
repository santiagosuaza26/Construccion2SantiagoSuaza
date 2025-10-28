package app.clinic.infrastructure.persistence.jpa;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;

import app.clinic.domain.model.entities.DiagnosticAidOrder;
import app.clinic.domain.model.entities.MedicationOrder;
import app.clinic.domain.model.entities.Order;
import app.clinic.domain.model.entities.ProcedureOrder;
import app.clinic.domain.model.valueobject.Id;
import app.clinic.domain.model.valueobject.OrderNumber;
import app.clinic.domain.repository.OrderRepository;

@Repository
public class OrderRepositoryImpl implements OrderRepository {
    private final OrderJpaRepository jpaRepository;

    public OrderRepositoryImpl(OrderJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public void save(Order order) {
        OrderJpaEntity entity = new OrderJpaEntity();
        entity.setOrderNumber(order.getOrderNumber().getValue());
        entity.setPatientIdentificationNumber(order.getPatientIdentificationNumber());
        entity.setDoctorIdentificationNumber(order.getDoctorIdentificationNumber());
        entity.setDate(order.getDate());
        entity.setDiagnosis(order.getDiagnosis());

        // Convert and save medications
        List<MedicationOrderJpaEntity> medicationEntities = order.getMedications().stream()
            .map(med -> new MedicationOrderJpaEntity(
                null, // id auto-generated
                med.getOrderNumber().getValue(),
                med.getItem(),
                med.getMedicationId().getValue(),
                med.getDosage(),
                med.getDuration(),
                med.getCost(),
                entity
            ))
            .collect(Collectors.toList());

        // Convert and save procedures
        List<ProcedureOrderJpaEntity> procedureEntities = order.getProcedures().stream()
            .map(proc -> new ProcedureOrderJpaEntity(
                null, // id auto-generated
                proc.getOrderNumber().getValue(),
                proc.getItem(),
                proc.getProcedureId().getValue(),
                proc.getQuantity(),
                proc.getFrequency(),
                proc.isRequiresSpecialist(),
                proc.getSpecialistId() != null ? proc.getSpecialistId().getValue() : null,
                proc.getCost(),
                entity
            ))
            .collect(Collectors.toList());

        // Convert and save diagnostic aids
        List<DiagnosticAidOrderJpaEntity> diagnosticAidEntities = order.getDiagnosticAids().stream()
            .map(aid -> new DiagnosticAidOrderJpaEntity(
                null, // id auto-generated
                aid.getOrderNumber().getValue(),
                aid.getItem(),
                aid.getDiagnosticAidId().getValue(),
                aid.getQuantity(),
                aid.isRequiresSpecialist(),
                aid.getSpecialistId() != null ? aid.getSpecialistId().getValue() : null,
                aid.getCost(),
                entity
            ))
            .collect(Collectors.toList());

        entity.setMedications(medicationEntities);
        entity.setProcedures(procedureEntities);
        entity.setDiagnosticAids(diagnosticAidEntities);

        jpaRepository.save(entity);
    }

    @Override
    public Optional<Order> findByOrderNumber(OrderNumber orderNumber) {
        return jpaRepository.findByOrderNumber(orderNumber.getValue())
            .map(this::toDomain);
    }

    @Override
    public List<Order> findByPatientIdentificationNumber(String patientId) {
        return jpaRepository.findByPatientIdentificationNumber(patientId).stream()
            .map(this::toDomain)
            .collect(Collectors.toList());
    }

    @Override
    public List<Order> findByDoctorIdentificationNumber(String doctorId) {
        return jpaRepository.findByDoctorIdentificationNumber(doctorId).stream()
            .map(this::toDomain)
            .collect(Collectors.toList());
    }

    @Override
    public boolean existsByOrderNumber(OrderNumber orderNumber) {
        return jpaRepository.existsByOrderNumber(orderNumber.getValue());
    }

    private Order toDomain(OrderJpaEntity entity) {
        Order order = new Order(
            new OrderNumber(entity.getOrderNumber()),
            entity.getPatientIdentificationNumber(),
            entity.getDoctorIdentificationNumber(),
            entity.getDate(),
            entity.getDiagnosis()
        );

        // Add medications
        if (entity.getMedications() != null) {
            entity.getMedications().forEach(medEntity -> {
                MedicationOrder med = new MedicationOrder(
                    new OrderNumber(medEntity.getOrderNumber()),
                    medEntity.getItem(),
                    new Id(medEntity.getMedicationId()),
                    medEntity.getDosage(),
                    medEntity.getDuration(),
                    medEntity.getCost()
                );
                order.addMedication(med);
            });
        }

        // Add procedures
        if (entity.getProcedures() != null) {
            entity.getProcedures().forEach(procEntity -> {
                ProcedureOrder proc = new ProcedureOrder(
                    new OrderNumber(procEntity.getOrderNumber()),
                    procEntity.getItem(),
                    new Id(procEntity.getProcedureId()),
                    procEntity.getQuantity(),
                    procEntity.getFrequency(),
                    procEntity.isRequiresSpecialist(),
                    procEntity.getSpecialistId() != null ? new Id(procEntity.getSpecialistId()) : null,
                    procEntity.getCost()
                );
                order.addProcedure(proc);
            });
        }

        // Add diagnostic aids
        if (entity.getDiagnosticAids() != null) {
            entity.getDiagnosticAids().forEach(aidEntity -> {
                DiagnosticAidOrder aid = new DiagnosticAidOrder(
                    new OrderNumber(aidEntity.getOrderNumber()),
                    aidEntity.getItem(),
                    new Id(aidEntity.getDiagnosticAidId()),
                    aidEntity.getQuantity(),
                    aidEntity.isRequiresSpecialist(),
                    aidEntity.getSpecialistId() != null ? new Id(aidEntity.getSpecialistId()) : null,
                    aidEntity.getCost()
                );
                order.addDiagnosticAid(aid);
            });
        }

        return order;
    }
}