package exe.exe201be.controller;

import exe.exe201be.dto.response.APIResponse;
import exe.exe201be.pojo.Task;
import exe.exe201be.service.Task.TaskService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/{projectId}/tasks")
public class TaskController {
    @Autowired
    private TaskService taskService;

    @GetMapping
    public APIResponse<List<Task>> getAllTasksByProjectId(@PathVariable("projectId")  String projectId) {
        APIResponse<List<Task>> response = new APIResponse<>();
        ObjectId id = new ObjectId(projectId);
        List<Task> tasks = taskService.getAllTasksByProjectId(id);
        if (tasks != null) {
            response.setMessage("Tasks retrieved successfully");
            response.setData(tasks);
        }
        return response;
    }
}
