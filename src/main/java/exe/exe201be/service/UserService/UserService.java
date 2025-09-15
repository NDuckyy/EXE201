package exe.exe201be.service.UserService;

import exe.exe201be.dto.request.RegisterRequest;
import exe.exe201be.dto.request.UserUpdateRequest;
import exe.exe201be.dto.response.UserResponse;
import exe.exe201be.pojo.User;

import java.util.List;

public interface UserService {
    List<UserResponse> getAllUser();
    boolean createUser(RegisterRequest request);

    User getUserById(String id);


    User updateUser(String id, UserUpdateRequest request);

    void deleteUser(String id);

    User getUserByEmail(String email);
}
