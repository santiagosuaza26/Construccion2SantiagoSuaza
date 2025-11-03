package app.clinic.infrastructure.service;

import org.springframework.stereotype.Service;

import app.clinic.domain.repository.PatientRepository;
import app.clinic.domain.repository.UserRepository;
import app.clinic.domain.service.RoleBasedAccessService;
import app.clinic.domain.service.VitalSignsService;

@Service
public class VitalSignsServiceImpl extends VitalSignsService {

    public VitalSignsServiceImpl(PatientRepository patientRepository, UserRepository userRepository, RoleBasedAccessService roleBasedAccessService) {
        super(patientRepository, userRepository, roleBasedAccessService);
    }

    // Infrastructure layer service that extends the domain service
    // Can add infrastructure-specific concerns like logging, caching, etc.
}