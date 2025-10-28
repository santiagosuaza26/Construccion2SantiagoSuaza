package app.clinic.infrastructure.persistence.jpa;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VitalSignsJpaRepository extends JpaRepository<VitalSignsJpaEntity, VitalSignsId> {
    List<VitalSignsJpaEntity> findByPatientIdentificationNumber(String patientIdentificationNumber);
}