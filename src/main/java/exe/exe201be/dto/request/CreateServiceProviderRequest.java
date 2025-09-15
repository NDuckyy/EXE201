package exe.exe201be.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Request to create a new service provider")
public class CreateServiceProviderRequest {
    @Schema(description = "Name of the service provider", example = "Shine Solutions")
    @NotBlank(message = "Name is required")
    @Size(max = 255, message = "Name must be at most 255 characters")
    private String name;

    @Schema(description = "Contact email of the service provider", example = "support@shine-solutions.example")
    @NotBlank(message = "Contact email is required")
    @Email(message = "Contact email must be valid")
    private String contactEmail;

    @Schema(description = "Phone number of the service provider", example = "+84 28 1234 5678")
    @NotBlank(message = "Phone number is required")
    @Pattern(
            regexp = "^\\+?[0-9. ()-]{7,25}$",
            message = "Phone number is invalid"
    )
    private String phoneNumber;

    @Schema(description = "Address of the service provider", example = "District 1, Ho Chi Minh City, Vietnam")
    @NotBlank(message = "Address is required")
    @Size(max = 500, message = "Address must be at most 500 characters")
    private String address;

    @Schema(description = "Website of the service provider", example = "https://shine-solutions.example")
    @NotBlank(message = "Website is required")
    @Pattern(
            regexp = "^(https?://)?[\\w.-]+(\\.[\\w.-]+)+[/#?]?.*$",
            message = "Website URL is invalid"
    )
    private String website;
}
