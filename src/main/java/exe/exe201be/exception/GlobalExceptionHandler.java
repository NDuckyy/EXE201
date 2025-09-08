package exe.exe201be.exception;

import exe.exe201be.dto.response.APIResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(value = Exception.class)
    ResponseEntity<String> handleIllegalArgumentException (IllegalArgumentException ex){
        return ResponseEntity.badRequest().body(ex.getMessage());
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    ResponseEntity<APIResponse> handleMethodArgumentNotValidException (MethodArgumentNotValidException ex){
        String enumKey = ex.getFieldError().getDefaultMessage();
        ErrorCode errorCode = ErrorCode.valueOf(enumKey);
        APIResponse apiResponse = new APIResponse();
        apiResponse.setCode(errorCode.getCode());
        apiResponse.setMessage(errorCode.getMessage());
        return ResponseEntity.badRequest().body(apiResponse);
    }

    @ExceptionHandler(value = AppException.class)
    ResponseEntity<APIResponse> handleAppException (AppException ex){
        ErrorCode errorCode = ex.getErrorCode();
        APIResponse apiResponse = new APIResponse();
        apiResponse.setCode(errorCode.getCode());
        apiResponse.setMessage(errorCode.getMessage());
        return ResponseEntity.badRequest().body(apiResponse);
    }
}
