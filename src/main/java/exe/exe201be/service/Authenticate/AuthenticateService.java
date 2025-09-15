package exe.exe201be.service.Authenticate;

import exe.exe201be.dto.request.LoginRequest;
import exe.exe201be.pojo.User;

public interface AuthenticateService {
    boolean checkLogin(LoginRequest loginRequest);
}
