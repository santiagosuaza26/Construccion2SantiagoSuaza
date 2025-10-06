package app.clinic.domain.model;

import java.util.List;
import java.util.Objects;

/**
 * Value Object representing a summary of billing information for a patient.
 * Contains patient details, insurance information, and order summaries for billing.
 */
public class BillingSummary {
    private final PatientFullName patientName;
    private final PatientAge patientAge;
    private final PatientCedula patientCedula;
    private final InsuranceCompanyName insuranceCompany;
    private final PolicyNumber policyNumber;
    private final PolicyValidityDays validityDays;
    private final PolicyExpirationDate expirationDate;
    private final List<OrderSummary> orderSummaries;

    private BillingSummary(PatientFullName patientName, PatientAge patientAge, PatientCedula patientCedula,
                          InsuranceCompanyName insuranceCompany, PolicyNumber policyNumber,
                          PolicyValidityDays validityDays, PolicyExpirationDate expirationDate,
                          List<OrderSummary> orderSummaries) {
        this.patientName = patientName;
        this.patientAge = patientAge;
        this.patientCedula = patientCedula;
        this.insuranceCompany = insuranceCompany;
        this.policyNumber = policyNumber;
        this.validityDays = validityDays;
        this.expirationDate = expirationDate;
        this.orderSummaries = List.copyOf(orderSummaries != null ? orderSummaries : List.of());
    }

    public static BillingSummary of(PatientFullName patientName, PatientAge patientAge, PatientCedula patientCedula,
                                   InsuranceCompanyName insuranceCompany, PolicyNumber policyNumber,
                                   PolicyValidityDays validityDays, PolicyExpirationDate expirationDate,
                                   List<OrderSummary> orderSummaries) {
        return new BillingSummary(patientName, patientAge, patientCedula, insuranceCompany, policyNumber,
                                 validityDays, expirationDate, orderSummaries);
    }

    public PatientFullName getPatientName() {
        return patientName;
    }

    public PatientAge getPatientAge() {
        return patientAge;
    }

    public PatientCedula getPatientCedula() {
        return patientCedula;
    }

    public InsuranceCompanyName getInsuranceCompany() {
        return insuranceCompany;
    }

    public PolicyNumber getPolicyNumber() {
        return policyNumber;
    }

    public PolicyValidityDays getValidityDays() {
        return validityDays;
    }

    public PolicyExpirationDate getExpirationDate() {
        return expirationDate;
    }

    public List<OrderSummary> getOrderSummaries() {
        return orderSummaries;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BillingSummary that = (BillingSummary) o;
        return Objects.equals(patientName, that.patientName) &&
               Objects.equals(patientAge, that.patientAge) &&
               Objects.equals(patientCedula, that.patientCedula) &&
               Objects.equals(insuranceCompany, that.insuranceCompany) &&
               Objects.equals(policyNumber, that.policyNumber) &&
               Objects.equals(validityDays, that.validityDays) &&
               Objects.equals(expirationDate, that.expirationDate) &&
               Objects.equals(orderSummaries, that.orderSummaries);
    }

    @Override
    public int hashCode() {
        return Objects.hash(patientName, patientAge, patientCedula, insuranceCompany, policyNumber,
                          validityDays, expirationDate, orderSummaries);
    }

    @Override
    public String toString() {
        return String.format("BillingSummary{patientName=%s, patientAge=%s, patientCedula=%s, insuranceCompany=%s}",
                           patientName, patientAge, patientCedula, insuranceCompany);
    }
}