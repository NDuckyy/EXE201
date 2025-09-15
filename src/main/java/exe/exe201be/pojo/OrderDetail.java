package exe.exe201be.pojo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "order_details")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderDetail {

    @Id
    @JsonSerialize(using = ToStringSerializer.class)
    private ObjectId id;

    @Schema(description = "Order ID this item belongs to", example = "651200000000000000000001")
    @Field("order_id")
    @Indexed
    private ObjectId orderId;

    @Schema(description = "Package/Gig ID purchased in this item", example = "651100000000000000000002")
    @Field("package_id")
    @Indexed
    private ObjectId packageId;

    @Schema(description = "Unit price of the package at purchase time", example = "79.0")
    @Field("unit_price")
    @Indexed
    private Double unitPrice;

    @Schema(description = "Quantity purchased", example = "1")
    @Indexed
    private Integer quantity;

    @Schema(description = "Currency of the item", example = "USD")
    @Indexed
    private String currency;
}
