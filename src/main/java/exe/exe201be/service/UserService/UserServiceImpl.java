package exe.exe201be.service.UserService;

import exe.exe201be.dto.request.RegisterRequest;
import exe.exe201be.pojo.Role;
import exe.exe201be.pojo.User;
import exe.exe201be.pojo.UserGlobalRole;
import exe.exe201be.pojo.type.Status;
import exe.exe201be.repository.RoleRepository;
import exe.exe201be.repository.UserGlobalRepository;
import exe.exe201be.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    UserGlobalRepository userGlobalRepository;


    @Override
    public List<User> getAllUser() {
        return userRepository.findAll();
    }

    @Override
    @Transactional
    public boolean createUser(RegisterRequest request) {
        // 1) Check tồn tại (ưu tiên existsByEmail nếu có, fallback findByEmail)
        boolean emailExisted = userRepository.existsByEmail((request.getEmail()));
        // Nếu repo của bạn chưa có existsByEmail, dùng:
        // boolean emailExisted = userRepository.findByEmail(request.getEmail()) != null;

        if (emailExisted) {
            return false;
        }

        // 2) Chuẩn hoá dữ liệu an toàn
        String email = normalizeEmail(request.getEmail());
        String fullName = safeTrim(request.getFullName());
        String avatarUrl = safeTrim(request.getAvatarUrl());
        String phone = safeTrim(request.getPhone());
        String address = safeTrim(request.getAddress());
        String image = safeTrim(request.getImage());

        // 3) Tạo user
        User user = new User();
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setFullName(fullName);
        user.setAvatar_url(avatarUrl);
        user.setPhone(phone);
        user.setGender(request.getGender());
        user.setAddress(address);
        user.setImage(image);
        user.setStatus(Status.ACTIVE);

        userRepository.save(user);

        // 4) Gán quyền
        Role role = roleRepository.findByKey("USER");
        if (role == null) {
            // rollback transaction bằng cách ném RuntimeException
            throw new IllegalStateException("Không tìm thấy role mặc định USER");
        }

        UserGlobalRole userGlobalRole = new UserGlobalRole();
        userGlobalRole.setUserId(user.getId());
        userGlobalRole.setRoleId(role.getId());
        userGlobalRepository.save(userGlobalRole);

        return true;
    }

    private String normalizeEmail(String email) {
        if (email == null) return null;
        String t = email.trim();
        return t.isEmpty() ? null : t.toLowerCase();
    }

    private String safeTrim(String s) {
        if (s == null) return null;
        String t = s.trim();
        return t.isEmpty() ? null : t;
    }

}
