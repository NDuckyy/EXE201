package exe.exe201be.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class APIResponse<T> {
    @Schema(description = "Response status code", example = "200")
    private int code = 200;

    @Schema(description = "Response message", example = "Success")
    private  String message;

    @Schema(description = "Response data")
    private T data;

    @Schema(description = "Timestamp when the response was created", example = "2023-10-05T14:48:00.000+00:00")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yy HH:mm:ss", timezone = "UTC")
    private Date createdAt = new Date();

}
