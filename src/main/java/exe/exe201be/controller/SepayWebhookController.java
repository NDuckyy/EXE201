package exe.exe201be.controller;

import exe.exe201be.dto.request.ChangeStatusRequest;
import exe.exe201be.dto.request.SepayWebhookRequest;
import exe.exe201be.dto.response.APIResponse;
import exe.exe201be.pojo.Order;
import exe.exe201be.pojo.type.Status;
import exe.exe201be.repository.OrderRepository;
import exe.exe201be.service.Order.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/webhook")
@RequiredArgsConstructor
public class SepayWebhookController {

    private final OrderService orderService;

    @PostMapping("/sepay")
    public ResponseEntity<APIResponse<String>> handleSepayWebhook(@RequestBody SepayWebhookRequest data) {
        APIResponse<String> response = new APIResponse<>();

        try {
            // 1. Kiểm tra description
            String ref = extractReference(data.getDescription());
            if (ref == null) {
                response.setCode(400);
                response.setMessage("Invalid or missing reference code in description");
                return ResponseEntity.badRequest().body(response);
            }

            // 2. Tìm order theo referenceCode
            Order order = orderService.findByReferenceCode(ref);
            if (order == null) {
                response.setCode(404);
                response.setMessage("Order not found for reference: " + ref);
                return ResponseEntity.status(404).body(response);
            }

            // 3. Kiểm tra trạng thái hiện tại
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

            // 4. Kiểm tra số tiền
            if (!Objects.equals(order.getTotal(), data.getAmount())) {
                // Nếu số tiền không khớp, có thể log lại và set FAILED
                orderService.updateStatusOrder(order.getId(),
                        ChangeStatusRequest.builder().status(Status.FAILED).build());

                response.setCode(400);
                response.setMessage("Amount mismatch. Expected: " + order.getTotal() + ", got: " + data.getAmount());
                return ResponseEntity.badRequest().body(response);
            }

            // 5. Happy case -> Update thành PAID
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

    // Helper để extract reference từ description
    private String extractReference(String description) {
        if (description != null && description.startsWith("PAY_")) {
            return description;
        }
        return null;
    }

}
