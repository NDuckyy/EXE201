package exe.exe201be.dto.response;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import exe.exe201be.pojo.type.Status;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaskResponse {
    @Schema(description = "ID of the task", example = "64f444000000000000000001")
    @JsonSerialize(using = ToStringSerializer.class)
    private String id;

    @Schema(description = "ID of the project", example = "64f111000000000000000001")
    private ProjectResponse projectId;

    @Schema(description = "Name of the task", example = "Thiết kế UI trang chủ")
    private String name;

    @Schema(description = "Description of the task", example = "Thiết kế giao diện trang chủ cho website thương mại điện tử")
    private String description;

    @Schema(description = "Status of the task", example = "In Progress")
    private Status status;

    @Schema(description = "Priority of the task", example = "High")
    private Status priority;

    @Schema(description = "Due date of the task", example = "2023-02-15T00:00:00.000+00:00")
    private Date dueDate;

    @Schema(description = "ID of the user who created the task", example = "64f000000000000000000001")
    private UserResponse createdBy;
}
