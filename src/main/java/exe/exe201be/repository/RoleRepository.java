package exe.exe201be.repository;

import exe.exe201be.pojo.Role;
import org.bson.types.ObjectId;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoleRepository extends CrudRepository<Role, ObjectId> {
    Role findByKey(String key);
}
