package exe.exe201be.dto.request;

import exe.exe201be.pojo.type.Gender;
import exe.exe201be.pojo.type.Status;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Request object for updating user information")
public class UserUpdateRequest {

    @Schema(description = "Full name of the user", example = "John Doe")
    private String fullName;

    @Schema(description = "Phone number of the user", example = "+1234567890")
    private String phone;

    @Schema(description = "Avatar URL of the user", example = "http://example.com/avatar.jpg")
    private String avatarUrl;

    @Schema(description = "Gender of the user", example = "MALE")
    private Gender gender;

    @Schema(description = "Address of the user", example = "123 Main St, City, Country")
    private String address;

    @Schema(description = "Image URL of the user", example = "http://example.com/image.jpg")
    private String image;

    @Schema(description = "Status of the user", example = "ACTIVE")
    private Status status;
}
