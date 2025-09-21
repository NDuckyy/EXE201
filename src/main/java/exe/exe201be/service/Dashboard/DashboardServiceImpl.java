package exe.exe201be.service.Dashboard;

import exe.exe201be.dto.response.TotalResponse;
import exe.exe201be.pojo.type.Status;
import exe.exe201be.repository.ProjectRepository;
import exe.exe201be.repository.TaskRepository;
import exe.exe201be.repository.TaskUserRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DashboardServiceImpl implements DashboardService{

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private TaskUserRepository taskUserRepository;

    @Override
    public TotalResponse countData(ObjectId managerId) {
        long projectCount = projectRepository.countByManagerIdAndStatus(managerId, Status.ACTIVE);
        long taskCount = taskUserRepository.countByUserId(managerId);
        return TotalResponse.builder()
                .totalProjects(projectCount)
                .totalTasks(taskCount)
                .build();
    }
}
