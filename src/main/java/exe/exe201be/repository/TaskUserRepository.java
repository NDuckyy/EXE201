package exe.exe201be.repository;

import exe.exe201be.pojo.TaskUser;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskUserRepository extends MongoRepository<TaskUser, ObjectId> {
    List<TaskUser> findByUserId(ObjectId userId);
    long countByUserId(ObjectId userId);
}
