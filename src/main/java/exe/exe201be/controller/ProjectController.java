package exe.exe201be.controller;

import exe.exe201be.dto.request.AddMemberRequest;
import exe.exe201be.dto.request.ChangeStatusRequest;
import exe.exe201be.dto.request.CreateProjectRequest;
import exe.exe201be.dto.request.SearchRequest;
import exe.exe201be.dto.response.APIResponse;
import exe.exe201be.dto.response.ProjectResponse;
import exe.exe201be.dto.response.SearchResponse;
import exe.exe201be.exception.AppException;
import exe.exe201be.exception.ErrorCode;
import exe.exe201be.pojo.Project;
import exe.exe201be.service.Project.ProjectService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletResponse;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/projects")
public class ProjectController {
    @Autowired
    private ProjectService projectService;

    @GetMapping
    @Operation(summary = "Get All Projects", description = "Retrieve a list of all projects")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successful operation",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Project.class))
            )
    })
    public APIResponse<List<ProjectResponse>> getAllProjects() {
        APIResponse<List<ProjectResponse>> response = new APIResponse<>();
        response.setMessage("Success");
        response.setData(projectService.getAllProjects());
        return response;
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get Project by ID", description = "Retrieve a project by its ID")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successful operation",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Project.class))
            )
    })
    public APIResponse<ProjectResponse> getProjectById(@PathVariable String id) {
        APIResponse<ProjectResponse> response = new APIResponse<>();
        response.setMessage("Success");
        response.setData(projectService.getProjectById(id));
        return response;
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Change Status Project", description = "Change status of a project by its ID")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successful operation",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Project.class))
            )
    })
    public APIResponse<?> changeStatusProject(@PathVariable String id, @RequestBody ChangeStatusRequest status) {
        APIResponse<?> response = new APIResponse<>();
        projectService.changeStatusProject(id, status);
        response.setMessage("Change status project success");
        return response;
    }

    @PostMapping
    @Operation(summary = "Create Project", description = "Create a new project")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successful operation",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = APIResponse.class))
            )
    })
        public APIResponse<?> createProject(@RequestBody CreateProjectRequest createProject, @AuthenticationPrincipal Jwt jwt, HttpServletResponse httpServletResponse) {
            APIResponse<?> response = new APIResponse<>();
            ObjectId id = new ObjectId(jwt.getSubject());
            projectService.createProject(createProject, id, httpServletResponse);
            response.setMessage("Create project success");
            return response;
        }

    @PostMapping("/{projectId}/members")
    public APIResponse<?> addMemberToProject(@PathVariable String projectId, @RequestBody AddMemberRequest request, HttpServletResponse httpServletResponse) {
        APIResponse<?> response = new APIResponse<>();
        ObjectId pId = new ObjectId(projectId);
        projectService.addMemberToProject(pId, request.getEmail());
        response.setMessage("Add member to project success");
        return response;
    }

    @GetMapping("/search")
    @Operation(summary = "Search Projects", description = "Search for projects with pagination and sorting")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Search completed successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = SearchResponse.class))
            )
    })
    public APIResponse<SearchResponse<ProjectResponse>> searchProjects(
            @RequestParam(defaultValue = "", required = false) String keyword,
            @RequestParam(defaultValue = "1", required = false) int page,
            @RequestParam(defaultValue = "10", required = false) int size,
            @RequestParam(defaultValue = "createdAt", required = false) String sortBy,
            @RequestParam(defaultValue = "asc", required = false) String sortDir
    ) {
        APIResponse<SearchResponse<ProjectResponse>> response = new APIResponse<>();
        SearchRequest searchRequest = new SearchRequest(keyword, page, size, sortBy, sortDir);
        response.setMessage("Search completed successfully");
        response.setData(projectService.searchProjects(searchRequest));
        return response;
    }

    @PostMapping("/send-invite")
    public APIResponse<?> sendInvite(@RequestParam String email, @RequestParam String projectId) {
        projectService.sendInvitationEmail(email, projectId);
        APIResponse<?> apiResponse = new APIResponse<>();
        apiResponse.setMessage("Gửi lời mời thành công. Vui lòng kiểm tra email.");
        return apiResponse;
    }

    @GetMapping("/verify")
    public APIResponse<?> verifyEmail(@RequestParam String token,@RequestParam String email, @RequestParam String projectId) {
        projectService.verifyEmail(token,email, projectId);
        APIResponse<?> apiResponse = new APIResponse<>();
        apiResponse.setMessage("Xác thực email thành công. Bạn có thể đăng nhập ngay bây giờ.");
        return apiResponse;
    }

    @GetMapping("/my-projects")
    @Operation(summary = "Get My Projects", description = "Retrieve a list of projects associated with the authenticated user")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successful operation",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Project.class))
            )
    })
    public APIResponse<List<ProjectResponse>> getMyProjects(@AuthenticationPrincipal Jwt jwt) {
        APIResponse<List<ProjectResponse>> response = new APIResponse<>();
        if( jwt == null ) {
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }
        ObjectId userId = new ObjectId(jwt.getSubject());
        response.setMessage("Success");
        response.setData(projectService.getProjectsByUserId(userId));
        return response;
    }
}
