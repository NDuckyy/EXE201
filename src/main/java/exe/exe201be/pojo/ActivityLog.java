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
import java.util.Date;

@Document(collection = "activity_logs")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ActivityLog {

    @Id
    @JsonSerialize(using = ToStringSerializer.class)
    private ObjectId id;

    @Schema(description = "ID of the related task", example = "64f444000000000000000001")
    @Field("task_id")
    @Indexed
    private ObjectId taskId;

    @Schema(description = "ID of the user affected by this activity", example = "64f000000000000000000001")
    @Field("user_id")
    @Indexed
    private ObjectId userId;

    @Schema(description = "ID of the user who performed the action", example = "64f000000000000000000001")
    @Field("performed_by")
    @Indexed
    private String performedBy;

    @Schema(description = "Type of action performed", example = "create")
    @Field("action_type")
    @Indexed
    private String actionType;

    @Schema(description = "Description of the activity", example = "Alice đã tạo task 'Thiết kế UI trang chủ'")
    @Indexed
    private String description;

    @Schema(description = "Timestamp when the activity was created")
    @Field("created_at")
    @CreatedDate
    private Instant createdAt;
}
