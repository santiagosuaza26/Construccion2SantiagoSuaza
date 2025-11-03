package app.clinic.domain.service;

import java.time.LocalDate;

import app.clinic.domain.model.entities.MedicalRecord;
import app.clinic.domain.model.valueobject.Id;
import app.clinic.domain.model.valueobject.Role;
import app.clinic.domain.repository.MedicalRecordRepository;
import app.clinic.domain.repository.PatientRepository;

public class MedicalRecordService {
    private final MedicalRecordRepository medicalRecordRepository;
    private final PatientRepository patientRepository;
    private final RoleBasedAccessService roleBasedAccessService;

    public MedicalRecordService(MedicalRecordRepository medicalRecordRepository, PatientRepository patientRepository, RoleBasedAccessService roleBasedAccessService) {
        this.medicalRecordRepository = medicalRecordRepository;
        this.patientRepository = patientRepository;
        this.roleBasedAccessService = roleBasedAccessService;
    }

    public MedicalRecord getOrCreateMedicalRecord(String patientId, Role currentUserRole) {
        Id id = new Id(patientId);
        if (!patientRepository.existsByIdentificationNumber(id)) {
            throw new IllegalArgumentException("Patient not found");
        }

        // Validar acceso a registros médicos (solo médicos pueden acceder a registros completos)
        roleBasedAccessService.validatePatientDataAccess(currentUserRole, true);

        return medicalRecordRepository.findByPatientIdentificationNumber(patientId).orElse(new MedicalRecord(patientId));
    }

    // Método sobrecargado para compatibilidad con código existente (debe ser usado con precaución)
    @Deprecated
    public MedicalRecord getOrCreateMedicalRecord(String patientId) {
        // Nota: Este método no valida permisos. Debe ser usado solo en contextos donde ya se validaron permisos
        Id id = new Id(patientId);
        if (!patientRepository.existsByIdentificationNumber(id)) {
            throw new IllegalArgumentException("Patient not found");
        }
        return medicalRecordRepository.findByPatientIdentificationNumber(patientId).orElse(new MedicalRecord(patientId));
    }

    public void addRecord(String patientId, String doctorId, String reason, String symptoms, String diagnosis) {
        // Validar que la cédula del médico tenga máximo 10 dígitos
        if (doctorId.length() > 10) {
            throw new IllegalArgumentException("Doctor ID must be maximum 10 digits");
        }

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

    public java.util.List<MedicalRecord> getMedicalHistory(String patientId) {
        // For now, return a list with the current record. In a real implementation,
        // this would return all historical records for the patient
        MedicalRecord record = getOrCreateMedicalRecord(patientId);
        return java.util.List.of(record);
    }
}