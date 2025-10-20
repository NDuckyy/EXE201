package exe.exe201be.service.Task;

import exe.exe201be.dto.request.ChangeStatusRequest;
import exe.exe201be.dto.request.CreateTaskRequest;
import exe.exe201be.dto.response.RecentTaskResponse;
import exe.exe201be.dto.response.TaskResponse;
import exe.exe201be.pojo.Task;
import org.bson.types.ObjectId;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.List;

public interface TaskService {
    List<TaskResponse> getAllTasksByProjectId(ObjectId projectId);
    void changeTaskStatus(ObjectId taskId, ChangeStatusRequest status);
    Task createTask(CreateTaskRequest task, ObjectId projectId, ObjectId userId);
    TaskResponse getTaskById(ObjectId taskId);

    List<RecentTaskResponse> getRecentTasksForUser(ObjectId userId);
}
