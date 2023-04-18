package space.maxus.dnevnik.controllers;

import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import space.maxus.dnevnik.data.cascade.CascadingSubject;
import space.maxus.dnevnik.data.model.Subject;
import space.maxus.dnevnik.data.model.Teacher;
import space.maxus.dnevnik.data.service.SubjectService;
import space.maxus.dnevnik.data.service.TeacherService;

import java.util.List;

@RestController
@Log4j2
public class TestController {
    private final TeacherService service;
    private final SubjectService subjects;

    public TestController(TeacherService service, SubjectService subjects) {
        this.service = service;
        this.subjects = subjects;
    }

    @GetMapping("/teachers")
    public List<Teacher> allTeachers() {
        return service.findAll();
    }

    @GetMapping("/subjects")
    public List<CascadingSubject> allSubjects() {
        return subjects.findAll().stream().map(Subject::cascade).toList();
    }
}
