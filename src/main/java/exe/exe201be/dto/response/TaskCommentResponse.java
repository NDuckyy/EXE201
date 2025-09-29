package exe.exe201be.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskCommentResponse {
    private String id;
    private String taskId;
    private String userId;
    private String content;
    private Instant createdAt;

    private String userName;
    private String avatarUrl;
}
