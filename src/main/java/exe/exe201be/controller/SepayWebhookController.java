package exe.exe201be.controller;

import exe.exe201be.dto.request.ChangeStatusRequest;
import exe.exe201be.dto.request.SepayWebhookRequest;
import exe.exe201be.dto.response.APIResponse;
import exe.exe201be.pojo.Order;
import exe.exe201be.pojo.type.Status;
import exe.exe201be.service.Order.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RestController
@RequestMapping("/api/webhook")
@RequiredArgsConstructor
public class SepayWebhookController {

    private final OrderService orderService;

    @Value("${api-key-webhook-payment}")
    private String sepayWebhookApiKey;

    @PostMapping("/sepay")
    public ResponseEntity<APIResponse<String>> handleSepayWebhook(
            @RequestBody SepayWebhookRequest data,
            @RequestHeader(value = "Authorization", required = false) String authHeader
    ) {
        APIResponse<String> response = new APIResponse<>();

        try {
            // 0. Xác thực API Key
            String expectedHeader = "Apikey " + sepayWebhookApiKey;
            if (authHeader == null || !authHeader.equals(expectedHeader)) {
                response.setCode(401);
                response.setMessage("Unauthorized: Invalid API Key");
                return ResponseEntity.status(401).body(response);
            }

            String ref = extractReference(data.getContent(), data.getDescription());
            if (ref == null) {
                response.setCode(400);
                response.setMessage("Invalid or missing reference code in content/description");
                return ResponseEntity.badRequest().body(response);
            }

            if (ref == null) {
                response.setCode(400);
                response.setMessage("Invalid or missing reference code in description");
                return ResponseEntity.badRequest().body(response);
            }

            Order order = orderService.findByReferenceCode(ref);
            if (order == null) {
                response.setCode(404);
                response.setMessage("Order not found for reference: " + ref);
                return ResponseEntity.status(404).body(response);
            }

            if (order.getStatus() == Status.PAID) {
                response.setCode(200);
                response.setMessage("Order already marked as PAID");
                return ResponseEntity.ok(response);
            }
            if (order.getStatus() == Status.CANCELLED) {
                response.setCode(400);
                response.setMessage("Order was cancelled, cannot update");
                return ResponseEntity.badRequest().body(response);
            }

            if (!Objects.equals(order.getTotal(), data.getTransferAmount())) {
                orderService.updateStatusOrder(order.getId(),
                        ChangeStatusRequest.builder().status(Status.FAILED).build());

                response.setCode(400);
                response.setMessage("Amount mismatch. Expected: " + order.getTotal() + ", got: " + data.getTransferAmount());
                return ResponseEntity.badRequest().body(response);
            }

            orderService.updateStatusOrder(order.getId(),
                    ChangeStatusRequest.builder().status(Status.PAID).build());

            System.out.println("✅ Order " + order.getId() + " đã thanh toán thành công.");

            response.setCode(200);
            response.setMessage("Order " + order.getId() + " updated to PAID");
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            response.setCode(500);
            response.setMessage("Internal server error: " + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }

    private String extractReference(String content, String description) {
        // Ưu tiên tìm trong content
        String ref = findPayCode(content);
        if (ref != null) return ref;

        // Fallback sang description
        return findPayCode(description);
    }

    private String findPayCode(String text) {
        if (text == null) return null;

        // Regex: tìm từ "PAY" + các ký tự chữ/số liền sau
        java.util.regex.Pattern pattern = java.util.regex.Pattern.compile("(PAY[\\w\\d]+)");
        java.util.regex.Matcher matcher = pattern.matcher(text);
        if (matcher.find()) {
            return matcher.group(1); // chỉ lấy "PAYd6da959d"
        }
        return null;
    }


}
