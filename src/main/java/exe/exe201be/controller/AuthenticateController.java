package exe.exe201be.controller;

import exe.exe201be.dto.request.LoginRequest;
import exe.exe201be.dto.request.RegisterRequest;
import exe.exe201be.dto.response.APIResponse;
import exe.exe201be.service.Authenticate.AuthenticateService;
import exe.exe201be.service.UserService.UserService;
import exe.exe201be.utils.JWTUtilsHelper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/v1/auth")
public class AuthenticateController {
    @Autowired
    AuthenticateService authenticateService;
    @Autowired
    UserService userService;

    @Autowired
    JWTUtilsHelper jwtUtilsHelper;


    @PostMapping("/login")
    @Operation(
            summary = "User Login",
            description = "Authenticate user and return JWT token if credentials are valid"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Login successful",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = String.class))
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Invalid username or password",
                    content = @Content(mediaType = "application/json")
            )
    })
    public APIResponse<String> login(@RequestBody @Valid LoginRequest loginRequest) {
        APIResponse<String> response = new APIResponse<>();

        boolean checkLogin = authenticateService.checkLogin(loginRequest);
        if (checkLogin) {
            String token = jwtUtilsHelper.generateToken(loginRequest.getEmail());
            response.setMessage("Login Successfully");
            response.setData(token);
        } else {
            response.setMessage("Invalid username or password");
            response.setData(null);
        }
        return response;
    }



    @PostMapping("/register")
    @Operation(
            summary = "User Registration",
            description = "Register a new user and return JWT token if successful"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Register successful",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = String.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Register failed",
                    content = @Content(mediaType = "application/json")
            )
    })
    public APIResponse<String> register(@RequestBody @Valid RegisterRequest registerRequest) {
        APIResponse<String> response = new APIResponse<>();
        try {
            boolean created = userService.createUser(registerRequest);
            if (created) {
                String token = jwtUtilsHelper.generateToken(registerRequest.getEmail());
                response.setMessage("Register Successfully");
                response.setData(token);
            } else {
                response.setMessage("Register failed: User already exists");
                response.setData(null);
            }
        } catch (Exception e) {
            log.error("Register error: {}", e.getMessage());
            response.setMessage("Register failed: " + e.getMessage());
            response.setData(null);
        }
        return response;
    }


}
