package space.maxus.dnevnik.controllers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import lombok.experimental.StandardException;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import space.maxus.dnevnik.auth.Auth;
import space.maxus.dnevnik.controllers.request.LoginRequest;
import space.maxus.dnevnik.controllers.request.TeacherRegisterRequest;
import space.maxus.dnevnik.controllers.response.QueryResponse;
import space.maxus.dnevnik.controllers.response.auth.LoginResponse;
import space.maxus.dnevnik.data.model.Teacher;
import space.maxus.dnevnik.data.service.TeacherService;

import java.util.Date;

@RestController
public class TeacherAuthController {
    private final TeacherService teacherService;

    public TeacherAuthController(TeacherService teacherService) {
        this.teacherService = teacherService;
    }

    @SneakyThrows
    @PostMapping("/teacher/login")
    public QueryResponse<LoginResponse> login(HttpServletRequest request, HttpServletResponse response, @RequestBody @NotNull LoginRequest login) {
        Teacher teacher = Auth.teacherFromEmail(login.getEmail()).orElseThrow(() -> new TeacherNotFoundException("Could not find teacher with email %s".formatted(login.getEmail())));
        boolean hashAuthentic = Auth.verifyHash(teacher.getPassHash(), login.getPassword().toCharArray());
        if(!hashAuthentic)
            return QueryResponse.failure("Incorrect password");
        return QueryResponse.success(new LoginResponse(Auth.persistAccess(response, Auth.genAccessToken(teacher)), Auth.persistRefresh(response, Auth.genRefreshToken(teacher)), teacher.getId(), new Date(System.currentTimeMillis() + Auth.STUDENT_EXPIRATION_TIME)));
    }

    @PostMapping("/teacher/register")
    public QueryResponse<LoginResponse> register(HttpServletRequest request, HttpServletResponse response, @RequestBody TeacherRegisterRequest register) {
        return Auth.validateRegisterToken(register.getRegisterToken())
                .map(email -> {
                    if(!email.equals(register.getEmail()))
                        return QueryResponse.<LoginResponse>failure("Invalid email");
                    Teacher newTeacher = new Teacher(register.getName(), register.getSurname(), register.getPatronymic(), register.getEmail(), Auth.hashPassword(register.getPassword().toCharArray()));
                    teacherService.insertUpdate(newTeacher);
                    return QueryResponse.success(new LoginResponse(Auth.persistAccess(response, Auth.genAccessToken(newTeacher)), Auth.persistRefresh(response, Auth.genRefreshToken(newTeacher)), newTeacher.getId(), new Date(System.currentTimeMillis() + Auth.STUDENT_EXPIRATION_TIME)));
                })
                .orElseGet(() -> QueryResponse.failure("Invalid register token"));
    }

    @ExceptionHandler(TeacherNotFoundException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public QueryResponse<LoginResponse> handleTeacherNotFoundException(TeacherNotFoundException e) {
        return QueryResponse.failure(e.getMessage());
    }

    @StandardException
    private static class TeacherNotFoundException extends Exception { }
}

