package exe.exe201be.service.Order;

import exe.exe201be.dto.request.ChangeStatusRequest;
import exe.exe201be.dto.request.CreateOrderRequest;
import exe.exe201be.dto.request.SearchRequest;
import exe.exe201be.dto.response.*;
import exe.exe201be.exception.AppException;
import exe.exe201be.exception.ErrorCode;
import exe.exe201be.pojo.*;
import exe.exe201be.pojo.type.Status;
import exe.exe201be.repository.*;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

    @Autowired
    private ServiceProviderRepository serviceProviderRepository;

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

    @Override
    public SearchResponse<OrderResponse> getHistoryOrder(ObjectId userId, SearchRequest req) {
        // 1) Tìm provider theo user
        ServiceProvider provider = serviceProviderRepository.findByUserId(userId);
        if (provider == null) {
            throw new AppException(ErrorCode.SERVICE_PROVIDER_NOT_FOUND);
        }

        // 2) Lấy các package của provider
        List<ObjectId> packageIds = servicePackageRepository.findByProviderId(provider.getId())
                .stream().map(ServicePackage::getId).toList();

        // 3) Lấy orderIds từ OrderDetail theo các package
        List<ObjectId> orderIds = orderDetailRepository.findByPackageIdIn(packageIds)
                .stream().map(OrderDetail::getOrderId).distinct().toList();

        // 4) Nếu không có order -> trả về trang rỗng
        int page = (req.getPage() <= 0) ? 1 : req.getPage();
        int size = (req.getSize() <= 0) ? 20 : req.getSize();
        if (orderIds.isEmpty()) {
            return SearchResponse.<OrderResponse>builder()
                    .content(Collections.emptyList())
                    .totalElements(0)
                    .totalPages(0)
                    .page(page)
                    .size(size)
                    .build();
        }

        // 5) Phân trang + sort
        String sortBy = (req.getSortBy() == null || req.getSortBy().isBlank()) ? "createdAt" : req.getSortBy();
        Sort.Direction dir = "asc".equalsIgnoreCase(req.getSortDir()) ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(dir, sortBy));

        // 6) Lấy trang Order theo danh sách orderIds
        Page<Order> orderPage = orderRepository.findByIdIn(orderIds, pageable);
        List<Order> orders = orderPage.getContent();

        // 7) Gom userIds & paymentIds từ CHÍNH TRANG NÀY rồi fetch batch
        Set<ObjectId> userIds = orders.stream()
                .map(Order::getUserId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        Map<ObjectId, User> userByIds = userRepository.findAllById(userIds).stream()
                .collect(Collectors.toMap(User::getId, Function.identity()));

        Set<ObjectId> paymentIds = orders.stream()
                .map(Order::getPaymentId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        Map<ObjectId, Payment> paymentMap = paymentRepository.findAllById(paymentIds).stream()
                .collect(Collectors.toMap(Payment::getId, Function.identity()));

        // 8) Map ra OrderResponse (giống getAll... nhưng theo trang)
        List<OrderResponse> data = orders.stream().map(o -> {
            User u = userByIds.get(o.getUserId());
            Payment p = paymentMap.get(o.getPaymentId());

            UserResponse userResponse = null;
            if (u != null) {
                userResponse = UserResponse.builder()
                        .id(u.getId().toHexString())
                        .email(u.getEmail())
                        .fullName(u.getFullName())
                        .avatarUrl(u.getAvatar_url())
                        .gender(u.getGender())
                        .image(u.getImage())
                        .status(u.getStatus())
                        .phone(u.getPhone())
                        .address(u.getAddress())
                        .build();
            }

            return OrderResponse.builder()
                    .id(o.getId() != null ? o.getId().toHexString() : null)
                    .user(userResponse)
                    .payment(p)
                    .total(o.getTotal())
                    .status(o.getStatus())
                    .createdAt(o.getCreatedAt() != null ? Date.from(o.getCreatedAt()) : null)
                    .build();
        }).toList();

        // 9) Trả về SearchResponse phân trang
        return SearchResponse.<OrderResponse>builder()
                .content(data)
                .totalElements(orderPage.getTotalElements())
                .totalPages(orderPage.getTotalPages())
                .page(page)
                .size(size)
                .build();
    }

    @Override
    public SearchResponse<OrderResponse> getAllOrders(SearchRequest searchRequest) {
        // Phân trang + sắp xếp
        int page = (searchRequest.getPage() <= 0) ? 1 : searchRequest.getPage();
        int size = (searchRequest.getSize() <= 0) ? 20 : searchRequest.getSize();
        String sortBy = (searchRequest.getSortBy() == null || searchRequest.getSortBy().isBlank()) ? "createdAt" : searchRequest.getSortBy();
        Sort.Direction dir = "asc".equalsIgnoreCase(searchRequest.getSortDir()) ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(dir, sortBy));

        // Lấy trang Order
        Page<Order> orderPage = orderRepository.findAll(pageable);
        List<Order> orders = orderPage.getContent();

        // Gom userIds & paymentIds từ CHÍNH TRANG NÀY rồi fetch batch
        Set<ObjectId> userIds = orders.stream()
                .map(Order::getUserId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        Map<ObjectId, User> userByIds = userRepository.findAllById(userIds).stream()
                .collect(Collectors.toMap(User::getId, Function.identity()));

        Set<ObjectId> paymentIds = orders.stream()
                .map(Order::getPaymentId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        Map<ObjectId, Payment> paymentMap = paymentRepository.findAllById(paymentIds).stream()
                .collect(Collectors.toMap(Payment::getId, Function.identity()));

        // Map ra OrderResponse
        List<OrderResponse> data = orders.stream().map(o -> {
            User u = userByIds.get(o.getUserId());
            Payment p = paymentMap.get(o.getPaymentId());
            OrderDetail orderDetail = orderDetailRepository.findByOrderId(o.getId());
            ServicePackage servicePackage = servicePackageRepository.findById(orderDetail.getPackageId()).orElse(null);
            if(servicePackage == null){
                System.out.println("Service package not found for orderDetail: " + orderDetail.getId());
                throw new AppException(ErrorCode.SERVICE_PACKAGE_NOT_FOUND);
            }

            UserResponse userResponse = null;
            if (u != null) {
                userResponse = UserResponse.builder()
                        .id(u.getId().toHexString())
                        .email(u.getEmail())
                        .fullName(u.getFullName())
                        .avatarUrl(u.getAvatar_url())
                        .gender(u.getGender())
                        .image(u.getImage())
                        .status(u.getStatus())
                        .phone(u.getPhone())
                        .address(u.getAddress())
                        .build();
            }

            ServicePackageResponse servicePackageResponse = ServicePackageResponse.builder()
                    .id(servicePackage.getId() != null ? servicePackage.getId().toHexString() : null)
                    .name(servicePackage.getName())
                    .description(servicePackage.getDescription())
                    .price(servicePackage.getPrice())
                    .status(servicePackage.getStatus())
                    .build();

            OrderDetailResponse orderDetailResponse = null;
            if (orderDetail != null) {
                orderDetailResponse = OrderDetailResponse.builder()
                        .id(orderDetail.getId() != null ? orderDetail.getId().toHexString() : null)
                        .orderId(orderDetail.getOrderId() != null ? orderDetail.getOrderId().toHexString() : null)
                        .servicePackage(servicePackageResponse)
                        .unit_price(orderDetail.getUnitPrice())
                        .quantity(orderDetail.getQuantity())
                        .build();
            }

            return OrderResponse.builder()
                    .id(o.getId() != null ? o.getId().toHexString() : null)
                    .user(userResponse)
                    .orderDetail(orderDetailResponse)
                    .payment(p)
                    .total(o.getTotal())
                    .status(o.getStatus())
                    .createdAt(o.getCreatedAt() != null ? Date.from(o.getCreatedAt()) : null)
                    .build();
        }).toList();

        // 9) Trả về SearchResponse phân trang
        return SearchResponse.<OrderResponse>builder()
                .content(data)
                .totalElements(orderPage.getTotalElements())
                .totalPages(orderPage.getTotalPages())
                .page(page)
                .size(size)
                .build();
    }

}