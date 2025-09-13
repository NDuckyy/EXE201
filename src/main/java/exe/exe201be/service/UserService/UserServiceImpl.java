package exe.exe201be.service.UserService;

import exe.exe201be.dto.request.RegisterRequest;
import exe.exe201be.pojo.User;
import exe.exe201be.repository.RoleRepository;
import exe.exe201be.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class UserServiceImpl implements UserService{
    @Autowired
    private UserRepository userRepository;


    @Override
    public List<User> getAllUser() {
        return userRepository.findAll();
    }

    @Override
    public boolean createUser(RegisterRequest request) {
//        // Kiểm tra email đã tồn tại chưa
//        if (userRepository.findByEmail(request.getEmail()) != null) {
//            return false;
//        }
//
//        User user = new User();
//        user.setEmail(request.getEmail());
//        user.setPassword(passwordEncoder.encode(request.getPassword()));
//        user.setFullName(request.getFullName());
//        user.setAvatar_url(request.getAvatarUrl());
//        user.setPhone(request.getPhone());
//        user.setGender(request.getGender());
//        user.setAddress(request.getAddress());
//        user.setImage(request.getImage());
//        user.setStatus("inactive");
//        user.setCreatedAt(LocalDateTime.now());
//        user.setUpdatedAt(LocalDateTime.now());
//
//        userRepository.save(user);
        return true;
    }

}
