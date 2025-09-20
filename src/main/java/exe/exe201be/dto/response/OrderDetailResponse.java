package exe.exe201be.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderDetailResponse {
    private String id;
    private String orderId;
    private ServicePackageResponse servicePackage;
    private int quantity;
    private double unit_price;

}
