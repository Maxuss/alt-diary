package space.maxus.dnevnik.util;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "diary")
@Getter @Setter
public class DiaryConfiguration {
    private MailData mail;
    private AdminData admin;

    @Getter @Setter
    public static class MailData {
        private String username;
        private String password;
    }

    @Getter @Setter
    public static class AdminData {
        private String secret;
    }
}
