package exe.exe201be.dto.response;

import exe.exe201be.pojo.ServiceProvider;
import exe.exe201be.pojo.type.Status;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ServicePackageResponse {
    private String id;
    private ServiceProviderResponse providerId;
    private String name;
    private String description;
    private double price;
    private String currency;
    private int durationMonths;
    private int discountPercent;
    private List<String> features;
    private String image;
    private Status status;
}
