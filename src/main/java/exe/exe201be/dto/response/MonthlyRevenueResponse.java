package exe.exe201be.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MonthlyRevenueResponse {
    private int year;    // 2025
    private int month;   // 1..12
    private double total; // tổng tiền trong tháng (VND)
}
