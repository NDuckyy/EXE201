package exe.exe201be.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SepayWebhookRequest {

    private Long id;

    private String gateway;

    @JsonProperty("transactionDate")
    private String transactionDate;

    @JsonProperty("accountNumber")
    private String accountNumber;

    @JsonProperty("subAccount")
    private String subAccount;

    private String code;

    private String content;

    @JsonProperty("transferType")
    private String transferType; // "in" hoặc "out"

    private String description;

    @JsonProperty("transferAmount")
    private Double transferAmount;

    @JsonProperty("referenceCode")
    private String referenceCode;

    private Double accumulated;
}
