package exe.exe201be.dto.request;

import exe.exe201be.dto.response.OrderDetailResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateOrderRequest {
    private String servicePackageId;
    private int quantity;
    private String paymentMethod;
}
