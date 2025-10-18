package exe.exe201be.dto.response;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProjectDashboardResponse {
    private long totalProjects;
    private long newProjectsThisMonth;
    private double avgProjectProgress;   // 0..100
    private long activeProjects;
    private long completedProjects;
}

