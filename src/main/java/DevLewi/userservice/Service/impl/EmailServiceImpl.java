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

import java.io.File;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    public static final String UTF_8_ENCODING = "UTF_8_ENCODING";
    @Value("${spring.mail.verify.host}")
    private String host;

    @Value("${spring.mail.username}")
    private String fromEmail;

    private final JavaMailSender emailSender;



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
            MimeMessage message =  getMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message,true, UTF_8_ENCODING);
            helper.setPriority(1);
            helper.setFrom(fromEmail);
            helper.setTo(to);
            helper.setSubject("Verification Email");
            helper.setText("Dear " + name + ",\n\nPlease verify your email by clicking the following link: "
                    + host + "/verify?token=" + token + "\n\nBest regards,\nYour Company");
            //Add attachments
            FileSystemResource euro = new FileSystemResource(new File(System.getProperty("user.home") + "/Downloads/images/euro.jpeg"));
            FileSystemResource portugal = new FileSystemResource(new File(System.getProperty("user.home") + "/Downloads/images/portugal.webp"));
            FileSystemResource symbol = new FileSystemResource(new File(System.getProperty("user.home") + "/Downloads/images/symbol.jpg"));

            emailSender.send(message);
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
            throw new RuntimeException(exception.getMessage());
        }
    }



    @Override
    @Async
    public void sendMimeMessageWithEmbeddedFiles(String name, String to, String token) {

    }

    @Override
    @Async
    public void sendHtmlEmail(String name, String to, String token) {

    }

    @Override
    @Async
    public void sendHtmlEmailWithEmbeddedFiles(String name, String to, String token) {}

    private MimeMessage getMimeMessage(){
        return emailSender.createMimeMessage();
    }
}
