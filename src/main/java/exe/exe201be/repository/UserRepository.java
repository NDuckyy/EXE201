package exe.exe201be.repository;

import exe.exe201be.pojo.User;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends MongoRepository<User, ObjectId> {
    User findByFullName(String fullName);

    User findByEmail(String email);

    boolean existsByEmail(String email);
}
