package exe.exe201be.repository;

import exe.exe201be.pojo.Project;
import exe.exe201be.pojo.UserProjectRole;
import exe.exe201be.pojo.type.Status;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface ProjectRepository extends MongoRepository<Project, ObjectId> {

    Page<Project> findByNameContainingIgnoreCase(String name, Pageable pageable);

    List<Project> findAllByIdInAndStatus(Set<ObjectId> ids, Status status);

}
