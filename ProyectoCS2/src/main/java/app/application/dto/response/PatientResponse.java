package app.application.dto.response;

public class PatientResponse {
    
    private String idCard;
    private String fullName;
    private String email;
    private String phone;
    private String birthDate;
    private String gender;
    private String address;
    private String username;
    private int age;
    
    // Información del contacto de emergencia
    private EmergencyContactInfo emergencyContact;
    
    // Información del seguro médico
    private InsurancePolicyInfo insurancePolicy;
    
    private String registrationDate;
    
    // Default constructor
    public PatientResponse() {}
    
    // Constructor básico (solo información esencial)
    public PatientResponse(String idCard, String fullName, String email, String phone) {
        this.idCard = idCard;
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
    }
    
    // Constructor completo
    public PatientResponse(String idCard, String fullName, String email, String phone, String birthDate, 
                          String gender, String address, String username, int age, 
                          EmergencyContactInfo emergencyContact, InsurancePolicyInfo insurancePolicy,
                          String registrationDate) {
        this.idCard = idCard;
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
        this.birthDate = birthDate;
        this.gender = gender;
        this.address = address;
        this.username = username;
        this.age = age;
        this.emergencyContact = emergencyContact;
        this.insurancePolicy = insurancePolicy;
        this.registrationDate = registrationDate;
    }
    
    // Getters
    public String getIdCard() { return idCard; }
    public String getFullName() { return fullName; }
    public String getEmail() { return email; }
    public String getPhone() { return phone; }
    public String getBirthDate() { return birthDate; }
    public String getGender() { return gender; }
    public String getAddress() { return address; }
    public String getUsername() { return username; }
    public int getAge() { return age; }
    public EmergencyContactInfo getEmergencyContact() { return emergencyContact; }
    public InsurancePolicyInfo getInsurancePolicy() { return insurancePolicy; }
    public String getRegistrationDate() { return registrationDate; }
    
    // Setters
    public void setIdCard(String idCard) { this.idCard = idCard; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public void setEmail(String email) { this.email = email; }
    public void setPhone(String phone) { this.phone = phone; }
    public void setBirthDate(String birthDate) { this.birthDate = birthDate; }
    public void setGender(String gender) { this.gender = gender; }
    public void setAddress(String address) { this.address = address; }
    public void setUsername(String username) { this.username = username; }
    public void setAge(int age) { this.age = age; }
    public void setEmergencyContact(EmergencyContactInfo emergencyContact) { this.emergencyContact = emergencyContact; }
    public void setInsurancePolicy(InsurancePolicyInfo insurancePolicy) { this.insurancePolicy = insurancePolicy; }
    public void setRegistrationDate(String registrationDate) { this.registrationDate = registrationDate; }
    
    // Utility methods
    public boolean hasInsurance() {
        return insurancePolicy != null && insurancePolicy.hasValidPolicy();
    }
    
    public boolean hasActiveInsurance() {
        return hasInsurance() && insurancePolicy.isActive();
    }
    
    public boolean hasEmergencyContact() {
        return emergencyContact != null;
    }
    
    public boolean hasAddress() {
        return address != null && !address.isBlank();
    }
    
    public boolean isMinor() {
        return age < 18;
    }
    
    public boolean isSenior() {
        return age >= 65;
    }
    
    // Clases internas para información relacionada
    public static class EmergencyContactInfo {
        private String fullName;
        private String relationship;
        private String phone;
        
        public EmergencyContactInfo() {}
        
        public EmergencyContactInfo(String fullName, String relationship, String phone) {
            this.fullName = fullName;
            this.relationship = relationship;
            this.phone = phone;
        }
        
        // Getters and Setters
        public String getFullName() { return fullName; }
        public void setFullName(String fullName) { this.fullName = fullName; }
        public String getRelationship() { return relationship; }
        public void setRelationship(String relationship) { this.relationship = relationship; }
        public String getPhone() { return phone; }
        public void setPhone(String phone) { this.phone = phone; }
        
        @Override
        public String toString() {
            return "EmergencyContactInfo{" +
                    "fullName='" + fullName + '\'' +
                    ", relationship='" + relationship + '\'' +
                    ", phone='" + phone + '\'' +
                    '}';
        }
    }
    
    public static class InsurancePolicyInfo {
        private String company;
        private String policyNumber;
        private boolean active;
        private String endDate;
        private int remainingDays;
        
        public InsurancePolicyInfo() {}
        
        public InsurancePolicyInfo(String company, String policyNumber, boolean active, 
                                 String endDate, int remainingDays) {
            this.company = company;
            this.policyNumber = policyNumber;
            this.active = active;
            this.endDate = endDate;
            this.remainingDays = remainingDays;
        }
        
        // Getters and Setters
        public String getCompany() { return company; }
        public void setCompany(String company) { this.company = company; }
        public String getPolicyNumber() { return policyNumber; }
        public void setPolicyNumber(String policyNumber) { this.policyNumber = policyNumber; }
        public boolean isActive() { return active; }
        public void setActive(boolean active) { this.active = active; }
        public String getEndDate() { return endDate; }
        public void setEndDate(String endDate) { this.endDate = endDate; }
        public int getRemainingDays() { return remainingDays; }
        public void setRemainingDays(int remainingDays) { this.remainingDays = remainingDays; }
        
        public boolean hasValidPolicy() {
            return company != null && !company.isBlank() && 
                   policyNumber != null && !policyNumber.isBlank();
        }
        
        public boolean isExpiringSoon() {
            return remainingDays <= 30 && remainingDays > 0;
        }
        
        public boolean isExpired() {
            return remainingDays <= 0;
        }
        
        @Override
        public String toString() {
            return "InsurancePolicyInfo{" +
                    "company='" + company + '\'' +
                    ", policyNumber='" + policyNumber + '\'' +
                    ", active=" + active +
                    ", endDate='" + endDate + '\'' +
                    ", remainingDays=" + remainingDays +
                    '}';
        }
    }
    
    @Override
    public String toString() {
        return "PatientResponse{" +
                "idCard='" + idCard + '\'' +
                ", fullName='" + fullName + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", gender='" + gender + '\'' +
                ", age=" + age +
                ", hasInsurance=" + hasInsurance() +
                ", hasEmergencyContact=" + hasEmergencyContact() +
                ", registrationDate='" + registrationDate + '\'' +
                '}';
    }
}