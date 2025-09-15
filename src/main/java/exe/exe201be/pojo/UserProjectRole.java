package exe.exe201be.pojo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "user_project_roles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "UserGlobalRole entity representing the association between a user and a global role")
public class UserProjectRole {
    @Schema(description = "Unique identifier of the user", example = "507f1f77bcf86cd799439011")
    @Indexed
    @Field("user_id")
    private ObjectId userId;

    @Schema(description = "Unique identifier of the role", example = "507f1f77bcf86cd799439012")
    @Indexed
    @Field("role_id")
    private ObjectId roleId;

    @Schema(description = "Unique identifier of the project", example = "507f1f77bcf86cd799439013")
    @Indexed
    @Field("project_id")
    private ObjectId projectId;
}
