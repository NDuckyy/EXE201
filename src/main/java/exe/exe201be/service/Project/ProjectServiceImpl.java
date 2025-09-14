package exe.exe201be.service.Project;

import exe.exe201be.dto.response.ProjectResponse;
import exe.exe201be.dto.response.UserResponse;
import exe.exe201be.exception.AppException;
import exe.exe201be.exception.ErrorCode;
import exe.exe201be.pojo.Project;
import exe.exe201be.pojo.User;
import exe.exe201be.pojo.type.Status;
import exe.exe201be.repository.ProjectRepository;
import exe.exe201be.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class ProjectServiceImpl implements ProjectService {
    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public List<ProjectResponse> getAllProjects() {
        List<Project> projects = projectRepository.findAll();
        if (projects.isEmpty()) return List.of();

        Set<String> managerIds = projects.stream()
                .map(Project::getManagerId)
                .filter(Objects::nonNull)
                .filter(id -> !id.isBlank())
                .collect(Collectors.toSet());

        Map<String, User> usersById = userRepository.findAllById(managerIds).stream()
                .collect(Collectors.toMap(User::getId, Function.identity()));

        return projects.stream()
                .map(project -> {
                    User user = usersById.get(project.getManagerId());
                    UserResponse userResponse = user == null ? null :
                            UserResponse.builder()
                                    .id(user.getId())
                                    .fullName(user.getFullName())
                                    .email(user.getEmail())
                                    .phone(user.getPhone())
                                    .gender(user.getGender())
                                    .avatarUrl(user.getAvatar_url())
                                    .image(user.getImage())
                                    .address(user.getAddress())
                                    .status(user.getStatus())
                                    .build();

                    return ProjectResponse.builder()
                            .id(project.getId())
                            .name(project.getName())
                            .description(project.getDescription())
                            .managerId(userResponse)
                            .startDate(project.getStartDate())
                            .endDate(project.getEndDate())
                            .status(project.getStatus())
                            .teamSize(project.getTeamSize())
                            .progress(project.getProgress())
                            .build();
                })
                .collect(Collectors.toList());
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