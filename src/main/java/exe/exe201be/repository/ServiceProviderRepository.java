package exe.exe201be.repository;

import exe.exe201be.pojo.ServiceProvider;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ServiceProviderRepository extends MongoRepository<ServiceProvider, String> {
}
