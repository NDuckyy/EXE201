package exe.exe201be.service.Authority;

import exe.exe201be.pojo.*;
import exe.exe201be.repository.*;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AuthorityService {
    @Autowired
    private UserProjectRepository userProjectRepository;

    @Autowired
    private TaskUserRepository taskUserRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserGlobalRepository userGlobalRepository;
    @Autowired
    private ProjectRepository projectRepository;


    public List<String> getAuthoritiesForUser(String userId) {
        List<String> authorities = new ArrayList<>();
        ObjectId id = new ObjectId(userId);
        UserGlobalRole userGlobalRole = userGlobalRepository.findByUserId(id);

        Role role = roleRepository.findById(userGlobalRole.getRoleId()).orElse(null);
        if (role != null) {
            authorities.add(role.getKey());
        }

        // Project roles
        List<UserProjectRole> projectRoles = userProjectRepository.findByUserId(id);
        for (UserProjectRole upr : projectRoles) {
            roleRepository.findById(upr.getRoleId()).ifPresent(
                    role1 -> authorities.add("PROJECT_" + upr.getProjectId()
                            + "_" + role1.getKey()));
        }

        // Task roles
        List<TaskUser> taskRoles = taskUserRepository.findByUserId(id);
        for (TaskUser utr : taskRoles) {
            roleRepository.findById(utr.getRoleId()).ifPresent(
                    taskRole -> authorities.add("PROJECT_" + utr.getProjectId()
                            + "_TASK_" + utr.getTaskId()
                            + "_" + taskRole.getKey()));
        }

        return authorities;
    }
}

