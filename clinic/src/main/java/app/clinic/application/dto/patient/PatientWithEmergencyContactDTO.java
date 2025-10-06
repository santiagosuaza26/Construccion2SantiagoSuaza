package app.clinic.application.dto.patient;

/**
 * Data Transfer Object for Patient information that always includes emergency contact.
 * Used for API responses where emergency contact information is mandatory.
 */
public class PatientWithEmergencyContactDTO {
    private String cedula;
    private String username;
    private String fullName;
    private String birthDate;
    private String gender;
    private String address;
    private String phoneNumber;
    private String email;
    private int age;
    private EmergencyContactDTO emergencyContact;
    private InsurancePolicyDTO insurancePolicy;

    // Default constructor
    public PatientWithEmergencyContactDTO() {}

    // Constructor with parameters
    public PatientWithEmergencyContactDTO(String cedula, String username, String fullName,
                                        String birthDate, String gender, String address,
                                        String phoneNumber, String email, int age,
                                        EmergencyContactDTO emergencyContact,
                                        InsurancePolicyDTO insurancePolicy) {
        this.cedula = cedula;
        this.username = username;
        this.fullName = fullName;
        this.birthDate = birthDate;
        this.gender = gender;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.age = age;
        this.emergencyContact = emergencyContact;
        this.insurancePolicy = insurancePolicy;
    }

    // Getters and Setters
    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public EmergencyContactDTO getEmergencyContact() {
        return emergencyContact;
    }

    public void setEmergencyContact(EmergencyContactDTO emergencyContact) {
        this.emergencyContact = emergencyContact;
    }

    public InsurancePolicyDTO getInsurancePolicy() {
        return insurancePolicy;
    }

    public void setInsurancePolicy(InsurancePolicyDTO insurancePolicy) {
        this.insurancePolicy = insurancePolicy;
    }

    @Override
    public String toString() {
        return String.format("PatientWithEmergencyContactDTO{cedula='%s', username='%s', fullName='%s', gender='%s', age=%d, hasEmergencyContact=%s}",
                           cedula, username, fullName, gender, age, emergencyContact != null);
    }
}