package exe.exe201be.repository;

import exe.exe201be.pojo.ServicePackage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ServicePackageRepository extends MongoRepository<ServicePackage, String> {
    Page<ServicePackage> findByNameContainingIgnoreCase(String keyword, Pageable pageable);
}
