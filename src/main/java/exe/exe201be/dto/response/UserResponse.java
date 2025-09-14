package exe.exe201be.dto.response;

import exe.exe201be.pojo.type.Gender;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class UserResponse {
    private String id;
    private String email;
    private String fullName;
    private String avatarUrl;
    private String phone;
    private Gender gender;
    private String address;
    private String image;
    private String status;

}
