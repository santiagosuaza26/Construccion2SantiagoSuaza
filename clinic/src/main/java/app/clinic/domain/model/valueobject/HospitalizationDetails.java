package app.clinic.domain.model.valueobject;

public class HospitalizationDetails {
    private final String details;
    private final boolean hospitalized;

    public HospitalizationDetails(boolean hospitalized, String details) {
        this.hospitalized = hospitalized;
        this.details = details != null ? details.trim() : "";
    }

    public boolean isHospitalized() {
        return hospitalized;
    }

    public String getDetails() {
        return details;
    }

    public boolean hasDetails() {
        return !details.isEmpty();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HospitalizationDetails that = (HospitalizationDetails) o;
        return hospitalized == that.hospitalized && details.equals(that.details);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(hospitalized, details);
    }

    @Override
    public String toString() {
        return hospitalized ? "Hospitalized: " + details : "Not hospitalized";
    }
}