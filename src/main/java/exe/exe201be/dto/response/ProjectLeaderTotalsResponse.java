// exe.exe201be.dto.response
package exe.exe201be.dto.response;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ProjectLeaderTotalsResponse {
    private int totalLeaderProjects; // tổng project mà user là leader
    private long totalTasks;         // tổng task thuộc các project đó
}
