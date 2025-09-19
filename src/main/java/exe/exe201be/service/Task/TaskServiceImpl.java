package exe.exe201be.service.Task;

import exe.exe201be.dto.request.ChangeStatusRequest;
import exe.exe201be.exception.AppException;
import exe.exe201be.exception.ErrorCode;
import exe.exe201be.pojo.Task;
import exe.exe201be.repository.TaskRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskServiceImpl implements  TaskService {
    @Autowired
    private TaskRepository taskRepository;

    @Override
    public List<Task> getAllTasksByProjectId(ObjectId projectId) {
        return taskRepository.findByProjectId(projectId);
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
}
