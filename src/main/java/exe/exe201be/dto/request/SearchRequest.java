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

    public SearchRequest(int page, int size, String sortBy, String sortDir) {
        this.page = page;
        this.size = size;
        this.sortBy = sortBy;
        this.sortDir = sortDir;
    }
}
