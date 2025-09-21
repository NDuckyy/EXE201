package exe.exe201be.repository;

import exe.exe201be.pojo.ProjectUser;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectUserRepository extends MongoRepository<ProjectUser, ObjectId> {
    long countByUserId(ObjectId userId);
}
