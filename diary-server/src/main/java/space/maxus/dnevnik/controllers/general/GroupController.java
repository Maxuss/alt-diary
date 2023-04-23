package space.maxus.dnevnik.controllers.general;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.*;
import space.maxus.dnevnik.auth.Auth;
import space.maxus.dnevnik.controllers.request.create.GroupAddStudentRequest;
import space.maxus.dnevnik.controllers.request.create.GroupCreateRequest;
import space.maxus.dnevnik.controllers.response.QueryResponse;
import space.maxus.dnevnik.controllers.response.ResponseGroup;
import space.maxus.dnevnik.controllers.response.create.GenericCreationResponse;
import space.maxus.dnevnik.controllers.response.create.GenericPutResponse;
import space.maxus.dnevnik.data.model.Group;
import space.maxus.dnevnik.data.service.GroupService;
import space.maxus.dnevnik.data.service.StudentService;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@RestController
public class GroupController {
    private final GroupService groupService;
    private final StudentService studentService;

    public GroupController(GroupService groupService, StudentService studentService) {
        this.groupService = groupService;
        this.studentService = studentService;
    }

    @GetMapping("/group/{id}")
    public QueryResponse<ResponseGroup> group(
            HttpServletRequest request,
            HttpServletResponse response,
            @PathVariable("id") long id
    ) {
        return Auth.requireAny(request)
                .map(uid -> groupService.findById(id)
                        .map(it -> QueryResponse.success(it.response()))
                        .orElseGet(() -> QueryResponse.failure("Could not find group with id %s".formatted(id)))
                ).orElseGet(() -> Auth.notAuthorized(response));
    }

    @PostMapping("/group/create")
    public QueryResponse<GenericCreationResponse> create(
            HttpServletRequest request,
            HttpServletResponse response,
            @RequestBody GroupCreateRequest create
    ) {
        return Auth.requireTeacher(request)
                .map(teacher -> {
                    Group newGroup = new Group(create.getName(), teacher);
                    newGroup.setStudentsIds(create.getStudents().toArray(new UUID[0]));
                    groupService.insertUpdate(newGroup);
                    return QueryResponse.success(new GenericCreationResponse(newGroup.getId()));
                })
                .orElseGet(() -> Auth.notAuthorized(response));
    }

    @PutMapping("/group/add")
    public QueryResponse<GenericPutResponse<Long>> addStudent(
            HttpServletRequest request,
            HttpServletResponse response,
            @RequestBody GroupAddStudentRequest add
    ) {
        return Auth.requireTeacher(request)
                .map(teacher -> {
                    Group group = groupService.findById(add.getGroupId()).orElseThrow();
                    if (!studentService.getRepository().existsById(add.getStudentId()))
                        return QueryResponse.<GenericPutResponse<Long>>failure("Invalid student ID");
                    List<UUID> studentIds = Arrays.asList(group.getStudentsIds());
                    studentIds.add(add.getStudentId());
                    group.setStudentsIds(studentIds.toArray(new UUID[0]));
                    groupService.insertUpdate(group);
                    return QueryResponse.success(new GenericPutResponse<>(group.getId()));
                })
                .orElseGet(() -> Auth.notAuthorized(response));
    }
}
