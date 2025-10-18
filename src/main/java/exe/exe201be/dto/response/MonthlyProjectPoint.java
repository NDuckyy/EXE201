package exe.exe201be.dto.response;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MonthlyProjectPoint {
    private int year;      // ví dụ 2025
    private int month;     // 1..12
    private long created;  // số project tạo trong (year, month)
}
