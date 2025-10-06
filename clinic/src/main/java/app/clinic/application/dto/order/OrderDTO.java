package app.clinic.application.dto.order;

import java.util.List;

/**
 * Data Transfer Object for complete order information.
 * Used for API responses containing order data.
 */
public class OrderDTO {
    private String orderNumber;
    private String patientCedula;
    private String doctorCedula;
    private String creationDate;
    private List<MedicationOrderDTO> medications;
    private List<ProcedureOrderDTO> procedures;
    private List<DiagnosticAidOrderDTO> diagnosticAids;

    // Default constructor
    public OrderDTO() {}

    // Constructor with parameters
    public OrderDTO(String orderNumber, String patientCedula, String doctorCedula,
                   String creationDate, List<MedicationOrderDTO> medications,
                   List<ProcedureOrderDTO> procedures, List<DiagnosticAidOrderDTO> diagnosticAids) {
        this.orderNumber = orderNumber;
        this.patientCedula = patientCedula;
        this.doctorCedula = doctorCedula;
        this.creationDate = creationDate;
        this.medications = medications;
        this.procedures = procedures;
        this.diagnosticAids = diagnosticAids;
    }

    // Getters and Setters
    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getPatientCedula() {
        return patientCedula;
    }

    public void setPatientCedula(String patientCedula) {
        this.patientCedula = patientCedula;
    }

    public String getDoctorCedula() {
        return doctorCedula;
    }

    public void setDoctorCedula(String doctorCedula) {
        this.doctorCedula = doctorCedula;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    public List<MedicationOrderDTO> getMedications() {
        return medications;
    }

    public void setMedications(List<MedicationOrderDTO> medications) {
        this.medications = medications;
    }

    public List<ProcedureOrderDTO> getProcedures() {
        return procedures;
    }

    public void setProcedures(List<ProcedureOrderDTO> procedures) {
        this.procedures = procedures;
    }

    public List<DiagnosticAidOrderDTO> getDiagnosticAids() {
        return diagnosticAids;
    }

    public void setDiagnosticAids(List<DiagnosticAidOrderDTO> diagnosticAids) {
        this.diagnosticAids = diagnosticAids;
    }

    @Override
    public String toString() {
        return String.format("OrderDTO{orderNumber='%s', patientCedula='%s', doctorCedula='%s'}",
                           orderNumber, patientCedula, doctorCedula);
    }
}