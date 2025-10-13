package exe.exe201be.repository;

import exe.exe201be.pojo.OrderDetail;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface OrderDetailRepository extends MongoRepository<OrderDetail, ObjectId> {
    Collection<Object> findByPackageId(ObjectId id);

    List<OrderDetail> findByPackageIdIn(List<ObjectId> collect);
}
