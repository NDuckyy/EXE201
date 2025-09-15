package exe.exe201be.service.Role;

import exe.exe201be.pojo.Role;
import org.bson.types.ObjectId;

public interface RoleService {
    Role getRoleById(ObjectId id);
}
