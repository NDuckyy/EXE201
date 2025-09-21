package exe.exe201be.service.Project;

import exe.exe201be.dto.request.ChangeStatusRequest;
import exe.exe201be.dto.request.CreateProjectRequest;
import exe.exe201be.dto.response.ProjectResponse;
import exe.exe201be.pojo.Project;
import org.bson.types.ObjectId;

import java.util.List;

public interface ProjectService {
    List<ProjectResponse> getAllProjects();
    ProjectResponse getProjectById(String id);
    void createProject(CreateProjectRequest project, ObjectId userId);
    ProjectResponse updateProject(String id, Project project);
    void changeStatusProject(String id, ChangeStatusRequest status);
}
