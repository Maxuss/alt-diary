package space.maxus.dnevnik;

import lombok.extern.log4j.Log4j2;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import space.maxus.dnevnik.util.DiaryConfiguration;

@SpringBootApplication
@Log4j2
@EnableConfigurationProperties(DiaryConfiguration.class)
public class DiaryServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(DiaryServerApplication.class, args);
    }
}
