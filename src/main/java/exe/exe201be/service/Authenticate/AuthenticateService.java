package exe.exe201be.service.Authenticate;

import exe.exe201be.dto.request.LoginRequest;

public interface AuthenticateService {
    boolean checkLogin(LoginRequest loginRequest);
}
