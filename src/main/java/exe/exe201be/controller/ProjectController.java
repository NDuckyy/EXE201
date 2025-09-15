package exe.exe201be.controller;

import exe.exe201be.dto.response.APIResponse;
import exe.exe201be.dto.response.ProjectResponse;
import exe.exe201be.pojo.Project;
import exe.exe201be.service.Project.ProjectService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
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
    public APIResponse<ProjectResponse> getProjectById(@PathVariable  String id) {
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
    public APIResponse<?> changeStatusProject(@PathVariable String id) {
        APIResponse<?> response = new APIResponse<>();
        projectService.changeStatusProject(id);
        response.setMessage("Change status project success");
        return response;
    }
}
