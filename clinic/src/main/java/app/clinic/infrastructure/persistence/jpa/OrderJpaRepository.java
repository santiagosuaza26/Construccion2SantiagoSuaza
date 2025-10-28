package app.clinic.infrastructure.persistence.jpa;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderJpaRepository extends JpaRepository<OrderJpaEntity, String> {
    Optional<OrderJpaEntity> findByOrderNumber(String orderNumber);

    @Query("SELECT o FROM OrderJpaEntity o WHERE o.patientIdentificationNumber = :patientId")
    List<OrderJpaEntity> findByPatientIdentificationNumber(@Param("patientId") String patientId);

    @Query("SELECT o FROM OrderJpaEntity o WHERE o.doctorIdentificationNumber = :doctorId")
    List<OrderJpaEntity> findByDoctorIdentificationNumber(@Param("doctorId") String doctorId);

    boolean existsByOrderNumber(String orderNumber);
}