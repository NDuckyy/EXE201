package exe.exe201be.service.Task;

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
}
