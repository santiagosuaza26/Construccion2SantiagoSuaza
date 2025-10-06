package app.clinic.domain.model;

import java.util.Objects;

/**
 * Entity representing a patient visit to the clinic.
 * Contains visit details, patient, and associated records.
 */
public class PatientVisit {
    private final String id;
    private final PatientCedula patientCedula;
    private final AppointmentDateTime visitDateTime;
    private final PatientVisitRecord visitRecord;
    private final boolean completed;

    private PatientVisit(String id, PatientCedula patientCedula, AppointmentDateTime visitDateTime,
                        PatientVisitRecord visitRecord, boolean completed) {
        this.id = id;
        this.patientCedula = patientCedula;
        this.visitDateTime = visitDateTime;
        this.visitRecord = visitRecord;
        this.completed = completed;
    }

    public static PatientVisit of(String id, PatientCedula patientCedula, AppointmentDateTime visitDateTime,
                                 PatientVisitRecord visitRecord) {
        return new PatientVisit(id, patientCedula, visitDateTime, visitRecord, false);
    }

    public static PatientVisit of(String id, PatientCedula patientCedula, AppointmentDateTime visitDateTime,
                                 PatientVisitRecord visitRecord, boolean completed) {
        return new PatientVisit(id, patientCedula, visitDateTime, visitRecord, completed);
    }

    public String getId() {
        return id;
    }

    public PatientCedula getPatientCedula() {
        return patientCedula;
    }

    public AppointmentDateTime getVisitDateTime() {
        return visitDateTime;
    }

    public PatientVisitRecord getVisitRecord() {
        return visitRecord;
    }

    public boolean isCompleted() {
        return completed;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PatientVisit that = (PatientVisit) o;
        return completed == that.completed &&
               Objects.equals(id, that.id) &&
               Objects.equals(patientCedula, that.patientCedula) &&
               Objects.equals(visitDateTime, that.visitDateTime) &&
               Objects.equals(visitRecord, that.visitRecord);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, patientCedula, visitDateTime, visitRecord, completed);
    }

    @Override
    public String toString() {
        return String.format("PatientVisit{id=%s, patient=%s, dateTime=%s, completed=%s}",
                           id, patientCedula, visitDateTime, completed);
    }
}