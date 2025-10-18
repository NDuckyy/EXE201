package exe.exe201be.controller;

import exe.exe201be.dto.response.*;
import exe.exe201be.service.Dashboard.DashboardService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {
    @Autowired
    private DashboardService dashboardService;

    @GetMapping("/member-data")
    public APIResponse<TotalResponse> getMemberData(@AuthenticationPrincipal Jwt jwt) {
        ObjectId id = new ObjectId(jwt.getSubject());
        APIResponse<TotalResponse> response = new APIResponse<>();
        TotalResponse totalResponse = dashboardService.countData(id);
        response.setMessage("Dashboard data retrieved successfully");
        response.setData(totalResponse);
        return response;
    }

    @GetMapping("/provider-data")
    public APIResponse<DashboardProviderResponse> getProviderData(@AuthenticationPrincipal Jwt jwt) {
        ObjectId id = new ObjectId(jwt.getSubject());
        APIResponse<DashboardProviderResponse> response = new APIResponse<>();
        DashboardProviderResponse dashboardProviderResponse = dashboardService.getDashboardProvider(id);
        response.setMessage("Dashboard data retrieved successfully");
        response.setData(dashboardProviderResponse);
        return response;
    }

    @GetMapping("provider-orders-report" )
    public APIResponse<List<OrderDashboardResponse>> getProviderOrdersReport(@AuthenticationPrincipal Jwt jwt, @RequestParam int year) {
        ObjectId id = new ObjectId(jwt.getSubject());
        APIResponse<List<OrderDashboardResponse>> response = new APIResponse<>();
        List<OrderDashboardResponse> orderDashboardResponses = dashboardService.getOrderByMonth(id, year);
        response.setMessage("Provider orders report retrieved successfully");
        response.setData(orderDashboardResponses);
        return response;
    }

    @GetMapping("count-order-by-service" )
    public APIResponse<List<CountOrderByServiceResponse>> getCountOrderByService(@AuthenticationPrincipal Jwt jwt) {
        ObjectId id = new ObjectId(jwt.getSubject());
        APIResponse<List<CountOrderByServiceResponse>> response = new APIResponse<>();
        List<CountOrderByServiceResponse> countOrderByServiceResponses = dashboardService.CountOrderByServiceAndProvider(id);
        response.setMessage("Count order by service retrieved successfully");
        response.setData(countOrderByServiceResponses);
        return response;
    }

    @GetMapping("/admin-data")
    public  APIResponse<DashboardAdminResponse> getDashboardAdmin(){
        APIResponse<DashboardAdminResponse> response = new APIResponse<>();
        DashboardAdminResponse dashboardAdminResponse = dashboardService.getDashboardAdmin();
        response.setMessage("Dashboard data retrieved successfully");
        response.setData(dashboardAdminResponse);
        return response;
    }

    @GetMapping("/admin-data-project")
    public APIResponse<ProjectDashboardResponse> getProjectDashboard() {
        APIResponse<ProjectDashboardResponse> res = new APIResponse<>();
        ProjectDashboardResponse data = dashboardService.getProjectDashboard();
        res.setMessage("Fetched project dashboard successfully");
        res.setData(data);
        return res;
    }

    @GetMapping("/projects/monthly-trend")
    public APIResponse<List<MonthlyProjectPoint>> getMonthlyTrend(
            @RequestParam(defaultValue = "6") int months // ví dụ 6 tháng gần nhất
    ) {
        APIResponse<List<MonthlyProjectPoint>> res = new APIResponse<>();
        List<MonthlyProjectPoint> data = dashboardService.getMonthlyProjectTrend(months);
        res.setCode(200);
        res.setMessage("Fetched monthly project trend successfully");
        res.setData(data);
        return res;
    }
}
