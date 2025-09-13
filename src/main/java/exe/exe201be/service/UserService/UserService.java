package exe.exe201be.service.UserService;

import exe.exe201be.dto.request.RegisterRequest;
import exe.exe201be.pojo.User;

import java.util.List;

public interface UserService {
    List<User> getAllUser ();
    boolean createUser(RegisterRequest request);
}
