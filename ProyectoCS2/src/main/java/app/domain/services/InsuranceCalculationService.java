package app.domain.services;

import java.time.LocalDate;

import app.domain.model.InsurancePolicy;

public class InsuranceCalculationService {
    
    private static final long COPAY_AMOUNT = 50_000L;
    private static final long YEARLY_COPAY_LIMIT = 1_000_000L;
    
    public boolean isPolicyActiveAndValid(InsurancePolicy policy, LocalDate referenceDate) {
        if (policy == null) return false;
        if (!policy.isActive()) return false;
        if (policy.getEndDate() != null && policy.getEndDate().isBefore(referenceDate)) {
            return false;
        }
        return true;
    }
    
    public long calculateCopay(InsurancePolicy policy, long totalAmount, 
                                long copayPaidThisYear, LocalDate referenceDate) {
        
        if (!isPolicyActiveAndValid(policy, referenceDate)) {
            return totalAmount; // Patient pays everything
        }
        
        if (copayPaidThisYear >= YEARLY_COPAY_LIMIT) {
            return 0; // No more copay this year
        }
        
        long remainingCopayCapacity = YEARLY_COPAY_LIMIT - copayPaidThisYear;
        return Math.min(COPAY_AMOUNT, Math.min(remainingCopayCapacity, totalAmount));
    }
    
    public long calculateInsuranceCoverage(long totalAmount, long copay) {
        return totalAmount - copay;
    }
}
