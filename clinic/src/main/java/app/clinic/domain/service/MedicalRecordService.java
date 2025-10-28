package app.clinic.domain.service;

import java.time.LocalDate;

import org.springframework.stereotype.Service;

import app.clinic.domain.model.entities.MedicalRecord;
import app.clinic.domain.model.valueobject.Id;
import app.clinic.domain.repository.MedicalRecordRepository;
import app.clinic.domain.repository.PatientRepository;

@Service
public class MedicalRecordService {
    private final MedicalRecordRepository medicalRecordRepository;
    private final PatientRepository patientRepository;

    public MedicalRecordService(MedicalRecordRepository medicalRecordRepository, PatientRepository patientRepository) {
        this.medicalRecordRepository = medicalRecordRepository;
        this.patientRepository = patientRepository;
    }

    public MedicalRecord getOrCreateMedicalRecord(String patientId) {
        Id id = new Id(patientId);
        if (!patientRepository.existsByIdentificationNumber(id)) {
            throw new IllegalArgumentException("Patient not found");
        }
        return medicalRecordRepository.findByPatientIdentificationNumber(patientId).orElse(new MedicalRecord(patientId));
    }

    public void addRecord(String patientId, String doctorId, String reason, String symptoms, String diagnosis) {
        MedicalRecord record = getOrCreateMedicalRecord(patientId);
        record.addRecord(LocalDate.now(), doctorId, reason, symptoms, diagnosis);
        medicalRecordRepository.save(record);
    }

    public void addMedicationToRecord(String patientId, String orderNumber, String medicationId, String dosage, String duration) {
        MedicalRecord record = getOrCreateMedicalRecord(patientId);
        record.addMedicationToRecord(LocalDate.now(), orderNumber, medicationId, dosage, duration);
        medicalRecordRepository.save(record);
    }

    public void addProcedureToRecord(String patientId, String orderNumber, String procedureId, String quantity, String frequency, boolean requiresSpecialist, String specialistId) {
        MedicalRecord record = getOrCreateMedicalRecord(patientId);
        record.addProcedureToRecord(LocalDate.now(), orderNumber, procedureId, quantity, frequency, requiresSpecialist, specialistId);
        medicalRecordRepository.save(record);
    }

    public void addDiagnosticAidToRecord(String patientId, String orderNumber, String diagnosticAidId, String quantity, boolean requiresSpecialist, String specialistId) {
        MedicalRecord record = getOrCreateMedicalRecord(patientId);
        record.addDiagnosticAidToRecord(LocalDate.now(), orderNumber, diagnosticAidId, quantity, requiresSpecialist, specialistId);
        medicalRecordRepository.save(record);
    }
}