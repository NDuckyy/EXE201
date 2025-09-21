package exe.exe201be.controller;

import exe.exe201be.dto.response.APIResponse;
import exe.exe201be.dto.response.TotalResponse;
import exe.exe201be.service.Dashboard.DashboardService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
