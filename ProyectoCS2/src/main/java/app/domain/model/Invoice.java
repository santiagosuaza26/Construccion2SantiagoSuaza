package app.domain.model;

import java.util.List;

public class Invoice {

    private Patient patient;
    private List<Order> orders;
    private double totalCost;
    private double copayment;
    private double insuranceCoverage;

    public Invoice(Patient patient, List<Order> orders) {
        this.patient = patient;
        this.orders = orders;
    }

    public Invoice(String id, String patientId, double total) {
        //TODO Auto-generated constructor stub
    }

    public Patient getPatient() {
        return patient;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public double getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(double totalCost) {
        this.totalCost = totalCost;
    }

    public double getCopayment() {
        return copayment;
    }

    public void setCopayment(double copayment) {
        this.copayment = copayment;
    }

    public double getInsuranceCoverage() {
        return insuranceCoverage;
    }

    public void setInsuranceCoverage(double insuranceCoverage) {
        this.insuranceCoverage = insuranceCoverage;
    }
}
