package exe.exe201be.service.Mail;

public interface MailService {
    void sendVerificationEmail(String to, String token, String projectId);

    void sendEmail(String to, String contactEmail, String phoneNumber);
}
