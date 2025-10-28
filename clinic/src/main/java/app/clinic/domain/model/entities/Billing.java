package app.clinic.domain.model.entities;

import java.time.LocalDate;

import app.clinic.domain.model.valueobject.OrderNumber;

public class Billing {
    private final OrderNumber orderNumber;
    private final String patientName;
    private final int age;
    private final String identificationNumber;
    private final String doctorName;
    private final String company;
    private final String policyNumber;
    private final int validityDays;
    private final LocalDate validityDate;
    private final double totalCost;
    private final double copay;
    private final double insuranceCoverage;
    private final String appliedMedications;
    private final String appliedProcedures;
    private final String appliedDiagnosticAids;

    public Billing(OrderNumber orderNumber, String patientName, int age, String identificationNumber, String doctorName, String company, String policyNumber, int validityDays, LocalDate validityDate, double totalCost, double copay, double insuranceCoverage, String appliedMedications, String appliedProcedures, String appliedDiagnosticAids) {
        this.orderNumber = orderNumber;
        this.patientName = patientName;
        this.age = age;
        this.identificationNumber = identificationNumber;
        this.doctorName = doctorName;
        this.company = company;
        this.policyNumber = policyNumber;
        this.validityDays = validityDays;
        this.validityDate = validityDate;
        this.totalCost = totalCost;
        this.copay = copay;
        this.insuranceCoverage = insuranceCoverage;
        this.appliedMedications = appliedMedications;
        this.appliedProcedures = appliedProcedures;
        this.appliedDiagnosticAids = appliedDiagnosticAids;
    }

    public OrderNumber getOrderNumber() {
        return orderNumber;
    }

    public String getPatientName() {
        return patientName;
    }

    public int getAge() {
        return age;
    }

    public String getIdentificationNumber() {
        return identificationNumber;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public String getCompany() {
        return company;
    }

    public String getPolicyNumber() {
        return policyNumber;
    }

    public int getValidityDays() {
        return validityDays;
    }

    public LocalDate getValidityDate() {
        return validityDate;
    }

    public double getTotalCost() {
        return totalCost;
    }

    public double getCopay() {
        return copay;
    }

    public double getInsuranceCoverage() {
        return insuranceCoverage;
    }

    public String getAppliedMedications() {
        return appliedMedications;
    }

    public String getAppliedProcedures() {
        return appliedProcedures;
    }

    public String getAppliedDiagnosticAids() {
        return appliedDiagnosticAids;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Billing billing = (Billing) o;
        return orderNumber.equals(billing.orderNumber);
    }

    @Override
    public int hashCode() {
        return orderNumber.hashCode();
    }

    @Override
    public String toString() {
        return "Billing for order " + orderNumber;
    }
}