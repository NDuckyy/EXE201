package exe.exe201be.controller;

import exe.exe201be.dto.request.SearchRequest;
import exe.exe201be.dto.response.APIResponse;
import exe.exe201be.dto.response.SearchResponse;
import exe.exe201be.pojo.ServicePackage;
import exe.exe201be.service.ServicePackage.ServicePackageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/service-packages")
public class ServicePackageController {
    @Autowired
    private ServicePackageService servicePackageService;

    @GetMapping
    @Operation(summary = "Get All Service Packages", description = "Retrieve a list of all service packages")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successful operation",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ServicePackage.class))
            ),
    })
    public APIResponse<List<ServicePackage>> getAllServicePackages() {
        APIResponse<List<ServicePackage>> response = new APIResponse<>();
        response.setMessage("Success");
        response.setData(servicePackageService.getAllServicePackages());
        return response;
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get Service Package by ID", description = "Retrieve a service package by its ID")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successful operation",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ServicePackage.class))
            ),
    })
    public APIResponse<ServicePackage> getServicePackageById(@PathVariable String id) {
        APIResponse<ServicePackage> response = new APIResponse<>();
        response.setMessage("Get service package by id success");
        response.setData(servicePackageService.getServicePackageById(id));
        return response;
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Change Status of Service Package", description = "Toggle the status of a service package between ACTIVE and INACTIVE")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Status updated successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = APIResponse.class))
            ),
    })
    public APIResponse<?> changeStatusServicePackage(@PathVariable String id) {
        APIResponse<?> response = new APIResponse<>();
        servicePackageService.changeStatusServicePackage(id);
        response.setMessage("Update status success");
        return response;
    }

    @GetMapping("/search")
    @Operation(summary = "Search Service Packages", description = "Search for service packages with pagination and sorting")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Search completed successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = APIResponse.class))
            ),
    })
    public APIResponse<SearchResponse<ServicePackage>> searchServicePackages(
            @RequestParam(value = "keyword", required = false, defaultValue = "") String keyword,
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "size", required = false, defaultValue = "10") int size,
            @RequestParam(value = "sortBy", required = false, defaultValue = "createdAt") String sortBy,
            @RequestParam(value = "sortDir", required = false, defaultValue = "desc") String sortDir
    ) {
        SearchRequest searchRequest = new SearchRequest(keyword, page, size, sortBy, sortDir);

        APIResponse<SearchResponse<ServicePackage>> response = new APIResponse<>();
        response.setMessage("Search service packages success");
        response.setData(servicePackageService.searchServicePackages(searchRequest));
        return response;
    }
}
