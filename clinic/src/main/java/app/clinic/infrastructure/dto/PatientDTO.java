package app.clinic.infrastructure.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PatientDTO {
    private String identificationNumber;
    private String fullName;
    private String dateOfBirth;
    private String gender;
    private String address;
    private String phone;
    private String email;
    private String emergencyName;
    private String emergencyRelation;
    private String emergencyPhone;
    private String companyName;
    private String policyNumber;
    private boolean insuranceActive;
    private String validityDate;
}