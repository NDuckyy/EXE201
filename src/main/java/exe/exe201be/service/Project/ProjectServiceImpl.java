package exe.exe201be.service.Project;

import exe.exe201be.dto.request.ChangeStatusRequest;
import exe.exe201be.dto.request.CreateProjectRequest;
import exe.exe201be.dto.response.ProjectResponse;
import exe.exe201be.dto.response.UserResponse;
import exe.exe201be.exception.AppException;
import exe.exe201be.exception.ErrorCode;
import exe.exe201be.pojo.*;
import exe.exe201be.pojo.type.Status;
import exe.exe201be.repository.*;
import exe.exe201be.service.Authority.AuthorityService;
import jakarta.servlet.http.HttpServletResponse;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class ProjectServiceImpl implements ProjectService {
    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private ProjectUserRepository projectUserRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserProjectRepository userProjectRepository;

    @Autowired
    private AuthorityService authorityService;


    @Override
    public List<ProjectResponse> getAllProjects() {
        List<Project> projects = projectRepository.findAll();
        if (projects.isEmpty()) return List.of();

        Set<ObjectId> managerIds = projects.stream()
                .map(Project::getManagerId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        Map<ObjectId, User> usersById = userRepository.findAllById(managerIds).stream()
                .collect(Collectors.toMap(User::getId, Function.identity()));

        return projects.stream()
                .map(project -> {
                    User user = usersById.get(project.getManagerId());
                    return getProjectResponse(project, user);
                })
                .collect(Collectors.toList());
    }

    @Override
    public ProjectResponse getProjectById(String id) {
        ObjectId objectId = new ObjectId(id);
        Project project = projectRepository.findById(objectId).orElse(null);
        if (project == null) {
            throw new AppException(ErrorCode.PROJECT_NOT_FOUND);
        }
        User user = userRepository.findById(project.getManagerId()).orElse(null);

        return getProjectResponse(project, user);
    }

    private ProjectResponse getProjectResponse(Project project, User user) {
        UserResponse userResponse = user == null ? null :
                UserResponse.builder()
                        .id(user.getId().toHexString())
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
                .id(project.getId().toHexString())
                .name(project.getName())
                .description(project.getDescription())
                .managerId(userResponse)
                .startDate(project.getStartDate())
                .endDate(project.getEndDate())
                .status(project.getStatus())
                .teamSize(project.getTeamSize())
                .progress(project.getProgress())
                .build();
    }

    @Override
    public void createProject(CreateProjectRequest project, ObjectId userId, HttpServletResponse httpServletResponse) {
        Role role = roleRepository.findByKey("PROJECT_LEADER");
        if (role == null) {
            throw new AppException(ErrorCode.ROLE_NOT_FOUND);
        }
        Project newProject = Project.builder()
                .name(project.getName())
                .description(project.getDescription())
                .managerId(userId)
                .status(Status.ACTIVE)
                .startDate(project.getStartDate())
                .endDate(project.getEndDate())
                .teamSize(1)
                .progress(0.0)
                .build();
        projectRepository.save(newProject);

        ProjectUser projectUser = ProjectUser.builder()
                .projectId(newProject.getId())
                .userId(userId)
                .roleId(role.getId())
                .status(Status.ACTIVE)
                .joinedAt(new Date())
                .build();
        projectUserRepository.save(projectUser);

        UserProjectRole userProjectRole = UserProjectRole.builder()
                .userId(userId)
                .projectId(newProject.getId())
                .roleId(role.getId())
                .build();
        userProjectRepository.save(userProjectRole);

        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            throw new AppException(ErrorCode.USER_NOT_FOUND);
        }

        String token = authorityService.refreshUserToken(user , httpServletResponse);
    }

    @Override
    public ProjectResponse updateProject(String id, Project project) {
        return null;
    }

    @Override
    public void changeStatusProject(String id, ChangeStatusRequest status) {
        ObjectId objectId = new ObjectId(id);
        Project project = projectRepository.findById(objectId).orElse(null);
        if (project == null) {
            throw new AppException(ErrorCode.PROJECT_NOT_FOUND);
        } else {
            project.setStatus(status.getStatus());
            projectRepository.save(project);
        }
    }

    @Override
    public void addMemberToProject(ObjectId projectId, String email) {
        Project project = projectRepository.findById(projectId).orElse(null);
        if (project == null) {
            throw new AppException(ErrorCode.PROJECT_NOT_FOUND);
        }
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new AppException(ErrorCode.USER_NOT_FOUND);
        }
        Role role = roleRepository.findByKey("PROJECT_MEMBER");
        if (role == null) {
            throw new AppException(ErrorCode.ROLE_NOT_FOUND);
        }
        ProjectUser existingProjectUser = projectUserRepository.findByProjectIdAndUserId(projectId, user.getId());
        if (existingProjectUser != null) {
            throw new AppException(ErrorCode.USER_ALREADY_IN_PROJECT);
        }

        project.setTeamSize(project.getTeamSize() + 1);
        projectRepository.save(project);

        ProjectUser projectUser = ProjectUser.builder()
                .projectId(projectId)
                .userId(user.getId())
                .roleId(role.getId())
                .status(Status.ACTIVE)
                .joinedAt(new Date())
                .build();
        projectUserRepository.save(projectUser);

        UserProjectRole userProjectRole = UserProjectRole.builder()
                .userId(user.getId())
                .projectId(projectId)
                .roleId(role.getId())
                .build();
        userProjectRepository.save(userProjectRole);

    }

}