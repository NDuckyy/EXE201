package exe.exe201be.dto.request;

import exe.exe201be.exception.ErrorCode;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateUserRequest {
    @Size(min = 2, message = "FULLNAME_INVALID")
    private String fullName;

    @NotBlank(message = "BLANK")
    @Email(message = "EMAIL_INVALID")
    private String email;

    @Size(min = 6, message = "PASSWORD_INVALID")
    private String password;

    @Size(min = 10, max = 15, message = "PHONE_INVALID")
    @Pattern(regexp = "^(\\+\\d{1,3})?\\d{9,12}$",
            message = "PHONE_INVALID")
    private String phone;

}
