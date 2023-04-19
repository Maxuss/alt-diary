package space.maxus.dnevnik;

import lombok.extern.log4j.Log4j2;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import space.maxus.dnevnik.data.model.Group;
import space.maxus.dnevnik.data.model.Student;
import space.maxus.dnevnik.data.model.Teacher;
import space.maxus.dnevnik.data.service.GroupService;
import space.maxus.dnevnik.data.service.StudentService;
import space.maxus.dnevnik.data.service.TeacherService;

import java.util.UUID;
import java.util.stream.Stream;

@SpringBootApplication
@Log4j2
public class DiaryServerApplication {
	private static TeacherService teacherService;
	private static StudentService studentService;
	private static GroupService groupService;

	public DiaryServerApplication(TeacherService teacherService, StudentService studentService, GroupService groupService) {
		DiaryServerApplication.teacherService = teacherService;
		DiaryServerApplication.studentService = studentService;
		DiaryServerApplication.groupService = groupService;
	}

	public static void main(String[] args) {
		SpringApplication.run(DiaryServerApplication.class, args);


		var teacher1 = new Teacher("Teacher Name", "Teacher Surname", "Patr.", "email 1", "<>");
		var student1 = new Student("Name 1", "Surname 1", "email 1", "<>");
		var student2 = new Student("Name 2", "Surname 2", "email 2", "<>");
		var group = new Group("Test Group", teacher1, Stream.of(student1, student2).map(Student::getId).toList().toArray(new UUID[0]));

		teacherService.insertUpdate(teacher1);
		studentService.insertUpdate(student1);
		studentService.insertUpdate(student2);
		groupService.insertUpdate(group);
	}
}
