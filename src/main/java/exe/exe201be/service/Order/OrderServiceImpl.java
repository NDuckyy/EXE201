package exe.exe201be.service.Order;

import exe.exe201be.dto.request.ChangeStatusRequest;
import exe.exe201be.dto.request.CreateOrderRequest;
import exe.exe201be.dto.response.OrderDetailResponse;
import exe.exe201be.dto.response.OrderResponse;
import exe.exe201be.dto.response.UserResponse;
import exe.exe201be.exception.AppException;
import exe.exe201be.exception.ErrorCode;
import exe.exe201be.pojo.*;
import exe.exe201be.pojo.type.Status;
import exe.exe201be.repository.*;
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

    @Autowired
    private ServicePackageRepository servicePackageRepository;

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
    public Order createOrder(ObjectId userId, ObjectId servicePackageId, CreateOrderRequest createOrderRequest) {
        String referenceCode = "PAY" + UUID.randomUUID().toString().substring(0, 8);
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            throw new AppException(ErrorCode.USER_NOT_FOUND);
        }
        ServicePackage servicePackage = servicePackageRepository.findById(servicePackageId).orElse(null);
        if (servicePackage == null) {
            throw new AppException(ErrorCode.SERVICE_PACKAGE_NOT_FOUND);
        }
        Payment payment = paymentRepository.findAllByMethod(createOrderRequest.getPaymentMethod());
        if (payment == null) {
            throw new AppException(ErrorCode.PAYMENT_NOT_FOUND);
        }

        Order order = Order.builder()
                .userId(userId)
                .paymentId(payment.getId())
                .total(servicePackage.getPrice() * createOrderRequest.getQuantity())
                .currency("VND")
                .referenceCode(referenceCode)
                .status(Status.PENDING)
                .build();
        orderRepository.save(order);

        OrderDetail orderDetail = OrderDetail.builder()
                .orderId(order.getId())
                .packageId(servicePackageId)
                .unitPrice(servicePackage.getPrice())
                .quantity(createOrderRequest.getQuantity())
                .build();

        orderDetailRepository.save(orderDetail);
        return order;
    }

    @Override
    public void updateStatusOrder(ObjectId orderId, ChangeStatusRequest status) {
        Order order = orderRepository.findById(orderId).orElse(null);
        if (order == null) {
            throw new AppException(ErrorCode.ORDER_NOT_FOUND);
        }
        order.setStatus(status.getStatus());
        orderRepository.save(order);
    }

    @Override
    public Optional<Order> getOrderById(ObjectId orderId) {
        Order order = orderRepository.findById(orderId).orElse(null);
        if (order == null) {
            throw new AppException(ErrorCode.ORDER_NOT_FOUND);
        }
        return Optional.of(order);
    }

    @Override
    public Order findByReferenceCode(String ref) {
        return orderRepository.findByReferenceCode(ref);
    }
}