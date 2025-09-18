package exe.exe201be.repository;

import exe.exe201be.pojo.Task;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends MongoRepository<Task, ObjectId> {
    List<Task> findByProjectId(ObjectId projectId);
}
