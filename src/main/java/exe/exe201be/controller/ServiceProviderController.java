package exe.exe201be.controller;

import exe.exe201be.dto.request.CreateServiceProviderRequest;
import exe.exe201be.dto.response.APIResponse;
import exe.exe201be.dto.response.ServiceProviderResponse;
import exe.exe201be.pojo.ServiceProvider;
import exe.exe201be.service.ServiceProvider.ServiceProviderService;
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
@RequestMapping("/api/service-providers")
public class ServiceProviderController {
    @Autowired
    private ServiceProviderService serviceProviderService;

    @GetMapping
    @Operation(summary = "Get All Service Providers", description = "Retrieve a list of all service providers")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successful operation",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ServiceProvider.class))
            )
    })
    public APIResponse<List<ServiceProvider>> getAllServiceProviders() {
        APIResponse<List<ServiceProvider>> response = new APIResponse<>();
        response.setMessage("Success");
        response.setData(serviceProviderService.getAllServiceProviders());
        return response;
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get Service Provider by ID", description = "Retrieve a service provider by its ID")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successful operation",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ServiceProvider.class))
            )
    })
    public APIResponse<ServiceProvider> getServiceProviderById(@PathVariable String id) {
        APIResponse<ServiceProvider> response = new APIResponse<>();
        response.setMessage("Get service provider by id success");
        response.setData(serviceProviderService.getServiceProviderById(id));
        return response;
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Change Status of Service Provider", description = "Toggle the active status of a service provider by its ID")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Status changed successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = APIResponse.class))
            )
    })
    public APIResponse<?> changeStatusServiceProvider(@PathVariable String id) {
        APIResponse<?> response = new APIResponse<>();
        serviceProviderService.changeStatusServiceProvider(id);
        response.setMessage("Change status service provider success");
        return response;
    }

    @PostMapping
    @Operation(summary = "Create Service Provider", description = "Create a new service provider")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Service provider created successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ServiceProviderResponse.class))
            )
    })
    public APIResponse<ServiceProviderResponse> createServiceProvider(@RequestBody  CreateServiceProviderRequest request , @AuthenticationPrincipal Jwt jwt) {
        ObjectId userId = new ObjectId(jwt.getSubject());
        ServiceProviderResponse serviceProviderResponse = serviceProviderService.createServiceProvider(userId, request);
        APIResponse<ServiceProviderResponse> response = new APIResponse<>();
        response.setMessage("Create service provider success");
        response.setData(serviceProviderResponse);
        return response;
    }

}
