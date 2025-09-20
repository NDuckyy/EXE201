package exe.exe201be.utils;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JOSEObjectType;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.Instant;
import java.util.Base64;
import java.util.Date;
import java.util.List;

@Component
public class JwtTokenGenerator {
    private final byte[] secret; // bytes của secret (Base64 decode khuyến nghị)

    // Inject secret dạng Base64: app.jwt.secret-base64 (>= 32 bytes khi decode)
    public JwtTokenGenerator(@Value("${app.jwt.secret-base64}") String secretBase64) {
        this.secret = Base64.getDecoder().decode(secretBase64);
        if (this.secret.length < 32) {
            throw new IllegalStateException("JWT secret must be at least 32 bytes for HS256");
        }
    }

    private static final Duration DEFAULT_TTL = Duration.ofHours(2);

    /**
     * Generate token mặc định 15 phút
     */
    public String generate(String userId, String email, List<String> authorities) {
        return generate(userId, email, authorities, DEFAULT_TTL);
    }

    /**
     * Generate token với TTL tuỳ chọn
     */
    public String generate(String userId, String email, List<String> authorities, Duration ttl) {
        try {
            Instant now = Instant.now();
            // Claims
            JWTClaimsSet claims = new JWTClaimsSet.Builder()
                    .subject(userId)                          // sub
                    .claim("email", email)                    // email
                    .claim("authorities", authorities)            // ["ADMIN"]...
                    .issueTime(Date.from(now))                // iat
                    .notBeforeTime(Date.from(now))            // nbf
                    .expirationTime(Date.from(now.plus(ttl))) // exp
                    .build();

            // Header HS256
            JWSHeader header = new JWSHeader.Builder(JWSAlgorithm.HS256)
                    .type(JOSEObjectType.JWT)
                    .build();

            // Ký
            SignedJWT signed = new SignedJWT(header, claims);
            signed.sign(new MACSigner(secret));
            System.out.println("Secret (decoded) = " + new String(secret, StandardCharsets.UTF_8));
            return signed.serialize(); // chuỗi JWT
        } catch (JOSEException e) {
            throw new RuntimeException("Failed to sign JWT", e);
        }
    }
}
