package space.maxus.dnevnik.data.fetch;

import lombok.Getter;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;
import space.maxus.dnevnik.data.service.*;

@Service
@Getter
public class AggregatorService implements InitializingBean {
    public static AggregatorService INSTANCE = null;

    private final TeacherService teacherService;
    private final StudentService studentService;
    private final AttachmentService attachmentService;
    private final HomeworkService homeworkService;
    private final SubjectService subjectService;
    private final LessonService lessonService;

    public AggregatorService(TeacherService teacherService, StudentService studentService, AttachmentService attachmentService, HomeworkService homeworkService, SubjectService subjectService, LessonService lessonService) {
        this.teacherService = teacherService;
        this.studentService = studentService;
        this.attachmentService = attachmentService;
        this.homeworkService = homeworkService;
        this.subjectService = subjectService;
        this.lessonService = lessonService;
    }

    @Override
    public void afterPropertiesSet() {
        if (INSTANCE == null)
            AggregatorService.INSTANCE = this;
    }
}
