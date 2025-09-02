package app.domain.services;

import app.domain.model.Patient;
import app.domain.port.PatientRepository;

public class AdministrativeService {
    private final PatientRepository patientRepository;

    public AdministrativeService(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    public Patient registerPatient(Patient patient) {
        if (patientRepository.findByIdCard(patient.getIdCard()).isPresent()) {
            throw new IllegalArgumentException("Patient already exists");
        }
        return patientRepository.save(patient);
    }

    public void removePatient(String idCard) {
        patientRepository.deleteByIdCard(idCard);
    }
}
