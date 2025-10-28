package app.clinic.infrastructure.persistence.jpa;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "appointments")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentJpaEntity {
    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "patient_id")
    private String patientId;

    @Column(name = "admin_id")
    private String adminId;

    @Column(name = "doctor_id")
    private String doctorId;

    @Column(name = "appointment_date")
    private LocalDateTime appointmentDate;

    @Column(name = "reason")
    private String reason;

    @Column(name = "status")
    private String status;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}