package exe.exe201be.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CountUserByQuarter {
    private int year;
    private int quarter;
    private long count;
}
