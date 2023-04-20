package space.maxus.dnevnik;

import lombok.extern.log4j.Log4j2;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import space.maxus.dnevnik.data.model.*;
import space.maxus.dnevnik.data.service.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.UUID;

@SpringBootApplication
@Log4j2
public class DiaryServerApplication {
	private static TeacherService teacherService;
	private static StudentService studentService;
	private static GroupService groupService;
	private static MarkService markService;
	private static LessonService lessonService;
	private static ScheduleService scheduleService;
	private static HomeworkService homeworkService;
	private static SubjectService subjectService;
	private static AttachmentService attachmentService;

	public DiaryServerApplication(TeacherService teacherService, StudentService studentService, GroupService groupService, MarkService markService, HomeworkService homeworkService, ScheduleService scheduleService, LessonService lessonService, SubjectService subjectService, AttachmentService attachmentService) {
		DiaryServerApplication.teacherService = teacherService;
		DiaryServerApplication.studentService = studentService;
		DiaryServerApplication.groupService = groupService;
		DiaryServerApplication.markService = markService;
		DiaryServerApplication.homeworkService = homeworkService;
		DiaryServerApplication.scheduleService = scheduleService;
		DiaryServerApplication.lessonService = lessonService;
		DiaryServerApplication.subjectService = subjectService;
		DiaryServerApplication.attachmentService = attachmentService;
	}

	public static void main(String[] args) {
		SpringApplication.run(DiaryServerApplication.class, args);

		var teacher = new Teacher("Teacher Name", "Teacher Surname", "Teacher Patr.", "Teacher Email", "Teacher Password");
		var student = new Student("Student Name", "Student Surname", "Student Email", "Student Password", new ArrayList<>());
		var attachment = new Attachment(1L, "/test.txt");
		var homework1 = new Homework(1L, false, "Some homework summary", teacher.getId(), new long[] {1});
		var homework2 = new Homework(2L, true, "", teacher.getId(), new long[0]);
		var group = new Group("Group Name", teacher, new UUID[]{student.getId()});
		var subject1 = new Subject(1, "Maths", new UUID[] { teacher.getId() });
		var subject2 = new Subject(2, "Physics", new UUID[] { teacher.getId() });
		var schedule = new Schedule(1L, group,(byte) 1, new long[] {subject1.getId(), subject2.getId()});
		var lesson1 = new Lesson(1, new long[] { 1, 2 }, homework1.getId(), java.util.Date.from(Instant.now()));
		var lesson2 = new Lesson(2, new long[] { 3 }, homework2.getId(), java.util.Date.from(Instant.now()));
		var mark1 = new Mark((byte) 4, (byte) 3, Mark.Kind.GENERIC, lesson1, student, teacher.getId());
		var mark2 = new Mark((byte) 5, (byte) 4, Mark.Kind.EXAM, lesson1, student, teacher.getId());
		var mark3 = new Mark((byte) 0, (byte) 1, Mark.Kind.POINT, lesson2, student, teacher.getId());

		teacherService.insertUpdate(teacher);
		studentService.insertUpdate(student);
		subjectService.insertUpdate(subject1);
		subjectService.insertUpdate(subject2);
		homeworkService.insertUpdate(homework1);
		homeworkService.insertUpdate(homework2);
		attachmentService.insertUpdate(attachment);
		groupService.insertUpdate(group);
		scheduleService.insertUpdate(schedule);
		lessonService.insertUpdate(lesson1);
		lessonService.insertUpdate(lesson2);
		markService.insertUpdate(mark1);
		markService.insertUpdate(mark2);
		markService.insertUpdate(mark3);
	}
}
