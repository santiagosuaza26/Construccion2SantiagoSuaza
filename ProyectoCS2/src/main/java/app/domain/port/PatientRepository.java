package app.domain.port;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import app.domain.model.Patient;

public interface PatientRepository {
    Optional<Patient> findByIdCard(String idCard);
    Optional<Patient> findByUsername(String username);
    Patient save(Patient patient);
    void deleteByIdCard(String idCard);
    long totalCopayPaidInYear(String idCard, int year);
    boolean existsByIdCard(String idCard);
    boolean existsByUsername(String username);
    List<Patient> findAll();
    List<Patient> findByRegistrationDateRange(LocalDate startDate, LocalDate endDate);
    long count();
}