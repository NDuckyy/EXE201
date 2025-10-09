package exe.exe201be.service.UserService;

import exe.exe201be.dto.request.RegisterRequest;
import exe.exe201be.dto.request.UserUpdateRequest;
import exe.exe201be.dto.response.UserResponse;
import exe.exe201be.pojo.Role;
import exe.exe201be.pojo.User;
import exe.exe201be.pojo.UserGlobalRole;
import exe.exe201be.pojo.type.Status;
import exe.exe201be.repository.RoleRepository;
import exe.exe201be.repository.UserGlobalRepository;
import exe.exe201be.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final UserGlobalRepository userGlobalRepository;

    // ====== READ ALL (Entity -> DTO) ======
    @Override
    public List<UserResponse> getAllUser() {
        return userRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    // ====== CREATE ======
    @Override
    @Transactional
    public boolean createUser(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) return false;

        User user = new User();
        user.setEmail(normalizeEmail(request.getEmail()));
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setFullName(safeTrim(request.getFullName()));
        user.setPhone(safeTrim(request.getPhone()));
        user.setGender(request.getGender());
        user.setStatus(Status.ACTIVE);
        userRepository.save(user);

        Role role = roleRepository.findByKey("USER");
        if (role == null) throw new IllegalStateException("Không tìm thấy role mặc định USER");

        UserGlobalRole ugr = new UserGlobalRole();
        ugr.setUserId(user.getId());
        ugr.setRoleId(role.getId());
        userGlobalRepository.save(ugr);
        return true;
    }

    // ====== READ BY ID ======
    @Override
    public User getUserById(String id) {
        ObjectId objectId = new ObjectId(id);
        return userRepository.findById(objectId)
                .orElseThrow(() -> new NoSuchElementException("User not found with id: " + id));
    }

    // ====== UPDATE (partial, không dùng mapper) ======
    @Override
    public User updateUser(String id, UserUpdateRequest req) {
        ObjectId objectId = new ObjectId(id);
        User existing = userRepository.findById(objectId)
                .orElseThrow(() -> new NoSuchElementException("User not found with id: " + id));

        // Chỉ set các field có giá trị (tránh ghi đè null)
        if (req.getFullName() != null) existing.setFullName(safeTrim(req.getFullName()));
        if (req.getPhone() != null)    existing.setPhone(safeTrim(req.getPhone()));
        if (req.getAvatarUrl() != null) existing.setAvatar_url(safeTrim(req.getAvatarUrl()));
        if (req.getGender() != null)   existing.setGender(req.getGender());
        if (req.getAddress() != null)  existing.setAddress(safeTrim(req.getAddress()));
        if (req.getImage() != null)    existing.setImage(safeTrim(req.getImage()));
        if (req.getStatus() != null)   existing.setStatus(req.getStatus());

        return userRepository.save(existing);
    }

    // ====== SOFT DELETE ======
    @Override
    public void deleteUser(String id) {
        ObjectId objectId = new ObjectId(id);
        User existing = userRepository.findById(objectId)
                .orElseThrow(() -> new NoSuchElementException("User not found with id: " + id));
        existing.setStatus(Status.INACTIVE);
        userRepository.save(existing);
    }

    @Override
    public User getUserByEmail(String email) {
        String normalizedEmail = normalizeEmail(email);
        if (normalizedEmail == null) return null;
        return userRepository.findByEmail(normalizedEmail);
    }

    // ====== Helpers ======
    private UserResponse toResponse(User u) {
        // Map Entity -> DTO bằng tay
        UserResponse dto = new UserResponse();
        dto.setId(u.getId().toHexString());
        dto.setEmail(u.getEmail());
        dto.setFullName(u.getFullName());
        dto.setAvatarUrl(u.getAvatar_url()); // lưu ý khác tên: avatar_url -> avatarUrl
        dto.setPhone(u.getPhone());
        dto.setGender(u.getGender());
        dto.setAddress(u.getAddress());
        dto.setImage(u.getImage());
        dto.setStatus(u.getStatus());
        // nếu UserResponse còn field nào khác, set thêm ở đây
        return dto;
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
