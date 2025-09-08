package exe.exe201be.controller;

import exe.exe201be.dto.request.CreateUserRequest;
import exe.exe201be.dto.response.APIResponse;
import exe.exe201be.pojo.User;
import exe.exe201be.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/getAll")
    @Operation(summary = "Find All User", description = "Returns all users")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successful operation",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = APIResponse.class)
                    )
            )
    })
    public APIResponse<List<User>> getAll() {
        APIResponse<List<User>> response = new APIResponse<>();
        response.setData(userService.getAllUser());
        return response;
    }

    @PostMapping("/create")
    @Operation(summary = "Create User", description = "Creates a new user")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "User created successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = APIResponse.class)
                    )
            )
    })
    public APIResponse<?> createUser(@RequestBody @Valid CreateUserRequest request) {
        APIResponse<?> response = new APIResponse<>();
        userService.createUser(request);
        response.setMessage("Success");
        return response;
    }
}
