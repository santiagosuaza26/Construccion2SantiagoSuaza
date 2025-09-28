package app.domain.exception;

/**
 * Excepción específica para validaciones de pacientes
 * Mejora el manejo de errores específicos y el cumplimiento de OCP
 */
public class PatientValidationException extends DomainValidationException {

    private String errorCode;

    public PatientValidationException(String message) {
        super(message);
    }

    public PatientValidationException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public PatientValidationException(String message, Throwable cause) {
        super(message, cause);
    }

    public PatientValidationException(String message, String errorCode, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode;
    }

    // Excepciones específicas para diferentes tipos de validación

    public static class PatientNotFoundException extends PatientValidationException {
        public PatientNotFoundException(String idCard) {
            super("Patient not found with ID: " + idCard, "PATIENT_NOT_FOUND");
        }
    }

    public static class DuplicatePatientException extends PatientValidationException {
        public DuplicatePatientException(String field, String value) {
            super("Patient with " + field + " " + value + " already exists", "DUPLICATE_PATIENT");
        }
    }

    public static class InvalidPatientDataException extends PatientValidationException {
        public InvalidPatientDataException(String details) {
            super("Invalid patient data: " + details, "INVALID_PATIENT_DATA");
        }
    }

    public static class UnauthorizedPatientAccessException extends PatientValidationException {
        public UnauthorizedPatientAccessException(String user, String operation) {
            super("User " + user + " is not authorized to " + operation, "UNAUTHORIZED_ACCESS");
        }
    }

    public static class PatientRemovalNotAllowedException extends PatientValidationException {
        public PatientRemovalNotAllowedException(String reason) {
            super("Patient cannot be removed: " + reason, "PATIENT_REMOVAL_NOT_ALLOWED");
        }
    }
}