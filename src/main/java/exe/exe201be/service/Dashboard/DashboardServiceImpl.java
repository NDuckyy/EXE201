package exe.exe201be.service.Dashboard;

import exe.exe201be.dto.response.TotalResponse;
import exe.exe201be.pojo.type.Status;
import exe.exe201be.repository.ProjectRepository;
import exe.exe201be.repository.ProjectUserRepository;
import exe.exe201be.repository.TaskRepository;
import exe.exe201be.repository.TaskUserRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DashboardServiceImpl implements DashboardService{

    @Autowired
    private ProjectUserRepository projectUserRepository;

    @Autowired
    private TaskUserRepository taskUserRepository;

    @Override
    public TotalResponse countData(ObjectId userId) {
        long projectCount = projectUserRepository.countByUserId(userId);
        long taskCount = taskUserRepository.countByUserId(userId);
        return TotalResponse.builder()
                .totalProjects(projectCount)
                .totalTasks(taskCount)
                .build();
    }
}
