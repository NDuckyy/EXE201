package exe.exe201be.service.Project;

import exe.exe201be.pojo.Project;

import java.util.List;

public interface ProjectService {
    List<Project> getAllProjects();
    Project getProjectById(String id);
    Project createProject(Project project);
    Project updateProject(String id, Project project);
    void changeStatusProject(String id);
}
