package exe.exe201be.service.Task;

import exe.exe201be.dto.request.ChangeStatusRequest;
import exe.exe201be.pojo.Task;
import org.bson.types.ObjectId;

import java.util.List;

public interface TaskService {
    List<Task> getAllTasksByProjectId(ObjectId projectId);
    void changeTaskStatus(ObjectId taskId, ChangeStatusRequest status);
}
