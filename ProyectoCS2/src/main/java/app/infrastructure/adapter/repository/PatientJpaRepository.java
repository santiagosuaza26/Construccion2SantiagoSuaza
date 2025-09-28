package app.infrastructure.adapter.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import app.infrastructure.adapter.entity.PatientEntity;

@Repository
public interface PatientJpaRepository extends JpaRepository<PatientEntity, String> {
    Optional<PatientEntity> findByIdCard(String idCard);
    Optional<PatientEntity> findByCredentials_Username(String username);
    Optional<PatientEntity> findByEmail(String email);
    boolean existsByCredentials_Username(String username);
    boolean existsByIdCard(String idCard);
    boolean existsByEmail(String email);
    List<PatientEntity> findAll();
    void deleteByIdCard(String idCard);

    @Query("SELECT SUM(i.copay) FROM InvoiceEntity i WHERE i.patientIdCard = :patientIdCard AND YEAR(i.policyEndDate) = :year")
    Long sumCopayByPatientAndYear(@Param("patientIdCard") String patientIdCard, @Param("year") int year);
}