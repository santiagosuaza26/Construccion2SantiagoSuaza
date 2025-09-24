package app.domain.port;

import java.util.List;
import java.util.Optional;

import app.domain.model.Medication;

public interface MedicationRepository {
    Optional<Medication> findById(String medicationId);
    Medication save(Medication medication);
    void deleteById(String medicationId);
    List<Medication> findAll();
}