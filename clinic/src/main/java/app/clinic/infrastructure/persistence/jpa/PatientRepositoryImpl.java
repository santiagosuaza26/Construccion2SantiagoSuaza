package app.clinic.infrastructure.persistence.jpa;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import app.clinic.domain.model.entities.Patient;
import app.clinic.domain.model.entities.VitalSigns;
import app.clinic.domain.model.valueobject.Id;
import app.clinic.domain.repository.PatientRepository;

@Repository
public class PatientRepositoryImpl implements PatientRepository {
    // TODO: Implement with JPA repository
    // For now, using in-memory storage for demonstration

    @Override
    public void save(Patient patient) {
        // TODO: Implement JPA save
        System.out.println("Saving patient: " + patient.getIdentificationNumber().getValue());
    }

    @Override
    public Optional<Patient> findByIdentificationNumber(Id identificationNumber) {
        // TODO: Implement JPA query
        return Optional.empty();
    }

    @Override
    public List<Patient> findAll() {
        // TODO: Implement JPA query
        return List.of();
    }

    @Override
    public boolean existsByIdentificationNumber(Id identificationNumber) {
        // TODO: Implement JPA query
        return false;
    }

    @Override
    public void deleteByIdentificationNumber(Id identificationNumber) {
        // TODO: Implement JPA delete
        System.out.println("Deleting patient: " + identificationNumber.getValue());
    }

    @Override
    public void saveVitalSigns(VitalSigns vitalSigns) {
        // TODO: Implement JPA save
        System.out.println("Saving vital signs for patient: " + vitalSigns.getPatientIdentificationNumber());
    }

    @Override
    public List<VitalSigns> findVitalSignsByPatientId(Id patientId) {
        // TODO: Implement JPA query
        return List.of();
    }
}