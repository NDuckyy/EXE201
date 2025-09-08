package exe.exe201be.pojo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "users")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Schema(description = "User entity representing a user in the system")
public class User {
    @Id
    @Schema(description = "Unique identifier of the user", example = "507f1f77bcf86cd799439011")
    private String id;

    @Indexed
    @Schema(description = "Email of the user", example = "john_doe@gmail.com")
    private String email;

    @Indexed
    @Schema(description = "Password of the user", example = "securePassword123")
    private String password;

    @Indexed
    @Schema(description = "Full name of the user", example = "John Doe")
    private String fullName;

    @Indexed
    @Schema(description = "Phone number of the user", example = "+1234567890")
    private String phone;

    @Indexed
    @Schema(description = "Status of the user", example = "true")
    private boolean status;

    @Indexed
    @Schema(description = "Role of the user (0: user, 1: admin)", example = "0")
    private Integer role; // 0: user, 1: admin

}
