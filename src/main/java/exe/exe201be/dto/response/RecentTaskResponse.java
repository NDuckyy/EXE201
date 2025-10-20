package exe.exe201be.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecentTaskResponse {
    private String id;
    private String title;          // tên task
    private String status;         // OPEN / IN_PROGRESS / DONE ...
    private String projectId;
    private String projectName;
}