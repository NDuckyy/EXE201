package exe.exe201be.dto.request;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SepayWebhookRequest {
    private String id;
    private String account_number;
    private double amount;
    private String description;
    private String transaction_date;
}
