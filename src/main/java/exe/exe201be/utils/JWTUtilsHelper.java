package exe.exe201be.utils;

import exe.exe201be.pojo.Role;
import exe.exe201be.pojo.User;
import exe.exe201be.pojo.UserGlobalRole;
import exe.exe201be.repository.RoleRepository;
import exe.exe201be.repository.UserGlobalRepository;
import exe.exe201be.repository.UserRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.UUID;

@Component
public class JWTUtilsHelper {

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    UserGlobalRepository userGlobalRepository;



    @Value("${myapp.api-key}")
    private String privateKey;

    public String generateToken(String email){
        SecretKey key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(privateKey));
        User user = userRepository.findByEmail((email));
        UserGlobalRole globalRole = userGlobalRepository.findByUserId((user.getId()));
        Role roleName = roleRepository.findById((globalRole.getRoleId()));
        String jws = Jwts.builder().setSubject(email)
                .claim("userId", user.getId())
                .claim("role", roleName.getName())
                .setIssuer("FounderHub.com")
                .setIssuedAt(new Date())
                .claim("jti", UUID.randomUUID().toString())
                .setExpiration(new Date(
                        Instant.now().plus(10, ChronoUnit.DAYS).toEpochMilli()
                ))
                .signWith(key).compact();
        return jws;
    }

    public boolean verifyToken(String token) {
        try {
            SecretKey key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(privateKey));

            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);

            return true; // Token valid
        } catch (Exception e) {
            return false; // Token invalid
        }
    }

}