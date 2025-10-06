package app.clinic.infrastructure.entity;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * JPA entity representing vital signs table in the database.
 * Maps domain VitalSigns objects to database records.
 */
@Entity
@Table(name = "vital_signs")
public class VitalSignsEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "blood_pressure", nullable = false, length = 20)
    private String bloodPressure;

    @Column(name = "temperature", nullable = false, precision = 4, scale = 2)
    private BigDecimal temperature;

    @Column(name = "pulse", nullable = false)
    private Integer pulse;

    @Column(name = "oxygen_level", nullable = false, precision = 5, scale = 2)
    private BigDecimal oxygenLevel;

    // Default constructor
    public VitalSignsEntity() {}

    // Constructor with parameters
    public VitalSignsEntity(String bloodPressure, BigDecimal temperature, Integer pulse, BigDecimal oxygenLevel) {
        this.bloodPressure = bloodPressure;
        this.temperature = temperature;
        this.pulse = pulse;
        this.oxygenLevel = oxygenLevel;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBloodPressure() {
        return bloodPressure;
    }

    public void setBloodPressure(String bloodPressure) {
        this.bloodPressure = bloodPressure;
    }

    public BigDecimal getTemperature() {
        return temperature;
    }

    public void setTemperature(BigDecimal temperature) {
        this.temperature = temperature;
    }

    public Integer getPulse() {
        return pulse;
    }

    public void setPulse(Integer pulse) {
        this.pulse = pulse;
    }

    public BigDecimal getOxygenLevel() {
        return oxygenLevel;
    }

    public void setOxygenLevel(BigDecimal oxygenLevel) {
        this.oxygenLevel = oxygenLevel;
    }
}