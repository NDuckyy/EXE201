package exe.exe201be.service.Dashboard;

import exe.exe201be.dto.request.SearchRequest;
import exe.exe201be.dto.response.*;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;

import java.util.List;

public interface DashboardService {
    TotalResponse countData(ObjectId managerId);

    DashboardProviderResponse getDashboardProvider(ObjectId providerId);

    List<OrderDashboardResponse> getOrderByMonth(ObjectId managerId, int year);

    List<CountOrderByServiceResponse> CountOrderByServiceAndProvider(ObjectId userId);

    DashboardAdminResponse getDashboardAdmin();

    ProjectDashboardResponse getProjectDashboard();

    List<MonthlyProjectPoint> getMonthlyProjectTrend(int monthsBack);

}
