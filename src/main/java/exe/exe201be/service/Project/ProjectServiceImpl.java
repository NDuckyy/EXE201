package exe.exe201be.service.Project;

import exe.exe201be.exception.AppException;
import exe.exe201be.exception.ErrorCode;
import exe.exe201be.pojo.Project;
import exe.exe201be.pojo.type.Status;
import exe.exe201be.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectServiceImpl implements ProjectService {
    @Autowired
    private ProjectRepository projectRepository;

    @Override
    public List<Project> getAllProjects() {
        return projectRepository.findAll();
    }

    @Override
    public Project getProjectById(String id) {
        Project project = projectRepository.findById(id).orElse(null);
        if (project == null) {
            throw new AppException(ErrorCode.PROJECT_NOT_FOUND);
        }
        return project;
    }

    @Override
    public Project createProject(Project project) {
        return null;
    }

    @Override
    public Project updateProject(String id, Project project) {
        return null;
    }

    @Override
    public void changeStatusProject(String id) {
        Project project = projectRepository.findById(id).orElse(null);
        if (project == null) {
            throw new AppException(ErrorCode.PROJECT_NOT_FOUND);
        } else {
            if (project.getStatus() == Status.ACTIVE) {
                project.setStatus(Status.INACTIVE);
                projectRepository.save(project);
            } else {
                project.setStatus(Status.ACTIVE);
                projectRepository.save(project);
            }
        }
    }
}