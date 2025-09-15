package exe.exe201be.pojo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.Instant;
import java.util.Date;

@Document(collection = "tasks")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Task {

    @Id
    @JsonSerialize(using = ToStringSerializer.class)
    private ObjectId id;

    @Schema(description = "Unique identifier of the project", example = "507f1f77bcf86cd799439013")
    @Field("project_id")
    @Indexed
    private ObjectId projectId;

    @Schema(description = "Name of the task", example = "Thiết kế UI trang chủ")
    @Indexed
    private String name;

    @Schema(description = "Description of the task", example = "Thiết kế giao diện trang chủ cho website thương mại điện tử")
    @Indexed
    private String description;

    @Schema(description = "Status of the task", example = "in_progress")
    @Indexed
    private String status;

    @Schema(description = "Priority of the task", example = "high")
    @Indexed
    private String priority;

    @Schema(description = "Due date of the task")
    @Field("due_date")
    @Indexed
    private Date dueDate;

    @Schema(description = "User who created the task", example = "507f1f77bcf86cd799439011")
    @Field("created_by")
    @Indexed
    private String createdBy;

    @Schema(description = "Timestamp when the task was created")
    @Field("created_at")
    @CreatedDate
    private Instant createdAt;

    @Schema(description = "Timestamp when the task was last updated")
    @Field("updated_at")
    @LastModifiedDate
    private Instant updatedAt;
}
