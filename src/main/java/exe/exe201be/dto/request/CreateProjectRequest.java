package exe.exe201be.dto.request;

import exe.exe201be.pojo.type.Status;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.bson.types.ObjectId;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateProjectRequest {

    @Schema(description = "Name of the project", example = "Project Alpha")
    private String name;

    @Schema(description = "Description of the project", example = "This is a sample project description.")
    private String description;

    @Schema(description = "Start date of the project (yyyy-MM-dd)", example = "2023-01-01")
    private Date startDate;

    @Schema(description = "End date of the project (yyyy-MM-dd)", example = "2023-12-31")
    private Date endDate;

    /* ✅ Các field mới (optional) */
    @Schema(description = "Short introduction for guests or project overview",
            example = "Ứng dụng ngân hàng di động giúp người dùng chuyển tiền nhanh chóng và an toàn.",
            nullable = true)
    private String shortIntro;

    @Schema(description = "Highlighted achievements or key selling point",
            example = "Hơn 10.000 người dùng trong tháng đầu ra mắt.",
            nullable = true)
    private String highlight;

    @Schema(description = "Demo URL for guests to explore or view the product",
            example = "https://demo.mybankapp.com",
            nullable = true)
    private String demoUrl;

    @Schema(description = "List of screenshot image URLs for project showcase",
            example = "[\"https://cdn.example.com/screen1.png\", \"https://cdn.example.com/screen2.png\"]",
            nullable = true)
    private List<String> screenshots;
}
