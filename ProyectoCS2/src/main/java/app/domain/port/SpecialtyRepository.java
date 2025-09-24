package app.domain.port;

import java.util.Optional;

import app.domain.model.Specialty;

public interface SpecialtyRepository {
    Optional<Specialty> findById(String specialtyId);
}