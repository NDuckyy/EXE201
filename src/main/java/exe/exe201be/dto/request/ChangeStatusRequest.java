package exe.exe201be.dto.request;

import exe.exe201be.pojo.type.Status;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChangeStatusRequest {
    @Schema(description = "Status of the task: In progress, active, inactive, completed")
    private Status status;
}
