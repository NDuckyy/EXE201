package exe.exe201be.pojo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import exe.exe201be.pojo.type.Status;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.Instant;
import java.util.Date;

@Document(collection = "service_providers")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ServiceProvider {

    @Id
    @JsonSerialize(using = ToStringSerializer.class)
    private ObjectId id;

    @Schema(description = "User ID associated with the service provider", example = "651000000000000000000002")
    @Field("user_id")
    private ObjectId userId;

    @Schema(description = "Name of the service provider", example = "Shine Solutions")
    @Indexed
    private String name;

    @Schema(description = "Contact email of the service provider", example = "support@shine-solutions.example")
    @Field("contact_email")
    @Indexed
    private String contactEmail;

    @Schema(description = "Phone number of the service provider", example = "+84 28 1234 5678")
    @Field("phone_number")
    @Indexed
    private String phoneNumber;

    @Schema(description = "Address of the service provider", example = "District 1, Ho Chi Minh City, Vietnam")
    @Indexed
    private String address;

    @Schema(description = "Website of the service provider", example = "https://shine-solutions.example")
    @Indexed
    private String website;

    @Schema(description = "Status of the service provider", example = "active")
    @Indexed
    private Status status;

    @Schema(description = "Timestamp when the service provider was created")
    @Field("created_at")
    @CreatedDate
    private Instant createdAt;

    @Schema(description = "Timestamp when the service provider was last updated")
    @Field("updated_at")
    @LastModifiedDate
    private Instant updatedAt;
}
