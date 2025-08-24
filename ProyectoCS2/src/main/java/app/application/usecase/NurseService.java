package app.application.usecase;

import app.domain.model.MedicalRecord;
import app.domain.repository.MedicalRecordRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class NurseService {

    private final MedicalRecordRepository medicalRecordRepository;

    public NurseService(MedicalRecordRepository medicalRecordRepository) {
        this.medicalRecordRepository = medicalRecordRepository;
    }

    public void registerVitals(String patientId, String notes) {
        MedicalRecord record = medicalRecordRepository.findById(patientId)
                .orElse(new MedicalRecord(patientId));
        record.addEntry(LocalDateTime.now().toString(), notes);
        medicalRecordRepository.save(record);
    }
}