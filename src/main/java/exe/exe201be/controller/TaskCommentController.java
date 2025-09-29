package exe.exe201be.controller;

import exe.exe201be.dto.request.AddCommentRequest;
import exe.exe201be.dto.response.APIResponse;
import exe.exe201be.dto.response.TaskCommentResponse;
import exe.exe201be.pojo.TaskComment;
import exe.exe201be.service.TaskComment.TaskCommentService;
import io.swagger.v3.oas.annotations.Operation;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/api")
public class TaskCommentController {

    @Autowired
    TaskCommentService commentService;

    @GetMapping("/{taskId}/comments")
    public APIResponse<List<TaskCommentResponse>> getCommentsByTaskId(@PathVariable("taskId") String taskId) {
        APIResponse<List<TaskCommentResponse>> response = new APIResponse<>();
        ObjectId tId = new ObjectId(taskId);
        List<TaskCommentResponse> comments = commentService.getCommentsByTaskId(tId);
        response.setMessage("Comments retrieved successfully");
        response.setData(comments);
        return response;
    }

    @PostMapping("/{taskId}/comments")
    public APIResponse<TaskCommentResponse> addComment(
            @PathVariable("taskId") String taskId,
            @RequestBody AddCommentRequest request,
            @AuthenticationPrincipal Jwt jwt) {

        APIResponse<TaskCommentResponse> response = new APIResponse<>();
        ObjectId tId = new ObjectId(taskId);
        ObjectId uId = new ObjectId(jwt.getSubject());

        TaskCommentResponse comment = commentService.addComment(tId, uId, request.getContent());
        response.setMessage("Comment added successfully");
        response.setData(comment);
        return response;
    }
}
