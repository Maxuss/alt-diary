package space.maxus.dnevnik.controllers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import lombok.experimental.StandardException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import space.maxus.dnevnik.auth.Auth;
import space.maxus.dnevnik.controllers.request.LoginRequest;
import space.maxus.dnevnik.controllers.request.RegisterRequest;
import space.maxus.dnevnik.controllers.response.QueryResponse;
import space.maxus.dnevnik.controllers.response.auth.LoginResponse;
import space.maxus.dnevnik.controllers.response.auth.RefreshResponse;
import space.maxus.dnevnik.data.model.Student;
import space.maxus.dnevnik.data.service.StudentService;

import java.util.ArrayList;
import java.util.Date;

@RestController
public class AuthController {
    private final StudentService studentService;

    public AuthController(StudentService studentService) {
        this.studentService = studentService;
    }

    @PostMapping("/student/login")
    @SneakyThrows
    public QueryResponse<LoginResponse> studentLogin(HttpServletRequest request, @RequestBody LoginRequest login, HttpServletResponse response) {
        Student student = Auth.studentFromEmail(login.getEmail()).orElseThrow(() -> new StudentNotFoundException("Could not find student with provided email!"));
        boolean hashAuthentic = Auth.verifyHash(student.getPassHash(), login.getPassword().toCharArray());
        if(!hashAuthentic)
            return QueryResponse.failure("Invalid password");
        return QueryResponse.success(new LoginResponse(Auth.persistAccess(response, Auth.genAccessToken(student)), Auth.persistRefresh(response, Auth.genRefreshToken(student)), student.getId(), new Date(System.currentTimeMillis() + Auth.STUDENT_EXPIRATION_TIME)));
    }

    @PostMapping("/student/register")
    @SneakyThrows
    public QueryResponse<LoginResponse> studentRegister(HttpServletRequest request, @RequestBody RegisterRequest register, HttpServletResponse response) {
        return Auth.studentFromEmail(register.getEmail())
                .map(student -> QueryResponse.<LoginResponse>failure("Student with this email already exists"))
                .orElseGet(() -> {
                    var newStudent = new Student(register.getName(), register.getSurname(), register.getEmail(), Auth.hashPassword(register.getPassword().toCharArray()), new ArrayList<>());
                    studentService.insertUpdate(newStudent);
                    return QueryResponse.success(new LoginResponse(Auth.persistAccess(response, Auth.genAccessToken(newStudent)), Auth.persistRefresh(response, Auth.genRefreshToken(newStudent)), newStudent.getId(), new Date(System.currentTimeMillis() + Auth.STUDENT_EXPIRATION_TIME)));
                });
    }

    @GetMapping("/student/refresh")
    @SneakyThrows
    public QueryResponse<RefreshResponse> studentRefresh(HttpServletRequest request, HttpServletResponse response) {
        Auth.JWTData jwt = Auth.verifyJwt(Auth.getRefreshToken(request).orElseThrow(() -> new JwtNotProvidedException("No refresh token provided")));
        if(!jwt.isValid() || jwt.isTeacher())
            return QueryResponse.failure("Invalid refresh token");
        return studentService.findById(jwt.getUid())
                .map(student -> QueryResponse.success(new RefreshResponse(Auth.persistAccess(response, Auth.genAccessToken(student)), jwt.getUid(), new Date(System.currentTimeMillis() + Auth.ACCESS_TOKEN_EXPIRATION_TIME))))
                .orElseGet(() -> QueryResponse.failure("Could not find student"));
    }

    @GetMapping("/student/test")
    public QueryResponse<Student> testRequireAuth(HttpServletRequest request) {
        return Auth.require(request).map(QueryResponse::success).orElse(QueryResponse.failure("Failed to authorize"));
    }

    @StandardException
    private static class StudentNotFoundException extends Exception { }
    @StandardException
    private static class JwtNotProvidedException extends Exception { }
}
