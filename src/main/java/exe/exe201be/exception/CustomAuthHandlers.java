package exe.exe201be.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import exe.exe201be.dto.response.APIResponse;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomAuthHandlers {

    private final ObjectMapper objectMapper = new ObjectMapper();

    // Xử lý 401 Unauthorized
    public AuthenticationEntryPoint authenticationEntryPoint() {
        return (request, response, authException) -> {
            APIResponse<Object> apiResponse = new APIResponse<>();
            apiResponse.setCode(HttpServletResponse.SC_UNAUTHORIZED);
            apiResponse.setMessage("Unauthorized");

            writeResponse(response, apiResponse, HttpServletResponse.SC_UNAUTHORIZED);
        };
    }

    // Xử lý 403 Forbidden
    public AccessDeniedHandler accessDeniedHandler() {
        return (request, response, accessDeniedException) -> {
            APIResponse<Object> apiResponse = new APIResponse<>();
            apiResponse.setCode(HttpServletResponse.SC_FORBIDDEN);
            apiResponse.setMessage("Forbidden");

            writeResponse(response, apiResponse, HttpServletResponse.SC_FORBIDDEN);
        };
    }

    private void writeResponse(HttpServletResponse response, APIResponse<Object> apiResponse, int status) throws IOException {
        response.setStatus(status);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        objectMapper.writeValue(response.getWriter(), apiResponse);
    }
}
