package exe.exe201be.repository;

import exe.exe201be.pojo.ProjectUser;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface ProjectUserRepository extends MongoRepository<ProjectUser, ObjectId> {
    long countByUserId(ObjectId userId);

    ProjectUser findByProjectIdAndUserId(ObjectId projectId, ObjectId userId);

    List<ProjectUser> findByUserId(ObjectId userId);

    List<ProjectUser> findByUserIdAndRoleId(ObjectId userId, ObjectId id);

    void deleteByProjectId(ObjectId id);
}
