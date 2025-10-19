package exe.exe201be.repository;

import exe.exe201be.pojo.Order;
import exe.exe201be.pojo.type.Status;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends MongoRepository<Order, ObjectId> {
    List<Order> findByUserId(ObjectId userId);

    Order findByReferenceCode(String ref);

    List<Order> findByIdIn(List<ObjectId> collect);

    List<Order> findByIdInAndCreatedAtBetween(Collection<ObjectId> orderIds, Instant start, Instant end);

    Page<Order> findByIdIn(List<ObjectId> ids, Pageable pageable);

    List<Order> findByCreatedAtBetweenAndStatusIn(Instant from, Instant to, List<Status> countedStatuses);
}
