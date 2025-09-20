package exe.exe201be.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import exe.exe201be.pojo.Payment;
import exe.exe201be.pojo.type.Status;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderResponse {
    private String id;
    private UserResponse user;
    private Payment payment;
    private Double total;
    private Status status;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yy HH:mm:ss", timezone = "UTC")
    private Date createdAt;

}
