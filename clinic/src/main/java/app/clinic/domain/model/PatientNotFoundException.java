package app.clinic.domain.model;

public class PatientNotFoundException extends DomainException {
    public PatientNotFoundException(String patientId) {
        super("Patient not found with ID: " + patientId);
    }
}