package space.maxus.dnevnik.controllers.request.create;

import lombok.Data;

import java.util.UUID;

@Data
public class GroupAddStudentRequest {
    private long groupId;
    private UUID studentId;
}
