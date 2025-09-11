package exe.exe201be.pojo;

import exe.exe201be.pojo.type.Gender;
import exe.exe201be.pojo.type.Status;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.Instant;

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

    @Schema(description = "Email of the user", example = "john_doe@gmail.com")
    @Indexed
    private String email;

    @Schema(description = "Password of the user", example = "securePassword123")
    @Indexed
    private String password;

    @Schema(description = "Full name of the user", example = "John Doe")
    @Field("full_name")
    @Indexed
    private String fullName;

    @Schema(description = "Phone number of the user", example = "+1234567890")
    @Indexed
    private String phone;

    @Schema(description = "Avatar URL of the user", example = "http://example.com/avatar.jpg")
    @Indexed
    private String avatar_url;

    @Schema(description = "Status of the user", example = "true")
    @Indexed
    private Status status;

    @CreatedDate
    @Schema(description = "Timestamp when the user was created")
    @Field("created_at")
    private Instant createdAt;

    @LastModifiedDate
    @Schema(description = "Timestamp when the user was last updated")
    @Field("updated_at")
    private Instant updatedAt;

    @Schema(description = "Gender of the user", example = "MALE")
    @Indexed
    private Gender gender;

    @Schema(description = "Address of the user", example = "123 Main St, City, Country")
    @Indexed
    private String address;

    @Schema(description = "Image URL of the user", example = "http://example.com/image.jpg")
    @Indexed
    private String image;


}
