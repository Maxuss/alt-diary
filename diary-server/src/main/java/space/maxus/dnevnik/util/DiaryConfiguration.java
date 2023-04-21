package space.maxus.dnevnik.util;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "diary")
@Getter @Setter
public class DiaryConfiguration {
    private String mailbox;
    private String mailpass;
}
