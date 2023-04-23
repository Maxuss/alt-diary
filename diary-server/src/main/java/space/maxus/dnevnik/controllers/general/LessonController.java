package space.maxus.dnevnik.controllers.general;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import space.maxus.dnevnik.auth.Auth;
import space.maxus.dnevnik.controllers.request.LessonsRequest;
import space.maxus.dnevnik.controllers.response.QueryResponse;
import space.maxus.dnevnik.controllers.response.ResponseDailySchedule;
import space.maxus.dnevnik.data.model.DailySchedule;
import space.maxus.dnevnik.data.model.Group;
import space.maxus.dnevnik.data.model.Schedule;
import space.maxus.dnevnik.data.service.*;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Date;

@RestController
public class LessonController {
    private final GroupService groupService;
    private final DailyScheduleService dailyScheduleService;

    public LessonController(LessonService lessonService, ScheduleService scheduleService, GroupService groupService, SubjectService subjectService, DailyScheduleService dailyScheduleService) {
        this.groupService = groupService;
        this.dailyScheduleService = dailyScheduleService;
    }

    @GetMapping("/lessons")
    public QueryResponse<ResponseDailySchedule> lessons(HttpServletRequest request, HttpServletResponse response, @DateTimeFormat(pattern = "yyyy-MM-dd") @RequestParam("date") Date date) {
        return Auth.require(request)
                .map(student -> {
                    Group group = groupService.findAll().stream().filter(g -> Arrays.stream(g.getStudentsIds()).toList().contains(student.getId())).findFirst().orElseThrow();
                    LocalDate local = LocalDate.from(date.toInstant());
                    int dayOfWeek = local.getDayOfWeek().ordinal();
                    Schedule schedule = group.getSchedules().stream().filter(s -> s.getDayOfWeek() == dayOfWeek).findFirst().orElseThrow();
                    DailySchedule daily = dailyScheduleService.scheduleForDate(schedule, date);
                    return QueryResponse.success(daily.response());
                })
                .orElseGet(() -> Auth.notAuthorized(response));
    }

    @GetMapping("/lessons/by-group")
    public QueryResponse<ResponseDailySchedule> lessons(HttpServletRequest request, HttpServletResponse response, @RequestBody LessonsRequest req) {
        return Auth.requireAny(request)
                .map(student -> {
                    Group group = groupService.findById(req.getGroupId()).orElseThrow();
                    LocalDate local = req.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                    int dayOfWeek = local.getDayOfWeek().ordinal();
                    Schedule schedule = group.getSchedules().stream().filter(s -> s.getDayOfWeek() == dayOfWeek).findFirst().orElseThrow();
                    DailySchedule daily = dailyScheduleService.scheduleForDate(schedule, req.getDate());
                    return QueryResponse.success(daily.response());
                })
                .orElseGet(() -> Auth.notAuthorized(response));
    }
}
