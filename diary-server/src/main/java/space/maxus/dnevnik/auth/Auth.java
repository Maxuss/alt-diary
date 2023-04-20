package space.maxus.dnevnik.auth;

import at.favre.lib.crypto.bcrypt.BCrypt;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import jakarta.servlet.http.HttpServletRequest;
import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.NotNull;
import space.maxus.dnevnik.data.fetch.AggregatorService;
import space.maxus.dnevnik.data.model.Student;

import java.security.SecureRandom;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@UtilityClass
public class Auth {
    // 21 days -> three weeks
    public long EXPIRATION_TIME = 21 * 24 * 60 * 60 * 1000L;
    public String SECRET = "d7bd17b2941f85bcb0106d0e67bdaa8c370dd8599c2f8fc391100f0f2bdf934b";
    public String AUTH_HEADER = "Authentication";
    public String BEARER = "Bearer ";
    public SecureRandom RANDOM = new SecureRandom();

    public Optional<Student> studentFromEmail(String email) {
        return AggregatorService.INSTANCE.getStudentService().findAll().stream().filter(student -> student.getEmail().equals(email)).findFirst();
    }

    public boolean verifyHash(String hash, char[] provided) {
        return BCrypt.verifyer().verify(provided, hash).verified;
    }

    public String hashPassword(char[] password) {
        return BCrypt.with(RANDOM).hashToString(10, password);
    }

    public String genToken(@NotNull Student student) {
        return JWT.create()
                .withSubject(student.getId().toString())
                .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .sign(Algorithm.HMAC512(SECRET.getBytes()));
    }

    public Optional<Student> require(HttpServletRequest request) {
        String headerValue = request.getHeader(AUTH_HEADER);
        if(headerValue == null)
            return Optional.empty();

        String userIdStr = JWT.require(Algorithm.HMAC512(SECRET.getBytes()))
                .build()
                .verify(headerValue.replace(BEARER, ""))
                .getSubject();
        if(userIdStr == null)
            return Optional.empty();
        UUID userId = UUID.fromString(userIdStr);
        return AggregatorService.INSTANCE.getStudentService().findById(userId);
    }
}
