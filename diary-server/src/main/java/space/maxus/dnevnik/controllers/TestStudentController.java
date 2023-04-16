package space.maxus.dnevnik.controllers;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import space.maxus.dnevnik.data.model.Student;
import space.maxus.dnevnik.data.service.StudentService;

import java.util.List;

@RestController
@Log4j2
public class TestStudentController {
    @Autowired
    private StudentService studentService;

    @GetMapping("/getall")
    public List<Student> findStudents() {

        return studentService.findAll();
    }

    @PutMapping("/add")
    public Boolean insertStudent(@RequestBody Student student) {
        log.info(student);
        studentService.insertUpdate(student);
        return true;
    }
}
