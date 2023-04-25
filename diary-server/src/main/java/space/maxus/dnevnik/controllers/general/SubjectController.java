package space.maxus.dnevnik.controllers.general;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.*;
import space.maxus.dnevnik.auth.Auth;
import space.maxus.dnevnik.controllers.request.create.SubjectCreateRequest;
import space.maxus.dnevnik.controllers.response.QueryResponse;
import space.maxus.dnevnik.controllers.response.ResponseSubject;
import space.maxus.dnevnik.controllers.response.create.SubjectCreationResponse;
import space.maxus.dnevnik.data.model.Subject;
import space.maxus.dnevnik.data.service.SubjectService;

import java.util.List;

@RestController
public class SubjectController {
    private final SubjectService subjectService;

    public SubjectController(SubjectService subjectService) {
        this.subjectService = subjectService;
    }

    @GetMapping("/subjects")
    public QueryResponse<List<ResponseSubject>> subjects(HttpServletRequest request, HttpServletResponse response) {
        return Auth.requireAny(request)
                .map(uid -> QueryResponse.success(subjectService.findAll().stream().map(Subject::response).toList()))
                .orElseGet(() -> Auth.notAuthorized(response));
    }

    @GetMapping("/subjects/{id}")
    public QueryResponse<ResponseSubject> subject(HttpServletRequest request, HttpServletResponse response, @PathVariable("id") Long id) {
        return Auth.requireAny(request)
                .map(uid -> subjectService.findById(id).map(subject -> QueryResponse.success(subject.response())).orElseGet(() -> QueryResponse.failure("Не удалось найти предмет")))
                .orElseGet(() -> Auth.notAuthorized(response));
    }

    @PostMapping("/subjects/create")
    public QueryResponse<SubjectCreationResponse> subject(HttpServletRequest request, HttpServletResponse response, @RequestBody SubjectCreateRequest create) {
        return Auth.requireTeacher(request)
                .map(teacher -> {
                    Subject newSubject = new Subject(create.getName(), List.of(teacher.getId()));
                    subjectService.insertUpdate(newSubject);
                    return QueryResponse.success(new SubjectCreationResponse(newSubject.getId(), newSubject.getName()));
                })
                .orElseGet(() -> Auth.notAuthorized(response));
    }
}
