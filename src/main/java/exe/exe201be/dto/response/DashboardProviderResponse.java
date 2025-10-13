package exe.exe201be.dto.response;

import lombok.*;

import java.util.List;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DashboardProviderResponse {
    Double totalRevenue;
    Integer totalOrder;
    Integer totalServicePackage;
    String mostPopularServicePackage;
    List<RevenueResponse> revenueEachServicePackage;
    List<StatusPackageResponse> statusEachServicePackage;
}
