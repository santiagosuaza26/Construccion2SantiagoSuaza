package app.domain.services;

public class InvoiceService {

    public double calculateCopayment(double totalCost, boolean activePolicy, double yearlyCopayment) {
        if (!activePolicy) {
            return totalCost;
        }
        if (yearlyCopayment >= 1_000_000) {
            return 0.0;
        }
        return Math.min(50_000, totalCost);
    }
}