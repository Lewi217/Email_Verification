package DevLewi.userservice.Service.impl;

import DevLewi.userservice.Service.EmailService;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.File;

import static DevLewi.userservice.utils.EmailUtils.getVerificationUrl;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    public static final String EMAIL_TEMPLATE = "emailtemplate";

    @Value("${spring.mail.verify.host}")
    private String host;

    @Value("${spring.mail.username}")
    private String fromEmail;

    private final JavaMailSender emailSender;
    private final TemplateEngine templateEngine;

    @Override
    @Async
    public void sendSimpleMailMessage(String name, String to, String token) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(to);
            message.setSubject("Verification Email");
            message.setText("Dear " + name + ",\n\nPlease verify your email by clicking the following link: "
                    + host + "/verify?token=" + token + "\n\nBest regards,\nYour Company");

            emailSender.send(message);
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
            throw new RuntimeException(exception.getMessage());
        }
    }

    @Override
    @Async
    public void sendMimeMessageWithAttachments(String name, String to, String token) {
        try {
            MimeMessage message = getMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setPriority(1);
            helper.setFrom(fromEmail);
            helper.setTo(to);
            helper.setSubject("Verification Email");
            helper.setText("Dear " + name + ",\n\nPlease verify your email by clicking the following link: "
                    + host + "/verify?token=" + token + "\n\nBest regards,\nYour Company");

            // Add attachments
            FileSystemResource euro = new FileSystemResource(new File(System.getProperty("user.home") + "/Downloads/images/euro.jpeg"));
            FileSystemResource portugal = new FileSystemResource(new File(System.getProperty("user.home") + "/Downloads/images/portugal.webp"));
            FileSystemResource symbol = new FileSystemResource(new File(System.getProperty("user.home") + "/Downloads/images/symbol.jpg"));

            helper.addAttachment("euro.jpeg", euro);
            helper.addAttachment("portugal.webp", portugal);
            helper.addAttachment("symbol.jpg", symbol);

            emailSender.send(message);
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
            throw new RuntimeException(exception.getMessage());
        }
    }

    @Override
    @Async
    public void sendMimeMessageWithEmbeddedFiles(String name, String to, String token) {
        try {
            MimeMessage message = getMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setPriority(1);
            helper.setFrom(fromEmail);
            helper.setTo(to);
            helper.setSubject("Verification Email");
            helper.setText("Dear " + name + ",\n\nPlease verify your email by clicking the following link: "
                    + host + "/verify?token=" + token + "\n\nBest regards,\nYour Company");

            // Add embedded files
            FileSystemResource euro = new FileSystemResource(new File(System.getProperty("user.home") + "/Downloads/images/euro.jpeg"));
            FileSystemResource portugal = new FileSystemResource(new File(System.getProperty("user.home") + "/Downloads/images/portugal.webp"));
            FileSystemResource symbol = new FileSystemResource(new File(System.getProperty("user.home") + "/Downloads/images/symbol.jpg"));

            helper.addInline(getContentId(euro.getFilename()), euro);
            helper.addInline(getContentId(portugal.getFilename()), portugal);
            helper.addInline(getContentId(symbol.getFilename()), symbol);

            emailSender.send(message);
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
            throw new RuntimeException(exception.getMessage());
        }
    }

    @Override
    @Async
    public void sendHtmlEmail(String name, String to, String token) {
        try {
            Context context = new Context();
            context.setVariable("name", name);
            context.setVariable("url", getVerificationUrl(host, token));

            String text = templateEngine.process(EMAIL_TEMPLATE, context);

            MimeMessage message = getMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setPriority(1);
            helper.setFrom(fromEmail);
            helper.setTo(to);
            helper.setSubject("Verification Email");
            helper.setText(text, true);
            FileSystemResource euro = new FileSystemResource(new File(System.getProperty("user.home") + "/Downloads/images/euro.jpeg"));
            FileSystemResource portugal = new FileSystemResource(new File(System.getProperty("user.home") + "/Downloads/images/portugal.webp"));
            FileSystemResource symbol = new FileSystemResource(new File(System.getProperty("user.home") + "/Downloads/images/symbol.jpg"));

            helper.addAttachment("euro.jpeg", euro);
            helper.addAttachment("portugal.webp", portugal);
            helper.addAttachment("symbol.jpg", symbol);

            emailSender.send(message);
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
            throw new RuntimeException(exception.getMessage());
        }
    }

    @Override
    @Async
    public void sendHtmlEmailWithEmbeddedFiles(String name, String to, String token) {
        // Implementation
    }

    private MimeMessage getMimeMessage() {
        return emailSender.createMimeMessage();
    }

    private String getContentId(String filename) {
        return "<" + filename + ">";
    }
}
