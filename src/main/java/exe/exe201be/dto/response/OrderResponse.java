package exe.exe201be.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import exe.exe201be.pojo.Payment;
import exe.exe201be.pojo.type.Status;
import io.swagger.v3.core.util.Json;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrderResponse {
    private String id;
    private UserResponse user;
    private Payment payment;
    private Double total;
    private Status status;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yy HH:mm:ss", timezone = "UTC")
    private Date createdAt;
    private String QRLink;

}
