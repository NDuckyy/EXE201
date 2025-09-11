package exe.exe201be.pojo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "payments")
@Schema(description = "Payment entity representing a payment method and its details")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Payment {
    @Id
    private String id;

    @Schema(description = "Payment method type", example = "Credit Card")
    @Indexed
    private String method;

    @Schema(description = "Name associated with the payment method", example = "Thẻ Visa/MasterCard")
    @Indexed
    private String name;

    @Schema(description = "Status of the payment method", example = "active")
    @Indexed
    private String status;
}
