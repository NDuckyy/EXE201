package exe.exe201be.controller;

import exe.exe201be.dto.request.AddCommentRequest;
import exe.exe201be.dto.request.ChangeStatusRequest;
import exe.exe201be.dto.request.CreateTaskRequest;
import exe.exe201be.dto.response.APIResponse;
import exe.exe201be.dto.response.TaskResponse;
import exe.exe201be.pojo.Task;
import exe.exe201be.pojo.TaskComment;
import exe.exe201be.service.Task.TaskService;
import exe.exe201be.service.TaskComment.TaskCommentService;
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
@RequestMapping("/api/{projectId}/tasks")
public class TaskController {
    @Autowired
    private TaskService taskService;
    @Autowired
    private TaskCommentService commentService;

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
    public APIResponse<List<TaskResponse>> getAllTasksByProjectId(@PathVariable("projectId") String projectId) {
        APIResponse<List<TaskResponse>> response = new APIResponse<>();
        ObjectId id = new ObjectId(projectId);
        List<TaskResponse> tasks = taskService.getAllTasksByProjectId(id);
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

    @PostMapping
    @Operation(summary = "Create a new Task", description = "Create a new task within a specific project")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Task created successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Task.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid input data"
            )
    })
    public APIResponse<Task> createTask(@PathVariable("projectId") String projectId, @RequestBody CreateTaskRequest task, @AuthenticationPrincipal Jwt jwt) {
        ObjectId uId = new ObjectId(jwt.getSubject());
        ObjectId pId = new ObjectId(projectId);
        APIResponse<Task> response = new APIResponse<>();
        taskService.createTask(task, pId, uId);
        response.setMessage("Task created successfully");
        return response;
    }

    @GetMapping("/{taskId}")
    @Operation(summary = "Get Task by ID", description = "Retrieve a specific task by its ID within a project")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Task retrieved successfully",
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
    public APIResponse<TaskResponse> getTaskById(@PathVariable("taskId") String taskId, @PathVariable("projectId") String projectId) {
        APIResponse<TaskResponse> response = new APIResponse<>();
        ObjectId id = new ObjectId(taskId);
        TaskResponse task = taskService.getTaskById(id);
        if (task != null) {
            response.setMessage("Task retrieved successfully");
            response.setData(task);
        }
        return response;
    }

    @DeleteMapping("/{taskId}")
    @Operation(summary = "Delete Task by ID", description = "Delete a specific task by its ID within a project")
    public APIResponse<?> deleteTaskById(@PathVariable("taskId") String taskId, @PathVariable("projectId") String projectId) {
        APIResponse<?> response = new APIResponse<>();
        ObjectId id = new ObjectId(taskId);
        taskService.deleteTasksById(id);
        response.setMessage("Task deleted successfully");
        return response;
    }

    @PutMapping("/{taskId}")
    @Operation(summary = "Update Task by ID", description = "Update a specific task by its ID within a project")
    public APIResponse<?> updateTaskById(
            @PathVariable("taskId") String taskId,
            @PathVariable("projectId") String projectId,
            @RequestBody CreateTaskRequest taskRequest
    ) {
        APIResponse<?> response = new APIResponse<>();
        ObjectId tId = new ObjectId(taskId);
        ObjectId pId = new ObjectId(projectId);
        taskService.updateTask(tId, pId, taskRequest);
        response.setMessage("Task updated successfully");
        return response;
    }
}
