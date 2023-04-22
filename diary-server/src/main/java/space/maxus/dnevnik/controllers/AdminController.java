package space.maxus.dnevnik.controllers;

import lombok.Data;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import space.maxus.dnevnik.auth.Auth;
import space.maxus.dnevnik.util.DiaryConfiguration;

import java.util.Objects;

@RestController
public class AdminController {
    private final DiaryConfiguration configuration;

    public AdminController(DiaryConfiguration configuration) {
        this.configuration = configuration;
    }

    @PostMapping("/admin/teacher-token")
    public String genTeacherRegisterToken(@RequestBody AdminRequest<String> request) {
        if(Objects.equals(request.adminToken, configuration.getAdmin().getSecret()))
            return Auth.genTeacherRegisterToken(request.payload);
        return null;
    }

    @Data
    private static class AdminRequest<V> {
        private final V payload;
        private final String adminToken;
    }
}
