package exe.exe201be.service.TaskComment;

import exe.exe201be.dto.response.TaskCommentResponse;
import exe.exe201be.pojo.TaskComment;
import exe.exe201be.pojo.User;
import exe.exe201be.repository.TaskCommentRepository;
import exe.exe201be.repository.UserRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
public class TaskCommentService {
    @Autowired
    private TaskCommentRepository commentRepository;

    @Autowired
    private UserRepository userRepository;

    public List<TaskCommentResponse> getCommentsByTaskId(ObjectId taskId) {
        List<TaskComment> comments = commentRepository.findByTaskId(taskId);

        return comments.stream().map(c -> {
            User user = userRepository.findById(c.getUserId()).orElse(null);

            return TaskCommentResponse.builder()
                    .id(c.getId().toHexString())
                    .taskId(c.getTaskId().toHexString())
                    .userId(c.getUserId().toHexString())
                    .content(c.getContent())
                    .createdAt(c.getCreatedAt())
                    .userName(user != null ? user.getFullName() : "Unknown User")
                    .avatarUrl(user != null ? user.getAvatar_url() : "/default-avatar.png")
                    .build();
        }).toList();
    }

    public TaskCommentResponse addComment(ObjectId taskId, ObjectId userId, String content) {
        TaskComment comment = TaskComment.builder()
                .taskId(taskId)
                .userId(userId)
                .content(content)
                .createdAt(Instant.now())
                .build();

        TaskComment saved = commentRepository.save(comment);
        User user = userRepository.findById(userId).orElse(null);

        return TaskCommentResponse.builder()
                .id(saved.getId().toHexString())
                .taskId(saved.getTaskId().toHexString())
                .userId(saved.getUserId().toHexString())
                .content(saved.getContent())
                .createdAt(saved.getCreatedAt())
                .userName(user != null ? user.getFullName() : "Unknown User")
                .avatarUrl(user != null ? user.getAvatar_url() : "https://i.pravatar.cc/")
                .build();
    }
}
