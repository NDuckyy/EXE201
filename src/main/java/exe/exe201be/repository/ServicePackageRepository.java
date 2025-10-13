package exe.exe201be.repository;

import exe.exe201be.pojo.ServicePackage;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ServicePackageRepository extends MongoRepository<ServicePackage, ObjectId> {
    Page<ServicePackage> findByNameContainingIgnoreCase(String keyword, Pageable pageable);

    List<ServicePackage> findByProviderId(ObjectId objectId);

    Integer countByProviderId(ObjectId providerId);
}
