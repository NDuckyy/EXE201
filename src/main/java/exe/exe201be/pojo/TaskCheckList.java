package exe.exe201be.pojo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.Instant;

@Document(collection = "task_checklists")
@Schema(description = "TaskCheckList entity representing a checklist item associated with a task")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaskCheckList {
    @Id
    @JsonSerialize(using = ToStringSerializer.class)
    private ObjectId id;

    @Schema(description = "Unique identifier of the task", example = "507f1f77bcf86cd799439021")
    @Field("task_id")
    @Indexed
    private ObjectId taskId;

    @Schema(description = "Description of the checklist item", example = "Design the UI mockups")
    @Indexed
    private String description;

    @Schema(description = "Completion status of the checklist item", example = "false")
    @Indexed
    private boolean isCompleted;

    @Schema(description = "Timestamp when the checklist item was created")
    @Field("created_at")
    @CreatedDate
    private Instant createdAt;
}
