package app.clinic.domain.model;

/**
 * Enumeration representing the different roles in the clinic system.
 * Each role has specific permissions and responsibilities.
 */
public enum UserRole {
    /**
     * Human Resources role - manages users and staff information.
     * Cannot view patient, medication, or procedure information.
     */
    HUMAN_RESOURCES,

    /**
     * Administrative staff role - manages patient registration,
     * appointments, and billing information.
     */
    ADMINISTRATIVE_STAFF,

    /**
     * Information Support role - maintains data integrity,
     * inventory management, and technical support.
     */
    INFORMATION_SUPPORT,

    /**
     * Nurse role - manages patient vital signs, administered medications,
     * and performed procedures.
     */
    NURSE,

    /**
     * Doctor role - manages medical records, diagnoses, treatments,
     * and prescriptions.
     */
    DOCTOR
}