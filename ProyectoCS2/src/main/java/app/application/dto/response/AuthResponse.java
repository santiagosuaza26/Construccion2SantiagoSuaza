package app.application.dto.response;

import java.util.List;

public class AuthResponse {
    
    private String userId;
    private String fullName;
    private String role;
    private boolean isStaff;
    private List<String> permissions;
    private String sessionToken;
    
    // Default constructor
    public AuthResponse() {}
    
    // Constructor básico
    public AuthResponse(String userId, String fullName, String role, boolean isStaff) {
        this.userId = userId;
        this.fullName = fullName;
        this.role = role;
        this.isStaff = isStaff;
    }
    
    // Constructor completo
    public AuthResponse(String userId, String fullName, String role, boolean isStaff, 
                       List<String> permissions, String sessionToken) {
        this.userId = userId;
        this.fullName = fullName;
        this.role = role;
        this.isStaff = isStaff;
        this.permissions = permissions;
        this.sessionToken = sessionToken;
    }
    
    // Getters
    public String getUserId() { return userId; }
    public String getFullName() { return fullName; }
    public String getRole() { return role; }
    public boolean isStaff() { return isStaff; }
    public List<String> getPermissions() { return permissions; }
    public String getSessionToken() { return sessionToken; }
    
    // Setters
    public void setUserId(String userId) { this.userId = userId; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public void setRole(String role) { this.role = role; }
    public void setStaff(boolean staff) { isStaff = staff; }
    public void setPermissions(List<String> permissions) { this.permissions = permissions; }
    public void setSessionToken(String sessionToken) { this.sessionToken = sessionToken; }
    
    // Utility methods
    public boolean isPatient() {
        return "PATIENT".equals(role);
    }
    
    public boolean isDoctor() {
        return "DOCTOR".equals(role);
    }
    
    public boolean isNurse() {
        return "NURSE".equals(role);
    }
    
    public boolean isAdministrative() {
        return "ADMINISTRATIVE".equals(role);
    }
    
    public boolean isHumanResources() {
        return "HUMAN_RESOURCES".equals(role);
    }
    
    public boolean isSupport() {
        return "SUPPORT".equals(role);
    }
    
    public boolean hasPermissions() {
        return permissions != null && !permissions.isEmpty();
    }
    
    public boolean hasPermission(String permission) {
        return permissions != null && permissions.contains(permission);
    }
    
    public boolean hasSessionToken() {
        return sessionToken != null && !sessionToken.isBlank();
    }
    
    @Override
    public String toString() {
        return "AuthResponse{" +
                "userId='" + userId + '\'' +
                ", fullName='" + fullName + '\'' +
                ", role='" + role + '\'' +
                ", isStaff=" + isStaff +
                ", permissionsCount=" + (permissions != null ? permissions.size() : 0) +
                ", hasSessionToken=" + hasSessionToken() +
                '}';
    }
}