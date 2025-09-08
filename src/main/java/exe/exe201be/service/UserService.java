package exe.exe201be.service;

import exe.exe201be.dto.request.CreateUserRequest;
import exe.exe201be.pojo.User;

import java.util.List;

public interface UserService {
    List<User> getAllUser ();
    void createUser (CreateUserRequest user);
}
