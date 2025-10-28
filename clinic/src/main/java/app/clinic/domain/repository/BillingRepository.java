package app.clinic.domain.repository;

import java.util.List;
import java.util.Optional;

import app.clinic.domain.model.entities.Billing;
import app.clinic.domain.model.valueobject.OrderNumber;

public interface BillingRepository {
    void save(Billing billing);
    Optional<Billing> findByOrderNumber(OrderNumber orderNumber);
    List<Billing> findByPatientIdentificationNumber(String patientId);
    List<Billing> findAll();
}