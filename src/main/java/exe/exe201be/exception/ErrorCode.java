package exe.exe201be.exception;

import lombok.Getter;

public enum ErrorCode {
    VALIDATION_FAILED(400, "Validation Failed"),
    USER_EXISTS(409, "User already exists"),
    BLANK(1000, "Fields must not be blank"),
    FULLNAME_INVALID(1001, "Full name must be at least 2 characters long"),
    EMAIL_INVALID(1002, "Email should be valid"),
    PASSWORD_INVALID(1003, "Password must be at least 6 characters long"),
    PHONE_INVALID(1004, "Phone number must be valid (e.g., +1234567890)"),
    SERVICE_PACKAGE_NOT_FOUND(404, "Service package not found"),
    SERVICE_PROVIDER_NOT_FOUND(404, "Service provider not found"),
    INVALID_LOGIN(401, "Invalid username or password"),
    PROJECT_NOT_FOUND(404, "Project not found"),
    SERVICE_PROVIDER_NAME_ALREADY_EXISTS(409, "Service provider name already exists"),
    SERVICE_PROVIDER_CONTACT_EMAIL_ALREADY_EXISTS(409, "Service provider contact email already exists"),
    SERVICE_PROVIDER_PHONE_NUMBER_ALREADY_EXISTS(409, "Service provider phone number already exists"),
    ROLE_NOT_FOUND(404, "Role not found"),
    USER_GLOBAL_NOT_FOUND(404, "User global not found"),
    TASK_NOT_FOUND(404, "Task not found"),
    ORDER_NOT_FOUND(404, "Order not found"),
    USER_NOT_FOUND(404, "User not found"),
    USER_ALREADY_IN_PROJECT(409, "User already in project"),
    USER_ALREADY_A_SERVICE_PROVIDER(409, "Provider role is inactive please contact admin"),

    ;
    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    private int code;
    private String message;

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
