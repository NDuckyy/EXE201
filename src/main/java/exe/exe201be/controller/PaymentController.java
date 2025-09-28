package exe.exe201be.controller;

import exe.exe201be.dto.request.CreateOrderRequest;
import exe.exe201be.dto.response.APIResponse;
import exe.exe201be.dto.response.OrderResponse;
import exe.exe201be.pojo.Order;
import exe.exe201be.pojo.type.Status;
import exe.exe201be.repository.OrderRepository;
import exe.exe201be.service.Order.OrderService;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.*;

@RestController
@RequestMapping("/api/payment")
@RequiredArgsConstructor
public class PaymentController {

    private final OrderService orderService;

    @PostMapping("/create-order")
    public ResponseEntity<APIResponse<OrderResponse>> createOrder(
            @AuthenticationPrincipal Jwt jwt,
            @RequestBody CreateOrderRequest createOrderRequest) {

        ObjectId id = new ObjectId(jwt.getSubject());
        ObjectId servicePackageObjId = new ObjectId(createOrderRequest.getServicePackageId());

        Order order = orderService.createOrder(id, servicePackageObjId, createOrderRequest);

        // Sinh link QR
        String qrUrl = "https://img.vietqr.io/image/970423-99992692004-compact2.png" +
                "?amount=" + order.getTotal() +
                "&addInfo=" + order.getReferenceCode() +
                "&accountName=LE HUY VU";

        // Gắn QR vào referenceCode (giữ nguyên logic bạn viết)
        OrderResponse responseOrder = OrderResponse.builder()
                .id(order.getId().toString())
                .QRLink(qrUrl)
                .createdAt(Date.from(order.getCreatedAt()))
                .build();


        // Trả về APIResponse
        APIResponse<OrderResponse> response = new APIResponse<>();
        response.setCode(200);
        response.setMessage("Success");
        response.setData(responseOrder);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/orders/{id}")
    public ResponseEntity<APIResponse<OrderResponse>> getOrder(@PathVariable String id) {
        APIResponse<OrderResponse> response = new APIResponse<>();

        try {
            ObjectId objectId = new ObjectId(id);
            return orderService.getOrderById(objectId)
                    .map(order -> {
                        // Build QR link giống createOrder
                        String qrUrl = "https://img.vietqr.io/image/970423-99992692004-compact2.png" +
                                "?amount=" + order.getTotal() +
                                "&addInfo=" + order.getReferenceCode() +
                                "&accountName=LE HUY VU";

                        // Map sang DTO OrderResponse
                        OrderResponse responseOrder = OrderResponse.builder()
                                .id(order.getId().toString())
                                .user(null) // TODO: map UserResponse nếu cần
                                .payment(order.getPaymentId() != null ? null : null)
                                .total(order.getTotal())
                                .status(order.getStatus())
                                .createdAt(Date.from(order.getCreatedAt()))
                                .QRLink(qrUrl)
                                .build();

                        response.setCode(200);
                        response.setMessage("Success");
                        response.setData(responseOrder);
                        return ResponseEntity.ok(response);
                    })
                    .orElseGet(() -> {
                        response.setCode(404);
                        response.setMessage("Order not found");
                        return ResponseEntity.status(404).body(response);
                    });
        } catch (IllegalArgumentException e) {
            response.setCode(400);
            response.setMessage("Invalid order id");
            return ResponseEntity.badRequest().body(response);
        }
    }

}

