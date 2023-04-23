package space.maxus.dnevnik.controllers.general;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import space.maxus.dnevnik.auth.Auth;
import space.maxus.dnevnik.controllers.request.create.HomeworkCreateRequest;
import space.maxus.dnevnik.controllers.response.QueryResponse;
import space.maxus.dnevnik.controllers.response.ResponseAttachment;
import space.maxus.dnevnik.controllers.response.ResponseHomework;
import space.maxus.dnevnik.data.model.Attachment;
import space.maxus.dnevnik.data.model.Homework;
import space.maxus.dnevnik.data.model.Lesson;
import space.maxus.dnevnik.data.service.AttachmentService;
import space.maxus.dnevnik.data.service.HomeworkService;
import space.maxus.dnevnik.data.service.LessonService;
import space.maxus.dnevnik.util.FileService;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Objects;

@RestController
public class HomeworkController {
    private final HomeworkService homeworkService;
    private final AttachmentService attachmentService;
    private final FileService fileService;
    private final LessonService lessonService;

    public HomeworkController(HomeworkService homeworkService, AttachmentService attachmentService, FileService fileService, LessonService lessonService) {
        this.homeworkService = homeworkService;
        this.attachmentService = attachmentService;
        this.fileService = fileService;
        this.lessonService = lessonService;
    }

    @PostMapping("/attachments/create")
    public QueryResponse<ResponseAttachment> createAttachment(
            HttpServletRequest request,
            HttpServletResponse response,
            @RequestParam("file") MultipartFile file
    ) throws IOException {
        if (Auth.requireAny(request).isEmpty())
            return Auth.notAuthorized(response);
        Path path = fileService.saveFile(file, Objects.requireNonNull(file.getOriginalFilename()));
        String fileName = path.getFileName().toString();
        Attachment attachment = new Attachment(fileName);
        attachmentService.insertUpdate(attachment);
        return QueryResponse.success(attachment.response());
    }

    @GetMapping("/attachments/get/{name}")
    public ResponseEntity<InputStreamResource> readAttachment(
            HttpServletResponse response,
            @PathVariable("name") String fileName
    ) throws IOException {
        return fileService.loadFileResponse(response, fileName);
    }

    @PutMapping("/homework/append")
    public QueryResponse<ResponseHomework> createHomework(
            HttpServletRequest request,
            HttpServletResponse response,
            @RequestBody HomeworkCreateRequest create
    ) {
        return Auth.requireTeacher(request)
                .map(teacher -> {
                    Lesson lesson = lessonService.findById(create.getLessonId()).orElseThrow();
                    Homework existing = homeworkService.findById(lesson.getHomeworkId()).orElseThrow();
                    Homework homework = new Homework(
                            create.isNone(),
                            create.getSummary(),
                            teacher.getId(),
                            ArrayUtils.toPrimitive(create.getAttachments().toArray(new Long[0]))
                    );
                    homeworkService.insertUpdate(existing.merge(homework));
                    return QueryResponse.success(existing.response());
                })
                .orElseGet(() -> Auth.notAuthorized(response));
    }
}
