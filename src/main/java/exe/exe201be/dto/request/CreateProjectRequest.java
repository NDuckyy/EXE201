package exe.exe201be.dto.request;

import exe.exe201be.pojo.type.Status;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateProjectRequest {
    @Schema(description = "Name of the project", example = "Project Alpha")
    private String name;

    @Schema(description = "Description of the project", example = "This is a sample project description.")
    private String description;

    @Schema(description = "Due date of the task (yyyy-MM-dd)", example = "2023-12-31")
    private Date startDate;

    @Schema(description = "Due date of the task (yyyy-MM-dd)", example = "2023-12-31")
    private Date endDate;

    @Schema(description = "Size of the project team", example = "10")
    private Integer teamSize;
}
