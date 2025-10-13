package exe.exe201be.service.Dashboard;

import exe.exe201be.dto.response.*;
import exe.exe201be.exception.AppException;
import exe.exe201be.exception.ErrorCode;
import exe.exe201be.pojo.Order;
import exe.exe201be.pojo.OrderDetail;
import exe.exe201be.pojo.ServicePackage;
import exe.exe201be.pojo.ServiceProvider;
import exe.exe201be.pojo.type.Status;
import exe.exe201be.repository.*;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.Provider;
import java.util.*;
import java.util.stream.Collectors;

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
}
