package app.clinic.domain.model.entities;

import app.clinic.domain.model.valueobject.CurrentDiagnosis;
import app.clinic.domain.model.valueobject.CurrentMedications;
import app.clinic.domain.model.valueobject.HospitalizationDetails;

public class PatientMedicalState {
    private final String patientId;
    private CurrentDiagnosis currentDiagnosis;
    private CurrentMedications currentMedications;
    private String currentProcedures;
    private String currentDiagnosticAids;
    private HospitalizationDetails hospitalizationDetails;

    public PatientMedicalState(String patientId) {
        this.patientId = patientId;
        this.currentDiagnosis = new CurrentDiagnosis("");
        this.currentMedications = new CurrentMedications("");
        this.currentProcedures = "";
        this.currentDiagnosticAids = "";
        this.hospitalizationDetails = new HospitalizationDetails(false, "");
    }

    public String getPatientId() {
        return patientId;
    }

    public CurrentDiagnosis getCurrentDiagnosis() {
        return currentDiagnosis;
    }

    public void updateCurrentDiagnosis(CurrentDiagnosis diagnosis) {
        this.currentDiagnosis = diagnosis != null ? diagnosis : new CurrentDiagnosis("");
    }

    public CurrentMedications getCurrentMedications() {
        return currentMedications;
    }

    public void updateCurrentMedications(CurrentMedications medications) {
        this.currentMedications = medications != null ? medications : new CurrentMedications("");
    }

    public String getCurrentProcedures() {
        return currentProcedures;
    }

    public void updateCurrentProcedures(String procedures) {
        this.currentProcedures = procedures != null ? procedures : "";
    }

    public String getCurrentDiagnosticAids() {
        return currentDiagnosticAids;
    }

    public void updateCurrentDiagnosticAids(String diagnosticAids) {
        this.currentDiagnosticAids = diagnosticAids != null ? diagnosticAids : "";
    }

    public HospitalizationDetails getHospitalizationDetails() {
        return hospitalizationDetails;
    }

    public void updateHospitalizationDetails(HospitalizationDetails details) {
        this.hospitalizationDetails = details != null ? details : new HospitalizationDetails(false, "");
    }

    public boolean isHospitalized() {
        return hospitalizationDetails.isHospitalized();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PatientMedicalState that = (PatientMedicalState) o;
        return patientId.equals(that.patientId);
    }

    @Override
    public int hashCode() {
        return patientId.hashCode();
    }

    @Override
    public String toString() {
        return "PatientMedicalState for patient " + patientId;
    }
}