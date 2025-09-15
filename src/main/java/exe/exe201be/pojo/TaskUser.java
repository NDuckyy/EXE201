package exe.exe201be.pojo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;

@Document(collection = "task_users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaskUser {

    @Id
    @JsonSerialize(using = ToStringSerializer.class)
    ObjectId id;

    @Schema(description = "Unique identifier of the task", example = "507f1f77bcf86cd799439021")
    @Field("task_id")
    @Indexed
    ObjectId taskId;

    @Schema(description = "Unique identifier of the user", example = "507f1f77bcf86cd799439011")
    @Field("user_id")
    @Indexed
    ObjectId userId;

    @Schema(description = "Unique identifier of the role", example = "64fc000000000000000101")
    @Field("role_id")
    @Indexed
    private ObjectId roleId;

    @Schema(description = "Date when the user was assigned to the task")
    @Field("assigned_at")
    @Indexed
    private Date assignedAt;

    @Schema(description = "Status of the assignment", example = "active")
    @Indexed
    private String status;
}
