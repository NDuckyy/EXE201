package exe.exe201be.service.Dashboard;

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

import java.security.Provider;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class DashboardServiceImpl implements DashboardService {

    @Autowired
    private ProjectUserRepository projectUserRepository;

    @Autowired
    private TaskUserRepository taskUserRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ServicePackageRepository servicePackageRepository;

    @Autowired
    private ServiceProviderRepository serviceProviderRepository;

    @Autowired
    private OrderDetailRepository orderDetailRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public TotalResponse countData(ObjectId userId) {
        long projectCount = projectUserRepository.countByUserId(userId);
        long taskCount = taskUserRepository.countByUserId(userId);
        return TotalResponse.builder()
                .totalProjects(projectCount)
                .totalTasks(taskCount)
                .build();
    }

    @Override
    public DashboardProviderResponse getDashboardProvider(ObjectId userId) {
        ServiceProvider provider = serviceProviderRepository.findByUserId(userId);
        if (provider == null) {
            throw new AppException(ErrorCode.SERVICE_PROVIDER_NOT_FOUND);
        }
        List<ServicePackage> servicePackages = servicePackageRepository.findByProviderId(provider.getId());
        List<OrderDetail> getOrderByPackage = orderDetailRepository.findByPackageIdIn(
                servicePackages.stream().map(ServicePackage::getId).collect(Collectors.toList())
        );
        List<Order> getOrders = orderRepository.findByIdIn(
                getOrderByPackage.stream().map(OrderDetail::getOrderId).distinct().collect(Collectors.toList())
        );
        Set<ObjectId> packageIds = getOrderByPackage.stream()
                .map(OrderDetail::getPackageId)
                .collect(Collectors.toSet());

        Double totalRevenue = getOrders.stream()
                .filter(order -> order.getStatus() == Status.PAID)
                .mapToDouble(Order::getTotal)
                .sum();

        Integer totalOrders = getOrders.size();

        Integer totalServices = servicePackageRepository.countByProviderId(provider.getId());

        Map<ObjectId, Long> packageCountMap = getOrderByPackage.stream()
                .collect(Collectors.groupingBy(OrderDetail::getPackageId, Collectors.counting()));
        String mostPopularServicePackage = null;
        Long maxCount = 0L;
        for (Map.Entry<ObjectId, Long> entry : packageCountMap.entrySet()) {
            if (entry.getValue() > maxCount) {
                maxCount = entry.getValue();
                mostPopularServicePackage = servicePackageRepository.findById(entry.getKey())
                        .map(ServicePackage::getName)
                        .orElse(null);
            }
        }

        Map<String, Double> revenueMap = getOrderByPackage.stream()
                .filter(orderDetail -> getOrders.stream()
                        .anyMatch(order -> order.getStatus() == Status.PAID
                                && order.getId().equals(orderDetail.getOrderId())))
                .map(orderDetail -> {
                    ServicePackage servicePackage = servicePackageRepository.findById(orderDetail.getPackageId()).orElse(null);
                    if (servicePackage != null) {
                        double revenue = orderDetail.getUnitPrice() * orderDetail.getQuantity();
                        return new AbstractMap.SimpleEntry<>(servicePackage.getName(), revenue);
                    }
                    return null;
                })
                .filter(Objects::nonNull)
                .collect(Collectors.groupingBy(
                        Map.Entry::getKey,
                        Collectors.summingDouble(Map.Entry::getValue)
                ));

        List<RevenueResponse> revenueResponses = revenueMap.entrySet().stream()
                .map(entry -> RevenueResponse.builder()
                        .servicePackageName(entry.getKey())
                        .revenue(entry.getValue())
                        .build())
                .toList();

        List<StatusPackageResponse> statusResponses = getOrders.stream()
                .collect(Collectors.groupingBy(Order::getStatus, Collectors.counting()))
                .entrySet().stream()
                .map(entry -> StatusPackageResponse.builder()
                        .status(entry.getKey())
                        .quantity(entry.getValue().intValue())
                        .build()
                )
                .toList();


        return DashboardProviderResponse.builder()
                .totalRevenue(totalRevenue)
                .totalOrder(totalOrders)
                .totalServicePackage(totalServices)
                .mostPopularServicePackage(mostPopularServicePackage)
                .revenueEachServicePackage(revenueResponses)
                .statusEachServicePackage(statusResponses)
                .build();

    }

    @Override
    public List<OrderDashboardResponse> getOrderByMonth(ObjectId userId, int year) {
        ServiceProvider provider = serviceProviderRepository.findByUserId(userId);
        if (provider == null) {
            throw new AppException(ErrorCode.SERVICE_PROVIDER_NOT_FOUND);
        }

        List<ServicePackage> servicePackages =
                servicePackageRepository.findByProviderId(provider.getId());

        List<ObjectId> packageIds = servicePackages.stream()
                .map(ServicePackage::getId)
                .collect(Collectors.toList());

        List<OrderDetail> orderDetails =
                orderDetailRepository.findByPackageIdIn(packageIds);

        List<ObjectId> orderIds = orderDetails.stream()
                .map(OrderDetail::getOrderId)
                .distinct()
                .collect(Collectors.toList());

        if (orderIds.isEmpty()) {
            return IntStream.rangeClosed(1, 12)
                    .mapToObj(m -> new OrderDashboardResponse(year, m, 0L, 0.0))
                    .collect(Collectors.toList());
        }

        ZoneId zone = ZoneId.of("Asia/Ho_Chi_Minh");
        Instant start = LocalDate.of(year, 1, 1).atStartOfDay(zone).toInstant();
        Instant end = LocalDate.of(year + 1, 1, 1).atStartOfDay(zone).toInstant();

        List<Order> orders = orderRepository.findByIdInAndCreatedAtBetween(orderIds, start, end);

        orders = orders.stream().filter(o -> o.getStatus() == Status.PAID).toList();

        Map<Integer, List<Order>> byMonth = orders.stream()
                .collect(Collectors.groupingBy(o ->
                        LocalDateTime.ofInstant(o.getCreatedAt(), zone).getMonthValue()
                ));

        return IntStream.rangeClosed(1, 12)
                .mapToObj(month -> {
                    List<Order> inMonth = byMonth.getOrDefault(month, Collections.emptyList());
                    long orderCount = inMonth.size();
                    double totalAmount = inMonth.stream()
                            .map(o -> o.getTotal() == null ? 0.0 : o.getTotal())
                            .mapToDouble(Double::doubleValue)
                            .sum();

                    return new OrderDashboardResponse(year, month, orderCount, totalAmount);
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<CountOrderByServiceResponse> CountOrderByServiceAndProvider(ObjectId userId) {
        ServiceProvider provider = serviceProviderRepository.findByUserId(userId);
        if (provider == null) {
            throw new AppException(ErrorCode.SERVICE_PROVIDER_NOT_FOUND);
        }

        List<ServicePackage> servicePackages =
                servicePackageRepository.findByProviderId(provider.getId());

        List<ObjectId> packageIds = servicePackages.stream()
                .map(ServicePackage::getId)
                .collect(Collectors.toList());

        List<OrderDetail> orderDetails =
                orderDetailRepository.findByPackageIdIn(packageIds);

        List<ObjectId> orderIds = orderDetails.stream()
                .map(OrderDetail::getOrderId)
                .distinct()
                .collect(Collectors.toList());

        if (orderIds.isEmpty()) {
            return Collections.emptyList();
        }

        // ✅ Không lọc theo năm, lấy tất cả đơn hàng của provider
        List<Order> orders = orderRepository.findByIdIn(orderIds);

        // Chỉ lấy đơn hàng đã thanh toán
        orders = orders.stream()
                .filter(o -> o.getStatus() == Status.PAID)
                .toList();

        List<Order> finalOrders = orders;

        Map<ObjectId, Long> orderCountByPackage = orderDetails.stream()
                .filter(od -> finalOrders.stream().anyMatch(o -> o.getId().equals(od.getOrderId())))
                .collect(Collectors.groupingBy(OrderDetail::getPackageId, Collectors.counting()));

        List<CountOrderByServiceResponse> responses = new ArrayList<>();

        for (Map.Entry<ObjectId, Long> entry : orderCountByPackage.entrySet()) {
            servicePackageRepository.findById(entry.getKey()).ifPresent(servicePackage ->
                    responses.add(new CountOrderByServiceResponse(
                            servicePackage.getName(),
                            entry.getValue()
                    ))
            );
        }

        return responses;
    }

}
