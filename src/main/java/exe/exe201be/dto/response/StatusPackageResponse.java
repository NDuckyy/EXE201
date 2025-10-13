package exe.exe201be.dto.response;

import exe.exe201be.pojo.type.Status;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StatusPackageResponse {
    Status status;
    Integer quantity;
}
