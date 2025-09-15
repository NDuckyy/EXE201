package exe.exe201be.repository;

import exe.exe201be.pojo.ServiceProvider;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ServiceProviderRepository extends MongoRepository<ServiceProvider, ObjectId> {
    boolean existsByName(String name);
    boolean existsByContactEmail(String contactEmail);
    boolean existsByPhoneNumber(String phoneNumber);
}
