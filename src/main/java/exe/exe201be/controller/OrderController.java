package exe.exe201be.controller;

import exe.exe201be.dto.request.ChangeStatusRequest;
import exe.exe201be.dto.request.CreateOrderRequest;
import exe.exe201be.dto.request.SearchRequest;
import exe.exe201be.dto.response.APIResponse;
import exe.exe201be.dto.response.MonthlyRevenueResponse;
import exe.exe201be.dto.response.OrderResponse;
import exe.exe201be.dto.response.SearchResponse;
import exe.exe201be.pojo.Order;
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
        Order order = orderService.createOrder(id, servicePackageObjId, createOrderRequest);
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


    @GetMapping("/get-history-order")
    public APIResponse<SearchResponse<OrderResponse>> getHistoryOrder(@AuthenticationPrincipal Jwt jwt,
                                                                      @RequestParam(defaultValue = "1") int page,
                                                                      @RequestParam(defaultValue = "20") int size,
                                                                      @RequestParam(defaultValue = "createdAt") String sortBy,
                                                                      @RequestParam(defaultValue = "desc") String sortDir) {
        ObjectId id = new ObjectId(jwt.getSubject());
        APIResponse<SearchResponse<OrderResponse>> response = new APIResponse<>();
        SearchRequest req = new SearchRequest(page, size, sortBy, sortDir);
        SearchResponse<OrderResponse> orderResponses = orderService.getHistoryOrder(id, req);
        response.setMessage("History order retrieved successfully");
        response.setData(orderResponses);
        return response;
    }

    @GetMapping("/all-orders")
    @Operation(summary = "Get All Orders", description = "Retrieve all orders with pagination and sorting")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "All orders retrieved successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = SearchResponse.class))
            )
    })
    public APIResponse<SearchResponse<OrderResponse>> getAllOrders(@RequestParam(defaultValue = "1") int page,
                                                                   @RequestParam(defaultValue = "20") int size,
                                                                   @RequestParam(defaultValue = "createdAt") String sortBy,
                                                                   @RequestParam(defaultValue = "desc") String sortDir) {
        APIResponse<SearchResponse<OrderResponse>> response = new APIResponse<>();
        SearchRequest searchRequest = new SearchRequest(page, size, sortBy, sortDir);
        SearchResponse<OrderResponse> orderResponses = orderService.getAllOrders(searchRequest);
        response.setMessage("All orders retrieved successfully");
        response.setData(orderResponses);
        return response;
    }

    @GetMapping("/admin/monthly-revenue")
    public APIResponse<List<MonthlyRevenueResponse>> getMonthlyRevenue(@RequestParam  int year) {
        APIResponse<List<MonthlyRevenueResponse>> response = new APIResponse<>();
        String timezone = "Asia/Ho_Chi_Minh";
        List<MonthlyRevenueResponse> monthlyRevenue = orderService.getMonthlyRevenue(year, timezone);
        response.setMessage("Monthly revenue retrieved successfully");
        response.setData(monthlyRevenue);
        return response;
    }
}
