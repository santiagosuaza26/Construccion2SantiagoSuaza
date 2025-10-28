package app.clinic.infrastructure.service;

import org.springframework.stereotype.Service;

import app.clinic.domain.repository.AppointmentRepository;
import app.clinic.domain.repository.PatientRepository;
import app.clinic.domain.repository.UserRepository;
import app.clinic.domain.service.AppointmentService;
import app.clinic.domain.service.RoleBasedAccessService;

@Service
public class AppointmentServiceImpl extends AppointmentService {

    public AppointmentServiceImpl(AppointmentRepository appointmentRepository,
                                  PatientRepository patientRepository,
                                  UserRepository userRepository,
                                  RoleBasedAccessService roleBasedAccessService) {
        super(appointmentRepository, patientRepository, userRepository, roleBasedAccessService);
    }

    // Infrastructure layer service that extends the domain service
    // Can add infrastructure-specific concerns like logging, caching, etc.
}