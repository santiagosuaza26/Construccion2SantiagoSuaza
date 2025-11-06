package app.clinic.infrastructure.dto;

import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO para representar una cita médica")
public class AppointmentDTO {
    private String id;
    private String patientId;
    private String patientName;
    private String adminId;
    private String adminName;
    private String doctorId;
    private String doctorName;
    private LocalDateTime dateTime;
    private String reason;
    private String status;
}