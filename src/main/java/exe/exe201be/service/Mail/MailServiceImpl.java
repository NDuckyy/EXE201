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
        String verifyUrl = "http://localhost:3000/workspace/verify?token=" + token + "&email=" + to + "&projectId=" + projectId;

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
                        <p style="margin: 4px 0;">© 2025 FoundersHub. All rights reserved.</p>
                        <p style="margin: 0;">Email này được gửi tự động, vui lòng không trả lời.</p>
                    </div>
                </div>
                """.formatted(verifyUrl);

        // Dữ liệu JSON gửi đến Resend API
        Map<String, Object> payload = new HashMap<>();
        payload.put("from", fromEmail);
        payload.put("to", new String[]{to});
        payload.put("subject", "Thư mời tham gia dự án");
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

    @Override
    public void sendEmail(String to, String contactEmail, String phoneNumber) {
        // (Tuỳ chọn) kiểm tra đầu vào nhanh gọn
        if (to == null || to.isBlank()) throw new IllegalArgumentException("Người nhận (to) không hợp lệ");
        if ((contactEmail == null || contactEmail.isBlank()) && (phoneNumber == null || phoneNumber.isBlank())) {
            throw new IllegalArgumentException("Cần ít nhất email hoặc số điện thoại của nhà cung cấp");
        }

        String subject = "Thông tin liên hệ nhà cung cấp dịch vụ";

        String htmlContent = """
            <div style="font-family: 'Segoe UI', Arial, sans-serif; max-width: 600px; margin: auto;
                        border: 1px solid #e0e0e0; border-radius: 10px; overflow: hidden; background-color: #ffffff;">
                <div style="background-color: #0079bf; padding: 30px 20px; text-align: center; color: white;">
                    <img src="https://scontent.fsgn5-5.fna.fbcdn.net/v/t39.30808-6/552786229_122095343319043574_6557106202731065632_n.jpg?_nc_cat=100&ccb=1-7&_nc_sid=6ee11a&_nc_ohc=x4eT_UydcssQ7kNvwH2PG7I&_nc_oc=AdmlqPiB_wOITeBV3MV8ypiIT5J_82eI4eo6IDpABRF9ndrMMxSoSXkc7pV6CqwHbnc&_nc_zt=23&_nc_ht=scontent.fsgn5-5.fna&_nc_gid=rgJNsWYFnfuhZHx12_Fd3w&oh=00_AfemVyZCzZdR4BVRtJ3R1Okn3Sb0c35_DSad7X82exhafA&oe=68E959AF"
                         alt="Project Logo"
                         style="width: 90px; height: 90px; margin-bottom: 10px;
                                border-radius: 50%%; object-fit: cover; border: 3px solid #ffffff;">
                    <h2 style="margin: 0; font-size: 24px; font-weight: bold;">Thông tin liên hệ nhà cung cấp dịch vụ</h2>
                </div>
                <div style="padding: 24px 30px; color: #333333; font-size: 15px; line-height: 1.6;">
                    <p>Xin chào,</p>
                    <p>Bạn nhận được email này từ hệ thống <strong>FoundersHub</strong> với thông tin liên hệ của nhà cung cấp dịch vụ:</p>

                    <div style="margin: 18px 0; padding: 16px; background:#f7fbff; border:1px solid #e0f0ff; border-radius:8px;">
                        %s
                        %s
                    </div>

                    <p style="margin-top: 18px;">Bạn có thể bấm trực tiếp vào các liên kết trên để bắt đầu soạn email hoặc gọi điện.</p>

                    <div style="text-align:center; margin: 28px 0 6px;">
                        %s
                        %s
                    </div>

                    <p style="font-size:13px; color:#666; margin-top: 22px;">
                        Nếu bạn không yêu cầu thông tin này, vui lòng bỏ qua email.
                    </p>

                    <p>Trân trọng,<br><strong>Đội ngũ Hỗ trợ FoundersHub</strong></p>
                </div>
                <div style="background-color: #f5f5f5; color: #777; text-align: center;
                            padding: 14px; font-size: 13px; border-top: 1px solid #e0e0e0;">
                    <p style="margin: 4px 0;">© 2025 FoundersHub. All rights reserved.</p>
                    <p style="margin: 0;">Email này được gửi tự động, vui lòng không trả lời.</p>
                </div>
            </div>
            """.formatted(
                // Khối dòng thông tin (email)
                (contactEmail != null && !contactEmail.isBlank())
                        ? "<p style=\"margin:6px 0;\"><strong>Email:</strong> "
                        + "<a href=\"mailto:" + contactEmail + "\" style=\"color:#0079bf; text-decoration:none;\">"
                        + contactEmail + "</a></p>"
                        : "",
                // Khối dòng thông tin (điện thoại)
                (phoneNumber != null && !phoneNumber.isBlank())
                        ? "<p style=\"margin:6px 0;\"><strong>Điện thoại:</strong> "
                        + "<a href=\"tel:" + phoneNumber + "\" style=\"color:#0079bf; text-decoration:none;\">"
                        + phoneNumber + "</a></p>"
                        : "",
                // Nút CTA email
                (contactEmail != null && !contactEmail.isBlank())
                        ? "<a href=\"mailto:" + contactEmail + "\" "
                        + "style=\"background-color:#0079bf; color:white; padding:12px 24px; "
                        + "text-decoration:none; border-radius:6px; font-weight:bold; display:inline-block; margin: 0 6px;\">"
                        + "Soạn Email</a>"
                        : "",
                // Nút CTA gọi
                (phoneNumber != null && !phoneNumber.isBlank())
                        ? "<a href=\"tel:" + phoneNumber + "\" "
                        + "style=\"background-color:#00a86b; color:white; padding:12px 24px; "
                        + "text-decoration:none; border-radius:6px; font-weight:bold; display:inline-block; margin: 0 6px;\">"
                        + "Gọi ngay</a>"
                        : ""
        );

        Map<String, Object> payload = new HashMap<>();
        payload.put("from", fromEmail);
        payload.put("to", new String[]{to});
        payload.put("subject", subject);
        payload.put("html", htmlContent);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apiKey);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(payload, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(
                "https://api.resend.com/emails", request, String.class
        );

        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new RuntimeException("Gửi email thất bại: " + response.getBody());
        }
    }
}
