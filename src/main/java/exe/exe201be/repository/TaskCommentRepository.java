package exe.exe201be.repository;

import exe.exe201be.pojo.TaskComment;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface TaskCommentRepository extends MongoRepository<TaskComment, ObjectId> {
    List<TaskComment> findByTaskId(ObjectId taskId);
}
