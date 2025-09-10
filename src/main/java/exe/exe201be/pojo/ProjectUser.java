package exe.exe201be.pojo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;

@Document(collection = "project_users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProjectUser {
    @Id
    private String id;

    @Schema(description = "Unique identifier of the project", example = "507f1f77bcf86cd799439013")
    @Field("project_id")
    @Indexed
    private String projectId;

    @Schema(description = "Unique identifier of the user", example = "507f1f77bcf86cd799439011")
    @Indexed
    private String userId;

    @Schema(description = "Unique identifier of the role", example = "507f1f77bcf86cd799439012")
    @Indexed
    private String roleId;

    @Schema(description = "Date when the user joined the project")
    @Field("joined_at")
    @Indexed
    private Date joinedAt;

    @Schema(description = "Status of the user in the project", example = "active")
    @Indexed
    private String status;
}
