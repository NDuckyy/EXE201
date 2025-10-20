package exe.exe201be.service.Task;

import exe.exe201be.dto.request.ChangeStatusRequest;
import exe.exe201be.dto.request.CreateTaskRequest;
import exe.exe201be.dto.response.ProjectResponse;
import exe.exe201be.dto.response.RecentTaskResponse;
import exe.exe201be.dto.response.TaskResponse;
import exe.exe201be.dto.response.UserResponse;
import exe.exe201be.exception.AppException;
import exe.exe201be.exception.ErrorCode;
import exe.exe201be.pojo.*;
import exe.exe201be.pojo.type.Status;
import exe.exe201be.repository.ProjectRepository;
import exe.exe201be.repository.ProjectUserRepository;
import exe.exe201be.repository.TaskRepository;
import exe.exe201be.repository.UserRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class TaskServiceImpl implements TaskService {
    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProjectUserRepository projectUserRepository;

    @Override
    public List<TaskResponse> getAllTasksByProjectId(ObjectId projectId) {
        List<Task> tasks = taskRepository.findByProjectId(projectId);
        if (tasks.isEmpty())
            return null;

        Set<ObjectId> users = tasks.stream()
                .map(Task::getCreatedBy)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        Map<ObjectId, User> usersById = userRepository.findAllById(users).stream()
                .collect(Collectors.toMap(User::getId, Function.identity()));

        Project project = projectRepository.findById(projectId).orElse(null);

        return tasks.stream().map(
                t -> {
                    User user = usersById.get(t.getCreatedBy());
                    return getTaskResponse(t, user, project);
                }).collect(Collectors.toList());
    }

    @Override
    public void changeTaskStatus(ObjectId taskId, ChangeStatusRequest status) {
        Task task = taskRepository.findById(taskId).orElse(null);

        if (task != null) {
            task.setStatus(status.getStatus());
            taskRepository.save(task);
        } else {
            throw new AppException(ErrorCode.TASK_NOT_FOUND);
        }
    }

    @Override
    public Task createTask(CreateTaskRequest task, ObjectId projectId, ObjectId userId) {
        Task newTask = Task.builder()
                .name(task.getName())
                .description(task.getDescription())
                .projectId(projectId)
                .status(task.getStatus())
                .priority(task.getPriority())
                .dueDate(task.getDueDate())
                .createdBy(userId)
                .build();
        return taskRepository.save(newTask);
    }

    @Override
    public TaskResponse getTaskById(ObjectId taskId) {
        Task t = taskRepository.findById(taskId).orElse(null);
        if (t == null)
            throw new AppException(ErrorCode.TASK_NOT_FOUND);

        User user = userRepository.findById(t.getCreatedBy()).orElse(null);
        Project project = projectRepository.findById(t.getProjectId()).orElse(null);

        return getTaskResponse(t, user, project);
    }

    public List<RecentTaskResponse> getRecentTasksForUser(ObjectId userId) {
        // 1) Lấy các project mà user thuộc (member/leader)
        List<ProjectUser> pus = projectUserRepository.findByUserId(userId);
        if (pus.isEmpty()) return List.of();

        List<ObjectId> projectIds = pus.stream()
                .map(ProjectUser::getProjectId)
                .distinct()
                .toList();

        // 2) Lấy 5 task mới nhất trong các project đó
        List<Task> tasks = taskRepository.findTop5ByProjectIdInOrderByCreatedAtDesc(projectIds);
        if (tasks.isEmpty()) return List.of();

        // 3) Nạp tên project để map response
        var projectMap = projectRepository.findAllById(
                tasks.stream().map(Task::getProjectId).distinct().toList()
        ).stream().collect(Collectors.toMap(Project::getId, Function.identity()));

        // 4) Map sang DTO gọn
        return tasks.stream().map(t -> {
            Project p = projectMap.get(t.getProjectId());
            return RecentTaskResponse.builder()
                    .id(t.getId().toHexString())
                    .title(t.getName())                 // đổi theo field thực tế (name/subject/summary)// nếu schema có, không thì bỏ
                    .status(t.getStatus() != null ? t.getStatus().name() : null)
                    .projectId(t.getProjectId().toHexString())
                    .projectName(p != null ? p.getName() : null)
                    .build();
        }).toList();
    }

    private TaskResponse getTaskResponse(Task t, User user, Project project) {
        UserResponse userResponse = user != null ? UserResponse.builder()
                .id(user.getId().toHexString())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .avatarUrl("https://i.pravatar.cc/")
                .phone(user.getPhone())
                .address(user.getAddress())
                .build() : null;
        ProjectResponse projectResponse = project != null ? ProjectResponse.builder()
                .id(project.getId().toHexString())
                .name(project.getName())
                .description(project.getDescription())
                .status(project.getStatus())
                .build() : null;
        return TaskResponse.builder()
                .id(t.getId().toHexString())
                .name(t.getName())
                .description(t.getDescription())
                .projectId(projectResponse)
                .status(t.getStatus())
                .priority(t.getPriority())
                .dueDate(t.getDueDate())
                .createdBy(userResponse)
                .build();
    }
}
