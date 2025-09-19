package exe.exe201be.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import exe.exe201be.pojo.User;
import exe.exe201be.pojo.type.Status;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProjectResponse {
    @Schema(description = "ID of the project", example = "507f1f77bcf86cd799439011")
    private String id;

    @Schema(description = "Name of the project", example = "Project Alpha")
    private String name;

    @Schema(description = "Description of the project", example = "This is a sample project description.")
    private String description;

    @Schema(description = "Identifier of the project manager", example = "507f1f77bcf86cd799439011")
    private UserResponse managerId;

    @Schema(description = "Status of the project", example = "active")
    private Status status;

    @Schema(description = "Start date of the project", example = "2023-01-01")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yy", timezone = "UTC")
    private Date startDate;

    @Schema(description = "End date of the project", example = "2023-12-31")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yy", timezone = "UTC")
    private Date endDate;

    @Schema(description = "Size of the project team", example = "10")
    private Integer teamSize;

    @Schema(description = "Progress of the project in percentage", example = "75.5")
    private Double progress;
}
