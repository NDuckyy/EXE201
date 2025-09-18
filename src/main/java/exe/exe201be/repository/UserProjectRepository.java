package exe.exe201be.repository;

import exe.exe201be.pojo.UserProjectRole;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserProjectRepository extends MongoRepository<UserProjectRole, ObjectId> {
    List<UserProjectRole> findByUserId(ObjectId userId);
}
