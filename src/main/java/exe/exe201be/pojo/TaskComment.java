package exe.exe201be.pojo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.Instant;

@Document(collection = "task_comments")
@Schema(description = "TaskComment entity representing a comment made on a task")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaskComment {
    @Id
    private String id;

    @Schema(description = "Unique identifier of the task", example = "507f1f77bcf86cd799439021")
    @Field("task_id")
    @Indexed
    private String taskId;

    @Schema(description = "Unique identifier of the user who made the comment", example = "507f1f77bcf86cd799439011")
    @Field("user_id")
    @Indexed
    private String userId;

    @Schema(description = "Content of the comment", example = "This is a comment on the task.")
    @Indexed
    private String content;

    @Schema(description = "Timestamp when the comment was created")
    @Field("created_at")
    @CreatedDate
    private Instant createdAt;
}
