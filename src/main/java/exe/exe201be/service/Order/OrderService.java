package exe.exe201be.service.Order;

import exe.exe201be.dto.response.OrderDetailResponse;
import exe.exe201be.dto.response.OrderResponse;
import exe.exe201be.pojo.Order;
import exe.exe201be.pojo.OrderDetail;
import org.bson.types.ObjectId;

import java.util.List;

public interface OrderService {
    List<OrderResponse> getAllOrderByUserId(ObjectId userId);
    List<OrderDetailResponse> getOrderDetailById(ObjectId orderDetailId);
    void createOrder(ObjectId userId, ObjectId servicePackageId, int quantity);
    void updateStatusOrder(ObjectId orderId, String status);
}
