package exe.exe201be.controller;

import exe.exe201be.dto.request.ChangeStatusRequest;
import exe.exe201be.dto.request.CreateOrderRequest;
import exe.exe201be.dto.response.APIResponse;
import exe.exe201be.dto.response.OrderResponse;
import exe.exe201be.service.Order.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {
    @Autowired
    private OrderService orderService;

    @GetMapping("/user")
    @Operation(summary = "Get Orders by User ID", description = "Retrieve all orders associated with a specific user ID")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Orders retrieved successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = OrderResponse.class)
                    )
            )
    })
    public APIResponse<List<OrderResponse>> getOrderByUserId(@AuthenticationPrincipal Jwt jwt) {
        ObjectId id = new ObjectId(jwt.getSubject());
        APIResponse<List<OrderResponse>> response = new APIResponse<>();
        List<OrderResponse> orderResponse = orderService.getAllOrderByUserId(id);
        response.setMessage("Order retrieved successfully");
        response.setData(orderResponse);
        return response;
    }

    @PostMapping
    @Operation(summary = "Create Order", description = "Create a new order for a specific service package")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Order created successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = APIResponse.class)
                    )
            )
    })
    public APIResponse<?> createOrder(@AuthenticationPrincipal Jwt jwt, CreateOrderRequest createOrderRequest) {
        ObjectId id = new ObjectId(jwt.getSubject());
        ObjectId servicePackageObjId = new ObjectId(createOrderRequest.getServicePackageId());
        APIResponse<?> response = new APIResponse<>();
        orderService.createOrder(id, servicePackageObjId, createOrderRequest);
        response.setMessage("Create order successfully");
        return response;
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Update Order Status", description = "Update the status of an existing order by its ID")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Order status updated successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = APIResponse.class)
                    )
            )
    })
    public APIResponse<?> updateStatusOrder(@PathVariable String id, @RequestBody ChangeStatusRequest status) {
        ObjectId orderObjId = new ObjectId(id);
        APIResponse<?> response = new APIResponse<>();
        orderService.updateStatusOrder(orderObjId, status);
        response.setMessage("Update order status successfully");
        return response;
    }
}
