package exe.exe201be.pojo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "user_project_roles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "UserGlobalRole entity representing the association between a user and a global role")
public class UserProjectRole {
    @Schema(description = "Unique identifier of the user", example = "507f1f77bcf86cd799439011")
    private String userId;

    @Schema(description = "Unique identifier of the role", example = "507f1f77bcf86cd799439012")
    private String roleId;

    @Schema(description = "Unique identifier of the project", example = "507f1f77bcf86cd799439013")
    private String projectId;
}
