package exe.exe201be.dto.response;


import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DashboardAdminResponse {
    private long projectCount;
    private long userCount;
    private Double totalRevenue;
    private List<CountUserByQuarter> countUserByQuarter;

}
