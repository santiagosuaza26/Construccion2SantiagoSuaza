package app.clinic.infrastructure.persistence.jpa;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

public class VitalSignsId implements Serializable {
    private String patientIdentificationNumber;
    private LocalDateTime dateTime;

    public VitalSignsId() {}

    public VitalSignsId(String patientIdentificationNumber, LocalDateTime dateTime) {
        this.patientIdentificationNumber = patientIdentificationNumber;
        this.dateTime = dateTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VitalSignsId that = (VitalSignsId) o;
        return Objects.equals(patientIdentificationNumber, that.patientIdentificationNumber) &&
               Objects.equals(dateTime, that.dateTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(patientIdentificationNumber, dateTime);
    }
}