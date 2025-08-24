package app.domain.repository;

import app.domain.model.Patient;
import java.util.List;
import java.util.Optional;

public interface PatientRepository {
    void save(Patient patient);

    Optional<Patient> findById(String id);

    List<Patient> findAll();

    void deleteById(String id);
}
