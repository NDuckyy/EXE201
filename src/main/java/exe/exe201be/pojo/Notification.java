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

@Document(collection = "notifications")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Notification {

    @Id
    private String id;

    @Schema(description = "Notification type", example = "message")
    @Indexed
    private String type;

    @Schema(description = "Notification content", example = "Bạn có tin nhắn mới từ Alice")
    @Indexed
    private String content;

    @Schema(description = "Flag whether notification has been read", example = "false")
    @Field("is_read")
    @Indexed
    private Boolean isRead;

    @Schema(description = "User ID who receives this notification", example = "64f000000000000000000001")
    @Field("user_id")
    @Indexed
    private String userId;

    @Schema(description = "Date when notification was created")
    @Field("created_at")
    @CreatedDate
    private Instant createdAt;
}
