package space.maxus.dnevnik.auth;

import lombok.SneakyThrows;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class MailService {
    private final JavaMailSender mailSender;

    public MailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendMail(String to, String subject, String text) {
        var message = new SimpleMailMessage();
        message.setFrom("no-reply@maxus.space");
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        mailSender.send(message);
    }

    @SneakyThrows
    public void sendValidationMail(String to, String code) {
        var message = mailSender.createMimeMessage();
        var helper = new MimeMessageHelper(message, "UTF-8");
        helper.setText("""
                <div>
                Ваш код верификации аккаунта на дневнике: <strong> %s </strong><br/>
                                
                Если это были не вы, то просто проигнорируйте это письмо.
                </div>
                """.formatted(code), true);
        helper.setFrom("no-reply@maxus.space");
        helper.setTo(to);
        helper.setSubject("Верификация аккаунта");
        mailSender.send(message);
    }
}
