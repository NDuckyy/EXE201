package exe.exe201be.service.Role;

import exe.exe201be.exception.AppException;
import exe.exe201be.exception.ErrorCode;
import exe.exe201be.pojo.UserGlobalRole;
import exe.exe201be.repository.UserGlobalRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GlobalRoleServiceImpl implements  GlobalRoleService {
    @Autowired
    private UserGlobalRepository userGlobalRepository;

    @Override
    public UserGlobalRole getUserGlobalRoleByUserId(ObjectId userId) {
        UserGlobalRole userGlobalRole = userGlobalRepository.findByUserId(userId);
        if(userGlobalRole == null) {
            throw new AppException(ErrorCode.USER_GLOBAL_NOT_FOUND);
        }
        return userGlobalRole;
    }
}
