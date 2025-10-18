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
import java.time.*;
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

    @Autowired
    private ProjectRepository projectRepository;

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

    @Override
    public DashboardAdminResponse getDashboardAdmin() {
        long totalUsers = userRepository.count();
        long totalProjects = projectUserRepository.count();

        // Tính revenue
        List<Order> orders = orderRepository.findAll();
        Double totalRevenue = orders.stream()
                .filter(o -> o.getStatus() == Status.PAID)
                .mapToDouble(Order::getTotal)
                .sum();

        // Lấy toàn bộ user
        List<User> users = userRepository.findAll();

        // Nhóm user theo Year + Quarter
        Map<String, Long> grouped = users.stream()
                .filter(u -> u.getCreatedAt() != null)
                .collect(Collectors.groupingBy(u -> {
                    LocalDate date = u.getCreatedAt()
                            .atZone(ZoneId.systemDefault())
                            .toLocalDate();

                    int year = date.getYear();
                    int quarter = (date.getMonthValue() - 1) / 3 + 1;
                    return year + "-Q" + quarter;
                }, Collectors.counting()));

        // Convert map -> list CountUserByQuarter
        List<CountUserByQuarter> countUserByQuarter = grouped.entrySet().stream()
                .map(e -> {
                    String[] parts = e.getKey().split("-Q");
                    int year = Integer.parseInt(parts[0]);
                    int quarter = Integer.parseInt(parts[1]);
                    return new CountUserByQuarter(year, quarter, e.getValue());
                })
                .sorted(Comparator.comparing(CountUserByQuarter::getYear)
                        .thenComparing(CountUserByQuarter::getQuarter))
                .toList();

        // Build response
        DashboardAdminResponse resp = new DashboardAdminResponse();
        resp.setProjectCount(totalProjects);
        resp.setUserCount(totalUsers);
        resp.setTotalRevenue(totalRevenue);
        resp.setCountUserByQuarter(countUserByQuarter);
        return resp;
    }

    @Override
    public ProjectDashboardResponse getProjectDashboard() {
        // Lấy toàn bộ để tính trung bình & fallback created time
        // (Nếu dữ liệu lớn, bạn có thể chuyển sang aggregate trên DB)
        List<Project> projects = projectRepository.findAll();

        long total = projects.size();

        // Đếm Active / Completed theo status
        long active = projects.stream()
                .filter(p -> p.getStatus() == Status.ACTIVE)
                .count();

        long completed = projects.stream()
                .filter(p -> p.getStatus() == Status.COMPLETED)
                .count();

        // Avg progress (bỏ null)
        double avgProgress = projects.stream()
                .map(Project::getProgress)
                .filter(Objects::nonNull)
                .mapToDouble(Double::doubleValue)
                .average()
                .orElse(0.0);

        // New projects this month
        YearMonth nowYm = YearMonth.now();
        long newThisMonth = projects.stream()
                .filter(p -> {
                    LocalDate createdDate = resolveCreatedDate(p);
                    YearMonth ym = YearMonth.from(createdDate);
                    return ym.equals(nowYm);
                })
                .count();

        return ProjectDashboardResponse.builder()
                .totalProjects(total)
                .newProjectsThisMonth(newThisMonth)
                .avgProjectProgress(avgProgress)
                .activeProjects(active)
                .completedProjects(completed)
                .build();
    }

    public List<MonthlyProjectPoint> getMonthlyProjectTrend(int monthsBack) {
        if (monthsBack < 1) monthsBack = 6;

        // Lấy toàn bộ projects (nếu về sau quá lớn -> dùng aggregation ở DB)
        List<Project> projects = projectRepository.findAll();

        // Gom theo YearMonth của thời điểm tạo
        Map<YearMonth, Long> counts = projects.stream()
                .map(this::resolveCreatedDate)          // LocalDate
                .map(YearMonth::from)                   // YearMonth
                .collect(Collectors.groupingBy(
                        Function.identity(),
                        Collectors.counting()
                ));

        // Dải thời gian cần trả về: monthsBack gần nhất, theo thứ tự tăng dần
        YearMonth now = YearMonth.now();
        YearMonth start = now.minusMonths(monthsBack - 1);

        List<MonthlyProjectPoint> series = new ArrayList<>();
        YearMonth cursor = start;
        while (!cursor.isAfter(now)) {
            long c = counts.getOrDefault(cursor, 0L);
            series.add(MonthlyProjectPoint.builder()
                    .year(cursor.getYear())
                    .month(cursor.getMonthValue())   // 1..12
                    .created(c)
                    .build());
            cursor = cursor.plusMonths(1);
        }

        return series;
    }

    /** Ưu tiên field createdAt nếu có; nếu không suy ra từ ObjectId (Mongo) */
    private LocalDate resolveCreatedDate(Project p) {
        if (p.getCreatedAt() != null) {
            return p.getCreatedAt()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate();
        }
        // fallback từ ObjectId
        ObjectId id = p.getId();
        Instant created = Instant.ofEpochSecond(id.getTimestamp());
        return created.atZone(ZoneId.systemDefault()).toLocalDate();
    }

}
