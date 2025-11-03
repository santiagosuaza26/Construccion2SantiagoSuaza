package app.clinic.domain.model.entities;

import java.time.LocalDate;

public interface MedicalEntry {
    LocalDate getDate();
    String getDoctorId();
}