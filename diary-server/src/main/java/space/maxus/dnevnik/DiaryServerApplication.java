package space.maxus.dnevnik;

import lombok.extern.log4j.Log4j2;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import space.maxus.dnevnik.util.DiaryConfiguration;

@SpringBootApplication
@Log4j2
@EnableConfigurationProperties(DiaryConfiguration.class)
public class DiaryServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(DiaryServerApplication.class, args);
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**").allowedOrigins("http://localhost:3000", "http://localhost", "https://dnevnik.maxus.space").allowCredentials(true).allowedHeaders("*").allowedMethods("*").exposedHeaders("*");
            }
        };
    }
}
