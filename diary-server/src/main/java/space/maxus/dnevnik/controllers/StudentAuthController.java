package space.maxus.dnevnik.controllers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.SneakyThrows;
import lombok.experimental.StandardException;
import org.springframework.http.HttpStatus;
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

import java.util.ArrayList;
import java.util.Date;

@RestController
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
        Student student = Auth.studentFromEmail(login.getEmail()).orElseThrow(() -> new StudentNotFoundException("Could not find student with provided email!"));
        boolean hashAuthentic = Auth.verifyHash(student.getPassHash(), login.getPassword().toCharArray());
        if (!hashAuthentic)
            return QueryResponse.failure("Invalid password");
        return QueryResponse.success(new LoginResponse(Auth.persistAccess(response, Auth.genAccessToken(student)), Auth.persistRefresh(response, Auth.genRefreshToken(student)), student.getId(), new Date(System.currentTimeMillis() + Auth.STUDENT_EXPIRATION_TIME)));
    }

    @PostMapping("/student/register")
    @SneakyThrows
    public QueryResponse<LoginResponse> studentRegister(HttpServletRequest request, @RequestBody @Valid RegisterRequest register, HttpServletResponse response) {
        return Auth.studentFromEmail(register.getEmail())
                .map(student -> QueryResponse.<LoginResponse>failure("Student with this email already exists"))
                .orElseGet(() -> {
                    var newStudent = new Student(register.getName(), register.getSurname(), register.getEmail(), Auth.hashPassword(register.getPassword().toCharArray()), new ArrayList<>());
                    studentService.insertUpdate(newStudent);
                    return QueryResponse.success(new LoginResponse(Auth.persistAccess(response, Auth.genAccessToken(newStudent)), Auth.persistRefresh(response, Auth.genRefreshToken(newStudent)), newStudent.getId(), new Date(System.currentTimeMillis() + Auth.STUDENT_EXPIRATION_TIME)));
                });
    }

    @PostMapping("/student/refresh")
    @SneakyThrows
    public QueryResponse<RefreshResponse> studentRefresh(HttpServletRequest request, HttpServletResponse response) {
        Auth.JWTData jwt = Auth.verifyJwt(Auth.getRefreshToken(request).orElseThrow(() -> new JwtNotProvidedException("No refresh token provided")));
        if (!jwt.isValid() || jwt.isTeacher())
            return QueryResponse.failure("Invalid refresh token");
        return studentService.findById(jwt.getUid())
                .map(student -> QueryResponse.success(new RefreshResponse(new Date(System.currentTimeMillis() + Auth.ACCESS_TOKEN_EXPIRATION_TIME), Auth.persistAccess(response, Auth.genAccessToken(student)), jwt.getUid())))
                .orElseGet(() -> QueryResponse.failure("Could not find student"));
    }

    @GetMapping("/student/test")
    public QueryResponse<Student> testRequireAuth(HttpServletRequest request) {
        return Auth.require(request).map(QueryResponse::success).orElse(QueryResponse.failure("Failed to authorize"));
    }

    @PostMapping("/student/confirm/request")
    public QueryResponse<IntermediateConfirmationResponse> requestConfirm(HttpServletRequest request, HttpServletResponse response) {
        return Auth.require(request).map(student -> {
            if(student.isConfirmed())
                return QueryResponse.<IntermediateConfirmationResponse>failure("Already confirmed");
            mailService.sendValidationMail(student.getEmail(), Auth.genConfirmCode(student.getId()));
            return QueryResponse.success(new IntermediateConfirmationResponse());
        }).orElseGet(() -> Auth.notAuthorized(response));
    }

    @PostMapping("/student/confirm/commit")
    public QueryResponse<ConfirmationResponse> confirmCommit(HttpServletRequest request, HttpServletResponse response, @RequestBody ConfirmationRequest confirm) {
        return Auth.require(request).map(student -> {
            if(student.isConfirmed())
                return QueryResponse.<ConfirmationResponse>failure("Already confirmed");
            return Auth.validateConfirmCode(confirm.getCode()).map(valid -> {
                student.setConfirmed(true);
                studentService.insertUpdate(student);
                return QueryResponse.success(new ConfirmationResponse());
            }).orElseGet(() -> QueryResponse.failure("Invalid code"));
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

    @StandardException
    private static class JwtNotProvidedException extends Exception {
    }
}
