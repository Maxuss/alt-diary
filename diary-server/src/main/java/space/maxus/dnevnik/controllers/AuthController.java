package space.maxus.dnevnik.controllers;

import jakarta.servlet.http.HttpServletRequest;
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
    public QueryResponse<LoginResponse> studentLogin(HttpServletRequest request, @RequestBody LoginRequest login) {
        Student student = Auth.studentFromEmail(login.getEmail()).orElseThrow(() -> new StudentNotFoundException("Could not find student with provided email!"));
        boolean hashAuthentic = Auth.verifyHash(student.getPassHash(), login.getPassword().toCharArray());
        if(!hashAuthentic)
            return QueryResponse.failure("Invalid password");
        return QueryResponse.success(new LoginResponse(Auth.genToken(student), student.getId(), new Date(System.currentTimeMillis() + Auth.EXPIRATION_TIME)));
    }

    @PostMapping("/student/register")
    @SneakyThrows
    public QueryResponse<LoginResponse> studentRegister(HttpServletRequest request, @RequestBody RegisterRequest register) {
        return Auth.studentFromEmail(register.getEmail())
                .map(student -> QueryResponse.<LoginResponse>failure("Student with this email already exists"))
                .orElseGet(() -> {
                    var newStudent = new Student(register.getName(), register.getSurname(), register.getEmail(), Auth.hashPassword(register.getPassword().toCharArray()), new ArrayList<>());
                    studentService.insertUpdate(newStudent);
                    return QueryResponse.success(new LoginResponse(Auth.genToken(newStudent), newStudent.getId(), new Date(System.currentTimeMillis() + Auth.EXPIRATION_TIME)));
                });
    }

    @GetMapping("/student/test")
    public QueryResponse<Student> testRequireAuth(HttpServletRequest request) {
        return Auth.require(request).map(QueryResponse::success).orElse(QueryResponse.failure("Failed to authorize"));
    }

    @StandardException
    public static class StudentNotFoundException extends Exception { }
}
