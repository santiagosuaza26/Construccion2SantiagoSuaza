package app.domain.port;

import java.util.Optional;

import app.domain.model.Medication;


public interface MedicationRepository {
    Optional<Medication> findById(String medicationId);
}