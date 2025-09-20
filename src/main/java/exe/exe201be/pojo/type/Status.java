package exe.exe201be.pojo.type;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum Status {
    ACTIVE("active"),
    INACTIVE("inactive"),

    COMPLETED("completed"),
    TO_DO("To Do"),
    IN_PROGRESS("In Progress"),
    DONE("Done"),
    CANCELLED("Cancelled"),

    HIGH("High"),
    MEDIUM("Medium"),
    LOW("Low"),
    EMERGENCY("Emergency"),

    PENDING("Pending"),
    PAID("Paid"),
    FAILED("Failed")
    ;

    private final String value;
    Status(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    @JsonCreator
    public static Status fromValue(String value) {
        for (Status s : Status.values()) {
            if (s.value.equalsIgnoreCase(value)) {
                return s;
            }
        }
        throw new IllegalArgumentException("Unknown value: " + value);
    }
}

