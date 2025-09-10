package exe.exe201be.service;

import exe.exe201be.dto.request.CreateUserRequest;
import exe.exe201be.exception.AppException;
import exe.exe201be.exception.ErrorCode;
import exe.exe201be.pojo.User;
import exe.exe201be.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService{
    @Autowired
    private UserRepository userRepository;

    @Override
    public List<User> getAllUser() {
        return userRepository.findAll();
    }

//    @Override
//    public void createUser(CreateUserRequest user) {
//        User existUser = userRepository.findByFullName(user.getFullName());
//        if(existUser != null){
//            throw new AppException(ErrorCode.USER_EXISTS);
//        }
//        User userEntity = new User();
//        userEntity.setFullName(user.getFullName());
//        userEntity.setPassword(user.getPassword());
//        userEntity.setEmail(user.getEmail());
//        userEntity.setPhone(user.getPhone());
//        userEntity.setStatus(true);
//        userRepository.save(userEntity);
//    }
}
