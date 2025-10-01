package app.domain.model;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class InsurancePolicy {
    private final String company;
    private final String policyNumber;
    private final boolean active;
    private final LocalDate endDate;

    public InsurancePolicy(String company, String policyNumber, boolean active, LocalDate endDate) {
        this.company = company;
        this.policyNumber = policyNumber;
        this.active = active;
        this.endDate = endDate;
    }

    public String getCompany() { return company; }
    public String getPolicyNumber() { return policyNumber; }
    public boolean isActive() { return active; }
    public LocalDate getEndDate() { return endDate; }

    public long remainingDaysFrom(LocalDate reference) {
        if (endDate == null || reference == null) return 0;
        if (endDate.isBefore(reference)) return 0;
        return ChronoUnit.DAYS.between(reference, endDate);
    }

    // Validaciones removidas del constructor - ahora son responsabilidad de servicios específicos
    // Esto permite crear objetos InsurancePolicy para testing sin lanzar excepciones prematuras
}
