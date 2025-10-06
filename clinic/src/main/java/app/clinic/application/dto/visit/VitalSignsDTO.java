package app.clinic.application.dto.visit;

/**
 * Data Transfer Object for vital signs information.
 * Used for API responses containing patient vital signs data.
 */
public class VitalSignsDTO {
    private String bloodPressure;
    private String temperature;
    private Integer pulse;
    private String oxygenLevel;

    // Default constructor
    public VitalSignsDTO() {}

    // Constructor with parameters
    public VitalSignsDTO(String bloodPressure, String temperature, Integer pulse, String oxygenLevel) {
        this.bloodPressure = bloodPressure;
        this.temperature = temperature;
        this.pulse = pulse;
        this.oxygenLevel = oxygenLevel;
    }

    // Getters and Setters
    public String getBloodPressure() {
        return bloodPressure;
    }

    public void setBloodPressure(String bloodPressure) {
        this.bloodPressure = bloodPressure;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public Integer getPulse() {
        return pulse;
    }

    public void setPulse(Integer pulse) {
        this.pulse = pulse;
    }

    public String getOxygenLevel() {
        return oxygenLevel;
    }

    public void setOxygenLevel(String oxygenLevel) {
        this.oxygenLevel = oxygenLevel;
    }

    @Override
    public String toString() {
        return String.format("VitalSignsDTO{bloodPressure='%s', temperature='%s', pulse=%d, oxygenLevel='%s'}",
                           bloodPressure, temperature, pulse, oxygenLevel);
    }
}