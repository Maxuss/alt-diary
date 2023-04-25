package space.maxus.dnevnik.controllers.general;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import space.maxus.dnevnik.auth.Auth;
import space.maxus.dnevnik.controllers.response.AccountInformation;
import space.maxus.dnevnik.controllers.response.QueryResponse;
import space.maxus.dnevnik.data.service.StudentService;
import space.maxus.dnevnik.data.service.TeacherService;

import java.util.NoSuchElementException;
import java.util.UUID;

@RestController
public class AccountController {
    private final StudentService studentService;
    private final TeacherService teacherService;

    public AccountController(StudentService studentService, TeacherService teacherService) {
        this.studentService = studentService;
        this.teacherService = teacherService;
    }

    @GetMapping("/account/foreign/{id}")
    public QueryResponse<AccountInformation> accountInfo(
            HttpServletRequest req,
            HttpServletResponse res,
            @PathVariable("id") UUID id
    ) {
        try {
            return Auth.requireAny(req)
                    .map(uid -> {
                        AccountInformation info = studentService.findById(id)
                                .map(AccountInformation::student)
                                .orElseGet(() -> AccountInformation.teacher(teacherService.findById(id).orElseThrow()));
                        return QueryResponse.success(info);
                    })
                    .orElseGet(() -> Auth.notAuthorized(res));
        } catch (NoSuchElementException e) {
            return QueryResponse.failure("Не удалось найти такого учителя/ученика");
        }
    }

    @GetMapping("/account/self")
    public QueryResponse<AccountInformation> selfAccountInfo(
            HttpServletRequest req,
            HttpServletResponse res
    ) {
        return Auth.require(req)
                .map(student -> QueryResponse.success(AccountInformation.student(student)))
                .orElseGet(() ->
                        Auth.requireTeacher(req)
                                .map(teacher -> QueryResponse.success(AccountInformation.teacher(teacher)))
                                .orElseGet(() -> Auth.notAuthorized(res))
                );
    }
}
