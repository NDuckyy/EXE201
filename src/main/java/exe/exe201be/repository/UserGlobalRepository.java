package exe.exe201be.repository;

import exe.exe201be.pojo.UserGlobalRole;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface    UserGlobalRepository extends MongoRepository<UserGlobalRole, ObjectId> {
    UserGlobalRole findByUserId(ObjectId userId);
}
