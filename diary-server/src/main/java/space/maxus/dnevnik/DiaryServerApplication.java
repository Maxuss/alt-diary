package space.maxus.dnevnik;

import lombok.extern.log4j.Log4j2;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import space.maxus.dnevnik.data.model.Subject;
import space.maxus.dnevnik.data.model.Teacher;
import space.maxus.dnevnik.data.service.SubjectService;
import space.maxus.dnevnik.data.service.TeacherService;

import java.util.UUID;
import java.util.stream.Stream;

@SpringBootApplication
@Log4j2
public class DiaryServerApplication {
	private static TeacherService teacherService;
	private static SubjectService subjectService;

	public DiaryServerApplication(TeacherService teacherService, SubjectService subjectService) {
		DiaryServerApplication.teacherService = teacherService;
		DiaryServerApplication.subjectService = subjectService;
	}

	public static void main(String[] args) {
		SpringApplication.run(DiaryServerApplication.class, args);

		var teacher1 = new Teacher(UUID.randomUUID(), "Name 1", "Surname 1", "Patr. 1", "email 1", "<>");
		var teacher2 = new Teacher(UUID.randomUUID(), "Name 2", "Surname 2", "Patr. 2", "email 2", "<>");
		var subject1 = new Subject("Subject 1", Stream.of(teacher1, teacher2).map(Teacher::getId).toList());
		var subject2 = new Subject("Subject 2", Stream.of(teacher1).map(Teacher::getId).toList());
		var subject3 = new Subject("Subject 3", Stream.of(teacher2).map(Teacher::getId).toList());

		log.error(subject1);

		teacherService.insertUpdate(teacher1);
		teacherService.insertUpdate(teacher2);

		subjectService.insertUpdate(subject1);
		subjectService.insertUpdate(subject2);
		subjectService.insertUpdate(subject3);
	}

}
