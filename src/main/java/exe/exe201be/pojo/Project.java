package exe.exe201be.pojo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.Instant;
import java.util.Date;

@Document(collection = "projects")
@Schema(description = "Project entity representing a project in the system")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Project {
    @Id
    private String id;

    @Schema(description = "Name of the project", example = "Project Alpha")
    @Indexed
    private String name;

    @Schema(description = "Description of the project", example = "This is a sample project description.")
    @Indexed
    private String description;

    @Schema(description = "Identifier of the project manager", example = "507f1f77bcf86cd799439011")
    @Indexed
    private String managerId;

    @Schema(description = "Status of the project", example = "active")
    @Indexed
    private String status;

    @Schema(description = "Start date of the project", example = "2023-01-01")
    @Field("start_date")
    @Indexed
    private Date startDate;

    @Schema(description = "End date of the project", example = "2023-12-31")
    @Field("end_date")
    @Indexed
    private Date endDate;

    @Schema(description = "Size of the project team", example = "10")
    @Indexed
    private Integer teamSize;

    @Schema(description = "Progress of the project in percentage", example = "75.5")
    @Indexed
    private Double progress;

    @CreatedDate
    @Schema(description = "Timestamp when the project was created")
    @Field("created_at")
    private Instant createdAt;

    @LastModifiedDate
    @Schema(description = "Timestamp when the project was last updated")
    @Field("updated_at")
    private Instant updatedAt;

}
