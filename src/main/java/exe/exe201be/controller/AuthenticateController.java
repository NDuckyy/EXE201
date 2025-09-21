package exe.exe201be.controller;

import exe.exe201be.dto.request.LoginRequest;
import exe.exe201be.dto.request.RegisterRequest;
import exe.exe201be.dto.response.APIResponse;
import exe.exe201be.exception.ErrorCode;
import exe.exe201be.pojo.Role;
import exe.exe201be.pojo.User;
import exe.exe201be.pojo.UserGlobalRole;
import exe.exe201be.service.Authenticate.AuthenticateService;
import exe.exe201be.service.Authority.AuthorityService;
import exe.exe201be.service.Role.GlobalRoleService;
import exe.exe201be.service.Role.RoleService;
import exe.exe201be.service.UserService.UserService;
import exe.exe201be.utils.JwtTokenGenerator;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/auth")
public class AuthenticateController {
    @Autowired
    AuthenticateService authenticateService;
    @Autowired
    UserService userService;

    @Autowired
    AuthorityService authorityService;

    @Autowired
    JwtTokenGenerator jwtUtilsHelper;


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
    public APIResponse<String> login(@RequestBody @Valid LoginRequest loginRequest, HttpServletResponse httpResponse) {
        APIResponse<String> response = new APIResponse<>();

        boolean ok = authenticateService.checkLogin(loginRequest);
        if (ok) {
            User user = userService.getUserByEmail(loginRequest.getEmail());

            // 🔹 Gọi service để lấy tất cả authorities
            List<String> authorities = authorityService.getAuthoritiesForUser(user.getId().toHexString());

            // 🔹 Generate JWT với authorities
            String token = jwtUtilsHelper.generate(user.getId().toHexString(), user.getEmail(), authorities);

            // 🔹 Set cookie
            Cookie cookie = new Cookie("access_token", token);
            cookie.setHttpOnly(true);
            cookie.setSecure(true);
            cookie.setPath("/");
            cookie.setMaxAge(60 * 60 * 24 * 30); // 30 ngày

            httpResponse.addCookie(cookie);

            response.setMessage("Login Successfully");
            response.setData(token);
        } else {
            response.setMessage("Invalid username or password");
            response.setData(null);
        }

        return response;
    }


    @PostMapping(value = "/register", consumes = "application/json", produces = "application/json")
    @Operation(
            summary = "User Registration",
            description = "Register a new user and return JWT token if successful"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Register successful",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = String.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Register failed",
                    content = @Content(mediaType = "application/json")
            )
    })
    public ResponseEntity<APIResponse<String>> register(@RequestBody @Valid RegisterRequest registerRequest,
                                                        BindingResult bindingResult) {
        APIResponse<String> response = new APIResponse<>();


        try {

            String email = safeTrimToNull(registerRequest.getEmail());
            if (email == null) {
                response.setCode(ErrorCode.VALIDATION_FAILED.getCode());
                response.setMessage("Register failed: Email không hợp lệ");
                return ResponseEntity.badRequest().body(response);
            }
            registerRequest.setEmail(email.toLowerCase());

            boolean created = userService.createUser(registerRequest);
            if (created) {
                response.setMessage("Register Successfully");
                return ResponseEntity.ok(response);
            } else {
                response.setCode(ErrorCode.USER_EXISTS.getCode());
                response.setMessage("Register failed: User already exists");
                response.setData(null);
                return ResponseEntity.badRequest().body(response);
            }
        } catch (Exception e) {
            log.error("Register error", e);
            response.setCode(ErrorCode.SERVICE_PROVIDER_NOT_FOUND.getCode());
            response.setMessage("Register failed: " + e.getMessage());
            response.setData(null);
            return ResponseEntity.badRequest().body(response);
        }
    }

    private String safeTrimToNull(String s) {
        if (s == null) return null;
        String t = s.trim();
        return t.isEmpty() ? null : t;
    }


}
