package app.clinic.infrastructure.persistence.jpa;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BillingJpaRepository extends JpaRepository<BillingJpaEntity, String> {
    Optional<BillingJpaEntity> findByOrderNumber(String orderNumber);
    List<BillingJpaEntity> findByIdentificationNumber(String patientId);
}