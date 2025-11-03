package app.clinic.infrastructure.dto;

import java.time.LocalDate;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDTO {
    private String orderNumber;
    private String patientId;
    private String doctorId;
    private LocalDate date;
    private String diagnosis;
    private List<String> medications;
    private List<String> procedures;
    private List<String> diagnosticAids;
}