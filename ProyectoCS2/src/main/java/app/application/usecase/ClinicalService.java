package app.application.usecase;

import app.domain.model.Patient;
import app.domain.model.MedicalRecord;
import app.domain.repository.PatientRepository;
import app.domain.repository.MedicalRecordRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClinicalService {

    private final PatientRepository patientRepository;
    private final MedicalRecordRepository medicalRecordRepository;

    public ClinicalService(PatientRepository patientRepository, MedicalRecordRepository medicalRecordRepository) {
        this.patientRepository = patientRepository;
        this.medicalRecordRepository = medicalRecordRepository;
    }

    // Patients
    public void createPatient(Patient patient) {
        patientRepository.save(patient);
    }

    public List<Patient> getAllPatients() {
        return patientRepository.findAll();
    }

    public Optional<Patient> getPatientById(String id) {
        return patientRepository.findById(id);
    }

    public void deletePatient(String id) {
        patientRepository.deleteById(id);
    }

    // Medical Records
    public void createMedicalRecord(MedicalRecord record) {
        medicalRecordRepository.save(record);
    }

    public List<MedicalRecord> getAllMedicalRecords() {
        return medicalRecordRepository.findAll();
    }

    public Optional<MedicalRecord> getRecordById(String id) {
        return medicalRecordRepository.findById(id);
    }
}
