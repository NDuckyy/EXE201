package exe.exe201be.service.Project;

import exe.exe201be.dto.request.ChangeStatusRequest;
import exe.exe201be.dto.response.ProjectResponse;
import exe.exe201be.pojo.Project;

import java.util.List;

public interface ProjectService {
    List<ProjectResponse> getAllProjects();
    ProjectResponse getProjectById(String id);
    ProjectResponse createProject(Project project);
    ProjectResponse updateProject(String id, Project project);
    void changeStatusProject(String id, ChangeStatusRequest status);
}
