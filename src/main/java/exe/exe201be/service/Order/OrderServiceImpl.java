package exe.exe201be.service.Order;

import exe.exe201be.dto.response.OrderDetailResponse;
import exe.exe201be.dto.response.OrderResponse;
import exe.exe201be.dto.response.UserResponse;
import exe.exe201be.exception.AppException;
import exe.exe201be.exception.ErrorCode;
import exe.exe201be.pojo.Order;
import exe.exe201be.pojo.Payment;
import exe.exe201be.pojo.User;
import exe.exe201be.repository.OrderDetailRepository;
import exe.exe201be.repository.OrderRepository;
import exe.exe201be.repository.PaymentRepository;
import exe.exe201be.repository.UserRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderDetailRepository orderDetailRepository;

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public List<OrderResponse> getAllOrderByUserId(ObjectId userId) {
        List<Order> order = orderRepository.findByUserId(userId);
        User user = userRepository.findById(userId).orElse(null);
        if (order == null) {
            throw new AppException(ErrorCode.ORDER_NOT_FOUND);
        }
        if (user == null) {
            throw new AppException(ErrorCode.USER_NOT_FOUND);
        }
        Set<ObjectId> paymentIds = order.stream()
                .map(Order::getPaymentId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        Map<ObjectId, Payment> paymentMap = paymentRepository.findAllById(paymentIds).stream()
                .collect(Collectors.toMap(Payment::getId, Function.identity()));

        UserResponse userResponse = UserResponse.builder()
                .id(user.getId().toHexString())
                .email(user.getEmail())
                .fullName(user.getFullName())
                .avatarUrl(user.getAvatar_url())
                .gender(user.getGender())
                .image(user.getImage())
                .status(user.getStatus())
                .phone(user.getPhone())
                .address(user.getAddress())
                .build();

        return order.stream()
                .map(o -> {
                    Payment payment = paymentMap.get(o.getPaymentId());
                    return OrderResponse.builder()
                            .id(o.getId().toHexString())
                            .user(userResponse)
                            .payment(payment)
                            .total(o.getTotal())
                            .status(o.getStatus())
                            .createdAt(Date.from(o.getCreatedAt()))
                            .build();
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<OrderDetailResponse> getOrderDetailById(ObjectId orderDetailId) {
        return null;
    }

    @Override
    public void createOrder(ObjectId userId, ObjectId servicePackageId, int quantity) {

    }

    @Override
    public void updateStatusOrder(ObjectId orderId, String status) {

    }
}