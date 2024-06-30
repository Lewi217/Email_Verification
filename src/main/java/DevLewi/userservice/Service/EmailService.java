package DevLewi.userservice.Service;

public interface EmailService {
    void sendSimpleMailMessage(String name, String to, String token);
    void sendMimeMessageWithAttachments(String name, String to, String token);
    void sendMimeMessageWithEmbeddedFiles(String name, String to, String token);
    void sendHtmlEmail(String name, String to, String token);
    void sendHtmlEmailWithEmbeddedFiles(String name, String to, String token);
}
