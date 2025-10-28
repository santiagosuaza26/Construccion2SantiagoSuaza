package app.clinic.infrastructure.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
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