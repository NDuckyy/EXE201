package exe.exe201be.controller;

import exe.exe201be.dto.request.ChangeStatusRequest;
import exe.exe201be.dto.response.APIResponse;
import exe.exe201be.pojo.Task;
import exe.exe201be.service.Task.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/{projectId}/tasks")
public class TaskController {
    @Autowired
    private TaskService taskService;

    @GetMapping
    @Operation(summary = "Get All Tasks by Project ID", description = "Retrieve all tasks associated with a specific project ID")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Tasks retrieved successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Task.class)
                    )
            )
    })
    public APIResponse<List<Task>> getAllTasksByProjectId(@PathVariable("projectId") String projectId) {
        APIResponse<List<Task>> response = new APIResponse<>();
        ObjectId id = new ObjectId(projectId);
        List<Task> tasks = taskService.getAllTasksByProjectId(id);
        if (tasks != null) {
            response.setMessage("Tasks retrieved successfully");
            response.setData(tasks);
        }
        return response;
    }

    @PatchMapping("/{taskId}")
    @Operation(summary = "Update Task Status", description = "Update the status of a specific task by its ID")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Task status updated successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Task.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Task not found"
            )
    })
    public APIResponse<?> changeStatus(@PathVariable("taskId") String taskId, @PathVariable("projectId") String projectId, @RequestBody ChangeStatusRequest status) {
        APIResponse<?> response = new APIResponse<>();
        ObjectId id = new ObjectId(taskId);
        taskService.changeTaskStatus(id, status);
        response.setMessage("Task status updated successfully");
        return response;
    }
}
