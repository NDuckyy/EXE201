package exe.exe201be.dto.request;

import exe.exe201be.pojo.type.Status;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateServicePackageRequest {

    @Schema(description = "Gig name", example = "Market Research - Starter")
    private String name;

    @Schema(description = "Gig description", example = "Gói nghiên cứu thị trường cơ bản cho SME: khảo sát nhanh, tổng hợp insight.")
    private String description;

    @Schema(description = "Price", example = "149.0")
    private Double price;

    @Schema(description = "Currency code", example = "USD")
    private String currency;

    @Schema(description = "Duration in months", example = "1")
    private Integer durationMonths;

    @Schema(description = "Discount percent", example = "10")
    private Integer discountPercent;

    @Schema(description = "List of features included in the package", example = "[\"Feature 1\", \"Feature 2\"]")
    private List<String> features;

    @Schema(description = "Scope of services", example = "[\"Basic\", \"Standard\"]")
    private List<String> serviceScope;

    @Schema(description = "Estimated delivery times", example = "[\"3 days\", \"5 days\"]")
    private List<String> estimatedDelivery;

    @Schema(description = "Image URL", example = "https://example.com/image.jpg")
    private String image;

    @Schema(description = "status", example = "active")
    private Status status;

}
