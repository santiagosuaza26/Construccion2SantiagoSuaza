package app.domain.services;

import app.domain.model.MedicalInsurance;

public class BillingPolicyService {

    public boolean isPolicyValid(MedicalInsurance policy) {
        return policy != null && policy.isActive();
    }
}