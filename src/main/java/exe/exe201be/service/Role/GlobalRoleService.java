package exe.exe201be.service.Role;

import exe.exe201be.pojo.UserGlobalRole;
import org.bson.types.ObjectId;

public interface GlobalRoleService {
    UserGlobalRole getUserGlobalRoleByUserId(ObjectId userId);
}
