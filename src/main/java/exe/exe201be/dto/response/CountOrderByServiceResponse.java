package exe.exe201be.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CountOrderByServiceResponse {
    private String serviceName;
    private Long orderCount;
}
