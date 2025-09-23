package exe.exe201be.service.Task;

import exe.exe201be.dto.request.ChangeStatusRequest;
import exe.exe201be.dto.request.CreateTaskRequest;
import exe.exe201be.dto.response.ProjectResponse;
import exe.exe201be.dto.response.TaskResponse;
import exe.exe201be.dto.response.UserResponse;
import exe.exe201be.exception.AppException;
import exe.exe201be.exception.ErrorCode;
import exe.exe201be.pojo.*;
import exe.exe201be.pojo.type.Status;
import exe.exe201be.repository.ProjectRepository;
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
