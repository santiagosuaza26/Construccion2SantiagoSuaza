package app.clinic.domain.model;

import java.util.Objects;

/**
 * Value Object representing a patient visit record.
 * Contains vital signs, nurse observations, and visit details.
 */
public class PatientVisitRecord {
    private final PatientCedula patientCedula;
    private final VitalSignsData vitalSigns;
    private final Observations observations;
    private final String administeredMedications;
    private final String performedProcedures;

    private PatientVisitRecord(PatientCedula patientCedula, VitalSignsData vitalSigns,
                              Observations observations, String administeredMedications,
                              String performedProcedures) {
        this.patientCedula = patientCedula;
        this.vitalSigns = vitalSigns;
        this.observations = observations;
        this.administeredMedications = administeredMedications != null ? administeredMedications : "";
        this.performedProcedures = performedProcedures != null ? performedProcedures : "";
    }

    public static PatientVisitRecord of(PatientCedula patientCedula, VitalSignsData vitalSigns,
                                       Observations observations) {
        return new PatientVisitRecord(patientCedula, vitalSigns, observations, "", "");
    }

    public static PatientVisitRecord of(PatientCedula patientCedula, VitalSignsData vitalSigns,
                                       Observations observations, String administeredMedications,
                                       String performedProcedures) {
        return new PatientVisitRecord(patientCedula, vitalSigns, observations,
                                     administeredMedications, performedProcedures);
    }

    public PatientCedula getPatientCedula() {
        return patientCedula;
    }

    public VitalSignsData getVitalSigns() {
        return vitalSigns;
    }

    public Observations getObservations() {
        return observations;
    }

    public String getAdministeredMedications() {
        return administeredMedications;
    }

    public String getPerformedProcedures() {
        return performedProcedures;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PatientVisitRecord that = (PatientVisitRecord) o;
        return Objects.equals(patientCedula, that.patientCedula) &&
               Objects.equals(vitalSigns, that.vitalSigns) &&
               Objects.equals(observations, that.observations) &&
               Objects.equals(administeredMedications, that.administeredMedications) &&
               Objects.equals(performedProcedures, that.performedProcedures);
    }

    @Override
    public int hashCode() {
        return Objects.hash(patientCedula, vitalSigns, observations, administeredMedications, performedProcedures);
    }

    @Override
    public String toString() {
        return String.format("PatientVisitRecord{patient=%s, vitalSigns=%s, observations=%s}",
                           patientCedula, vitalSigns, observations);
    }
}