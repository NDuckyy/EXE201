package exe.exe201be.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderDashboardResponse {
    private int year;
    private int month;
    private long orderCount;
    private double totalAmount;
}
