package exe.exe201be.service.Authenticate;

import exe.exe201be.dto.request.LoginRequest;
import exe.exe201be.exception.AppException;
import exe.exe201be.exception.ErrorCode;
import exe.exe201be.pojo.User;
import exe.exe201be.pojo.type.Status;
import exe.exe201be.repository.UserRepository;
import exe.exe201be.utils.JWTUtilsHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class AuthenticateServiceImpl implements AuthenticateService {

    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    UserRepository userRepository;





    public boolean checkLogin(LoginRequest loginRequest) {

        User user = userRepository.findByEmail(loginRequest.getEmail());
        if (user == null || (user.getStatus() == Status.INACTIVE)) {
            throw new AppException(ErrorCode.INVALID_LOGIN);
        }
        boolean authenticated = passwordEncoder.matches(loginRequest.getPassword(), user.getPassword());

        if (!authenticated) {
            throw new AppException(ErrorCode.INVALID_LOGIN);
        }
        return true;
    }





}