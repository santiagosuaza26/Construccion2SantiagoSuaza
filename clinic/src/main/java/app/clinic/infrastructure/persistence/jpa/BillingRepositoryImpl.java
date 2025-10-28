package app.clinic.infrastructure.persistence.jpa;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;

import app.clinic.domain.model.entities.Billing;
import app.clinic.domain.model.valueobject.OrderNumber;
import app.clinic.domain.repository.BillingRepository;

@Repository
public class BillingRepositoryImpl implements BillingRepository {

    private final BillingJpaRepository billingJpaRepository;

    public BillingRepositoryImpl(BillingJpaRepository billingJpaRepository) {
        this.billingJpaRepository = billingJpaRepository;
    }

    @Override
    public void save(Billing billing) {
        BillingJpaEntity entity = new BillingJpaEntity(
            billing.getOrderNumber().getValue(),
            billing.getPatientName(),
            billing.getAge(),
            billing.getIdentificationNumber(),
            billing.getDoctorName(),
            billing.getCompany(),
            billing.getPolicyNumber(),
            billing.getValidityDays(),
            billing.getValidityDate(),
            billing.getTotalCost(),
            billing.getCopay(),
            billing.getInsuranceCoverage(),
            billing.getAppliedMedications(),
            billing.getAppliedProcedures(),
            billing.getAppliedDiagnosticAids(),
            billing.getGeneratedAt(),
            billing.getGeneratedBy()
        );
        billingJpaRepository.save(entity);
    }

    @Override
    public Optional<Billing> findByOrderNumber(OrderNumber orderNumber) {
        return billingJpaRepository.findByOrderNumber(orderNumber.getValue())
            .map(this::toDomain);
    }

    @Override
    public List<Billing> findByPatientIdentificationNumber(String patientId) {
        return billingJpaRepository.findByIdentificationNumber(patientId)
            .stream()
            .map(this::toDomain)
            .collect(Collectors.toList());
    }

    @Override
    public List<Billing> findAll() {
        return billingJpaRepository.findAll()
            .stream()
            .map(this::toDomain)
            .collect(Collectors.toList());
    }

    private Billing toDomain(BillingJpaEntity entity) {
        return new Billing(
            new OrderNumber(entity.getOrderNumber()),
            entity.getPatientName(),
            entity.getAge(),
            entity.getIdentificationNumber(),
            entity.getDoctorName(),
            entity.getCompany(),
            entity.getPolicyNumber(),
            entity.getValidityDays(),
            entity.getValidityDate(),
            entity.getTotalCost(),
            entity.getCopay(),
            entity.getInsuranceCoverage(),
            entity.getAppliedMedications(),
            entity.getAppliedProcedures(),
            entity.getAppliedDiagnosticAids(),
            entity.getGeneratedAt(),
            entity.getGeneratedBy()
        );
    }
}