package exe.exe201be.pojo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "roles")
@Schema(description = "Role entity representing a user role in the system")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Role {
    @Id
    private String id;

    @Schema(description = "Unique key for the role", example = "ADMIN")
    private String key;

    @Schema(description = "Name of the role", example = "Admin")
    private String name;

    @Schema(description = "Description of the role", example = "Administrator with full access")
    private String description;

    @Schema(description = "Scope of the role", example = "global")
    private String scope;

}
