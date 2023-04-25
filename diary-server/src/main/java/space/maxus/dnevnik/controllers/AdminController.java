package space.maxus.dnevnik.controllers;

import lombok.Data;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import space.maxus.dnevnik.auth.Auth;
import space.maxus.dnevnik.controllers.response.ResponseStudent;
import space.maxus.dnevnik.controllers.response.ResponseTeacher;
import space.maxus.dnevnik.data.model.Student;
import space.maxus.dnevnik.data.model.Teacher;
import space.maxus.dnevnik.data.service.StudentService;
import space.maxus.dnevnik.data.service.TeacherService;
import space.maxus.dnevnik.util.DiaryConfiguration;

import java.util.List;
import java.util.Objects;

@RestController
public class AdminController {
    private final DiaryConfiguration configuration;
    private final StudentService studentService;
    private final TeacherService teacherService;

    public AdminController(DiaryConfiguration configuration, StudentService studentService, TeacherService teacherService) {
        this.configuration = configuration;
        this.studentService = studentService;
        this.teacherService = teacherService;
    }

    @PostMapping("/admin/teacher-token")
    public String genTeacherRegisterToken(@RequestBody AdminRequest<String> request) {
        if (Objects.equals(request.adminToken, configuration.getAdmin().getSecret()))
            return Auth.genTeacherRegisterToken(request.payload);
        return null;
    }

    @PostMapping("/admin/all-students")
    public List<ResponseStudent> getAllStudents(@RequestBody AdminRequest<String> request) {
        if (Objects.equals(request.adminToken, configuration.getAdmin().getSecret()))
            return studentService.findAll().stream().map(Student::response).toList();
        return null;
    }

    @PostMapping("/admin/all-teachers")
    public List<ResponseTeacher> getAllTeachers(@RequestBody AdminRequest<String> request) {
        if (Objects.equals(request.adminToken, configuration.getAdmin().getSecret()))
            return teacherService.findAll().stream().map(Teacher::response).toList();
        return null;
    }


    @Data
    private static class AdminRequest<V> {
        private final V payload;
        private final String adminToken;
    }
}
