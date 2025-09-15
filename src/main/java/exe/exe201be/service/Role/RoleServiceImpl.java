package exe.exe201be.service.Role;

import exe.exe201be.exception.AppException;
import exe.exe201be.exception.ErrorCode;
import exe.exe201be.pojo.Role;
import exe.exe201be.repository.RoleRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleServiceImpl implements RoleService{
    @Autowired
    private RoleRepository roleRepository;

    @Override
    public Role getRoleById(ObjectId roleId) {
        Role role = roleRepository.findById(roleId).orElse(null);
        if(role == null) {
            throw new AppException(ErrorCode.ROLE_NOT_FOUND);
        }
        return role;
    }
}
