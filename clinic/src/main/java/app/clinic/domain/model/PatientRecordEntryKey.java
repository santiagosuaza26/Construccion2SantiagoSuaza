package app.clinic.domain.model;

import java.util.Objects;

/**
 * Value Object representing a composite key for patient record entries.
 * Combines patient cedula, record date, and entry ID for unique identification.
 */
public class PatientRecordEntryKey {
    private final PatientCedula patientCedula;
    private final PatientRecordDate recordDate;
    private final PatientRecordEntryId entryId;

    private PatientRecordEntryKey(PatientCedula patientCedula, PatientRecordDate recordDate,
                                 PatientRecordEntryId entryId) {
        this.patientCedula = patientCedula;
        this.recordDate = recordDate;
        this.entryId = entryId;
    }

    public static PatientRecordEntryKey of(PatientCedula patientCedula, PatientRecordDate recordDate,
                                          PatientRecordEntryId entryId) {
        return new PatientRecordEntryKey(patientCedula, recordDate, entryId);
    }

    public PatientCedula getPatientCedula() {
        return patientCedula;
    }

    public PatientRecordDate getRecordDate() {
        return recordDate;
    }

    public PatientRecordEntryId getEntryId() {
        return entryId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PatientRecordEntryKey that = (PatientRecordEntryKey) o;
        return Objects.equals(patientCedula, that.patientCedula) &&
               Objects.equals(recordDate, that.recordDate) &&
               Objects.equals(entryId, that.entryId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(patientCedula, recordDate, entryId);
    }

    @Override
    public String toString() {
        return patientCedula.getValue() + "_" + recordDate.getValue() + "_" + entryId.getValue();
    }
}