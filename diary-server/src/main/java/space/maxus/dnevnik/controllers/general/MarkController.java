package space.maxus.dnevnik.controllers.general;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.*;
import space.maxus.dnevnik.auth.Auth;
import space.maxus.dnevnik.controllers.request.create.MarkCreateRequest;
import space.maxus.dnevnik.controllers.response.QueryResponse;
import space.maxus.dnevnik.controllers.response.ResponseMark;
import space.maxus.dnevnik.data.model.Lesson;
import space.maxus.dnevnik.data.model.Mark;
import space.maxus.dnevnik.data.service.LessonService;
import space.maxus.dnevnik.data.service.MarkService;
import space.maxus.dnevnik.data.service.StudentService;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@RestController
public class MarkController {
    private final MarkService markService;
    private final LessonService lessonService;
    private final StudentService studentService;

    public MarkController(MarkService markService, LessonService lessonService, StudentService studentService) {
        this.markService = markService;
        this.lessonService = lessonService;
        this.studentService = studentService;
    }

    @PutMapping("/marks/grant")
    public QueryResponse<ResponseMark> createMark(
            HttpServletRequest request,
            HttpServletResponse response,
            @RequestBody MarkCreateRequest create
    ) {
        return Auth.requireTeacher(request)
                .map(teacher -> {
                    if(!lessonService.getRepository().existsById(create.getLessonId()))
                        return QueryResponse.<ResponseMark>failure("Invalid lesson ID");
                    if(!studentService.getRepository().existsById(create.getStudentId()))
                        return QueryResponse.<ResponseMark>failure("Invalid student ID");
                    Lesson lesson = lessonService.findById(create.getLessonId()).orElseThrow();
                    Mark mark = new Mark(
                            create.getValue(),
                            create.getIndex(),
                            create.getKind(),
                            create.getMessage(),
                            lesson,
                            studentService.findById(create.getStudentId()).orElseThrow(),
                            teacher.getId()
                    );
                    markService.insertUpdate(mark);
                    Lesson newLesson = lesson.withAdditionalMark(mark.getId());
                    lessonService.insertUpdate(newLesson);
                    return QueryResponse.success(mark.response());
                })
                .orElseGet(() -> Auth.notAuthorized(response));
    }

    @GetMapping("/marks/lesson/{lesson}")
    public QueryResponse<List<ResponseMark>> marksForLesson(
            HttpServletRequest request,
            HttpServletResponse response,
            @PathVariable("lesson") Long lesson
    ) {
        return Auth.require(request)
                .map(student -> QueryResponse.success(student.getMarks().stream().filter(mark -> Objects.equals(mark.getLesson().getId(), lesson)).map(Mark::response).toList()))
                .orElseGet(() -> Auth.notAuthorized(response));
    }

    @GetMapping("/marks/all")
    public QueryResponse<List<ResponseMark>> marksForStudent(
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        return Auth.require(request)
                .map(student -> QueryResponse.success(student.getMarks().stream().map(Mark::response).toList()))
                .orElseGet(() -> Auth.notAuthorized(response));
    }

    @GetMapping("/marks/student/{student}")
    public QueryResponse<List<ResponseMark>> marksForStudent(
            HttpServletRequest request,
            HttpServletResponse response,
            @PathVariable("student") UUID student
    ) {
        return Auth.requireAny(request)
                .map(uid -> QueryResponse.success(studentService.findById(student).orElseThrow().getMarks().stream().map(Mark::response).toList()))
                .orElseGet(() -> Auth.notAuthorized(response));
    }

    @GetMapping("/marks/subject/{subject}")
    public QueryResponse<List<ResponseMark>> marksForSubject(
            HttpServletRequest request,
            HttpServletResponse response,
            @PathVariable("subject") long subject
    ) {
        return Auth.require(request)
                .map(student -> QueryResponse.success(student.getMarks().stream().filter(mark -> mark.getLesson().getSubjectId() == subject).map(Mark::response).toList()))
                .orElseGet(() -> Auth.notAuthorized(response));
    }
}
