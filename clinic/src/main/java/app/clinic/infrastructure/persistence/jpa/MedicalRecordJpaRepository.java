package app.clinic.infrastructure.persistence.jpa;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MedicalRecordJpaRepository extends JpaRepository<MedicalRecordJpaEntity, Long> {
    List<MedicalRecordJpaEntity> findByPatientId(String patientId);

    boolean existsByPatientId(String patientId);

    @Query("SELECT mr FROM MedicalRecordJpaEntity mr WHERE mr.patientId = :patientId ORDER BY mr.consultationDate DESC")
    List<MedicalRecordJpaEntity> findByPatientIdOrderByConsultationDateDesc(@Param("patientId") String patientId);
}