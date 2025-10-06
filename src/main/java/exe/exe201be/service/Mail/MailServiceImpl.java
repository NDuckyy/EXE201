package exe.exe201be.service.Mail;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class MailServiceImpl implements MailService {

    @Value("${resend.api-key}")
    private String apiKey;

    @Value("${resend.from}")
    private String fromEmail;

    private final RestTemplate restTemplate = new RestTemplate();

    @Override
    public void sendVerificationEmail(String to, String token, String projectId) {
        String verifyUrl = "https://foundershub.nducky.id.vn/api/projects/verify?token=" + token + "&email=" + to + "&projectId=" + projectId;

        String htmlContent = """
                <div style="font-family: 'Segoe UI', Arial, sans-serif; max-width: 600px; margin: auto;
                            border: 1px solid #e0e0e0; border-radius: 10px; overflow: hidden; background-color: #ffffff;">
                    <div style="background-color: #0079bf; padding: 30px 20px; text-align: center; color: white;">
                        <img src="https://scontent.fsgn5-5.fna.fbcdn.net/v/t39.30808-6/552786229_122095343319043574_6557106202731065632_n.jpg?_nc_cat=100&ccb=1-7&_nc_sid=6ee11a&_nc_ohc=x4eT_UydcssQ7kNvwH2PG7I&_nc_oc=AdmlqPiB_wOITeBV3MV8ypiIT5J_82eI4eo6IDpABRF9ndrMMxSoSXkc7pV6CqwHbnc&_nc_zt=23&_nc_ht=scontent.fsgn5-5.fna&_nc_gid=rgJNsWYFnfuhZHx12_Fd3w&oh=00_AfemVyZCzZdR4BVRtJ3R1Okn3Sb0c35_DSad7X82exhafA&oe=68E959AF"
                             alt="Project Logo"
                             style="width: 90px; height: 90px; margin-bottom: 10px;
                                    border-radius: 50%%; object-fit: cover; border: 3px solid #ffffff;">
                        <h2 style="margin: 0; font-size: 24px; font-weight: bold;">Bạn đã được mời vào dự án</h2>
                    </div>
                    <div style="padding: 24px 30px; color: #333333; font-size: 15px; line-height: 1.6;">
                        <p>Xin chào,</p>
                        <p>Bạn đã được mời tham gia vào một dự án trên hệ thống <strong>FoundersHub</strong>.</p>
                        <p>Vui lòng nhấn vào nút bên dưới để chấp nhận lời mời và bắt đầu tham gia dự án:</p>
                        <div style="text-align: center; margin: 36px 0;">
                            <a href="%s"
                               style="background-color: #0079bf; color: white; padding: 14px 32px;
                                      text-decoration: none; border-radius: 6px; font-weight: bold;
                                      display: inline-block; font-size: 16px;">
                                CHẤP NHẬN
                            </a>
                        </div>
                        <p>Nếu bạn không muốn tham gia, vui lòng bỏ qua email này.</p>
                        <p>Trân trọng,<br><strong>Đội ngũ Hỗ trợ FoundersHub</strong></p>
                    </div>
                    <div style="background-color: #f5f5f5; color: #777; text-align: center;
                                padding: 14px; font-size: 13px; border-top: 1px solid #e0e0e0;">
                        <p style="margin: 4px 0;">© 2025 FPT Corporation. All rights reserved.</p>
                        <p style="margin: 0;">Email này được gửi tự động, vui lòng không trả lời.</p>
                    </div>
                </div>
                """.formatted(verifyUrl);

        // Dữ liệu JSON gửi đến Resend API
        Map<String, Object> payload = new HashMap<>();
        payload.put("from", fromEmail);
        payload.put("to", new String[]{to});
        payload.put("subject", "Xác thực tài khoản của bạn");
        payload.put("html", htmlContent);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apiKey);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(payload, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(
                "https://api.resend.com/emails", request, String.class);

        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new RuntimeException("Gửi email thất bại: " + response.getBody());
        }
    }
}
