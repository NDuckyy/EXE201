package exe.exe201be.dto.request;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SearchRequest {
    String keyword;
    int page;
    int size;
    String sortBy;
    String sortDir;
}
