package app.infrastructure.adapter.jpa.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class VitalSignsEmbedded {

    @Column
    private String bloodPressure;

    @Column
    private Double temperature;

    @Column
    private Integer pulse;

    @Column
    private Integer oxygenLevel;

    // Constructor vacío para JPA
    public VitalSignsEmbedded() {}

    // Constructor con parámetros
    public VitalSignsEmbedded(String bloodPressure, Double temperature, Integer pulse, Integer oxygenLevel) {
        this.bloodPressure = bloodPressure;
        this.temperature = temperature;
        this.pulse = pulse;
        this.oxygenLevel = oxygenLevel;
    }

    // Getters y Setters
    public String getBloodPressure() { return bloodPressure; }
    public void setBloodPressure(String bloodPressure) { this.bloodPressure = bloodPressure; }

    public Double getTemperature() { return temperature; }
    public void setTemperature(Double temperature) { this.temperature = temperature; }

    public Integer getPulse() { return pulse; }
    public void setPulse(Integer pulse) { this.pulse = pulse; }

    public Integer getOxygenLevel() { return oxygenLevel; }
    public void setOxygenLevel(Integer oxygenLevel) { this.oxygenLevel = oxygenLevel; }
}