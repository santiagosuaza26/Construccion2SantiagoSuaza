package app.clinic.application.usecase;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import app.clinic.domain.model.entities.MedicalRecord;
import app.clinic.domain.model.valueobject.Role;
import app.clinic.domain.service.MedicalRecordService;

@Service
public class GetMedicalRecordUseCase {
    private final MedicalRecordService medicalRecordService;

    public GetMedicalRecordUseCase(MedicalRecordService medicalRecordService) {
        this.medicalRecordService = medicalRecordService;
    }

    public MedicalRecord execute(String patientId) {
        Role currentUserRole = getCurrentUserRole();
        return medicalRecordService.getOrCreateMedicalRecord(patientId, currentUserRole);
    }

    private Role getCurrentUserRole() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getAuthorities() != null && !authentication.getAuthorities().isEmpty()) {
            String roleString = authentication.getAuthorities().iterator().next().getAuthority();
            if (roleString.startsWith("ROLE_")) {
                roleString = roleString.substring(5); // Remove "ROLE_" prefix
            }
            return Role.valueOf(roleString);
        }
        throw new IllegalStateException("No se pudo determinar el rol del usuario actual");
    }
}