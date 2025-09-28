package exe.exe201be.pojo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import exe.exe201be.pojo.type.Status;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.bson.types.ObjectId;
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
@Builder(toBuilder = true)
public class Order {

    @Id
    @JsonSerialize(using = ToStringSerializer.class)
    private ObjectId id;

    @Schema(description = "ID of the user who created the order", example = "64f000000000000000000001")
    @Field("user_id")
    @Indexed
    private ObjectId userId;

    @Schema(description = "Payment transaction ID", example = "651300000000000000000001")
    @Field("payment_id")
    @Indexed
    private ObjectId paymentId;

    @Schema(description = "Total amount of the order", example = "98.0")
    @Indexed
    private Double total;

    @Schema(description = "Currency of the order", example = "USD")
    @Indexed
    private String currency;

    @Schema(description = "Order status", example = "paid")
    @Indexed
    private Status status;

    @Schema(description = "Date when the order was created")
    @Field("created_at")
    @CreatedDate
    private Instant createdAt;

    @Field("reference_code")
    @Indexed
    private String referenceCode; // PAY_xxx để khớp webhook

}
