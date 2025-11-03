package app.clinic.infrastructure.service;

import org.springframework.stereotype.Service;

import app.clinic.domain.repository.MedicalRecordRepository;
import app.clinic.domain.repository.PatientRepository;
import app.clinic.domain.service.MedicalRecordService;
import app.clinic.domain.service.RoleBasedAccessService;

@Service
public class MedicalRecordServiceImpl extends MedicalRecordService {

    public MedicalRecordServiceImpl(MedicalRecordRepository medicalRecordRepository, PatientRepository patientRepository, RoleBasedAccessService roleBasedAccessService) {
        super(medicalRecordRepository, patientRepository, roleBasedAccessService);
    }

    // Infrastructure layer service that extends the domain service
    // Can add infrastructure-specific concerns like logging, caching, etc.
}