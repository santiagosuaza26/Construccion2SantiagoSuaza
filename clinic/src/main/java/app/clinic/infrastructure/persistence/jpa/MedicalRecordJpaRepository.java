package app.clinic.infrastructure.persistence.jpa;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MedicalRecordJpaRepository extends JpaRepository<MedicalRecordJpaEntity, Long> {
    List<MedicalRecordJpaEntity> findByPatientIdOrderByConsultationDateDesc(String patientId);
    boolean existsByPatientId(String patientId);
}