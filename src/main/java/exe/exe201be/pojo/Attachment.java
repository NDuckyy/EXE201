package exe.exe201be.pojo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
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

@Document(collection = "attachments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Attachment {

    @Id
    @JsonSerialize(using = ToStringSerializer.class)
    private ObjectId id;

    @Schema(description = "ID of the task this file belongs to", example = "64f444000000000000000001")
    @Field("task_id")
    @Indexed
    private ObjectId taskId;

    @Schema(description = "URL of the uploaded file", example = "https://example.com/files/wireframe_homepage.png")
    @Field("file_url")
    @Indexed
    private String fileUrl;

    @Schema(description = "User ID of who uploaded this file", example = "64f000000000000000000001")
    @Field("uploaded_by")
    @Indexed
    private String uploadedBy;

    @Schema(description = "Timestamp when the file was uploaded")
    @Field("uploaded_at")
    @CreatedDate
    private Instant uploadedAt;
}
