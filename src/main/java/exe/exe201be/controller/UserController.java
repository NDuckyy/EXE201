package exe.exe201be.controller;

import exe.exe201be.dto.request.CreateUserRequest;
import exe.exe201be.dto.request.UserUpdateRequest;
import exe.exe201be.dto.response.APIResponse;
import exe.exe201be.dto.response.UserResponse;
import exe.exe201be.pojo.User;
import exe.exe201be.service.UserService.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    // === GET ALL USERS ===
    @GetMapping("/getAllUser")
    @Operation(summary = "Find All User", description = "Returns all users")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = APIResponse.class)))
    })
    public APIResponse<List<UserResponse>> getAll() {
        APIResponse<List<UserResponse>> response = new APIResponse<>();
        response.setData(userService.getAllUser());
        return response;
    }

    // === GET USER BY ID ===
    @GetMapping("/{id}")
    @Operation(summary = "Find User by ID", description = "Return user with given ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = APIResponse.class))),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    public APIResponse<User> getById(@PathVariable String id) {
        APIResponse<User> response = new APIResponse<>();
        response.setData(userService.getUserById(id));
        return response;
    }



    // === UPDATE USER ===
    @PutMapping("/{id}")
    @Operation(summary = "Update User", description = "Update existing user by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User updated successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = APIResponse.class))),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    public APIResponse<User> update(@PathVariable String id, @RequestBody UserUpdateRequest user) {
        APIResponse<User> response = new APIResponse<>();
        response.setData(userService.updateUser(id, user));
        return response;
    }

    // === DELETE USER ===
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete User", description = "Delete user by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User deleted successfully"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    public APIResponse<String> delete(@PathVariable String id) {
        userService.deleteUser(id);
        APIResponse<String> response = new APIResponse<>();
        response.setData("User marked as INACTIVE");
        return response;
    }
}
