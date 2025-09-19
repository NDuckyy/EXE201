package exe.exe201be.dto.request;

import exe.exe201be.pojo.type.Status;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateTaskRequest {
    @Schema(description = "Name of the task", example = "Thiết kế UI trang chủ")
    private String name;

    @Schema(description = "Description of the task", example = "Thiết kế giao diện trang chủ cho website thương mại điện tử")
    private String description;

    @Schema(description = "Status of the task", example = "IN_PROGRESS")
    private Status status;

    @Schema(description = "Priority of the task", example = "high")
    private Status priority;

    @Schema(description = "Due date of the task (yyyy-MM-dd)", example = "2023-12-31")
    private Date dueDate;

}
