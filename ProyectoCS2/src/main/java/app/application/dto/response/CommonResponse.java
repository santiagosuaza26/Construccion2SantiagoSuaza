package app.application.dto.response;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class CommonResponse<T> {
    
    private boolean success;
    private String message;
    private T data;
    private String errorCode;
    private String timestamp;
    
    // Default constructor
    public CommonResponse() {
        this.timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));
    }
    
    // Constructor for success response
    public CommonResponse(boolean success, String message, T data) {
        this();
        this.success = success;
        this.message = message;
        this.data = data;
    }
    
    // Constructor for error response with error code
    public CommonResponse(boolean success, String message, T data, String errorCode) {
        this();
        this.success = success;
        this.message = message;
        this.data = data;
        this.errorCode = errorCode;
    }
    
    // Static factory methods for easier creation
    public static <T> CommonResponse<T> success(String message, T data) {
        return new CommonResponse<>(true, message, data);
    }
    
    public static <T> CommonResponse<T> success(T data) {
        return new CommonResponse<>(true, "Operation completed successfully", data);
    }
    
    public static <T> CommonResponse<T> success(String message) {
        return new CommonResponse<>(true, message, null);
    }
    
    public static <T> CommonResponse<T> error(String message) {
        return new CommonResponse<>(false, message, null);
    }
    
    public static <T> CommonResponse<T> error(String message, String errorCode) {
        return new CommonResponse<>(false, message, null, errorCode);
    }
    
    public static <T> CommonResponse<T> error(String message, T data, String errorCode) {
        return new CommonResponse<>(false, message, data, errorCode);
    }
    
    // Getters
    public boolean isSuccess() { return success; }
    public String getMessage() { return message; }
    public T getData() { return data; }
    public String getErrorCode() { return errorCode; }
    public String getTimestamp() { return timestamp; }
    
    // Setters
    public void setSuccess(boolean success) { this.success = success; }
    public void setMessage(String message) { this.message = message; }
    public void setData(T data) { this.data = data; }
    public void setErrorCode(String errorCode) { this.errorCode = errorCode; }
    public void setTimestamp(String timestamp) { this.timestamp = timestamp; }
    
    // Utility methods
    public boolean hasError() {
        return !success;
    }
    
    public boolean hasData() {
        return data != null;
    }
    
    public boolean hasErrorCode() {
        return errorCode != null && !errorCode.isBlank();
    }
    
    @Override
    public String toString() {
        return "CommonResponse{" +
                "success=" + success +
                ", message='" + message + '\'' +
                ", hasData=" + hasData() +
                ", errorCode='" + errorCode + '\'' +
                ", timestamp='" + timestamp + '\'' +
                '}';
    }
}