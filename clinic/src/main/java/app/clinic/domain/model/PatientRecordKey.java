package app.clinic.domain.model;

import java.util.Objects;

/**
 * Value Object representing a composite key for patient medical records.
 * Combines patient cedula and record date for unique identification.
 */
public class PatientRecordKey {
    private final PatientCedula patientCedula;
    private final PatientRecordDate recordDate;

    private PatientRecordKey(PatientCedula patientCedula, PatientRecordDate recordDate) {
        this.patientCedula = patientCedula;
        this.recordDate = recordDate;
    }

    public static PatientRecordKey of(PatientCedula patientCedula, PatientRecordDate recordDate) {
        return new PatientRecordKey(patientCedula, recordDate);
    }

    public PatientCedula getPatientCedula() {
        return patientCedula;
    }

    public PatientRecordDate getRecordDate() {
        return recordDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PatientRecordKey that = (PatientRecordKey) o;
        return Objects.equals(patientCedula, that.patientCedula) &&
               Objects.equals(recordDate, that.recordDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(patientCedula, recordDate);
    }

    @Override
    public String toString() {
        return patientCedula.getValue() + "_" + recordDate.getValue();
    }
}