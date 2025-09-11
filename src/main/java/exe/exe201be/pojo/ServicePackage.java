package exe.exe201be.pojo;

import exe.exe201be.pojo.type.Status;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.Instant;
import java.util.Date;

@Document(collection = "service_packages")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ServicePackage {

    @Id
    private String id;

    @Schema(description = "Provider (company/freelancer) ID", example = "651000000000000000000001")
    @Field("provider_id")
    @Indexed
    private String providerId;

    @Schema(description = "Gig name", example = "Market Research - Starter")
    @Indexed
    private String name;

    @Schema(description = "Gig description", example = "Gói nghiên cứu thị trường cơ bản cho SME: khảo sát nhanh, tổng hợp insight.")
    @Indexed
    private String description;

    @Schema(description = "Price", example = "149.0")
    @Indexed
    private Double price;

    @Schema(description = "Currency code", example = "USD")
    @Indexed
    private String currency;

    @Schema(description = "Duration in months", example = "1")
    @Field("duration_months")
    @Indexed
    private Integer durationMonths;

    @Schema(description = "Discount percent", example = "10")
    @Field("discount_percent")
    @Indexed
    private Integer discountPercent;

    @Schema(description = "status", example = "active")
    @Indexed
    private Status status;

    @Schema(description = "Created at (auto)")
    @Field("created_at")
    @CreatedDate
    private Instant createdAt;

    @Schema(description = "Updated at (auto)")
    @Field("updated_at")
    @LastModifiedDate
    private Instant updatedAt;
}
