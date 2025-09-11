package exe.exe201be.pojo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "task_labels")
@Schema(description = "TaskLabel entity representing a label associated with a task")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaskLabel {
    @Id
    private String id;

    @Indexed
    @Schema(description = "Unique identifier of the task", example = "507f1f77bcf86cd799439021")
    private String taskId;

    @Indexed
    @Schema(description = "Name of the label", example = "Urgent")
    private String labelName;
}
