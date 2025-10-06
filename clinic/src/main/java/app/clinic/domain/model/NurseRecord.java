package app.clinic.domain.model;

import java.util.Objects;

/**
 * Value Object representing a record created by a nurse.
 * Contains vital signs, administered medications, performed procedures, and observations.
 */
public class NurseRecord {
    private final VitalSignsData vitalSigns;
    private final Observations observations;
    private final String administeredMedications;
    private final String performedProcedures;

    private NurseRecord(VitalSignsData vitalSigns, Observations observations,
                       String administeredMedications, String performedProcedures) {
        this.vitalSigns = vitalSigns;
        this.observations = observations;
        this.administeredMedications = administeredMedications != null ? administeredMedications : "";
        this.performedProcedures = performedProcedures != null ? performedProcedures : "";
    }

    public static NurseRecord of(VitalSignsData vitalSigns, Observations observations) {
        return new NurseRecord(vitalSigns, observations, "", "");
    }

    public static NurseRecord of(VitalSignsData vitalSigns, Observations observations,
                                String administeredMedications, String performedProcedures) {
        return new NurseRecord(vitalSigns, observations, administeredMedications, performedProcedures);
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
        NurseRecord that = (NurseRecord) o;
        return Objects.equals(vitalSigns, that.vitalSigns) &&
               Objects.equals(observations, that.observations) &&
               Objects.equals(administeredMedications, that.administeredMedications) &&
               Objects.equals(performedProcedures, that.performedProcedures);
    }

    @Override
    public int hashCode() {
        return Objects.hash(vitalSigns, observations, administeredMedications, performedProcedures);
    }

    @Override
    public String toString() {
        return String.format("NurseRecord{vitalSigns=%s, observations=%s, medications='%s', procedures='%s'}",
                           vitalSigns, observations, administeredMedications, performedProcedures);
    }
}