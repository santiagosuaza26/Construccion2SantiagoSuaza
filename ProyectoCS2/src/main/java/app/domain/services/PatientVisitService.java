package app.domain.services;

import java.time.LocalDateTime;

import app.domain.model.PatientVisit;
import app.domain.model.Role;
import app.domain.model.VitalSigns;
import app.domain.port.PatientVisitRepository;

public class PatientVisitService {
    private final PatientVisitRepository patientVisitRepository;
    
    public PatientVisitService(PatientVisitRepository patientVisitRepository) {
        this.patientVisitRepository = patientVisitRepository;
    }
    
    public PatientVisit recordVisit(String patientIdCard, String staffIdCard, 
                                    Role staffRole, String notes, VitalSigns vitalSigns,
                                    String relatedOrderNumber) {
        
        String visitId = patientVisitRepository.nextVisitId();
        
        PatientVisit visit = new PatientVisit(
            visitId,
            patientIdCard,
            staffIdCard,
            staffRole,
            LocalDateTime.now(),
            notes,
            vitalSigns,
            relatedOrderNumber
        );
        
        return patientVisitRepository.save(visit);
    }
}