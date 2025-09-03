package app.domain.model;

public class VitalSigns {
    private final String bloodPressure; // e.g., 120/80
    private final double temperature;   // Celsius
    private final int pulse;            // bpm
    private final int oxygenLevel;      // %

    public VitalSigns(String bloodPressure, double temperature, int pulse, int oxygenLevel) {
        this.bloodPressure = bloodPressure;
        this.temperature = temperature;
        this.pulse = pulse;
        this.oxygenLevel = oxygenLevel;
        validate();
    }

    public String getBloodPressure() { return bloodPressure; }
    public double getTemperature() { return temperature; }
    public int getPulse() { return pulse; }
    public int getOxygenLevel() { return oxygenLevel; }

    private void validate() {
        if (oxygenLevel < 0 || oxygenLevel > 100) throw new IllegalArgumentException("Invalid oxygen level");
    }
}