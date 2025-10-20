package exe.exe201be.repository;

import exe.exe201be.pojo.ServiceProvider;
import exe.exe201be.pojo.type.Status;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ServiceProviderRepository extends MongoRepository<ServiceProvider, ObjectId> {
    boolean existsByName(String name);
    boolean existsByContactEmail(String contactEmail);
    boolean existsByPhoneNumber(String phoneNumber);

    ServiceProvider findByUserId(ObjectId id);

    List<ServiceProvider> findByStatus(Status status);
}
