package space.maxus.dnevnik.controllers.general;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.web.bind.annotation.*;
import space.maxus.dnevnik.auth.Auth;
import space.maxus.dnevnik.controllers.request.create.ScheduleCreateRequest;
import space.maxus.dnevnik.controllers.response.QueryResponse;
import space.maxus.dnevnik.controllers.response.ResponseSchedule;
import space.maxus.dnevnik.controllers.response.create.GenericCreationResponse;
import space.maxus.dnevnik.data.model.Schedule;
import space.maxus.dnevnik.data.service.GroupService;
import space.maxus.dnevnik.data.service.ScheduleService;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
public class ScheduleController {
    private final ScheduleService scheduleService;
    private final GroupService groupService;

    public ScheduleController(ScheduleService scheduleService, GroupService groupService) {
        this.scheduleService = scheduleService;
        this.groupService = groupService;
    }

    // Group create
    // => Schedule create
    // => Lessons get
    @GetMapping("/schedule/{id}")
    public QueryResponse<ResponseSchedule> schedule(HttpServletRequest request, HttpServletResponse response, @PathVariable("id") Long id) {
        return Auth.requireAny(request)
                .map(uid -> scheduleService.findById(id).map(it -> QueryResponse.success(it.response())).orElseGet(() -> QueryResponse.failure("Не удалось найти расписание с ID %s".formatted(id))))
                .orElseGet(() -> Auth.notAuthorized(response));
    }

    @PostMapping("/schedule/create")
    @SneakyThrows
    public QueryResponse<GenericCreationResponse> scheduleCreate(HttpServletRequest request, HttpServletResponse response, @RequestBody ScheduleCreateRequest create) {
        return Auth.requireTeacher(request)
                .map(teacher -> {
                    try {
                        Schedule newSchedule = new Schedule(
                                groupService.findById(create.getGroupId()).orElseThrow(),
                                create.getDayOfWeek(),
                                ArrayUtils.toPrimitive(create.getSubjects().toArray(new Long[0])),
                                ArrayUtils.toPrimitive(create.getBreakDurations().toArray(new Long[0])),
                                List.of()
                        );
                        scheduleService.insertUpdate(newSchedule);
                        return QueryResponse.success(new GenericCreationResponse(newSchedule.getId()));
                    } catch (NoSuchElementException e) {
                        return QueryResponse.<GenericCreationResponse>failure("Класс с ID %s не существует".formatted(create.getGroupId()));
                    }
                })
                .orElseGet(() -> Auth.notAuthorized(response));
    }
}
