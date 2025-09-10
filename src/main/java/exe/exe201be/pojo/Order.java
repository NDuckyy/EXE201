package exe.exe201be.pojo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.Instant;
import java.util.Date;

@Document(collection = "orders")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Order {

    @Id
    private String id;

    @Schema(description = "ID of the user who created the order", example = "64f000000000000000000001")
    @Field("user_id")
    @Indexed
    private String userId;

    @Schema(description = "Payment transaction ID", example = "651300000000000000000001")
    @Field("payment_id")
    @Indexed
    private String paymentId;

    @Schema(description = "Total amount of the order", example = "98.0")
    @Indexed
    private Double total;

    @Schema(description = "Currency of the order", example = "USD")
    @Indexed
    private String currency;

    @Schema(description = "Order status", example = "paid")
    @Indexed
    private String status;

    @Schema(description = "Date when the order was created")
    @Field("created_at")
    @CreatedDate
    private Instant createdAt;
}
