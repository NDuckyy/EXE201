package exe.exe201be.dto.response;

import exe.exe201be.pojo.type.Gender;
import exe.exe201be.pojo.type.Status;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class    UserResponse {
    private String id;
    private String email;
    private String fullName;
    private String avatarUrl;
    private String phone;
    private Gender gender;
    private String address;
    private String image;
    private Status status;

}
