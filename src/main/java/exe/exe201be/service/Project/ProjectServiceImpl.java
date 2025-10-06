package exe.exe201be.service.Project;

import com.nimbusds.jwt.SignedJWT;
import exe.exe201be.dto.request.ChangeStatusRequest;
import exe.exe201be.dto.request.CreateProjectRequest;
import exe.exe201be.dto.request.SearchRequest;
import exe.exe201be.dto.response.ProjectResponse;
import exe.exe201be.dto.response.SearchResponse;
import exe.exe201be.dto.response.UserResponse;
import exe.exe201be.exception.AppException;
import exe.exe201be.exception.ErrorCode;
import exe.exe201be.pojo.*;
import exe.exe201be.pojo.type.Status;
import exe.exe201be.repository.*;
import exe.exe201be.service.Authority.AuthorityService;
import exe.exe201be.service.Mail.MailService;
import exe.exe201be.utils.JwtTokenGenerator;
import jakarta.servlet.http.HttpServletResponse;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

    @Autowired
    private JwtTokenGenerator jwtTokenGenerator;

    @Autowired
    private MailService mailService;


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
                .demoUrl(project.getDemoUrl())
                .highlight(project.getHighlight())
                .screenshots(project.getScreenshots())
                .shortIntro(project.getShortIntro())
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
                .shortIntro(project.getShortIntro())
                .highlight(project.getHighlight())
                .demoUrl(project.getDemoUrl())
                .screenshots(project.getScreenshots())
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

        String token = authorityService.refreshUserToken(user, httpServletResponse);
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

    @Override
    public void verifyEmail(String token, String email, String projectId) {
        try {
            SignedJWT jwt = SignedJWT.parse(token);
            Date exp = jwt.getJWTClaimsSet().getExpirationTime();

            if (exp.before(new Date())) {
                throw new AppException(ErrorCode.TOKEN_EXPIRED);
            }

            User user = userRepository.findByEmail(email);
            if (user == null) throw new AppException(ErrorCode.USER_NOT_FOUND);
            ObjectId projectObjId = new ObjectId(projectId);
            addMemberToProject(projectObjId, email);

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void sendInvitationEmail(String email, String projectId) {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new AppException(ErrorCode.USER_NOT_FOUND);
        }
        Project project = projectRepository.findById(new ObjectId(projectId)).orElse(null);
        if (project == null) {
            throw new AppException(ErrorCode.PROJECT_NOT_FOUND);
        }
        String token = jwtTokenGenerator.generateEmailVerifyToken(email);

        mailService.sendVerificationEmail(email, token, projectId);
    }

    @Override
    public SearchResponse<ProjectResponse> searchProjects(SearchRequest request) {
        Pageable pageable = PageRequest.of(
                request.getPage() - 1,
                request.getSize(),
                Sort.by(Sort.Direction.fromString(request.getSortDir()), request.getSortBy()));
        Page<Project> projects = projectRepository.findByNameContainingIgnoreCase(request.getKeyword(), pageable);
        if (projects.hasContent()) {
            return SearchResponse.<ProjectResponse>builder()
                    .content(projects.stream().map(p -> {
                        User user = userRepository.findById(p.getManagerId()).orElse(null);
                        return getProjectResponse(p, user);
                    }).collect(Collectors.toList()))
                    .page(projects.getNumber() + 1)
                    .size(projects.getSize())
                    .totalPages(projects.getTotalPages())
                    .totalElements(projects.getTotalElements())
                    .build();
        } else {
            throw new AppException(ErrorCode.PROJECT_NOT_FOUND);
        }
    }

    @Override
    public List<ProjectResponse> getProjectsByUserId(ObjectId userId, HttpServletResponse httpServletResponse) {
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            throw new AppException(ErrorCode.USER_NOT_FOUND);
        }
        List<ProjectUser> projectUsers = projectUserRepository.findByUserId(userId);
        if (projectUsers.isEmpty()){
            throw new AppException(ErrorCode.PROJECT_NOT_FOUND);
        }
        Set<ObjectId> projectIds = projectUsers.stream()
                .map(ProjectUser::getProjectId)
                .collect(Collectors.toSet());

        Map<ObjectId, Project> project = projectRepository.findAllById(projectIds).stream()
                .collect(Collectors.toMap(Project::getId, Function.identity()));

        String token = authorityService.refreshUserToken(user, httpServletResponse);

        return projectUsers.stream()
                .map(pu -> {
                    Project p = project.get(pu.getProjectId());
                    User manager = userRepository.findById(p.getManagerId()).orElse(null);
                    return getProjectResponse(p, manager);
                })
                .collect(Collectors.toList());

    }
}