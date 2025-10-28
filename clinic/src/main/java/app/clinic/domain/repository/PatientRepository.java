package app.clinic.domain.repository;

import java.util.List;
import java.util.Optional;

import app.clinic.domain.model.entities.Patient;
import app.clinic.domain.model.entities.VitalSigns;
import app.clinic.domain.model.valueobject.Id;

public interface PatientRepository {
    void save(Patient patient);
    Optional<Patient> findByIdentificationNumber(Id identificationNumber);
    List<Patient> findAll();
    boolean existsByIdentificationNumber(Id identificationNumber);
    void deleteByIdentificationNumber(Id identificationNumber);
    void saveVitalSigns(VitalSigns vitalSigns);
    List<VitalSigns> findVitalSignsByPatientId(Id patientId);
}