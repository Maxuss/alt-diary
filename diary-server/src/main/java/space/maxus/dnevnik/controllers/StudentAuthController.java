package space.maxus.dnevnik.controllers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.SneakyThrows;
import lombok.experimental.StandardException;
import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.util.Strings;
import org.springframework.http.HttpStatus;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import space.maxus.dnevnik.auth.Auth;
import space.maxus.dnevnik.auth.MailService;
import space.maxus.dnevnik.controllers.request.ConfirmationRequest;
import space.maxus.dnevnik.controllers.request.LoginRequest;
import space.maxus.dnevnik.controllers.request.RegisterRequest;
import space.maxus.dnevnik.controllers.response.QueryResponse;
import space.maxus.dnevnik.controllers.response.auth.ConfirmationResponse;
import space.maxus.dnevnik.controllers.response.auth.IntermediateConfirmationResponse;
import space.maxus.dnevnik.controllers.response.auth.LoginResponse;
import space.maxus.dnevnik.controllers.response.auth.RefreshResponse;
import space.maxus.dnevnik.data.model.Student;
import space.maxus.dnevnik.data.service.StudentService;
import space.maxus.dnevnik.exception.JwtNotProvidedException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

@RestController
@Log4j2
public class StudentAuthController {
    private final StudentService studentService;
    private final MailService mailService;

    public StudentAuthController(StudentService studentService, MailService mailService) {
        this.studentService = studentService;
        this.mailService = mailService;
    }

    @PostMapping("/student/login")
    @SneakyThrows
    public QueryResponse<LoginResponse> studentLogin(HttpServletRequest request, @RequestBody LoginRequest login, HttpServletResponse response) {
        Student student = Auth.studentFromEmail(login.getEmail()).orElseThrow(() -> new StudentNotFoundException("Не удалось найти ученика с этой почтой!"));
        boolean hashAuthentic = Auth.verifyHash(student.getPassHash(), login.getPassword().toCharArray());
        if (!hashAuthentic)
            return QueryResponse.failure("Неверный пароль");
        return QueryResponse.success(new LoginResponse(Auth.persistAccess(response, Auth.genAccessToken(student)), Auth.persistRefresh(response, Auth.genRefreshToken(student)), student.getId(), new Date(System.currentTimeMillis() + Auth.STUDENT_EXPIRATION_TIME)));
    }

    @PostMapping("/student/register")
    @SneakyThrows
    public QueryResponse<LoginResponse> studentRegister(HttpServletRequest request, @RequestBody @Valid RegisterRequest register, HttpServletResponse response) {
        return Auth.studentFromEmail(register.getEmail())
                .map(student -> QueryResponse.<LoginResponse>failure("Эта почта уже используется"))
                .orElseGet(() -> {
                    var newStudent = new Student(register.getName(), register.getSurname(), register.getEmail(), Auth.hashPassword(register.getPassword().toCharArray()), new ArrayList<>());
                    studentService.insertUpdate(newStudent);
                    return QueryResponse.success(new LoginResponse(Auth.persistAccess(response, Auth.genAccessToken(newStudent)), Auth.persistRefresh(response, Auth.genRefreshToken(newStudent)), newStudent.getId(), new Date(System.currentTimeMillis() + Auth.STUDENT_EXPIRATION_TIME)));
                });
    }

    @PostMapping("/student/refresh")
    @SneakyThrows
    public QueryResponse<RefreshResponse> studentRefresh(HttpServletRequest request, HttpServletResponse response, @RequestHeader MultiValueMap<String, String> headers) {
        Auth.JWTData jwt = Auth.verifyJwt(Auth.getRefreshToken(request).orElseThrow(() -> new JwtNotProvidedException("Токен обновления не указан")));
        if (!jwt.isValid() || jwt.isTeacher())
            return QueryResponse.failure("Неверный токен обновления");
        return studentService.findById(jwt.getUid())
                .map(student -> QueryResponse.success(new RefreshResponse(new Date(System.currentTimeMillis() + Auth.ACCESS_TOKEN_EXPIRATION_TIME), Auth.persistAccess(response, Auth.genAccessToken(student)), jwt.getUid())))
                .orElseGet(() -> QueryResponse.failure("Не удалось найти ученика"));
    }

    @PostMapping("/student/confirm/request")
    public QueryResponse<IntermediateConfirmationResponse> requestConfirm(HttpServletRequest request, HttpServletResponse response) {
        return Auth.require(request).map(student -> {
            if (student.isConfirmed())
                return QueryResponse.<IntermediateConfirmationResponse>failure("Аккаунт уже подтвержден");
            mailService.sendValidationMail(student.getEmail(), Auth.genConfirmCode(student.getId()));
            return QueryResponse.success(new IntermediateConfirmationResponse());
        }).orElseGet(() -> Auth.notAuthorized(response));
    }

    @PostMapping("/student/confirm/commit")
    public QueryResponse<ConfirmationResponse> confirmCommit(HttpServletRequest request, HttpServletResponse response, @RequestBody ConfirmationRequest confirm) {
        return Auth.require(request).map(student -> {
            if (student.isConfirmed())
                return QueryResponse.<ConfirmationResponse>failure("Аккаунт уже подтвержден");
            return Auth.validateConfirmCode(confirm.getCode()).map(valid -> {
                student.setConfirmed(true);
                studentService.insertUpdate(student);
                return QueryResponse.success(new ConfirmationResponse());
            }).orElseGet(() -> QueryResponse.failure("Неверный код подтверждения"));
        }).orElseGet(() -> Auth.notAuthorized(response));
    }

    @ExceptionHandler(StudentNotFoundException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public QueryResponse<Void> notFoundErrorHandler(StudentNotFoundException e) {
        return QueryResponse.failure(e.getMessage());
    }

    @StandardException
    private static class StudentNotFoundException extends Exception {
    }
}
