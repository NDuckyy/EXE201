package exe.exe201be.repository;

import exe.exe201be.pojo.Role;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends CrudRepository<Role, Long> {
    Role findById(String id);
}
