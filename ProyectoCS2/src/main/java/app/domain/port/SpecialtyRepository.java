package app.domain.port;

import java.util.List;
import java.util.Optional;

import app.domain.model.Specialty;

public interface SpecialtyRepository {
    Optional<Specialty> findById(String specialtyId);
    Specialty save(Specialty specialty);
    void deleteById(String specialtyId);
    List<Specialty> findAll();
}