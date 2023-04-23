package space.maxus.dnevnik.auth;

import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Bean;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Component;
import space.maxus.dnevnik.util.DiaryConfiguration;

import java.util.Properties;

@Component
@Log4j2
public class MailServiceBean {
    private final DiaryConfiguration configuration;

    public MailServiceBean(DiaryConfiguration configuration) {
        this.configuration = configuration;
    }

    @Bean
    public JavaMailSender getJavaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost("live.smtp.mailtrap.io");
        mailSender.setPort(587);

        mailSender.setUsername(configuration.getMail().getUsername());
        mailSender.setPassword(configuration.getMail().getPassword());

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        return mailSender;
    }
}
