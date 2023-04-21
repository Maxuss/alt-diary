package space.maxus.dnevnik.auth;

import at.favre.lib.crypto.bcrypt.BCrypt;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Data;
import lombok.experimental.UtilityClass;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;
import org.springframework.web.util.WebUtils;
import space.maxus.dnevnik.data.fetch.AggregatorService;
import space.maxus.dnevnik.data.model.Student;
import space.maxus.dnevnik.data.model.Teacher;

import java.security.SecureRandom;
import java.util.Date;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@UtilityClass
@Log4j2
public class Auth {
    // 21 days -> three weeks
    public long STUDENT_EXPIRATION_TIME = 21 * 24 * 60 * 60 * 1000L;
    // 42 days -> six weeks
    public long TEACHER_EXPIRATION_TIME = 42 * 24 * 60 * 60 * 1000L;
    // 1 hour
    public long ACCESS_TOKEN_EXPIRATION_TIME = 60 * 60 * 1000L;

    public String SECRET = "d977d014df1ca15d2d66d4c9266399797e4e1af9d0c458c858977c931cdff14dec1dad0470573ed307d843b375f0170090115f04778edd87be3556bbc8624e83";
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

    public String genRefreshToken(@NotNull Teacher student) {
        return JWT.create()
                .withSubject(student.getId().toString())
                .withClaim("access", false)
                .withClaim("teacher", true)
                .withExpiresAt(new Date(System.currentTimeMillis() + TEACHER_EXPIRATION_TIME))
                .sign(Algorithm.HMAC512(SECRET.getBytes()));
    }

    public String genRefreshToken(@NotNull Student student) {
        return JWT.create()
                .withSubject(student.getId().toString())
                .withClaim("access", false)
                .withClaim("teacher", false)
                .withExpiresAt(new Date(System.currentTimeMillis() + STUDENT_EXPIRATION_TIME))
                .sign(Algorithm.HMAC512(SECRET.getBytes()));
    }

    public String genAccessToken(@NotNull Teacher teacher) {
        return JWT.create()
                .withSubject(teacher.getId().toString())
                .withClaim("access", true)
                .withClaim("teacher", true)
                .withExpiresAt(new Date(System.currentTimeMillis() + ACCESS_TOKEN_EXPIRATION_TIME))
                .sign(Algorithm.HMAC512(SECRET.getBytes()));
    }

    public String genAccessToken(@NotNull Student student) {
        return JWT.create()
                .withSubject(student.getId().toString())
                .withClaim("access", true)
                .withClaim("teacher", false)
                .withExpiresAt(new Date(System.currentTimeMillis() + ACCESS_TOKEN_EXPIRATION_TIME))
                .sign(Algorithm.HMAC512(SECRET.getBytes()));
    }

    public Optional<String> getRefreshToken(HttpServletRequest request) {
        String headerValue = request.getHeader(AUTH_HEADER);
        Cookie cookie = WebUtils.getCookie(request, "refreshToken");
        if (headerValue == null && cookie == null)
            return Optional.empty();
        String value = Objects.requireNonNullElseGet(headerValue, () -> Objects.requireNonNull(cookie).getValue());
        return Optional.of(value);
    }

    public JWTData verifyJwt(String jwt) {
        try {
            var data = JWT.require(Algorithm.HMAC512(SECRET.getBytes())).build().verify(jwt);
            UUID uid = UUID.fromString(data.getSubject());
            boolean isTeacher = data.getClaim("teacher").asBoolean();
            boolean isAccess = data.getClaim("access").asBoolean();
            return new JWTData(true, data.getIssuedAt(), uid, isTeacher, isAccess);
        } catch (JWTVerificationException verificationException) {
            log.error("Invalid JWT: " + verificationException);
            return new JWTData(false, null, null, false, false);
        }
    }

    public Optional<Student> require(HttpServletRequest request) {
        String headerValue = request.getHeader(AUTH_HEADER);
        Cookie cookie = WebUtils.getCookie(request, "accessToken");
        if (headerValue == null && cookie == null)
            return Optional.empty();
        String value = Objects.requireNonNullElseGet(headerValue, () -> Objects.requireNonNull(cookie).getValue());
        var data = verifyJwt(value);
        if (!data.isValid() || data.isTeacher())
            return Optional.empty();

        return AggregatorService.INSTANCE.getStudentService().findById(data.getUid());
    }

    public Optional<Teacher> requireTeacher(HttpServletRequest request) {
        String headerValue = request.getHeader(AUTH_HEADER);
        Cookie cookie = WebUtils.getCookie(request, "accessToken");
        if (headerValue == null && cookie == null)
            return Optional.empty();
        String value = Objects.requireNonNullElseGet(headerValue, () -> Objects.requireNonNull(cookie).getValue());
        var data = verifyJwt(value);
        if (!data.isValid() || !data.isTeacher())
            return Optional.empty();

        return AggregatorService.INSTANCE.getTeacherService().findById(data.getUid());

    }

    public String persistRefresh(HttpServletResponse response, String refreshToken) {
        Cookie cookie = new Cookie("refreshToken", refreshToken);
        cookie.setHttpOnly(true);
        response.addCookie(cookie);
        return refreshToken;
    }

    public String persistAccess(HttpServletResponse response, String accessToken) {
        Cookie cookie = new Cookie("accessToken", accessToken);
        cookie.setHttpOnly(true);
        response.addCookie(cookie);
        return accessToken;
    }

    @Data
    public static class JWTData {
        private final boolean valid;
        private final Date issuedAt;
        private final UUID uid;
        private final boolean teacher;
        private final boolean access;
    }
}
