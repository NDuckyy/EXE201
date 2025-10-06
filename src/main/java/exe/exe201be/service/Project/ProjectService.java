package exe.exe201be.service.Project;

import exe.exe201be.dto.request.ChangeStatusRequest;
import exe.exe201be.dto.request.CreateProjectRequest;
import exe.exe201be.dto.request.SearchRequest;
import exe.exe201be.dto.response.ProjectResponse;
import exe.exe201be.dto.response.SearchResponse;
import exe.exe201be.pojo.Project;
import jakarta.servlet.http.HttpServletResponse;
import org.bson.types.ObjectId;

import java.util.List;

public interface ProjectService {
    List<ProjectResponse> getAllProjects();
    ProjectResponse getProjectById(String id);
    void createProject(CreateProjectRequest project, ObjectId userId, HttpServletResponse httpServletResponse);
    ProjectResponse updateProject(String id, Project project);
    void changeStatusProject(String id, ChangeStatusRequest status);

    void addMemberToProject(ObjectId projectId, String email);

    void verifyEmail(String token,String email, String projectId);

    void sendInvitationEmail(String email, String projectId);

    SearchResponse<ProjectResponse> searchProjects(SearchRequest request);

    List<ProjectResponse> getProjectsByUserId(ObjectId userId);
}
