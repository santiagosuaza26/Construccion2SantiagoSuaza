package app.clinic.infrastructure.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VitalSignsDTO {
    private String patientId;
    private String bloodPressure;
    private double temperature;
    private int pulse;
    private int oxygenLevel;
    private String recordedAt;
}