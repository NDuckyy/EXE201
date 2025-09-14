package exe.exe201be.dto.response;

import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ServiceProviderResponse {
    private String id;
    private String name;
    private String contactEmail;
    private String phoneNumber;
    private String address;
    private String website;
}
