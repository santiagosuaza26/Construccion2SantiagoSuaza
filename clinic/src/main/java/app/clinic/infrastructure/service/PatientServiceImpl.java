package app.clinic.infrastructure.service;

import org.springframework.stereotype.Service;

import app.clinic.domain.repository.PatientRepository;
import app.clinic.domain.service.PatientService;
import app.clinic.domain.service.RoleBasedAccessService;

@Service
public class PatientServiceImpl extends PatientService {

    public PatientServiceImpl(PatientRepository patientRepository, RoleBasedAccessService roleBasedAccessService) {
        super(patientRepository, roleBasedAccessService);
    }

    // Infrastructure layer service that extends the domain service
    // Can add infrastructure-specific concerns like logging, caching, etc.
}